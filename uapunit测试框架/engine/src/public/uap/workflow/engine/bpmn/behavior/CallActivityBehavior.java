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
package uap.workflow.engine.bpmn.behavior;

import java.util.ArrayList;
import java.util.List;
import nc.vo.pub.AggregatedValueObject;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.bpmn.data.AbstractDataAssociation;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.session.WorkflowContext;
/**
 * Implementation of the BPMN 2.0 call activity (limited currently to calling a
 * subprocess and not (yet) a global task).
 * 
 * @author Joram Barrez
 */
public class CallActivityBehavior extends AbstractBpmnActivityBehavior implements uap.workflow.engine.pvm.behavior.SubProcessActivityBehavior {
	public static final String APP_FORMINFO = "APP_FORMINFO";
	protected String processDefinitonKey;
	private List<AbstractDataAssociation> dataInputAssociations = new ArrayList<AbstractDataAssociation>();
	private List<AbstractDataAssociation> dataOutputAssociations = new ArrayList<AbstractDataAssociation>();
	private Expression processDefinitionExpression;
	public CallActivityBehavior(String processDefinitionKey) {
		this.processDefinitonKey = processDefinitionKey;
	}
	public CallActivityBehavior(Expression processDefinitionExpression) {
		super();
		this.processDefinitionExpression = processDefinitionExpression;
	}
	public void addDataInputAssociation(AbstractDataAssociation dataInputAssociation) {
		this.dataInputAssociations.add(dataInputAssociation);
	}
	public void addDataOutputAssociation(AbstractDataAssociation dataOutputAssociation) {
		this.dataOutputAssociations.add(dataOutputAssociation);
	}
	public void execute(IActivityInstance execution) throws Exception {
		if ((processDefinitonKey == null) && (processDefinitionExpression != null)) {
			processDefinitonKey = (String) processDefinitionExpression.getValue(execution);
		}

		IProcessInstance subProcessInstance = createSubProcessInstance(execution, processDefinitonKey);

		// copy process variables
		for (AbstractDataAssociation dataInputAssociation : dataInputAssociations) {
			Object value = null;
			if (dataInputAssociation.getSourceExpression() != null) {
				
				value = dataInputAssociation.getSourceExpression().getValue(execution);
			} else {
				value = execution.getVariable(dataInputAssociation.getSource());
			}
			subProcessInstance.setVariable(dataInputAssociation.getTarget(), value);
		}

		buildContext(execution, subProcessInstance);
		
		subProcessInstance.start();
//		 IProcessDefinition processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache()
//				.getProDefByProDefPk(processDefinitonKey);
//		// IProcessInstance subProcessInstance = createSubProcessInstance(processDefinition, execution, formInfo);
//		// copy process variables
//		for (AbstractDataAssociation dataInputAssociation : dataInputAssociations) {
//			Object value = null;
//			if (dataInputAssociation.getSourceExpression() != null) {
//				value = dataInputAssociation.getSourceExpression().getValue(execution);
//			} else {
//				value = execution.getVariable(dataInputAssociation.getSource());
//			}
//			subProcessInstance.setVariable(dataInputAssociation.getTarget(), value);
//		}
	}
	public void completing(IActivityInstance execution, IActivityInstance subProcessInstance) throws Exception {
		// only data. no control flow available on this execution.
		// copy process variables
		for (AbstractDataAssociation dataOutputAssociation : dataOutputAssociations) {
			Object value = null;
			if (dataOutputAssociation.getSourceExpression() != null) {
				value = dataOutputAssociation.getSourceExpression().getValue(subProcessInstance);
			} else {
				value = subProcessInstance.getVariable(dataOutputAssociation.getSource());
			}
			execution.setVariable(dataOutputAssociation.getTarget(), value);
		}
	}
	public void completed(IActivityInstance execution) throws Exception {
		leave(execution);
	}
	@Override
	public void lastExecutionEnded(IActivityInstance execution) {
		
	}
	
