package uap.workflow.engine.entity;
import java.util.List;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.jobexecutor.AsyncContinuationJobHandler;
import uap.workflow.engine.pvm.runtime.AtomicOperation;
import uap.workflow.engine.utils.ActivityInstanceUtil;
import uap.workflow.engine.utils.VariableInstanceUtil;
import uap.workflow.engine.vos.ProcessInstanceVO;
public class ProcessInstanceEntity extends VariableScopeImpl implements IProcessInstance {
	private static final long serialVersionUID = 2121948154555442881L;
	private String pk_proins;
	private ProcessInstanceStatus state_proins;
	private IProcessDefinition processDefinition;
	private IProcessInstance parentProcessInstance;
	private IProcessInstance rootProcessInstance;
	private IActivityInstance superActivityInstance;
	private ITask startTask;
	private IActivity startActiviti;
	private UFDateTime startdatetime;
	private UFDateTime enddatetime;
	private UFDateTime duedatetime;
	private String pk_group;
	private String pk_org;
	private String title;
	private String pk_starter;//流程实例启动人员
	private String pk_business;
	private String pk_form_ins;
	private String pk_bizobject;// 抽象于NC的单据类型
	private String pk_biztrans;// 抽象于NC的交易类型
	private String pk_form_ins_version;
	private ProcessInstanceVO proInsVo;
	@Override
	public boolean isEnded() {
		return false;
	}
	@Override
	public boolean isSuspended() {
		return false;
	}
	public String getPk_proins() {
		return pk_proins;
	}
	public void setPk_proins(String pk_proins) {
		this.pk_proins = pk_proins;
	}
	public ProcessInstanceStatus getState_proins() {
		if (proInsVo != null && state_proins == null) {
			state_proins = ProcessInstanceStatus.fromIntValue(proInsVo.getState_proins());
		}
		return state_proins;
	}
	public void setState_proins(ProcessInstanceStatus state_proins) {
		this.state_proins = state_proins;
	}
	public ITask getStartTask() {
		return startTask;
	}
	public void setStartTask(ITask startTask) {
		this.startTask = startTask;
	}
	public String getPk_starter() {
		if (pk_starter == null && proInsVo != null) {
			pk_starter = proInsVo.getPk_starter();
		}
		return pk_starter;
	}
	public void setPk_starter(String pk_starter) {
		this.pk_starter = pk_starter;
	}
	public String getPk_business() {
		return pk_business;
	}
	public void setPk_business(String pk_business) {
		this.pk_business = pk_business;
	}
	public UFDateTime getStartdatetime() {
		return startdatetime;
	}
	public void setStartdatetime(UFDateTime startdatetime) {
		this.startdatetime = startdatetime;
	}
	public UFDateTime getEnddatetime() {
		return enddatetime;
	}
	public void setEnddatetime(UFDateTime enddatetime) {
		this.enddatetime = enddatetime;
	}
	public UFDateTime getDuedatetime() {
		return duedatetime;
	}
	public void setDuedatetime(UFDateTime duedatetime) {
		this.duedatetime = duedatetime;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_org() {
		if (pk_org == null && proInsVo != null) {
			pk_org = proInsVo.getPk_org();
		}
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getTitle() {
		if (title == null && proInsVo != null) {
			title = proInsVo.getTitle();
		}
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public IProcessInstance getParentProcessInstance() {
		return parentProcessInstance;
	}
	public void setParentProcessInstance(IProcessInstance parentProcessInstance) {
		this.parentProcessInstance = parentProcessInstance;
	}
	public void setRootProcessInstance(IProcessInstance rootProcessInstance) {
		this.rootProcessInstance = rootProcessInstance;
	}

	public IActivityInstance getSuperActivityInstance() {
		return superActivityInstance;
	}
	public void setSuperActivityInstance(IActivityInstance superActivityInstance) {
		this.superActivityInstance = superActivityInstance;
	}
	/**
	 * 执行实例启动操作
	 */
	public void start() {
		ActivityInstanceEntity startActivitiInstance = new ActivityInstanceEntity();
		startActivitiInstance.setProcessInstance(this);
		startActivitiInstance.setActivity(this.getStartActiviti());
		startActivitiInstance.setStatus(ActivityInstanceStatus.Finished);
		if(this.getParentProcessInstance() != null){//如果流程实例的父流程实例不为空，说明此流程实例属于被调用的流程实例
			startActivitiInstance.setSuperExecution(this.getSuperActivityInstance());
		}
		startActivitiInstance.initialize();
		startActivitiInstance.asyn();
		if (AtomicOperation.PROCESS_START.isAsync(startActivitiInstance)) {
			scheduleAtomicOperationAsync(AtomicOperation.PROCESS_START, startActivitiInstance);
		} else {
			performOperationSync(AtomicOperation.PROCESS_START, startActivitiInstance);
		}
	}
	protected void performOperationSync(AtomicOperation executionOperation, IActivityInstance actIns) {
		Context.getCommandContext().performOperation(executionOperation, actIns);
	}
	protected void scheduleAtomicOperationAsync(AtomicOperation executionOperation, IActivityInstance actIns) {
		MessageEntity message = new MessageEntity();
		message.setExecution(actIns);
		message.setExclusive(getStartActiviti().isExclusive());
		message.setJobHandlerType(AsyncContinuationJobHandler.TYPE);
		Context.getCommandContext().getJobManager().send(message);
	}
	@Override
	public void deleteCascade(String deleteReason) {}
	public IProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
	public void setProcessDefinition(IProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	protected List<VariableInstanceEntity> loadVariableInstances() {
		return VariableInstanceUtil.getVarInsByProInsPk(this.getPk_proins());
	}

	protected VariableScopeImpl getParentVariableScope() {
		return null;
	}
	public void setRootProcessInstance(ProcessInstanceEntity rootProcessInstance) {
		this.rootProcessInstance = rootProcessInstance;
	}
	protected void initializeVariableInstanceBackPointer(VariableInstanceEntity variableInstance) 
	{
		variableInstance.setProcessInstanceId(this.pk_proins);
	}
	public IProcessInstance getRootProcessInstance() {
		return rootProcessInstance;
	}
	public IActivity getStartActiviti() {
		if (startActiviti == null) {
			startActiviti = this.getProcessDefinition().getInitial();
		}
		return startActiviti;
	}
	public void setStartActiviti(IActivity startActiviti) {
		this.startActiviti = startActiviti;
	}
	public ProcessInstanceVO getProInsVo() {
		return proInsVo;
	}
	public void setProInsVo(ProcessInstanceVO proInsVo) {
		this.proInsVo = proInsVo;
	}
	@Override
	public void initialize() {}
	public String getPk_form_ins() {
		if (pk_form_ins == null && proInsVo != null) {
			pk_form_ins = proInsVo.getPk_form_ins();
		}
		return pk_form_ins;
	}
	public void setPk_form_ins(String pk_form_ins) {
		this.pk_form_ins = pk_form_ins;
	}
	public String getPk_form_ins_version() {
		if (pk_form_ins_version == null && proInsVo != null) {
			pk_form_ins_version = proInsVo.getPk_form_ins_version();
		}
		return pk_form_ins_version;
	}
	public void setPk_form_ins_version(String pk_form_ins_version) {
		this.pk_form_ins_version = pk_form_ins_version;
	}
	@Override
	public String getProInsPk() {
		return this.pk_proins;
	}
	public String getPk_bizobject() {
		if (pk_bizobject == null && proInsVo != null) {
			pk_bizobject = proInsVo.getPk_bizobject();
		}
		return pk_bizobject;
	}
	public void setPk_bizobject(String pk_bizobject) {
		this.pk_bizobject = pk_bizobject;
	}
	public String getPk_biztrans() {
		if (pk_biztrans == null && proInsVo != null) {
			pk_biztrans = proInsVo.getPk_biztrans();
		}
		return pk_biztrans;
	}
	public void setPk_biztrans(String pk_biztrans) {
		this.pk_biztrans = pk_biztrans;
	}
	@Override
	public IActivityInstance[] getExecutions() {
		return ActivityInstanceUtil.getActInsByProInsPk(this.getProInsPk());
	}
}
