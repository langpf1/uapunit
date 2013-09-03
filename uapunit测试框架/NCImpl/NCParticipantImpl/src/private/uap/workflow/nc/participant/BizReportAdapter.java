package uap.workflow.nc.participant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.corg.IBusiReportStruQryService;
import nc.itf.org.IOrgEnumConst;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.corg.BusiReportStruMemberVO;
import nc.vo.corg.BusiReportStruVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.PFRuntimeException;

public class BizReportAdapter implements IParticipantAdapter{
	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		IBusiReportStruQryService qry = NCLocator.getInstance().lookup(
				IBusiReportStruQryService.class);
		BusiReportStruVO[] brs;
		try {
			brs = qry.queryBusiReportStruVOsByGroupIDAndClause(
					InvocationInfoProxy.getInstance().getGroupId(),
					"pk_busireportstru='" + context.getParticipantID() + "'");
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if (brs == null || brs.length == 0){
			throw new ParticipantException("未找到业务汇报关系 "+context.getTask().toString()+ " 请检查该业务汇报关系是否已被删除");
		}
			

		BaseDAO dao = new BaseDAO();
		Collection<BusiReportStruMemberVO> col;
		try {
			col = dao.retrieveByClause(
					BusiReportStruMemberVO.class,
					"pk_brs='" + brs[0].getPk_busireportstru() + "'");
		} catch (DAOException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if (col == null || col.size() == 0)
			return null;

		// 找发送人在业务汇报关系中所属的成员,人员>岗位>职务+部门
		String senderman = context.getTask().getOwner();//.getSenderman();
		BusiReportStruMemberVO exactMember;
		try {
			exactMember = findBrsMemberOfUser(col, senderman);
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if (exactMember == null) {
			// 发送人不在业务汇报关系中
			throw new PFRuntimeException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "BusiReportHandler-000000")/*
																		 * 发送人不在业务汇报关系中
																		 * ，
																		 * 无法根据业务汇报关系找到上级
																		 * 。
																		 */);
		}
		String fatherMemberPk = exactMember.getPk_fathermember();
		if (StringUtil.isEmptyWithTrim(fatherMemberPk))
			throw new PFRuntimeException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "BusiReportHandler-000001")/*
																		 * 发送人在业务汇报关系中没有上级
																		 * 。
																		 */);

		BusiReportStruMemberVO fatherMemberVO;
		try {
			fatherMemberVO = (BusiReportStruMemberVO) dao
					.retrieveByPK(BusiReportStruMemberVO.class, fatherMemberPk);
		} catch (DAOException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		try {
			return findUserFromBrsMember(fatherMemberVO);
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
	}

	private List<String> findUserFromBrsMember(
			BusiReportStruMemberVO fatherMemberVO) throws BusinessException {
		List<String> userList = new ArrayList<String>();
		switch (fatherMemberVO.getMembertype()) {
		case IOrgEnumConst.BUSIREPORTSTRUMEMBERTYPE_PSNDOC:
			// 如果是人员
			IUserManageQuery userqry = NCLocator.getInstance().lookup(
					IUserManageQuery.class);
			UserVO user = userqry.queryUserVOByPsnDocID(fatherMemberVO
					.getPk_org());
			userList.add(user.getCuserid());
			break;
		case IOrgEnumConst.BUSIREPORTSTRUMEMBERTYPE_POST:
			// 岗位
			userList = findUsersByPost(fatherMemberVO.getPk_org());
			break;
		case IOrgEnumConst.BUSIREPORTSTRUMEMBERTYPE_JOBDEPT:
			// 职务+部门
			userList = findUsersByJob(fatherMemberVO.getPk_org());
			break;
		default:
			break;
		}
		return userList;
	}

	private List<String> findUsersByJob(String pk_org) throws BusinessException {
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			String sql = "select cuserid from sm_user a left join bd_psnjob b on a.PK_BASE_DOC=b.PK_PSNDOC where b.PK_ORG='"
					+ pk_org + "'";
			ArrayList<String> userList = (ArrayList<String>) jdbc.executeQuery(
					sql, new ArrayListProcessor());
			return userList;
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "BusiReportHandler-000002")/*
																		 * 根据业务汇报关系获取流程参与者时发生数据库异常
																		 * ，
																		 * 详情请看日志
																		 * 。
																		 */);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	private List<String> findUsersByPost(String pk_post)
			throws BusinessException {
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			String sql = "select cuserid from sm_user a left join bd_psnjob b on a.PK_BASE_DOC=b.PK_PSNDOC where b.PK_POST='"
					+ pk_post + "'";
			ArrayList<String> userList = (ArrayList<String>) jdbc.executeQuery(
					sql, new ArrayListProcessor());
			return userList;
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "BusiReportHandler-000002")/*
																		 * 根据业务汇报关系获取流程参与者时发生数据库异常
																		 * ，
																		 * 详情请看日志
																		 * 。
																		 */);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	private BusiReportStruMemberVO findBrsMemberOfUser(
			Collection<BusiReportStruMemberVO> col, String senderman)
			throws BusinessException {
		IUserManageQuery userqry = NCLocator.getInstance().lookup(
				IUserManageQuery.class);
		BusiReportStruMemberVO exactMember = null;
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			for (BusiReportStruMemberVO brsMemberVO : col) {
				boolean ispsn = false;
				switch (brsMemberVO.getMembertype()) {
				case IOrgEnumConst.BUSIREPORTSTRUMEMBERTYPE_PSNDOC:
					// 如果是人员，优先级最高，所以找到立刻就退出
					String psndocid = userqry.queryPsndocByUserid(senderman);
					if (brsMemberVO.getPk_org().equals(psndocid)) {
						exactMember = brsMemberVO;
						ispsn = true;
					}
					break;
				case IOrgEnumConst.BUSIREPORTSTRUMEMBERTYPE_POST:
					// 岗位
					if (checkUserPost(jdbc, senderman, brsMemberVO.getPk_org())) {
						exactMember = brsMemberVO;
					}
					break;
				case IOrgEnumConst.BUSIREPORTSTRUMEMBERTYPE_JOBDEPT:
					// 职务+部门，因为这个优先级最低，所有只有当没有符合的成员时，才按职务+部门查找
					if (exactMember == null
							&& checkUserJobDept(jdbc, senderman,
									brsMemberVO.getPk_org())) {
						exactMember = brsMemberVO;
					}
					break;
				default:
					break;
				}
				if (ispsn)
					break;
				else
					continue;
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp", "BusiReportHandler-000002")/*
																		 * 根据业务汇报关系获取流程参与者时发生数据库异常
																		 * ，
																		 * 详情请看日志
																		 * 。
																		 */);
		} finally {
			if (persist != null)
				persist.release();
		}
		return exactMember;
	}

	private boolean checkUserJobDept(JdbcSession jdbc, String senderman,
			String pk_job) throws DbException {
		String sql = "select b.PK_PSNDOC from sm_user a left join bd_psnjob b on a.PK_BASE_DOC=b.PK_PSNDOC where a.CUSERID='"
				+ senderman + "' and b.pk_job='" + pk_job + "'";
		Object obj = jdbc.executeQuery(sql, new ColumnProcessor(1));

		return obj != null;
	}

	private boolean checkUserPost(JdbcSession jdbc, String senderman,
			String pk_post) throws DbException {
		String sql = "select b.PK_PSNDOC from sm_user a left join bd_psnjob b on a.PK_BASE_DOC=b.PK_PSNDOC where a.CUSERID='"
				+ senderman + "' and b.PK_POST='" + pk_post + "'";
		Object obj = jdbc.executeQuery(sql, new ColumnProcessor(1));

		return obj != null;
	}

	@Override
	public void checkValidity(ParticipantContext context) throws ParticipantException
	{

	}

}
