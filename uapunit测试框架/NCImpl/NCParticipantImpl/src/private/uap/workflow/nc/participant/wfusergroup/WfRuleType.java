package uap.workflow.nc.participant.wfusergroup;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 流程规则类型的枚举
 * 
 * @author yuyonga
 * @modifier zhouzhenga 新增规则函数类型
 */
public enum WfRuleType {
	SuperiorType(0), // 组织.上级主管
	SuperintendType(1), // 组织.主管
	HigherUpType(2), // 组织.上级负责人
	PrincipalType(3), // 组织.负责人
	DirectEmployeeType(4), // 组织.直属员工
	AllEmployeeType(5), // 组织.所有员工
	EmployerType(6); // 人员.直接上级

	// 枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
	 * 
	 * @param intValue
	 */
	private WfRuleType(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public static WfRuleType fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:必须保证与枚举值一致
		case 0:
			return SuperiorType;
		case 1:
			return SuperintendType;
		case 2:
			return HigherUpType;
		case 3:
			return PrincipalType;
		case 4:
			return DirectEmployeeType;
		case 5:
			return AllEmployeeType;
		case 6:
			return EmployerType;
		default:
			break;
		}
		return null;
	}

	public String toString() {
		switch (getIntValue()) {
		// XXX:必须保证与枚举值一致
		case 0:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000000")/* 组织.上级主管 */;
		case 1:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000001")/* 组织.主管 */;
		case 2:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000002")/* 组织.上级负责人 */;
		case 3:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000003")/* 组织.负责人 */;
		case 4:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000004")/* 组织.直属员工 */;
		case 5:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000005")/* 组织.所有员工 */;
		case 6:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000006")/* 人员.直接上级 */;
		default:
			break;
		}
		return "UnknownStatus";
	}
}
