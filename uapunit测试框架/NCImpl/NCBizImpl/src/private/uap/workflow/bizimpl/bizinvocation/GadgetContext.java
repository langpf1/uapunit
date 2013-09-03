package uap.workflow.bizimpl.bizinvocation;

import uap.workflow.bizitf.bizinvocation.WorkflowgadgetVO;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.extend.gadget.IGadgetContext;

public class GadgetContext implements IGadgetContext {
	/** 流程实例状态 */
	private ProcessInstanceStatus processStatus;
	private ActivityInstanceStatus taskStatus;

	private boolean isAutoActivity = false;
	
	/** 单据实体，可能为聚合VO */
	private Object billEntity;

	// 工作流交互的变量值 */
	//private HashMap hmVariable;

	/** 当前活动关联的工作流组件，及其实参 */
	private WorkflowgadgetVO gadgetVO;

	public GadgetContext(){
		
	}
	public GadgetContext(ProcessInstanceStatus procStatus, ActivityInstanceStatus taskStatus, 
			Object bizObject, WorkflowgadgetVO gadgetVO){
		this.processStatus = procStatus;
		this.taskStatus = taskStatus;
		this.billEntity = bizObject;
		this.gadgetVO = gadgetVO;
	}
	
	public ProcessInstanceStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(ProcessInstanceStatus processStatus) {
		this.processStatus = processStatus;
	}

	public ActivityInstanceStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(ActivityInstanceStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	public Object getBillEntity() {
		return billEntity;
	}

	public void setBillEntity(Object billEntity) {
		this.billEntity = billEntity;
	}

	public void setGadgetVO(WorkflowgadgetVO gadgetVO) {
		this.gadgetVO = gadgetVO;
	}

	public WorkflowgadgetVO getGadgetVO() {
		return gadgetVO;
	}

	public boolean isAutoActivity() {
		return isAutoActivity;
	}

	public void setAutoActivity(boolean isAutoActivity) {
		this.isAutoActivity = isAutoActivity;
	}

	@Override
	public void setGadgetVO(Object gadgetVO) {
		billEntity = gadgetVO;
	}

}
