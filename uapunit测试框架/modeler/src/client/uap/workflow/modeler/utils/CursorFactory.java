package uap.workflow.modeler.utils;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import nc.ui.pub.flowdesigner.GraphEditor;
import nc.ui.pub.flowdesigner.component.BuziflowCellLib;
import nc.ui.wfengine.designer.ResManager;
import nc.ui.wfengine.flowchart.UfWConstants;
import nc.vo.pub.buziflow.cellinfo.BillMsgDriveCellInfo;
import nc.vo.pub.buziflow.cellinfo.BilltypeCellInfo;
import nc.vo.pub.buziflow.cellinfo.EndCellInfo;
import nc.vo.pub.buziflow.cellinfo.NoteCellInfo;
import nc.vo.pub.buziflow.cellinfo.RouterCellInfo;
import nc.vo.pub.buziflow.cellinfo.StartCellInfo;
import nc.vo.pub.buziflow.cellinfo.SwimlaneCellInfo;
import nc.vo.pub.buziflow.cellinfo.VOExchangeCellInfo;
import nc.vo.pub.graph.element.UfGraphCell;

import com.mxgraph.swing.mxGraphComponent;

public class CursorFactory {
	
	public static Cursor billCursor ;
	
	public static Cursor startCursor;
	
	public static Cursor endCursor;
	
	public static Cursor actionCursor;
	
	public static Cursor lineCursor;
	
	public static Cursor noteCursor;
	
	public static Cursor ruleCursor;
	
	public static Cursor routeCursor;
	
	public static Cursor swimlaneCursor;
	
	private static Map<String,Cursor> cursorMap =new HashMap<String,Cursor>();
	
	private mxGraphComponent graphComponent;
	
	public CursorFactory(mxGraphComponent graphComponent){
		createCustomCursor(graphComponent);
	}
	
	private void createCustomCursor(mxGraphComponent graphComponent){
		
		
		ImageIcon curIc0 =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._startIcon));
		Point hotSpot0 = new Point(curIc0.getIconWidth() / 2, curIc0.getIconHeight() / 2);
		Image curIm0 = curIc0.getImage();		
		startCursor =graphComponent.getToolkit().createCustomCursor(curIm0, hotSpot0, "startCursor");
		cursorMap.put(StartCellInfo.class.getSimpleName(), startCursor);
		
		ImageIcon curIc =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._endIcon));	
		Point hotSpot = new Point(curIc.getIconWidth() / 2, curIc.getIconHeight() / 2);
		Image curIm = curIc.getImage();	
		endCursor =graphComponent.getToolkit().createCustomCursor(curIm, hotSpot, "endCursor");
		cursorMap.put(EndCellInfo.class.getSimpleName(), endCursor);
		
		ImageIcon curIc1 =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._billIcon));	
		Point hotSpot1 = new Point(curIc1.getIconWidth() / 2, curIc1.getIconHeight() / 2);
		Image curIm1 = curIc1.getImage();	
		billCursor =graphComponent.getToolkit().createCustomCursor(curIm1, hotSpot1, "billtypeCursor");
		cursorMap.put(BilltypeCellInfo.class.getSimpleName(), billCursor);
//		
		ImageIcon curIc2 =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._actionIcon));
		Point hotSpot2 = new Point(curIc2.getIconWidth() / 2, curIc.getIconHeight() / 2);
		Image curIm2 = curIc2.getImage();	
		actionCursor =graphComponent.getToolkit().createCustomCursor(curIm2, hotSpot2, "actionCursor");
		cursorMap.put(BillMsgDriveCellInfo.class.getSimpleName(), actionCursor);
//		
		ImageIcon curIc3 =new ImageIcon(ResManager.getResource(UfWConstants.TRANSACTION_CURSOR));
		Point hotSpot3 = new Point(curIc3.getIconWidth() / 2, curIc3.getIconHeight() / 2);
		Image curIm3 = curIc3.getImage();	
		lineCursor =graphComponent.getToolkit().createCustomCursor(curIm3, hotSpot3, "lineCursor");
		cursorMap.put("edge", lineCursor);
		
		
		
		ImageIcon curIc4 =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._noteIcon));
		Point hotSpot4 = new Point(curIc4.getIconWidth() / 2, curIc4.getIconHeight() / 2);
		Image curIm4 = curIc4.getImage();	
		noteCursor =graphComponent.getToolkit().createCustomCursor(curIm4, hotSpot4, "noteCursor");
		cursorMap.put(NoteCellInfo.class.getSimpleName(), noteCursor);
				
		ImageIcon curIc5 =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._ruleIcon));
//		Point hotSpot5 = new Point(curIc5.getIconWidth() / 2, curIc5.getIconHeight() / 2);
		Point hotSpot5 = new Point(12, 12);
		Image curIm5 = curIc5.getImage();	
		ruleCursor =graphComponent.getToolkit().createCustomCursor(curIm5, hotSpot5, "ruleCursor");
		cursorMap.put(VOExchangeCellInfo.class.getSimpleName(), ruleCursor);
		
		ImageIcon curIc6 =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._swimlaneIcon));	
		Point hotSpot6 = new Point(curIc6.getIconWidth() / 2, curIc6.getIconHeight() / 2);
		Image curIm6 = curIc6.getImage();	
		swimlaneCursor =graphComponent.getToolkit().createCustomCursor(curIm6, hotSpot6, "swimlaneCursor");
		cursorMap.put(SwimlaneCellInfo.class.getSimpleName(), swimlaneCursor);
		
		ImageIcon curIc7 =new ImageIcon(GraphEditor.class.getResource(BuziflowCellLib._routerIcon));	
		Point hotSpot7 = new Point(curIc7.getIconWidth() / 2, curIc7.getIconHeight() / 2);
		Image curIm7 = curIc7.getImage();	
		routeCursor =graphComponent.getToolkit().createCustomCursor(curIm7, hotSpot7, "routeCursor");
		cursorMap.put(RouterCellInfo.class.getSimpleName(), routeCursor);
		
	}
	
	public Cursor getCursor(UfGraphCell cell){
		if (cell.isVertex())
			return cursorMap.get(cell.getValue().getClass().getSimpleName());
		else
			return cursorMap.get("edge");
	}
	
	
}
