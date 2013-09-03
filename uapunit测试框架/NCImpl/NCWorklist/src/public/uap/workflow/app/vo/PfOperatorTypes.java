package uap.workflow.app.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ����ƽ̨ʹ�õ���һЩ������������� <li>
 * �Ƚ��������"<",">","==","<=",">=","!=","like","in","not in","gin" <li>
 * �߼��������"&&","||","!" <li>�����������"+","-","*","/","%","^"
 * 
 * @author leijun 2006-8-2
 * @modifier leijun 2007-12 ���ӱȽ������"into"
 * @modifier leijun 2008-12 ���ӱȽ������"not like"
 */
public class PfOperatorTypes {
	public static final int OP_GT_INT = 0;

	public static final int OP_LT_INT = 1;

	public static final int OP_EQ_INT = 2;

	public static final int OP_LE_INT = 3;

	public static final int OP_GE_INT = 4;

	public static final int OP_NE_INT = 5;

	public static final int OP_LIKE_INT = 6;

	public static final int OP_AND_INT = 7;

	public static final int OP_OR_INT = 8;

	public static final int OP_NOT_INT = 9;

	public static final int OP_PLUS_INT = 10;

	public static final int OP_SUBTRACT_INT = 11;

	public static final int OP_UMINUS_INT = 12;

	public static final int OP_MUL_INT = 13;

	public static final int OP_DIV_INT = 14;

	public static final int OP_MOD_INT = 15;

	public static final int OP_POWER_INT = 16;

	public static final int OP_IN_INT = 17;

	public static final int OP_NOTIN_INT = 18;

	public static final int OP_INTO_INT = 19;

	public static final int OP_NOTLIKE_INT = 20;

	public static final String[] TAGS = { ">", "<", "==", "<=", ">=", "!=",
			"like", "&&", "||", "!", "+", "-", "-", "*", "/", "%", "^", "in",
			"not in", "into", "not like" };

	public static final String[] CODES = { "GT", "LT", "EQ", "LE", "GE", "NE",
			"LIKE", "AND", "OR", "NOT", "PLUS", "SUBTRACT", "UMINUS", "MUL",
			"DIV", "MOD", "POWER", "IN", "NOTIN", "GIN", "NOTLIKE" };

	public static final PfOperatorTypes GT = new PfOperatorTypes(OP_GT_INT);

	public static final PfOperatorTypes LT = new PfOperatorTypes(OP_LT_INT);

	public static final PfOperatorTypes EQ = new PfOperatorTypes(OP_EQ_INT);

	public static final PfOperatorTypes LE = new PfOperatorTypes(OP_LE_INT);

	public static final PfOperatorTypes GE = new PfOperatorTypes(OP_GE_INT);

	public static final PfOperatorTypes NE = new PfOperatorTypes(OP_NE_INT);

	public static final PfOperatorTypes LIKE = new PfOperatorTypes(OP_LIKE_INT);

	public static final PfOperatorTypes AND = new PfOperatorTypes(OP_AND_INT);

	public static final PfOperatorTypes OR = new PfOperatorTypes(OP_OR_INT);

	public static final PfOperatorTypes NOT = new PfOperatorTypes(OP_NOT_INT);

	public static final PfOperatorTypes PLUS = new PfOperatorTypes(OP_PLUS_INT);

	public static final PfOperatorTypes SUBTRACT = new PfOperatorTypes(
			OP_SUBTRACT_INT);

	public static final PfOperatorTypes UMINUS = new PfOperatorTypes(
			OP_UMINUS_INT);

	public static final PfOperatorTypes MUL = new PfOperatorTypes(OP_MUL_INT);

	public static final PfOperatorTypes DIV = new PfOperatorTypes(OP_DIV_INT);

	public static final PfOperatorTypes MOD = new PfOperatorTypes(OP_MOD_INT);

	public static final PfOperatorTypes POWER = new PfOperatorTypes(
			OP_POWER_INT);

	public static final PfOperatorTypes IN = new PfOperatorTypes(OP_IN_INT);

	public static final PfOperatorTypes NOTIN = new PfOperatorTypes(
			OP_NOTIN_INT);

	public static final PfOperatorTypes INTO = new PfOperatorTypes(OP_INTO_INT);

	public static final PfOperatorTypes NOTLIKE = new PfOperatorTypes(
			OP_NOTLIKE_INT);

	private static final HashMap<String, PfOperatorTypes> tagMap = new HashMap<String, PfOperatorTypes>();

	private final int _value;

	private static final PfOperatorTypes[] VALUES = { GT, LT, EQ, LE, GE, NE,
			LIKE, AND, OR, NOT, PLUS, SUBTRACT, UMINUS, MUL, DIV, MOD, POWER,
			IN, NOTIN, INTO, NOTLIKE };

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	private PfOperatorTypes(int value) {
		_value = value;
	}

	/**
	 * ������TAG��Ӧ�������
	 * 
	 * @param tag
	 * @return
	 */
	public static PfOperatorTypes fetchTypeByTag(String tag) {
		PfOperatorTypes type = (PfOperatorTypes) tagMap.get(tag);
		if (type == null && tag != null)
			throw new IllegalArgumentException(tag);
		return type;
	}

	/**
	 * ������TAG��Ӧ����ʾ����(i18n)
	 * 
	 * @param tag
	 * @return
	 */
	public static String getShowNameByTag(String tag) {
		return fetchTypeByTag(tag).showName();
	}

	public int getValue() {
		return _value;
	}

	public String toString() {
		return TAGS[_value];
	}

	/**
	 * ���ظ����������ʾ����(i18n)
	 * 
	 * @return
	 */
	public String showName() {
		// FIXME::i18n
		switch (_value) {
		case OP_GT_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000000")/* ���� */;
		case OP_LT_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000001")/* С�� */;
		case OP_EQ_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000002")/* ���� */;
		case OP_GE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000003")/* ���ڵ��� */;
		case OP_LE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000004")/* С�ڵ��� */;
		case OP_NE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000005")/* ������ */;
		case OP_LIKE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000006")/* ���� */;
		case OP_AND_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000007")/* ���� */;
		case OP_OR_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000008")/* ���� */;
		case OP_NOT_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000009")/* �� */;
		case OP_PLUS_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000010")/* �� */;
		case OP_SUBTRACT_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000011")/* �� */;
		case OP_UMINUS_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000012")/* �� */;
		case OP_MUL_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000013")/* �� */;
		case OP_DIV_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000014")/* �� */;
		case OP_MOD_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000015")/* �� */;
		case OP_POWER_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000016")/* �� */;
		case OP_IN_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000017")/* ���� */;
		case OP_NOTIN_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000018")/* ������ */;
		case OP_INTO_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000019")/* �������� */;
		case OP_NOTLIKE_INT:
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1",
					"PfOperatorTypes-000020")/* ������ */;
		default:
			return "ERROR";
		}
	}

	/**
	 * �����intֵ��Ӧ��һ�������
	 * 
	 * @param iType
	 * @return OperatorTypes
	 */
	public static PfOperatorTypes resolveType(int iType) {
		if (iType < 0 || iType > VALUES.length - 1)
			throw new IllegalArgumentException();
		return VALUES[iType];
	}
}
