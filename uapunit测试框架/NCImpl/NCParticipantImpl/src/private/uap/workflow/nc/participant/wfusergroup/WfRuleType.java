package uap.workflow.nc.participant.wfusergroup;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ���̹������͵�ö��
 * 
 * @author yuyonga
 * @modifier zhouzhenga ��������������
 */
public enum WfRuleType {
	SuperiorType(0), // ��֯.�ϼ�����
	SuperintendType(1), // ��֯.����
	HigherUpType(2), // ��֯.�ϼ�������
	PrincipalType(3), // ��֯.������
	DirectEmployeeType(4), // ��֯.ֱ��Ա��
	AllEmployeeType(5), // ��֯.����Ա��
	EmployerType(6); // ��Ա.ֱ���ϼ�

	// ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
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
		// XXX:���뱣֤��ö��ֵһ��
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
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000000")/* ��֯.�ϼ����� */;
		case 1:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000001")/* ��֯.���� */;
		case 2:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000002")/* ��֯.�ϼ������� */;
		case 3:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000003")/* ��֯.������ */;
		case 4:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000004")/* ��֯.ֱ��Ա�� */;
		case 5:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000005")/* ��֯.����Ա�� */;
		case 6:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"WfRuleType-000006")/* ��Ա.ֱ���ϼ� */;
		default:
			break;
		}
		return "UnknownStatus";
	}
}
