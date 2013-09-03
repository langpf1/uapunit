package uap.workflow.nc.participant.wfusergroup;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 流程用户组类型的枚举
 * 
 * @author yuyonga
 */
public enum WfGroupType {

	DisperseType(0), FormulaType(1);

	// 枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
	 * 
	 * @param intValue
	 */
	private WfGroupType(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public static WfGroupType fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:必须保证与枚举值一致
		case 0:
			return DisperseType;
		case 1:
			return FormulaType;
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
					"WfGroupType-000000")/* 离散用户 */;
		case 1:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfGroupType-000001")/* 规则用户 */;
		default:
			break;
		}
		return "UnknownStatus";
	}
}
