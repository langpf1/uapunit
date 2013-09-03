package uap.workflow.engine.context;
public enum Logic {
	Sequence, Parallel;
	public static Logic getLogicByString(String logic) {
		if (logic == null) {
			return null;
		}
		if (logic.equalsIgnoreCase("Sequence")) {
			return Logic.Sequence;
		}
		if (logic.equalsIgnoreCase("Parallel")) {
			return Logic.Parallel;
		}
		return null;
	}
	public static String logicToString(Logic logic) {
		if (logic.equals(Logic.Sequence)) {
			return "Sequence";
		}
		if (logic.equals(Logic.Parallel)) {
			return "Parallel";
		}
		return null;
	}
}
