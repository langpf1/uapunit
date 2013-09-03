package uap.workflow.nc.participant.wfusergroup;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.MultiLangUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * �����û�������VO ���pub_wfgroup ��Ӧ
 * 
 * @dingxm 2009-07-14
 * @modifer zhouzhenga ���������Ϣ�Ͷ�������֧��
 */
public class WFUserGroupVO extends SuperVO {

	/** ���� */
	public String pk_wfgroup;
	/** ���� */
	public String code;
	/** ���� */
	public String name;

	public String name2;

	public String name3;
	/** ���Ͷ��� */
	public String deftype;
	/** �������� */
	public String pk_group;
	/** ����ʱ�� */
	public UFDateTime createdate;
	/** ������ */
	public String createman;
	/** �������� */
	public String billtype;
	/** ʱ��� */
	private UFDateTime ts;
	/** ����޸��� */
	private String operator;
	/** ����޸�ʱ�� */
	private UFDateTime operatedate;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public UFDateTime getOperatedate() {
		return operatedate;
	}

	public void setOperatedate(UFDateTime operatedate) {
		this.operatedate = operatedate;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getPk_wfgroup() {
		return pk_wfgroup;
	}

	public void setPk_wfgroup(String pk_wfgroup) {
		this.pk_wfgroup = pk_wfgroup;
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

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getDeftype() {
		return deftype;
	}

	public void setDeftype(String deftype) {
		this.deftype = deftype;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public UFDateTime getCreatedate() {
		return createdate;
	}

	public void setCreatedate(UFDateTime createdate) {
		this.createdate = createdate;
	}

	public String getCreateman() {
		return createman;
	}

	public void setCreateman(String createman) {
		this.createman = createman;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_wfgroup";
	}

	public String getPrimaryKey() {
		return pk_wfgroup;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "pub_wfgroup";
	}

	public boolean validata() {
		return true;
	}

	public String getNameOfCurrLang() {
		int langIdx = MultiLangUtil.getCurrentLangSeq();

		String s = null;
		switch (langIdx) {
		case 1:
			s = name;
			break;
		case 2:
			s = name2;
			break;
		case 3:
			s = name3;
			break;
		default:
			break;
		}

		if (StringUtil.isEmptyWithTrim(s)) {
			s = getName() != null ? getName()
					: (getName2() != null ? getName2()
							: getName3() != null ? getName3() : "");
		}

		return s;
	}

	@Override
	public String toString() {
		return this.getCode() + " " + getNameOfCurrLang();
	}

}
