package uap.workflow.nc.participant;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IRoleGroupQueryService;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.excp.RbacException;
import nc.vo.uap.rbac.role.RoleGroupVO;
import nc.vo.uap.rbac.role.RoleVO;

public class RoleGroupAdapter implements IParticipantAdapter{
	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		checkValidity(context);
		
		//角色组的解析
		IRoleManageQuery roleQuery= NCLocator.getInstance().lookup(IRoleManageQuery.class);
		//organizeID 角色组id,belongOrg当前登陆的组织id
		RoleVO[] roles;
		try {
			roles = roleQuery.queryRoleinGroup(context.getParticipantID(), context.getPk_org(), null);
		} catch (RbacException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(roles == null || roles.length == 0)
			return new ArrayList<String>();
		ArrayList<String> rolepks = new ArrayList<String>();
		for(RoleVO role : roles) {
			rolepks.add(role.getPk_role());
		}
		UserVO[] users;
		try {
			users = NCLocator.getInstance().lookup(IUserManageQuery.class).queryUserByRoles(rolepks.toArray(new String[0]),null);
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < users.length; i++) {
			list.add(users[i].getCuserid());
		}
		return list;
	}

	@Override
	public void checkValidity(ParticipantContext context) throws ParticipantException
	{
        RoleGroupVO groupvo;
		try {
			groupvo = NCLocator.getInstance().lookup(IRoleGroupQueryService.class).getRoleGroupVO(context.getParticipantID());
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		
		if(groupvo==null){
			throw new ParticipantException("未找到相关角色组，请检查"+context.getParticipantID()+ "是否已被删除");
		}
		
	}
}
