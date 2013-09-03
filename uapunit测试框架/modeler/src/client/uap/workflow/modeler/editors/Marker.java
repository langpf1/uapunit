package uap.workflow.modeler.editors;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;

public class Marker extends mxCellMarker {
	mxRectangle rangebounds;
	boolean isStart;

	public Marker(mxGraphComponent graphcomp, Color validColor, boolean isStart) {
		super(graphcomp, validColor);

		this.isStart = isStart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mxgraph.swing.handler.mxCellMarker#getState(java.awt.event.MouseEvent)
	 */
	public mxCellState getState(MouseEvent e) {
		//
		double scale = graphComponent.getGraph().getView().getScale();
		double left_botton_x = e.getX() - rangebounds.getWidth() / 2 * scale;
		double left_botton_y = e.getY() + rangebounds.getHeight() / 2 * scale;

		double right_top_x = e.getX() + rangebounds.getWidth() / 2 * scale;
		double right_top_y = e.getY() - rangebounds.getHeight() / 2 * scale;
		ArrayList<mxCell> l = new GraphUtil().getLeafVertices(graphComponent);

		for (Object o : l) {

			mxCellState cellstate = graphComponent.getGraph().getView().getState((mxCell) o);
			if (isStart) {
				if (cellstate.contains(left_botton_x, left_botton_y) && ((mxCell) o).getChildCount() == 0) {
					return cellstate;
				}
			} else {
				if (cellstate.contains(right_top_x, right_top_y) && ((mxCell) o).getChildCount() == 0) {
					return cellstate;
				}
			}
		}
		return null;
	}

	public void unmark() {
		super.unmark();
	}

	public mxRectangle getRangeBounds() {
		return rangebounds;
	}

	public void setRangeBounds(mxRectangle bounds) {
		this.rangebounds = bounds;
	}
}
