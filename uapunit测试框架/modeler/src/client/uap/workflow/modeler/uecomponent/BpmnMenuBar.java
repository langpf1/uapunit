package uap.workflow.modeler.uecomponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.plaf.ColorUIResource;

import uap.workflow.modeler.bpmn.editor.handler.ActionFactory;
import uap.workflow.modeler.utils.BmpnLayoutFactory;
import uap.workflow.modeler.utils.EditorActions.AlignCellsAction;
import uap.workflow.modeler.utils.EditorActions.AutosizeAction;
import uap.workflow.modeler.utils.EditorActions.BackgroundAction;
import uap.workflow.modeler.utils.EditorActions.BackgroundImageAction;
import uap.workflow.modeler.utils.EditorActions.ColorAction;
import uap.workflow.modeler.utils.EditorActions.DeleteAction;
import uap.workflow.modeler.utils.EditorActions.ExitAction;
import uap.workflow.modeler.utils.EditorActions.GridColorAction;
import uap.workflow.modeler.utils.EditorActions.GridStyleAction;
import uap.workflow.modeler.utils.EditorActions.HelpAction;
import uap.workflow.modeler.utils.EditorActions.HistoryAction;
import uap.workflow.modeler.utils.EditorActions.KeyValueAction;
import uap.workflow.modeler.utils.EditorActions.NewAction;
import uap.workflow.modeler.utils.EditorActions.OpenAction;
import uap.workflow.modeler.utils.EditorActions.OpenFromFileAction;
import uap.workflow.modeler.utils.EditorActions.PageBackgroundAction;
import uap.workflow.modeler.utils.EditorActions.PageSetupAction;
import uap.workflow.modeler.utils.EditorActions.PrintAction;
import uap.workflow.modeler.utils.EditorActions.PromptPropertyAction;
import uap.workflow.modeler.utils.EditorActions.PromptValueAction;
import uap.workflow.modeler.utils.EditorActions.SaveAction;
import uap.workflow.modeler.utils.EditorActions.SaveFileAction;
import uap.workflow.modeler.utils.EditorActions.ScaleAction;
import uap.workflow.modeler.utils.EditorActions.SetLabelPositionAction;
import uap.workflow.modeler.utils.EditorActions.SetStyleAction;
import uap.workflow.modeler.utils.EditorActions.StyleAction;
import uap.workflow.modeler.utils.EditorActions.StylesheetAction;
import uap.workflow.modeler.utils.EditorActions.ToggleAction;
import uap.workflow.modeler.utils.EditorActions.TogglePropertyItem;
import uap.workflow.modeler.utils.EditorActions.ZoomPolicyAction;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIMenu;
import nc.ui.pub.beans.UIMenuBar;

public class BpmnMenuBar extends UIMenuBar {

