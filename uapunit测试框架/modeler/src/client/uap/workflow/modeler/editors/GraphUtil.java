package uap.workflow.modeler.editors;

import java.util.ArrayList;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

public class GraphUtil {
	
	/**
	 * @param component
	 * @return
	 */
	public ArrayList<mxCell> getLeafVertices(mxGraphComponent component)  {
		ArrayList<mxCell> l = new ArrayList<mxCell>();
		collectLeafVertices(l,(mxCell)component.getGraph().getDefaultParent());
		l.remove(component.getGraph().getDefaultParent());
		return l;
	}
	
	/**
	 * @param l
	 * @param parent
	 */
	private void collectLeafVertices(ArrayList<mxCell> l, mxCell parent) {
		if (parent.getChildCount() > 0) {

			for (int i = 0; i < parent.getChildCount(); i++)
				collectLeafVertices(l, (mxCell) parent.getChildAt(i));
		} else if(parent.isVertex()) {///
			l.add(parent);
		}

	}
	
	

}
