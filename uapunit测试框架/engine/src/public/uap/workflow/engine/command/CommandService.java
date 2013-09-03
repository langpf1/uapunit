package uap.workflow.engine.command;

/**
 * 命令服务实现类
 * 
 * @author tianchw
 * 
 */
public class CommandService implements ICommandService {
	public <T> T execute(ICommand<T> command) {
		return command.execute();
	}
}
