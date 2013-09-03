package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * 短信业务插件配置VO
 * <li>配置在文件
 * 
 * @author leijun 2006-2-14
 */
public class MobilePluginVO implements Serializable{
	/**
	 * 插件所属的模块名
	 */
	public String module;

	/**
	 * 指令集
	 */
	public String[] commands;

	/**
	 * 业务插件类名
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
