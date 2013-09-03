package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * ����ҵ��������VO
 * <li>�������ļ�
 * 
 * @author leijun 2006-2-14
 */
public class MobilePluginVO implements Serializable{
	/**
	 * ���������ģ����
	 */
	public String module;

	/**
	 * ָ�
	 */
	public String[] commands;

	/**
	 * ҵ��������
	 */
	public String classname;

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String[] getCommands() {
		return commands;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

}
