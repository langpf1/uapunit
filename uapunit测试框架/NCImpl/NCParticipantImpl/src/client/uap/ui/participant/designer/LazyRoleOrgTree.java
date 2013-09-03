package uap.ui.participant.designer;


import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.IPFRemoteInvocation;
import nc.itf.uap.pf.IPFRemoteService;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.vo.pf.remote.LazyRoleOrgTree_fetchRoles;
import nc.vo.pub.BusinessException;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.rbac.constant.IRoleConst;
import nc.vo.uap.rbac.role.RoleGroupVO;
import nc.vo.uap.rbac.role.RoleVO;
import nc.vo.wfengine.core.activity.OrganizeTransferObj;
import nc.vo.wfengine.core.activity.TransferObjOfDesigner;

/**
 * �ӳټ��صĽ�ɫ ��
 * <li>�����nc.ui.pub.pfworkflow.RoleCheckerTree��
 *
 * @author �׾� created on 2004-12-28
 * @modififer leijun 2006-4-17 NC50ʹ���µĽ�ɫ
 */
public class LazyRoleOrgTree extends OrganizationTree {

	/**
	 * @param dsHandler
	 * @param mainUI
	 */
	public LazyRoleOrgTree(IDragStartHandler dsHandler, Component mainUI) {
		super(dsHandler, mainUI);
	}

	/* (non-Javadoc)
	 * @see nc.ui.wfengine.designer.OrganizationTree#populateTreeModel()
	 */
	protected DefaultTreeModel populateTreeModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("101203", "UPP101203-000062")/*@res "��Ȩ�޽�ɫ"*/);
		DefaultTreeModel model = new DefaultTreeModel(root);
		addGroupNode(root);
