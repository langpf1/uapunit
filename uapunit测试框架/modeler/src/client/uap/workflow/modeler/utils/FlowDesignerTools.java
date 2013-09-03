package uap.workflow.modeler.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import uap.workflow.modeler.uecomponent.GraphElementHolder;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.buziflow.cellinfo.BillMsgDriveCellInfo;
import nc.vo.pub.buziflow.cellinfo.BillsourceCellInfo;
import nc.vo.pub.buziflow.cellinfo.BilltypeCellInfo;
import nc.vo.pub.buziflow.cellinfo.ConnectorCellInfo;
import nc.vo.pub.buziflow.cellinfo.EndCellInfo;
import nc.vo.pub.buziflow.cellinfo.NoteCellInfo;
import nc.vo.pub.buziflow.cellinfo.StartCellInfo;
import nc.vo.pub.buziflow.cellinfo.SwimlaneCellInfo;
import nc.vo.pub.buziflow.cellinfo.VOExchangeCellInfo;
import nc.vo.pub.graph.element.IUfGraphUserOjbect;
import nc.vo.pub.graph.element.UfGraphCell;

import com.mxgraph.model.mxCell;

public class FlowDesignerTools {
	
	public static ArrayList<String> errMsg=new ArrayList<String>();
	
	//У��ʧ�ܵ�cellids
	public static HashSet<String> errCellids =new HashSet<String>();
	
	public static void clearErrorMsg(){
		errMsg.clear();
	}
	
	public static ArrayList<String> getErrorMsg(){
		return errMsg;
	}
	
	public static ArrayList<String> getErrCellId(){
		if(errCellids==null||errCellids.size()==0)
			return new ArrayList<String>();
		return new ArrayList<String>(errCellids);
	}
	
	public static void clearErrCellId(){
		errCellids.clear();
	}
	
	/**
	 * У��SplitEdge�ĺϷ���
	 * */
	public static boolean checkSplitValidity(Object edge,Object[] cells){
		UfGraphCell edgecell = (UfGraphCell) edge;
		String warningMsg="";
		boolean isValidity =true;
		if(!(edgecell.getValue() instanceof ConnectorCellInfo)){
			isValidity =false;
			warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000009")/*ֻ�������߲ſ��Էָ*/;
		}
		
		for(int i=0,end =cells==null?0:cells.length;i<end&&StringUtil.isEmptyWithTrim(warningMsg);i++){
			if(!((UfGraphCell)cells[i]).isVertex()){
				isValidity =false;
				warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000010")/*�����Ƕ�����ָܷ������ߣ�*/;
			}									
			if(!(((UfGraphCell)cells[i]).getValue() instanceof BillMsgDriveCellInfo)&&!(((UfGraphCell)cells[i]).getValue() instanceof BilltypeCellInfo)){
				isValidity =false;
				warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000011")/*��Ϣ������ҵ�񵥾ݲ��������ָ������ߣ�*/;
			}				
		}			
		
		if(StringUtil.isEmptyWithTrim(warningMsg)&&edgecell.getSource()!=null &&!(((mxCell)edgecell.getSource()).getValue() instanceof BilltypeCellInfo)){
			isValidity =false;
			warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000012")/*λ��ҵ�񵥾�֮�������߲ſɱ��ָ*/;	
		}
		if(StringUtil.isEmptyWithTrim(warningMsg)&&edgecell.getTarget()!=null &&!(((mxCell)edgecell.getTarget()).getValue() instanceof BilltypeCellInfo)){
			isValidity =false;
			warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000012")/*λ��ҵ�񵥾�֮�������߲ſɱ��ָ*/;		
		}
		if(!isValidity){
			MessageDialog.showWarningDlg(null, null, warningMsg);
		}			
		return isValidity;
	}
	
	
	public static boolean checkConnectValidity(Object edge,Object source,Object terminal){
		String warningMsg="";
		boolean isValidity =true;
		
		UfGraphCell edgecell = (UfGraphCell) edge;
		
		UfGraphCell oldcell =(UfGraphCell)source;	
		
		UfGraphCell terminalcell = (UfGraphCell) terminal;
		
		//һ�����ݶ����ϲ������ж��vo����������֮����
		UfGraphCell  needCheckCell=null;
		
		if(terminalcell!=null&&terminalcell.getEdgeCount()>=1&&(terminalcell.getValue() instanceof EndCellInfo||terminalcell.getValue() instanceof StartCellInfo)){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000000")/*��ʼ�ڵ�ͽ����ڵ�ֻ����һ������������*/;
			isValidity=false;
		}			
		//�ߺ��߲�������
		if(terminalcell!=null&&terminalcell.isEdge()){
			isValidity =false;
		}
		
		if(edgecell.getValue() instanceof BillsourceCellInfo &&((terminal!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))||
				(oldcell!=null &&!(oldcell.getValue() instanceof BilltypeCellInfo)))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000024")/*��ʽ���������ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof SwimlaneCellInfo){
			isValidity =false;
		}
		
