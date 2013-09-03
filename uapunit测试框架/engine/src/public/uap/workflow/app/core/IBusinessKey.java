package uap.workflow.app.core;

import java.io.Serializable;

/**
 * �����̽Ƕȵĳ����ҵ�������
 */
public interface IBusinessKey extends Serializable {
	
	void setBizObjects(Object[] bizObjects);

	Object[] getBizObjects();

	/**
	 * ҵ��������ͣ��ӿ����ֲ����ˣ�������չ
	 * 
	 * @return
	 */
	String getBillType();

	/**
	 * ҵ��������ͣ��ӿ����ֲ����ˣ�������չ
	 * 
	 * @param billtype
	 */
	void setBillType(String billtype);

	/**
	 * ҵ�����PK���ӿ����ֲ����ˣ�������չ
	 * �޶�������ʱ���봫�޶�����PK
	 * @return
	 */
	String getBillId();

	/**
	 * ҵ�����PK���ӿ����ֲ����ˣ�������չ
	 * 
	 * @return
	 */
	void setBillId(String billId);
	
	/**
	 * ҵ�������룬�ӿ����ֲ����ˣ�������չ
	 * 
	 * @return
	 */
	String getBillNo();

	/**
	 * ҵ�������룬�ӿ����ֲ����ˣ�������չ
	 * 
	 * @return
	 */
	void setBillNo(String billNo);
}
