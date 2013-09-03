/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.bpmn.deployer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nc.bs.framework.common.InvocationInfoProxy;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.bpmn.parser.BpmnParser;
import uap.workflow.engine.bpmn.parser.MessageEventDefinition;
import uap.workflow.engine.cfg.IdGenerator;
import uap.workflow.engine.cmd.DeleteJobsCmd;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.deploy.Deployer;
import uap.workflow.engine.deploy.DeploymentCache;
import uap.workflow.engine.el.ExpressionManager;
import uap.workflow.engine.entity.DeploymentEntity;
import uap.workflow.engine.entity.EventSubscriptionEntity;
import uap.workflow.engine.entity.MessageEventSubscriptionEntity;
import uap.workflow.engine.entity.ProcessDefinitionEntity;
import uap.workflow.engine.entity.ResourceEntity;
import uap.workflow.engine.entity.TimerEntity;
import uap.workflow.engine.event.MessageEventHandler;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.io.IoUtil;
import uap.workflow.engine.jobexecutor.Job;
import uap.workflow.engine.jobexecutor.TimerDeclarationImpl;
import uap.workflow.engine.jobexecutor.TimerStartEventJobHandler;
import uap.workflow.engine.mgr.ProcessDefinitionManager;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class BpmnDeployer implements Deployer {
	public static final String BPMN_RESOURCE_SUFFIX = "bpmn20.xml";
	public static final String[] DIAGRAM_SUFFIXES = new String[] { "png", "jpg", "gif", "svg" };
	protected ExpressionManager expressionManager;
	protected BpmnParser bpmnParser;
	protected IdGenerator idGenerator;
	public void deploy(DeploymentEntity deployment) {
		List<IProcessDefinition> processDefinitions = new ArrayList<IProcessDefinition>();
		Map<String, ResourceEntity> resources = deployment.getResources();
		for (String resourceName : resources.keySet()) {
			if (resourceName.endsWith(BPMN_RESOURCE_SUFFIX)) {
				ResourceEntity resource = resources.get(resourceName);
				byte[] bytes = resource.getBytes();
				BpmnParse bpmnParse = bpmnParser.createParse().sourceInputStream(new ByteArrayInputStream(bytes)).deployment(deployment).name(resourceName);
				if (!deployment.isValidatingSchema()) {
					bpmnParse.setSchemaResource(null);
				}
				bpmnParse.execute();
				for (IProcessDefinition processDefinition : bpmnParse.getProcessDefinitions()) {
					processDefinition.setResourceName(resourceName);
					processDefinition.setResourceBytes(bytes);
					processDefinition.setPublic(true);
					String diagramResourceName = getDiagramResourceForProcess(resourceName, processDefinition.getProDefPk(), resources);
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					if (diagramResourceName != null) {
						InputStream inputStream = classLoader.getResourceAsStream(diagramResourceName);
						if (inputStream != null) {
							bytes = IoUtil.readInputStream(inputStream, null);
						}
					}
					processDefinition.setDiagramBytes(bytes);
					processDefinitions.add(processDefinition);
				}
			}
		}
		CommandContext commandContext = Context.getCommandContext();
		ProcessDefinitionManager processDefinitionManager = commandContext.getProcessDefinitionManager();
		DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
		for (IProcessDefinition processDefinition : processDefinitions) {
			if (deployment.isNew()) {
				int processDefinitionVersion = 0;
				IProcessDefinition latestProcessDefinition =processDefinitions.get(0);
				if (latestProcessDefinition != null) {
					processDefinitionVersion = latestProcessDefinition.getVersion() + 1;
				} else {
					processDefinitionVersion = 1;
				}
				processDefinition.setVersion(processDefinitionVersion);
//				processDefinition.setDeploymentId(deployment.getId());
//				String processDefinitionId = processDefinition.getProDefId();
//				processDefinition.setProDefId(processDefinitionId);
				processDefinition.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
				// removeObsoleteTimers(processDefinition);
				// addTimerDeclarations(processDefinition);
				// removeObsoleteMessageEventSubscriptions(processDefinition,
				// latestProcessDefinition);
				// addMessageEventSubscriptions(processDefinition);
				((ProcessDefinitionEntity) processDefinition).asyn();
				deploymentCache.addProcessDefinition(processDefinition);
			} else {
				String deploymentId = deployment.getId();
				processDefinition.setDeploymentId(deploymentId);
				IProcessDefinition persistedProcessDefinition = processDefinitionManager.findProcessDefinitionByDeploymentAndKey(deploymentId, processDefinition.getProDefPk());
				processDefinition.setProDefId(persistedProcessDefinition.getId());
				processDefinition.setVersion(persistedProcessDefinition.getVersion());
				deploymentCache.addProcessDefinition(processDefinition);
			}
			//Context.getProcessEngineConfiguration().getDeploymentCache().addProcessDefinition(processDefinition);
		}
	}
	@SuppressWarnings("unchecked")
	protected void addTimerDeclarations(IProcessDefinition processDefinition) {
		List<TimerDeclarationImpl> timerDeclarations = (List<TimerDeclarationImpl>) processDefinition.getProperty(BpmnParse.PROPERTYNAME_START_TIMER);
		if (timerDeclarations != null) {
			for (TimerDeclarationImpl timerDeclaration : timerDeclarations) {
				TimerEntity timer = timerDeclaration.prepareTimerEntity(null);
				Context.getCommandContext().getJobManager().schedule(timer);
			}
		}
	}
	protected void removeObsoleteTimers(IProcessDefinition processDefinition) {
		List<Job> jobsToDelete = Context.getCommandContext().getJobManager().findJobsByConfiguration(TimerStartEventJobHandler.TYPE, processDefinition.getProDefId());
		for (Job job : jobsToDelete) {
			new DeleteJobsCmd(job.getId()).execute(Context.getCommandContext());
		}
	}
	protected void removeObsoleteMessageEventSubscriptions(IProcessDefinition processDefinition, IProcessDefinition latestProcessDefinition) {
		// remove all subscriptions for the previous version
		if (latestProcessDefinition != null) {
			CommandContext commandContext = Context.getCommandContext();
			List<EventSubscriptionEntity> subscriptionsToDelete = commandContext.getEventSubscriptionManager().findEventSubscriptionsByConfiguration(MessageEventHandler.TYPE,
					latestProcessDefinition.getId());
			for (EventSubscriptionEntity eventSubscriptionEntity : subscriptionsToDelete) {
				eventSubscriptionEntity.delete();
			}
		}
	}
	@SuppressWarnings("unchecked")
	protected void addMessageEventSubscriptions(IProcessDefinition processDefinition) {
		CommandContext commandContext = Context.getCommandContext();
		List<MessageEventDefinition> messageEventDefinitions = (List<MessageEventDefinition>) processDefinition.getProperty(BpmnParse.PROPERTYNAME_MESSAGE_EVENT_DEFINITIONS);
		if (messageEventDefinitions != null) {
			for (MessageEventDefinition messageEventDefinition : messageEventDefinitions) {
				if (messageEventDefinition.isStartEvent()) {
					// look for subscriptions for the same name in db:
					List<EventSubscriptionEntity> subscriptionsForSameMessageName = commandContext.getEventSubscriptionManager().findEventSubscriptionByName(MessageEventHandler.TYPE,
							messageEventDefinition.getName());
					// also look for subscriptions created in the session:
					List<MessageEventSubscriptionEntity> cachedSubscriptions = commandContext.getDbSqlSession().findInCache(MessageEventSubscriptionEntity.class);
					for (MessageEventSubscriptionEntity cachedSubscription : cachedSubscriptions) {
						if (messageEventDefinition.getName().equals(cachedSubscription.getEventName()) && !subscriptionsForSameMessageName.contains(cachedSubscription)) {
							subscriptionsForSameMessageName.add(cachedSubscription);
						}
					}
					// remove subscriptions deleted in the same command
					subscriptionsForSameMessageName = commandContext.getDbSqlSession().pruneDeletedEntities(subscriptionsForSameMessageName);
					if (!subscriptionsForSameMessageName.isEmpty()) {
						throw new WorkflowException("Cannot deploy process definition '" + processDefinition.getResourceName()
								+ "': there already is a message event subscription for the message with name '" + messageEventDefinition.getName() + "'.");
					}
					MessageEventSubscriptionEntity newSubscription = new MessageEventSubscriptionEntity();
					newSubscription.setEventName(messageEventDefinition.getName());
					newSubscription.setActivityId(messageEventDefinition.getActivityId());
					newSubscription.setConfiguration(processDefinition.getId());
					newSubscription.insert();
				}
			}
		}
	}
	/**
	 * Returns the default name of the image resource for a certain process.
	 * 
	 * It will first look for an image resource which matches the process
	 * specifically, before resorting to an image resource which matches the
	 * BPMN 2.0 xml file resource.
	 * 
	 * Example: if the deployment contains a BPMN 2.0 xml resource called
	 * 'abc.bpmn20.xml' containing only one process with key 'myProcess', then
	 * this method will look for an image resources called 'abc.myProcess.png'
	 * (or .jpg, or .gif, etc.) or 'abc.png' if the previous one wasn't found.
	 * 
	 * Example 2: if the deployment contains a BPMN 2.0 xml resource called
	 * 'abc.bpmn20.xml' containing three processes (with keys a, b and c), then
	 * this method will first look for an image resource called 'abc.a.png'
	 * before looking for 'abc.png' (likewise for b and c). Note that if
	 * abc.a.png, abc.b.png and abc.c.png don't exist, all processes will have
	 * the same image: abc.png.
	 * 
	 * @return null if no matching image resource is found.
	 */
	protected String getDiagramResourceForProcess(String bpmnFileResource, String processKey, Map<String, ResourceEntity> resources) {
		for (String diagramSuffix : DIAGRAM_SUFFIXES) {
			String diagramForBpmnFileResource = getBpmnFileImageResourceName(bpmnFileResource, diagramSuffix);
			String processDiagramResource = getProcessImageResourceName(bpmnFileResource, processKey, diagramSuffix);
			if (resources.containsKey(processDiagramResource)) {
				return processDiagramResource;
			} else if (resources.containsKey(diagramForBpmnFileResource)) {
				return diagramForBpmnFileResource;
			}
		}
		return null;
	}
	protected String getBpmnFileImageResourceName(String bpmnFileResource, String diagramSuffix) {
		String bpmnFileResourceBase = bpmnFileResource.substring(0, bpmnFileResource.length() - 10); // minus
		// 10
		// to
		// remove
		// 'bpmn20.xml'
		return bpmnFileResourceBase + diagramSuffix;
	}
	protected String getProcessImageResourceName(String bpmnFileResource, String processKey, String diagramSuffix) {
		String bpmnFileResourceBase = bpmnFileResource.substring(0, bpmnFileResource.length() - 10); // minus
		// 10
		// to
		// remove
		// 'bpmn20.xml'
		return bpmnFileResourceBase + processKey + "." + diagramSuffix;
	}
	protected void createResource(String name, byte[] bytes, DeploymentEntity deploymentEntity) {
		ResourceEntity resource = new ResourceEntity();
		resource.setName(name);
		resource.setBytes(bytes);
		resource.setDeploymentId(deploymentEntity.getId());
		// Mark the resource as 'generated'
		resource.setGenerated(true);
		Context.getCommandContext().getDbSqlSession().insert(resource);
	}
	public ExpressionManager getExpressionManager() {
		return expressionManager;
	}
	public void setExpressionManager(ExpressionManager expressionManager) {
		this.expressionManager = expressionManager;
	}
	public BpmnParser getBpmnParser() {
		return bpmnParser;
	}
	public void setBpmnParser(BpmnParser bpmnParser) {
		this.bpmnParser = bpmnParser;
	}
	public IdGenerator getIdGenerator() {
		return idGenerator;
	}
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
