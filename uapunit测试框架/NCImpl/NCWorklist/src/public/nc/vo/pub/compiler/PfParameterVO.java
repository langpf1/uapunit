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
 * ƽ̨���еĲ�����
 * 
 * @author ���ھ� 2002-2-28
 * @modifier leijun 2005-3-14 ����һ������,����Ƶ�������������
 * @modifier leijun 2007-5-9 �����������ı���
 * @modifier leijun 2008-2-26 ����ҵ�񵥾�ʵ��ı�����������Ԫ����
 * @modifier yanke1 2012-4-19 ҵ����־��Ҫͨ������getterȡ����pk����������getM_billVersionPK()������
 * 							     
 */
public class PfParameterVO implements Serializable {
	/** �������� */
	public String m_billType = null;

	/** �������� */
	public String m_transType = null;
	
	/** ҵ������ */
	public String m_businessType = null;

	/** �������� */
	public String m_actionName = null;

	/** ���ݺ� */
	public String m_billNo = null;

	/** ����ID */
	public String m_billVersionPK = null;
	
	/**m_billId*/
	public String m_billId =null;

	/** �Ƶ���PK */
	public String m_makeBillOperator = null;

	/** ����ԱPK */
	public String m_operator = null;

	/** ��֯���� XXX:add by leijun 2009-3 */
	public String m_pkOrg = null;

	/** �������� XXX:add by leijun 2009-3 */
	public String m_pkGroup = null;

	/** ��׼������VO */
	public PublicHeadVO m_standHeadVo = null;

	/** ��Ʒ���Զ������ */
	public Object m_userObj = null;

	/** ��Ʒ���Զ���������� */
	public Object[] m_userObjs = null;

	/** ԭ����Voֵ */
	public AggregatedValueObject m_preValueVo = null;

	/** ԭ����Voֵ���� */
	public AggregatedValueObject[] m_preValueVos = null;

	/** ������VO */
	public WorkflownoteVO m_workFlow = null;

	/** �Ƿ��ύ���Զ����� lj+ */
	public boolean m_autoApproveAfterCommit = false;

	/** �ֵ��γɵ�Voֵ���� */
	public AggregatedValueObject[] m_splitValueVos = null;

	/** ���̶���PK */
	public String m_flowDefPK = null;

	/** ����ʵ����� */
	public Object m_billEntity = null;

	/** ����ʵ��������� */
	public Object[] m_billEntities = null;
	
	public NCObject ncobject =null;

	private HashMap<String, Object> customProperties = new HashMap<String, Object>();
	
	/**�޶����� */
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
	 * ���Ƴ�һ���µĲ�������
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
		// ��Ʒ���Զ������
		tmpParaVo.m_userObj = this.m_userObj;
		// ԭ����Voֵ
		tmpParaVo.m_preValueVo = this.m_preValueVo;
		tmpParaVo.m_preValueVos =this.m_preValueVos;
		tmpParaVo.emendEnum =this.emendEnum;
		tmpParaVo.putCustomPropertyBatch(this.getCustomPropertyBatch());
		return tmpParaVo;
	}
	
	/**
	 * ���Ƴ�һ���µĲ�������
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