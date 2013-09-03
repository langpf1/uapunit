package uap.workflow.engine.logger;
import nc.bs.logging.Log;
/**
 * 
 * @author tianchw
 *
 */
public class WorkflowLogger {
	private static Log lfwlogger = Log.getInstance("workflow");
	public static void info(String msg) {
		lfwlogger.info(msg);
	}
	public static void debug(String msg) {
		lfwlogger.debug(msg);
	}
	public static void error(String msg, Throwable t) {
		lfwlogger.error(msg, t);
	}
	public static void error(String msg) {
		lfwlogger.error(msg);
	}
	public static void error(Throwable e) {
		lfwlogger.error(e.getMessage(), e);
	}
	public static boolean isDebugEnabled() {
		return lfwlogger.isDebugEnabled();
	}
	public static boolean isInfoEnabled() {
		return lfwlogger.isInfoEnabled();
	}
	public static void warn(String string) {
		lfwlogger.warn(string);
	}
	public static boolean isWarnEnabled() {
		return lfwlogger.isWarnEnabled();
	}
}
