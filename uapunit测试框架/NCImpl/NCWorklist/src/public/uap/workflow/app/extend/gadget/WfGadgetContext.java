package uap.workflow.app.extend.gadget;

import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.TaskInstanceStatus;


/**
 * 工作流组件的运行上下文 
 * 
 * @author leijun 2008-8
 * @since 5.5
 */
public class WfGadgetContext {
	/** 流程实例状态 */
	private ProcessInstanceStatus processStatus;
	private TaskInstanceStatus taskStatus;

	private boolean isAutoActivity = false;
	
	/** 单据实体，可能为聚合VO */
	private Object billEntity;

	// 工作流交互的变量值 */
	//private HashMap hmVariable;

	/** 当前活动关联的工作流组件，及其实参 */
	private WorkflowgadgetVO gadgetVO;

	public ProcessInstanceStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(ProcessInstanceStatus processStatus) {
		this.processStatus = processStatus;
	}

	public TaskInstanceStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskInstanceStatus taskStatus) {
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


}
