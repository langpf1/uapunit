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
 * �����������ߡ��޶�ģʽ��ͬ��˾��ͬ����
 * <li>�㷨��
 * <li>1.��ѯ�����˵Ĵ�����˾
 * <li>2.��ѯ�������ڵ�ǰ��¼��˾����Ա�����������ڷ����˴�����˾����Ա������������������������ţ�
 * <li>3.��ѯ������߶��ڵ���֯��Ԫ�µĿɵ�¼�ù�˾�����ڸò��ŵ������û�
 */
public class SameCorpAndDeptFilter extends DefaultParticipantFilter {

	public List<String> filterUsers(ParticipantFilterContext pfc){

		//FIXME::leijun V6�������
//		throw new PFBusinessException("V6�ݲ�֧��ͬ��˾ͬ�����޶�ģʽ");
		
//		//��ѯ�����˵Ĵ�����˾
//		IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
//		String sSenderCorp = umq.getUser(pfc.getSenderman()).getPk_org();
//		if (StringUtil.isEmptyWithTrim(sSenderCorp))
//			throw new PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
//					"UPPpfworkflow-000284")/* @res "�޶�ͬ��˾��ͬ����ʱ,������û��������˾" */);
//
//		//��ѯ�����˹�������Ա���������ڵ�ǰ��¼��˾����Ա������
//		PsndocVO voPsndoc = umq.getPsndocByUserid(pfc.getCurrCorp(), pfc.getSenderman());
//		String dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//		if (StringUtil.isEmptyWithTrim(dept)) {
//			//�����˹�������Ա�����������û�������˾����Ա������
//			voPsndoc = umq.getPsndocByUserid(sSenderCorp, pfc.getSenderman());
//			dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//			if (StringUtil.isEmptyWithTrim(dept))
//				throw new PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
//						"UPPpfworkflow-000283")/* @res "�޶�ͬ��˾��ͬ����ʱ,������û����������" */);
//		}
//
//		//��ѯĳ��֯��Ԫ�µ��뷢����ͬ��˾��ͬ���ŵ������û�
//		corp = sSenderCorp;
//		OrganizeUnit[] ous = NCLocator.getInstance().lookup(IPFOrgUnit.class).queryUsersByCorpAndDept(
//				pfc.getParticipantType(), pfc.getParticipantId(), corp, dept);
//		HashSet<String> hsPKs = new HashSet<String>();
//		for (int i = 0; i < (ous == null ? 0 : ous.length); i++)
//			hsPKs.add(ous[i].getPk());
//
//		return hsPKs;
		List<String> ids = new ArrayList<String>();
		IUserManageQuery queryUsers = NCLocator.getInstance().lookup(IUserManageQuery.class);
		//������ش��б���Ч�ʺܵͣ����̫���ӵ�SQL���������ܽ���
		UserVO[] userVOs = null;
		for(String user : pfc.getFilterSource()){
			 try {
				userVOs = queryUsers.queryUserByClause(
						" pk_base_doc in (select pk_psndoc from bd_psnjob job where job.pk_dept in ("+
						"select pk_dept from bd_psnjob where pk_psndoc in (select pk_base_doc from sm_user where cuserid='"+user+"')))");
			} catch (BusinessException e) {
				throw new ParticipantException(e.getMessage(), e);
			} 
			
			
			for (UserVO userVO : userVOs) {
				if (pfc.getUserList().contains(userVO.getCuserid()))
					ids.add(userVO.getCuserid());
			}
		}
		return ids;
	}
}
