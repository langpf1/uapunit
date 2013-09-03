package uap.workflow.nc.participantfilter;

import java.util.ArrayList;

import uap.workflow.app.participant.IParticipantFilter;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.DeptVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * �����������ߡ�Ĭ�Ϲ��������� 
 */
public abstract class DefaultParticipantFilter implements IParticipantFilter {

	/**
	 * ���Ҳ��Ÿ�������Ϣ
	 * @param deptdocPK ����PK
	 * @return {���Ÿ������û�PK;����VO}
	 * @throws DAOException
	 * @throws BusinessException
	 */
	protected ArrayList<Object[]> findDeptMgrOfDept(String deptdocPK) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		//����PK�ҵ�����VO
		DeptVO deptdocVO = (DeptVO) dao.retrieveByPK(DeptVO.class, deptdocPK);
		if (deptdocVO == null)
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "DefaultParticipantFilter-000000", null, new String[]{deptdocPK})/*����PK={0}�Ҳ�������VO*/);
		
		//TODO::  ���ݲ���ȡ������ 
		
		//�ҵ����Ÿ�����Ա��������������������
		String deptMgr = deptdocVO.getPrincipal();
//		String deptMgr2 = deptdocVO.getPk_psndoc2();
//		String deptMgr3 = deptdocVO.getPk_psndoc3();

		ArrayList<Object[]> alRet = new ArrayList<Object[]>();
				if (!StringUtil.isEmpty(deptMgr))
					//��һ�����Ÿ�����
					alRet.add(new Object[] { queryUserIdOfPsndoc(deptMgr), deptdocVO });
		//
		//		if (!StringUtil.isEmpty(deptMgr2))
		//			//�ڶ������Ÿ�����
		//			alRet.add(new Object[] { queryUserIdOfPsndoc(deptMgr), deptdocVO });
		//
		//		if (!StringUtil.isEmpty(deptMgr3))
		//			//���������Ÿ�����
		//			alRet.add(new Object[] { queryUserIdOfPsndoc(deptMgr), deptdocVO });
		//
		//		if (alRet.size() == 0)
		//			throw new PFBusinessException("����[" + deptdocVO.getName() + "]û�ж��帺����Ա");
		return alRet;
	}

	/**
	 * �ҵ����Ÿ�����Ա��������Ӧ���û�
	 * @param deptMgr ���Ÿ�����Ա������
	 * @return
	 * @throws BusinessException
	 */
	protected String queryUserIdOfPsndoc(String deptMgr) throws BusinessException {
		if (StringUtil.isEmptyWithTrim(deptMgr))
			return null;

		BaseDAO dao = new BaseDAO();
		PsndocVO deptMgrVO = (PsndocVO) dao.retrieveByPK(PsndocVO.class, deptMgr);

		//�ҵ����Ÿ�����Ա��Ӧ���û�
		IUserManageQuery ucQry = (IUserManageQuery) NCLocator.getInstance().lookup(
				IUserManageQuery.class.getName());
		UserVO[] clerkVOs = ucQry.queryUserByClause("PK_BASE_DOC='"+deptMgrVO.getPk_psndoc()+"'");
		if (clerkVOs == null || clerkVOs.length == 0)
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "DefaultParticipantFilter-000001", null, new String[]{deptMgrVO.getUsedname()})/*�Ҳ������Ÿ�����Ա{0}��Ӧ���û�*/);
		else
			return clerkVOs[0].getCuserid();
	}
}
