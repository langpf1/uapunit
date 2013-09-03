package uap.workflow.nc.participant.wfusergroup;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * �����û������͵�ö��
 * 
 * @author yuyonga
 */
public enum WfGroupType {

	DisperseType(0), FormulaType(1);

	// ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
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
		// XXX:���뱣֤��ö��ֵһ��
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
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfGroupType-000000")/* ��ɢ�û� */;
		case 1:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfGroupType-000001")/* �����û� */;
		default:
			break;
		}
		return "UnknownStatus";
	}
}
