package uap.workflow.nc.participantfilter;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.ParticipantException;
import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * 审批流参与者　限定模式：同公司
 * <li>算法：
 * <li>1.查询发送人的创建公司
 * <li>2.查询活动参与者对于的组织单元下可登录该公司的用户
 */
public class SameCorpFilter extends DefaultParticipantFilter {

	public List<String> filterUsers(ParticipantFilterContext pfc) {

		List<String> userIDs = pfc.getUserList();
		//查询某组织单元下可登录该公司的所有用户
		List<String> ids = new ArrayList<String>();
		IUserManageQuery queryUsers = NCLocator.getInstance().lookup(IUserManageQuery.class);
		//UserVO[] userVOs = queryUsers.queryUserByClause(" sm_user.pk_org in (select pk_org from sm_user  where cuserid = '" +pfc.getSenderman() + "')");
		UserVO[] userVOs = null;
		for(String user : pfc.getFilterSource()){
			try {
				userVOs = queryUsers.queryUserByClause(" pk_base_doc in (select pk_psndoc from bd_psndoc doc where doc.pk_org in ("+
						"select pk_org from bd_psndoc where pk_psndoc in (select pk_base_doc from sm_user where cuserid='"+user+"')))");
			} catch (BusinessException e) {
				throw new ParticipantException(e.getMessage(), e);
			} 
			for (UserVO userVO : userVOs) {
				if (userIDs.contains(userVO.getCuserid()))
					ids.add(userVO.getCuserid());
			}
		}
		return ids;
	}
}