	@SuppressWarnings("serial")
	public BpmnMenuBar(final mxGraphComponent graphComponent) {

		final mxGraph graph = graphComponent.getGraph();
		JMenu menu = null;
		JMenu submenu = null;
		setBorder(BorderFactory.createLineBorder(new ColorUIResource(0x88, 0xA7, 0xC6), 1));

		// Creates the file menu
		menu = add(new UIMenu(getDisplayName("file")));

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("new"), "(Ctrl+N)", new NewAction(), "/com/mxgraph/examples/swing/images/new.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("open Process"), "(Ctrl+O)", new OpenAction(), "/com/mxgraph/examples/swing/images/open.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("Open From File"), "", new OpenFromFileAction(), "/com/mxgraph/examples/swing/images/open.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("Save and Deploy"), "(Ctrl+S)", new SaveAction(false), "/com/mxgraph/examples/swing/images/save.gif"));
		//menu.add(ActionFactory.bind(graphComponent,getDisplayName("save Draft"), "", new SaveAction(true), "/com/mxgraph/examples/swing/images/save.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("save as file"), "", new SaveFileAction(), "/com/mxgraph/examples/swing/images/save.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("pageSetup"),"", new PageSetupAction(), "/com/mxgraph/examples/swing/images/pagesetup.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("print"), "(Ctrl+P)", new PrintAction(), "/com/mxgraph/examples/swing/images/print.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("exit"), new ExitAction()));

		// Creates the edit menu
		menu = add(new UIMenu(getDisplayName("edit")));

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("undo"), "(Ctrl+Z)", new HistoryAction(true), "/com/mxgraph/examples/swing/images/undo.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("redo"), "(Ctrl+Y)", new HistoryAction(false), "/com/mxgraph/examples/swing/images/redo.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("cut"), "(Ctrl+X)", TransferHandler.getCutAction(), "/com/mxgraph/examples/swing/images/cut.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("copy"), "(Ctrl+C)", TransferHandler.getCopyAction(), "/com/mxgraph/examples/swing/images/copy.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("paste"), "(Ctrl+V)", TransferHandler.getPasteAction(), "/com/mxgraph/examples/swing/images/paste.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("delete"), "(Delete)", new DeleteAction("delete"), "/com/mxgraph/examples/swing/images/delete.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("selectAll"), "(Ctrl+A)", mxGraphActions.getSelectAllAction(), null));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("selectNone"), "(Ctrl+D)", mxGraphActions.getSelectNoneAction(), null));

		menu.addSeparator();

		// menu.add(editor.bind(getDisplayName("warning"), new
		// WarningAction()));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("edit"), mxGraphActions.getEditAction()));

		// Creates the view menu
		menu = add(new UIMenu(getDisplayName("view")));

		JMenuItem item = menu.add(new TogglePropertyItem(graphComponent, getDisplayName("pageLayout"), "PageVisible", true, new ActionListener() {
			/**
					 * 
					 */
			public void actionPerformed(ActionEvent e) {
				if (graphComponent.isPageVisible() && graphComponent.isCenterPage()) {
					graphComponent.zoomAndCenter();
				} else {
					graphComponent.getGraphControl().updatePreferredSize();
				}
			}
		}));

		item.addActionListener(new ActionListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof TogglePropertyItem) {
					TogglePropertyItem toggleItem = (TogglePropertyItem) e.getSource();

					if (toggleItem.isSelected()) {
						// Scrolls the view to the center
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								graphComponent.scrollToCenter(true);
								graphComponent.scrollToCenter(false);
							}
						});
					} else {
						// Resets the translation of the view
						mxPoint tr = graphComponent.getGraph().getView().getTranslate();

						if (tr.getX() != 0 || tr.getY() != 0) {
							graphComponent.getGraph().getView().setTranslate(new mxPoint());
						}
					}
				}
			}
		});

		menu.add(new TogglePropertyItem(graphComponent, getDisplayName("AntiAlias"), "AntiAlias", true));

		menu.addSeparator();

//		menu.add(new ToggleGridItem(editor, getDisplayName("grid")));
//		menu.add(new ToggleRulersItem(editor, getDisplayName("rulers")));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("zoom")));

		submenu.add(ActionFactory.bind(graphComponent,"400%", new ScaleAction(4)));
		submenu.add(ActionFactory.bind(graphComponent,"200%", new ScaleAction(2)));
		submenu.add(ActionFactory.bind(graphComponent,"150%", new ScaleAction(1.5)));
		submenu.add(ActionFactory.bind(graphComponent,"100%", new ScaleAction(1)));
		submenu.add(ActionFactory.bind(graphComponent,"75%", new ScaleAction(0.75)));
		submenu.add(ActionFactory.bind(graphComponent,"50%", new ScaleAction(0.5)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("custom"), new ScaleAction(1.0)));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("zoomIn"), "(Ctrl+Plus)", mxGraphActions.getZoomInAction(), null));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("zoomOut"), "(Ctrl+Sub)", mxGraphActions.getZoomOutAction(), null));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("page"), new ZoomPolicyAction(mxGraphComponent.ZOOM_POLICY_PAGE)));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("width"), new ZoomPolicyAction(mxGraphComponent.ZOOM_POLICY_WIDTH)));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("actualSize"), mxGraphActions.getZoomActualAction()));

		// Creates the format menu
		menu = add(new UIMenu(getDisplayName("format")));

		populateFormatMenu(menu, graphComponent);

		// Creates the shape menu
		menu = add(new UIMenu(getDisplayName("shape")));

		populateShapeMenu(menu, graphComponent);

		// Creates the diagram menu
		menu = add(new UIMenu(getDisplayName("diagram")));

