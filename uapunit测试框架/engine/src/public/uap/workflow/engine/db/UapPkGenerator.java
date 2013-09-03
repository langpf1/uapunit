package uap.workflow.engine.db;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.login.vo.NCSession;

import uap.workflow.engine.cfg.IdGenerator;
import uap.workflow.engine.interceptor.CommandExecutor;
public class UapPkGenerator implements IdGenerator {
	protected CommandExecutor commandExecutor;
	public String getNextId() {
		String groupNo = null;
		// String dsName = null;
		NCSession session = WorkbenchEnvironment.getInstance().getSession();
		if (session == null) {
			groupNo = "0001";
			// dsName = "design";
		} else {
			groupNo = session.getGroupCode();
			// dsName = session.getDsName();
		}
		return new SequenceGenerator().generate(groupNo);
	}
	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
	public void setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}
}