	public void buildContext(IActivityInstance execution, IProcessInstance subProcessInstance)throws Exception{
		
		IBusinessKey bizObjRef = WorkflowContext.getCurrentBpmnSession().getBusinessObject();

		
		 //2013.1.21 by zhailzh 因为initializeData 返回为String，不能够转化为Object，返回为null而报错。而暂时改动 
		
		/*
		Object[] bizObjects = (Object[])subProcessInstance.getVariable("initializeData");
		if (bizObjects != null && bizObjects.length > 0){
			if(bizObjects[0] instanceof AggregatedValueObject){
				AggregatedValueObject bizObject = (AggregatedValueObject)bizObjects[0];
				bizObjRef.setBillId(bizObject.getParentVO().getPrimaryKey());
				//bizObjRef.setBillVersionPK(bizObject.getParentVO().getPrimaryKey());
				bizObjRef.setBillType(bizObject.getParentVO().getClass().getName());
				//bizObjRef.setBillVos(bizObjects);
				subProcessInstance.setPk_form_ins(bizObject.getParentVO().getPrimaryKey());
				subProcessInstance.setPk_form_ins_version(bizObject.getParentVO().getPrimaryKey());
			}
		}else
			throw new Exception("不能得到初始化数据");
		  */
		Object bizObjects = subProcessInstance.getVariable("initializeData");
		if (bizObjects != null){
			if(bizObjects instanceof AggregatedValueObject){
				AggregatedValueObject bizObject = (AggregatedValueObject)bizObjects;
				bizObjRef.setBillId(bizObject.getParentVO().getPrimaryKey());
				//bizObjRef.setBillVersionPK(bizObject.getParentVO().getPrimaryKey());
				bizObjRef.setBillType(bizObject.getParentVO().getClass().getName());
				//bizObjRef.setBillVos(bizObjects);
				subProcessInstance.setPk_form_ins(bizObject.getParentVO().getPrimaryKey());
				subProcessInstance.setPk_form_ins_version(bizObject.getParentVO().getPrimaryKey());
			}
		}else{
			//原来的情况，默认如果得不到初始的数据，直接的抛错，现在是把现下单据的信息赋予子流程
			subProcessInstance.setPk_form_ins(bizObjRef.getBillId());
			subProcessInstance.setPk_form_ins_version(bizObjRef.getBillId());
		}
		subProcessInstance.setPk_form_ins(bizObjRef.getBillId());
		subProcessInstance.setPk_form_ins_version(bizObjRef.getBillId());
		subProcessInstance.setVariable(CallActivityBehavior.APP_FORMINFO, bizObjects);
		((ProcessInstanceEntity) subProcessInstance).asyn();
	}
	
	public IProcessInstance createSubProcessInstance(IActivityInstance execution, String procDefID){
	    
		IProcessDefinition processDefinition = 
			Context.getProcessEngineConfiguration().getDeploymentCache().getProDefByProDefId(procDefID);

		IProcessInstance subProcessInstance = null;// execution.createSubProcessInstance(processDefinition);

		subProcessInstance = processDefinition.createProcessInstance();

		// subProcessInstance.executions = new ArrayList<ExecutionEntity>();

		// manage bidirectional super-subprocess relation
		subProcessInstance.setSuperActivityInstance(execution);
		// this.setSubProcessInstance(subProcessInstance);

		// Initialize the new execution
		subProcessInstance.setProcessDefinition((IProcessDefinition) processDefinition);
		subProcessInstance.setParentProcessInstance(execution.getProcessInstance());

//		CommandContext commandContext = Context.getCommandContext();
//		int historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
//		if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_ACTIVITY) {
//			DbSqlSession dbSqlSession = commandContext.getSession(DbSqlSession.class);
//			HistoricProcessInstanceEntity historicProcessInstance = new HistoricProcessInstanceEntity(
//					(ExecutionEntity) subProcessInstance);
//			dbSqlSession.insert(historicProcessInstance);
//		}

		return subProcessInstance;
	}
	
	/**
	 * creates a new sub process instance. The current execution will be the
	 * super execution of the created execution.
	 * 
	 * @param processDefinition
	 *            The {@link IProcessDefinition} of the subprocess.
	 */
//	public IProcessInstance createSubProcessInstance(IProcessDefinition processDefinition,
//			IActivityInstance superActivityInstance, IBusinessObject formInfo) {
//		IProcessInstance parentProcessInstance = superActivityInstance.getProcessInstance();
//		FlowInfoCtx oldFlowInfo = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
//		List<UserTaskRunTimeCtx> runTimeCtx = new ArrayList<UserTaskRunTimeCtx>();
//
//		UserTaskRunTimeCtx nextInfo = new UserTaskRunTimeCtx();
//		nextInfo.setActivityId(processDefinition.getInitial().getId());
//		nextInfo.setUserPks(new String[] { oldFlowInfo.getUserPk() });
//		nextInfo.setSequential(false);
//		runTimeCtx.add(nextInfo);
//		CommitProInsCtx flowInfo = new CommitProInsCtx();
//		flowInfo.setProDefPk(processDefinition.getProDefPk());
//		flowInfo.setUserPk(oldFlowInfo.getUserPk());
//		flowInfo.setNextInfo(runTimeCtx.toArray(new UserTaskRunTimeCtx[0]));
//		flowInfo.setMakeBill(false);
//		//FormInfoCtx formInfo = WorkflowContext.getCurrentBpmnSession().getFormInfoCtx();
//		IFlowRequest request = BizProcessServer.createFlowRequest(formInfo, flowInfo);
//		IFlowResponse respone = BizProcessServer.createFlowResponse();
//		BizProcessServer.exec(request, respone);
//
//		if (respone.getProcessInstance() != null) {
//			IProcessInstance processInstance = respone.getProcessInstance();
//			processInstance.setSuperActivityInstance(superActivityInstance);
//			processInstance.setParentProcessInstance(parentProcessInstance);
//			processInstance.asyn();
//			respone.getProcessInstance().setVariable(this.APP_FORMINFO, formInfo);
//		}
//		return null;
//	}
}
