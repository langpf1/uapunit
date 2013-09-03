package uap.workflow.modeler.bpmn.graph;

import java.awt.Color;
import java.awt.Shape;
import com.mxgraph.shape.mxMarkerRegistry;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxConstants;

public class SwingCanvas extends mxInteractiveCanvas {
	
	public SwingCanvas() {
		super();
		putShape("task", new TaskShape());
		putShape("subprocess", new SubProcessShape());
		putShape("event", new EventShape());
		putShape("gateway", new GatewayShape());
		putShape("intermediaEvent", new IntermediaEventShape());
		putShape("annotation", new AnnotationShape());
		putShape("dataObject", new DataShape());
		putShape("swimlane", new SwimlaneShape());

		mxMarkerRegistry.registerMarker("sequenceFlow", new DefaultSequenceFlowMarker());
		mxMarkerRegistry.registerMarker("hadCondition", new ConditionMarker());
	}
	
}