//        addCorpNodes(root);
		return model;
	}

	/* (non-Javadoc)
	 * @see nc.ui.wfengine.designer.OrganizationTree#getTransferObject(javax.swing.tree.TreeNode)
	 */
	public TransferObjOfDesigner getTransferObject(TreeNode treeNode) {
		if (treeNode == null)
			return null;
		OrganizeTransferObj transferObj = null;
		//XXX::���뱣֤������ȷ
		OrganizeUnit orgUnit = (OrganizeUnit) ((DefaultMutableTreeNode) treeNode).getUserObject();

		// �����������
		if (orgUnit.getOrgUnitType() == OrganizeUnitTypes.Role_INT) {
			transferObj = new OrganizeTransferObj();
			transferObj.setOrgUnitName(orgUnit.getName());
			transferObj.setOrgUnitType(OrganizeUnitTypes.Role.toString());
			transferObj.setOrgUnitTypeName(OrganizeUnitTypes.Role.showName());
			transferObj.setOrgUnitPK(orgUnit.getPk());
			transferObj.setBelongOrg(orgUnit.getPkOrg());
			transferObj.setIconOfMakebill(OrganizeUnitTypes.Role.defaultMakeBillIconClsPath());
			transferObj.setIconPath(OrganizeUnitTypes.Role.defaultCheckBillIconClsPath());
		}else if(orgUnit.getOrgUnitType() == OrganizeUnitTypes.RoleGroup_INT) {
			transferObj = new OrganizeTransferObj();
			transferObj.setOrgUnitName(orgUnit.getName());
			transferObj.setOrgUnitType(OrganizeUnitTypes.RoleGroup.toString());
			transferObj.setOrgUnitTypeName(OrganizeUnitTypes.RoleGroup.showName());
			transferObj.setOrgUnitPK(orgUnit.getPk());
			transferObj.setBelongOrg(orgUnit.getPkOrg());
			transferObj.setIconOfMakebill(OrganizeUnitTypes.RoleGroup.defaultMakeBillIconClsPath());
			transferObj.setIconPath(OrganizeUnitTypes.RoleGroup.defaultCheckBillIconClsPath());
		}
		return transferObj;
	}

	/* (non-Javadoc)
	 * @see nc.ui.wfengine.designer.OrganizationTree#nodeDoubleClicked(java.lang.String, java.lang.String, javax.swing.tree.DefaultMutableTreeNode)
	 */
	public void nodeDoubleClicked(String funCode, String pkCorp, DefaultMutableTreeNode corpNode) {
		//	��ѯ����ǰ��˾�Ľ�ɫ
		HashMap<RoleGroupVO,RoleVO[]> alRoles = new HashMap<RoleGroupVO,RoleVO[]> ();
		fetchRoles(pkCorp, alRoles);
		//�����ѯ���Ľ�ɫΪ��,��״̬����ʾ������
		if (alRoles.size() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("101203", "UPP101203-000064")/*@res "�ù�˾û�ж����ɫ"*/);
			return;
		}
		DefaultMutableTreeNode roleGroupNode = null;
		//��ӽ�ɫ���ڵ�
		OrganizeUnit treerolegroupvo = null;
		Iterator it = alRoles.entrySet().iterator();
		while(it.hasNext()){
			Entry entry = (Entry)it.next();
			RoleGroupVO  vo = (RoleGroupVO)entry.getKey();
			RoleVO[] vos = alRoles.get(vo);
			/**��ȡ��ɫ��*/
			treerolegroupvo = new OrganizeUnit(vo);
			roleGroupNode = new DefaultMutableTreeNode(treerolegroupvo);
			roleGroupNode.setAllowsChildren(true);	
			/**ѭ����ӽ�ɫ*/
			for(int m=0 ; m<vos.length ; m++){
				RoleVO rolevo = (RoleVO)vos[m];
				OrganizeUnit treerolevo = new OrganizeUnit(rolevo);
				roleGroupNode.add(new DefaultMutableTreeNode(treerolevo));
			}
			corpNode.add(roleGroupNode);
		}
	}

	
	protected void fetchRoles(String currCorpPK, HashMap<RoleGroupVO,RoleVO[]> alUsers) {
		//WARN::��øù�˾���������н�ɫ
		RoleVO[] rolevos = null;
		RoleGroupVO[] rolegroupvos = null;
		try {
			IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator.getInstance().lookup(
					IRoleManageQuery.class.getName());
			/**��ȡ��֯��Ԫ�µ�����ҵ���ɫ��*/
			rolegroupvos = roleBS.queryRoleGroupByOrgWithoutAuthPerm(currCorpPK,1);
			/**ѭ���ӽ�ɫ���еõ���ɫ,�������Ǳ��浽��Ӧ�Ľ�ɫ����*/
			
			for (int i = 0; i < (rolegroupvos == null ? 0 : rolegroupvos.length); i++) {
                rolevos = roleBS.queryRoleinGroup(rolegroupvos[i].getPk_role_group(),currCorpPK,rolegroupvos[i].getGroup_type());
				VOUtil.ascSort(rolevos, new String[]{"role_code"});
				/**key:��ɫ��;value:��Ӧ�Ľ�ɫ����*/
				alUsers.put(rolegroupvos[i], rolevos);
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}
	// TODO: IPFRemoteInvocation�ڲ��Ի������в�ͨ���Ȼع����Ժ��ٲ�ԭ��
//	protected void fetchRoles(String currCorpPK, HashMap<RoleGroupVO,RoleVO[]> alUsers) {
//		// yanke1 2012-5-23 ��ѭ��Զ�̵��÷�װΪ����Զ�̵���
//		try {
//			IPFRemoteInvocation invocation = new LazyRoleOrgTree_fetchRoles(currCorpPK);
//
//			IPFRemoteService srv = NCLocator.getInstance().lookup(IPFRemoteService.class);
//			HashMap<RoleGroupVO, RoleVO[]> map = (HashMap<RoleGroupVO, RoleVO[]>) srv.doRemote(invocation);
//
//			alUsers.putAll(map);
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//		}
//		
////		//WARN::��øù�˾���������н�ɫ
////		RoleVO[] rolevos = null;
////		RoleGroupVO[] rolegroupvos = null;
////		try {
////			IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator.getInstance().lookup(
////					IRoleManageQuery.class.getName());
////			/**��ȡ��֯��Ԫ�µ�����ҵ���ɫ��*/
////			rolegroupvos = roleBS.queryRoleGroupByOrgWithoutAuthPerm(currCorpPK,1);
////			/**ѭ���ӽ�ɫ���еõ���ɫ,�������Ǳ��浽��Ӧ�Ľ�ɫ����*/
////			
////			for (int i = 0; i < (rolegroupvos == null ? 0 : rolegroupvos.length); i++) {
////                rolevos = roleBS.queryRoleinGroup(rolegroupvos[i].getPk_role_group(),currCorpPK,rolegroupvos[i].getGroup_type());
////				VOUtil.ascSort(rolevos, new String[]{"role_code"});
////				/**key:��ɫ��;value:��Ӧ�Ľ�ɫ����*/
////				alUsers.put(rolegroupvos[i], rolevos);
////			}
////
////		} catch (BusinessException e) {
////			Logger.error(e.getMessage(), e);
////		}
//
//	}

	@Override
	public boolean isOrganizeUnitTypes(OrganizeUnit localOrg) {
		int type = localOrg.getOrgUnitType();
		if (type == OrganizeUnitTypes.Role_INT || type == OrganizeUnitTypes.VIRTUALROLE_INT)
			return true;
		return false;
	}
}