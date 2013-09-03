package uap.workflow.app.extend.gadget;

import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.TaskInstanceStatus;


/**
 * ��������������������� 
 * 
 * @author leijun 2008-8
 * @since 5.5
 */
public class WfGadgetContext {
	/** ����ʵ��״̬ */
	private ProcessInstanceStatus processStatus;
	private TaskInstanceStatus taskStatus;

	private boolean isAutoActivity = false;
	
	/** ����ʵ�壬����Ϊ�ۺ�VO */
	private Object billEntity;

	// �����������ı���ֵ */
	//private HashMap hmVariable;

	/** ��ǰ������Ĺ��������������ʵ�� */
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
