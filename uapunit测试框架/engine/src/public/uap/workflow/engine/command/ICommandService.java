package uap.workflow.engine.command;
/**
 * �������ӿ�
 * 
 * @author tianchw
 * 
 */
public interface ICommandService {
	<T> T execute(ICommand<T> command);
}
