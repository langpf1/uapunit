package uap.workflow.modeler.uecomponent;

import javax.swing.TransferHandler;

import uap.workflow.modeler.bpmn.editor.handler.ActionFactory;

import com.mxgraph.swing.util.mxGraphActions;

import nc.ui.pub.beans.UIPopupMenu;

public class VertexPopupMenu extends UIPopupMenu {

	public VertexPopupMenu(Object eventSource) {
		add(ActionFactory.bind(eventSource, getDisplayName("cut"), TransferHandler.getCutAction(), "/com/mxgraph/examples/swing/images/cut.gif"));
		add(ActionFactory.bind(eventSource, getDisplayName("copy"), TransferHandler.getCopyAction(), "/com/mxgraph/examples/swing/images/copy.gif"));
		add(ActionFactory.bind(eventSource, getDisplayName("paste"), TransferHandler.getPasteAction(), "/com/mxgraph/examples/swing/images/paste.gif"));

		addSeparator();

		add(ActionFactory.bind(eventSource, getDisplayName("toTop"), mxGraphActions.getToFrontAction()));
		add(ActionFactory.bind(eventSource, getDisplayName("toFront"), mxGraphActions.getToFrontAction()));
		add(ActionFactory.bind(eventSource, getDisplayName("toBottom"), mxGraphActions.getToBackAction()));
		add(ActionFactory.bind(eventSource, getDisplayName("toBack"), mxGraphActions.getToBackAction()));
	}

	private static String getDisplayName(String name) {
//		String str = NCLangRes.getInstance().getStrByID("pfgraph", name);
		return name;
	}
}
