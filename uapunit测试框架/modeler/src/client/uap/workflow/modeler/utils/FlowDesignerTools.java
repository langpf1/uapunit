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
	
	//校验失败的cellids
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
	 * 校验SplitEdge的合法性
	 * */
	public static boolean checkSplitValidity(Object edge,Object[] cells){
		UfGraphCell edgecell = (UfGraphCell) edge;
		String warningMsg="";
		boolean isValidity =true;
		if(!(edgecell.getValue() instanceof ConnectorCellInfo)){
			isValidity =false;
			warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000009")/*只有连接线才可以分割！*/;
		}
		
		for(int i=0,end =cells==null?0:cells.length;i<end&&StringUtil.isEmptyWithTrim(warningMsg);i++){
			if(!((UfGraphCell)cells[i]).isVertex()){
				isValidity =false;
				warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000010")/*必须是顶点才能分割连接线！*/;
			}									
			if(!(((UfGraphCell)cells[i]).getValue() instanceof BillMsgDriveCellInfo)&&!(((UfGraphCell)cells[i]).getValue() instanceof BilltypeCellInfo)){
				isValidity =false;
				warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000011")/*消息驱动和业务单据才能用来分割连接线！*/;
			}				
		}			
		
		if(StringUtil.isEmptyWithTrim(warningMsg)&&edgecell.getSource()!=null &&!(((mxCell)edgecell.getSource()).getValue() instanceof BilltypeCellInfo)){
			isValidity =false;
			warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000012")/*位于业务单据之间连接线才可被分割！*/;	
		}
		if(StringUtil.isEmptyWithTrim(warningMsg)&&edgecell.getTarget()!=null &&!(((mxCell)edgecell.getTarget()).getValue() instanceof BilltypeCellInfo)){
			isValidity =false;
			warningMsg =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000012")/*位于业务单据之间连接线才可被分割！*/;		
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
		
		//一个单据顶点上不允许有多个vo交换顶点与之相连
		UfGraphCell  needCheckCell=null;
		
		if(terminalcell!=null&&terminalcell.getEdgeCount()>=1&&(terminalcell.getValue() instanceof EndCellInfo||terminalcell.getValue() instanceof StartCellInfo)){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000000")/*开始节点和结束节点只能与一条连接线相连*/;
			isValidity=false;
		}			
		//线和线不能相连
		if(terminalcell!=null&&terminalcell.isEdge()){
			isValidity =false;
		}
		
		if(edgecell.getValue() instanceof BillsourceCellInfo &&((terminal!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))||
				(oldcell!=null &&!(oldcell.getValue() instanceof BilltypeCellInfo)))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000024")/*拉式生单必须和业务单据相连！*/;
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
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000006")/*VO交换顶点不能与连接线开始端相连！*/;
			isValidity =false;
		}
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO交换顶点必须和业务单据相连！*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO交换顶点必须和业务单据相连！*/;
			isValidity =false;
		}
		
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =terminalcell;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =oldcell;				
			int edgecount = terminalcell.getEdgeCount();
			if(edgecount>=1){
				warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000023")/*VO交换顶点只能和一个单据相连！*/;
				isValidity =false;	
			}
		}			
		
		if(needCheckCell!=null){
			for(int i=0,end=needCheckCell.getEdgeCount();i<end;i++){
				UfGraphCell cur_edge= (UfGraphCell) needCheckCell.getEdgeAt(i);
				if(cur_edge.getSource() ==needCheckCell&&cur_edge.getTarget()!=null&&cur_edge.getTarget().getValue() instanceof VOExchangeCellInfo){
					warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000022")/*不可有多个VO交换顶点与单据相连！*/;
					isValidity =false;
					break;
				}
			}
		}
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof BillMsgDriveCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*消息驱动需要和业务单据相连！*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof BillMsgDriveCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*消息驱动需要和业务单据相连！*/;
			isValidity =false;
		}
		if(!isValidity&&!StringUtil.isEmptyWithTrim(warningMsg)){
			MessageDialog.showWarningDlg(null, null, warningMsg);
		}
		
		return isValidity;
	}
	
	
	
	/**
	 * 校验连接的合法性
	 * */
	public static boolean checkConnectValidity(Object edge, Object terminal, boolean source){
		String warningMsg="";
		boolean isValidity =true;
		UfGraphCell edgecell = (UfGraphCell) edge;
		//将连接顶点
		UfGraphCell terminalcell = (UfGraphCell) terminal;
		//已连接顶点
		UfGraphCell oldcell =(UfGraphCell) (source?edgecell.getTarget():edgecell.getSource());	
		//一个单据顶点上不允许有多个vo交换顶点与之相连
		UfGraphCell  needCheckCell=null;
		
		if(terminalcell!=null&&terminalcell.getEdgeCount()>=1&&(terminalcell.getValue() instanceof EndCellInfo||terminalcell.getValue() instanceof StartCellInfo)){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000000")/*开始节点和结束节点只能与一条连接线相连*/;
			isValidity=false;
		}			
		//线和线不能相连
		if(terminalcell!=null&&terminalcell.isEdge()){
			isValidity =false;
		}
		
		if(edgecell.getValue() instanceof BillsourceCellInfo &&((terminal!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))||
				(oldcell!=null &&!(oldcell.getValue() instanceof BilltypeCellInfo)))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000024")/*拉式生单必须和业务单据相连！*/;
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
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000006")/*VO交换顶点不能与连接线开始端相连！*/;
			isValidity =false;
		}
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO交换顶点必须和业务单据相连！*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000007")/*VO交换顶点必须和业务单据相连！*/;
			isValidity =false;
		}
		
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =terminalcell;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof VOExchangeCellInfo){
			needCheckCell =oldcell;				
			int edgecount = terminalcell.getEdgeCount();
			if(edgecount>=1){
				warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000023")/*VO交换顶点只能和一个单据相连！*/;
				isValidity =false;	
			}
		}			
		
		if(needCheckCell!=null){
			for(int i=0,end=needCheckCell.getEdgeCount();i<end;i++){
				UfGraphCell cur_edge= (UfGraphCell) needCheckCell.getEdgeAt(i);
				if(cur_edge.getSource() ==needCheckCell&&cur_edge.getTarget()!=null&&cur_edge.getTarget().getValue() instanceof VOExchangeCellInfo){
					warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000022")/*不可有多个VO交换顶点与单据相连！*/;
					isValidity =false;
					break;
				}
			}
		}
		
		
		if(oldcell!=null&&oldcell.getValue() instanceof BillMsgDriveCellInfo &&(terminalcell!=null &&!(terminalcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*消息驱动需要和业务单据相连！*/;
			isValidity =false;
		}
		
		if(terminalcell!=null&&terminalcell.getValue() instanceof BillMsgDriveCellInfo&&(oldcell!=null&&!(oldcell.getValue() instanceof BilltypeCellInfo))){
			warningMsg=NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000008")/*消息驱动需要和业务单据相连！*/;
			isValidity =false;
		}
		
		//XXXX消息驱动不允许多对一或者多对多。
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
	 * 保存之前检验作图的合法性
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
	 * 校验配置的内容
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
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000001", null, new String[]{String.valueOf(errcount)})/*存在{0}个配置错误图元！*/);
		}		
	}
	
	/**
	 * 校验是否存在start和end
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
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000019")/*开始和结束节点只能存在一个！*/);
		}
	}
	
	/**
	 * 检验edge是否都有输入输出
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
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000002", null, new String[]{String.valueOf(invalidityEdge)})/*存在{0}条游离的边*/);
		}
		if(invalidityVertex>0){
			errMsg.add(NCLangRes.getInstance().getStrByID("pfgraph", "FlowDesignerTools-000003", null, new String[]{String.valueOf(invalidityVertex)})/*存在{0}个游离的顶点*/);
		}
	}
	
	
	
}
