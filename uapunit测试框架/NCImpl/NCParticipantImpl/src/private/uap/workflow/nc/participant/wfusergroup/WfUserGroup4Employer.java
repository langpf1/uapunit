package uap.workflow.nc.participant.wfusergroup;

import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.uap.IUAPQueryBS;
import nc.md.data.access.NCObject;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * ��Ա.ֱ���ϼ�
 * ������userid
 * @author guowl
 *
 */
public class WfUserGroup4Employer implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		Object obj = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			obj = prevParticipant;
		}
		if(obj == null) {
			Logger.debug("�����û��飨��Ա.ֱ���ϼ�����Ԫ������û�л�ȡ������ֵ�����ԣ�" + parameter);
			return hsUser;
		}
		
		//����Ӧ����һ��userid
		String userPk = obj.toString();
		try {
			//�����û�id�ҵ���Ա����
			IUserPubService userService = NCLocator.getInstance().lookup(IUserPubService.class);
			String psndocpk = userService.queryPsndocByUserid(userPk);
			if(StringUtil.isEmptyWithTrim(psndocpk)) {
				Logger.debug("�����û��飨��Ա.ֱ���ϼ��������û�idû���ҵ���Ա�������û�id��" + userPk);
				return hsUser;
			}
			IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			PsndocVO psndocVO = (PsndocVO)queryBS.retrieveByPK(PsndocVO.class, psndocpk);
			//����֯������
			String principalPk = queryPrincipalByOrgPk(psndocVO.getPk_org());
			if(!StringUtil.isEmptyWithTrim(principalPk)) {
				 if(!principalPk.equals(userPk))
					 //������˲�����֯������
					hsUser.add(principalPk);
				 else {
					 //������˾�����֯������,�����ϼ���֯�ĸ����ˡ�����ϼ���֯�ĸ����˻����������أ�
					 IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
					 OrgVO parentOrg = orgQry.getParentOrg(psndocVO.getPk_org());
					 String higherPrincipalPk = queryPrincipalByOrgPk(parentOrg.getPk_org());
					 hsUser.add(higherPrincipalPk);
				 }
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return hsUser;
	}
	
	private String queryPrincipalByOrgPk(String orgPk) throws BusinessException {
		IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
		OrgVO org = orgQry.getOrg(orgPk);
	    IUserPubService userpubService = NCLocator.getInstance().lookup(IUserPubService.class);
		UserVO uservo = userpubService.queryUserVOByPsnDocID(org.getPrincipal());
		String principalPk = uservo == null? null:uservo.getCuserid();
		return principalPk;
	}

}
