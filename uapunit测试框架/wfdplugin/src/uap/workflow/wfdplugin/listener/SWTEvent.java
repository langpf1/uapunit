package uap.workflow.wfdplugin.listener;

import uap.workflow.wfdplugin.util.StringUtil;

/**
 * 
 * @author kongml
 * 
 */
public class SWTEvent {
	private boolean isValidate;
	private Object source;
	private EventType type;
	public static final String Prefix = "event:";
	public static final String TypeStr = "type";
	public static final String SourceStr = "source";
	public static final String Split = ",";

	public boolean isValidate() {
		return isValidate;
	}

	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public static SWTEvent parse(String input) {
		SWTEvent event = new SWTEvent();
		event.setValidate(false);
		if (!StringUtil.isNullOrEmpty(input)) {
			if (input.startsWith(Prefix)) {
				int bgn = input.indexOf("{");
				int end = input.lastIndexOf("}");
				String eventStr = input.substring(bgn + 1, end);
				String[] properties = eventStr.split(Split);
				buildEvent(event, properties);
				event.setValidate(true);
			}
		}
		return event;
	}

	private static void buildEvent(SWTEvent event, String[] properties) {
		if (properties == null || properties.length < 1) {
			return;
		}
		for (String property : properties) {
			String[] keyValue = property.split(":");
			if (keyValue == null || keyValue.length != 2) {
				continue;
			}
			if (keyValue[0].toLowerCase().equals(TypeStr)) {
				try {
					event.setType(EventType.valueOf(keyValue[1]));
				} catch (IllegalArgumentException ie) {
					event.setType(EventType.unkown);
				}
			} else if (keyValue[0].toLowerCase().equals(SourceStr)) {
				event.setSource(keyValue[1]);
			}
		}
	}

	public enum EventType {
		opentype, unkown
	}
}
