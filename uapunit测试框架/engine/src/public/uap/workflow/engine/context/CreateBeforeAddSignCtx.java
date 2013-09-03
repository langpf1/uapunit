package uap.workflow.engine.context;
public class CreateBeforeAddSignCtx extends AddSignTaskInfoCtx {
	private static final long serialVersionUID = 8010541345193091699L;
	private Logic logic;
	public Logic getLogic() {
		return logic;
	}
	public void setLogic(Logic logic) {
		this.logic = logic;
	}

}
