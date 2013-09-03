package uap.workflow.pub.app.notice;

import java.io.Serializable;
import java.util.HashMap;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 消息配置中的发送条件
 * @author 雷军 created on 2005-4-13
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
   * 获得标记值
   */
  public String getTag() {
    return TAGS[_value];
  }

  public String getXpression() {
    return CONDITION_XPRESSIONS[_value];
  }

  /**
   * 将NC30的插件的条件类型转换为NC31的格式
   * <li>该方法自NC31以后将废弃
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
   * 获得显示名称
   */
  public String toString() {
    switch (_value) {
      case NONE_INT:
        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000188")/*@res "无条件"*/;
      case CHECKPASS_INT:
        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000189")/*@res "审核通过"*/;
      case NOPASS_INT:
        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000190")/*@res "审核不通过"*/;
      case REJECT_INT:
      	return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000804")/*@res "驳回"*/;
      default:
        return "ERROR";
    }
  }

}