/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package test.biz.praybill.vo;
	
import nc.vo.pub.SuperVO;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:
 * @author 
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
public class TestPrayBillMaster extends SuperVO {
	private java.lang.String id;
	private java.lang.String supplier;
	private nc.vo.pub.lang.UFDouble sum;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	private test.biz.praybill.vo.TestPrayBillDetail[] detailid;

	public static final String ID = "id";
	public static final String SUPPLIER = "supplier";
	public static final String SUM = "sum";
			
	/**
	 * 属性id的Getter方法.属性名：master id
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getId () {
		return id;
	}   
	/**
	 * 属性id的Setter方法.属性名：master id
	 * 创建日期:
	 * @param newId java.lang.String
	 */
	public void setId (java.lang.String newId ) {
	 	this.id = newId;
	} 	  
	/**
	 * 属性supplier的Getter方法.属性名：supplier name
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getSupplier () {
		return supplier;
	}   
	/**
	 * 属性supplier的Setter方法.属性名：supplier name
	 * 创建日期:
	 * @param newSupplier java.lang.String
	 */
	public void setSupplier (java.lang.String newSupplier ) {
	 	this.supplier = newSupplier;
	} 	  
	/**
	 * 属性sum的Getter方法.属性名：sum name
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getSum () {
		return sum;
	}   
	/**
	 * 属性sum的Setter方法.属性名：sum name
	 * 创建日期:
	 * @param newSum nc.vo.pub.lang.UFDouble
	 */
	public void setSum (nc.vo.pub.lang.UFDouble newSum ) {
	 	this.sum = newSum;
	} 	  
	/**
	 * 属性detailid的Getter方法.属性名：detail id
	 * 创建日期:
	 * @return test.biz.praybill.vo.TestPrayBillDetail[]
	 */
	public test.biz.praybill.vo.TestPrayBillDetail[] getDetailid () {
		return detailid;
	}   
	/**
	 * 属性detailid的Setter方法.属性名：detail id
	 * 创建日期:
	 * @param newDetailid test.biz.praybill.vo.TestPrayBillDetail[]
	 */
	public void setDetailid (test.biz.praybill.vo.TestPrayBillDetail[] newDetailid ) {
	 	this.detailid = newDetailid;
	} 	  
	/**
	 * 属性dr的Getter方法.属性名：dr
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性ts的Getter方法.属性名：ts
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.属性名：ts
	 * 创建日期:
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "id";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "praybillmaster";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "test2";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public TestPrayBillMaster() {
		super();	
	}    
} 


