package uap.workflow.modeler.utils;

public enum RejectTypeEnum {
	LastStep(0),	//上一步，制单人，全部活动，指定活动
	BillMaker(1),
	AllActivity(2),
	SpecifiedActivity(3);

	
	private int intValue;

	
	private RejectTypeEnum(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue(){
		return intValue;
	}
	
	public static RejectTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		case 0:
			return LastStep;
		case 1:
			return BillMaker;
		case 2:
			return AllActivity;
		case 3:
			return SpecifiedActivity;
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
			return "LastStep";
		case 1:
			return "BillMaker";
		case 2:
			return "AllActivity";
		case 3:
			return "SpecifiedActivity";
		default:
			return null;
		}
	}
}
