package uap.workflow.engine.core;


/**
 * ���̶�������pub_wf_def.workflow_type��ö�٣� ���̶�������pub_wf_def.node_type��ö��
 * 
 * <li>ʵ����pub_wf_instance.workflow_type�͹������pub_workflownote.workflow_typeҲ���ø�ö��
 * 
 * @author leijun 2009-7
 * @since 6.0
 */
public enum WorkflowTypeEnum {
	/** �� */
	Package(1),
	/** ������ */
	Approveflow(2),
	/** ���������� */
	SubApproveflow(3),
	/** ������ */
	Workflow(4),
	/** ���������� */
	SubWorkflow(5),
	/** �������������̡�����ʵ������Ϊ���̶���û���������� */
	SubWorkApproveflow(6),
	/** ҵ���� */
	Busiflow(7);

	// ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
	 * 
	 * @param intValue
	 */
	private WorkflowTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public String getStrValue() {
		return String.valueOf(this.intValue);
	}

	public static WorkflowTypeEnum fromStrValue(String strValue) {
		return fromIntValue(Integer.valueOf(strValue));
	}

	public static boolean isMainFlow(int iWfType) {
		if (iWfType == Approveflow.getIntValue()
				|| iWfType == Workflow.getIntValue()
				|| iWfType == Busiflow.getIntValue())
			return true;
		return false;
	}

	public static boolean isSubFlow(int iWfType) {
		if (iWfType == SubApproveflow.getIntValue()
				|| iWfType == SubWorkflow.getIntValue()
				|| iWfType == SubWorkApproveflow.getIntValue())
			return true;
		return false;
	}

	/**
	 * ʵ�������Ƿ�Ϊ������ <li>����������������������������������
	 * 
	 * @param iInstanceType
	 * @return
	 */
	public static boolean isWorkflowInstance(int iInstanceType) {
		if (iInstanceType == SubWorkflow.getIntValue()
				|| iInstanceType == Workflow.getIntValue()
				|| iInstanceType == SubWorkApproveflow.getIntValue())
			return true;
		return false;
	}

	/**
	 * @param iInstanceType
	 * @return
	 */
	public static String getInstanceTypeInSql(int iInstanceType) {
		String str = "";
		if (iInstanceType == SubWorkApproveflow.getIntValue()) {
			str = "(" + SubWorkApproveflow.getIntValue() + ")";
		} else if (isWorkflowInstance(iInstanceType))
			str = "(" + Workflow.getIntValue() + ","
					+ SubWorkflow.getIntValue() + ","
					+ SubWorkApproveflow.getIntValue() + ")";
		else
			str = "(" + Approveflow.getIntValue() + ","
					+ SubApproveflow.getIntValue() + ")";

		return str;
	}

	public static WorkflowTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:���뱣֤��ö��ֵһ��
		case 1:
			return Package;
		case 2:
			return Approveflow;
		case 3:
			return SubApproveflow;
		case 4:
			return Workflow;
		case 5:
			return SubWorkflow;
		case 6:
			return SubWorkApproveflow;
		case 7:
			return Busiflow;
		default:
			break;
		}
		return null;
	}

	public static int getFatherFlowType(int iType) {
		if (iType == SubWorkflow.getIntValue()
				|| iType == SubWorkApproveflow.getIntValue())
			return Workflow.getIntValue();
		if (iType == SubApproveflow.getIntValue())
			return Approveflow.getIntValue();

		return 0;
	}

}
