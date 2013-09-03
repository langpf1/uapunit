package uap.workflow.engine.command;
/**
 * 命令服务接口
 * 
 * @author tianchw
 * 
 */
public interface ICommandService {
	<T> T execute(ICommand<T> command);
}
