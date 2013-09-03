package uap.workflow.engine.core;


/**
 * 流程定义类型pub_wf_def.workflow_type的枚举； 流程定义类型pub_wf_def.node_type的枚举
 * 
 * <li>实例表pub_wf_instance.workflow_type和工作项表pub_workflownote.workflow_type也引用该枚举
 * 
 * @author leijun 2009-7
 * @since 6.0
 */
public enum WorkflowTypeEnum {
	/** 包 */
	Package(1),
	/** 审批流 */
	Approveflow(2),
	/** 审批子流程 */
	SubApproveflow(3),
	/** 工作流 */
	Workflow(4),
	/** 工作子流程 */
	SubWorkflow(5),
	/** 工作审批子流程。用于实例，因为流程定义没有这种类型 */
	SubWorkApproveflow(6),
	/** 业务流 */
	Busiflow(7);

	// 枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
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
	 * 实例类型是否为工作流 <li>包括工作流、工作子流、工作审批子流
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
		// XXX:必须保证与枚举值一致
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
