package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * 移动短信UFMobile-NC配置VO
 * <li>与配置文件mobileplugin.xml对应
 * 
 * @author leijun 2006-2-14
 * @modifier ewei 2007-9-26 增加公共开发部短信服务服务器地址
 */
public class MobileConfig implements Serializable {
	/**
	 * UFMobile配置信息
	 */
	public UFMobileConfig ufmobile;

	/**
	 * 公共开发部短信服务服务器信息
	 */
	public PubDevSmsConfig pubdev;

	/**
	 * 默认使用的数据源编码，应用于多帐套系统
	 */
	public String datasource;

	/**
	 * 数字签名服务实现类
	 */
	public String signprovider;

	/**
	 * 邮件审批是否使用数字签名验证
	 */
	public boolean signature = false;

	/**
	 * NC短信业务处理插件
	 */
	public MobilePluginVO[] mobilePlugins;

	/**
	 * 短信服务实现类注册信息
	 */
	public MobileImplInfo[] mobileimplinfos;

	public MobileImplInfo[] getMobileimplinfos() {
		return mobileimplinfos;
	}

	public void setMobileimplinfos(MobileImplInfo[] mobileimplinfos) {
		this.mobileimplinfos = mobileimplinfos;
	}

	public String getDatasource() {
		return datasource;
	}

	public UFMobileConfig getUfmobile() {
		return ufmobile;
	}

	public void setUfmobile(UFMobileConfig ufmobile) {
		this.ufmobile = ufmobile;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public MobilePluginVO[] getMobilePlugins() {
		return mobilePlugins;
	}

	public void setMobilePlugins(MobilePluginVO[] mobilePlugins) {
		this.mobilePlugins = mobilePlugins;
	}

	public PubDevSmsConfig getPubSms() {
		return pubdev;
	}

	public void setPubSms(PubDevSmsConfig pubdev) {
		this.pubdev = pubdev;
	}

	public boolean isSignature() {
		return signature;
	}

	public void setSignature(boolean signature) {
		this.signature = signature;
	}

	public String getSignprovider() {
		return signprovider;
	}

	public void setSignprovider(String signprovider) {
		this.signprovider = signprovider;
	}

}
