package uap.workflow.modeler.bpmn.graph;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Map;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxImageShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

public class GatewayShape extends mxImageShape {

	@Override
	public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {
		
		//create shape
		Rectangle temp = state.getRectangle();
		int x = temp.x;
		int y = temp.y;
		int w = temp.width;
		int h = temp.height;
		int halfWidth = w / 2;
		int halfHeight = h / 2;

		Polygon rhombus = new Polygon();
		rhombus.addPoint(x + halfWidth, y);
		rhombus.addPoint(x + w, y + halfHeight);
		rhombus.addPoint(x + halfWidth, y + h);
		rhombus.addPoint(x, y + halfHeight);
		
		Color previous=canvas.getGraphics().getColor();
		canvas.getGraphics().setColor(getStrokeColor(canvas,state));	
		canvas.getGraphics().draw(rhombus);
		canvas.getGraphics().setColor(getFillColor(canvas,state));
		canvas.getGraphics().fill(rhombus);
		canvas.getGraphics().setColor(previous);
		
		
		boolean flipH = mxUtils.isTrue(state.getStyle(),
				mxConstants.STYLE_IMAGE_FLIPH, false);
		boolean flipV = mxUtils.isTrue(state.getStyle(),
				mxConstants.STYLE_IMAGE_FLIPV, false);
		boolean xor = mxUtils.isTrue(state.getStyle(),
				"imgxor", false);
		if (xor){
			canvas.getGraphics().setXORMode(Color.BLACK);
		}
		canvas.drawImage(getImageBounds(canvas, state),
				getImageForStyle(canvas, state),
				mxGraphics2DCanvas.PRESERVE_IMAGE_ASPECT, flipH, flipV);
	}

	/**
	 * 
	 */
	public Rectangle getImageBounds(mxGraphics2DCanvas canvas, mxCellState state)
	{
		Map<String, Object> style = state.getStyle();
		double scale = canvas.getScale();
		String imgAlign = mxUtils.getString(style,
				mxConstants.STYLE_IMAGE_ALIGN, mxConstants.ALIGN_LEFT);
		String imgValign = mxUtils.getString(style,
				mxConstants.STYLE_IMAGE_VERTICAL_ALIGN,
				mxConstants.ALIGN_MIDDLE);
		int imgWidth = (int) (mxUtils.getInt(style,
				mxConstants.STYLE_IMAGE_WIDTH, mxConstants.DEFAULT_IMAGESIZE) * scale);
		int imgHeight = (int) (mxUtils.getInt(style,
				mxConstants.STYLE_IMAGE_HEIGHT, mxConstants.DEFAULT_IMAGESIZE) * scale);
		int spacing = (int) (mxUtils
				.getInt(style, mxConstants.STYLE_SPACING, 2) * scale);

		mxRectangle imageBounds = new mxRectangle(state);

		if (imgAlign.equals(mxConstants.ALIGN_CENTER))
		{
			imageBounds.setX(imageBounds.getX()
					+ (imageBounds.getWidth() - imgWidth) / 2);
		}
		else if (imgAlign.equals(mxConstants.ALIGN_RIGHT))
		{
			imageBounds.setX(imageBounds.getX() + imageBounds.getWidth()
					- imgWidth - spacing - 2);
		}
		else
		// LEFT
		{
			imageBounds.setX(imageBounds.getX() + spacing + 4);
		}

		if (imgValign.equals(mxConstants.ALIGN_TOP))
		{
			imageBounds.setY(imageBounds.getY() + spacing);
		}
		else if (imgValign.equals(mxConstants.ALIGN_BOTTOM))
		{
			imageBounds.setY(imageBounds.getY() + imageBounds.getHeight()
					- imgHeight - spacing);
		}
		else
		// MIDDLE
		{
			imageBounds.setY(imageBounds.getY()
					+ (imageBounds.getHeight() - imgHeight) / 2);
		}

		imageBounds.setWidth(imgWidth);
		imageBounds.setHeight(imgHeight);

		return imageBounds.getRectangle();
	}

	/**
	 * 
	 */
	public Color getFillColor(mxGraphics2DCanvas canvas, mxCellState state)
	{
		return mxUtils.getColor(state.getStyle(), mxConstants.STYLE_FILLCOLOR);
	}

	/**
	 * 
	 */
	public Color getStrokeColor(mxGraphics2DCanvas canvas, mxCellState state)
	{
		return mxUtils
				.getColor(state.getStyle(), mxConstants.STYLE_STROKECOLOR);
	}

	/**
	 * 
	 */
	public boolean hasGradient(mxGraphics2DCanvas canvas, mxCellState state)
	{
		return true;
	}

	

}
