package uap.ui.participant.designer;

import java.awt.Component;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.org.IOrgUnitQryService;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.component.CorpRoleUserTreeCellRender;
import nc.ui.wfengine.designer.editors.PerformerNameMaker;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.GroupVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.pfworkflow.LocalBillTypeVO;
import nc.vo.pub.workflowusergroup.WFUserGroupVO;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.pf.virtualrole.AllBillMaker;
import nc.vo.wfengine.core.activity.TransferObjOfDesigner;

/**
 * 组织机构树  
 * 可直接拖放到工作流设计器中作为参与者图元。
 * 
 * @author 雷军 created on 2004-9-5 15:31:32
 * @modifier 雷军 2005-5-20 增加重新刷新树的方法
 * @modifier 雷军 2005-5-25 新增了CellRender用于切换图标
 */
public abstract class OrganizationTree extends UITree implements DragGestureListener,
		DragSourceListener, MouseListener {
	/** 拖动源 */
	private DragSource _dragSource = null;

	/** 可传输的对象 */
	private TransferObjOfDesigner _transferObject = null;

	/** 拖拽开始事件处理器 */
	private transient IDragStartHandler _dragStartHandler = null;

	/** 装载组织机构树的ToftPanel容器，即功能节点对应的UI */
	protected transient Component _mainUI = null;

	protected ArrayList _alOrgNodes = new ArrayList();

	/** 已建账所有公司 */
	private Vector m_vecOrgsOfThisGroup = null;

	public OrganizationTree(IDragStartHandler dsHandler, Component mainUI) {
		super();
		//树结构 显示
		setShowsRootHandles(true);
		setRootVisible(false);
		setSelectionRow(-1);

		putClientProperty("JTree.lineStyle", "Angled");

		_mainUI = mainUI;
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		_dragSource = DragSource.getDefaultDragSource();
		_dragSource.createDefaultDragGestureRecognizer(this, //component
				java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE, //actions
				this); //DragGestureListener
		_dragStartHandler = dsHandler;

		//侦听器
		this.addMouseListener(this);
		//渲染器
		this.setCellRenderer(new CorpRoleUserTreeCellRender());
		//搜索器
		getTreeNodeSearcher().setWildcardEnabled(true);

	}

	/**
	 * 构造树模型
	 */
	public void constructTreeModel() {
		DefaultTreeModel model = populateTreeModel();
		model.setAsksAllowsChildren(false);
		setModel(model);
	}

	protected String getSelectedBillNodecode() {
		String funcodeOfBillType = null;
		//TODO
		/*
		if (_mainUI instanceof UfWorkflowDesignerUI) {
			LocalBillTypeVO localBT = ((UfWorkflowDesignerUI) _mainUI).getCurrentBillType();
			funcodeOfBillType = localBT.getNodeCode();
		}
		*/
		return funcodeOfBillType;
	}

	/**
	 * @param hint
	 */
	protected void showHintMessage(String hint) {		
		if(_mainUI instanceof PerformerNameMaker){
			((PerformerNameMaker)_mainUI).showHintMsg(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000168", null, new String[]{hint})/*提示:{0}*/);
		}
	}

	/**
	 * 获得所有已经建账的所有公司VO数组,为组织机构树共用
	 */
	public Vector getOrgsOfThisGroup() {
		if (m_vecOrgsOfThisGroup == null) {
			m_vecOrgsOfThisGroup = new Vector();
			try {
				IOrgUnitQryService corpService = (IOrgUnitQryService) NCLocator.getInstance().lookup(
						IOrgUnitQryService.class.getName());

				OrgVO[] aryCorps = corpService.queryAllOrgUnitVOsByGroupID(WorkbenchEnvironment
						.getInstance().getGroupVO().getPk_group(), false, true);
				for (int i = 0; i < (aryCorps == null ? 0 : aryCorps.length); i++) {
					m_vecOrgsOfThisGroup.addElement(aryCorps[i]);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return m_vecOrgsOfThisGroup;
	}

	protected abstract DefaultTreeModel populateTreeModel();

	/**
	 * 为根结点添加"业务单元"子节点
	 * @param root
	 */
	public void addOrgNodes(DefaultMutableTreeNode root) {
		Vector orgs = getOrgsOfThisGroup();
		DefaultMutableTreeNode orgNode = null;
		OrganizeUnit orgUnit = null;
		for (int i = 0; i < orgs.size(); i++) {

			OrgVO orgVO = (OrgVO) orgs.get(i);
			orgUnit = new OrganizeUnit(orgVO);
			orgNode = new DefaultMutableTreeNode(orgUnit);
			root.add(orgNode);

			_alOrgNodes.add(orgNode);
		}
		
	}

	

	/**
	 * 为根结点添加"集团"子节点
	 * <li>同时增加所有"业务单元"子节点
	 * 
	 * @param root
	 */
	public void addGroupNode(DefaultMutableTreeNode root) {
		GroupVO groupVO = new GroupVO();
		groupVO.setPk_group(WorkbenchEnvironment.getInstance().getGroupVO().getPk_group());
		groupVO.setCode(WorkbenchEnvironment.getInstance().getGroupVO().getCode());
		groupVO.setName(WorkbenchEnvironment.getInstance().getGroupVO().getName());
		OrganizeUnit orgUnit = new OrganizeUnit(groupVO);

		DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(orgUnit);
		root.add(groupNode);

		//
		_alOrgNodes.add(groupNode);

		//同时增加所有"业务单元"子节点
		addOrgNodes(root);
	}

	/**
	 * 移除公司节点下的所有子节点,设置为"未查询",刷新树
	 */
	public void resetLoad() {
		for (int i = 0; i < _alOrgNodes.size(); i++) {
			DefaultMutableTreeNode corpNode = (DefaultMutableTreeNode) _alOrgNodes.get(i);
			OrganizeUnit corpTreeVo = (OrganizeUnit) corpNode.getUserObject();
			corpTreeVo.setQueryed(false);
			corpNode.removeAllChildren();
			//XXX:leijun@1008-8 对于公司数据上千个的情况下，存在效率问题
			//this.updateUI();
		}
		this.updateUI();
	}

	/**
	 * dragGestureRecognized
	 *
	 * @param dge DragGestureEvent
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {

		boolean isAltAndCtrlDown = false;
		int dragType = IDragStartHandler.DRAG_TYPE_MOVE;

		//取得构成该拖拽事件的第一个鼠标事件
		Iterator eventsComrisingTheGesture = dge.iterator();
		if (eventsComrisingTheGesture.hasNext()) {
			MouseEvent mevent = (MouseEvent) eventsComrisingTheGesture.next();
			if (mevent.isAltDown() && mevent.isControlDown()) {
				//如果按下了Ctrl+Alt键进行拖拽，则被认为是替换活动的参与者
				isAltAndCtrlDown = true;
			}
		}

		//set cursor
		java.awt.Cursor cursor = null;
		switch (dge.getDragAction()) {
			case DnDConstants.ACTION_MOVE:
				dragType = IDragStartHandler.DRAG_TYPE_MOVE;
				cursor = DragSource.DefaultMoveDrop;
				break;
			case DnDConstants.ACTION_COPY:
				dragType = IDragStartHandler.DRAG_TYPE_COPY;
				cursor = DragSource.DefaultCopyDrop;
				break;
			default:
				cursor = DragSource.DefaultMoveNoDrop;
		}

		//cursor = DragSource.DefaultCopyDrop; //set cursor
		java.awt.Point jap = dge.getDragOrigin(); //drag point
		int x = (int) jap.getX();
		int y = (int) jap.getY();
		javax.swing.tree.TreePath tp = this.getPathForLocation(x, y);

		//获得托拽的树节点
		if (tp != null) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
			//获得传输对象
			_transferObject = getTransferObject(treeNode);
		}
		if (_transferObject != null) {
			//NOTE::只有传输对象非空，才可开始托拽
			_transferObject.setAltCtrlDown(isAltAndCtrlDown);
			dge.startDrag(cursor, //cursor
					_transferObject, //transferable
					this); //DragSourceListener

			//通知拖拽开始
			if (isAltAndCtrlDown)
				//NOTE::REPLACE拖拽从属于COPY拖拽
				dragType = IDragStartHandler.DRAG_TYPE_REPLACE;
			if (_dragStartHandler != null)
				_dragStartHandler.dragStarting(dragType);
		}
	}

	public abstract TransferObjOfDesigner getTransferObject(TreeNode treeNode);

	// ============================================
	// DragSourceListener接口实现；
	// 由于不关心与拖动源相关的事件，所以这里都是空方法。
	// ============================================
	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() < 2 || this.getSelectionPath() == null)
			return;
		showHintMessage("");

		DefaultMutableTreeNode orgNode = (DefaultMutableTreeNode) this.getSelectionPath()
				.getLastPathComponent();
		Object userObj = orgNode.getUserObject();
		
		
		if(userObj instanceof AllBillMaker || userObj instanceof WFUserGroupVO){
			if(_mainUI instanceof PerformerNameMaker){
				((PerformerNameMaker)_mainUI).onOk();
			}
			return;
		}
		
		if (!(userObj instanceof OrganizeUnit))
			return;

		OrganizeUnit localOrg = (OrganizeUnit) userObj;
		
		if(isOrganizeUnitTypes(localOrg)){
			//双击即确定
			if(_mainUI instanceof PerformerNameMaker){
				((PerformerNameMaker)_mainUI).onOk();
			}
		}
		if (localOrg.getOrgUnitType() != OrganizeUnitTypes.Group_INT
				&& localOrg.getOrgUnitType() != OrganizeUnitTypes.Org_INT)
			return;

		//获得当前单据类型关联的节点编码
		final String funCode = getSelectedBillNodecode();

		//如果当前功能结点号为空，警告一下
		if (StringUtil.isEmptyWithTrim(funCode)) {
			Logger.warn(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000170")/*当前单据类型的nodecode为空*/);
		}

		//如果已经查询过了
		if (localOrg.isQueryed()) {
			if (orgNode.getChildCount() != 0) {
//				showHintMessage(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000066")/*@res "请选中组或人员拖动到单据面板上"*/);
				return;
			} else {
				showHintMessage(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000067")/*@res "该公司没有定义有权限审核单据的组和用户"*/);
				return;
			}
		}

		//调用子类的处理
		nodeDoubleClicked(funCode, localOrg.getPkOrg(), orgNode);

		//标识已经查询过了
		localOrg.setQueryed(true);
		//显示树路径
		TreePath currpath = this.getSelectionPath();
		this.expandPath(currpath);
		this.makeVisible(currpath);
		this.updateUI();

		//延迟加载后重置树搜索器
		getTreeNodeSearcher().refreshSearchContent();
	}
	
	public abstract boolean isOrganizeUnitTypes(OrganizeUnit localOrg);

	/**
	 * @param funCode
	 * @param pkOrg
	 * @param orgNode
	 */
	public abstract void nodeDoubleClicked(String funCode, String pkOrg,
			DefaultMutableTreeNode orgNode);
	
	

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}