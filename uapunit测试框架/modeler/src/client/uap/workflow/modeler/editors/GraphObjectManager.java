package uap.workflow.modeler.editors;

import java.io.StringReader;
import java.util.ArrayList;
import nc.bs.framework.common.NCLocator;
import nc.vo.jcom.lang.StringUtil;
import uap.workflow.bpmn2.model.Annotation;
import uap.workflow.bpmn2.model.Association;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.Connector;
import uap.workflow.bpmn2.model.DataObject;
import uap.workflow.bpmn2.model.FlowNode;
import uap.workflow.bpmn2.model.MessageFlow;
import uap.workflow.bpmn2.model.SequenceFlow;
import uap.workflow.bpmn2.model.event.BoundaryEvent;
import uap.workflow.bpmn2.model.event.CatchEvent;
import uap.workflow.bpmn2.model.event.IntermediateCatchEvent;
import uap.workflow.bpmn2.parser.ProcessDefinitionsManager;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.modeler.bpmn.graph.itf.IGraphListenerClaim;
import uap.workflow.modeler.bpmn.graph.itf.ListenerType;
import uap.workflow.modeler.uecomponent.UfGraphCell;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;

public class GraphObjectManager implements IGraphListenerClaim {

//	private IUfGraphUserOjbect graphObject;

	private mxGraphComponent component;

//	private String pk_graph;

	private String pkProcDef;

	private ArrayList<GraphActionListener> actionListeners = new ArrayList<GraphActionListener>();
	
	private int searchIndex=1;
	
	/**
	 * 是否粘连键
	 * */
	private boolean isPersistent;
	
	/**
	 * @param component
	 * @param pk_bsiObject
	 * */
	public GraphObjectManager(mxGraphComponent component, String pkProcDef) {
		this.component = component;
		this.pkProcDef = pkProcDef;
	}
		
	public boolean isPersistent() {
		return isPersistent;
	}

	public void setPersistent(boolean isPersistent) {
		this.isPersistent = isPersistent;
	}



	public void addGraphActionListener(GraphActionListener l) {
		actionListeners.add(l);
	}

//	public void clearData() {
//		pk_graph = null;
//	}
//
//	/**
//	 * @param clazz
//	 */
//	private void buildNewGraphOjbect() {
//		try {
//			graphObject = new DefaultGraphObject();
//			graphObject.setBusinessPk(pkProcDef);
//			graphObject.setPk_group(PfUtilUITools.getLoginGroup());
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//		}
//		return;
//	}
	
	/**
	 * @param busiobjct
	 */
//	public void initGraphData() {
//	}
	
	@ListenerType(eventType = mxEvent.REMOVE_CELLS)
	public void invokeRemove(Object sender, mxEventObject evt) {
	}
	

//	@ListenerType(eventType = mxEvent.SPLIT_EDGE)
//	public void invokeSplitEdge(Object sender, mxEventObject evt) {
//		Object[] moved = (Object[]) evt.getProperty("cells");
//		mxCell newEdge = (mxCell) evt.getProperty("newEdge");
//		for (Object obj : moved) {
//			mxICell cell = ((mxICell) obj);
//			GraphElementHolder holder = new GraphElementHolder();
//			holder.setId(cell.getId());
//			getElementMap().put(cell.getId(), holder);
//		}
//		GraphElementHolder holder = new GraphElementHolder();
//		holder.setId(newEdge.getId());
//		getElementMap().put(newEdge.getId(), holder);
//		
//	}

	/**
	 * @param celllist
	 * @param cellroot
	 */
	private void getAllCells(ArrayList<Object> celllist, mxCell cellroot) {
		celllist.add(cellroot);
		if (cellroot.getChildCount() > 0) {
			for (int i = 0; i < cellroot.getChildCount(); i++) {
				getAllCells(celllist, (mxCell) cellroot.getChildAt(i));
			}
		}
	}
	
