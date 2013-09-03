package uap.workflow.bizitf.bizinvocation;

import nc.vo.wfengine.pub.WfTaskOrInstanceStatus;

/**
 * ��������������������� 
 * 
 * @author leijun 2008-8
 * @since 5.5
 */
public class WfGadgetContext {
	/** ����ʵ��״̬ */
	private WfTaskOrInstanceStatus processStatus;
	private WfTaskOrInstanceStatus taskStatus;

	private boolean isAutoActivity = false;
	
	/** ����ʵ�壬����Ϊ�ۺ�VO */
	private Object billEntity;

	// �����������ı���ֵ */
	//private HashMap hmVariable;

	/** ��ǰ������Ĺ��������������ʵ�� */
	private WorkflowgadgetVO gadgetVO;

	public WfTaskOrInstanceStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(WfTaskOrInstanceStatus processStatus) {
		this.processStatus = processStatus;
	}

	public WfTaskOrInstanceStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(WfTaskOrInstanceStatus taskStatus) {
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
