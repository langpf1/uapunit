package uap.workflow.modeler.bpmn.graph;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.TransferHandler;
import nc.vo.pub.guid.UUID;

import org.w3c.dom.Document;

import uap.workflow.bpmn2.model.Process;
import uap.workflow.modeler.BpmnModeler;
import uap.workflow.modeler.bpmn.editor.handler.BpmnConnectionHandler;
import uap.workflow.modeler.bpmn.editor.handler.BpmnGraphHandler;
import uap.workflow.modeler.bpmn.editor.handler.BpmnGraphTransferHandler;
import uap.workflow.modeler.bpmn.editor.handler.BpmnInsertHandler;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.handler.mxInsertHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.swing.view.mxICellEditor;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class BpmnGraphComponent extends mxGraphComponent implements mxIEventListener {
	private static final long serialVersionUID = 4562194109753546979L;
	
	public static final ThreadLocal<BpmnGraphComponent> graphComponentObject = new ThreadLocal<BpmnGraphComponent>();
	public static BpmnGraphComponent getGraphComponentObject() {
		return graphComponentObject.get();
	}
	public static void setGraphComponentObject(BpmnGraphComponent graphComponent) {
		graphComponentObject.set(graphComponent);
	}
	public static void removeFormSession() {
		graphComponentObject.remove();
	}

	
	private mxICell selectedEntry = null;
	private Process mainProcess = null;
	public BpmnGraphComponent(mxGraph graph) {
		super(graph);
		initDefaultStyle();
		mainProcess = new Process();
		mainProcess.setName("mainProcess");
		mainProcess.setId(java.util.UUID.randomUUID().toString());
		((mxCell)graph.getDefaultParent()).setValue(mainProcess);
	}
	public Process getMainProcess() {
		return (Process)((mxCell)graph.getDefaultParent()).getValue();
	}
	public mxICell getSelectedEntry() {
		return selectedEntry;
	}
	protected void createHandlers() {
		super.createHandlers();
		createInsertHandler();
	}
	protected mxICellEditor createCellEditor() {
		return new BpmnCellEditor(this);
	}
	@Override
	public Object getCellAt(int x, int y, boolean hitSwimlaneContent, Object parent) {
		//return super.getCellAt(x, y, hitSwimlaneContent, parent);
		
		///*
		if (parent == null)
		{
			parent = graph.getDefaultParent();
		}

		if (parent != null)
		{
			Point previousTranslate = canvas.getTranslate();
			double previousScale = canvas.getScale();

			try
			{
				canvas.setScale(graph.getView().getScale());
				canvas.setTranslate(0, 0);

				mxIGraphModel model = graph.getModel();
				mxGraphView view = graph.getView();

				Rectangle hit = new Rectangle(x, y, 1, 1);
				int childCount = model.getChildCount(parent);

				for (int i = childCount - 1; i >= 0; i--)
				{
					Object cell = model.getChildAt(parent, i);
					Object result = getCellAt(x, y, hitSwimlaneContent, cell);

					if (result != null)
					{
						return result;
					}
					else if (graph.isCellVisible(cell))
					{
						mxCellState state = view.getState(cell);

						if (state != null
								&& canvas.intersects(this, hit, state)
								&& (!graph.isSwimlane(cell) || hitSwimlaneContent || 
										(transparentSwimlaneContent && !canvas
										.hitSwimlaneContent(this, state, x, y))))
						{
							return cell;
						}
					}
				}
			}
			finally
			{
				canvas.setScale(previousScale);
				canvas.setTranslate(previousTranslate.x, previousTranslate.y);
			}
		}

		return null;	
		//*/
	}
	protected mxInsertHandler createInsertHandler() {
		return new BpmnInsertHandler(this, "");
	}
	private void initDefaultStyle() {
		setPageVisible(false);
		setGridVisible(true);
		setToolTips(true);
		setTextAntiAlias(false);
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(BpmnModeler.class.getResource("/com/mxgraph/examples/swing/resources/default-style.xml").toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		getViewport().setOpaque(false);
		setBackground(Color.WHITE);
	}
	public mxInteractiveCanvas createCanvas() {
		return new SwingCanvas();
	}
	protected mxConnectionHandler createConnectionHandler() {
		return new BpmnConnectionHandler(this);
	}
	protected TransferHandler createTransferHandler() {
		return new BpmnGraphTransferHandler();
	}
	protected mxGraphHandler createGraphHandler() {
		return new BpmnGraphHandler(this);
	}
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		Object tmp = evt.getProperty("transferable");
		if (tmp instanceof mxGraphTransferable) {
			mxGraphTransferable t = (mxGraphTransferable) tmp;
			Object[] cloneCell = getGraph().cloneCells(t.getCells());
			selectedEntry = (mxICell) cloneCell[0];
			// 连接线不允许拖拽,select不允许拖拽
			//setImportEnabled(selectedEntry.isVertex() && selectedEntry.getValue() != null);
		}
	}
}
