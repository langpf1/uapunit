package uap.workflow.nc.participant.wfusergroup;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.vo.pub.BusinessException;

public class UserGroupManager {
	private UserGroupManager() {
		super();
	}
	public static WFUserGroupVO getUserGroupVOByPK(String pk) throws BusinessException {
		IWfUserGroupQueryService qry = NCLocator.getInstance().lookup(IWfUserGroupQueryService.class);
		WFUserGroupVO vo = null;
		try {
			vo = qry.getUserGroupVOByPK(pk);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return vo;
	}
}
