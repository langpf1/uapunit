package uap.workflow.engine.command;
public class StartProInsCmd implements ICommand<Void> {
	public Void execute() {
		ICommandService commandService = new CommandService();
		/**
		 * Ȩ�޼��
		 */
		if (commandService.execute(new StartUpPurCheckCmd()).booleanValue()) {
			/**
			 * ��ˮ������
			 */
			commandService.execute(new GenFormNumbCmd());
			/**
			 * ��������ʵ��
			 */
			commandService.execute(new CreateProInsCmd());
		}
		return null;
	}
}
