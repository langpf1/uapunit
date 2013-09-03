package uap.workflow.modeler.utils;

public enum OpenUIStyleEnum {
	BisunessUI(0),	//上一步，制单人，全部活动，指定活动
	ApproveUI(1),
	DefinedUI(2),
	CustomURI(3);

	
	private int intValue;

	
	private OpenUIStyleEnum(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue(){
		return intValue;
	}
	
	public static OpenUIStyleEnum fromIntValue(int intValue) {
		switch (intValue) {
		case 0:
			return BisunessUI;
		case 1:
			return ApproveUI;
		case 2:
			return DefinedUI;
		case 3:
			return CustomURI;
		default:
			break;
		}
		return null;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public String toString(){
		switch (getIntValue()) {
		case 0:
			return "BisunessUI";
		case 1:
			return "ApproveUI";
		case 2:
			return "DefinedUI";
		case 3:
			return "CustomURI";
		default:
			return null;
		}
	}
}
