package uap.workflow.nc.participantfilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uap.workflow.app.participant.ParticipantException;
import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.rbac.IUserManageQuery_C;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * �����������ߡ��޶�ģʽ��ͬ����
 * <li>�㷨��
 * <li>1.��ѯ�������ڵ�ǰ��¼��˾����Ա������������������������ţ�
 * <li>2.���ޣ����ѯ�������ڷ����˴�����˾����Ա������������������������ţ�
 * <li>3.��ѯ������߶��ڵ���֯��Ԫ�����ڸò��ŵ��û�
 */
public class SameDeptFilter extends DefaultParticipantFilter {

	public List<String> filterUsers(ParticipantFilterContext pfc) {

		//FIXME::leijun V6�������
//		throw new PFBusinessException("V6�ݲ�֧��ͬ�����޶�ģʽ");
		//��ѯ�����������Ĳ���
//		IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
//		//�����˹�������Ա���������ڵ�ǰ��¼��˾����Ա������ leijun@2008-9
//		PsndocVO voPsndoc = umq.getPsndocByUserid(pfc.getCurrCorp(), pfc.getSenderman());
//		String dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//		if (StringUtil.isEmptyWithTrim(dept)) {
//			//�����˹�������Ա�����������û�������˾����Ա������
//			String sSenderCorp = umq.getUser(pfc.getSenderman()).getPk_org();
//			voPsndoc = umq.getPsndocByUserid(sSenderCorp, pfc.getSenderman());
//			dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//			if (StringUtil.isEmptyWithTrim(dept))
//				throw new PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
//						"UPPpfworkflow-000281")/* @res "�޶�ͬ����ʱ,������û����������" */);
//		}
//
//		//��ѯĳ��֯��Ԫ�µ��뷢����ͬ���ŵ������û�
//		OrganizeUnit[] ous = NCLocator.getInstance().lookup(IPFOrgUnit.class).queryUsersByCorpAndDept(
//				pfc.getParticipantType(), pfc.getParticipantId(), pfc.getParticipantBelongCorp(), dept);
//		HashSet<String> hsPKs = new HashSet<String>();
//		for (int i = 0; i < (ous == null ? 0 : ous.length); i++)
//			hsPKs.add(ous[i].getPk());
//
//		return hsPKs;
		
		List<String> userIDs = pfc.getUserList();
		
		return processFilter(pfc, userIDs);

	}

	private List<String> processFilter(ParticipantFilterContext pfc, List<String> userIDs)
	{
		List<String> ids = new ArrayList<String>();
		IUserManageQuery_C umq = NCLocator.getInstance().lookup(IUserManageQuery_C.class);
		try {
			//�����˹�������Ա���������ڵ�ǰ��¼��˾����Ա������ leijun@2008-9
			for(String user : pfc.getFilterSource())
			{
				String psnID = umq.queryPsndocByUserid(user);
				//��user����
				IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				Collection<PsnjobVO> psnJobVOs = queryBS.retrieveByClause(PsnjobVO.class, " pk_psndoc ='"+psnID + "'");
				if (psnJobVOs != null && psnJobVOs.size()> 0){
					PsnjobVO vo = (PsnjobVO)psnJobVOs.toArray()[0];
					String dept = vo.getPk_dept();
					psnJobVOs.clear();
					IUserManageQuery queryUsers = NCLocator.getInstance().lookup(IUserManageQuery.class);
					UserVO[] userVOs = queryUsers.queryUserByClause(
							" pk_base_doc in (select pk_psndoc from bd_psnjob job where job.pk_dept = '"+dept+"')"); //������ش��б���Ч�ʺܵͣ����̫���ӵ�SQL���������ܽ���
					
					for (UserVO userVO : userVOs) 
					{
						if (userIDs.contains(userVO.getCuserid()))
							ids.add(userVO.getCuserid());
					}
				}
			}
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		return ids;
	}
}
