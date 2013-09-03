package uap.workflow.modeler.bpmn.graph;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;

import uap.workflow.modeler.bpmn.graph.SwingCanvas;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxBasicShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import com.mxgraph.shape.mxMarkerRegistry;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxConstants;

public class SwimlaneShape extends mxBasicShape
{

	
	public void paintShape(mxGraphics2DCanvas canvas, mxCellState state)
	{
		int start = (int) Math.round(mxUtils.getInt(state.getStyle(),
				mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE)
				* canvas.getScale());

		Rectangle tmp = state.getRectangle();

		if (mxUtils
				.isTrue(state.getStyle(), mxConstants.STYLE_HORIZONTAL, true))
		{
			if (configureGraphics(canvas, state, true))
			{
				canvas.fillShape(new Rectangle(tmp.x, tmp.y, tmp.width, Math
						.min(tmp.height, start)));
			}

			if (configureGraphics(canvas, state, false))
			{
				canvas.getGraphics().drawRect(tmp.x, tmp.y, tmp.width,
						Math.min(tmp.height, start));
				canvas.getGraphics().drawRect(tmp.x, tmp.y + start, tmp.width,
						tmp.height - start);
			}
		}
		else
		{
			if (configureGraphics(canvas, state, true))
			{
				canvas.fillShape(new Rectangle(tmp.x, tmp.y, Math.min(
						tmp.width, start), tmp.height));
				
			//	state.getStyle().get("lableColor");
				canvas.getGraphics().setColor(Color.lightGray);
				canvas.getGraphics().fill(new Rectangle(tmp.x + start, tmp.y,
						tmp.width - start, tmp.height));	
			}

			if (configureGraphics(canvas, state, false))
			{
				canvas.getGraphics().drawRect(tmp.x, tmp.y,
						Math.min(tmp.width, start), tmp.height);
				canvas.getGraphics().drawRect(tmp.x + start, tmp.y,
						tmp.width - start, tmp.height);
			}
		}

	}
	
	
	protected mxRectangle getGradientBounds(mxGraphics2DCanvas canvas,
			mxCellState state)
	{
		int start = (int) Math.round(mxUtils.getInt(state.getStyle(),
				mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE)
				* canvas.getScale());
		mxRectangle result = new mxRectangle(state);

		if (mxUtils
				.isTrue(state.getStyle(), mxConstants.STYLE_HORIZONTAL, true))
		{
			result.setHeight(Math.min(result.getHeight(), start));
		}
		else
		{
			result.setWidth(Math.min(result.getWidth(), start));
		}

		return result;
	}

}
