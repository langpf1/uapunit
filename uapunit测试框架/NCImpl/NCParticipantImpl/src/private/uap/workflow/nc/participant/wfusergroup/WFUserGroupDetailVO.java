package uap.workflow.nc.participant.wfusergroup;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
/**
 * �����û����ӱ�VO
 * ���pub_wfgroup_b��Ӧ
 * @dingxm 2009-07-14 
 */
public class WFUserGroupDetailVO extends SuperVO{
	
	/**����*/
	public String pk_wfgroup_b;
	/**���*/
	public String pk_wfgroup;

	//	/**��������*/
	//	public String rule_name;
	/**�������*/
	public String rule_code;
	/**��������*/
	public String rely_attribute;
	//���������
	private String rely_attrName;
	/**��������*/
	public String rule_type;
	/**��������*/
	public String rule_description;
	/***/
	public String pk_member;
	/**���²�����Ҫ�ֶ�*/
	public String opControl;	
	/**ʱ���*/
	private UFDateTime ts;  
	
	///////////�����ݿ��ֶΣ�������ʾ�ã�Added by guowl
	private String memberCode;
	private String memberName;
	private String memberOrgName;
	
	public String getRely_attrName() {
		return rely_attrName;
	}
	public void setRely_attrName(String rely_attrName) {
		this.rely_attrName = rely_attrName;
	}
	public String getOpControl() {
		return opControl;
	}
	public void setOpControl(String opControl) {
		this.opControl = opControl;
	}	
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getPk_wfgroup_b() {
		return pk_wfgroup_b;
	}
	public void setPk_wfgroup_b(String pk_wfgroup_b) {
		this.pk_wfgroup_b = pk_wfgroup_b;
	}
	public String getPk_wfgroup() {
		return pk_wfgroup;
	}
	public void setPk_wfgroup(String pk_wfgroup) {
		this.pk_wfgroup = pk_wfgroup;
	}
	public String getRule_code() {
		return rule_code;
	}
	public void setRule_code(String rule_code) {
		this.rule_code = rule_code;
	}
	public String getRely_attribute() {
		return rely_attribute;
	}
	public void setRely_attribute(String rely_attribute) {
		this.rely_attribute = rely_attribute;
	}
	public String getRule_type() {
		return rule_type;
	}
	public void setRule_type(String rule_type) {
		this.rule_type = rule_type;
	}
	public String getPk_member() {
		return pk_member;
	}
	public void setPk_member(String pk_member) {
		this.pk_member = pk_member;
	}
	@Override
	public String getPKFieldName() {
		return pk_wfgroup_b;
	}
	@Override
	public String getParentPKFieldName() {
		return null;
	}
	@Override
	public String getTableName() {
		return "pub_wfgroup_b";
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberOrgName() {
		return memberOrgName;
	}
	public void setMemberOrgName(String memberOrgName) {
		this.memberOrgName = memberOrgName;
	}
	public String getRule_description() {
		return rule_description;
	}
	public void setRule_description(String rule_description) {
		this.rule_description = rule_description;
	}
}
