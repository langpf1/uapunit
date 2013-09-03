package uap.workflow.modeler.editors;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.view.mxCellState;

public class BoundaryMarker extends mxCellMarker {

	public BoundaryMarker(mxGraphComponent graphComponent, Color validColor) {
		super(graphComponent, validColor);
	}
	
	@Override
	public mxCellState process(MouseEvent e) {
		// TODO Auto-generated method stub
		return super.process(e);
	}
}
