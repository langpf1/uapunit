package uap.workflow.pub.app.notice;

import java.io.Serializable;
import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ��Ϣ��������Ϣ���������
 * @author �׾� 2005-4-13
 * @modifier leijun 2008-7 ����һ����Ϣ���ͣ�ҵ����Ϣ����ֱ�Ӵ򿪵���UI��
 */
public class PfPluginTypes   implements Serializable{
	public static final int NOTE_INT = 0;

	public static final int MAIL_INT = 1;

	public static final int SMS_INT = 2;

	public static final int BIZ_INT = 3;
	
	public static final int MSG_INT =4;

	public static final PfPluginTypes NOTE = new PfPluginTypes(NOTE_INT);

	public static final PfPluginTypes MAIL = new PfPluginTypes(MAIL_INT);

	public static final PfPluginTypes SMS = new PfPluginTypes(SMS_INT);

	public static final PfPluginTypes BIZ = new PfPluginTypes(BIZ_INT);
	
	public static final PfPluginTypes MSG = new PfPluginTypes(MSG_INT);
	
	private static final String[] TAGS = { "NOTE", "MAIL", "SMS", "BIZ","MSG" };

//	private static final String[] OLD_TAGS = { "ʵʱ��Ϣ", "�ʼ�", "����Ϣ", "ҵ����Ϣ","֪ͨ��Ϣ" };

	public static final PfPluginTypes[] VALUES = { NOTE, MAIL, SMS, BIZ ,MSG};

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	public static PfPluginTypes fromString(String tag) {
		PfPluginTypes style = (PfPluginTypes) tagMap.get(tag);
		if (style == null && tag != null)
			throw new IllegalArgumentException(tag);
		return style;
	}

	private PfPluginTypes(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

	/**
	 * ��ñ��ֵ
	 */
	public String getTag() {
		return TAGS[_value];
	}

	/**
	 * ��NC30�Ĳ������ת��ΪNC31�ĸ�ʽ
	 * <li>�÷�����NC31�Ժ󽫷���
	 * @param oldTag
	 * @return
	 */
//	public static String oldTagToNew(String oldTag) {
//		for (int i = 0; i < OLD_TAGS.length; i++) {
//			if (OLD_TAGS[i].equals(oldTag))
//				return TAGS[i];
//		}
//		return null;
//	}

	/**
	 * �����ʾ����
	 */
	public String toString() {
		switch (_value) {
			case NOTE_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000187")/*@res "ʵʱ��Ϣ"*/;
			case MAIL_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000185")/*@res "�ʼ�"*/;
			case SMS_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000186")/*@res "����Ϣ"*/;
			case BIZ_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000201")/*@res "ҵ����Ϣ"*/;
			case MSG_INT:
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000402")/*֪ͨ��Ϣ*/;
			default:
				return "ERROR";
		}
	}

}