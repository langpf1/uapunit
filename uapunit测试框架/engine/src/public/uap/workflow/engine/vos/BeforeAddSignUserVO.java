package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * ��������:
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
	 * ����pk_user��Getter����.���������û����� ��������:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_user() {
		return pk_user;
	}
	/**
	 * ����pk_user��Setter����.���������û����� ��������:
	 * 
	 * @param newPk_user
	 *            java.lang.String
	 */
	public void setPk_user(java.lang.String newPk_user) {
		this.pk_user = newPk_user;
	}
	/**
	 * ����pk_dept��Getter����.���������������� ��������:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_dept() {
		return pk_dept;
	}
	/**
	 * ����pk_dept��Setter����.���������������� ��������:
	 * 
	 * @param newPk_dept
	 *            java.lang.String
	 */
	public void setPk_dept(java.lang.String newPk_dept) {
		this.pk_dept = newPk_dept;
	}
	/**
	 * ����pk_beforeaddsign��Getter����.��������ǰ��ǩ���� ��������:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_beforeaddsign() {
		return pk_beforeaddsign;
	}
	/**
	 * ����pk_beforeaddsign��Setter����.��������ǰ��ǩ���� ��������:
	 * 
	 * @param newPk_beforeaddsign
	 *            java.lang.String
	 */
	public void setPk_beforeaddsign(java.lang.String newPk_beforeaddsign) {
		this.pk_beforeaddsign = newPk_beforeaddsign;
	}
	/**
	 * ����dr��Getter����.��������dr ��������:
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}
	/**
	 * ����dr��Setter����.��������dr ��������:
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}
	/**
	 * ����ts��Getter����.��������ts ��������:
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}
	/**
	 * ����ts��Setter����.��������ts ��������:
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}
	/**
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}
	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_beforeaddsignuser";
	}
	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:
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
	 * ���ر�����.
	 * <p>
	 * ��������:
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "wfm_addsignuser";
	}
	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:
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