	/**
	 * @param cell
	 */
//	private void connectEdges(mxCell cell)  {
//		getElementMap().get(cell.getId()).setSourceid(cell.getSource() == null ? null : cell.getSource().getId());
//		getElementMap().get(cell.getId()).setTargetid(cell.getTarget() == null ? null : cell.getTarget().getId());
//	}
	
//	private void connectVertex(UfGraphCell cell,Object[] edges){
//		//是否有后继边
//		boolean hasPostEdge =false;
//		//是否有前驱边
//		boolean hasPreEdge =false;
//		
//		if(cell.getValue() instanceof StartCellInfo){
//			hasPreEdge =true;
//		}
//		if(cell.getValue() instanceof EndCellInfo||cell.getValue() instanceof VOExchangeCellInfo ||cell.getValue() instanceof NoteCellInfo){
//			hasPostEdge =true;
//		}
//		
//		
//		for(Object edge:edges){
//			if(cell==((mxCell)edge).getSource()){
//				hasPostEdge =true;
//				
//			}
//			if(cell==((mxCell)edge).getTarget()){
//				hasPreEdge =true;
//			}
//		}
//		getElementMap().get(cell.getId()).setSourceid(hasPreEdge?"1":"0");
//		getElementMap().get(cell.getId()).setTargetid(hasPostEdge?"1":"0");
//	}

	/**
	 * 
	 */
//	public void setGraphElementHolders() {
//		if (component == null)
//			return;
//		// Object[] vertices =
//		// component.getGraph().getChildVertices(component.getGraph().getDefaultParent());
//		Object[] edges = component.getGraph().getChildEdges(component.getGraph().getDefaultParent());
//		Object[] topcells = component.getGraph().getChildCells(component.getGraph().getDefaultParent());
//		ArrayList<Object> celllist = new ArrayList<Object>();
//		for (Object o : topcells) {
//			getAllCells(celllist, (mxCell) o);
//			// celllist.add(o);
//			// celllist.addAll(Arrays.asList(component.getGraph().getChildCells(o)));
//		}
//		for (Object o : celllist) {// ///
//			mxCell cell = (mxCell) o;
//			getElementMap().get(cell.getId()).setGeometry(cell.getGeometry());
//			getElementMap().get(cell.getId()).setPk_graph(pk_graph);
//			getElementMap().get(cell.getId()).setGraphstyle(cell.getStyle());
//			getElementMap().get(cell.getId()).setParentid(cell.getParent().getId());
//			getElementMap().get(cell.getId()).setType(cell.isVertex() ? 0 : 1);
//			if (cell.getValue() != null && (cell.getValue() instanceof IUfGraphUserOjbect))
//				((IUfGraphUserOjbect) (cell.getValue())).setPk_group(PfUtilUITools.getLoginGroup());// /
//			getElementMap().get(cell.getId()).setUserobject(cell.getValue());
//		}
//		for (Object o : edges) {
//			connectEdges((mxCell) o);
//		}
//		
//		for(Object o : celllist)  {
//			if(((mxCell) o).isEdge()) {
//				connectEdges((mxCell) o);
//			}
//			if(((mxCell) o).isVertex()){
//				connectVertex((UfGraphCell) o,edges);
//			}
//		}
//	}
	
	/**
	 * @param matchingValue 查询条件
	 * @return void 
	 * */
	public void searchElement(String matchingValue){
		component.getGraph().clearSelection();
		if (component == null||StringUtil.isEmptyWithTrim(matchingValue))
			return;		
		Object[] edges = component.getGraph().getChildEdges(component.getGraph().getDefaultParent());
		Object[] topcells = component.getGraph().getChildCells(component.getGraph().getDefaultParent());
		ArrayList<Object> celllist = new ArrayList<Object>();
		for (Object o : topcells) {
			getAllCells(celllist, (mxCell) o);
		}
		for(Object o:edges){
			celllist.add(o);
		}
		//符合搜索条件的cell
		ArrayList<mxCell> matchingElements = new ArrayList<mxCell>();
		for(Object o :celllist){
			if(((mxCell)o).getValue().toString().indexOf(matchingValue)!=-1)
				matchingElements.add((mxCell)o);				
		}
		if(matchingElements.size()==0)
			return;
		component.getGraph().setSelectionCell(matchingElements.get(getRealSearchIndex(matchingElements.size(),searchIndex++)));	
	}
	
