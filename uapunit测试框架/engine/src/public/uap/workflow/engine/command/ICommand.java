package uap.workflow.engine.command;
/**
 * ����ӿ�
 * 
 * @author tianchw
 * 
 * @param <T>
 */
public interface ICommand<T> {
	T execute();
}
