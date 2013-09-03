package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
/**
 * 
 * @author tianchw
 * 
 */
public class ActivityInstanceVO extends SuperVO {
	private static final long serialVersionUID = -2083244509642009460L;
	/**
	 * 活动实例标识
	 */
	private String pk_actins;
	/**
	 * 流程实例标识
	 */
	private String pk_proins;
	/**
	 * 父活动实例标识
	 */
	private String pk_parent;
	/**
	 * 人工活动节点ID
	 */
	private String port_id;
	/**
	 * 父人工活动节点ID
	 */
	private String pport_id;
	/**
	 * 只要有一个任务执行过，这个活动实例就是被执行过
	 */
	private UFBoolean isexe;
	/**
	 * 这个节点是否通过（根据完成策略来计算是否完成）
	 */
	private UFBoolean ispass;
	/**
	 * 这个用来标识是否是退回产生的节点
	 */
	private UFBoolean isreject;
	/**
	 * 子流程里的活动实例才有，指向对应的子流程活动实例
	 */
	private String pk_super;
	
	/**
	 * 活动实例状态
	 */
	private int state_actins;
	private String pk_prodef;
	private String prodef_id;
	private UFDateTime begindate;
	private UFDateTime ts;
	private Integer dr = 0;
	public String getPk_actins() {
		return pk_actins;
	}
	public void setPk_actins(String pk_actins) {
		this.pk_actins = pk_actins;
	}
	public String getPk_proins() {
		return pk_proins;
	}
	public void setPk_proins(String pk_proins) {
		this.pk_proins = pk_proins;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getPort_id() {
		return port_id;
	}
	public void setPort_id(String port_id) {
		this.port_id = port_id;
	}
	public UFBoolean getIsexe() {
		return isexe;
	}
	public void setIsexe(UFBoolean isexe) {
		this.isexe = isexe;
	}
	public UFBoolean getIspass() {
		return ispass;
	}
	public void setIspass(UFBoolean ispass) {
		this.ispass = ispass;
	}
	public UFBoolean getIsreject() {
		return isreject;
	}
	public void setIsreject(UFBoolean isreject) {
		this.isreject = isreject;
	}
	public int getState_actins() {
		return state_actins;
	}
	public void setState_actins(int state_actins) {
		this.state_actins = state_actins;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getPKFieldName() {
		return "pk_actins";
	}
	public String getTableName() {
		return "wf_actins";
	}
	public String getPk_prodef() {
		return pk_prodef;
	}
	public void setPk_prodef(String pk_prodef) {
		this.pk_prodef = pk_prodef;
	}
	public String getProdef_id() {
		return prodef_id;
	}
	public void setProdef_id(String prodef_id) {
		this.prodef_id = prodef_id;
	}
	public String getPport_id() {
		return pport_id;
	}
	public void setPport_id(String pport_id) {
		this.pport_id = pport_id;
	}
	public String getPk_super() {
		return pk_super;
	}
	public void setPk_super(String pk_super) {
		this.pk_super = pk_super;
	}
	public UFDateTime getBegindate() {
		return begindate;
	}
	public void setBegindate(UFDateTime begindate) {
		this.begindate = begindate;
	}
}
