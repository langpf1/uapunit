package uap.workflow.nc.participant.wfusergroup;

import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgManagerQryService;
import nc.md.data.access.NCObject;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgManagerVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * ��֯.����
 * @author guowl
 *
 */
public class WfUserGroup4Superintend implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		//����Ӧ����һ����֯pk
		Object value = ncObj.getAttributeValue(parameter);
		Logger.info("billvo:"+billvo);
		Logger.info("parameter:"+parameter);
		Logger.info("ncObj:"+ncObj);
		Logger.info("value:"+value);
		if(billtype == null){
			value = pk_org;
		}
		if(value == null) {
			Logger.debug("�����û��飨��֯.���ܣ���Ԫ������û�л�ȡ������ֵ�����ԣ�" + parameter);
			return hsUser;
		}
		
		String orgPk = value.toString();

		IOrgManagerQryService orgManagerQry = NCLocator.getInstance().lookup(IOrgManagerQryService.class);
		try {
			OrgManagerVO[] orgManagerVOs = orgManagerQry.queryOrgManagerVOSByOrgID(orgPk);
			if(orgManagerVOs == null || orgManagerVOs.length == 0) {
				Logger.debug("�����û��飨��֯.���ܣ�������֯pkû���ҵ���֯������Ϣ����֯pk��" + orgPk);
				return hsUser;
			}
			
			for (OrgManagerVO orgManagerVO : orgManagerVOs) {
				String userPk = orgManagerVO.getCuserid();
				if(!StringUtil.isEmptyWithTrim(userPk))
					hsUser.add(userPk);
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		Logger.info("hsUser:"+hsUser);
		return hsUser;
	}

}
