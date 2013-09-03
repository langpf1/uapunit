package uap.workflow.app.action;

/**
 * 对流程平台中进行调用的单据动作编码 进行常量定义
 * 
 */
public interface IPFActionName {
	/**
	 * 审批流审批
	 */
	public static final String APPROVE = "APPROVE";

	/**
	 * 审批流弃审
	 */
	public static final String UNAPPROVE = "UNAPPROVE";

	/**
	 * 删除
	 */
	public static final String DEL_DELETE = "DELETE";

	/**
	 * 废弃
	 */
	public static final String DEL_DISCARD = "DISCARD";

	/**
	 * 失效或移除
	 */
	public static final String DEL_SOBLANKOUT = "BLANKOUT";

	/**
	 * 提交审批流
	 */
	public static final String SAVE = "SAVE";
	
	/**
	 * 收回审批流
	 */
	public static final String UNSAVE = "UNSAVE";
	
	/**
	 * 取消提交工作流
	 */
	public static final String RECALL = "RECALL";

	/**
	 * 编辑或修改（也可触发审批流）
	 */
	public static final String EDIT = "EDIT";

	/**
	 * 保存
	 */
	public static final String WRITE = "WRITE";

	/**
	 * 启动工作流
	 */
	public static final String START = "START";

	/**
	 * 执行工作流
	 */
	public static final String SIGNAL = "SIGNAL";

	/**
	 * 回退工作流
	 */
	public static final String ROLLBACK = "ROLLBACK";
	
	/**
	 * 终止工作流
	 */
	public static final String TERMINATE = "TERMINATE";
}
