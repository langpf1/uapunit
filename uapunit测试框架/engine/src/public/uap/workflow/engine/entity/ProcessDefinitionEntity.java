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
package uap.workflow.engine.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.vo.pub.lang.UFDateTime;

import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.form.StartFormHandler;
import uap.workflow.engine.pvm.process.ScopeImpl;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.task.TaskDefinition;
/**
 * @author Tom Baeyens
 * @目标：流程定义实体
 */
public class ProcessDefinitionEntity extends ScopeImpl implements IProcessDefinition {
	private static final long serialVersionUID = 1L;
	protected String deploymentId;
	protected String proDefId;
	protected String proDefPk;
	protected String pk_group;
	protected String category;
	protected String name;
	protected String description;
	protected String resourceName;
	private byte[] resourceBytes;
	private String diagramName;
	private byte[] diagramBytes;
	protected int version;
	private String pk_bizobject;// 抽象于NC的单据类型
	private String pk_biztrans;// 抽象于NC的交易类型
	protected boolean hasStartFormKey;
	protected int suspensionState = SuspensionState.ACTIVE.getStateCode();
	protected IActivity initial;
	protected StartFormHandler startFormHandler;
	protected boolean isPublic;
	protected int validity=0;
	protected Map<String, TaskDefinition> taskDefinitions = new HashMap<String, TaskDefinition>();
	protected Map<IActivity, List<IActivity>> initialActivityStacks = new HashMap<IActivity, List<IActivity>>();//初始化  活动    栈
	public ProcessDefinitionEntity(String id) {
		super(id, null);
		this.proDefId = id;
		processDefinition = this;
	}
	public List<IActivity> getInitialActivityStack() {
		return getInitialActivityStack(initial);
	}
	public synchronized List<IActivity> getInitialActivityStack(IActivity startActivity) {
		List<IActivity> initialActivityStack = initialActivityStacks.get(startActivity);
		if (initialActivityStack == null) {
			initialActivityStack = new ArrayList<IActivity>();
			IActivity activity = startActivity;
			while (activity != null) {
				initialActivityStack.add(0, activity);
				activity = (IActivity) activity.getParentActivity();
			}
			initialActivityStacks.put(startActivity, initialActivityStack);
		}
		return initialActivityStack;
	}
	protected IProcessInstance newProcessInstance(IActivity startActivity) {
		ProcessInstanceEntity proIns = new ProcessInstanceEntity();
		proIns.setStartdatetime(new UFDateTime());
		proIns.setState_proins(ProcessInstanceStatus.Started);
		proIns.setStartActiviti(startActivity);
		proIns.setProcessDefinition(this);
		proIns.setPk_business(WorkflowContext.getCurrentBpmnSession().getBusinessPk());
		proIns.setPk_starter(WorkflowContext.getCurrentBpmnSession().getCntUserPk());
		proIns.asyn();
		return proIns;
	}
	public String getDeploymentId() {
		return this.deploymentId;
	}
	public IActivity getInitial() {
		if (initial == null) {
			initial = (IActivity) this.getProperty(BpmnParse.PROPERTYNAME_INITIAL);
		}
		return initial;
	}
	public void setInitial(IActivity initial) {
		this.initial = initial;
	}
	public String toString() {
		return "ProcessDefinition(" + id + ")";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return (String) getProperty("documentation");
	}
	@Override
	public String getProDefId() {
		return this.proDefId;
	}
	@Override
	public String getProDefPk() {
		return this.proDefPk;
	}
	@Override
	public String getCategory() {
		return null;
	}
	@Override
	public int getVersion() {
		return this.version;
	}
	@Override
	public String getResourceName() {
		return this.resourceName;
	}
	@Override
	public boolean isSuspended() {
		return false;
	}
	public Map<IActivity, List<IActivity>> getInitialActivityStacks() {
		return initialActivityStacks;
	}
	public void setInitialActivityStacks(Map<IActivity, List<IActivity>> initialActivityStacks) {
		this.initialActivityStacks = initialActivityStacks;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public void setProDefPk(String proDefPk) {
		this.proDefPk = proDefPk;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	@Override
	public String setProDefId(String proDefId) {
		return this.proDefId = proDefId;
	}
	@Override
	public Map<String, TaskDefinition> getTaskDefinitions() {
		return taskDefinitions;
	}
	@Override
	public StartFormHandler getStartFormHandler() {
		return this.startFormHandler;
	}
	@Override
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public void setTaskDefinitions(Map<String, TaskDefinition> taskDefinitions) {
		this.taskDefinitions = taskDefinitions;
	}
	@Override
	public int getSuspensionState() {
		return 0;
	}
	@Override
	public void setStartFormHandler(StartFormHandler startFormHandler) {
		this.startFormHandler = startFormHandler;
	}
	@Override
	public void setStartFormKey(boolean hasStartFormKey) {
		this.hasStartFormKey = hasStartFormKey;
	}
	@Override
	public void setSuspensionState(int suspensionState) {
		this.suspensionState = suspensionState;
	}
	public void setResourceBytes(byte[] resourceBytes) {
		this.resourceBytes = resourceBytes;
	}
	public IProcessInstance createProcessInstance() {
		return this.createProcessInstanceForInitial(initial);
	}
	public IProcessInstance createProcessInstance(IActivity startActivity) {
		return this.createProcessInstanceForInitial(startActivity);
	}
	/** creates a process instance using the provided activity as initial */
	public IProcessInstance createProcessInstanceForInitial(IActivity initial) {
		if (initial == null) {
			initial = this.getInitial();
		}
		if (initial == null) {
			throw new WorkflowException("Cannot start process instance, initial is null");
		}
		IProcessInstance processInstance = newProcessInstance(initial);
		processInstance.initialize();//空的实现
		return processInstance;
	}
	@Override
	public String getDiagramName() {
		return this.diagramName;
	}
	@Override
	public void setDiagramName(String diagramName) {
		this.diagramName = diagramName;
	}
	@Override
	public void setDiagramBytes(byte[] bytes) {
		this.diagramBytes = bytes;
	}
	@Override
	public byte[] getDiagramBytes() {
		return this.diagramBytes;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	@Override
	public byte[] getResourceBytes() {
		return this.resourceBytes;
	}
	@Override
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
		// TODO Auto-generated method stub
	}
	@Override
	public String getPk_group() {
		return this.pk_group;
	}
	public String getPk_bizobject() {
		return pk_bizobject;
	}
	public void setPk_bizobject(String pk_bizobject) {
		this.pk_bizobject = pk_bizobject;
	}
	public String getPk_biztrans() {
		return pk_biztrans;
	}
	public void setPk_biztrans(String pk_biztrans) {
		this.pk_biztrans = pk_biztrans;
	}
	public int getValidity() {
		return validity;
	}
	public void setValidity(int validity) {
		this.validity = validity;
	}
}
