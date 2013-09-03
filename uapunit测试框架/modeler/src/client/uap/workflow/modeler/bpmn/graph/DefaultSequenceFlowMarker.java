package uap.workflow.modeler.bpmn.graph;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxIMarker;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

public class DefaultSequenceFlowMarker implements mxIMarker {

	@Override
	public mxPoint paintMarker(mxGraphics2DCanvas canvas, mxCellState state, String type, mxPoint pe, 
			double nx, double ny, double size, boolean source) {
		canvas.getGraphics().drawLine((int)Math.round(pe.getX()), (int)Math.round(pe.getY()), 
				(int)Math.round(pe.getX() - nx), (int)Math.round(pe.getY() - ny));
		canvas.getGraphics().drawLine((int) Math.round(pe.getX() - nx / 2 - ny ), (int) Math.round(pe.getY() + nx / 2 - ny ), 
				(int) Math.round(pe.getX() - nx  + ny / 2), (int) Math.round(pe.getY() - ny / 2 - nx ));
		return new mxPoint(-nx / 2, -ny / 2);
	}

}
