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
 * ������״̬��д��
 * @author leijun 2008-3
 * @since 5.5
 */
public abstract class AbstractBusiStateCallback {

	public AbstractBusiStateCallback() {
		super();
	}

	/**
	 * ����ʱ�������ݵ�����״̬��дΪ ������
	 * @param paraVo
	 * @param intCheckState
	 * @throws Exception
	 */
	private void execApproveGoing(PfParameterVO paraVo) throws Exception {
		Logger.info("****ִ������ͨ��execApproveGoing��ʼ****");

		//1.����״̬��д---�Ե��������Լ�����VO������״̬
		changeStatusWhenApprove(paraVo, IPfRetCheckInfo.GOINGON);

		Logger.info("****ִ������ͨ��execApproveGoing����****");
	}

	/**
	 * ��õ������͹�����ҵ��ʵ���Ӧ�����ݿ���������ֶ�
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	protected abstract String[] getTableInfo(String billtype) throws BusinessException;

	/**
	 * ����ʱ����д�������ݿ�������״̬���Լ�����VO������״̬  
	 * @param paraVo
	 * @param iApproveStatus
	 */
	protected abstract void changeStatusWhenApprove(PfParameterVO paraVo, int iApproveStatus)
			throws Exception;

	/**
	 * ����ʱ����д����״̬���Ե��������Լ�����VO������״̬  
	 * @param paraVo
	 * @param preCheckman
	 * @param iBackCheckState
	 */
	protected abstract void changeStatusWhenUnapprove(PfParameterVO paraVo, String preCheckman,
			int iBackCheckState) throws Exception;

	/**
	 * ����ʱ�������ݵ�����״̬��дΪ ��ͨ��
	 * @param paraVo
	 * @throws Exception
	 */
	private void execApproveNoPass(PfParameterVO paraVo) throws Exception {
		Logger.info("****ִ������δͨ��execApproveNoPass��ʼ****");

		//1.�޸ĵ���Ϊδͨ���������Ե��������Լ�����VO������״̬
		changeStatusWhenApprove(paraVo, IPfRetCheckInfo.NOPASS);

		Logger.info("****ִ������δͨ��execApproveNoPass����****");
	}

	/**
	 * ����ʱ�������ݵ�����״̬��дΪ ͨ��
	 * @param paraVo
	 * @throws Exception
	 */
	private void execApprovePass(PfParameterVO paraVo) throws Exception {
		Logger.info("****ִ������ͨ��execApprovePass��ʼ****");

		//�޸ĵ���Ϊͨ���������Ե��������Լ�����VO������״̬
		changeStatusWhenApprove(paraVo, IPfRetCheckInfo.PASSING);

		Logger.info("****ִ������ͨ��execApprovePass����****");
	}

	/**
	 * ����ʱ����д���ݵ�����״̬
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
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "AbstractBusiStateCallback-000000", null, new String[]{String.valueOf(intCheckState)})/*���Ϸ�������״̬:{0}*/);
		}
	}

	/*
	 * ��д���ݵ�����״̬Ϊ����̬
	 */
	private void execUnApproveFree(PfParameterVO paraVo) throws Exception {
		Logger.info("���е���״̬���޸�Ϊ����̬��ʼ");

		//1.�Ե��������Լ�����VO������״̬
		changeStatusWhenUnapprove(paraVo, null, IPfRetCheckInfo.NOSTATE);

		Logger.info("���е���״̬���޸�Ϊ����̬����");
	}

	/**
	 * ��д���ݵ�����״̬Ϊ�ύ̬
	 */
	private void execUnApproveCommit(PfParameterVO paraVo) throws Exception {
		Logger.info("���е���״̬���޸�Ϊ����̬��ʼ");

		//1.�Ե��������Լ�����VO������״̬
		changeStatusWhenUnapprove(paraVo, null, IPfRetCheckInfo.COMMIT);

		Logger.info("���е���״̬���޸�Ϊ����̬����");
	}
	
	/**
	 * ����ʱ�������ݵ�����״̬��дΪ ������
	 * @param paraVo
	 * @param preCheckMan
	 * @throws Exception
	 */
	private void execUnApproveGoing(PfParameterVO paraVo, String preCheckMan) throws Exception {
		Logger.info("���е���״̬���޸ģ�����������(��������)��ʼ");

		//1.����״̬��д---�Ե��������Լ�����VO������״̬
		changeStatusWhenUnapprove(paraVo, preCheckMan, IPfRetCheckInfo.GOINGON);

		Logger.info("���е���״̬���޸ģ�����������(��������)����");
	}

	/**
	 * ����ʱ����д���ݵ�����״̬
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
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "AbstractBusiStateCallback-000001")/*���Ϸ��ķ�����״̬*/);
		}
	}

	/**
	 * �����ݿ��ѯ�������TS�ֶΣ�����ֵ��VO��
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
	 * ��ѯĳSuperVO��TS
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
