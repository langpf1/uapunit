package uap.workflow.modeler.editors;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import uap.workflow.modeler.bpmn.graph.BpmnGraph;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;

import nc.ui.ml.NCLangRes;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;

public class GraphPreviewDlgMaker{
	
	private static GraphPreviewDlg previewDlg =null;

	public static void showGraphPreviewDlg(String pkProcDef, Point location) {
		if(previewDlg==null){
			previewDlg =new GraphPreviewDlg();
		}
		previewDlg.getGraphComponent().getGraph().cellsRemoved(previewDlg.getGraphComponent().getGraph().getChildCells(previewDlg.getGraphComponent().getGraph().getDefaultParent()));
		if(GraphPreviewDlg.objectManager!=null){
			//清空之前的数据
			//GraphPreviewDlg.objectManager.getElementMap().clear();
		}
		previewDlg.showPreview(pkProcDef, location);
	}

}

class GraphPreviewDlg extends JDialog {

	static GraphObjectManager objectManager;

	mxGraphComponent graphComponent;

	GraphPreviewDlg() {
		super();
		setTitle(NCLangRes.getInstance().getStrByID("pfgraph", "GraphPreviewDlgMaker-000000")/*业务流定义预览图(ESC关闭)*/);
		graphComponent = new BpmnGraphComponent(new BpmnGraph()) {
			protected mxConnectionHandler createConnectionHandler() {
				return null;
			}
		};
		setContentPane(graphComponent);		
		setResizable(true);		
		setSize(new Dimension(700, 800));
		graphComponent.zoomTo(1.0, graphComponent.isCenterZoom());		
	}
	
	protected JRootPane createRootPane() {
		KeyStroke esc =KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
		KeyStroke zoomIn =KeyStroke.getKeyStroke("control ADD");
		KeyStroke zoomOut =KeyStroke.getKeyStroke("control SUBTRACT");
		
		
		JRootPane rootpane =new JRootPane();
		ActionListener escListener =new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}		
		};
		
		ActionListener zoomInListener =new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				double scale =graphComponent.getGraph().getView().getScale();
				graphComponent.zoomTo(((scale*100+5)>150?150:(scale*100+5))/100, graphComponent.isCenterZoom());
			}		
		};
		
		ActionListener zoomOutListener =new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				double scale =graphComponent.getGraph().getView().getScale();
				graphComponent.zoomTo(((scale*100-5)<50?50:(scale*100-5))/100, graphComponent.isCenterZoom());
			}		
		};
		rootpane.registerKeyboardAction(escListener, esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
		rootpane.registerKeyboardAction(zoomInListener, zoomIn, JComponent.WHEN_IN_FOCUSED_WINDOW);
		rootpane.registerKeyboardAction(zoomOutListener, zoomOut, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootpane;
	}
	
	
	void showPreview(String pkProcDef, Point location){
		objectManager = new GraphObjectManager(graphComponent, pkProcDef);
		objectManager.buildGraph();
		setLocation(location);
		setVisible(true);
	}
	
	mxGraphComponent getGraphComponent() {
		return graphComponent;
	}	
	
}
