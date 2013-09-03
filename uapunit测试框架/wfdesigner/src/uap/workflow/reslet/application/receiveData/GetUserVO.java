package uap.workflow.reslet.application.receiveData;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.excp.RbacException;
import nc.vo.uap.rbac.role.RoleVO;
public class GetUserVO {
	private Map<String,UserVO> userlist = new HashMap<String,UserVO>();
	/**通过用户pk得到用户的名称*/
	public String getUserNameByuserpk(String pk_owner){
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
		
		UserVO user = new UserVO();
		String username = null;
		try {
			if(userlist.containsKey(pk_owner)){
				 username = userlist.get(pk_owner).getUser_name();
			}else{
				user = iCorpService.getUser(pk_owner);
				userlist.put(user.getPrimaryKey(), user);
				username =  user.getUser_name();
			}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return username;
	}
	/**通过用户的姓名得到用户的pk*/
	public String getUserIDByusername(String name){
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
		
		UserVO user = null;
		String userid = null;
		try {
			if(userlist.containsKey(name)){
				 userid = userlist.get(name).getPrimaryKey();
			}else{
				user = iCorpService.findUserByCode(name, null);
				if( user != null)
				    userid =  user.getPrimaryKey();
			}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return userid;
	}
	public String getUserRole(String username) {
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		IRoleManageQuery iCorpService = (IRoleManageQuery) NCLocator.getInstance().lookup(IRoleManageQuery.class);
		IUserManageQuery iCorpuserService = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO user = null;
		String roleID = null;
		RoleVO[] role;
		try {
			user = iCorpuserService.findUserByCode(username, null);
			if(user != null){
				role = iCorpService.queryRoleByUserID(user.getPrimaryKey(), user.getPk_group());
				if(role != null) roleID = role[0].getPk_org();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleID;
	}
}
