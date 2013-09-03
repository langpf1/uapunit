package uap.workflow.engine.utils;
import java.lang.reflect.Method;
import uap.workflow.engine.exception.WorkflowRuntimeException;
public class WfmClassUtil {
	@SuppressWarnings("rawtypes")
	public static Object invokeMethod(Object owner, String methodName, Object[] args) {
		Class<? extends Object> ownerClass = owner.getClass();
		Class[] argsClass = null;
		if (args != null) {
			argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				if (args[i] == null) {
					return null;
				}
				argsClass[i] = args[i].getClass();
			}
		}
		Method method = null;
		try {
			method = ownerClass.getMethod(methodName, argsClass);
			return method.invoke(owner, args);
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	public static Object loadClass(String className) {
		try {
			Object object = Class.forName(className).newInstance();
			return object;
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
}
