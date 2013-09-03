package uap.workflow.nc.participant;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IResponsibilityQueryService;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.ResponsibilityVO;

public class ResponsibilityAdapter implements IParticipantAdapter{

	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		checkValidity(context);
		//����ְ��PK�ҵ�user
		String responsibilityPK= context.getParticipantID();
		if(StringUtil.isEmptyWithTrim(responsibilityPK))
			return null;
		//�ô���֯Ϊ���������廷���ϵ���֯�޶��Ľ�� ����Ϊ���
		String[] pk_orgs =context.getResponsibility_orgs();
		UserVO[] uservos;
		try {
			uservos = NCLocator.getInstance().lookup(IUserPubService.class).getUsersOwnedRespAndOrgsPerm(new String[]{responsibilityPK},pk_orgs);
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(uservos==null||uservos.length==0)
			return null;
		ArrayList<String> userPKs =new ArrayList<String>();
		for(UserVO uservo:uservos){
			userPKs.add(uservo.getPrimaryKey());
		}
		return userPKs;
	}

	@Override
	public void checkValidity(ParticipantContext context) throws ParticipantException
	{
		ResponsibilityVO[] resvos;
		try {
			resvos = NCLocator.getInstance().lookup(IResponsibilityQueryService.class).queryResponsibilityByPKs(new String[]{context.getParticipantID()});
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(resvos==null||resvos.length==0)
			throw new ParticipantException("δ�ҵ�ְ�� "+context.getParticipantID()+ " �����ְ���Ƿ��ѱ�ɾ��");
		
	}

}
