package uap.workflow.app.vo;

/**
 * 流程常量？
 * 
 * @author 雷军 2004-10-27
 * @modifier 雷军 2005-1-20 增加审批处理意见常量枚举
 * FIXME：废弃？
 */
public interface IApproveflowConst {

	/** 当前登陆集团 */
	public static final String LOGIN_GROUP_PK = "LOGINGROUP";
	/**当前单据类型编码*/
	public static final String CURRENT_BILLTYPE_PK = "CURRENT_BILLTYPE";
	/**当前单据类型pk*/
	public static final String CURRENT_BILLTYPE_ID="CURRENT_BILLTYPE_PK";

	/** 制单人信息 */
	public static final String BILLMAKER = "BILLMAKER";

	public static final String BILLMAKER_NAME = "BILLMAKER_NAME";

	public static final String BILLMAKER_TYPE = "BILLMAKER_TYPE";

	/** 审批通过/不通过的固定条件表达式 */
	public static final String CHECK_PASS = "ACT_Check_Result.equals(\"Y\")";

	public static final String CHECK_NOPASS = "ACT_Check_Result.equals(\"N\")";

	/** 审批流程，记录审批结果的变量名称 */
	public static final String ACT_CHECK_RESULT = "ACT_Check_Result";

	public static final String CHECK_RESULT = "Check_Result";

	public static int DEFAULT_APPROVE_HEIGHT = 51;

	public static int DEFAULT_APPROVE_WIDTH = 51;

	/** 审批处理意见 */
	public static final int CHECK_RESULT_PASS = 0;

	public static final int CHECK_RESULT_NOPASS = 1;

	public static final int CHECK_RESULT_REJECT_LAST = 2;

	public static final int CHECK_RESULT_REJECT_FIRST = 3;
	
	public static final String CURRENT_APPLICATIONARGNAME = "CURRENT_APPLICATIONARGNAME";

	/**流程限定**/
	//	public final static String sameCorpPattern = "@corp@";
	//	public final static String sameDeptPattern = "@dept@";
	//	public final static String sameDeptAndCorpPattern = "@deptcorp@";
}