package uap.workflow.nc.participant.wfusergroup;

import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

public interface IWfUserGroupResolver {
	/**
	 * ���Ҹ������û����·���������������Ա������Ա��
	 */
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context, String parameter, String pk_org, String prevParticipant);
	
}
