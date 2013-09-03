package nc.itf.uap.pf.metadata;

import uap.workflow.app.core.IBusinessKey;
import nc.vo.pub.lang.UFDateTime;

/**
 * ����ƽ̨ҵ��ӿ�-��ȡ����д���������Ϣ
 * <li>��ҵ�񵥾ݶ���Ԫģ��ʵ��
 * 
 * @author leijun 2008-3
 * @modifier leijun 2009-3 ����V6����֯
 * 
 * @since 5.5
 */
public interface IFlowBizItf extends IBusinessKey{
	static final String ATTRIBUTE_BILLMAKER = "billmaker";

	static final String ATTRIBUTE_APPROVER = "approver";

	static final String ATTRIBUTE_BILLID = "billid";

	static final String ATTRIBUTE_BILLNO = "billno";

	static final String ATTRIBUTE_PKORG = "pkorg";

	static final String ATTRIBUTE_BUSITYPE = "busitype";

	static final String ATTRIBUTE_APPROVESTATUS = "approvestatus";

	static final String ATTRIBUTE_APPROVENOTE = "approvenote";

	static final String ATTRIBUTE_APPROVEDATE = "approvedate";

	static final String ATTRIBUTE_BILLTYPE = "billtype";
	
	static final String ATTRIBUTE_SRCBILLTYPE = "srcbilltype";
	
	static final String ATTRIBUTE_SRCBILLID = "srcbillid";
	
	static final String ATTRIBUTE_TRANSTYPE = "transtype";
	
	static final String ATTRIBUTE_TRANSTYPEPK = "transtypepk";
	
	//�Ƿ��޶�����
	static final String ATTRIBUTE_EMEND="emendenum";
	//�����޶�������ʱʹ��
	static final String ATTRIBUTE_BILLVERSIONPK="billversionpk";
	
	
	String getBillVersionPK();
	
	void setBillVersionPK(String billVersionPK);
	
	/**
	 * 
	 * */
	Integer getEmendEnum();
	
	/**
	 * ��д�Ƿ��޶�����
	 * */
	void setEmendEnum(Integer emendEnum);
	
	/**
	 * ���ؽ�������
	 * @return
	 */
	String getTranstype();

	/**
	 * ��д��������
	 * @param transtype
	 */
	void setTranstype(String transtype);

	/**
	 * ���ص�������
	 * @return
	 */
	String getBillType();

	/**
	 * ��д��������
	 * @param billtype
	 */
	void setBillType(String billtype);

	/**
	 * �����Ƶ���PK
	 * @return
	 */
	String getBillMaker();

	/**
	 * ����������PK
	 * @return
	 */
	String getApprover();

	/**
	 * ���ص���PK
	 * @return
	 */
	String getBillId();

	/**
	 * ���ص��ݺ�
	 * @return
	 */
	String getBillNo();

	/**
	 * ������֯PK
	 * @return
	 */
	String getPkorg();

	/**
	 * ����ҵ������PK
	 * @return
	 */
	String getBusitype();

	/**
	 * ���ؽӿ������ڵ���ʵ���� ��Ӧ�����ݿ��ֶ� 
	 * @return
	 */
	String getColumnName(String itfAttrName);

	/**
	 * ���ص��ݵ�����״̬
	 * @return
	 */
	Integer getApproveStatus();

	/**
	 * ���ص��ݵ���������
	 * @return
	 */
	String getApproveNote();

	/**
	 * ���ص��ݵ�����ʱ��
	 * @return
	 */
	UFDateTime getApproveDate();

	/**
	 * ��д���ݵ�����ʱ��
	 * @return
	 */
	void setApproveDate(UFDateTime approveDate);

	/**
	 * ��д���ݵ���������
	 * @return
	 */
	void setApproveNote(String approveNote);

	/**
	 * ��д���ݵ�����״̬
	 * @return
	 */
	void setApproveStatus(Integer iStatus);

	/**
	 * ��д��֯PK
	 * @return
	 */
	void setPkorg(String pkorg);

	/**
	 * ��д���ݵ�ҵ������
	 * @return
	 */
	void setBusitype(String busitype);

	/**
	 * ��д���ݵ�������
	 * @return
	 */
	void setApprover(String approver);

	/**
	 * ��д���ݺ�
	 * @return
	 */
	void setBillNo(String billNo);

	/**
	 * ��д����PK
	 * @return
	 */
	void setBillId(String billId);

	/**
	 * ��д���ݵ��Ƶ���
	 * @return
	 */
	void setBillMaker(String maker);
	
	String getSourceBillType();
	
	String getSourceBillID();
	
	void setSourceBillType(String srcBillType);
	
	void setSourceBillID(String srcBillId);
	
	String getTranstypePk();
	void setTranstypePk(String transtypePk);
}