	/**
	 * @param arrSize 匹配的元素个数
	 * @param index   要查询第几个匹配的元素
	 * */
	private int getRealSearchIndex(int arrSize,int index){
		if(index%arrSize==0)
			return arrSize-1;
		else{
			return index-((index/arrSize)*arrSize)-1;
		}
	}

	/**
	 * build your graph.
	 */
	public void buildGraph() {//
		if(pkProcDef != null){
			IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
			ProcessDefinitionVO proDef = proDefQry.getProDefVoByPk(pkProcDef);
			Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(new StringReader(proDef.getProcessStr()));
			model.getProcesses().get(0).setProcessDefinitionPk(pkProcDef);
			component.getGraph().getModel().beginUpdate();
			Bpmn2MemoryModel.addToGraphModel(component, model);
			component.setGridVisible(false);
			component.getGraph().getModel().endUpdate();
		}
//		HashMap<String, mxCell> cellmap = new HashMap<String, mxCell>();
//		HashMap<String, ArrayList<mxCell>> h = new HashMap<String, ArrayList<mxCell>>();
//		ArrayList<mxCell> topcells = new ArrayList<mxCell>();
//		try {
//			component.getGraph().getModel().beginUpdate();
//			for (GraphElementHolder holder : getElementMap().values()) {
//				UfGraphCell cell = new UfGraphCell(holder);
//				cell.setVertex(holder.getType() == 0); // important!
//				cell.setEdge(!cell.isVertex());
//				if (holder.getParentid() != null && !"1".equals(holder.getParentid())) {
//					if (cellmap.get(holder.getParentid()) == null) {
//						ArrayList<mxCell> waitingList = h.get(holder.getParentid());
//						if (waitingList == null) {
//							waitingList = new ArrayList<mxCell>();
//							h.put(holder.getParentid(), waitingList);
//						}
//						waitingList.add(cell);
//					} else {
//						cellmap.get(holder.getParentid()).insert(cell);
//					}
//				} else {
//					topcells.add(cell);
//					// component.getGraph().addCell(cell,
//					// component.getGraph().getDefaultParent());
//				}
//				cellmap.put(cell.getId(), cell);
//			
//			}
//
//			for (String id : h.keySet())
//				for (mxCell c : h.get(id))
//					cellmap.get(id).insert(c);
//			
//			for (GraphElementHolder holder : getElementMap().values()) {
//			//holder为edge时候设置
//			if(holder.getType() != 0){
//				cellmap.get(holder.getId()).setSource(cellmap.get(holder.getSourceid()));
//				cellmap.get(holder.getId()).setTarget(cellmap.get(holder.getTargetid()));
//			}			
//		}
//
//			for (mxCell c : topcells) {
//				component.getGraph().addCell(c, component.getGraph().getDefaultParent());
//			}
//
//		} finally {
//			component.getGraph().getModel().endUpdate();
//		}
	}

