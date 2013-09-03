package uap.workflow.nc.participant;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserGroupQueryService;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.UserGroupVO;

public class UserGroupAdapter implements IParticipantAdapter{
	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		checkValidity(context);
		
		//用户组的解析
		UserGroupVO condVO = new UserGroupVO();
		//condVO.setGrouptype(UserGroupVO.GROUP_TYPE_COMMON);
		condVO.setPk_usergroup(context.getParticipantID());
		
		
		UserVO[] users;
		try {
			users = NCLocator.getInstance().lookup(IUserManageQuery.class)
					.queryAllUserinUserGroup(condVO.getPk_usergroup(), true, true);
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
		UserGroupVO groupVO;
		try {
			groupVO = NCLocator.getInstance().lookup(IUserGroupQueryService.class).getUserGroupByID(context.getParticipantID());
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(groupVO==null){
			throw new ParticipantException("未找到相关用户组，请检查"+context.getParticipantID()+ "是否已被删除");
		}	
	}
}