//		menu.add(new ToggleOutlineItem(editor, getDisplayName("outline")));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("background")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("backgroundColor"), new BackgroundAction()));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("backgroundImage"), new BackgroundImageAction()));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("pageBackground"), new PageBackgroundAction()));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("grid")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("gridSize"), new PromptPropertyAction(graph, "Grid Size", "GridSize")));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("gridColor"), new GridColorAction()));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("dashed"), new GridStyleAction(mxGraphComponent.GRID_STYLE_DASHED)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("dot"), new GridStyleAction(mxGraphComponent.GRID_STYLE_DOT)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("line"), new GridStyleAction(mxGraphComponent.GRID_STYLE_LINE)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("cross"), new GridStyleAction(mxGraphComponent.GRID_STYLE_CROSS)));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("layout")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("verticalHierarchical"), BmpnLayoutFactory.graphLayout("verticalHierarchical",graphComponent)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("horizontalHierarchical"), BmpnLayoutFactory.graphLayout("horizontalHierarchical",graphComponent)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("verticalTree"), BmpnLayoutFactory.graphLayout("verticalTree",graphComponent)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("horizontalTree"), BmpnLayoutFactory.graphLayout("horizontalTree",graphComponent)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("organicLayout"), BmpnLayoutFactory.graphLayout("organicLayout",graphComponent)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("circleLayout"), BmpnLayoutFactory.graphLayout("circleLayout",graphComponent)));


		menu.addSeparator();

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("stylesheet")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("basicStyle"), new StylesheetAction("/com/mxgraph/examples/swing/resources/basic-style.xml")));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("defaultStyle"), new StylesheetAction("/com/mxgraph/examples/swing/resources/default-style.xml")));

		// Creates the options menu
		menu = add(new UIMenu(getDisplayName("options")));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("zoom")));

		submenu.add(new TogglePropertyItem(graphComponent, getDisplayName("centerZoom"), "CenterZoom", true));


		submenu.addSeparator();

		submenu.add(new TogglePropertyItem(graphComponent, getDisplayName("centerPage"), "CenterPage", true, new ActionListener() {
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				if (graphComponent.isPageVisible() && graphComponent.isCenterPage()) {
					graphComponent.zoomAndCenter();
				}
			}
		}));

		menu.addSeparator();
		submenu.addSeparator();
		menu.add(new TogglePropertyItem(graphComponent.getGraphHandler(), getDisplayName("imagePreview"), "ImagePreview"));
		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("labels")));

		submenu.add(new TogglePropertyItem(graph, getDisplayName("htmlLabels"), "HtmlLabels", true));
		submenu.add(new TogglePropertyItem(graph, getDisplayName("showLabels"), "LabelsVisible", true));

		submenu.addSeparator();

		submenu.add(new TogglePropertyItem(graph, getDisplayName("moveEdgeLabels"), "EdgeLabelsMovable"));
		submenu.add(new TogglePropertyItem(graph, getDisplayName("moveVertexLabels"), "VertexLabelsMovable"));

		menu.addSeparator();

		menu = add(new UIMenu(getDisplayName("help")));

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("aboutGraphEditor"), "(F1)", new HelpAction(), null));
	}

	/**
	 * Adds menu items to the given shape menu. This is factored out because the
	 * shape menu appears in the menubar and also in the popupmenu.
	 */
	public static void populateShapeMenu(JMenu menu,mxGraphComponent graphComponent) {
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("exitGroup"), "(PAGE_UP)", mxGraphActions.getExitGroupAction(), "/com/mxgraph/examples/swing/images/up.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("enterGroup"), "(PAGE_DOWN)", mxGraphActions.getEnterGroupAction(), "/com/mxgraph/examples/swing/images/down.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("group"), "(Ctrl+G)", mxGraphActions.getGroupAction(), "/com/mxgraph/examples/swing/images/group.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("ungroup"), "(Ctrl+U)", mxGraphActions.getUngroupAction(), "/com/mxgraph/examples/swing/images/ungroup.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("removeFromGroup"), mxGraphActions.getRemoveFromParentAction()));

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("updateGroupBounds"), mxGraphActions.getUpdateGroupBoundsAction()));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("collapse"), mxGraphActions.getCollapseAction(), "/com/mxgraph/examples/swing/images/collapse.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("expand"), mxGraphActions.getExpandAction(), "/com/mxgraph/examples/swing/images/expand.gif"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("toBack"), mxGraphActions.getToBackAction(), "/com/mxgraph/examples/swing/images/toback.gif"));
		menu.add(ActionFactory.bind(graphComponent,getDisplayName("toFront"), mxGraphActions.getToFrontAction(), "/com/mxgraph/examples/swing/images/tofront.gif"));

		menu.addSeparator();

		JMenu submenu = (JMenu) menu.add(new UIMenu(getDisplayName("align")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("alignleft"), new AlignCellsAction(mxConstants.ALIGN_LEFT), "/com/mxgraph/examples/swing/images/alignleft.png"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("alignvcenter"), new AlignCellsAction(mxConstants.ALIGN_CENTER), "/com/mxgraph/examples/swing/images/alignvcenter.png"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("alignright"), new AlignCellsAction(mxConstants.ALIGN_RIGHT), "/com/mxgraph/examples/swing/images/alignright.png"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("aligntop"), new AlignCellsAction(mxConstants.ALIGN_TOP), "/com/mxgraph/examples/swing/images/aligntop.png"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("alignhcenter"), new AlignCellsAction(mxConstants.ALIGN_MIDDLE), "/com/mxgraph/examples/swing/images/alignhcenter.png"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("alignbottom"), new AlignCellsAction(mxConstants.ALIGN_BOTTOM), "/com/mxgraph/examples/swing/images/alignbottom.png"));

		menu.addSeparator();

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("autosize"), new AutosizeAction()));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("direction")));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("horizontally"), new KeyValueAction(mxConstants.STYLE_HORIZONTAL, "true"), null));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("vertically"), new KeyValueAction(mxConstants.STYLE_HORIZONTAL, "false"), null));
	}

	/**
	 * Adds menu items to the given format menu. This is factored out because
	 * the format menu appears in the menubar and also in the popupmenu.
	 */
	public static void populateFormatMenu(JMenu menu,mxGraphComponent graphComponent) {
		JMenu submenu = (JMenu) menu.add(new UIMenu(getDisplayName("background")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("fillcolor"), new ColorAction("Fillcolor", mxConstants.STYLE_FILLCOLOR), "/com/mxgraph/examples/swing/images/fillcolor.png"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("gradient"), new ColorAction("Gradient", mxConstants.STYLE_GRADIENTCOLOR)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("image"), new PromptValueAction(mxConstants.STYLE_IMAGE, "Image")));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("shadow"), new ToggleAction(mxConstants.STYLE_SHADOW)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("opacity"), new PromptValueAction(mxConstants.STYLE_OPACITY, "Opacity (0-100)")));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("label")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("fontcolor"), new ColorAction("Fontcolor", mxConstants.STYLE_FONTCOLOR), "/com/mxgraph/examples/swing/images/fontcolor.png"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("labelFill"), new ColorAction("Label Fill", mxConstants.STYLE_LABEL_BACKGROUNDCOLOR)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("labelBorder"), new ColorAction("Label Border", mxConstants.STYLE_LABEL_BORDERCOLOR)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("rotateLabel"), new ToggleAction(mxConstants.STYLE_HORIZONTAL, true)));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("textOpacity"), new PromptValueAction(mxConstants.STYLE_TEXT_OPACITY, "Opacity (0-100)")));

		submenu.addSeparator();

		JMenu subsubmenu = (JMenu) submenu.add(new UIMenu(getDisplayName("position")));

		subsubmenu.add(ActionFactory.bind(graphComponent,getDisplayName("top"), new SetLabelPositionAction(mxConstants.ALIGN_TOP, mxConstants.ALIGN_BOTTOM)));
		subsubmenu.add(ActionFactory.bind(graphComponent,getDisplayName("middle"), new SetLabelPositionAction(mxConstants.ALIGN_MIDDLE, mxConstants.ALIGN_MIDDLE)));
		subsubmenu.add(ActionFactory.bind(graphComponent,getDisplayName("bottom"), new SetLabelPositionAction(mxConstants.ALIGN_BOTTOM, mxConstants.ALIGN_TOP)));

		subsubmenu.addSeparator();

		subsubmenu.add(ActionFactory.bind(graphComponent,getDisplayName("left"), new SetLabelPositionAction(mxConstants.ALIGN_LEFT, mxConstants.ALIGN_RIGHT)));
		subsubmenu.add(ActionFactory.bind(graphComponent,getDisplayName("center"), new SetLabelPositionAction(mxConstants.ALIGN_CENTER, mxConstants.ALIGN_CENTER)));
		subsubmenu.add(ActionFactory.bind(graphComponent,getDisplayName("right"), new SetLabelPositionAction(mxConstants.ALIGN_RIGHT, mxConstants.ALIGN_LEFT)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("hide"), new ToggleAction(mxConstants.STYLE_NOLABEL)));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("line")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("linecolor"), new ColorAction("Linecolor", mxConstants.STYLE_STROKECOLOR), "/com/mxgraph/examples/swing/images/linecolor.png"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("dashed"), new ToggleAction(mxConstants.STYLE_DASHED)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("linewidth"), new PromptValueAction(mxConstants.STYLE_STROKEWIDTH, "Linewidth")));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("connector")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("straight"), new SetStyleAction("straight"), "/com/mxgraph/examples/swing/images/straight.gif"));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("horizontal"), new SetStyleAction(""), "/com/mxgraph/examples/swing/images/connect.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("vertical"), new SetStyleAction("vertical"), "/com/mxgraph/examples/swing/images/vertical.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("entityRelation"), new SetStyleAction("edgeStyle=mxEdgeStyle.EntityRelation"), "/com/mxgraph/examples/swing/images/entity.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("arrow"), new SetStyleAction("arrow"), "/com/mxgraph/examples/swing/images/arrow.png"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("plain"), new ToggleAction(mxConstants.STYLE_NOEDGESTYLE)));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("linestart")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("open"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OPEN), "/com/mxgraph/examples/swing/images/open_start.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("classic"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_CLASSIC), "/com/mxgraph/examples/swing/images/classic_start.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("block"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_BLOCK), "/com/mxgraph/examples/swing/images/block_start.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("diamond"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_DIAMOND), "/com/mxgraph/examples/swing/images/diamond_start.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("oval"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OVAL), "/com/mxgraph/examples/swing/images/oval_start.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("none"), new KeyValueAction(mxConstants.STYLE_STARTARROW, mxConstants.NONE)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("size"), new PromptValueAction(mxConstants.STYLE_STARTSIZE, "Linestart Size")));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("lineend")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("open"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OPEN), "/com/mxgraph/examples/swing/images/open_end.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("classic"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC), "/com/mxgraph/examples/swing/images/classic_end.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("block"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK), "/com/mxgraph/examples/swing/images/block_end.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("diamond"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND), "/com/mxgraph/examples/swing/images/diamond_end.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("oval"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OVAL), "/com/mxgraph/examples/swing/images/oval_end.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("none"), new KeyValueAction(mxConstants.STYLE_ENDARROW, mxConstants.NONE)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("size"), new PromptValueAction(mxConstants.STYLE_ENDSIZE, "Linestart Size")));

		menu.addSeparator();

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("alignment")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("left"), new KeyValueAction(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT), "/com/mxgraph/examples/swing/images/left.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("center"), new KeyValueAction(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER), "/com/mxgraph/examples/swing/images/center.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("right"), new KeyValueAction(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_RIGHT), "/com/mxgraph/examples/swing/images/right.gif"));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("top"), new KeyValueAction(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_TOP), "/com/mxgraph/examples/swing/images/top.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("middle"), new KeyValueAction(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE), "/com/mxgraph/examples/swing/images/middle.gif"));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("bottom"), new KeyValueAction(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM), "/com/mxgraph/examples/swing/images/bottom.gif"));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("spacing")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("top"), new PromptValueAction(mxConstants.STYLE_SPACING_TOP, "Top Spacing")));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("right"), new PromptValueAction(mxConstants.STYLE_SPACING_RIGHT, "Right Spacing")));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("bottom"), new PromptValueAction(mxConstants.STYLE_SPACING_BOTTOM, "Bottom Spacing")));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("left"), new PromptValueAction(mxConstants.STYLE_SPACING_LEFT, "Left Spacing")));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("global"), new PromptValueAction(mxConstants.STYLE_SPACING, "Spacing")));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("sourceSpacing"), new PromptValueAction(mxConstants.STYLE_SOURCE_PERIMETER_SPACING, mxResources.get("sourceSpacing"))));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("targetSpacing"), new PromptValueAction(mxConstants.STYLE_TARGET_PERIMETER_SPACING, mxResources.get("targetSpacing"))));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("perimeter"), new PromptValueAction(mxConstants.STYLE_PERIMETER_SPACING, "Perimeter Spacing")));

		submenu = (JMenu) menu.add(new UIMenu(getDisplayName("direction")));

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("north"), new KeyValueAction(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_NORTH)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("east"), new KeyValueAction(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_EAST)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("south"), new KeyValueAction(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_SOUTH)));
		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("west"), new KeyValueAction(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_WEST)));

		submenu.addSeparator();

		submenu.add(ActionFactory.bind(graphComponent,getDisplayName("rotation"), new PromptValueAction(mxConstants.STYLE_ROTATION, "Rotation (0-360)")));

		menu.addSeparator();

		// menu.add(editor.bind(getDisplayName("rounded"), new ToggleAction(
		// mxConstants.STYLE_ROUNDED)));

		menu.add(ActionFactory.bind(graphComponent,getDisplayName("style"), new StyleAction()));
	}

	private static String getDisplayName(String name) {
		String str = NCLangRes.getInstance().getStrByID("pfgraph", name);
		return str;
	}
}
