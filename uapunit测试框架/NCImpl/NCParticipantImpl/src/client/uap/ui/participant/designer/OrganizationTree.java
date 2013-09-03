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
 * ��֯������  
 * ��ֱ���Ϸŵ����������������Ϊ������ͼԪ��
 * 
 * @author �׾� created on 2004-9-5 15:31:32
 * @modifier �׾� 2005-5-20 ��������ˢ�����ķ���
 * @modifier �׾� 2005-5-25 ������CellRender�����л�ͼ��
 */
public abstract class OrganizationTree extends UITree implements DragGestureListener,
		DragSourceListener, MouseListener {
	/** �϶�Դ */
	private DragSource _dragSource = null;

	/** �ɴ���Ķ��� */
	private TransferObjOfDesigner _transferObject = null;

	/** ��ק��ʼ�¼������� */
	private transient IDragStartHandler _dragStartHandler = null;

	/** װ����֯��������ToftPanel�����������ܽڵ��Ӧ��UI */
	protected transient Component _mainUI = null;

	protected ArrayList _alOrgNodes = new ArrayList();

	/** �ѽ������й�˾ */
	private Vector m_vecOrgsOfThisGroup = null;

	public OrganizationTree(IDragStartHandler dsHandler, Component mainUI) {
		super();
		//���ṹ ��ʾ
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

		//������
		this.addMouseListener(this);
		//��Ⱦ��
		this.setCellRenderer(new CorpRoleUserTreeCellRender());
		//������
		getTreeNodeSearcher().setWildcardEnabled(true);

	}

	/**
	 * ������ģ��
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
			((PerformerNameMaker)_mainUI).showHintMsg(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000168", null, new String[]{hint})/*��ʾ:{0}*/);
		}
	}

	/**
	 * ��������Ѿ����˵����й�˾VO����,Ϊ��֯����������
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
	 * Ϊ��������"ҵ��Ԫ"�ӽڵ�
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
	 * Ϊ��������"����"�ӽڵ�
	 * <li>ͬʱ��������"ҵ��Ԫ"�ӽڵ�
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

		//ͬʱ��������"ҵ��Ԫ"�ӽڵ�
		addOrgNodes(root);
	}

	/**
	 * �Ƴ���˾�ڵ��µ������ӽڵ�,����Ϊ"δ��ѯ",ˢ����
	 */
	public void resetLoad() {
		for (int i = 0; i < _alOrgNodes.size(); i++) {
			DefaultMutableTreeNode corpNode = (DefaultMutableTreeNode) _alOrgNodes.get(i);
			OrganizeUnit corpTreeVo = (OrganizeUnit) corpNode.getUserObject();
			corpTreeVo.setQueryed(false);
			corpNode.removeAllChildren();
			//XXX:leijun@1008-8 ���ڹ�˾������ǧ��������£�����Ч������
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

		//ȡ�ù��ɸ���ק�¼��ĵ�һ������¼�
		Iterator eventsComrisingTheGesture = dge.iterator();
		if (eventsComrisingTheGesture.hasNext()) {
			MouseEvent mevent = (MouseEvent) eventsComrisingTheGesture.next();
			if (mevent.isAltDown() && mevent.isControlDown()) {
				//���������Ctrl+Alt��������ק������Ϊ���滻��Ĳ�����
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

		//�����ק�����ڵ�
		if (tp != null) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
			//��ô������
			_transferObject = getTransferObject(treeNode);
		}
		if (_transferObject != null) {
			//NOTE::ֻ�д������ǿգ��ſɿ�ʼ��ק
			_transferObject.setAltCtrlDown(isAltAndCtrlDown);
			dge.startDrag(cursor, //cursor
					_transferObject, //transferable
					this); //DragSourceListener

			//֪ͨ��ק��ʼ
			if (isAltAndCtrlDown)
				//NOTE::REPLACE��ק������COPY��ק
				dragType = IDragStartHandler.DRAG_TYPE_REPLACE;
			if (_dragStartHandler != null)
				_dragStartHandler.dragStarting(dragType);
		}
	}

	public abstract TransferObjOfDesigner getTransferObject(TreeNode treeNode);

	// ============================================
	// DragSourceListener�ӿ�ʵ�֣�
	// ���ڲ��������϶�Դ��ص��¼����������ﶼ�ǿշ�����
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
			//˫����ȷ��
			if(_mainUI instanceof PerformerNameMaker){
				((PerformerNameMaker)_mainUI).onOk();
			}
		}
		if (localOrg.getOrgUnitType() != OrganizeUnitTypes.Group_INT
				&& localOrg.getOrgUnitType() != OrganizeUnitTypes.Org_INT)
			return;

		//��õ�ǰ�������͹����Ľڵ����
		final String funCode = getSelectedBillNodecode();

		//�����ǰ���ܽ���Ϊ�գ�����һ��
		if (StringUtil.isEmptyWithTrim(funCode)) {
			Logger.warn(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000170")/*��ǰ�������͵�nodecodeΪ��*/);
		}

		//����Ѿ���ѯ����
		if (localOrg.isQueryed()) {
			if (orgNode.getChildCount() != 0) {
//				showHintMessage(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000066")/*@res "��ѡ�������Ա�϶������������"*/);
				return;
			} else {
				showHintMessage(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000067")/*@res "�ù�˾û�ж�����Ȩ����˵��ݵ�����û�"*/);
				return;
			}
		}

		//��������Ĵ���
		nodeDoubleClicked(funCode, localOrg.getPkOrg(), orgNode);

		//��ʶ�Ѿ���ѯ����
		localOrg.setQueryed(true);
		//��ʾ��·��
		TreePath currpath = this.getSelectionPath();
		this.expandPath(currpath);
		this.makeVisible(currpath);
		this.updateUI();

		//�ӳټ��غ�������������
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