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
import nc.itf.uap.pf.IPFOrgUnit;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.ml.NCLangRes;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.pf.OrganizeUnitTypes;
import nc.vo.uap.rbac.constant.INCSystemUserConst;
import nc.vo.uap.rbac.UserGroupVO;
import nc.vo.wfengine.core.activity.OrganizeTransferObj;
import nc.vo.wfengine.core.activity.TransferObjOfDesigner;

/**
 * 延迟加载的用户/组 树
 * <li>借鉴于nc.ui.pub.pfworkflow.PersonCheckerTree类
 *
 * @author leijun created on 2004-12-28
 * @modififer leijun 2006-4-17 NC50废弃用户组
 */
public class LazyUserOrgTree extends OrganizationTree {
	/**
	 * @param dsHandler
	 * @param mainUI
	 */
	public LazyUserOrgTree(IDragStartHandler dsHandler, Component mainUI) {
		super(dsHandler, mainUI);
	}

	protected DefaultTreeModel populateTreeModel() {
		//只是获取已建帐公司,并作为树节点显示
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"101203", "UPP101203-000065")/*@res "有权限人员和组"*/);
		DefaultTreeModel model = new DefaultTreeModel(root);
		/**集团*/
		addGroupNode(root);
		//NC系统用户
		addNCSystemUser(root);
		return model;
	}
	
	private void addNCSystemUser(DefaultMutableTreeNode root) {
		IUserManageQuery userBS = (IUserManageQuery) NCLocator.getInstance().lookup(
				IUserManageQuery.class);
		try {
			UserVO sysUser = userBS.getUser(INCSystemUserConst.NC_USER_PK);
			if(sysUser == null)
				return;
			OrganizeUnit orgUnit = new OrganizeUnit(sysUser);
			root.add(new DefaultMutableTreeNode(orgUnit));
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		
	}

	public TransferObjOfDesigner getTransferObject(TreeNode treeNode) {
		OrganizeUnit orgUnit = (OrganizeUnit) ((DefaultMutableTreeNode) treeNode).getUserObject();
		// 构建传输对象
		OrganizeTransferObj transferObj = null;

		int type = orgUnit.getOrgUnitType();

		if (type == OrganizeUnitTypes.Operator_INT) {
			// 托拽操作员到设计器中
			transferObj = new OrganizeTransferObj();
			transferObj.setOrgUnitName(orgUnit.getName());
			transferObj.setOrgUnitType(OrganizeUnitTypes.Operator.toString());
			transferObj.setOrgUnitTypeName(OrganizeUnitTypes.Operator.showName());
			transferObj.setOrgUnitPK(orgUnit.getPk());
			transferObj.setBelongOrg(orgUnit.getPkOrg());
			transferObj.setIconOfMakebill(OrganizeUnitTypes.Operator.defaultMakeBillIconClsPath());
			transferObj.setIconPath(OrganizeUnitTypes.Operator.defaultCheckBillIconClsPath());
		} else if (type == OrganizeUnitTypes.UserGroup_INT) {
			/**用户组*/
			transferObj = new OrganizeTransferObj();
			transferObj.setOrgUnitName(orgUnit.getName());
			transferObj.setOrgUnitType(OrganizeUnitTypes.UserGroup.toString());
			transferObj.setOrgUnitTypeName(OrganizeUnitTypes.UserGroup.showName());
			transferObj.setOrgUnitPK(orgUnit.getPk());
			transferObj.setBelongOrg(orgUnit.getPkOrg());
			transferObj.setIconOfMakebill(OrganizeUnitTypes.UserGroup.defaultMakeBillIconClsPath());
			transferObj.setIconPath(OrganizeUnitTypes.UserGroup.defaultCheckBillIconClsPath());
		}
		return transferObj;
	}

	/**
	 * 查询出可登陆当前公司的所有用户，不仅仅包括本公司创建的用户
	 * @param pk_org
	 * @param alUsers
	 */
	protected void fetchUsers(String pk_org, HashMap<UserGroupVO, UserVO[]> alUsers) {
		//改为后台一次性获取 modified by zhangrui 2012-04-19
		IPFOrgUnit orgUnitService = NCLocator.getInstance().lookup(IPFOrgUnit.class);
		try {
			alUsers.putAll(orgUnitService.getUserGroupMap(pk_org));
		} catch (BusinessException e) {
			Logger.error(e);
		}
	}

	public void nodeDoubleClicked(String funCode, String pk_org, DefaultMutableTreeNode corpNode) {
		//查询出当前公司的所有用户
		HashMap<UserGroupVO, UserVO[]> usermap = new HashMap<UserGroupVO, UserVO[]>();
		fetchUsers(pk_org, usermap);

		//如果查询出的用户为空,则状态栏提示并返回
		if (usermap.size() == 0) {
			showHintMessage(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000067")/*@res "该公司没有定义用户"*/);
			return;
		}
		DefaultMutableTreeNode userGroupNode = null;
		//添加组和用户树节点
		//添加用户树节点
		OrganizeUnit treeusergroupvo = null;
		
		UserGroupVO[] vogroups =  usermap.keySet().toArray(new UserGroupVO[0]);
		VOUtil.ascSort(vogroups, new String[] { "groupcode" });
		
		for(UserGroupVO vo:vogroups) {
			UserVO[] vos = usermap.get(vo);
			/**获取用户组*/
			treeusergroupvo = new OrganizeUnit(vo);
			userGroupNode = new DefaultMutableTreeNode(treeusergroupvo);
			userGroupNode.setAllowsChildren(true);
			
			/**循环添加用户*/
			for (int m = 0 ,end =vos==null?0:vos.length; m < end; m++) {
				UserVO uservo = (UserVO) vos[m];
				OrganizeUnit treeuservo = new OrganizeUnit(uservo);
				userGroupNode.add(new DefaultMutableTreeNode(treeuservo));

			}
			corpNode.add(userGroupNode);
		}

	}

	@Override
	public boolean isOrganizeUnitTypes(OrganizeUnit localOrg) {
		int type = localOrg.getOrgUnitType();
		if (type == OrganizeUnitTypes.Operator_INT || type == OrganizeUnitTypes.VIRTUALROLE_INT)
			return true;
		return false;
	}
}