package uap.workflow.nc.participantfilter;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.ParticipantException;
import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

public class SameOrg4ResponsibilityFilter extends DefaultParticipantFilter {

	@Override
	public List<String> filterUsers(ParticipantFilterContext pfc) {
		// TODO Auto-generated method stub
		//����ְ��PK�ҵ�user
		String responsibilityPK= pfc.getParticipant().getParticipantID();
		if(StringUtil.isEmptyWithTrim(responsibilityPK))
			return null;
		//�ô���֯Ϊ���������廷���ϵ���֯�޶��Ľ�� ����Ϊ���
		String[] pk_orgs =pfc.getFilterOrgs4Responsibility()==null?null:pfc.getFilterOrgs4Responsibility().toArray(new String[0]);
		UserVO[] uservos;
		try {
			uservos = NCLocator.getInstance().lookup(IUserPubService.class).getUsersOwnedRespAndOrgsPerm(new String[]{responsibilityPK},pk_orgs);
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(uservos==null||uservos.length==0)
			return null;
		List<String> userPKs =new ArrayList<String>();
		for(UserVO uservo:uservos){
			userPKs.add(uservo.getPrimaryKey());
		}
		return userPKs;
		
	}

}
