package uap.workflow.engine.server;
public interface IProcessServer extends LifeCycle {
	void destory();
	void deploy();
	void monitor();
}
