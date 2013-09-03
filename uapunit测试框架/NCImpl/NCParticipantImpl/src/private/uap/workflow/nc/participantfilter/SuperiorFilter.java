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
 * �����������ߡ��޶�ģʽ���ϼ��޶� <li>�㷨�� <li>1.��ѯ���û�����߰����������û� <li>
 * 2a.ָ��ʱ��Ҳ��ʹ�ø��޶����������������ˣ��������ᵼ�»�Զ���� <li>2b.��ָ��ʱ��ʹ�ø��޶������ص�һ�������ˣ������ܻᵼ�»�Զ����
 * 
 */
public class SuperiorFilter extends DefaultParticipantFilter {

	public List<String> filterUsers(ParticipantFilterContext pfc)
	{
		// 1.��ѯ���û�����߰����������û�
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
		// "UPPpfworkflow-000259")/* @res "�Ҳ���ִ����" */
		// );
		// for (int i = 0; i < orgUnits.length; i++)
		// hsUserIds.add(orgUnits[i].getPk());
		// } catch (Exception e) {
		// throw new
		// PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
		// "UPPpfworkflow-000260")/* @res "������֯����Ԫ��" */
		// + pfc.getParticipantType()
		// + ":ID:"
		// + pfc.getParticipantId()
		// + NCLangResOnserver.getInstance().getStrByID("pfworkflow",
		// "UPPpfworkflow-000261")/* @res "��ִ����ʱ����:"*/
		// + e.getMessage(), e);
		// }

		List<String> hsUserIds = pfc.getUserList();

		// 2.����Ƿ���ָ�ɣ������ϼ��޶�
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
	 * ����û���ĳ����˾�Ĺ�����Ա����pk
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
		// FIXME::leijun V6�������
		if (pk_psndoc == null)
			throw new ParticipantException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000000")/* ��δʵ���û�����Ա�����Ĺ��� */);
		return pk_psndoc;
	}

