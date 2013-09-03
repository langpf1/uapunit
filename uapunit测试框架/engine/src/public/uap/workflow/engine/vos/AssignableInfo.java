package uap.workflow.engine.vos;
import java.io.Serializable;
import java.util.Vector;
import nc.vo.uap.pf.OrganizeUnit;
/**
 * 某活动的后继可指派活动的指派信息
 * 
 * @author wzhy 2005-2-3
 * @modifier leijun 2006-5-23
 * @modifier leijun 2008-10 可指派活动的相关信息增加checkResultCriterion
 */
public class AssignableInfo implements Serializable {
	private static final long serialVersionUID = 4133123187564240043L;
	/**
	 * 路径上的所有转移的审批结果条件都为"通过"或无
	 */
	public static String CRITERION_PASS = "Y";
	/**
	 * 路径上的所有转移的审批结果条件都为"不通过"或无
	 */
	public static String CRITERION_NOPASS = "N";
	/**
	 * 路径上所有转移的审批结果条件都为无
	 */
	public static String CRITERION_NOTGIVEN = "NotGiven";
	/**
	 * 路径上所有转移的审批结果条件存在"通过"和"不通过"，即永不可达(无效)
	 */
	public static String CRITERION_INVALID = "N/A";
	/** 活动的指派结果,即操作员PK数组 */
	private Vector<String> assignedOperatorPKs;
	/** 活动参与者拥有的所有操作员PK数组 */
	private Vector<String> operatorPKs;
	/** 活动参与者拥有的所有操作员数组 */
	private Vector<OrganizeUnit> ouUsers;
	/** 活动的指派结果,即操作员数组 */
	private Vector<OrganizeUnit> ouAssignedUsers;
	/** 指派描述,即描述这个指派点的意义,一般为指派活动的名称 */
	private String desc;
	/** 指派对象,即审批活动的guid */
	private String activityDefId;
	/** 指派对象,即审批活动所属流程定义的PK */
	private String processDefPK;
	/** 当前活动与可指派活动 路由上所经历转移的合并审批结果判定 */
	private String checkResultCriterion;
	public AssignableInfo() {
		super();
	}
	public String getCheckResultCriterion() {
		return checkResultCriterion;
	}
	public void setCheckResultCriterion(String checkResultCriterion) {
		this.checkResultCriterion = checkResultCriterion;
	}
	/**
	 * 活动的指派结果,即操作员PK数组
	 * 
	 * @return
	 */
	public Vector<String> getAssignedOperatorPKs() {
		if (assignedOperatorPKs == null) {
			// 初始化数组
			assignedOperatorPKs = new Vector<String>();
		}
		return assignedOperatorPKs;
	}
	/**
	 * 活动参与者拥有的所有操作员PK数组
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector<String> getOperatorPKs() {
		if (operatorPKs == null) {
			// 初始化数组
			operatorPKs = new Vector();
		}
		return operatorPKs;
	}
	/**
	 * 活动的指派结果,即操作员数组
	 * 
	 * @return
	 */
	public Vector<OrganizeUnit> getOuAssignedUsers() {
		if (ouAssignedUsers == null) {
			// 初始化数组
			ouAssignedUsers = new Vector<OrganizeUnit>();
		}
		return ouAssignedUsers;
	}
	/**
	 * 活动参与者拥有的所有操作员数组
	 * 
	 * @return
	 */
	public Vector<OrganizeUnit> getOuUsers() {
		if (ouUsers == null) {
			// 初始化数组
			ouUsers = new Vector<OrganizeUnit>();
		}
		return ouUsers;
	}
	public String getDesc() {
		return desc;
	}
	/**
	 * 获得被指派活动的GUID
	 * 
	 * @return
	 */
	public String getActivityDefId() {
		return activityDefId;
	}
	/**
	 * 获得被指派活动所属流程定义的PK
	 * 
	 * @return
	 */
	public String getProcessDefPK() {
		return processDefPK;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 设置被指派活动的GUID
	 * 
	 * @param target
	 */
	public void setActivityDefId(String target) {
		this.activityDefId = target;
	}
	/**
	 * 设置被指派活动所属流程定义的PK
	 */
	public void setProcessDefPK(String target) {
		this.processDefPK = target;
	}
}