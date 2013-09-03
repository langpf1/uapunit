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
 * �ӳټ��ص��û�/�� ��
 * <li>�����nc.ui.pub.pfworkflow.PersonCheckerTree��
 *
 * @author leijun created on 2004-12-28
 * @modififer leijun 2006-4-17 NC50�����û���
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
		//ֻ�ǻ�ȡ�ѽ��ʹ�˾,����Ϊ���ڵ���ʾ
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"101203", "UPP101203-000065")/*@res "��Ȩ����Ա����"*/);
		DefaultTreeModel model = new DefaultTreeModel(root);
		/**����*/
		addGroupNode(root);
		//NCϵͳ�û�
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
		// �����������
		OrganizeTransferObj transferObj = null;

		int type = orgUnit.getOrgUnitType();

		if (type == OrganizeUnitTypes.Operator_INT) {
			// ��ק����Ա���������
			transferObj = new OrganizeTransferObj();
			transferObj.setOrgUnitName(orgUnit.getName());
			transferObj.setOrgUnitType(OrganizeUnitTypes.Operator.toString());
			transferObj.setOrgUnitTypeName(OrganizeUnitTypes.Operator.showName());
			transferObj.setOrgUnitPK(orgUnit.getPk());
			transferObj.setBelongOrg(orgUnit.getPkOrg());
			transferObj.setIconOfMakebill(OrganizeUnitTypes.Operator.defaultMakeBillIconClsPath());
			transferObj.setIconPath(OrganizeUnitTypes.Operator.defaultCheckBillIconClsPath());
		} else if (type == OrganizeUnitTypes.UserGroup_INT) {
			/**�û���*/
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
	 * ��ѯ���ɵ�½��ǰ��˾�������û�����������������˾�������û�
	 * @param pk_org
	 * @param alUsers
	 */
	protected void fetchUsers(String pk_org, HashMap<UserGroupVO, UserVO[]> alUsers) {
		//��Ϊ��̨һ���Ի�ȡ modified by zhangrui 2012-04-19
		IPFOrgUnit orgUnitService = NCLocator.getInstance().lookup(IPFOrgUnit.class);
		try {
			alUsers.putAll(orgUnitService.getUserGroupMap(pk_org));
		} catch (BusinessException e) {
			Logger.error(e);
		}
	}

	public void nodeDoubleClicked(String funCode, String pk_org, DefaultMutableTreeNode corpNode) {
		//��ѯ����ǰ��˾�������û�
		HashMap<UserGroupVO, UserVO[]> usermap = new HashMap<UserGroupVO, UserVO[]>();
		fetchUsers(pk_org, usermap);

		//�����ѯ�����û�Ϊ��,��״̬����ʾ������
		if (usermap.size() == 0) {
			showHintMessage(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000067")/*@res "�ù�˾û�ж����û�"*/);
			return;
		}
		DefaultMutableTreeNode userGroupNode = null;
		//�������û����ڵ�
		//����û����ڵ�
		OrganizeUnit treeusergroupvo = null;
		
		UserGroupVO[] vogroups =  usermap.keySet().toArray(new UserGroupVO[0]);
		VOUtil.ascSort(vogroups, new String[] { "groupcode" });
		
		for(UserGroupVO vo:vogroups) {
			UserVO[] vos = usermap.get(vo);
			/**��ȡ�û���*/
			treeusergroupvo = new OrganizeUnit(vo);
			userGroupNode = new DefaultMutableTreeNode(treeusergroupvo);
			userGroupNode.setAllowsChildren(true);
			
			/**ѭ������û�*/
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