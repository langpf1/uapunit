package uap.workflow.pub.app.message.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * 消息片VO，对应数据库表pub_messagelet
 * 
 * @author leijun 2008-12
 * @since 5.5
 */
public class MessageLetVO extends SuperVO {

	private String pk_messagelet;

	private String uiclass;

	private String code;

	private String name;

	private String resid;

	/*用于前台选择，不存入数据库*/
	private UFBoolean isSelected;

	private Integer dr;

	private UFDateTime ts;

	public String getPk_messagelet() {
		return pk_messagelet;
	}

	public void setPk_messagelet(String pk_messagelet) {
		this.pk_messagelet = pk_messagelet;
	}

	public String getUiclass() {
		return uiclass;
	}

	public void setUiclass(String uiclass) {
		this.uiclass = uiclass;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_messagelet";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "pub_messagelet";
	}

	public UFBoolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(UFBoolean isSelected) {
		this.isSelected = isSelected;
	}

}
