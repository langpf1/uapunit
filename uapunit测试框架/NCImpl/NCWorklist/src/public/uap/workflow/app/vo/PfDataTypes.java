package uap.workflow.app.vo;

import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ����ƽ̨ʹ�õ���һЩ�������ͣ�������
 * 
 * @author leijun 2006-8-2
 * @modifier leijun 2009-5 ��������������
 */
public class PfDataTypes {
	// 0 �ַ���
	public static final int DT_STRING_INT = 0;

	// 1 ˫����
	public static final int DT_UFDOUBLE_INT = 1;

	// 2 ������
	public static final int DT_UFDATE_INT = 2;

	// 3 ����
	public static final int DT_INTEGER_INT = 3;

	// 4 ʱ���
	public static final int DT_UFDATETIME_INT = 4;

	// 5 ������
	public static final int DT_UFBOOLEAN_INT = 5;

	// 6 ����
	public static final int DT_VOID_INT = 6;

	// 7 ������
	public static final int DT_ARRAY_INT = 7;

	// 8 �Ի�����
	public static final int DT_DLG_INT = 8;

	// 9 �����
	public static final int DT_PANEL_INT = 9;

	// 10 ��������
	public static final int DT_PKREF_INT = 10;

	// 11 �������
	public static final int DT_CODEREF_INT = 11;

	// 12 ���Ʋ���
	public static final int DT_NAMEREF_INT = 12;
	
	// 13 ��������
	public static final int DT_COMBOBOX_INT = 13;
	
	// 14 Ԫ����
	public static final int DT_METADATA_INT = 14;
	
	// 15 ϵͳ����
	public static final int DT_SYSPARAM_INT = 15;
	
	public static final String[] TAGS = { "STRING", "DOUBLE", "UFDATE", "INTEGER", "UFDATETIME",
			"BOOLEAN", "VOID", "ARRAYLIST", "DLG", "PANEL", "PKREF", "CODEREF", "NAMEREF", "COMBOBOX", "METADATA", "SYSPARAM" };

	public static final PfDataTypes STRING = new PfDataTypes(DT_STRING_INT);

	public static final PfDataTypes UFDOUBLE = new PfDataTypes(DT_UFDOUBLE_INT);

	public static final PfDataTypes UFDATE = new PfDataTypes(DT_UFDATE_INT);

	public static final PfDataTypes INTEGER = new PfDataTypes(DT_INTEGER_INT);

	public static final PfDataTypes UFDATETIME = new PfDataTypes(DT_UFDATETIME_INT);

	public static final PfDataTypes UFBOOLEAN = new PfDataTypes(DT_UFBOOLEAN_INT);

	public static final PfDataTypes VOID = new PfDataTypes(DT_VOID_INT);

	public static final PfDataTypes ARRAY = new PfDataTypes(DT_ARRAY_INT);

	public static final PfDataTypes DIALOG = new PfDataTypes(DT_DLG_INT);

	public static final PfDataTypes PANEL = new PfDataTypes(DT_PANEL_INT);

	public static final PfDataTypes PKREF = new PfDataTypes(DT_PKREF_INT);

	public static final PfDataTypes CODEREF = new PfDataTypes(DT_CODEREF_INT);

	public static final PfDataTypes NAMEREF = new PfDataTypes(DT_NAMEREF_INT);
	
	public static final PfDataTypes COMBOBOX = new PfDataTypes(DT_COMBOBOX_INT);
	
	public static final PfDataTypes METADATA = new PfDataTypes(DT_METADATA_INT);
	
	public static final PfDataTypes SYSPARAM = new PfDataTypes(DT_SYSPARAM_INT);

	private static final HashMap<String, PfDataTypes> tagMap = new HashMap<String, PfDataTypes>();

	private final int _value;

	private static final PfDataTypes[] VALUES = { STRING, UFDOUBLE, UFDATE, INTEGER, UFDATETIME,
			UFBOOLEAN, VOID, ARRAY, DIALOG, PANEL, PKREF, CODEREF, NAMEREF, COMBOBOX, METADATA, SYSPARAM };

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	private PfDataTypes(int value) {
		_value = value;
	}

	/**
	 * ������TAG��Ӧ����������
	 * 
	 * @param tag
	 * @return
	 */
	public static PfDataTypes fetchTypeByTag(String tag) {
		PfDataTypes type = (PfDataTypes) tagMap.get(tag);
		if (type == null && tag != null)
			throw new IllegalArgumentException(tag);
		return type;
	}

	public int getValue() {
		return _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// FIXME::i18n
		switch (_value) {
			case DT_ARRAY_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfDataTypes-000000")/*���鼯��*/;
			case DT_DLG_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfDataTypes-000001")/*�Ի���*/;
			case DT_INTEGER_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("102220", "UPP102220-000199")/* @res "����" */;
			case DT_PANEL_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfDataTypes-000002")/*���*/;
			case DT_STRING_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("102220", "UPP102220-000196")/* @res "�ַ���" */;
			case DT_UFBOOLEAN_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("102220", "UPP102220-000201") /* @res "������" */;
			case DT_UFDATE_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("102220", "UPP102220-000198")/* @res "������" */;
			case DT_UFDATETIME_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("102220", "UPP102220-000200")/*@res "ʱ�����" */;
			case DT_UFDOUBLE_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("102220", "UPP102220-000197")/*@res "������" */;
			case DT_VOID_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfDataTypes-000003")/*��*/;
			case DT_PKREF_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000618")/*@res "��������"*/;
			case DT_CODEREF_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000619")/*@res "�������"*/;
			case DT_NAMEREF_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000620") /*@res "���Ʋ���"*/;
			case DT_COMBOBOX_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfDataTypes-000004")/*������*/;
			case DT_METADATA_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfDataTypes-000005")/*Ԫ����*/;
			case DT_SYSPARAM_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfDataTypes-000006")/*ϵͳ����*/;
			default:
				return "ERROR";
		}

	}

	public String getTag() {
		return TAGS[_value];
	}

	/**
	 * �����intֵ��Ӧ��һ����������
	 * 
	 * @param iType
	 * @return DataTypes
	 */
	public static PfDataTypes resolveType(int iType) {
		if (iType < 0 || iType > VALUES.length - 1)
			throw new IllegalArgumentException();
		return VALUES[iType];
	}

}
