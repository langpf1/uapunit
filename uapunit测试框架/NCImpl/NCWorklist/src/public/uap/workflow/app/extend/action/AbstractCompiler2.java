package uap.workflow.app.extend.action;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.pf.IPfExchangeService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.app.client.PfUtilTools;

/**
 * 实现平台编程运行环境接口的类2
 * <li>单据动作脚本类都继承自该类
 * 
 * @author 樊冠军 2002-2-28
 */
public class AbstractCompiler2 extends AbstractCompiler {
	public nc.vo.pub.compiler.PfParameterVO m_tmpVo = null;

	public AbstractCompiler2() {
		super();
	}

	/*
	 * 备注：进行单据VO的交换
	 */
	protected AggregatedValueObject changeData(AggregatedValueObject vo, String sourceBillType,
			String destBillType) throws BusinessException {
		return PfUtilTools.runChangeData(sourceBillType, destBillType, vo, m_tmpVo);
	}

	/*
	 * 备注：进行单据VO[]的交换
	 */
	protected AggregatedValueObject[] changeDataAry(AggregatedValueObject[] vo,
			String sourceBillType, String destBillType) throws BusinessException {
		IPfExchangeService exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
		// TODO: exchangeService.runChangeDataAry(sourceBillType, destBillType, vo, m_tmpVo);
		return null;
	}

	/*
	 * 备注：获得当前单据的单据类型
	 */
	protected String getBillType() {
		return m_tmpVo.m_billType;
	}

	/*
	 * 备注：获得当前单据日期
	 */
	//	protected UFDateTime getUserDate() {
	//		return new UFDateTime(m_tmpVo.m_currentDate);
	//	}
	/*
	 * 备注：获得当前单据输入用户对象
	 */
	protected Object getUserObj() {
		return m_tmpVo.m_userObj;
	}

	/*
	 * 备注：获得当前单据输入用户对象
	 */
	protected Object[] getUserObjAry() {
		return m_tmpVo.m_userObjs;
	}

	/*
	 * 备注：获得当前单据输入VO
	 */
	protected nc.vo.pub.AggregatedValueObject getVo() {
		return m_tmpVo.m_preValueVo;
	}

	/*
	 * 备注：获得当前单据输入VO[]
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
	 * 备注：重新设置当前单据输入VO
	 */
	protected void setUserObj(Object setObj) {
		m_tmpVo.m_userObj = setObj;
	}

	/**
	 * 重新设置当前单据输入VO
	 * <li>该方法主要给批弃审的动作脚本调用；
	 * <li>所以，必须重新从单据VO中获取一些信息，参见开发文档！
	 */
	protected void setVo(nc.vo.pub.AggregatedValueObject setVo) {
		m_tmpVo.m_preValueVo = setVo;
		//modified by leijun 2005-12-13
		//重新从单据聚合VO中获取一些信息，如billId、billNO、makeBillOperator等
		getHeadInfo(m_tmpVo);
	}

	/**
	 * 备注：重新设置当前单据输入VO[]
	 */
	protected void setVos(nc.vo.pub.AggregatedValueObject[] setVos) {
		m_tmpVo.m_preValueVos = setVos;
	}

	//公布PFParamVO请小心使用.只允许修改billNO, billID; modify by LYD
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
