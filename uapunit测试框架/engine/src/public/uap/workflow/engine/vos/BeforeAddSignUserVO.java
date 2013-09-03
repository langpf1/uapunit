package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 创建日期:
 * 
 * @author
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
public class BeforeAddSignUserVO extends SuperVO {
	private java.lang.String pk_beforeaddsignuser;
	private java.lang.String pk_user;
	private java.lang.String pk_dept;
	private String order_str;
	private UFBoolean isusered;
	private java.lang.String pk_beforeaddsign;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	public static final String PK_ADDSIGNUSER = "pk_addsignuser";
	public static final String PK_USER = "pk_user";
	public static final String PK_DEPT = "pk_dept";
	public static final String PK_BEFOREADDSIGN = "pk_beforeaddsign";
	public java.lang.String getPk_beforeaddsignuser() {
		return pk_beforeaddsignuser;
	}
	public void setPk_beforeaddsignuser(java.lang.String pk_beforeaddsignuser) {
		this.pk_beforeaddsignuser = pk_beforeaddsignuser;
	}
	/**
	 * 属性pk_user的Getter方法.属性名：用户主键 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_user() {
		return pk_user;
	}
	/**
	 * 属性pk_user的Setter方法.属性名：用户主键 创建日期:
	 * 
	 * @param newPk_user
	 *            java.lang.String
	 */
	public void setPk_user(java.lang.String newPk_user) {
		this.pk_user = newPk_user;
	}
	/**
	 * 属性pk_dept的Getter方法.属性名：部门主键 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_dept() {
		return pk_dept;
	}
	/**
	 * 属性pk_dept的Setter方法.属性名：部门主键 创建日期:
	 * 
	 * @param newPk_dept
	 *            java.lang.String
	 */
	public void setPk_dept(java.lang.String newPk_dept) {
		this.pk_dept = newPk_dept;
	}
	/**
	 * 属性pk_beforeaddsign的Getter方法.属性名：前加签主键 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_beforeaddsign() {
		return pk_beforeaddsign;
	}
	/**
	 * 属性pk_beforeaddsign的Setter方法.属性名：前加签主键 创建日期:
	 * 
	 * @param newPk_beforeaddsign
	 *            java.lang.String
	 */
	public void setPk_beforeaddsign(java.lang.String newPk_beforeaddsign) {
		this.pk_beforeaddsign = newPk_beforeaddsign;
	}
	/**
	 * 属性dr的Getter方法.属性名：dr 创建日期:
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}
	/**
	 * 属性dr的Setter方法.属性名：dr 创建日期:
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}
	/**
	 * 属性ts的Getter方法.属性名：ts 创建日期:
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}
	/**
	 * 属性ts的Setter方法.属性名：ts 创建日期:
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}
	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}
	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_beforeaddsignuser";
	}
	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "wf_beforeaddsignuser";
	}
	public UFBoolean getIsusered() {
		return isusered;
	}
	public void setIsusered(UFBoolean isusered) {
		this.isusered = isusered;
	}
	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "wfm_addsignuser";
	}
	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:
	 */
	public BeforeAddSignUserVO() {
		super();
	}
	public String getOrder_str() {
		return order_str;
	}
	public void setOrder_str(String order_str) {
		this.order_str = order_str;
	}
}
