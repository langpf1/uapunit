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
 * ��֯.�ϼ�������
 * @author guowl
 *
 */
public class WfUserGroup4HigherUp implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		//����Ӧ����һ����֯pk
		Object value = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			value = pk_org;
		}
		if(value == null) {
			Logger.debug("�����û��飨��֯.�ϼ������ˣ���Ԫ������û�л�ȡ������ֵ�����ԣ�" + parameter);
			return hsUser;
		}
		
		String orgPk = value.toString();
		IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
		try {
			//����ϼ���֯
			OrgVO parentOrg = orgQry.getParentOrg(orgPk);
			if(parentOrg == null) {
				Logger.debug("�����û��飨��֯.�ϼ������ˣ�������֯pkû�л�ȡ���ϼ���֯����֯pk��" + orgPk);
				return hsUser;
			}
			
			String psndocpk = parentOrg.getPrincipal();
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