		if(oldcell !=null&&(oldcell.getValue() instanceof EndCellInfo)){
			isValidity =false;
		}
		
		if(terminalcell !=null&&terminalcell.getValue() instanceof StartCellInfo){
			isValidity =false;
		}
		
		if(oldcell !=null&& oldcell.getValue() instanceof VOExchangeCellInfo){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000006")/*VO�������㲻���������߿�ʼ��������*/;
			isValidity =false;
		}
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO������������ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO������������ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =terminalcell;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =oldcell;				
			int edgecount = terminalcell.getEdgeCount();
			if(edgecount>=1){
				warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000023")/*VO��������ֻ�ܺ�һ������������*/;
				isValidity =false;	
			}
		}			
		
		if(needCheckCell!=null){
			for(int i=0,end=needCheckCell.getEdgeCount();i<end;i++){
				UfGraphCell cur_edge= (UfGraphCell) needCheckCell.getEdgeAt(i);
				if(cur_edge.getSource() ==needCheckCell&&cur_edge.getTarget()!=null&&cur_edge.getTarget().getValue() instanceof VOExchangeCellInfo){
					warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000022")/*�����ж��VO���������뵥��������*/;
					isValidity =false;
					break;
				}
			}
		}
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof BillMsgDriveCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*��Ϣ������Ҫ��ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof BillMsgDriveCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*��Ϣ������Ҫ��ҵ�񵥾�������*/;
			isValidity =false;
		}
		if(!isValidity&&!StringUtil.isEmptyWithTrim(warningMsg)){
			MessageDialog.showWarningDlg(null, null, warningMsg);
		}
		
		return isValidity;
	}
	
	
	
	/**
	 * У�����ӵĺϷ���
	 * */
	public static boolean checkConnectValidity(Object edge, Object terminal, boolean source){
		String warningMsg="";
		boolean isValidity =true;
		UfGraphCell edgecell = (UfGraphCell) edge;
		//�����Ӷ���
		UfGraphCell terminalcell = (UfGraphCell) terminal;
		//�����Ӷ���
		UfGraphCell oldcell =(UfGraphCell) (source?edgecell.getTarget():edgecell.getSource());	
		//һ�����ݶ����ϲ������ж��vo����������֮����
		UfGraphCell  needCheckCell=null;
		
		if(terminalcell!=null&&terminalcell.getEdgeCount()>=1&&(terminalcell.getValue() instanceof EndCellInfo||terminalcell.getValue() instanceof StartCellInfo)){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000000")/*��ʼ�ڵ�ͽ����ڵ�ֻ����һ������������*/;
			isValidity=false;
		}			
		//�ߺ��߲�������
		if(terminalcell!=null&&terminalcell.isEdge()){
			isValidity =false;
		}
		
		if(edgecell.getValue() instanceof BillsourceCellInfo &&((terminal!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))||
				(oldcell!=null &&!(oldcell.getValue() instanceof BilltypeCellInfo)))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000024")/*��ʽ���������ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof SwimlaneCellInfo){
			isValidity =false;
		}
		
		if(source&&(terminalcell !=null&&terminalcell.getValue() instanceof EndCellInfo)){
			isValidity =false;
		}
		
		if(!source&&(terminalcell !=null&&terminalcell.getValue() instanceof StartCellInfo)){
			isValidity =false;
		}
		
		if(source&&(terminalcell !=null&& terminalcell.getValue() instanceof VOExchangeCellInfo)){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000006")/*VO�������㲻���������߿�ʼ��������*/;
			isValidity =false;
		}
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO������������ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO������������ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =terminalcell;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =oldcell;				
			int edgecount = terminalcell.getEdgeCount();
			if(edgecount>=1){
				warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000023")/*VO��������ֻ�ܺ�һ������������*/;
				isValidity =false;	
			}
		}			
		
		if(needCheckCell!=null){
			for(int i=0,end=needCheckCell.getEdgeCount();i<end;i++){
				UfGraphCell cur_edge= (UfGraphCell) needCheckCell.getEdgeAt(i);
				if(cur_edge.getSource() ==needCheckCell&&cur_edge.getTarget()!=null&&cur_edge.getTarget().getValue() instanceof VOExchangeCellInfo){
					warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000022")/*�����ж��VO���������뵥��������*/;
					isValidity =false;
					break;
				}
			}
		}
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof BillMsgDriveCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*��Ϣ������Ҫ��ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof BillMsgDriveCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*��Ϣ������Ҫ��ҵ�񵥾�������*/;
			isValidity =false;
		}
		
		//XXXX��Ϣ������������һ���߶�Զࡣ
