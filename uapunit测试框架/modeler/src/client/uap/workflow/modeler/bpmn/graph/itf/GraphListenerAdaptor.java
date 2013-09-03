package uap.workflow.modeler.bpmn.graph.itf;

import java.lang.reflect.Method;

import nc.bs.logging.Logger;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

/**
 * @author chengsc
 * 
 */
public class GraphListenerAdaptor implements mxIEventListener {
	private Object delegate;

	public GraphListenerAdaptor(Object delegate) {
		this.delegate = delegate;
	}

	@Override
	public final void invoke(Object sender, mxEventObject evt) {
		// TODO Auto-generated method stub
		if (delegate == null)
			return;

		Method[] mtds = delegate.getClass().getMethods();
		try {
			for (Method m : mtds) {
				ListenerType lt = m.getAnnotation(ListenerType.class);
				if (lt != null && lt.eventType() != null && lt.eventType().equals(evt.getName()))

					m.invoke(delegate, new Object[] { sender, evt });
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
		}
	}

}
