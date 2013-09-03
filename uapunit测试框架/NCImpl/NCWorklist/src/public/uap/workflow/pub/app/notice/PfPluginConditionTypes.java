package uap.workflow.pub.app.notice;

import java.io.Serializable;
import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ��Ϣ�����еķ�������
 * @author �׾� created on 2005-4-13
 */
public class PfPluginConditionTypes  implements Serializable{
  public static final int NONE_INT = 0;
  public static final int CHECKPASS_INT = 1;
  public static final int NOPASS_INT = 2;
  public static final int REJECT_INT =3;
  

  public static final PfPluginConditionTypes NONE = new PfPluginConditionTypes(NONE_INT);
  public static final PfPluginConditionTypes CHECKPASS = new PfPluginConditionTypes(CHECKPASS_INT);
  public static final PfPluginConditionTypes NOPASS = new PfPluginConditionTypes(NOPASS_INT);
  public static final PfPluginConditionTypes REJECT = new PfPluginConditionTypes(REJECT_INT);

  private static final String[] TAGS = { "NONE", "CHECKPASS", "NOPASS","REJECT"};
  private static final String[] CONDITION_XPRESSIONS = { "true", "CurrentWorkFlow.getIsCheckPass()==true",
      "CurrentWorkFlow.getIsCheckPass()==false" };

  public static final PfPluginConditionTypes[] VALUES = { NONE, CHECKPASS, NOPASS,REJECT};

  private static final HashMap tagMap = new HashMap();

  public final int _value;

  static {
    for (int i = 0; i < TAGS.length; i++) {
      tagMap.put(TAGS[i], VALUES[i]);
    }
  }

  public static PfPluginConditionTypes fromString(String tag) {
    PfPluginConditionTypes style = (PfPluginConditionTypes) tagMap.get(tag);
    if (style == null && tag != null)
      throw new IllegalArgumentException(tag);
    return style;
  }

  protected PfPluginConditionTypes(int value) {
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

  public String getXpression() {
    return CONDITION_XPRESSIONS[_value];
  }

  /**
   * ��NC30�Ĳ������������ת��ΪNC31�ĸ�ʽ
   * <li>�÷�����NC31�Ժ󽫷���
   * @param oldTag
   * @return
   */
  public static String oldTagToNew(String oldTag) {
    for (int i = 0; i < CONDITION_XPRESSIONS.length; i++) {
      if (CONDITION_XPRESSIONS[i].equals(oldTag))
        return TAGS[i];
    }
    return null;
  }

  /**
   * �����ʾ����
   */
  public String toString() {
    switch (_value) {
      case NONE_INT:
        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000188")/*@res "������"*/;
      case CHECKPASS_INT:
        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000189")/*@res "���ͨ��"*/;
      case NOPASS_INT:
        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000190")/*@res "��˲�ͨ��"*/;
      case REJECT_INT:
      	return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000804")/*@res "����"*/;
      default:
        return "ERROR";
    }
  }

}