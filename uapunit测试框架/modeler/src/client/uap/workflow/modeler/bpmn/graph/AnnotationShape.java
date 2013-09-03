package uap.workflow.modeler.bpmn.graph;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Map;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxBasicShape;
import com.mxgraph.shape.mxImageShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

public class AnnotationShape extends mxBasicShape {

	public void paintShape(mxGraphics2DCanvas canvas, mxCellState state)
	{
		canvas.getGraphics().setColor(Color.black);
		canvas.getGraphics().drawLine((int)state.getX(), 
				(int)state.getY(), 
				(int)(state.getWidth()/4+ state.getX()), 
				(int)(state.getY()));
		canvas.getGraphics().drawLine((int)state.getX(), 
				(int)state.getY(), 
				(int)(state.getX()), 
				(int)(state.getHeight()+ state.getY()));
		canvas.getGraphics().drawLine((int)state.getX(), 
				(int)(state.getY()+state.getHeight()), 
				(int)(state.getWidth()/4+ state.getX()), 
				(int)(state.getHeight()+ state.getY()));
	}



}
