package uap.workflow.app.vo;

/**
 * 单据的审批状态 列表
 * 
 * @author 樊冠军 2002-10-16
 * @modifier leijun 2008-12 统一单据的审批状态
 */
public interface IPfRetCheckInfo {
	/*  审批流内部使用的单据状态  */
	/**
	 * 自由态
	 */
	public static final int NOSTATE = -1;

	/**
	 * 未通过 态
	 */
	public static final int NOPASS = 0;

	/**
	 * 通过 态
	 */
	public static final int PASSING = 1;

	/**
	 * 进行中 态
	 */
	public static final int GOINGON = 2;

	/**
	 * 提交 态
	 */
	public static final int COMMIT = 3;

}
