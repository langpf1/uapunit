package nc.vo.pub.compiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.md.data.access.NCObject;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.change.PublicHeadVO;

/**
 * 平台运行的参数类
 * 
 * @author 樊冠军 2002-2-28
 * @modifier leijun 2005-3-14 增加一个变量,解决制单即审批的问题
 * @modifier leijun 2007-5-9 增加流程类别的变量
 * @modifier leijun 2008-2-26 增加业务单据实体的变量，以适配元数据
 * @modifier yanke1 2012-4-19 业务日志需要通过反射getter取单据pk，因此添加了getM_billVersionPK()方法。
 * 							     
 */
public class PfParameterVO implements Serializable {
	/** 单据类型 */
	public String m_billType = null;

	/** 交易类型 */
	public String m_transType = null;
	
	/** 业务类型 */
	public String m_businessType = null;

	/** 动作名称 */
	public String m_actionName = null;

	/** 单据号 */
	public String m_billNo = null;

	/** 单据ID */
	public String m_billVersionPK = null;
	
	/**m_billId*/
	public String m_billId =null;

	/** 制单人PK */
	public String m_makeBillOperator = null;

	/** 操作员PK */
	public String m_operator = null;

	/** 组织主键 XXX:add by leijun 2009-3 */
	public String m_pkOrg = null;

	/** 集团主键 XXX:add by leijun 2009-3 */
	public String m_pkGroup = null;

	/** 标准的主表VO */
	public PublicHeadVO m_standHeadVo = null;

	/** 产品组自定义对象 */
	public Object m_userObj = null;

	/** 产品组自定义对象数组 */
	public Object[] m_userObjs = null;

	/** 原传入Vo值 */
	public AggregatedValueObject m_preValueVo = null;

	/** 原传入Vo值数组 */
	public AggregatedValueObject[] m_preValueVos = null;

	/** 工作项VO */
	public WorkflownoteVO m_workFlow = null;

	/** 是否提交后自动审批 lj+ */
	public boolean m_autoApproveAfterCommit = false;

	/** 分单形成的Vo值数组 */
	public AggregatedValueObject[] m_splitValueVos = null;

	/** 流程定义PK */
	public String m_flowDefPK = null;

	/** 单据实体对象 */
	public Object m_billEntity = null;

	/** 单据实体对象数组 */
	public Object[] m_billEntities = null;
	
	public NCObject ncobject =null;

	private HashMap<String, Object> customProperties = new HashMap<String, Object>();
	
	/**修订单据 */
	public int emendEnum ;

	public PfParameterVO() {
		super();
	}

	public Object getCustomProperty(String key) {
		return customProperties.get(key);
	}

	public void pubCustomProperty(String key, Object value) {
		customProperties.put(key, value);
	}

	public void removeCustomProperty(String key) {
		customProperties.remove(key);
	}

	public void putCustomPropertyBatch(HashMap hmParam) {
		if (hmParam == null)
			return;

		for (Iterator iterator = hmParam.keySet().iterator(); iterator
				.hasNext();) {
			Object key = iterator.next();
			if (key == null)
				continue;
			pubCustomProperty((String) key, hmParam.get(key));
		}
	}

	public HashMap getCustomPropertyBatch() {
		HashMap newMap = new HashMap<String, Object>();
		for (Iterator iterator = customProperties.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			if (key == null)
				continue;
			newMap.put((String) key, customProperties.get(key));
		}
		return newMap;
	}
	
	/**
	 * 复制出一个新的参数对象
	 */
	public PfParameterVO clone() {
		PfParameterVO tmpParaVo = new PfParameterVO();
		tmpParaVo.m_actionName = this.m_actionName;
		tmpParaVo.m_billType = this.m_billType;
		tmpParaVo.m_businessType = this.m_businessType;
		tmpParaVo.m_billNo = this.m_billNo;
		tmpParaVo.m_makeBillOperator = this.m_makeBillOperator;
		tmpParaVo.m_operator = this.m_operator;
		tmpParaVo.m_pkOrg = this.m_pkOrg;
		tmpParaVo.m_pkGroup = this.m_pkGroup;
		tmpParaVo.m_billVersionPK = this.m_billVersionPK;
		tmpParaVo.m_billId =this.m_billId;
		tmpParaVo.m_autoApproveAfterCommit = this.m_autoApproveAfterCommit;
		// 产品组自定义对象
		tmpParaVo.m_userObj = this.m_userObj;
		// 原传入Vo值
		tmpParaVo.m_preValueVo = this.m_preValueVo;
		tmpParaVo.m_preValueVos =this.m_preValueVos;
		tmpParaVo.emendEnum =this.emendEnum;
		tmpParaVo.putCustomPropertyBatch(this.getCustomPropertyBatch());
		return tmpParaVo;
	}
	
	/**
	 * 复制出一个新的参数对象
	 */
	public WFAppParameter toWFAppParameter() {
		WFAppParameter para = new WFAppParameter();
		para.setActionName(this.m_actionName);
		//para.m_businessType = this.m_businessType;
		para.setBillMaker(this.m_makeBillOperator);
		para.setOperator(this.m_operator);
		para.setWorkFlow(this.m_workFlow);
		para.setAutoApproveAfterCommit(this.m_autoApproveAfterCommit);
		para.setProcessDefPK(m_flowDefPK);
		para.setOrgPK(this.m_pkOrg);
		para.setGroupPK(this.m_pkGroup);
		para.setEmendEnum(this.emendEnum);
		IBusinessKey businessObject = new BizObjectImpl();
		businessObject.setBillId(this.m_billVersionPK);
		businessObject.setBillNo(m_billNo);
		businessObject.setBillType(m_billType);
		if(this.m_preValueVos!=null)
		{
			businessObject.setBizObjects(this.m_preValueVos);
		}
		if(this.m_preValueVo!=null)
		{
			Object[] bizObject = new Object[1];
			bizObject[0] = this.m_preValueVo;
			businessObject.setBizObjects(bizObject);
		}
		
		para.setBusinessObject(businessObject);
		para.setBizDateTime(InvocationInfoProxy.getInstance().getBizDateTime());

//		if(this.m_preValueVo != null)
//		{
//			return this.m_preValueVo.getParentVO();
//		}
		return para;
	}


	public String getM_billVersionPK() {
		return m_billVersionPK;
	}
	
}