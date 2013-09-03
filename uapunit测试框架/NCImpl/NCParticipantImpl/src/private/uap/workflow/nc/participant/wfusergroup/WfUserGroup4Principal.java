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
 * ������-ϵͳԤ�õ��û���
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
		//����Ӧ����һ����֯pk
		Object obj = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			obj = pk_org;
		}
		if(obj == null) {
			Logger.debug("�����û��飨��֯.�����ˣ���Ԫ������û�л�ȡ������ֵ�����ԣ�" + parameter);
			return hsUser;
		}
		
		String orgPk = obj.toString();
		IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
		OrgVO org;
		try {
			org = orgQry.getOrg(orgPk);
			if(org == null) {
				//������֯pk�Ҳ���
				Logger.debug("�����û��飨��֯.�����ˣ�������֯pk�Ҳ�����֯����֯pk��" + orgPk);
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
