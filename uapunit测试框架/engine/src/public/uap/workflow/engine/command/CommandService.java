package uap.workflow.engine.command;

/**
 * �������ʵ����
 * 
 * @author tianchw
 * 
 */
public class CommandService implements ICommandService {
	public <T> T execute(ICommand<T> command) {
		return command.execute();
	}
}
