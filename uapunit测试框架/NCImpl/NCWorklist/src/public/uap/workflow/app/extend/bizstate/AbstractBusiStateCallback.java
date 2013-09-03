package uap.workflow.app.extend.bizstate;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.vo.IPfRetCheckInfo;

/**
 * 审批流状态回写类
 * @author leijun 2008-3
 * @since 5.5
 */
public abstract class AbstractBusiStateCallback {

	public AbstractBusiStateCallback() {
		super();
	}

	/**
	 * 审批时，将单据的审批状态回写为 进行中
	 * @param paraVo
	 * @param intCheckState
	 * @throws Exception
	 */
	private void execApproveGoing(PfParameterVO paraVo) throws Exception {
		Logger.info("****执行审批通过execApproveGoing开始****");

		//1.审批状态回写---对单据主表，以及单据VO的审批状态
		changeStatusWhenApprove(paraVo, IPfRetCheckInfo.GOINGON);

		Logger.info("****执行审批通过execApproveGoing结束****");
	}

	/**
	 * 获得单据类型关联的业务实体对应的数据库表及其主键字段
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	protected abstract String[] getTableInfo(String billtype) throws BusinessException;

	/**
	 * 审批时，回写单据数据库表的审批状态，以及单据VO的审批状态  
	 * @param paraVo
	 * @param iApproveStatus
	 */
	protected abstract void changeStatusWhenApprove(PfParameterVO paraVo, int iApproveStatus)
			throws Exception;

	/**
	 * 弃审时，回写审批状态，对单据主表，以及单据VO的审批状态  
	 * @param paraVo
	 * @param preCheckman
	 * @param iBackCheckState
	 */
	protected abstract void changeStatusWhenUnapprove(PfParameterVO paraVo, String preCheckman,
			int iBackCheckState) throws Exception;

	/**
	 * 审批时，将单据的审批状态回写为 不通过
	 * @param paraVo
	 * @throws Exception
	 */
	private void execApproveNoPass(PfParameterVO paraVo) throws Exception {
		Logger.info("****执行审批未通过execApproveNoPass开始****");

		//1.修改单据为未通过审批，对单据主表，以及单据VO的审批状态
		changeStatusWhenApprove(paraVo, IPfRetCheckInfo.NOPASS);

		Logger.info("****执行审批未通过execApproveNoPass结束****");
	}

	/**
	 * 审批时，将单据的审批状态回写为 通过
	 * @param paraVo
	 * @throws Exception
	 */
	private void execApprovePass(PfParameterVO paraVo) throws Exception {
		Logger.info("****执行审批通过execApprovePass开始****");

		//修改单据为通过审批，对单据主表，以及单据VO的审批状态
		changeStatusWhenApprove(paraVo, IPfRetCheckInfo.PASSING);

		Logger.info("****执行审批通过execApprovePass结束****");
	}

	/**
	 * 审批时，回写单据的审批状态
	 * @param paraVo
	 * @param intCheckState
	 * @throws Exception
	 */
	public void execApproveState(PfParameterVO paraVo, int intCheckState) throws Exception {
		switch (intCheckState) {
			case IPfRetCheckInfo.PASSING: {
				execApprovePass(paraVo);
				break;
			}
			case IPfRetCheckInfo.GOINGON: {
				execApproveGoing(paraVo);
				break;
			}
			case IPfRetCheckInfo.NOPASS: {
				execApproveNoPass(paraVo);
				break;
			}
			case IPfRetCheckInfo.NOSTATE: {
				execUnApproveFree(paraVo);
				break;
			}
			default:
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "AbstractBusiStateCallback-000000", null, new String[]{String.valueOf(intCheckState)})/*不合法的审批状态:{0}*/);
		}
	}

	/*
	 * 回写单据的审批状态为自由态
	 */
	private void execUnApproveFree(PfParameterVO paraVo) throws Exception {
		Logger.info("进行单据状态的修改为自由态开始");

		//1.对单据主表，以及单据VO的审批状态
		changeStatusWhenUnapprove(paraVo, null, IPfRetCheckInfo.NOSTATE);

		Logger.info("进行单据状态的修改为自由态结束");
	}

	/**
	 * 回写单据的审批状态为提交态
	 */
	private void execUnApproveCommit(PfParameterVO paraVo) throws Exception {
		Logger.info("进行单据状态的修改为自由态开始");

		//1.对单据主表，以及单据VO的审批状态
		changeStatusWhenUnapprove(paraVo, null, IPfRetCheckInfo.COMMIT);

		Logger.info("进行单据状态的修改为自由态结束");
	}
	
	/**
	 * 弃审时，将单据的审批状态回写为 进行中
	 * @param paraVo
	 * @param preCheckMan
	 * @throws Exception
	 */
	private void execUnApproveGoing(PfParameterVO paraVo, String preCheckMan) throws Exception {
		Logger.info("进行单据状态的修改：审批进行中(有审批流)开始");

		//1.弃审状态回写---对单据主表，以及单据VO的审批状态
		changeStatusWhenUnapprove(paraVo, preCheckMan, IPfRetCheckInfo.GOINGON);

		Logger.info("进行单据状态的修改：审批进行中(有审批流)结束");
	}

	/**
	 * 弃审时，回写单据的审批状态
	 * @param paraVo
	 * @param preCheckMan
	 * @param intBackCheckState
	 * @throws Exception
	 */
	public void execUnApproveState(PfParameterVO paraVo, String preCheckMan, int intBackCheckState)
			throws Exception {
		switch (intBackCheckState) {
			case IPfRetCheckInfo.GOINGON: {
				execUnApproveGoing(paraVo, preCheckMan);
				break;
			}
			case IPfRetCheckInfo.NOSTATE: {
				execUnApproveFree(paraVo);
				break;
			}
			case IPfRetCheckInfo.COMMIT: {
				execUnApproveCommit(paraVo);
				break;
			}
			default:
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "AbstractBusiStateCallback-000001")/*不合法的反审批状态*/);
		}
	}

	/**
	 * 从数据库查询出主表的TS字段，并赋值到VO中
	 * @param headVo
	 */
	protected void refreshHeadTs(Object headVo) {
		if (headVo instanceof SuperVO) {
			try {
				NCObject.newInstance(headVo).setAttributeValue("ts", queryTSByVO((SuperVO) headVo));
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 查询某SuperVO的TS
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	private UFDateTime queryTSByVO(SuperVO vo) throws BusinessException {
		BaseDAO baseDAO = new BaseDAO();
		String sql = "select ts from " + vo.getTableName() + " where " + vo.getPKFieldName() + " = ?";
		SQLParameter param = new SQLParameter();
		param.addParam(vo.getPrimaryKey());
		Object tsObj = baseDAO.executeQuery(sql, param, new ColumnProcessor());
		if (null == tsObj)
			return null;

		return new UFDateTime(tsObj.toString());
	}
}
