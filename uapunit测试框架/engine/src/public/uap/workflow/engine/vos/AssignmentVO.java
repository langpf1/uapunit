package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
public class AssignmentVO extends SuperVO {
	private static final long serialVersionUID = 6246313011206790904L;
	private String pk_assignment;
	private String pk_proins;
	private String activity_id;
	private String pk_user;
	private UFBoolean sequence;
	private String order_str;
	private UFDateTime ts;
	private java.lang.Integer dr;
	
	public String getPk_assignment() {
		return pk_assignment;
	}
	public void setPk_assignment(String pk_assignment) {
		this.pk_assignment = pk_assignment;
	}
	public String getPk_proins() {
		return pk_proins;
	}
	public void setPk_proins(String pk_proins) {
		this.pk_proins = pk_proins;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getPk_user() {
		return pk_user;
	}
	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}
	public UFBoolean getSequence() {
		return sequence;
	}
	public void setSequence(UFBoolean sequence) {
		this.sequence = sequence;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public java.lang.Integer getDr() {
		return dr;
	}
	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}
	public String getOrder_str() {
		return order_str;
	}
	public void setOrder_str(String order_str) {
		this.order_str = order_str;
	}
	public String getPKFieldName() {
		return "pk_assignment";
	}
	public String getTableName() {
		return "wf_assignment";
	}
}
