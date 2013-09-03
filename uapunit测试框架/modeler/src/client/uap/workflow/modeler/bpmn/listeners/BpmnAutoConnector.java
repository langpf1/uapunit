package uap.workflow.modeler.bpmn.listeners;

import java.util.ArrayList;

import uap.workflow.modeler.BasicBpmnGraphEditor;
import uap.workflow.modeler.bpmn.graph.itf.IGraphListenerClaim;
import uap.workflow.modeler.bpmn.graph.itf.ListenerType;
import uap.workflow.modeler.editors.GraphUtil;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;


public class BpmnAutoConnector implements IGraphListenerClaim {
	mxGraphComponent component;
	BasicBpmnGraphEditor editor;

	public BpmnAutoConnector(mxGraphComponent component, BasicBpmnGraphEditor editor) {
		this.component = component;
		this.editor = editor;
	}

	@Override
	public String[] getListenTargetType() {
		// TODO Auto-generated method stub
		return new String[] { mxEvent.MOVE_CELLS };
	}

	@ListenerType(eventType = mxEvent.MOVE_CELLS)
	public void invokeRemove(Object sender, mxEventObject evt) {//
		Object[] removed = (Object[]) evt.getProperty("cells");
		Boolean isCloned = (Boolean) evt.getProperty("clone");
		if (isCloned) {
			double dx = (Double) evt.getProperty("dx");
			double dy = (Double) evt.getProperty("dy");
			for (Object obj : removed) {
				mxCell cell = ((mxCell) obj);
				connectOnImport(dx, dy, cell);
			}
			editor.setIncomingCell(null);
			return;
		}
		for (Object obj : removed) {
			mxCell cell = ((mxCell) obj);
			if (cell.isVertex() || component.getGraph().getView().getState(cell) == null)
				return;
			int num = component.getGraph().getView().getState(cell).getAbsolutePointCount();
			double statex = component.getGraph().getView().getState(cell).getAbsolutePoint(0).getX();
			double statex1 = component.getGraph().getView().getState(cell).getAbsolutePoint(num - 1).getX();
			double statey = component.getGraph().getView().getState(cell).getAbsolutePoint(0).getY();
			double statey1 = component.getGraph().getView().getState(cell).getAbsolutePoint(num - 1).getY();

			double scale = component.getGraph().getView().getScale();
			double dx = (Double) evt.getProperty("dx") * scale;
			double dy = (Double) evt.getProperty("dy") * scale;

			mxCell sourceCell = null;
			mxCell targetCell = null;
			ArrayList<mxCell> l = new GraphUtil().getLeafVertices(component);
			for (Object o : l) {

				if (component.getGraph().getView().getState((mxCell) o).contains(statex + dx, statey + dy)) {
					if (targetCell == null || targetCell != o) {
						sourceCell = (mxCell) o;
					}
				} else if (component.getGraph().getView().getState((mxCell) o).contains(statex1 + dx, statey1 + dy)) {
					if (sourceCell == null || sourceCell != o) {
						targetCell = (mxCell) o;
					}
				}
				if (targetCell != null && sourceCell != null)
					break;
			}

			if (targetCell != sourceCell) {

				if (sourceCell != null && cell.getSource() == null && sourceCell.getChildCount() == 0) {

					component.getGraph().connectCell(cell, sourceCell, true);
				}

				if (targetCell != null && cell.getTarget() == null && targetCell.getChildCount() == 0) {
					component.getGraph().connectCell(cell, targetCell, false);//
				}
			}
		}
	}

	/**
	 * 支持快速添加驱动时
	 * */
	private void spliOnImport(double dx, double dy, mxCell cell){
		if(cell.isVertex()){
			
		}
	}
	
	
	/**
	 * @param dx
	 * @param dy
	 * @param cell
	 */
	private void connectOnImport(double dx, double dy, mxCell cell) {
	}

}
