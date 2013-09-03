package uap.workflow.nc.participant.wfusergroup;

import java.util.Collection;
import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.uap.IUAPQueryBS;
import nc.md.data.access.NCObject;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * ��֯.����Ա��
 * @author guowl
 *
 */
public class WfUserGroup4AllEmployee implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		Object obj = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			obj = pk_org;
		}
	
		if(obj == null) {
			Logger.debug("�����û��飨��֯.����Ա������Ԫ������û�л�ȡ������ֵ�����ԣ�" + parameter);
			return hsUser;
		}
		
		//����Ӧ����һ����֯pk
		String orgPk = obj.toString();
		try {
			IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
			OrgVO[] orgs = orgQry.getChildrenOrgs(orgPk);
			StringBuilder orgPks = new StringBuilder();
			orgPks.append("'").append(orgPk).append("',");
			if(orgs == null || orgs.length == 0) {
				Logger.debug("�����û��飨��֯.����Ա����������֯pkû���ҵ��¼���֯����֯pk��" + orgPk);
			}else {
				for (OrgVO orgVO : orgs) {
					orgPks.append("'").append(orgVO.getPk_org()).append("',");
				}
				orgPks = orgPks.deleteCharAt(orgPks.length()-1);
			}
			String strOrgPks = orgPks.toString();
			//��Ա��������ְ����=[����]����ְ��֯=[����]
			IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Collection<PsnjobVO> psnJobVOs = queryBS.retrieveByClause(PsnjobVO.class, "pk_dept in (" + strOrgPks + ") or pk_org in (" + strOrgPks + ")");
			HashSet<String> psndocPks = new HashSet<String>();
			for (PsnjobVO psnjobVO : psnJobVOs) {
				psndocPks.add(psnjobVO.getPk_psndoc());
			}
			IUserPubService userpubService = NCLocator.getInstance().lookup(IUserPubService.class);
			for (String pk_psndoc : psndocPks) {
				UserVO uservo = userpubService.queryUserVOByPsnDocID(pk_psndoc);
				if(uservo != null)
					hsUser.add(uservo.getCuserid());
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		
		return hsUser;
	}

}