	/**
	 * @param graphPersitClass
	 *            图本身关联的业务对象持久化类名称
	 * @param pluginClasses
	 *            业务数据持久化插件类名称
	 * @param isVersionCanPlus 
	 */
	public void persistGraph(String graphPersitClass, String[] pluginClasses) {
//		setGraphElementHolders();		
//		IGraphPersist persist = (IGraphPersist) NCLocator.getInstance().lookup(IGraphPersist.class);
//		String oldBusitypePK =pk_busiOjbect;
//		String newBusitypePK =null;
//		ArrayList<String> errMsgs=null;
//		try {
//			GraphElementHolder[] elements = getElementMap().values().toArray(new GraphElementHolder[0]);
//			customPersist();
//			FlowDesignerTools.clearErrorMsg();
//			FlowDesignerTools.clearErrCellId();
//			boolean isValidity =FlowDesignerTools.checkValiditionBeforeSave(getElementMap(),graphObject);
//			
//			if(!isValidity){
//				errMsgs =FlowDesignerTools.getErrorMsg();
//				//标示出错误的cellid
//				ArrayList<String> errcellids =FlowDesignerTools.getErrCellId();
//				//XXX业务流存在错误，不进行提示,直接保存。
////				int ok=MessageDialog.showYesNoDlg(null, null, NCLangRes.getInstance().getStrByID("pfgraph", "GraphObjectManager-000000")/*业务流定义存在错误，是否仍然保存？*/);
//				ArrayList<UfGraphCell> selectCell =new ArrayList<UfGraphCell>();
//				Object[] allcells=component.getGraph().getChildCells(component.getGraph().getDefaultParent());
//				for (int i = 0, end = allcells == null ? 0 : allcells.length; i < end; i++) {
//					if (errcellids.contains(((UfGraphCell) allcells[i]).getId()))
//						selectCell.add((UfGraphCell) allcells[i]);
//						
//				}
//				component.selectCellsForEvent(selectCell.toArray(), null);
//				
//				//在输出面板打印出错误消息
//				for (GraphActionListener l : actionListeners)
//						l.fireEvent(new GraphActionEvent(this, errMsgs.toArray(new String[0]), "error"));
//				
////				if(MessageDialog.ID_YES!=ok){										
////					return;
////				}				
//			}
//			
//			if (needPropCodeAndNameDlg()) {
//				CustomDialog dlg =new CustomDialog(null,((BuziflowBeanInfo)graphObject).getBusicode(),((BuziflowBeanInfo)graphObject).getBusiname());
//				dlg.setVisible(true);
//				if(!dlg.isOK())
//					return ;
//				((BuziflowBeanInfo)graphObject).setBusicode(dlg.getCode());
//				((BuziflowBeanInfo)graphObject).setBusiname(dlg.getName());
//			}
//			String primarybillcode= getPrimaryBillCode(elements);
//			((BuziflowBeanInfo)graphObject).setPrimarybilltype(primarybillcode);
//			
//			//如果是启用状态，与处理有实例的工作流相似
//			boolean isneedsetVersion =false;
//			if(((BuziflowBeanInfo)graphObject).getValidity()!=null&&WorkflowDefStatusEnum.Valid.getIntValue()==((BuziflowBeanInfo)graphObject).getValidity()){			
//				int result =MessageDialog.showYesNoCancelDlg(null, null, NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000072")/*是否保存为新版本，否则将覆盖旧版本？*/);
//				if(result==UIDialog.ID_CANCEL)
//					return;
//				if(result==UIDialog.ID_YES){
//					isneedsetVersion =true;
//					((BuziflowBeanInfo)graphObject).setPk_busitype(null);
//					for(GraphElementHolder holder:elements){
//						holder.setPk_ufgraph(null);
//						if(holder.getUserobject() instanceof IUfGraphUserOjbect){
//							((IUfGraphUserOjbect)holder.getUserobject()).setBusinessPk(null);
//						}					
//					}
//				}					
//			}
//			
//			//保存前，确保BuziflowBeanInfo的validity和version字段都赋值
//			if(((BuziflowBeanInfo)graphObject).getValidity()==null){
//				((BuziflowBeanInfo)graphObject).setValidity(WorkflowDefStatusEnum.Init.getIntValue());
//			}
//			if(StringUtil.isEmptyWithTrim(((BuziflowBeanInfo)graphObject).getVersion())){
//				((BuziflowBeanInfo)graphObject).setVersion("1.0");
//			}
//			
//			HashMap<String, String>[] maps = persist.persist(elements, graphObject, pluginClasses, graphPersitClass,isValidity,isneedsetVersion,getCurBusitypecode(),getCurBusitypename());		
//			// set pks after persist!
//			graphObject.setBusinessPk(maps[0].get(IGraphKeyConst.BUSI_KEY));			
//			newBusitypePK =graphObject.getBusinessPk();
//			
//			if(maps!=null){
//				for (String key : maps[1].keySet()) {// //
//					getElementMap().get(key).setPk_ufgraph(maps[1].get(key));
//				}
//
//				for (String key : maps[2].keySet()) {
//					((IUfGraphUserOjbect) getElementMap().get(key).getUserobject()).setBusinessPk(maps[2].get(key));
//					if((IUfGraphUserOjbect) getElementMap().get(key).getUserobject() instanceof BilltypeCellInfo){
//						((BilltypeCellInfo) getElementMap().get(key).getUserobject()).setPk_businesstype(maps[0].get(IGraphKeyConst.BUSI_KEY));
//					}
//				}
//				
//				for (GraphActionListener l : actionListeners) {
//					if (isValidity) {
//						l.fireEvent(new GraphActionEvent(this, NCLangRes.getInstance().getStrByID("pfgraph",
//								"graphhint-000013")/* 保存成功! */, "save"));
//					}
//				}
//					
//					
//			}
//			
//			if(StringUtil.compare(oldBusitypePK, newBusitypePK)!=0){
//				clearData();
//				pk_busiOjbect =newBusitypePK;
//				initGraphData(this.graphPersitster);
//			}
//			
//		
//
//		} catch (BusinessException e) {
//			Logger.error(e.getMessage(), e);
//			if(errMsgs==null){
//				errMsgs =new ArrayList<String>();
//			}
//			errMsgs.add(NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000014", null, new String[]{e.getMessage()})/*保存失败!{0}*/);
//			for (GraphActionListener l : actionListeners)
//				l.fireEvent(new GraphActionEvent(this, errMsgs.toArray(new String[0]), "error"));
//		}
//
	}

	
	private String getInBusitype(ArrayList<String> alBusitypes) {
		// 在调用该方法的上下文中已经保证了alBusitypes != null && alBusityoes.size()>0
		StringBuffer sb = new StringBuffer(" ");
		for (String busitype : alBusitypes) {
			sb.append(",'");
			sb.append(busitype);
			sb.append("'");
		}
		String inBusitype = sb.substring(1);
		return inBusitype;
	}
	
//	/**
//	 * 得到核心单据编码。和开始端相连的单据为核心单据 。同时将非核心单据的是否自制属性设置为N
//	 * */
//	private String getPrimaryBillCode(GraphElementHolder[] elements){
//		String primarybillid="";
//		Object[] edges =component.getGraph().getChildEdges(component.getGraph().getDefaultParent());
//		
//		for(Object edge:edges){
//			if(((UfGraphCell)((UfGraphCell)edge).getSource()).getValue() instanceof StartCellInfo){
//				primarybillid =((UfGraphCell)edge).getTarget().getId();
//			}		
//		}
//		
//		for(GraphElementHolder element:elements){
//			if(element.getId().equals(primarybillid))
//				continue;
//			//非核心单据的是否自制属性设置为N
//			if(element.getUserobject() instanceof BilltypeCellInfo){
//				((BilltypeCellInfo)(element.getUserobject())).setMakebillflag(false);
//			}
//		}
//		
//		GraphElementHolder primarybillcell =getElementMap().get(primarybillid);
//		if(primarybillcell!=null&&primarybillcell.getUserobject() instanceof BilltypeCellInfo){
//			BilltypeCellInfo cellinfo =(BilltypeCellInfo) primarybillcell.getUserobject();
//			return StringUtil.isEmptyWithTrim(cellinfo.getTranstype())?cellinfo.getBilltypecode():cellinfo.getTranstype();
//		}
//		return null;
//	}
	
//	/**
//	 * 设置跳转的业务单据
//	 * */
//	private void setJumpBillbusiness(GraphElementHolder[] elements){
//		boolean jumpflag =((BuziflowBeanInfo)graphObject).getJumpflag();
//		if(!jumpflag){
//			//流程定义的跳转标示位为false
//			for(GraphElementHolder element:elements){
//				if(element.getUserobject() instanceof BilltypeCellInfo){
//					((BilltypeCellInfo)(element.getUserobject())).setJumpflag(false);
//				}
//			}
//		}else{			
//			Object[] edges =component.getGraph().getChildEdges(component.getGraph().getDefaultParent());
//			ArrayList<String> jumpbillids =new ArrayList<String>();
//			for(Object edge:edges){
//				if(((UfGraphCell)((UfGraphCell)edge).getTarget()).getValue() instanceof EndCellInfo){
//					//设置jumpflag标示位为true
//					UfGraphCell sourcecell =(UfGraphCell) ((UfGraphCell)edge).getSource();
//					if(sourcecell.getValue() instanceof BilltypeCellInfo){
//						//和endcell相连的业务单据
//						jumpbillids.add(sourcecell.getId());
//					}else if(sourcecell.getValue() instanceof RouterCellInfo){
//						//或者通过路由和endcell相连的单据 
//						int edgecount =sourcecell.getEdgeCount();
//						for(int i=0;i<edgecount;i++){
//							UfGraphCell edgefromRouter =(UfGraphCell) sourcecell.getEdgeAt(i);
//							if(edgefromRouter.getTarget()==sourcecell){
//								UfGraphCell jumpbillcell =(UfGraphCell) ((UfGraphCell)edgefromRouter).getSource();
//								if(jumpbillcell.getValue() instanceof BilltypeCellInfo){
//									jumpbillids.add(jumpbillcell.getId());
//								}
//							}
//						}
//					}
//				}
//			}
//			
//			for(GraphElementHolder element:elements){
//				if(element.getUserobject() instanceof BilltypeCellInfo &&jumpbillids.contains(element.getId())){
//					((BilltypeCellInfo)(element.getUserobject())).setJumpflag(true);
//				}
//			}	
//		}		
//	}
//	
	/**
	 * 
	 * */
//	private boolean needPropCodeAndNameDlg(){		
//		if(graphObject instanceof BuziflowBeanInfo){
//			String code =((BuziflowBeanInfo)graphObject).getBusicode();
//			String name =((BuziflowBeanInfo)graphObject).getBusiname();
//			if(StringUtil.isEmptyWithTrim(code)||StringUtil.isEmptyWithTrim(name))
//				return true;
//		}
//		return false;
//	}
//
//		
//	/**
//	 * 持久化前的特殊处理
//	 * */
//	private void customPersist(){
//		try {
//			Field[] fields = graphObject.getClass().getFields();
//			if(fields!=null){
//				for(Field field :fields){
//					if(field.getAnnotation(IgnoreProp.class)!=null){
//						field.set(graphObject, null);						
//					}
//				}
//			}
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//		}	
//	}
	
	
//	public String getPk_graph() {
//		return pk_graph;
//	}
//
//	public void setPk_graph(String pk_graph) {
//		this.pk_graph = pk_graph;
//	}
//
//	public IUfGraphUserOjbect getGraphObject() {
//		return graphObject;
//	}
//
//	public void setGraphObject(IUfGraphUserOjbect graphObject) {
//		this.graphObject = graphObject;
//	}


	
	
	
	

	
	@ListenerType(eventType = mxEvent.CELL_CONNECTED)
	public void invokeCellConnected(Object sender, mxEventObject evt) {
		mxCell edge = (mxCell) evt.getProperty("edge");
		mxCell previous = (mxCell) evt.getProperty("previous");
		boolean isSource = (Boolean) evt.getProperty("source");
		mxCell terminal = (mxCell) evt.getProperty("terminal");
		if (edge.getValue() instanceof Connector) {
			if (isSource) {
				if(!(terminal.getValue() instanceof Annotation)&&!(terminal.getValue() instanceof DataObject)){
					((Connector) edge.getValue()).setSource((FlowNode) terminal.getValue());
					if (previous != null )
						((FlowNode) previous.getValue()).getOutgoing().remove((Connector) edge.getValue());
					((FlowNode) terminal.getValue()).getOutgoing().add((Connector) edge.getValue());
				}
			} else {
				if(!(terminal.getValue()instanceof Annotation)&&!(terminal.getValue() instanceof DataObject)){
					((Connector) edge.getValue()).setTarget((FlowNode) terminal.getValue());
					if (previous != null)
						((FlowNode) previous.getValue()).getIncoming().remove((Connector) edge.getValue());
					((FlowNode) terminal.getValue()).getIncoming().add((Connector) edge.getValue());
				}
			}
		}
	}

