package uap.workflow.nc.participant.wfusergroup;

import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

public interface IWfUserGroupResolver {
	/**
	 * 查找该流程用户组下符合条件的所有人员（操作员）
	 */
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context, String parameter, String pk_org, String prevParticipant);
	
}
