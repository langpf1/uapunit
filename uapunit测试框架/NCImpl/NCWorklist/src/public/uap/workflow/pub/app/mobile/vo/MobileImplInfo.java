package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * ���ŷ���ʵ���� ��ע����Ϣ
 * 
 * @author leijun 2007-3-22 
 */
public class MobileImplInfo implements Serializable {
	/**
	 * ʵ���� ȫ·���޶���
	 */
	private String qualifiedclass;

	/**
	 * �Ƿ�����
	 */
	private boolean active;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getQualifiedclass() {
		return qualifiedclass;
	}

	public void setQualifiedclass(String qualifiedclass) {
		this.qualifiedclass = qualifiedclass;
	}

}
