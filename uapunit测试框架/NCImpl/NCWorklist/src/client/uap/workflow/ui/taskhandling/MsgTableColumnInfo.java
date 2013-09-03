package uap.workflow.ui.taskhandling;

import java.util.HashMap;

import nc.ui.ml.NCLangRes;

/**
 * 消息面板表格列信息
 * @author leijun 2008-7
 * @since 5.5
 */
public class MsgTableColumnInfo {
	//整型值必须从0开始，逐一递增
	public static final int PK_INT = 0;

	public static final int PRIORITY_INT = 1;

	public static final int STATUS_INT = 2;

	public static final int BILLTYPE_INT = 3;

	public static final int TITLE_INT = 4;

	public static final int SENDER_INT = 5;

	public static final int PUBLISHDATE_INT = 6;

	public static final int DEALDATE_INT = 7;

	public static final int MSGTYPE_INT = 8;

	public static final MsgTableColumnInfo PK = new MsgTableColumnInfo(PK_INT);

	public static final MsgTableColumnInfo PRIORITY = new MsgTableColumnInfo(PRIORITY_INT);

	public static final MsgTableColumnInfo STATUS = new MsgTableColumnInfo(STATUS_INT);

	public static final MsgTableColumnInfo BILLTYPE = new MsgTableColumnInfo(BILLTYPE_INT);

	public static final MsgTableColumnInfo TITLE = new MsgTableColumnInfo(TITLE_INT);

	public static final MsgTableColumnInfo SENDER = new MsgTableColumnInfo(SENDER_INT);

	public static final MsgTableColumnInfo PUBLISHDATE = new MsgTableColumnInfo(PUBLISHDATE_INT);

	public static final MsgTableColumnInfo DEALDATE = new MsgTableColumnInfo(DEALDATE_INT);

	public static final MsgTableColumnInfo MSGTYPE = new MsgTableColumnInfo(MSGTYPE_INT);

	public static final String[] TAGS = { "pkmessage", "priority", "isnew", "pk_billtype", "title",
			"user_name", "publishdate", "dealdate", "msgtype" };

	public static final MsgTableColumnInfo[] VALUES = { PK, PRIORITY, STATUS, BILLTYPE, TITLE,
			SENDER, PUBLISHDATE, DEALDATE, MSGTYPE };

	private static final HashMap tagMap = new HashMap();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	/**
	 * 构造对象
	 * @param value The value
	 */
	private MsgTableColumnInfo(int value) {
		_value = value;
	}

	/**
	 * 根据字串来构造对象，字串必取值于TAGS数组中的某值。
	 *
	 * @param tag The String
	 * @return The MsgTableColumnInfo object
	 */
	public static MsgTableColumnInfo fromString(String tag) {
		MsgTableColumnInfo race = (MsgTableColumnInfo) tagMap.get(tag);
		if (race == null && tag != null)
			throw new IllegalArgumentException(tag);
		return race;
	}

	public String toString() {
		switch (_value) {
			case PK_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000425")/* 消息主键 */;
			case PRIORITY_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000415")/* "优先级" */;
			case STATUS_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000426")/* "状态" */;
			case BILLTYPE_INT:
				return NCLangRes.getInstance().getStrByID("common", "UC000-0000807")/*@res "单据类型"*/;
			case TITLE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000216")/* "主题" */;
			case SENDER_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000202")/* "发送人" */;
			case PUBLISHDATE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000429")/* "发送日期" */;
			case DEALDATE_INT:
				return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000430") /* "处理日期" */;
			case MSGTYPE_INT:
				return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0034")/*@res "类型"*/;
			default:
				return "ERROR";
		}
	}

	/**
	 * 获得标记值
	 */
	public String getTag() {
		return TAGS[_value];
	}
}