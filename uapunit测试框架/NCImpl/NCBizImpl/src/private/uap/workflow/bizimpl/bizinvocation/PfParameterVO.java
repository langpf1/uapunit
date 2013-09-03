package uap.workflow.bizimpl.bizinvocation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import nc.md.data.access.NCObject;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.change.PublicHeadVO;
import nc.vo.pub.workflownote.WorkflownoteVO;

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
		tmpParaVo.m_actionName = this.m_actionName;
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

	public String getM_billVersionPK() {
		return m_billVersionPK;
	}
	
}