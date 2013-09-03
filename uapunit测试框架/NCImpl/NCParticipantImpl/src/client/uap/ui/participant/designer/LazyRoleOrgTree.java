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
 * 延迟加载的角色 树
 * <li>借鉴于nc.ui.pub.pfworkflow.RoleCheckerTree类
 *
 * @author 雷军 created on 2004-12-28
 * @modififer leijun 2006-4-17 NC50使用新的角色
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
				.getStrByID("101203", "UPP101203-000062")/*@res "有权限角色"*/);
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
		//XXX::必须保证造型正确
		OrganizeUnit orgUnit = (OrganizeUnit) ((DefaultMutableTreeNode) treeNode).getUserObject();

		// 构建传输对象
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
		//	查询出当前公司的角色
		HashMap<RoleGroupVO,RoleVO[]> alRoles = new HashMap<RoleGroupVO,RoleVO[]> ();
		fetchRoles(pkCorp, alRoles);
		//如果查询出的角色为空,则状态栏提示并返回
		if (alRoles.size() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("101203", "UPP101203-000064")/*@res "该公司没有定义角色"*/);
			return;
		}
		DefaultMutableTreeNode roleGroupNode = null;
		//添加角色树节点
		OrganizeUnit treerolegroupvo = null;
		Iterator it = alRoles.entrySet().iterator();
		while(it.hasNext()){
			Entry entry = (Entry)it.next();
			RoleGroupVO  vo = (RoleGroupVO)entry.getKey();
			RoleVO[] vos = alRoles.get(vo);
			/**获取角色组*/
			treerolegroupvo = new OrganizeUnit(vo);
			roleGroupNode = new DefaultMutableTreeNode(treerolegroupvo);
			roleGroupNode.setAllowsChildren(true);	
			/**循环添加角色*/
			for(int m=0 ; m<vos.length ; m++){
				RoleVO rolevo = (RoleVO)vos[m];
				OrganizeUnit treerolevo = new OrganizeUnit(rolevo);
				roleGroupNode.add(new DefaultMutableTreeNode(treerolevo));
			}
			corpNode.add(roleGroupNode);
		}
	}

	
	protected void fetchRoles(String currCorpPK, HashMap<RoleGroupVO,RoleVO[]> alUsers) {
		//WARN::获得该公司创建的所有角色
		RoleVO[] rolevos = null;
		RoleGroupVO[] rolegroupvos = null;
		try {
			IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator.getInstance().lookup(
					IRoleManageQuery.class.getName());
			/**获取组织单元下的所有业务角色组*/
			rolegroupvos = roleBS.queryRoleGroupByOrgWithoutAuthPerm(currCorpPK,1);
			/**循环从角色组中得到角色,并把它们保存到对应的角色组中*/
			
			for (int i = 0; i < (rolegroupvos == null ? 0 : rolegroupvos.length); i++) {
                rolevos = roleBS.queryRoleinGroup(rolegroupvos[i].getPk_role_group(),currCorpPK,rolegroupvos[i].getGroup_type());
				VOUtil.ascSort(rolevos, new String[]{"role_code"});
				/**key:角色组;value:对应的角色集合*/
				alUsers.put(rolegroupvos[i], rolevos);
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}
	// TODO: IPFRemoteInvocation在测试环境下行不通。先回滚，以后再查原因
//	protected void fetchRoles(String currCorpPK, HashMap<RoleGroupVO,RoleVO[]> alUsers) {
//		// yanke1 2012-5-23 将循环远程调用封装为单次远程调用
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
////		//WARN::获得该公司创建的所有角色
////		RoleVO[] rolevos = null;
////		RoleGroupVO[] rolegroupvos = null;
////		try {
////			IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator.getInstance().lookup(
////					IRoleManageQuery.class.getName());
////			/**获取组织单元下的所有业务角色组*/
////			rolegroupvos = roleBS.queryRoleGroupByOrgWithoutAuthPerm(currCorpPK,1);
////			/**循环从角色组中得到角色,并把它们保存到对应的角色组中*/
////			
////			for (int i = 0; i < (rolegroupvos == null ? 0 : rolegroupvos.length); i++) {
////                rolevos = roleBS.queryRoleinGroup(rolegroupvos[i].getPk_role_group(),currCorpPK,rolegroupvos[i].getGroup_type());
////				VOUtil.ascSort(rolevos, new String[]{"role_code"});
////				/**key:角色组;value:对应的角色集合*/
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