package uap.workflow.modeler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import nc.bs.logging.Logger;
import nc.uap.ws.gen.util.StringUtil;
import nc.ui.ls.MessageBox;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.ExtTabbedPane;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISlider;
import nc.ui.pub.beans.UIToggleButton;
import nc.ui.pub.graph.itf.GraphObjectTreeRender;
import nc.ui.wfengine.designer.dock.DockableWindowManager;
import nc.vo.pub.graph.element.GraphActionEvent;

import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.designer.exports.BPMN20ExportMarshaller;
import uap.workflow.modeler.bpmn.commons.GraphListenerRepository;
import uap.workflow.modeler.bpmn.commons.GraphObjectViewer;
import uap.workflow.modeler.bpmn.editor.handler.BpmnRubberband;
import uap.workflow.modeler.bpmn.graph.itf.IGraphListenerClaim;
import uap.workflow.modeler.bpmn.listeners.BpmnAutoConnector;
import uap.workflow.modeler.bpmn.listeners.SliderMoveListener;
import uap.workflow.modeler.editors.CellTemplateSource;
import uap.workflow.modeler.editors.EditorKeyboardHandler;
import uap.workflow.modeler.editors.GraphActionListener;
import uap.workflow.modeler.editors.GraphObjectManager;
import uap.workflow.modeler.editors.Marker;
import uap.workflow.modeler.uecomponent.BpmnToolBar;
import uap.workflow.modeler.uecomponent.EdgePopupMenu;
import uap.workflow.modeler.uecomponent.ModelerPropertySheet;
import uap.workflow.modeler.uecomponent.VertexPopupMenu;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxGraphModel.mxChildChange;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;

public class BasicBpmnGraphEditor extends UIPanel implements GraphActionListener ,NotationGroupChange{
	private static final long serialVersionUID = 1L;

	protected mxGraphComponent graphComponent;

	protected mxGraphOutline graphOutline;

	protected JTabbedPane libraryPane;

	private String[] bizPersistPlugins;

	private String graphObjPersistPlugin;

	private UIPanel propertyEditorPane;

	private UIPanel objectTreePane;

	private UIPanel hintOutputPane;

	private JScrollPane hintScrollPane;

	private GraphObjectViewer objectTree;

	protected ModelerPropertySheet _sheetPage = null;

	private CellTemplateSource cellTempletelib;

	private static GraphObjectManager objectManager;
	
	private BpmnPalette palette = null;

	private BpmnAutoConnector autoConnector;

	private JTextArea hintText;

	private mxCell incomingCell;

	Marker startMarker;

	Marker endMarker;

	private BpmnGraphEditorFrame frame = null;

	protected mxUndoManager undoManager;

	protected String appTitle;

	protected String pkProcDef;

	protected JLabel statusBar;

	protected JPanel scalePanel;

	protected JPanel toolPanel;

	protected File currentFile;

	protected boolean modified = false;

	protected mxRubberband rubberband;

	private JSlider slider;

	private JLabel scaleLabel;

	protected mxKeyboardHandler keyboardHandler;

	protected DockableWindowManager dockablePanel;

	private final String OUTHINTMESSAGE = NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000000")/* 输出提示 */;

	private final String CELLLIB = NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditor-000000")/* 图元库 */;

	private final String PROPERTYSHEET = NCLangRes.getInstance().getStrByID("101203", "UPP101203-000046")/* 属性编辑器 */;

	private BpmnPalette shapesPalette;

	public BpmnPalette getShapesPalette() {
		return shapesPalette;
	}

	static {
		mxResources.add("com/mxgraph/examples/swing/resources/editor");
	}

	public Marker getStartMarker() {
		return startMarker;
	}

	public Marker getEndMarker() {
		return endMarker;
	}

