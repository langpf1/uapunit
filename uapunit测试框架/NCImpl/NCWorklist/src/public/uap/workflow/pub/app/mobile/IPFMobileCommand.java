package uap.workflow.pub.app.mobile;

/**
 * 流程平台支持的短信指令
 * 
 * @author leijun 2006-3-9
 */
public interface IPFMobileCommand {
	/**
	 * 审批
	 */
	String APPROVE = "SP";

	/**
	 * 弃审
	 */
	String UNAPPROVE = "QS";

	/**
	 * 查询未处理工作项
	 */
	String QUERY_WORKITEM_TODO = "CN";

	/**
	 * 查询已处理工作项
	 */
	String QUERY_WORKITEM_DONE = "CD";
}
