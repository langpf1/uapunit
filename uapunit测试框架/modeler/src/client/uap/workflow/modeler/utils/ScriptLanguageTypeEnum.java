package uap.workflow.modeler.utils;

public enum ScriptLanguageTypeEnum {

	javascript(0), 
	groovy(1),
	juel(2);

	// ö�ٵ�����ֵ
	private int intValue;
	
	private ScriptLanguageTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static ScriptLanguageTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return javascript;
		case 1:
			return groovy;
		case 2:
			return juel;
		default:
			break;
		}
		return null;
	}
	

	public String toString() {
		switch (getIntValue()) {
		case 0:
			return "javascript";
		case 1:
			return "groovy";
		case 2:
			return "juel";
		}
		return null;
	}

}
