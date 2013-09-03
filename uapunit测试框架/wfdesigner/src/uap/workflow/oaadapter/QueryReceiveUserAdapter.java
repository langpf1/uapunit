package uap.workflow.oaadapter;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.uap.rbac.IRoleGroupQueryService;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.uap.rbac.constant.IRoleConst;
import nc.vo.uap.rbac.role.RoleVO;
import uap.workflow.pub.app.notice.PfSysVariable;

public class QueryReceiveUserAdapter {
	public int perform(String receiveKind, String nodeId, StringWriter w) {
		
			//首先确定为四种源树的树模型的哪一种
		if(receiveKind.equals("Role")||receiveKind.equals("Operator")){//角色,操作员。构造treegrid载入数据
			return this.getRoleOrOperate(receiveKind, nodeId, w);
		}else if(receiveKind.equals("SystemVari")){//系统
			return this.getSystemVari(receiveKind,nodeId,w);
		}else if(receiveKind.equals("UserDefined")){//自定义
			return this.getUserDefined(receiveKind,nodeId,w);
		}else{
			return 0;
		}
	}
	private int getUserDefined(String receiveKind, String nodeId, StringWriter w) {
		// TODO Auto-generated method stub
		return 0;
	}
	private int getSystemVari(String receiveKind, String nodeId, StringWriter w) {
		int returnValue = 0;
		StringBuffer buffer = new StringBuffer();
		Collection coVars = PfSysVariable.instance().getAllVariables()
				.values();
		buffer.append("{'id':'0','text':'所有系统变量','state':'open','children':[");
		for (Iterator iter = coVars.iterator(); iter.hasNext();) {
			returnValue++;
			PfSysVariable.VarEntry var = (PfSysVariable.VarEntry) iter
			.next();
			if (buffer.length() > 55)
				buffer.append(",\n");
			buffer.append("{'id':'");
			buffer.append(returnValue+1);
			buffer.append("','text':'");
			buffer.append(var.getName());
			buffer.append("','state':'open'");
			buffer.append("}");
		}
		buffer.append("]}");
		w.append("[\n");
		w.append(buffer.toString());
		w.append("\n]");
		return returnValue;
		
	}
	private int getRoleOrOperate(String receiveKind, String nodeId, StringWriter w) {
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(), "ncc10".getBytes());// 绕过登陆
		int returnValue = 0;
		StringBuffer buffer = new StringBuffer();
		try {
			IOrgUnitQryService corpService = (IOrgUnitQryService) NCLocator.getInstance().lookup(
					IOrgUnitQryService.class.getName());
			OrgVO[] aryCorps = corpService.queryAllOrgUnitVOsByGroupID("00012410000000000H12",// 登陆名称：zhai
																								// WorkbenchEnvironment.getInstance().getGroupVO().getPk_group()为null
					true, true);
			if (nodeId == null) {
				returnValue = (aryCorps == null ? 0 : aryCorps.length);

				for (int i = 0; i < (aryCorps == null ? 0 : aryCorps.length); i++) {
					if (buffer.length() > 1)
						buffer.append(",\n");
					buffer.append("{'id':'");
					buffer.append(i);
					buffer.append("','text':'");
					buffer.append(aryCorps[i].getName());
					buffer.append("','state':'closed','children':[{}]");
					buffer.append("}");
				}
			} else {// 加载树节点，根据id
				 fetchRolesOfCorp(receiveKind,  aryCorps, nodeId, w);
				 return returnValue;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);

		}
		w.append("[\n");
		w.append(buffer.toString());
		w.append("\n]");
		return returnValue;
		}
	private void fetchRolesOfCorp(String receiveKind, OrgVO[] aryCorps, String nodeId, StringWriter w) {
		RoleVO[] roles = null;
		String[]node = nodeId.split(".");
		String corpPK = aryCorps[Integer.parseInt(nodeId)].getPk_org();//树要进行分级的加载 
		StringBuffer buffer = new StringBuffer();
		if(receiveKind.equalsIgnoreCase("Role")){
			try {
				roles = NCLocator.getInstance().lookup(IRoleManageQuery.class)
						.queryRoleByOrg(corpPK, IRoleConst.BUSINESS_TYPE);
				ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
				sc.token("LGW".getBytes(), "ncc10".getBytes());// 绕过登陆
				IRoleGroupQueryService userQry = NCLocator.getInstance().lookup(IRoleGroupQueryService.class);
				userQry.queryRoleGroupBySqlWhere("pk_org='"+corpPK+"'");
				IRoleManageQuery userQry1 = NCLocator.getInstance().lookup(IRoleManageQuery.class);
				userQry1.queryRoleGroupByOrgId(corpPK);
				for (int i = 0; i < (roles == null ? 0 : roles.length); i++) {
					if (buffer.length() > 1)
						buffer.append(",\n");
					buffer.append("{'id':'");
					buffer.append(nodeId+"."+i);
					buffer.append("','text':'");
					buffer.append(roles[i].getRole_name2());
					buffer.append("','key':'");
					buffer.append(roles[i].getPrimaryKey());
					buffer.append("','state':'open'");
					buffer.append("}");
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}	
		}else if(receiveKind.equalsIgnoreCase("Operator")){
			IUserManageQuery userBS = (IUserManageQuery) NCLocator.getInstance()
											.lookup(IUserManageQuery.class.getName());
			UserVO[] uservos = null;
			try {
				uservos = userBS.queryAllUsersByOrg(corpPK);
		        VOUtil.ascSort(uservos, new String[] { "usercode" });
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}

			for (int i = 0; i < (uservos == null ? 0 : uservos.length); i++) {
		
				if (buffer.length() > 1)
					buffer.append(",\n");
				buffer.append("{'id':'");
				buffer.append(nodeId+"_"+i);
				buffer.append("','text':'");
				buffer.append(uservos[i].getUser_name2());
				buffer.append("','key':'");
				buffer.append(uservos[i].getPrimaryKey());
				buffer.append("','state':'open'");
				buffer.append("}");
			}
			
		}
		w.append("[\n");
		w.append(buffer.toString());
		w.append("\n]");	
	}
}