	public ModelerPropertySheet getPropertySheetPage() {
		if (_sheetPage == null) {
			nc.ui.pub.graph.itf.PropertySheet sheetClaz = this.getClass().getAnnotation(nc.ui.pub.graph.itf.PropertySheet.class);
			try {
				_sheetPage = new ModelerPropertySheet();
				((ModelerPropertySheet) _sheetPage).addUIPropChangeListener(getObjectTree());
				((ModelerPropertySheet) _sheetPage).setGraphComponent(graphComponent);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return _sheetPage;
	}

	/**
	 * 结束CELL编辑状态
	 * */
	public void stopCellEditing() {
		getPropertySheetPage().stopCellEditing();
	}

	/**
	 * @return
	 */
	public GraphObjectViewer getObjectTree() {
		if (objectTree == null) {
			DefaultTreeCellRenderer render = null;
			GraphObjectTreeRender renderclaz = this.getClass().getAnnotation(GraphObjectTreeRender.class);
			if (renderclaz != null) {
				try {
					render = (DefaultTreeCellRenderer) Class.forName(renderclaz.value()).newInstance();
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}

			objectTree = new GraphObjectViewer(new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000002")/* 图元结构 */), graphComponent, render);

		}
		return objectTree;
	}

	/**
	 * 
	 */
	protected mxIEventListener undoHandler = new mxIEventListener() {
		public void invoke(Object source, mxEventObject evt) {
			undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
		}
	};

	/**
	 * 
	 */
	protected mxIEventListener changeTracker = new mxIEventListener() {
		public void invoke(Object source, mxEventObject evt) {
			setModified(true);
		}
	};

	private void installListeners() {
		// Updates the modified flag if the graph model changes
		graphComponent.getGraph().getModel().addListener(mxEvent.CHANGE, changeTracker);
		// Adds the command history to the model and view
		graphComponent.getGraph().getModel().addListener(mxEvent.UNDO, undoHandler);
		graphComponent.getGraph().getView().addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.REDO, undoHandler);
		// Display some useful information about repaint events
		installRepaintListener();
		installMouseListeners();
		new SliderMoveListener(createSlider(),graphComponent);
		shapesPalette.addListener(mxEvent.SELECT, new mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				Object tmp = evt.getProperty("transferable");
				if (tmp instanceof mxGraphTransferable) {
					mxGraphTransferable t = (mxGraphTransferable) tmp;
					Object cell = t.getCells()[0];
					if (graphComponent.getGraph().getModel().isEdge(cell)) {
//						((BpmnGraph) graphComponent.getGraph()).setEdgeTemplate(cell);
					}
				}
			}
		});
		shapesPalette.addListener(mxEvent.SELECT, (mxIEventListener)graphComponent);
	}

	private void createHandlers() {
		rubberband = new BpmnRubberband(graphComponent);
		keyboardHandler = new EditorKeyboardHandler(graphComponent);
		undoHandler = new mxIEventListener() {
			public void invoke(Object source, mxEventObject evt) {
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt.getProperty("edit")).getChanges();
				// undo同步GraphObjectManager的map
				for (mxUndoableChange change : changes) {
					if (change instanceof mxChildChange) {
//						String id = ((UfGraphCell) ((mxChildChange) change).getChild()).getId();
//						if (objectManager.getElementMap().containsKey(id)) {
//							objectManager.getElementMap().remove(id);
//						} else {
//							UfGraphCell cell = (UfGraphCell) ((mxChildChange) change).getChild();
//							GraphElementHolder holder = new GraphElementHolder();
//							holder.setId(id);
//							holder.setTargetid(cell.getTarget() == null ? null : cell.getTarget().getId());
//							holder.setSourceid(cell.getSource() == null ? null : cell.getSource().getId());
//							holder.setGeometry(cell.getGeometry());
//							holder.setParentid(cell.getParent() == null ? null : cell.getParent().getId());
//							holder.setUserobject(cell.getValue());
//							holder.setType(cell.isVertex() ? 0 : 1);
//							holder.setGraphstyle(cell.getStyle());
//							objectManager.getElementMap().put(id, holder);
//						}
					}
				}
			}
		};
	}

