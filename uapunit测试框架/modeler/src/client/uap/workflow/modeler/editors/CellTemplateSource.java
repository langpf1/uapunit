package uap.workflow.modeler.editors;

import java.util.HashMap;

import nc.bs.logging.Logger;

import com.mxgraph.model.mxCell;

public class CellTemplateSource {
	
	private HashMap<String, mxCell> map = new HashMap<String, mxCell>();
	
	public void addTempletSource(mxCell cell)  {
		map.put(cell.getStyle(), cell);
	}
	
	public mxCell genCellfromTemplate(String style) {
		mxCell cell = map.get(style);
		if (cell != null)
			try {
				return (mxCell) cell.clone();
			} catch (CloneNotSupportedException e) {
				Logger.error(e.getMessage(), e);
			}

		return null;
	}

}
