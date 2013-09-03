package uap.workflow.modeler.utils;


public enum TaskListenerTypeEnum {

	Create(0), 
	reject_before(11),
	reject_after(12),
	callback_before(21),
	callback_after(22),
	takeback_before(31),
	takeback_after(32),
	delegate_before(41),
	delegate_after(42),
	Assignment(5), 
	Complete(6),
	complete_before(61),
	complete_after(62);
	// 枚举的整型值
	private int intValue;
	
	private TaskListenerTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static TaskListenerTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:必须保证与枚举值一致
		case 0: return Create;
		case 11: return reject_before;
		case 12: return reject_after;
		case 21: return callback_before;
		case 22: return callback_after;
		case 31: return takeback_before;
		case 32: return takeback_after;
		case 41: return delegate_before;
		case 42: return delegate_after;
		case 5: return Assignment;
		case 6: return Complete;
		case 61: return complete_before;
		case 62: return complete_after;
		default:
			break;
		}
		return null;
	}
	

	public String toString() {
		switch (getIntValue()) {
		case 0: return "Create";
		case 11: return "reject_before";
		case 12: return "reject_after";
		case 21: return "callback_before";
		case 22: return "callback_after";
		case 31: return "takeback_before";
		case 32: return "takeback_after";
		case 41: return "delegate_before";
		case 42: return "delegate_after";
		case 5: return "Assignment";
		case 6: return "Complete";
		case 61: return "complete_before";
		case 62: return "complete_after";
		}
		return null;
	}

}