	private BoundaryEvent convertToBoundary(mxCell currentCell, mxCell boundaryCell){
		if (currentCell.getValue() instanceof IntermediateCatchEvent){
			BoundaryEvent boundaryEvent = new BoundaryEvent();
			((CatchEvent)currentCell.getValue()).CloneEventDefinitation(boundaryEvent, currentCell.getValue());
			boundaryEvent.setAttachedToRef(((BaseElement)boundaryCell.getValue()).getId());
			((UfGraphCell) currentCell).setValue(boundaryEvent);
			return boundaryEvent;
		}
		return null;
	}
	
	private IntermediateCatchEvent convertToIntermediate(mxCell currentCell){
		if (currentCell.getValue() instanceof BoundaryEvent){
			IntermediateCatchEvent intermediateEvent = new IntermediateCatchEvent();
			((CatchEvent)currentCell.getValue()).CloneEventDefinitation(intermediateEvent, currentCell.getValue());
			((UfGraphCell) currentCell).setValue(intermediateEvent);
			return intermediateEvent;
		}
		return null;
	}
	
	@ListenerType(eventType = mxEvent.MOVE_CELLS)
	public void invokeMoveCell(Object sender, mxEventObject evt) {
		Object[] cells = (Object[]) evt.getProperty("cells");
		Boolean isCloned = (Boolean) evt.getProperty("clone");
		mxCell currentCell = null;
		mxCell bindedCell = null;

		if (isCloned) {
			for (Object obj : cells) {
				mxICell cell = ((mxICell) obj);
				//GraphElementHolder holder = new GraphElementHolder();
				//holder.setId(cell.getId());
				//getElementMap().put(cell.getId(), holder);
			}
		} else {
			for (Object cell : cells) {
				currentCell = (mxCell) cell;
				// 如果intermedia移动到活动的边上，则转换为boundary单元，存储Boundary信息
				if (currentCell.getValue() instanceof CatchEvent) {
					bindedCell = ((UfGraphCell) currentCell).getBoundaryCell();
					if (bindedCell != null) { // 转换为边界单元
						convertToBoundary(currentCell, bindedCell);
					} else { // 转换为中间单元
						convertToIntermediate(currentCell);
					}
				}
			}
		}
	}

