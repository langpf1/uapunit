package uap.workflow.pub.app.mobile.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * 移动审批所需的会话信息
 * 
 * <li>与表pub_mobiledata对应
 * 
 * @author leijun 2006-9-18
 */
public class MobileData extends SuperVO {
	public String pk_mobiledata;

	public String cmd;

	public String pk_sid;

	public String mobile;

	public String billtype;

	public String billid;

	public UFDateTime ts;

	public Integer dr;

	//登录系统的编码
	private String bizCenterCode;
	
	//集团ID
	private String groupId;

	//集团Code
	private String groupNumber;

	//行业编码
	private String hyCode;

	//数据源
	private String userDataSource;

	//登录用户
	private String userId;

	//登录语言
	private String langCode;

	//业务日期
	private UFDateTime bizDateTime;
	
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPKFieldName() {
		return "pk_mobiledata";
	}

	@Override
	public String getTableName() {
		return "pub_mobiledata";
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPk_mobiledata() {
		return pk_mobiledata;
	}

	public void setPk_mobiledata(String pk_mobiledata) {
		this.pk_mobiledata = pk_mobiledata;
	}

	public String getPk_sid() {
		return pk_sid;
	}

	public void setPk_sid(String sid) {
		this.pk_sid = sid;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	/*----------------为接收短信审批新加的*/
	public String getHyCode() {
		return hyCode;
	}

	public void setHyCode(String hyCode) {
		this.hyCode = hyCode;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getUserDataSource() {
		return userDataSource;
	}

	public void setUserDataSource(String datasource) {
		this.userDataSource = datasource;
	}

	public String getBizCenterCode() {
		return bizCenterCode;
	}

	public void setBizCenterCode(String systemId) {
		this.bizCenterCode = systemId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public UFDateTime getBizDateTime() {
		return bizDateTime;
	}

	public void setBizDateTime(UFDateTime bizDateTime) {
		this.bizDateTime = bizDateTime;
	}
}
