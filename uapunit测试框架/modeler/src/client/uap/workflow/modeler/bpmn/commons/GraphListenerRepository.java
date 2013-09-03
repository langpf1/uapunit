package uap.workflow.modeler.bpmn.commons;


import uap.workflow.modeler.bpmn.graph.itf.GraphListenerAdaptor;
import uap.workflow.modeler.bpmn.graph.itf.IGraphListenerClaim;

import com.mxgraph.view.mxGraph;

public class GraphListenerRepository {
	private static GraphListenerRepository instance = new GraphListenerRepository();

	private GraphListenerRepository() {

	}

	public static GraphListenerRepository getInstance() {
		return instance;
	}

	/**
	 * @param graph
	 */
	public void installListeners(mxGraph graph, IGraphListenerClaim[] listeners) {
		// install default listeners
		for (IGraphListenerClaim l : listeners)
			for (String type : l.getListenTargetType())
				graph.addListener(type, new GraphListenerAdaptor(l));
	}

	/**
	 * @param graph
	 * @param listeners
	 */
	public void installSelectionListeners(mxGraph graph, IGraphListenerClaim[] listeners) {
		// install default listeners
		for (IGraphListenerClaim l : listeners)
			for (String type : l.getListenTargetType())
				graph.getSelectionModel().addListener(type, new GraphListenerAdaptor(l));
	}
}
