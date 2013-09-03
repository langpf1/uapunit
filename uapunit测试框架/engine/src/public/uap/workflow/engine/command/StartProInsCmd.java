package uap.workflow.engine.command;
public class StartProInsCmd implements ICommand<Void> {
	public Void execute() {
		ICommandService commandService = new CommandService();
		/**
		 * 权限检测
		 */
		if (commandService.execute(new StartUpPurCheckCmd()).booleanValue()) {
			/**
			 * 流水号生成
			 */
			commandService.execute(new GenFormNumbCmd());
			/**
			 * 创建流程实例
			 */
			commandService.execute(new CreateProInsCmd());
		}
		return null;
	}
}
