package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
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
public class BeforeAddSignVO extends SuperVO {
	public static final String TYPE_AND = "and";// //����
	public static final String TYPE_OR = "or";// //����
	private java.lang.String pk_beforeaddsign;
	private java.lang.String pk_task;
	private java.lang.String times;
	private java.lang.String logic;
	private java.lang.String scratch;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	private BeforeAddSignUserVO[] beforeAddSignUserVos;
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
	 * ����pk_task��Getter����.���������������� ��������:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_task() {
		return pk_task;
	}
	/**
	 * ����pk_task��Setter����.���������������� ��������:
	 * 
	 * @param newPk_task
	 *            java.lang.String
	 */
	public void setPk_task(java.lang.String newPk_task) {
		this.pk_task = newPk_task;
	}
	public java.lang.String getTimes() {
		return times;
	}
	public void setTimes(java.lang.String times) {
		this.times = times;
	}
	public java.lang.String getLogic() {
		return logic;
	}
	public void setLogic(java.lang.String logic) {
		this.logic = logic;
	}
	
	public java.lang.String getScratch() {
		return scratch;
	}
	public void setScratch(java.lang.String scratch) {
		this.scratch = scratch;
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
		return "pk_beforeaddsign";
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
		return "wf_beforeaddsign";
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
		return "wf_beforeaddsign";
	}
	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:
	 */
	public BeforeAddSignVO() {
		super();
	}
	public BeforeAddSignUserVO[] getBeforeAddSignUserVos() {
		return beforeAddSignUserVos;
	}
	public void setBeforeAddSignUserVos(BeforeAddSignUserVO[] beforeAddSignUserVos) {
		this.beforeAddSignUserVos = beforeAddSignUserVos;
	}
}
