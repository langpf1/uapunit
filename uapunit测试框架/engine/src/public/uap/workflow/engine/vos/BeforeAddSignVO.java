package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
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
public class BeforeAddSignVO extends SuperVO {
	public static final String TYPE_AND = "and";// //串行
	public static final String TYPE_OR = "or";// //并行
	private java.lang.String pk_beforeaddsign;
	private java.lang.String pk_task;
	private java.lang.String times;
	private java.lang.String logic;
	private java.lang.String scratch;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	private BeforeAddSignUserVO[] beforeAddSignUserVos;
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
	 * 属性pk_task的Getter方法.属性名：任务主键 创建日期:
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_task() {
		return pk_task;
	}
	/**
	 * 属性pk_task的Setter方法.属性名：任务主键 创建日期:
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
		return "pk_beforeaddsign";
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
		return "wf_beforeaddsign";
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
		return "wf_beforeaddsign";
	}
	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:
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