	private void BuildUI() {
		graphOutline = new mxGraphOutline(graphComponent);
		// 图元结构树
		objectTreePane = new UIPanel();
		objectTreePane.setLayout(new BorderLayout());
		UIScrollPane treescrollpane = new UIScrollPane();
		treescrollpane.setViewportView(getObjectTree());
		objectTreePane.setBorder(BorderFactory.createEtchedBorder());
		objectTreePane.add(treescrollpane, "Center");

		ExtTabbedPane leftBomTab = new ExtTabbedPane();
		leftBomTab.setOpaque(true);
		leftBomTab.addTab(NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000003")/* 缩略图 */, graphOutline);
		leftBomTab.addTab(NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000002")/* 图元结构 */, objectTreePane);
		libraryPane = new JTabbedPane();

		JSplitPane inner = new JSplitPane(JSplitPane.VERTICAL_SPLIT, libraryPane, leftBomTab);
		inner.setDividerLocation(540);
		inner.setResizeWeight(1);
		inner.setDividerSize(1);
		inner.setBorder(null);
		propertyEditorPane = new UIPanel();
		propertyEditorPane.setLayout(new BorderLayout());
		propertyEditorPane.add(getPropertySheetPage(), "Center");

		hintOutputPane = new UIPanel();
		hintScrollPane = new JScrollPane(hintOutputPane);
		hintOutputPane.setLayout(new BorderLayout());
		hintOutputPane.setBorder(BorderFactory.createEtchedBorder(1));
		JTextArea area = getTextHintArea();

		area.setEditable(false);
		hintOutputPane.add(area, "Center");
		// hintOutputPane.setPreferredSize(new
		// java.awt.Dimension(graphComponent.getWidth(),200));
		JSplitPane centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphComponent, hintScrollPane);
		centerPane.setDividerLocation(500);
		centerPane.setResizeWeight(1);
		centerPane.setDividerSize(6);
		centerPane.setBorder(null);

		JSplitPane inner1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerPane, propertyEditorPane);
		inner1.setDividerLocation(600);
		inner1.setResizeWeight(1);
		inner1.setDividerSize(6);
		inner1.setBorder(null);

		dockablePanel = new DockableWindowManager();
		dockablePanel.add(graphComponent);
		dockablePanel.addEntry("left", NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditor-000000")/* 图元库 */, inner);

		dockablePanel.getLeftDockingArea().getDockablePanel().setPreferredSize(new Dimension(220, 20));
		dockablePanel.getRightDockingArea().getDockablePanel().setPreferredSize(new Dimension(220, 20));
		dockablePanel.getBottomDockingArea().getDockablePanel().setPreferredSize(new Dimension(180, 20));

		dockablePanel.addEntry("right", NCLangRes.getInstance().getStrByID("101203", "UPP101203-000046")/*
																										 * @
																										 * res
																										 * "属性编辑器"
																										 */, propertyEditorPane);
		//
		// dockablePanel.addEntry("right", "图元结构", objectTreePane);
		dockablePanel.addEntry("bottom", OUTHINTMESSAGE, hintScrollPane);
		// dockablePanel.showDockableWindow("图元");
		// Creates the outer split pane that contains the inner split pane and
		// the graph component on the right side of the window
		JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inner, inner1);
		outer.setOneTouchExpandable(true);
		outer.setDividerLocation(200);
		outer.setDividerSize(6);
		outer.setBorder(null);
		statusBar = createStatusBar();
		UIPanel statusPanel = new UIPanel();
		statusPanel.setBorder(BorderFactory.createLineBorder(new ColorUIResource(0xF0, 0xF4, 0xF9), 1));
		statusPanel.setPreferredSize(new Dimension(1000, 35));
		statusPanel.setLayout(new BorderLayout());
		statusPanel.add(statusBar, BorderLayout.WEST);
		statusPanel.add(createToolPanel(), BorderLayout.EAST);
		// Puts everything together
		setLayout(new BorderLayout());
		// add(outer, BorderLayout.CENTER);
		UIPanel cpane = new UIPanel();
		cpane.setLayout(new BorderLayout());
		cpane.add(dockablePanel, BorderLayout.CENTER);
		// cpane.setBorder(BorderFactory.createEtchedBorder(1));

		add(cpane, BorderLayout.CENTER);// /
		cpane.setBorder(BorderFactory.createLineBorder(new ColorUIResource(0x88, 0xA7, 0xC6), 1));

		add(statusPanel, BorderLayout.SOUTH);
		installToolBar();
		startMarker = new Marker(graphComponent, Color.MAGENTA, true);
		endMarker = new Marker(graphComponent, Color.MAGENTA, false);
		shapesPalette = insertPalette(appTitle,NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000000")/* 流程元素 */);
	}

	/**
	 * 
	 */
	public BasicBpmnGraphEditor(String appTitle, mxGraphComponent component, String pkProcDef) {
		// Stores and updates the frame title
		this.appTitle = appTitle;
		this.pkProcDef = pkProcDef;
		graphComponent = component;
		graphComponent.setGridColor(new Color(53, 103, 153));
		undoManager = new mxUndoManager();
		autoConnector = new BpmnAutoConnector(graphComponent, this);
		objectManager = new GraphObjectManager(graphComponent, pkProcDef);
		objectManager.addGraphActionListener(this);
		createHandlers();
		BuildUI();
		installListeners();

		// dependency injection by annotations
		//BizGraphPersister bizPeristers = this.getClass().getAnnotation(BizGraphPersister.class);
		//GraphObjPersister graphPesister = this.getClass().getAnnotation(GraphObjPersister.class);
		//if (bizPeristers != null) {
		//	bizPersistPlugins = bizPeristers.value();
		//}
		//if (graphPesister != null) {
		//	graphObjPersistPlugin = graphPesister.value();
		//}

		//objectManager.initGraphData(graphObjPersistPlugin);

		//getPropertySheetPage().setGraphOjbect(objectManager.getGraphObject());
		_sheetPage.selectMainProcessCell();
		status(NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000004")/** 开始* ...* 拖动图元进行绘图*/);
	}

	public void showGraph() {
		objectManager.buildGraph();
	}

	private JTextArea getTextHintArea() {
		if (hintText == null)
			hintText = new JTextArea();
		return hintText;
	}


	private void installToolBar() {
		add(new BpmnToolBar(graphComponent, JToolBar.HORIZONTAL), BorderLayout.NORTH);
	}

	/**
	 * 
	 */
	private JLabel createStatusBar() {
		JLabel statusBar = new JLabel(mxResources.get("ready"));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		return statusBar;
	}

	private JSlider createSlider() {
		if (slider == null) {
			int defaultvalue = (int) (graphComponent.getGraph().getView().getScale() * 100);
			slider = new UISlider(0, 0, 400, defaultvalue);
			slider.setMajorTickSpacing(100);
			slider.setPreferredSize(new Dimension(130, 20));
			slider.setSize(new Dimension(130, 20));
			slider.setOpaque(false);
			slider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					int scale = slider.getValue();
					createScaleLabel().setText(scale + "%");
				}

			});
		}
		return slider;
	}

	private JLabel createScaleLabel() {
		if (scaleLabel == null) {
			scaleLabel = new UILabel();
			int initvalue = (int) (graphComponent.getGraph().getView().getScale() * 100);
			scaleLabel.setText(initvalue + "%");
			scaleLabel.setSize(new Dimension(35, 20));
			scaleLabel.setPreferredSize(new Dimension(35, 20));
		}
		return scaleLabel;
	}

	protected JPanel createScalePanel() {
		if (scalePanel == null) {
			scalePanel = new UIPanel();
			SpringLayout layout = new SpringLayout();
			UIButton left_bt = new UIButton();
			ImageIcon zoomout_normal = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/zoomout-normal.gif"));
			ImageIcon zoomout_click = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/zoomout-click.gif"));
			ImageIcon zoomout_hover = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/zoomout-hover.gif"));
			left_bt.setIcon(zoomout_normal);
			left_bt.setRolloverIcon(zoomout_hover);
			left_bt.setPressedIcon(zoomout_click);
			left_bt.setSize(new Dimension(zoomout_normal.getIconWidth(), zoomout_normal.getIconHeight()));
			left_bt.setPreferredSize(new Dimension(zoomout_normal.getIconWidth(), zoomout_normal.getIconHeight()));
			left_bt.setOpaque(false);
			left_bt.setBorder(BorderFactory.createEmptyBorder());

			UIButton right_bt = new UIButton();
			ImageIcon zoomin_normal = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/zoomin-normal.gif"));
			ImageIcon zoomin_click = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/zoomin-click.gif"));
			ImageIcon zoomin_hover = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/zoomin-hover.gif"));

			right_bt.setIcon(zoomin_normal);
			right_bt.setRolloverIcon(zoomin_hover);
			right_bt.setPressedIcon(zoomin_click);
			right_bt.setSize(new Dimension(zoomin_normal.getIconWidth(), zoomin_normal.getIconHeight()));
			right_bt.setPreferredSize(new Dimension(zoomin_normal.getIconWidth(), zoomin_normal.getIconHeight()));
			right_bt.setOpaque(false);
			right_bt.setBorder(BorderFactory.createEmptyBorder());
			left_bt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					int value = createSlider().getValue() - 5 < 0 ? 0 : createSlider().getValue() - 5;
					createSlider().setValue(value);
					graphComponent.zoomTo(value / 100.0, graphComponent.isCenterZoom());
					graphComponent.setPageVisible(false);
				}
			});

			right_bt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int value = createSlider().getValue() + 5 > 400 ? 400 : createSlider().getValue() + 5;
					createSlider().setValue(value);
					graphComponent.zoomTo(value / 100.0, graphComponent.isCenterZoom());
					graphComponent.setPageVisible(false);
				}

			});
			scalePanel.add(createScaleLabel());
			scalePanel.add(left_bt);
			scalePanel.add(createSlider());
			scalePanel.add(right_bt);
			layout.putConstraint(SpringLayout.WEST, scalePanel, 2, SpringLayout.WEST, createScaleLabel());
			layout.putConstraint(SpringLayout.WEST, createScaleLabel(), 2, SpringLayout.WEST, left_bt);
			layout.putConstraint(SpringLayout.EAST, createScaleLabel(), 1, SpringLayout.WEST, createSlider());
			layout.putConstraint(SpringLayout.EAST, createSlider(), 1, SpringLayout.WEST, right_bt);
			layout.putConstraint(SpringLayout.EAST, scalePanel, 2, SpringLayout.EAST, right_bt);

			scalePanel.setSize(new Dimension(220, 30));
			scalePanel.setPreferredSize(new Dimension(220, 30));
		}
		return scalePanel;
	}

	private JPanel createToolPanel() {
		if (toolPanel == null) {
			toolPanel = new UIPanel();
			toolPanel.setLayout(new BorderLayout());
			ImageIcon view1_normal = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/view1-normal.gif"));
			ImageIcon view1_click = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/view1-focus.gif"));
			ImageIcon view1_hover = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/view1-hover.gif"));

			ImageIcon view2_normal = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/view2-normal.gif"));
			ImageIcon view2_click = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/view2-focus.gif"));
			ImageIcon view2_hover = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/view2-hover.gif"));

			ImageIcon fitimage_normal = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/fitimage-normal.gif"));
			ImageIcon fitimage_click = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/fitimage-click.gif"));
			ImageIcon fitimage_hover = new ImageIcon(BasicBpmnGraphEditor.class.getResource("/com/mxgraph/examples/swing/images/fitimage-hover.gif"));
			UIToggleButton fitimgtb = new UIToggleButton(fitimage_normal);
			fitimgtb.setRolloverIcon(fitimage_hover);
			fitimgtb.setSelectedIcon(fitimage_click);
			fitimgtb.setPreferredSize(new Dimension(fitimage_normal.getIconWidth(), fitimage_normal.getIconHeight()));
			fitimgtb.setSize(new Dimension(fitimage_normal.getIconWidth(), fitimage_normal.getIconHeight()));
			fitimgtb.setOpaque(false);
			fitimgtb.setBorder(BorderFactory.createEmptyBorder());
			fitimgtb.setToolTipText(NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000005")/* 最合适大小 */);

			UIToggleButton verw1tb = new UIToggleButton();
			verw1tb.setIcon(view1_normal);
			verw1tb.setSelectedIcon(view1_click);
			verw1tb.setRolloverIcon(view1_hover);
			verw1tb.setSize(view1_normal.getIconWidth(), view1_normal.getIconHeight());
			verw1tb.setPreferredSize(new Dimension(view1_normal.getIconWidth(), view1_normal.getIconHeight()));
			verw1tb.setOpaque(false);
			verw1tb.setBorder(BorderFactory.createEmptyBorder());
			verw1tb.setToolTipText(NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000006")/* 打开侧边栏 */);
			// 默认打开侧边栏
			verw1tb.setSelected(true);

			UIToggleButton verw2tb = new UIToggleButton();
			verw2tb.setIcon(view2_normal);
			verw2tb.setSelectedIcon(view2_click);
			verw2tb.setRolloverIcon(view2_hover);
			verw2tb.setSize(view2_normal.getIconWidth(), view2_normal.getIconHeight());
			verw2tb.setPreferredSize(new Dimension(view2_normal.getIconWidth(), view2_normal.getIconHeight()));
			verw2tb.setOpaque(false);
			verw2tb.setBorder(BorderFactory.createEmptyBorder());
			verw2tb.setToolTipText(NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000007")/* 隐藏侧边栏 */);

			ButtonGroup bg = new ButtonGroup();
			bg.add(verw2tb);
			bg.add(verw1tb);

			JPanel panel0 = new UIPanel();
			panel0.add(verw1tb);
			panel0.add(verw2tb);

			JPanel panel2 = new UIPanel();
			panel2.add(fitimgtb);

			toolPanel.add(panel0, BorderLayout.WEST);
			toolPanel.add(createScalePanel(), BorderLayout.CENTER);
			toolPanel.add(panel2, BorderLayout.EAST);

			verw1tb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 打开侧边栏
					dockablePanel.showDockableWindow(CELLLIB);
					dockablePanel.showDockableWindow(PROPERTYSHEET);
				}

			});

			verw2tb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 隐藏侧边栏
					dockablePanel.hideDockableWindow(CELLLIB);
					dockablePanel.hideDockableWindow(PROPERTYSHEET);
				}

			});

			fitimgtb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					graphComponent.setPageVisible(true);
					graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
					createSlider().getModel().setValue((int) (graphComponent.getGraph().getView().getScale() * 100));
					createScaleLabel().setText((int) (graphComponent.getGraph().getView().getScale() * 100) + "%");
				}
			});
		}
		return toolPanel;
	}

	/**
	 * 
	 */
	private void installRepaintListener() {
		graphComponent.getGraph().addListener(mxEvent.REPAINT, new mxIEventListener() {
			public void invoke(Object source, mxEventObject evt) {
				String buffer = (graphComponent.getTripleBuffer() != null) ? "" : " (unbuffered)";
				mxRectangle dirty = (mxRectangle) evt.getProperty("region");

				if (dirty == null) {
					status("Repaint all" + buffer);
				} else {
					status("Repaint: x=" + (int) (dirty.getX()) + " y=" + (int) (dirty.getY()) + " w=" + (int) (dirty.getWidth()) + " h=" + (int) (dirty.getHeight()) + buffer);
				}
			}
		});
	}

	/**
	 * 
	 */
	public BpmnPalette insertPalette(String appTitle,String title) {
		JPanel paletteContainer = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		paletteContainer.setLayout(layout);
		
		NotationGroup naotationGroup = new NotationGroup(); 
		naotationGroup.insertButtons(appTitle,paletteContainer, layout);
		naotationGroup.addListener(this);

		palette = new BpmnPalette(getCellTempletelib());
		
		final JScrollPane scrollPane = new UIScrollPane(palette);
		
		paletteContainer.add(scrollPane, BorderLayout.CENTER);

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		libraryPane.addTab(title, paletteContainer);
		// Updates the widths of the palettes if the container size changes
		libraryPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int w = scrollPane.getWidth() - scrollPane.getVerticalScrollBar().getWidth();
				palette.setPreferredWidth(w);
			}

		});

		return palette;
	}

	/**
	 * 
	 */
	protected void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			graphComponent.zoomIn();
		} else {
			graphComponent.zoomOut();
		}

		status(mxResources.get("scale") + ": " + (int) (100 * graphComponent.getGraph().getView().getScale()) + "%");
	}

	/**
	 * 
	 */
	protected void showOutlinePopupMenu(MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(mxResources.get("magnifyPage"));
		item.setSelected(graphOutline.isFitPage());

		item.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				graphOutline.setFitPage(!graphOutline.isFitPage());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item2 = new JCheckBoxMenuItem(mxResources.get("showLabels"));
		item2.setSelected(graphOutline.isDrawLabels());

		item2.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				graphOutline.setDrawLabels(!graphOutline.isDrawLabels());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item3 = new JCheckBoxMenuItem(mxResources.get("buffering"));
		item3.setSelected(graphOutline.isTripleBuffered());

		item3.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				graphOutline.setTripleBuffered(!graphOutline.isTripleBuffered());
				graphOutline.repaint();
			}
		});

		JPopupMenu menu = new JPopupMenu();
		menu.add(item);
		menu.add(item2);
		menu.add(item3);
		menu.show(graphComponent, pt.x, pt.y);

		e.consume();
	}

	private void showPopupMenu(mxICell cell, MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
		JPopupMenu menu = null;
		if (cell == null) {

		} else if (cell.isEdge()) {
			menu = new EdgePopupMenu(graphComponent);
		} else {
			menu = new VertexPopupMenu(graphComponent);
		}
		if(menu != null)
			menu.show(graphComponent, pt.x, pt.y);
		e.consume();
	}

	protected void mouseLocationChanged(MouseEvent e) {
		status(e.getX() + ", " + e.getY());
	}

	private void installMouseListeners() {
		// Installs mouse wheel listener for zooming
		MouseWheelListener wheelTracker = new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
					BasicBpmnGraphEditor.this.mouseWheelMoved(e);
				}
			}

		};

		// Handles mouse wheel events in the outline and graph component
		graphOutline.addMouseWheelListener(wheelTracker);
		graphComponent.addMouseWheelListener(wheelTracker);

		// Installs the popup menu in the outline
		graphOutline.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {

				mouseReleased(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showOutlinePopupMenu(e);
				}
			}

		});

		// Installs the popup menu in the graph component
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Handles context menu on the Mac where the trigger is on
				// mousepressed
				mouseReleased(e);
			}

			/**
			 * 
			 */
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					Object cell = graphComponent.getCellAt(e.getX(), e.getY());
					showPopupMenu((mxICell) cell, e);

				}
			}
		});

		// Installs a mouse motion listener to display the mouse location
		graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				mouseLocationChanged(e);// /
			}

			public void mouseMoved(MouseEvent e) {
				mouseLocationChanged(e);
			}

		});

		GraphListenerRepository.getInstance().installListeners(
				graphComponent.getGraph(),new IGraphListenerClaim[] { 
				getObjectTree(), 
				(IGraphListenerClaim) getPropertySheetPage(), 
				objectManager, 
				autoConnector/*,
				((BpmnGraphComponent)graphComponent).getMemeryModel()*/ });
		GraphListenerRepository.getInstance().installSelectionListeners(
				graphComponent.getGraph(), 
				new IGraphListenerClaim[] { (IGraphListenerClaim) getPropertySheetPage()/*,
				((BpmnGraphComponent)graphComponent).getMemeryModel()*/ });
	}

	/**
	 * 
	 */
	public void setCurrentFile(File file) {
		File oldValue = currentFile;
		currentFile = file;

		firePropertyChange("currentFile", oldValue, file);

		if (oldValue != file) {
		}
	}

	public File getCurrentFile() {
		return currentFile;
	}

	/**
	 * 
	 * @param modified
	 */
	public void setModified(boolean modified) {
		boolean oldValue = this.modified;
		this.modified = modified;
		firePropertyChange("modified", oldValue, modified);
		if (oldValue != modified) {
		}
	}

	public boolean isModified() {
		return modified;
	}

	public mxGraphComponent getGraphComponent() {
		return graphComponent;
	}


	public mxGraphOutline getGraphOutline() {
		return graphOutline;
	}


	public mxUndoManager getUndoManager() {
		return undoManager;
	}

	public void status(String msg) {
		statusBar.setText(msg);
	}


	/**
	 * 
	 */
	public void about() {
		MessageDialog
				.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000008")/* 关于本软件 */, NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000009")/*
																																																 * UFIDA
																																																 * -
																																																 * NC
																																																 * -
																																																 * UAP
																																																 * 图形编辑器
																																																 */);
	}


	public void exit() {
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null) {
			frame.dispose();
		}
	}

	
	public UIDialog createDialog(JMenuBar menuBar, Container parent) {
		return null;
	}
	
	
	private static boolean valid(Process process) 
	{
		boolean valid=true;
		if (StringUtil.isEmptyOrNull(process.getObjectType()))
		{
			MessageBox.showMessageDialog("提示", "对象类型不能为空。");
			valid=false;
		}
		return valid;
	}
	public void onSave() {
		stopCellEditing();
		BPMN20ExportMarshaller marshaller = new BPMN20ExportMarshaller();

		Bpmn2MemoryModel bpmnModel=Bpmn2MemoryModel.constructOutputModel(this.getGraphComponent());
		if(!valid(bpmnModel.getMainProcess()))
			bpmnModel=null;
		marshaller.marshallDiagram(bpmnModel, "", false, false);

		
		
		
//		getObjectManager().persistGraph(getGraphObjPersistPlugin(), getBizPersistPlugins());
//		setModified(false);
		
//		IDiagramTypeProvider diagramTypeProvider = getDiagramTypeProvider();
//		/*     */     try
//		/*     */     {
//		/* 169 */       String diagramFileString = "/modelerDebug/src/main/resources/diagrams/MyProcess.bpmn";
//		/*     */ 
//		/* 171 */       
//		/* 172 */       marshaller.marshallDiagram(ModelHandler.getModel(EcoreUtil.getURI(getDiagramTypeProvider().getDiagram())), 
//		/* 173 */         diagramFileString, diagramTypeProvider.getFeatureProvider());
//		/*     */ 
//		/* 175 */       this.modelFile.refreshLocal(2, null);
//		/*     */     }
//		/*     */     catch (Exception e)
//		/*     */     {
//		/* 179 */       e.printStackTrace();
//		/*     */     }
//		/*     */ 
//		/* 182 */     ((BasicCommandStack)getEditingDomain().getCommandStack()).saveIsDone();
//		/* 183 */     updateDirtyState();
	}

	public JFrame getFrame() {
		return frame;
	}

	public CellTemplateSource getCellTempletelib() {
		if (cellTempletelib == null)
			cellTempletelib = new CellTemplateSource();
		return cellTempletelib;
	}

	public static GraphObjectManager getObjectManager() {
		return objectManager;
	}

	public static void setObjectManager(GraphObjectManager objectManager2) {
		objectManager = objectManager2;
	}

	public String[] getBizPersistPlugins() {
		return bizPersistPlugins;
	}

	public String getGraphObjPersistPlugin() {
		return graphObjPersistPlugin;
	}

	@Override
	public void fireEvent(GraphActionEvent event) {
		if (event.getType().equals("save") || event.getType().equals("error")) {
			//之前的信息要清空
			getTextHintArea().setText("");
			getTextHintArea().append(event.getHint());
			//输出面板自动展开
			dockablePanel.showDockableWindow(OUTHINTMESSAGE);
		}
	}

	public mxCell getIncomingCell() {
		return incomingCell;
	}

	public void setIncomingCell(mxCell incomingCell) {
		this.incomingCell = incomingCell;

		if (incomingCell != null) {
			startMarker.setRangeBounds(incomingCell.getGeometry());
			endMarker.setRangeBounds(incomingCell.getGeometry());
		}
	}

	@Override
	public void groupSelectedChange(String preGroup, String currentGroup) {
		palette.displayNotation(preGroup, currentGroup);
	}
}
