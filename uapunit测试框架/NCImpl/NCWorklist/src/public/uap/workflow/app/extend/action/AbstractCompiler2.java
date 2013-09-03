package uap.workflow.app.extend.action;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.pf.IPfExchangeService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.app.client.PfUtilTools;

/**
 * ʵ��ƽ̨������л����ӿڵ���2
 * <li>���ݶ����ű��඼�̳��Ը���
 * 
 * @author ���ھ� 2002-2-28
 */
public class AbstractCompiler2 extends AbstractCompiler {
	public nc.vo.pub.compiler.PfParameterVO m_tmpVo = null;

	public AbstractCompiler2() {
		super();
	}

	/*
	 * ��ע�����е���VO�Ľ���
	 */
	protected AggregatedValueObject changeData(AggregatedValueObject vo, String sourceBillType,
			String destBillType) throws BusinessException {
		return PfUtilTools.runChangeData(sourceBillType, destBillType, vo, m_tmpVo);
	}

	/*
	 * ��ע�����е���VO[]�Ľ���
	 */
	protected AggregatedValueObject[] changeDataAry(AggregatedValueObject[] vo,
			String sourceBillType, String destBillType) throws BusinessException {
		IPfExchangeService exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
		// TODO: exchangeService.runChangeDataAry(sourceBillType, destBillType, vo, m_tmpVo);
		return null;
	}

	/*
	 * ��ע����õ�ǰ���ݵĵ�������
	 */
	protected String getBillType() {
		return m_tmpVo.m_billType;
	}

	/*
	 * ��ע����õ�ǰ��������
	 */
	//	protected UFDateTime getUserDate() {
	//		return new UFDateTime(m_tmpVo.m_currentDate);
	//	}
	/*
	 * ��ע����õ�ǰ���������û�����
	 */
	protected Object getUserObj() {
		return m_tmpVo.m_userObj;
	}

	/*
	 * ��ע����õ�ǰ���������û�����
	 */
	protected Object[] getUserObjAry() {
		return m_tmpVo.m_userObjs;
	}

	/*
	 * ��ע����õ�ǰ��������VO
	 */
	protected nc.vo.pub.AggregatedValueObject getVo() {
		return m_tmpVo.m_preValueVo;
	}

	/*
	 * ��ע����õ�ǰ��������VO[]
	 */
	protected nc.vo.pub.AggregatedValueObject[] getVos() {
		return m_tmpVo.m_preValueVos;
	}

	/* (non-Javadoc)
	 * @see nc.bs.pub.compiler.IPfRun#runComClass(nc.vo.pub.compiler.PfParameterVO)
	 */
	public Object runComClass(PfParameterVO paraVo) throws BusinessException {
		return null;
	}

	/*
	 * ��ע���������õ�ǰ��������VO
	 */
	protected void setUserObj(Object setObj) {
		m_tmpVo.m_userObj = setObj;
	}

	/**
	 * �������õ�ǰ��������VO
	 * <li>�÷�����Ҫ��������Ķ����ű����ã�
	 * <li>���ԣ��������´ӵ���VO�л�ȡһЩ��Ϣ���μ������ĵ���
	 */
	protected void setVo(nc.vo.pub.AggregatedValueObject setVo) {
		m_tmpVo.m_preValueVo = setVo;
		//modified by leijun 2005-12-13
		//���´ӵ��ݾۺ�VO�л�ȡһЩ��Ϣ����billId��billNO��makeBillOperator��
		getHeadInfo(m_tmpVo);
	}

	/**
	 * ��ע���������õ�ǰ��������VO[]
	 */
	protected void setVos(nc.vo.pub.AggregatedValueObject[] setVos) {
		m_tmpVo.m_preValueVos = setVos;
	}

	//����PFParamVO��С��ʹ��.ֻ�����޸�billNO, billID; modify by LYD
	public String getOperator() {
		return m_tmpVo.m_operator;
	}

	public void setBillNO(String billNO) {
		m_tmpVo.m_billNo = billNO;
	}

	public void setBillID(String billID) {
		m_tmpVo.m_billVersionPK = billID;
	}

	public PfParameterVO getPfParameterVO() {
		return m_tmpVo;
	}
	//modify end;
}
