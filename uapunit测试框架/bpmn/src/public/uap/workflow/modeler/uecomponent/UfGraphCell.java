package uap.workflow.modeler.uecomponent;

import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.IUserObjectClone;
import uap.workflow.bpmn2.model.Lane;
import uap.workflow.bpmn2.model.Pool;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

/**
 * @author chengsc
 * 
 */
public class UfGraphCell extends mxCell {

	private UfGraphCell boundaryCell = null;
	
	
	public UfGraphCell(Object value, mxGeometry geometry, String style)
	{
		super(value,geometry,style);
	}
	
	
	public UfGraphCell(GraphElementMeta inMeta) {

		super(null, new mxGeometry(0, 0, inMeta.getWidth(), inMeta.getHeight()), inMeta.getStyle());
		if (inMeta.getUserObjectClass() != null && inMeta.getUserObjectClass().length() > 0 && inMeta.getUserObjectClass().indexOf(".") != -1) {
			try {
				Object value= Class.forName(inMeta.getUserObjectClass()).newInstance();
				if(value instanceof FlowElement){
					((FlowElement)value).setName(inMeta.getName());
				}else if (value instanceof Lane){
					((Lane)value).setName(inMeta.getName());
				}else if (value instanceof Pool){
					((Pool)value).setName(inMeta.getName());
				}
				setValue(value);//
			} catch (Exception e) {

			}
		}
		// setId(GuidUtils.generate());//
	}

	public UfGraphCell getBoundaryCell() {
		return boundaryCell;
	}


	public void setBoundaryCell(UfGraphCell boundaryCell) {
		this.boundaryCell = boundaryCell;
	}


	/**
	 * @param userObject
	 * @param geo
	 * @param style
	 */
	public UfGraphCell(Object userObject, mxGeometry geo, String style, GraphElementMeta inMeta) {
		super(userObject, geo, style);
		try {
			setValue(Class.forName(inMeta.getUserObjectClass()).newInstance());//
		} catch (Exception e) {

		}
	}

	/**
	 * @param holder
	 */
	public UfGraphCell(GraphElementHolder holder) {
		super(holder.getUserobject(), (mxGeometry) holder.getGeometry(), holder.getGraphstyle());
		setId(holder.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mxgraph.model.mxCell#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		UfGraphCell c = (UfGraphCell) super.clone();
		// only IUfGraphUserOjbect need replicate
		if (this.getValue() != null ) {
			if(this.getValue() instanceof IUfGraphUserOjbect){
				c.setValue(((IUfGraphUserOjbect) this.getValue()).replicate());
			}else if(this.getValue() instanceof IUserObjectClone){
				c.setValue(((IUserObjectClone) this.getValue()).replicate());
			}
			
			else{
				c.setValue(this.getValue());
			}
			
		}
		c.setId(java.util.UUID.randomUUID().toString());
		return c;
	}

	/**
	 * 
	 */
	public void cellValueChanged(Object o) {
		if (this.getValue() != null && (this.getValue() instanceof IUfGraphUserOjbect))
			((IUfGraphUserOjbect) this.getValue()).cellValueChanged(o);
	}

}
