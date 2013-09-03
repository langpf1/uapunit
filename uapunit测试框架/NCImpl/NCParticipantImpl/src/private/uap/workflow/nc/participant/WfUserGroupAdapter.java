package uap.workflow.nc.participant;

import java.util.Arrays;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;
import uap.workflow.nc.participant.wfusergroup.IWfUserGroupQueryService;
import nc.bs.framework.common.NCLocator;
import nc.vo.pub.BusinessException;

public class WfUserGroupAdapter implements IParticipantAdapter{

	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		IWfUserGroupQueryService qryService = NCLocator.getInstance().lookup(IWfUserGroupQueryService.class);
		String[] userPks;
		try {
			userPks = qryService.queryUsersOfWfUserGroup(context.getParticipantID(), context.getPk_org(), context.getTask());
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		return Arrays.asList(userPks);
	}

	@Override
	public void checkValidity(ParticipantContext context) throws ParticipantException
	{
		//IWfUserGroupQueryService内部处理掉
	}
}
