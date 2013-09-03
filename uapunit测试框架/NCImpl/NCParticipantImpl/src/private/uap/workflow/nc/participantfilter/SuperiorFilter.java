package uap.workflow.nc.participantfilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uap.workflow.app.participant.ParticipantException;
import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.DeptVO;
import nc.vo.pub.BusinessException;

/**
 * 审批流参与者　限定模式：上级限定 <li>算法： <li>1.查询出该活动参与者包含的所有用户 <li>
 * 2a.指派时，也可使用该限定（返回三个负责人），但不会导致活动自动完成 <li>2b.非指派时，使用该限定（返回第一个负责人），可能会导致活动自动完成
 * 
 */
public class SuperiorFilter extends DefaultParticipantFilter {

	public List<String> filterUsers(ParticipantFilterContext pfc)
	{
		// 1.查询出该活动参与者包含的所有用户
		// HashSet<String> hsUserIds = new HashSet<String>();
		// try {
		// OrganizeUnit[] orgUnits =
		// NCLocator.getInstance().lookup(IPFOrgUnit.class)
		// .queryUsersByCorpAndDept(pfc.getParticipantType(),
		// pfc.getParticipantId(),
		// pfc.getBelongOrg(), null);
		// if (orgUnits == null || orgUnits.length == 0)
		// throw new
		// PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
		// "UPPpfworkflow-000259")/* @res "找不到执行人" */
		// );
		// for (int i = 0; i < orgUnits.length; i++)
		// hsUserIds.add(orgUnits[i].getPk());
		// } catch (Exception e) {
		// throw new
		// PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
		// "UPPpfworkflow-000260")/* @res "根据组织机构元素" */
		// + pfc.getParticipantType()
		// + ":ID:"
		// + pfc.getParticipantId()
		// + NCLangResOnserver.getInstance().getStrByID("pfworkflow",
		// "UPPpfworkflow-000261")/* @res "查执行人时出错:"*/
		// + e.getMessage(), e);
		// }

		List<String> hsUserIds = pfc.getUserList();

		// 2.针对是否有指派，进行上级限定
		try {
			if (pfc.isForDispatch())
				return filterUsersForDispatch(hsUserIds, pfc);
			else
				return filterUsersNoDispatch(hsUserIds, pfc);
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
	}

	/**
	 * 获得用户在某个公司的管理人员档案pk
	 * 
	 * @param pkCorp
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	private String getPsndocByCorpAndUser(String pkCorp, String userId)
			throws BusinessException {
		IUserManageQuery userQry = (IUserManageQuery) NCLocator.getInstance()
				.lookup(IUserManageQuery.class.getName());
		String pk_psndoc = userQry.queryPsndocByUserid(userId);
		// FIXME::leijun V6编译错误？
		if (pk_psndoc == null)
			throw new ParticipantException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000000")/* 尚未实现用户与人员档案的关联 */);
		return pk_psndoc;
	}

	/**
	 * 非指派时的上级限定 处理逻辑 <li>可能会触发活动自动完成，从而导致流程自动流转 <li>只返回第一个负责人
	 * 
	 * @param hsUserIds
	 * @param pfc
	 * @return
	 * @throws BusinessException
	 */
	protected List<String> filterUsersNoDispatch(List<String> hsUserIds,
			ParticipantFilterContext pfc) throws BusinessException {
		// 1.判断发送人是否已经属于活动参与者了 leijun@2007-4-17

		// if (hsUserIds.contains(pfc.getSenderman())) {
		// //FIXME:WCJ自动完成的限定还是要继续，代码不能放到这里
		// 如果发送人非制单人，则流程自动流转
		// if (!pfc.getSenderman().equals(pfc.getBillmaker()))
		// throw new FlowNextException("后继活动自动完成，流程继续");
		// }

		// 2.先判断发送人（即当前操作员）是否为部门负责人
		// 根据用户找到人员管理档案
		String loginCorp = InvocationInfoProxy.getInstance().getGroupId();
		if (StringUtil.isEmptyWithTrim(loginCorp))
			loginCorp = null;
		// 查询该用户负责的所有部门
		DeptVO condVO = new DeptVO();
		BaseDAO dao = new BaseDAO();
		List<String> hsId = new ArrayList<String>();
		// 限定人会有多个的情况
		for (String user : pfc.getFilterSource()) {
			// 按照限定用户取人员信息
			String psnID = getPsndocByCorpAndUser(loginCorp, user);
			// 按照人员信息去对应的负责任部门
			ArrayList<DeptVO> alDept = (ArrayList) dao.retrieveByClause(
					DeptVO.class, "principal='" + psnID + "'", "innercode");

			String mgrId = null;
			if (alDept.size() > 0) {
				// 发送人为部门负责人，则返回该部门的上级部门负责人
				// XXX:先剔除有直接上下级关系中的下级部门 leijun+2009-3
				ArrayList<DeptVO> alSon = new ArrayList<DeptVO>();
				for (DeptVO deptVO : alDept) {
					String fatherPK = deptVO.getPk_fatherorg();
					if (fatherPK == null) {
						// 没有上级部门的情况，后面处理
					} else {
						boolean isExistFather = false;
						for (DeptVO dVO : alDept) {
							if (fatherPK.equals(dVO.getPrimaryKey())) {
								isExistFather = true;
								break;
							}
						}
						if (isExistFather)
							alSon.add(deptVO);
					}
				}
				alDept.removeAll(alSon);

				// 再查询所负责部门的上级部门负责人
				for (DeptVO deptVO : alDept) {
					mgrId = findDeptMgrRecursive(new Object[] { user, deptVO },
							hsUserIds);
					hsId.add(mgrId);
				}
			} else {
				// 发送人非部门负责人，查找发送人（即当前操作员）所在部门的负责人员对应的用户
				ArrayList<Object[]> alMgr = findDeptMgrOfUser(loginCorp, user);
				for (Object[] deptMgrInfos : alMgr) {
					String deptMgrUserId = (String) deptMgrInfos[0];
					if (user.equals(deptMgrUserId)) {
						mgrId = findDeptMgrRecursive(deptMgrInfos, hsUserIds);
					} else if (hsUserIds.contains(deptMgrUserId)) {
						mgrId = deptMgrUserId;
					}
					if (mgrId != null) {
						hsId.add(mgrId);
						mgrId = null;
					}
				}

			}
		}
		if (hsId.size() == 0)
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000001")/*
																		 * 后继活动上级限定错误
																		 * ，
																		 * 找不到活动的执行者
																		 */);
		return hsId;
	}

	/**
	 * 指派时使用的上级限定 处理逻辑 <li>不会触发活动自动完成，不会导致流程自动流转
	 * 
	 * @param hsUserIds
	 * @param pfc
	 * @return
	 * @throws BusinessException
	 */
	protected List<String> filterUsersForDispatch(List<String> hsUserIds, ParticipantFilterContext pfc) 
	throws BusinessException
	{
		// 2.先判断发送人（即当前操作员）是否为部门负责人
		// 根据用户找到人员管理档案
		String loginCorp = InvocationInfoProxy.getInstance().getGroupId();
		if (StringUtil.isEmptyWithTrim(loginCorp))
			loginCorp = null;
		// PsndocVO psndocVO = userQry.getPsndocByUserid(currCorp, senderman);

		// TODO:: DeptVO结构变了
		// 查询该用户负责的所有部门（部门可能有三个负责人）
		DeptVO condVO = new DeptVO();
		// condVO.setPk_psndoc(psndocVO.getPrimaryKey());
		BaseDAO dao = new BaseDAO();
		Collection<String> coSuperior = null;
		for (String user : pfc.getFilterSource()) {
			// 按照限定用户取人员信息
			String psnID = getPsndocByCorpAndUser(loginCorp, user);
			// 找负责的多个部门
			ArrayList<DeptVO> alDept = (ArrayList) dao.retrieveByClause(
					DeptVO.class, "principal='" + psnID + "'", "innercode");
			if (alDept.size() > 0) {
				// 发送人为部门负责人，则返回其负责部门的上级部门负责人
				coSuperior = findSuperiorDeptMgr(alDept, hsUserIds);
			} else {
				// 发送人非部门负责人，查找发送人所在部门的负责人员对应的用户
				ArrayList<Object[]> alMgr = findDeptMgrOfUser(loginCorp, user);
				for (Object[] deptMgrInfos : alMgr) {
					String deptMgrUserId = (String) deptMgrInfos[0];
					coSuperior.add(deptMgrUserId);
				}
			}
		}

		List<String> hsId = new ArrayList<String>();
		for (String mgrId : coSuperior) {
			if (hsUserIds.contains(mgrId))
				hsId.add(mgrId);
		}
		if (hsId.size() == 0)
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000001")/*
																		 * 后继活动上级限定错误
																		 * ，
																		 * 找不到活动的执行者
																		 */);

		return hsId;
	}

	/**
	 * @param alDept
	 * @param hsUserIds
	 * @return
	 * @throws BusinessException
	 */
	private ArrayList<String> findSuperiorDeptMgr(ArrayList<DeptVO> alDept,
			List<String> hsUserIds) throws BusinessException {
		ArrayList<String> alRet = new ArrayList<String>();
		for (DeptVO deptdocVO : alDept) {
			// 获得上级部门PK
			// String fatherDeptdocPK = deptdocVO.getPk_fathedept();
			String fatherDeptdocPK = deptdocVO.getPk_fatherorg();
			// 找到上级部门的多个负责人
			alRet.addAll(findMultiDeptMgrOfDept(fatherDeptdocPK));
		}

		return alRet;
	}

	/**
	 * 查询部门的多个负责人员对应的用户
	 * 
	 * @param deptdocPK
	 * @throws BusinessException
	 */
	private List<String> findMultiDeptMgrOfDept(String deptdocPK)
			throws BusinessException {
		BaseDAO dao = new BaseDAO();
		// 根据PK找到部门VO
		DeptVO deptdocVO = (DeptVO) dao.retrieveByPK(DeptVO.class, deptdocPK);
		if (deptdocVO == null)
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000002")/* 根据PK找不到部门VO */);

		List<String> hsRet = new ArrayList<String>();
		// TODO:: 根据部门取负责人
		// 找到部门负责人员管理档案
		String userOfDeptMgr = queryUserIdOfPsndoc(deptdocVO.getPrincipal());
		if (userOfDeptMgr != null)
			hsRet.add(userOfDeptMgr);
		return hsRet;
	}

	/**
	 * 查找某人所在部门的负责人员对应的用户
	 * 
	 * @param pkCorp
	 *            公司PK
	 * @param userId
	 *            用户PK
	 * @return {部门负责人用户PK;部门VO}的数组
	 * @throws BusinessException
	 * @throws DAOException
	 */
	private ArrayList<Object[]> findDeptMgrOfUser(String pkCorp, String userId)
			throws BusinessException, DAOException {
		// 根据用户找到人员管理档案
		String pk_psndoc = getPsndocByCorpAndUser(pkCorp, userId);
		BaseDAO dao = new BaseDAO();
		ArrayList<PsnjobVO> psnjobs = (ArrayList<PsnjobVO>) dao
				.retrieveByClause(PsnjobVO.class, "pk_psndoc='" + pk_psndoc
						+ "'");
		if (psnjobs != null && psnjobs.size() != 0)
			// 根据部门找到部门负责人
			return findDeptMgrOfDept(psnjobs.get(0).getPk_dept());
		return null;
	}

	/**
	 * 递归查找上级部门负责人员对应的用户PK
	 * 
	 * @param deptMgrInfos
	 *            {部门负责人用户PK;部门VO}
	 * @param hsUserIds
	 * @return
	 * @throws DAOException
	 * @throws BusinessException
	 */
	private String findDeptMgrRecursive(Object[] deptMgrInfos,
			List<String> hsUserIds) throws DAOException, BusinessException {
		String deptMgrUserId = (String) deptMgrInfos[0];

		// 获得上级部门PK
		// String fatherDeptdocPK = ((DeptVO)
		// deptMgrInfos[1]).getPk_fathedept();
		String fatherDeptdocPK = ((DeptVO) deptMgrInfos[1]).getPk_fatherorg();
		if (fatherDeptdocPK == null)
			throw new ParticipantException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000003")/*
																		 * 后继活动自动完成，
																		 * 流程继续
																		 */);

		// 找到上级部门负责人信息（FIXME::可能多个，但这里暂只处理一个）
		ArrayList<Object[]> alMgr = findDeptMgrOfDept(fatherDeptdocPK);
		Object[] fatherDeptMgrInfos = alMgr.get(0);
		String fatherDeptMgrUserId = (String) fatherDeptMgrInfos[0];
		if (fatherDeptMgrUserId.equals(deptMgrUserId))
			throw new ParticipantException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000003")/*
																		 * 后继活动自动完成，
																		 * 流程继续
																		 */);

		if (hsUserIds.contains(fatherDeptMgrUserId)) {
			return fatherDeptMgrUserId;
		} else {
			return findDeptMgrRecursive(fatherDeptMgrInfos, hsUserIds);
		}
	}
}
