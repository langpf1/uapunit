package uap.workflow.modeler.bpmn.listeners;

import uap.workflow.modeler.sheet.Property;

import com.mxgraph.model.mxICell;

/**
 * @author chengsc
 *
 */
public interface IGraphElementPropChangeListener {

	/**
	 * @param prop
	 * @param graphCell
	 * @param userObject
	 */
	public void handlePropertyChange(Property prop, mxICell graphCell, Object userObject);

}
