package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * 短信服务实现类 的注册信息
 * 
 * @author leijun 2007-3-22 
 */
public class MobileImplInfo implements Serializable {
	/**
	 * 实现类 全路径限定名
	 */
	private String qualifiedclass;

	/**
	 * 是否启用
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
