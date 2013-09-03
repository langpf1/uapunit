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
	 * �ʵ����ʶ
	 */
	private String pk_actins;
	/**
	 * ����ʵ����ʶ
	 */
	private String pk_proins;
	/**
	 * ���ʵ����ʶ
	 */
	private String pk_parent;
	/**
	 * �˹���ڵ�ID
	 */
	private String port_id;
	/**
	 * ���˹���ڵ�ID
	 */
	private String pport_id;
	/**
	 * ֻҪ��һ������ִ�й�������ʵ�����Ǳ�ִ�й�
	 */
	private UFBoolean isexe;
	/**
	 * ����ڵ��Ƿ�ͨ����������ɲ����������Ƿ���ɣ�
	 */
	private UFBoolean ispass;
	/**
	 * ���������ʶ�Ƿ����˻ز����Ľڵ�
	 */
	private UFBoolean isreject;
	/**
	 * ��������Ļʵ�����У�ָ���Ӧ�������̻ʵ��
	 */
	private String pk_super;
	
	/**
	 * �ʵ��״̬
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