//		
//		if(terminalcell!=null&&terminalcell.getValue() instanceof BillMsgDriveCellInfo){
//			int edgecount =terminalcell.getEdgeCount();
//			for(int start=0,end =edgecount;start<end;start++){
//				UfGraphCell edge_cell= (UfGraphCell) terminalcell.getEdgeAt(start);
//				if(edge_cell.getTarget()== terminalcell){
//					isValidity =false;
//					break;
//				}
//			}
//		}
		
		if(!isValidity&&!StringUtil.isEmptyWithTrim(warningMsg)){
			MessageDialog.showWarningDlg(null, null, warningMsg);
		}
		
		return isValidity;
		
	}
	
	/**
	 * ����֮ǰ������ͼ�ĺϷ���
	 * */
	public static boolean checkValiditionBeforeSave(HashMap<String, GraphElementHolder> elementMap,IUfGraphUserOjbect graphObject){
		
		clearErrCellId();
		clearErrorMsg();
		GraphElementHolder[] elements = elementMap.values().toArray(new GraphElementHolder[0]);
		
		if(elements==null||elements.length==0)
			return true;	
		checkEdge(elements);
		checkStartAndEnd(elements);		
		checkElementHolder(elements);
		return getErrCellId().size()==0;

	}
	
	/**
	 * У�����õ�����
	 * */
	private static void checkElementHolder(GraphElementHolder[] elements){
		int errcount=0;
		for(GraphElementHolder element:elements){
			if(element.getUserobject() instanceof IUfGraphUserOjbect){
				if(!((IUfGraphUserOjbect)element.getUserobject()).isValidity()){
					errCellids.add(element.getId());
					errcount++;
				}
			}
		}
		if(errcount>0){
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000001", null, new String[]{String.valueOf(errcount)})/*����{0}�����ô���ͼԪ��*/);
		}		
	}
	
	/**
	 * У���Ƿ����start��end
	 * */
	private static void checkStartAndEnd(GraphElementHolder[] elements){
		int istart=0;
		int iend=0;
		boolean flag=true;
		for(GraphElementHolder holder:elements){
			boolean isStartCell =false;
			boolean isEndCell=false;
			if(holder.getUserobject() instanceof StartCellInfo){
				isStartCell=true;
				istart++;
			}
			
			if(holder.getUserobject() instanceof EndCellInfo){
				isEndCell =true;
				iend++;
			}			
			if(istart>1){
				flag =false;
				if(isStartCell)
					errCellids.add(holder.getId());
			}
			if(iend>1){
				flag =false;
				if(isEndCell)
					errCellids.add(holder.getId());
			}
			
		}
		
		if(flag){
			flag=iend==1&&istart==1;
		}
		
		if(!flag){
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000019")/*��ʼ�ͽ����ڵ�ֻ�ܴ���һ����*/);
		}
	}
	
	/**
	 * ����edge�Ƿ����������
	 * */
	private static void checkEdge(GraphElementHolder[] elements){
		int invalidityEdge=0;
		int invalidityVertex=0;
		for(GraphElementHolder holder:elements){			
			if(holder.getType()==1&&(holder.getSourceid()==null||holder.getTargetid()==null)){
				invalidityEdge++;	
				errCellids.add(holder.getId());
			}
			
			if(holder.getType()==0&&("0".equals(holder.getSourceid())||"0".equals(holder.getTargetid()))
					&&!(holder.getUserobject() instanceof SwimlaneCellInfo)&&!(holder.getUserobject() instanceof NoteCellInfo)){
				invalidityVertex++;
				errCellids.add(holder.getId());
			}
		}
		if(invalidityEdge>0){
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000002", null, new String[]{String.valueOf(invalidityEdge)})/*����{0}������ı�*/);
		}
		if(invalidityVertex>0){
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000003", null, new String[]{String.valueOf(invalidityVertex)})/*����{0}������Ķ���*/);
		}
	}
	
	
	
}
