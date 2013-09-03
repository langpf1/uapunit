package uap.workflow.app.core;

import java.io.Serializable;

/**
 * 从流程角度的抽象的业务对象定义
 */
public interface IBusinessKey extends Serializable {
	
	void setBizObjects(Object[] bizObjects);

	Object[] getBizObjects();

	/**
	 * 业务对象类型，接口名字不变了，语义扩展
	 * 
	 * @return
	 */
	String getBillType();

	/**
	 * 业务对象类型，接口名字不变了，语义扩展
	 * 
	 * @param billtype
	 */
	void setBillType(String billtype);

	/**
	 * 业务对象PK，接口名字不变了，语义扩展
	 * 修订审批流时，请传修订单据PK
	 * @return
	 */
	String getBillId();

	/**
	 * 业务对象PK，接口名字不变了，语义扩展
	 * 
	 * @return
	 */
	void setBillId(String billId);
	
	/**
	 * 业务对象编码，接口名字不变了，语义扩展
	 * 
	 * @return
	 */
	String getBillNo();

	/**
	 * 业务对象编码，接口名字不变了，语义扩展
	 * 
	 * @return
	 */
	void setBillNo(String billNo);
}
