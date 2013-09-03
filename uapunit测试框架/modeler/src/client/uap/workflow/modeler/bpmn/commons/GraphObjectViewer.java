package uap.workflow.modeler.bpmn.commons;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import uap.workflow.modeler.bpmn.graph.itf.IGraphListenerClaim;
import uap.workflow.modeler.bpmn.listeners.GraphBeanPropChangeListener;
import uap.workflow.modeler.sheet.Property;

import nc.ui.pub.beans.UITree;
import nc.ui.pub.graph.itf.ListenerType;
import nc.vo.pub.graph.element.IUfGraphUserOjbect;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;

/**
 * @author chengsc
 *
 */
public class GraphObjectViewer extends UITree implements IGraphListenerClaim, GraphBeanPropChangeListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	mxGraphComponent graphComponent;
	private HashMap<String, DefaultMutableTreeNode> nodeMap = new HashMap<String, DefaultMutableTreeNode>();

	public static class ObjectTreeNodeOjbect {
		mxICell cell;

		public ObjectTreeNodeOjbect(mxICell cell) {
			this.cell = cell;
		}

		public String toString() {
			if (cell.getValue() != null) {
				if (cell.getValue() instanceof String) {
					return cell.getValue().toString();
				}

				if (cell.getValue() instanceof IUfGraphUserOjbect) {
					return ((IUfGraphUserOjbect) cell.getValue()).getDisplayName();
				}
			}
			return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0024")/*@res "未定义对象"*/;
		}

		public mxICell getCell() {
			return cell;
		}

		public void setCell(mxICell cell) {
			this.cell = cell;
		}
	}

	public GraphObjectViewer(DefaultMutableTreeNode p0, mxGraphComponent graphComponent, DefaultTreeCellRenderer render) {
		super(p0);
		this.graphComponent = graphComponent;
		if (render != null)
			this.setCellRenderer(render);
		init();
		nodeMap.put(((mxICell) graphComponent.getGraph().getDefaultParent()).getId(), p0);
	}

	/**
	 *
	 */
	private void init() {
		this.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				ObjectTreeNodeOjbect userobject = (ObjectTreeNodeOjbect) selectedNode.getUserObject();
				// graphComponent.getGraph().selectCells(true , false,
				// userobject.getCell());
				// graphComponent.getGraph().setcell
				// graphComponent.getGraph().selectVertices(userobject.getCell());
				graphComponent.getGraph().setSelectionCell(userobject.getCell());

			}

		});

	}

	@ListenerType(eventType = mxEvent.ADD_CELLS)
	public void invokeAdd(Object sender, mxEventObject evt) {
		Object[] added = (Object[]) evt.getProperty("cells");
		for (Object obj : added) {
			mxICell cell = ((mxICell) obj);

			DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
			// root.add(new DefaultMutableTreeNode(new
			// ObjectTreeNodeOjbect(cell)));
			addNode(root, cell);

		}
		this.updateUI();
	}

	@ListenerType(eventType = mxEvent.SPLIT_EDGE)
	public void invokeSplitEdge(Object sender, mxEventObject evt) {

		Object[] moved = (Object[]) evt.getProperty("cells");
		mxCell newEdge = (mxCell) evt.getProperty("newEdge");
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
		for (Object obj : moved) {
			mxICell cell = ((mxICell) obj);
			addNode(root, cell);
		}
		addNode(root, newEdge);
		this.updateUI();
	}

	/**
	 * @param parent
	 */
	private void addNode(DefaultMutableTreeNode parent, mxICell cell) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new ObjectTreeNodeOjbect(cell));
		nodeMap.put(cell.getId(), node);
		if (cell.getChildCount() > 0) {
			for (int i = 0; i < cell.getChildCount(); i++) {
				addNode(node, cell.getChildAt(i));
			}
		}
		parent.add(node);

	}

	/**
	 * @param cell
	 */
	private void removeSubTree(mxICell cell) {
		if(nodeMap.get(cell.getId())!=null){
			nodeMap.get(cell.getId()).removeFromParent();
		}
		nodeMap.remove(cell.getId());
		for (int i = 0; i < cell.getChildCount(); i++) {
			removeSubTree(cell.getChildAt(i));
		}
	}

	@ListenerType(eventType = mxEvent.REMOVE_CELLS)
	public void invokeRemove(Object sender, mxEventObject evt) {

		// delete
		Object[] removed = (Object[]) evt.getProperty("cells");
		ArrayList<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
		for (Object obj : removed) {
			mxICell cell = ((mxICell) obj);
			removeSubTree(cell);
		}
		this.updateUI();
	}



	@ListenerType(eventType = mxEvent.MOVE_CELLS)
	public void invokeMove(Object sender, mxEventObject evt) {
		// add
		Object[] moved = (Object[]) evt.getProperty("cells");
		Boolean isCloned = (Boolean) evt.getProperty("clone");
		if (!isCloned) {
			for (Object obj : moved) {
				mxICell cell = ((mxICell) obj);
				mxICell p = cell.getParent();
				// if (p != graphComponent.getGraph().getDefaultParent()) {
				DefaultMutableTreeNode node = nodeMap.get(p.getId());
				if (node != null && node != nodeMap.get(cell.getId()).getParent()) {
					nodeMap.get(cell.getId()).removeFromParent();
					node.add(nodeMap.get(cell.getId()));
				}
				// }
			}

		} else {
			for (Object obj : moved) {
				mxICell cell = ((mxICell) obj);
				mxICell p = cell.getParent();
				if (p != graphComponent.getGraph().getDefaultParent()) {
					DefaultMutableTreeNode node = nodeMap.get(p.getId());
					DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(new ObjectTreeNodeOjbect(cell));
					nodeMap.put(cell.getId(), newnode);
					if (node != null) {
						node.add(nodeMap.get(cell.getId()));
					}
				} else {
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
					DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(new ObjectTreeNodeOjbect(cell));
					root.add(newnode);
					nodeMap.put(cell.getId(), newnode);
				}

			}
		}
		this.updateUI();
	}

	/**
	 * @param cells
	 */
	// public void addNodeByCell(Object[] cells) {
	// // add
	// for (Object obj : cells) {
	// mxICell cell = ((mxICell) obj);
	//
	// DefaultMutableTreeNode root = (DefaultMutableTreeNode)
	// this.getModel().getRoot();
	// DefaultMutableTreeNode node = new DefaultMutableTreeNode(new
	// ObjectTreeNodeOjbect(cell));
	// root.add(node);
	// nodeMap.put(cell.getId(), node);
	//
	// }
	// this.updateUI();
	// }
	public mxGraphComponent getGraphComponent() {
		return graphComponent;
	}

	public void setGraphComponent(mxGraphComponent graphComponent) {
		this.graphComponent = graphComponent;
	}

	@Override
	public String[] getListenTargetType() {
		// TODO Auto-generated method stub
		return new String[] { mxEvent.ADD_CELLS, mxEvent.REMOVE_CELLS, mxEvent.MOVE_CELLS, mxEvent.SPLIT_EDGE};
	}

	@Override
	public void valueChanged(Property srcProp, Property targetProp, Object userObject) {
		// TODO Auto-generated method stub
		this.updateUI();
	}

	/**
	 *
	 */
	public void clear() {
		((DefaultMutableTreeNode) this.getModel().getRoot()).removeAllChildren();
		nodeMap.clear();
		nodeMap.put(((mxICell) graphComponent.getGraph().getDefaultParent()).getId(), (DefaultMutableTreeNode) this.getModel().getRoot());
		this.updateUI();
	}
}