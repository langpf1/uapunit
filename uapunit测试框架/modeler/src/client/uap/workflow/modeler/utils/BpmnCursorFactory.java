package uap.workflow.modeler.utils;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import uap.workflow.bpmn2.model.SequenceFlow;

import com.mxgraph.swing.mxGraphComponent;
import nc.ui.wfengine.designer.ResManager;
import nc.ui.wfengine.flowchart.UfWConstants;
import nc.uitheme.ui.ThemeResourceCenter;

public class BpmnCursorFactory {
	
	private static Cursor connectorCursor;
	
	private static Cursor invalidCursor;
	
	private static Cursor copyDropCursor;
	
	private static Map<String,Cursor> cursorMap =new HashMap<String,Cursor>();
	
	private static BpmnCursorFactory instance ;
	
	private  BpmnCursorFactory() {
		createConnectCursor();
		createInvalidCursor();
		createCopyDropCursor();
	}
	
	public static BpmnCursorFactory getInstance(){
		if(instance==null)
			instance =new BpmnCursorFactory();
		return instance;
	}
	
	private void createCopyDropCursor(){
		Point hotSpot3 = new Point(6, 2);
		Image curIm3 = BpmnImageFactory.getInstance().getImageIcon("win32_CopyDrop32x32.gif").getImage();	
		copyDropCursor = Toolkit.getDefaultToolkit().createCustomCursor(curIm3, hotSpot3, "lineCursor");
		cursorMap.put("copyDropCursor", copyDropCursor);
	}
	
	private void createInvalidCursor(){
		Point hotSpot3 = new Point(6, 2);
		Image curIm3 = BpmnImageFactory.getInstance().getImageIcon("invalid32x32.gif").getImage();	
		invalidCursor = Toolkit.getDefaultToolkit().createCustomCursor(curIm3, hotSpot3, "invalidCursor");
		cursorMap.put("invalidCursor", invalidCursor);
	}
	
	private void createConnectCursor(){
		ImageIcon curIc3 =new ImageIcon(ResManager.getResource(UfWConstants.TRANSACTION_CURSOR));
		Point hotSpot3 = new Point(curIc3.getIconWidth() / 2, curIc3.getIconHeight() / 2);
		Image curIm3 = curIc3.getImage();	
		connectorCursor = Toolkit.getDefaultToolkit().createCustomCursor(curIm3, hotSpot3, "lineCursor");
		cursorMap.put("connectorCursor", connectorCursor);
	}
	
	public  Cursor getConnectorCursor(){
		return cursorMap.get("connectorCursor");
	}
	
	public  Cursor getCopyDropCursor(){
		return cursorMap.get("copyDropCursor");
	}
	
	public  Cursor getInvalidCursor(){
		return cursorMap.get("invalidCursor");
	}
	
	

}