	@ListenerType(eventType = mxEvent.LABEL_CHANGED)
	public void invokeCellLabelChanged(Object sender, mxEventObject evt) {
		// 删除掉原有
//		Object newvalue = evt.getProperty("value");
//		Object oldvalue = evt.getProperty("oldvalue");
//		mxCell cell = (mxCell) evt.getProperty("cell");
//		mxCell parentCell = (mxCell) cell.getParent();
//		BaseElement container = null;
//		if (parentCell == null || parentCell.getValue() == null) {
//			// 添加到主流程
//			container = getMainProcess();
//		} else if (parentCell.getValue() instanceof FlowElementsContainer || parentCell.getValue() instanceof SubProcess) {
//			container = (BaseElement) parentCell.getValue();
//		}
//		if (container instanceof FlowElementsContainer) {
//			((FlowElementsContainer) container).getFlowElements().remove(oldvalue);
//			if (cell.getValue() instanceof Pool){
//				
//			}else{
//				((FlowElementsContainer) container).getFlowElements().add((FlowElement) newvalue);
//			}
//		} else if (container instanceof SubProcess) {
//			((SubProcess) container).getFlowElements().remove(oldvalue);
//			((SubProcess) container).getFlowElements().add((FlowElement) newvalue);
//		}
	}

	@Override
	public String[] getListenTargetType() {
		return new String[] { 
				mxEvent.CELL_CONNECTED, 
				mxEvent.MOVE_CELLS, 
				mxEvent.SPLIT_EDGE, 
				mxEvent.REMOVE_CELLS, 
				mxEvent.CELLS_ADDED, 
				mxEvent.LABEL_CHANGED, 
				mxEvent.GROUP_CELLS,
				mxEvent.UNGROUP_CELLS ,
				mxEvent.CHANGE};
	}
//	@Override
//	public String[] getListenTargetType() {
//		// TODO Auto-generated method stub
//		return new String[] { mxEvent.REMOVE_CELLS, mxEvent.MOVE_CELLS,mxEvent.SPLIT_EDGE};
//	}

}
