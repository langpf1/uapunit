package uap.workflow.app.notice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * ֪ͨ��������
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReceiverVO")
public class ReceiverVO implements Serializable {

	//����������
	@XmlAttribute
	public String name = "";

	//�����߱���
	@XmlAttribute
	public String code = "";

	//����������
	@XmlAttribute
	public String pk = "";

	//����������
	@XmlAttribute
	public int type = 0;

	//���������ڹ�˾����
	@XmlAttribute
	public String corppk = "";

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCorppk() {
		return corppk;
	}

	public void setCorppk(String corppk) {
		this.corppk = corppk;
	}

	public ReceiverVO() {
		name = "";
		code = "";
		pk = "";
		type = 0;
		corppk = "";
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPK() {
		return this.pk;
	}

	public void setPK(String pk) {
		this.pk = pk;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCorpPK() {
		return this.corppk;
	}

	public void setCorpPK(String corpPK) {
		this.corppk = corpPK;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof ReceiverVO) {
			ReceiverVO oth = (ReceiverVO) other;
			String srcSTR = getCorpPK() + getCode();
			String otherSTR = oth.getCorpPK() + oth.getCode();
			if (srcSTR.equals(otherSTR))
				return true;
		}

		return false;
	}
}