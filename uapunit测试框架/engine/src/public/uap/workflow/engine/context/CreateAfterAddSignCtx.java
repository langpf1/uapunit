package uap.workflow.engine.context;
public class CreateAfterAddSignCtx extends AddSignTaskInfoCtx {
	private static final long serialVersionUID = -7255508785122287864L;
	private Logic logic;
	@Override
	public void setTaskPk(String taskPk) {
		this.taskPk = taskPk;
	}
	public Logic getLogic() {
		return logic;
	}
	public void setLogic(Logic logic) {
		this.logic = logic;
	}
}
