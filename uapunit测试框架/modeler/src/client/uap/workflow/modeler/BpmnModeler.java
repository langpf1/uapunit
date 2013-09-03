package uap.workflow.modeler;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.TooManyListenersException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import uap.workflow.bpmn2.model.SubProcess;
import uap.workflow.bpmn2.model.event.Event;
import uap.workflow.modeler.bpmn.graph.BpmnGraph;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.uecomponent.BpmnCellLib;
import uap.workflow.modeler.uecomponent.GraphElementMeta;
import uap.workflow.modeler.uecomponent.IGraphCellLib;
import uap.workflow.modeler.uecomponent.UfGraphCell;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.flowdesigner.GraphEditor;
import nc.ui.pub.graph.itf.BizGraphPersister;
import nc.ui.pub.graph.itf.CellLib;
import nc.ui.pub.graph.itf.GraphObjPersister;
import nc.ui.pub.graph.itf.GraphObjectTreeRender;
import nc.ui.pub.graph.itf.PropertySheet;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
@PropertySheet("uap.workflow.modeler.uecomponent.BpmnPropertySheet")
@CellLib("uap.workflow.modeler.uecomponent.BpmnCellLib")
@GraphObjectTreeRender("nc.ui.wfengine.designer.BuziflowObjectTreeCellRender")
@GraphObjPersister("")
@BizGraphPersister({ "", "" })
public class BpmnModeler extends BasicBpmnGraphEditor {
	private static final long serialVersionUID = -4618229604523998201L;
	public BpmnModeler() {
		this("ActivitiModeler", new BpmnGraphComponent(new BpmnGraph()), null);
	}
	public BpmnModeler(String pkProcDef) {
		this("ActivitiModeler", new BpmnGraphComponent(new BpmnGraph()), pkProcDef);
	}
	public BpmnModeler(String appTitle,String pkProcDef) {
		this(appTitle, new BpmnGraphComponent(new BpmnGraph()), pkProcDef);
	}
	public BpmnModeler(String appTitle, mxGraphComponent component, String pkProcDef) {
		super(appTitle, component, pkProcDef);
		installDefaults();
		loadCellOnPalette(appTitle);
		installDropTargetListener();
	}
	private void installDefaults() {
		mxConstants.DEFAULT_IMAGESIZE = 16;
		getUndoManager().clear();
		graphComponent.setConnectable(true);
		dockablePanel.showDockableWindow(NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditor-000000")/* 图元库 */);
		dockablePanel.showDockableWindow(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000046")/** @* res* "属性编辑器"*/);
	}
	private void loadCellOnPalette(String appTitle) {
		String libclaz=null;
		if("ApproveModeler".equals(appTitle))
			libclaz="uap.workflow.modeler.uecomponent.ApproveCellLib";   //流程编辑器为审批版
		else 
		 {
			CellLib lib = this.getClass().getAnnotation(CellLib.class);
		    libclaz = lib.value();
		 }
		int count = 0;
		try {
			IGraphCellLib o = (IGraphCellLib) Class.forName(libclaz).newInstance();
			GraphElementMeta[] metas = o.getShapeCellsMeta();
			for (GraphElementMeta meta : metas) {
				UfGraphCell cell = null;
				if (!meta.isVertex()) {
					// 线
					mxGeometry geometry = new mxGeometry(0, 0, meta.getWidth(), meta.getHeight());
					geometry.setTerminalPoint(new mxPoint(0, meta.getHeight()), true);
					geometry.setTerminalPoint(new mxPoint(meta.getWidth(), 0), false);
					geometry.setRelative(true);
					cell = new UfGraphCell(null, geometry, meta.getStyle(), meta);
					cell.setStyle(meta.getStyle());
					getShapesPalette().addToTemplate(cell, meta.getName(), new ImageIcon(GraphEditor.class.getResource(meta.getImageURL())), meta.getNotationGroup());
				} else {
					// 顶点
					cell = new UfGraphCell(meta);
					getShapesPalette().addToTemplate(cell, meta.getName(), new ImageIcon(GraphEditor.class.getResource(meta.getImageURL())), meta.getNotationGroup());
					if (meta.getNotationGroup().equals(BpmnCellLib.NOTATION_STARTEVENT) || meta.getNotationGroup().equals(BpmnCellLib.NOTATION_ENDEVENT)
							|| meta.getNotationGroup().equals(BpmnCellLib.NOTATION_INTERMEDIATIONCATCHING) || meta.getNotationGroup().equals(BpmnCellLib.NOTATION_INTERMEDIATIONTHROWING)) {
						Event event = (Event) cell.getValue();
						event.ConstructDefinition(meta.getSubClass());
					}else if (meta.getName().equals("Event Subprocess")){
						SubProcess subProcess = (SubProcess) cell.getValue();
						subProcess.setTriggeredByEvent(true);						
					}/*else if (meta.getName().equals("Event Subprocess Collapsed")){
						CallActivity callActivity = (CallActivity) cell.getValue();
						callActivity.setTriggeredByEvent(true);						
					}*/					
				}
				cell.setVertex(meta.isVertex());
				cell.setEdge(!meta.isVertex());
				count++;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}
	private void installDropTargetListener() {
		DropTarget dropTarget = graphComponent.getDropTarget();
		if (dropTarget != null) {
			try {
				dropTarget.addDropTargetListener(new DropTargetListener() {
					public Point convertPoint(Point pt) {
						pt = SwingUtilities.convertPoint(graphComponent, pt, graphComponent.getGraphControl());
						pt.x -= graphComponent.getHorizontalScrollBar().getValue();
						pt.y -= graphComponent.getVerticalScrollBar().getValue();
						return pt;
					}
					public MouseEvent createEvent(DropTargetEvent e) {
						JComponent component = (JComponent) e.getDropTargetContext().getComponent();
						Point location = null;
						int action = 0;
						if (e instanceof DropTargetDropEvent) {
							location = ((DropTargetDropEvent) e).getLocation();
							action = ((DropTargetDropEvent) e).getDropAction();
						} else if (e instanceof DropTargetDragEvent) {
							location = ((DropTargetDragEvent) e).getLocation();
							action = ((DropTargetDragEvent) e).getDropAction();
						}
						if (location != null) {
							location = convertPoint(location);
							Rectangle r = graphComponent.getViewport().getViewRect();
							location.translate(r.x, r.y);
						}
						int mod = (action == TransferHandler.COPY) ? InputEvent.CTRL_MASK : 0;
						return new MouseEvent(component, 0, System.currentTimeMillis(), mod, (int) location.getX(), (int) location.getY(), 1, false, MouseEvent.BUTTON1);
					}
					@Override
					public void dragEnter(DropTargetDragEvent dtde) {}
					@Override
					public void dragExit(DropTargetEvent dte) {}
					@Override
					public void dragOver(DropTargetDragEvent dtde) {
						if (getIncomingCell() != null && getIncomingCell().isEdge()) {
							MouseEvent e = createEvent(dtde);
							getStartMarker().process(e);
							getEndMarker().process(e);
						}
					}
					@Override
					public void drop(DropTargetDropEvent dtde) {
						getStartMarker().unmark();
						getEndMarker().unmark();
						setIncomingCell(null);
					}
					@Override
					public void dropActionChanged(DropTargetDragEvent dtde) {}
				});
			} catch (TooManyListenersException tmle) {
				Logger.error(tmle.getMessage(), tmle);
			}
		}
	}
}
