package uap.workflow.app.vo;

/**
 * 对平台中配置信息进行常量定义
 * @author fangj 2005-1-21
 * @modifier leijun 2006-4-19 组相关 改为 角色相关
 */
public interface IPFConfigInfo {
	//系统固定的业务类型
	public static final String STATEBUSINESSTYPE = "KHHH0000000000000001";
	

	//	/*通用单据类型 后缀名*/
	//	public static final String  BILLTYPE_COMMON = "-COMMON";
	//	
	//	public static final String  BILLTYPE_COMMON_NAME = "-COMMON";
	
	//当前工作流节点
	public static final String CurrentWorkFlow = "CurrentWorkFlow";
	
	public static final String FUNCTION_OUT = "FunctionOutObject";

	//流程平台类的编译原代码目录
	public final static String ActionDir = "nc/bs/pub/action/";

	//编译后类所在位置
	public final static String ActionPack = "nc.bs.pub.action";

	/**
	 * 动作驱动/约束-操作员无关
	 */
	public static final int UserNoRelation = 1;

	/**
	 * 动作驱动/约束-操作员相关
	 */
	public static final int UserRelation = 2;

	/**
	 * 动作驱动/约束-组相关--->>
	 * 动作驱动/约束-角色相关
	 */
	public static final int RoleRelation = 3;
}