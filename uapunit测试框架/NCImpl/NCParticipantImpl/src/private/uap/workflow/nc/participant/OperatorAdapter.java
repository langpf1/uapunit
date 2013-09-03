package uap.workflow.nc.participant;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

public class OperatorAdapter implements IParticipantAdapter{

	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		checkValidity(context);
		ArrayList<String> userList = new ArrayList<String>();
		userList.add(context.getParticipantID());
		return userList;
	}

	@Override
	public void checkValidity(ParticipantContext context) throws ParticipantException
	{
		// 单个用户
		UserVO uservo;
		try {
			uservo = NCLocator.getInstance().lookup(IUserManageQuery.class).getUser(context.getParticipantID());
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(uservo==null){
			throw new ParticipantException("未找到相关用户，请检查"+context.getParticipantID()+ "是否已被删除");
		}	
	}
}
