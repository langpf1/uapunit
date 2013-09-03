package uap.workflow.oaadapter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.uap.rbac.IRoleGroupQueryService;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserGroupQueryService;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.UserGroupVO;
import nc.vo.uap.rbac.role.RoleGroupVO;
import nc.vo.uap.rbac.role.RoleVO;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryUserAdapter {
     private int pageNumber = 1;//Ĭ����ʾ��һҳ
     private int pageSize = 10;//Ĭ�ϵĵ�һҳ��10����¼
     private String filter ;
	 private String participanteKind = "Operator";//Ĭ�ϵ���ʾ
	 public int perform11(StringWriter w) {
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		if(participanteKind.equalsIgnoreCase("Operator")){//���е�user
			return getOperatorOrUserData(w,filter);
		}else if (participanteKind.equalsIgnoreCase("Role")){
			return getRoleData(w,filter);
		}else if (participanteKind.equalsIgnoreCase("RoleGroup")){
			return getRoleGroupData(w,filter);
		}else if (participanteKind.equalsIgnoreCase("UserGroup")){
			return getUserGroupData(w,filter);
		}else{//�������������ͣ�û��ģ�͵�֧�֣��Բ���Ա����
			return getOperatorOrUserData(w,filter);	
		}
		
	}

	private int getUserGroupData(StringWriter w, String filter2) {
		String group = "00012410000000000H12"; // ��½�ߣ�zhai pk_group
		UserGroupVO[] roleGroupvos =null;
		RoleGroupVO[] rolegroupvos = null;
		StringBuffer buffer = new StringBuffer();
		// ObjectMapper �̰߳�ȫ���л�����ƣ����ÿ��������Ч�ʣ�ʵ��ʹ���п���Ϊȫ�ֹ���
		ObjectMapper mapper = new ObjectMapper();
		try {
			IUserGroupQueryService roleGroupBS = (IUserGroupQueryService) NCLocator.getInstance().lookup(
					IUserGroupQueryService.class.getName());
			if (filter == null) {
				roleGroupvos = roleGroupBS.queryUserGroupBySqlWhere("pk_group='" + group + "'");
			} else {
				roleGroupvos = roleGroupBS
						.queryUserGroupBySqlWhere("pk_group='" + group + "'and Groupcode like '%" + filter + "%'");
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		int i = 0, length = (roleGroupvos.length > pageNumber * pageSize) ? pageNumber * pageSize : roleGroupvos.length;
		UserGroupVO roleGroup = null;
		for (i = (pageNumber - 1) * pageSize; i < length; i++) {
			roleGroup = roleGroupvos[i];
			if (buffer.length() > 1)
				buffer.append(",\n");
			buffer.append("{'id':'");
			buffer.append(roleGroup.getPrimaryKey());
			buffer.append("','code':'");
			buffer.append(roleGroup.getGroupcode());
			buffer.append("','name':'");
			buffer.append(roleGroup.getGroupname());
			buffer.append("'}");
		}
		w.append("[\n");
		w.append(buffer.toString());
		w.append("\n]");
		try {
			// writeXXX�����ǽ�����д��JSON��readXXX�����ǽ�JSON��װ�ɶ���
			mapper.writeValueAsString(w);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return roleGroupvos.length;
	}

	private int getRoleGroupData(StringWriter w, String filter) {
		String group = "00012410000000000H12"; // ��½�ߣ�zhai pk_group
		RoleGroupVO[] rolevos =null;
		RoleGroupVO[] rolegroupvos = null;
		StringBuffer buffer = new StringBuffer();
		// ObjectMapper �̰߳�ȫ���л�����ƣ����ÿ��������Ч�ʣ�ʵ��ʹ���п���Ϊȫ�ֹ���
		ObjectMapper mapper = new ObjectMapper();
		try {
			IRoleGroupQueryService roleGroupBS = (IRoleGroupQueryService) NCLocator.getInstance().lookup(
					IRoleGroupQueryService.class.getName());
			if (filter == null) {
				rolevos = roleGroupBS.queryRoleGroupBySqlWhere("pk_group='" + group + "'");
			} else {
				rolevos = roleGroupBS
						.queryRoleGroupBySqlWhere("pk_group='" + group + "'and group_code like '%" + filter + "%'");
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		int i = 0, length = (rolevos.length > pageNumber * pageSize) ? pageNumber * pageSize : rolevos.length;
		RoleGroupVO roleGroup = null;
		for (i = (pageNumber - 1) * pageSize; i < length; i++) {
			roleGroup = rolevos[i];
			if (buffer.length() > 1)
				buffer.append(",\n");
			buffer.append("{'id':'");
			buffer.append(roleGroup.getPrimaryKey());
			buffer.append("','code':'");
			buffer.append(roleGroup.getGroup_code());
			buffer.append("','name':'");
			buffer.append(roleGroup.getGroup_name());
			buffer.append("'}");
		}
		w.append("[\n");
		w.append(buffer.toString());
		w.append("\n]");
		try {
			// writeXXX�����ǽ�����д��JSON��readXXX�����ǽ�JSON��װ�ɶ���
			mapper.writeValueAsString(w);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rolevos.length;
	}

	private int getRoleData(StringWriter w, String filter) {
		String group = "00012410000000000H12"; // ��½�ߣ�zhai pk_group
		RoleVO[] rolevos = null;
		RoleGroupVO[] rolegroupvos = null;
		StringBuffer buffer = new StringBuffer();
		// ObjectMapper �̰߳�ȫ���л�����ƣ����ÿ��������Ч�ʣ�ʵ��ʹ���п���Ϊȫ�ֹ���
		ObjectMapper mapper = new ObjectMapper();
		try {
			IRoleManageQuery roleBS = (IRoleManageQuery) NCLocator.getInstance().lookup(
					IRoleManageQuery.class.getName());
			if (filter == null) {
				rolevos = roleBS.queryRoleByWhereClause("pk_group='" + group + "'");
			} else {
				rolevos = roleBS
						.queryRoleByWhereClause("pk_group='" + group + "'and role_code like '%" + filter + "%'");
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		int i = 0, length = (rolevos.length > pageNumber * pageSize) ? pageNumber * pageSize : rolevos.length;
		RoleVO role = null;
		for (i = (pageNumber - 1) * pageSize; i < length; i++) {
			role = rolevos[i];
			if (buffer.length() > 1)
				buffer.append(",\n");
			buffer.append("{'id':'");
			buffer.append(role.getPrimaryKey());
			buffer.append("','code':'");
			buffer.append(role.getRole_code());
			buffer.append("','name':'");
			buffer.append(role.getRole_name());
			buffer.append("'}");
		}
		w.append("[\n");
		w.append(buffer.toString());
		w.append("\n]");
		try {
			// writeXXX�����ǽ�����д��JSON��readXXX�����ǽ�JSON��װ�ɶ���
			mapper.writeValueAsString(w);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rolevos.length;
	}
		// ��ѯ��ǰ�����µ�����ҵ��Ԫ
		public Vector<OrgVO> getOrgsOfThisGroup(String group) {
			Vector<OrgVO> m_vecOrgsOfThisGroup = new Vector<OrgVO>();
			try {
				IOrgUnitQryService corpService = (IOrgUnitQryService) NCLocator.getInstance().lookup(IOrgUnitQryService.class.getName());
				OrgVO[] aryCorps = corpService.queryAllOrgUnitVOsByGroupID(group, true, true);//��½�ߣ�zhai��groupΪ������group
				for (int i = 0; i < (aryCorps == null ? 0 : aryCorps.length); i++) {
					m_vecOrgsOfThisGroup.addElement(aryCorps[i]);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
			return m_vecOrgsOfThisGroup;
		}
	private int getOperatorOrUserData(StringWriter w, String filter) {
		String group = "00012410000000000H12";	//��½�ߣ�zhai pk_group
		List<UserVO> users = new ArrayList<UserVO>();//�洢���е�user
		StringBuffer buffer = new StringBuffer();
		// ObjectMapper �̰߳�ȫ���л�����ƣ����ÿ��������Ч�ʣ�ʵ��ʹ���п���Ϊȫ�ֹ���
		ObjectMapper mapper = new ObjectMapper();
		// ��ѯ��ǰ���������й�˾

		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator
				.getInstance().lookup(IUserManageQuery.class);
		try {
			// �õ���ǰ��¼���ŵ�����ҵ��Ԫ
			Vector<OrgVO> allOrgsofThisGroup = getOrgsOfThisGroup(group);
			// ���β�ѯ����ҵ��Ԫ�µ��û�
			if (allOrgsofThisGroup == null || allOrgsofThisGroup.size() == 0)
				return 0;
			for (OrgVO orgvo : allOrgsofThisGroup) {
				UserVO[] temp = iCorpService.queryAllUsersByOrg(orgvo.getPk_org());
				for (int i = 0; i < (temp == null ? 0 : temp.length); i++) {
					if(filter==null){
						users.add(temp[i]);
					}else{
						if(temp[i].getUser_name().contains(filter))
							users.add(temp[i]);
					}
				}
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}	
			 
			 int i = 0,length=(users.size()>pageNumber*pageSize)?pageNumber*pageSize:users.size(); 
			 UserVO user = null; 
			for( i=(pageNumber-1)*pageSize;i<length ;i++){
				user=users.get(i);
				if (buffer.length() > 1)
					buffer.append(",\n");
				buffer.append("{'id':'");
				buffer.append(user.getCuserid());
				buffer.append("','code':'");
				buffer.append(user.getUser_code());
				buffer.append("','name':'");
				buffer.append(user.getUser_name());
				buffer.append("'}");
			}
			w.append("[\n");
			w.append(buffer.toString());
			w.append("\n]");
		try {
			// writeXXX�����ǽ�����д��JSON��readXXX�����ǽ�JSON��װ�ɶ���
			mapper.writeValueAsString(w);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return users.size();
	}
	public void setNum(int a ){
		this.pageNumber = a;
	}
	public void setSize(int pageSize) {
		this.pageSize = pageSize;	
	}
	public void setFilter(String filter) {
		this.filter = filter;	
	}
	public void setParticipanteKind(String participanteKind) {
		this.participanteKind = participanteKind;
	}
	}

