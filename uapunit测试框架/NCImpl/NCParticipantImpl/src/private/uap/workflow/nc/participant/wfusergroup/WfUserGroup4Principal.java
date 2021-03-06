package uap.workflow.nc.participant.wfusergroup;

import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgUnitQryService;
import nc.md.data.access.NCObject;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.org.OrgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * 负责人-系统预置的用户组
 * @author guowl
 *
 */
public class WfUserGroup4Principal implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		//参数应该是一个组织pk
		Object obj = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			obj = pk_org;
		}
		if(obj == null) {
			Logger.debug("流程用户组（组织.负责人）从元数据上没有获取到属性值，属性：" + parameter);
			return hsUser;
		}
		
		String orgPk = obj.toString();
		IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
		OrgVO org;
		try {
			org = orgQry.getOrg(orgPk);
			if(org == null) {
				//根据组织pk找不到
				Logger.debug("流程用户组（组织.负责人）根据组织pk找不到组织，组织pk：" + orgPk);
				return null;
			}
			String psndocpk = org.getPrincipal();
			IUserPubService userpubService = NCLocator.getInstance().lookup(IUserPubService.class);
			UserVO uservo = userpubService.queryUserVOByPsnDocID(psndocpk);
			if(uservo != null)
				hsUser.add(uservo.getCuserid());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return hsUser;
	}

}