	/**
	 * ��ָ��ʱ���ϼ��޶� �����߼� <li>���ܻᴥ����Զ���ɣ��Ӷ����������Զ���ת <li>ֻ���ص�һ��������
	 * 
	 * @param hsUserIds
	 * @param pfc
	 * @return
	 * @throws BusinessException
	 */
	protected List<String> filterUsersNoDispatch(List<String> hsUserIds,
			ParticipantFilterContext pfc) throws BusinessException {
		// 1.�жϷ������Ƿ��Ѿ����ڻ�������� leijun@2007-4-17

		// if (hsUserIds.contains(pfc.getSenderman())) {
		// //FIXME:WCJ�Զ���ɵ��޶�����Ҫ���������벻�ܷŵ�����
		// ��������˷��Ƶ��ˣ��������Զ���ת
		// if (!pfc.getSenderman().equals(pfc.getBillmaker()))
		// throw new FlowNextException("��̻�Զ���ɣ����̼���");
		// }

		// 2.���жϷ����ˣ�����ǰ����Ա���Ƿ�Ϊ���Ÿ�����
		// �����û��ҵ���Ա������
		String loginCorp = InvocationInfoProxy.getInstance().getGroupId();
		if (StringUtil.isEmptyWithTrim(loginCorp))
			loginCorp = null;
		// ��ѯ���û���������в���
		DeptVO condVO = new DeptVO();
		BaseDAO dao = new BaseDAO();
		List<String> hsId = new ArrayList<String>();
		// �޶��˻��ж�������
		for (String user : pfc.getFilterSource()) {
			// �����޶��û�ȡ��Ա��Ϣ
			String psnID = getPsndocByCorpAndUser(loginCorp, user);
			// ������Ա��Ϣȥ��Ӧ�ĸ����β���
			ArrayList<DeptVO> alDept = (ArrayList) dao.retrieveByClause(
					DeptVO.class, "principal='" + psnID + "'", "innercode");

			String mgrId = null;
			if (alDept.size() > 0) {
				// ������Ϊ���Ÿ����ˣ��򷵻ظò��ŵ��ϼ����Ÿ�����
				// XXX:���޳���ֱ�����¼���ϵ�е��¼����� leijun+2009-3
				ArrayList<DeptVO> alSon = new ArrayList<DeptVO>();
				for (DeptVO deptVO : alDept) {
					String fatherPK = deptVO.getPk_fatherorg();
					if (fatherPK == null) {
						// û���ϼ����ŵ���������洦��
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

				// �ٲ�ѯ�������ŵ��ϼ����Ÿ�����
				for (DeptVO deptVO : alDept) {
					mgrId = findDeptMgrRecursive(new Object[] { user, deptVO },
							hsUserIds);
					hsId.add(mgrId);
				}
			} else {
				// �����˷ǲ��Ÿ����ˣ����ҷ����ˣ�����ǰ����Ա�����ڲ��ŵĸ�����Ա��Ӧ���û�
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
																		 * ��̻�ϼ��޶�����
																		 * ��
																		 * �Ҳ������ִ����
																		 */);
		return hsId;
	}

	/**
	 * ָ��ʱʹ�õ��ϼ��޶� �����߼� <li>���ᴥ����Զ���ɣ����ᵼ�������Զ���ת
	 * 
	 * @param hsUserIds
	 * @param pfc
	 * @return
	 * @throws BusinessException
	 */
	protected List<String> filterUsersForDispatch(List<String> hsUserIds, ParticipantFilterContext pfc) 
	throws BusinessException
	{
		// 2.���жϷ����ˣ�����ǰ����Ա���Ƿ�Ϊ���Ÿ�����
		// �����û��ҵ���Ա������
		String loginCorp = InvocationInfoProxy.getInstance().getGroupId();
		if (StringUtil.isEmptyWithTrim(loginCorp))
			loginCorp = null;
		// PsndocVO psndocVO = userQry.getPsndocByUserid(currCorp, senderman);

		// TODO:: DeptVO�ṹ����
		// ��ѯ���û���������в��ţ����ſ��������������ˣ�
		DeptVO condVO = new DeptVO();
		// condVO.setPk_psndoc(psndocVO.getPrimaryKey());
		BaseDAO dao = new BaseDAO();
		Collection<String> coSuperior = null;
		for (String user : pfc.getFilterSource()) {
			// �����޶��û�ȡ��Ա��Ϣ
			String psnID = getPsndocByCorpAndUser(loginCorp, user);
			// �Ҹ���Ķ������
			ArrayList<DeptVO> alDept = (ArrayList) dao.retrieveByClause(
					DeptVO.class, "principal='" + psnID + "'", "innercode");
			if (alDept.size() > 0) {
				// ������Ϊ���Ÿ����ˣ��򷵻��为���ŵ��ϼ����Ÿ�����
				coSuperior = findSuperiorDeptMgr(alDept, hsUserIds);
			} else {
				// �����˷ǲ��Ÿ����ˣ����ҷ��������ڲ��ŵĸ�����Ա��Ӧ���û�
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
																		 * ��̻�ϼ��޶�����
																		 * ��
																		 * �Ҳ������ִ����
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
			// ����ϼ�����PK
			// String fatherDeptdocPK = deptdocVO.getPk_fathedept();
			String fatherDeptdocPK = deptdocVO.getPk_fatherorg();
			// �ҵ��ϼ����ŵĶ��������
			alRet.addAll(findMultiDeptMgrOfDept(fatherDeptdocPK));
		}

		return alRet;
	}

	/**
	 * ��ѯ���ŵĶ��������Ա��Ӧ���û�
	 * 
	 * @param deptdocPK
	 * @throws BusinessException
	 */
	private List<String> findMultiDeptMgrOfDept(String deptdocPK)
			throws BusinessException {
		BaseDAO dao = new BaseDAO();
		// ����PK�ҵ�����VO
		DeptVO deptdocVO = (DeptVO) dao.retrieveByPK(DeptVO.class, deptdocPK);
		if (deptdocVO == null)
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000002")/* ����PK�Ҳ�������VO */);

		List<String> hsRet = new ArrayList<String>();
		// TODO:: ���ݲ���ȡ������
		// �ҵ����Ÿ�����Ա������
		String userOfDeptMgr = queryUserIdOfPsndoc(deptdocVO.getPrincipal());
		if (userOfDeptMgr != null)
			hsRet.add(userOfDeptMgr);
		return hsRet;
	}

	/**
	 * ����ĳ�����ڲ��ŵĸ�����Ա��Ӧ���û�
	 * 
	 * @param pkCorp
	 *            ��˾PK
	 * @param userId
	 *            �û�PK
	 * @return {���Ÿ������û�PK;����VO}������
	 * @throws BusinessException
	 * @throws DAOException
	 */
	private ArrayList<Object[]> findDeptMgrOfUser(String pkCorp, String userId)
			throws BusinessException, DAOException {
		// �����û��ҵ���Ա������
		String pk_psndoc = getPsndocByCorpAndUser(pkCorp, userId);
		BaseDAO dao = new BaseDAO();
		ArrayList<PsnjobVO> psnjobs = (ArrayList<PsnjobVO>) dao
				.retrieveByClause(PsnjobVO.class, "pk_psndoc='" + pk_psndoc
						+ "'");
		if (psnjobs != null && psnjobs.size() != 0)
			// ���ݲ����ҵ����Ÿ�����
			return findDeptMgrOfDept(psnjobs.get(0).getPk_dept());
		return null;
	}

	/**
	 * �ݹ�����ϼ����Ÿ�����Ա��Ӧ���û�PK
	 * 
	 * @param deptMgrInfos
	 *            {���Ÿ������û�PK;����VO}
	 * @param hsUserIds
	 * @return
	 * @throws DAOException
	 * @throws BusinessException
	 */
	private String findDeptMgrRecursive(Object[] deptMgrInfos,
			List<String> hsUserIds) throws DAOException, BusinessException {
		String deptMgrUserId = (String) deptMgrInfos[0];

		// ����ϼ�����PK
		// String fatherDeptdocPK = ((DeptVO)
		// deptMgrInfos[1]).getPk_fathedept();
		String fatherDeptdocPK = ((DeptVO) deptMgrInfos[1]).getPk_fatherorg();
		if (fatherDeptdocPK == null)
			throw new ParticipantException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000003")/*
																		 * ��̻�Զ���ɣ�
																		 * ���̼���
																		 */);

		// �ҵ��ϼ����Ÿ�������Ϣ��FIXME::���ܶ������������ֻ����һ����
		ArrayList<Object[]> alMgr = findDeptMgrOfDept(fatherDeptdocPK);
		Object[] fatherDeptMgrInfos = alMgr.get(0);
		String fatherDeptMgrUserId = (String) fatherDeptMgrInfos[0];
		if (fatherDeptMgrUserId.equals(deptMgrUserId))
			throw new ParticipantException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "SuperiorFilter-000003")/*
																		 * ��̻�Զ���ɣ�
																		 * ���̼���
																		 */);

		if (hsUserIds.contains(fatherDeptMgrUserId)) {
			return fatherDeptMgrUserId;
		} else {
			return findDeptMgrRecursive(fatherDeptMgrInfos, hsUserIds);
		}
	}
}
