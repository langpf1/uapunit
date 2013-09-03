package uap.workflow.engine.server;

import java.io.Serializable;

public interface LifeCycle extends Serializable {
	public void start() throws Exception;
	public void stop() throws Exception;
}
