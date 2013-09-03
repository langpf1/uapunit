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
 * 审批流参与者　默认过滤器基类 
 */
public abstract class DefaultParticipantFilter implements IParticipantFilter {

	/**
	 * 查找部门负责人信息
	 * @param deptdocPK 部门PK
	 * @return {部门负责人用户PK;部门VO}
	 * @throws DAOException
	 * @throws BusinessException
	 */
	protected ArrayList<Object[]> findDeptMgrOfDept(String deptdocPK) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		//根据PK找到部门VO
		DeptVO deptdocVO = (DeptVO) dao.retrieveByPK(DeptVO.class, deptdocPK);
		if (deptdocVO == null)
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "DefaultParticipantFilter-000000", null, new String[]{deptdocPK})/*根据PK={0}找不到部门VO*/);
		
		//TODO::  根据部门取负责人 
		
		//找到部门负责人员管理档案（可能有三个）
		String deptMgr = deptdocVO.getPrincipal();
//		String deptMgr2 = deptdocVO.getPk_psndoc2();
//		String deptMgr3 = deptdocVO.getPk_psndoc3();

		ArrayList<Object[]> alRet = new ArrayList<Object[]>();
				if (!StringUtil.isEmpty(deptMgr))
					//第一个部门负责人
					alRet.add(new Object[] { queryUserIdOfPsndoc(deptMgr), deptdocVO });
		//
		//		if (!StringUtil.isEmpty(deptMgr2))
		//			//第二个部门负责人
		//			alRet.add(new Object[] { queryUserIdOfPsndoc(deptMgr), deptdocVO });
		//
		//		if (!StringUtil.isEmpty(deptMgr3))
		//			//第三个部门负责人
		//			alRet.add(new Object[] { queryUserIdOfPsndoc(deptMgr), deptdocVO });
		//
		//		if (alRet.size() == 0)
		//			throw new PFBusinessException("部门[" + deptdocVO.getName() + "]没有定义负责人员");
		return alRet;
	}

	/**
	 * 找到部门负责人员管理档案对应的用户
	 * @param deptMgr 部门负责人员管理档案
	 * @return
	 * @throws BusinessException
	 */
	protected String queryUserIdOfPsndoc(String deptMgr) throws BusinessException {
		if (StringUtil.isEmptyWithTrim(deptMgr))
			return null;

		BaseDAO dao = new BaseDAO();
		PsndocVO deptMgrVO = (PsndocVO) dao.retrieveByPK(PsndocVO.class, deptMgr);

		//找到部门负责人员对应的用户
		IUserManageQuery ucQry = (IUserManageQuery) NCLocator.getInstance().lookup(
				IUserManageQuery.class.getName());
		UserVO[] clerkVOs = ucQry.queryUserByClause("PK_BASE_DOC='"+deptMgrVO.getPk_psndoc()+"'");
		if (clerkVOs == null || clerkVOs.length == 0)
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "DefaultParticipantFilter-000001", null, new String[]{deptMgrVO.getUsedname()})/*找不到部门负责人员{0}对应的用户*/);
		else
			return clerkVOs[0].getCuserid();
	}
}
