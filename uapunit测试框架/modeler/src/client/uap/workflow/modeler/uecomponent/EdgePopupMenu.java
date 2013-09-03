package uap.workflow.modeler.uecomponent;

import javax.swing.JMenu;
import javax.swing.TransferHandler;

import uap.workflow.modeler.bpmn.editor.handler.ActionFactory;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.flowdesigner.editor.EditorActions.ColorAction;
import nc.ui.pub.flowdesigner.editor.EditorActions.KeyValueAction;
import nc.ui.pub.flowdesigner.editor.EditorActions.PromptValueAction;
import nc.ui.pub.flowdesigner.editor.EditorActions.SetStyleAction;
import nc.ui.pub.flowdesigner.editor.EditorActions.ToggleAction;

public class EdgePopupMenu extends UIPopupMenu {
	public EdgePopupMenu(Object resouce) {

		add(ActionFactory.bind(resouce, getDisplayName("cut"), TransferHandler.getCutAction(), "/com/mxgraph/examples/swing/images/cut.gif"));
		add(ActionFactory.bind(resouce, getDisplayName("copy"), TransferHandler.getCopyAction(), "/com/mxgraph/examples/swing/images/copy.gif"));
		add(ActionFactory.bind(resouce, getDisplayName("paste"), TransferHandler.getPasteAction(), "/com/mxgraph/examples/swing/images/paste.gif"));

		addSeparator();

		JMenu submenu = (JMenu) add(new JMenu(getDisplayName("line")));

		submenu.add(ActionFactory.bind(resouce, getDisplayName("linecolor"), new ColorAction("Linecolor", mxConstants.STYLE_STROKECOLOR), "/com/mxgraph/examples/swing/images/linecolor.png"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(resouce, getDisplayName("dashed"), new ToggleAction(mxConstants.STYLE_DASHED)));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("linewidth"), new PromptValueAction(mxConstants.STYLE_STROKEWIDTH, "Linewidth")));

		submenu = (JMenu) add(new JMenu(getDisplayName("connector")));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("straight"), new SetStyleAction("straight"), "/com/mxgraph/examples/swing/images/straight.gif"));

		submenu.add(ActionFactory.bind(resouce, getDisplayName("horizontal"), new SetStyleAction(""), "/com/mxgraph/examples/swing/images/connect.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("vertical"), new SetStyleAction("vertical"), "/com/mxgraph/examples/swing/images/vertical.gif"));

		submenu.add(ActionFactory.bind(resouce, getDisplayName("entityRelation"), new SetStyleAction("edgeStyle=mxEdgeStyle.EntityRelation"), "/com/mxgraph/examples/swing/images/entity.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("arrow"), new SetStyleAction("arrow"), "/com/mxgraph/examples/swing/images/arrow.png"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(resouce, getDisplayName("plain"), new ToggleAction(mxConstants.STYLE_NOEDGESTYLE)));

		addSeparator();

		submenu = (JMenu) add(new JMenu(getDisplayName("linestart")));

		submenu.add(ActionFactory.bind(resouce, getDisplayName("open"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OPEN), "/com/mxgraph/examples/swing/images/open_start.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("classic"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_CLASSIC),
				"/com/mxgraph/examples/swing/images/classic_start.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("block"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_BLOCK),
				"/com/mxgraph/examples/swing/images/block_start.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(resouce, getDisplayName("diamond"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_DIAMOND),
				"/com/mxgraph/examples/swing/images/diamond_start.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("oval"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OVAL), "/com/mxgraph/examples/swing/images/oval_start.gif"));

		submenu.addSeparator();
		submenu.add(ActionFactory.bind(resouce, getDisplayName("none"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.NONE)));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("size"), new PromptValueAction(mxConstants.STYLE_STARTSIZE, "Linestart Size")));
		submenu.addSeparator();

		submenu = (JMenu) add(new JMenu(getDisplayName("lineend")));

		submenu.add(ActionFactory.bind(resouce, getDisplayName("open"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OPEN), "/com/mxgraph/examples/swing/images/open_end.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("classic"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC),
				"/com/mxgraph/examples/swing/images/classic_end.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("block"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK), "/com/mxgraph/examples/swing/images/block_end.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(resouce, getDisplayName("diamond"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND),
				"/com/mxgraph/examples/swing/images/diamond_end.gif"));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("oval"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OVAL), "/com/mxgraph/examples/swing/images/oval_end.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(resouce, getDisplayName("none"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.NONE)));
		submenu.add(ActionFactory.bind(resouce, getDisplayName("size"), new PromptValueAction(mxConstants.STYLE_ENDSIZE, "Linestart Size")));

		addSeparator();

		add(ActionFactory.bind(resouce, getDisplayName("toTop"), mxGraphActions.getToFrontAction()));
		add(ActionFactory.bind(resouce, getDisplayName("toFront"), mxGraphActions.getToFrontAction()));
		add(ActionFactory.bind(resouce, getDisplayName("toBottom"), mxGraphActions.getToBackAction()));
		add(ActionFactory.bind(resouce, getDisplayName("toBack"), mxGraphActions.getToBackAction()));
	}

	private static String getDisplayName(String name) {
		String str = NCLangRes.getInstance().getStrByID("pfgraph", name);
		return str;
	}
}
