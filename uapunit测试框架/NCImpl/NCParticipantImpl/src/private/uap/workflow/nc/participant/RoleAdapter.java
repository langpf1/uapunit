package uap.workflow.nc.participant;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.role.RoleVO;

public class RoleAdapter implements IParticipantAdapter{

	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		checkValidity(context);
		
		UserVO[] users;
		try {
			users = NCLocator.getInstance().lookup(IUserManageQuery.class).queryUserByRole(context.getParticipantID(),null);
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
		// TODO Auto-generated method stub
		RoleVO role;
		try {
			role = NCLocator.getInstance().lookup(IRoleManageQuery.class).getRoleByPK(context.getParticipantID());
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(role==null){
			throw new ParticipantException("未找到相关角色，请检查"+context.getParticipantID()+ "是否已被删除");
		}
	}
}
