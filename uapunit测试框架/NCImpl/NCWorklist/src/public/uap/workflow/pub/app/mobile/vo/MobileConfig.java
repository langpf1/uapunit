package uap.workflow.pub.app.mobile.vo;

import java.io.Serializable;

/**
 * �ƶ�����UFMobile-NC����VO
 * <li>�������ļ�mobileplugin.xml��Ӧ
 * 
 * @author leijun 2006-2-14
 * @modifier ewei 2007-9-26 ���ӹ������������ŷ����������ַ
 */
public class MobileConfig implements Serializable {
	/**
	 * UFMobile������Ϣ
	 */
	public UFMobileConfig ufmobile;

	/**
	 * �������������ŷ����������Ϣ
	 */
	public PubDevSmsConfig pubdev;

	/**
	 * Ĭ��ʹ�õ�����Դ���룬Ӧ���ڶ�����ϵͳ
	 */
	public String datasource;

	/**
	 * ����ǩ������ʵ����
	 */
	public String signprovider;

	/**
	 * �ʼ������Ƿ�ʹ������ǩ����֤
	 */
	public boolean signature = false;

	/**
	 * NC����ҵ������
	 */
	public MobilePluginVO[] mobilePlugins;

	/**
	 * ���ŷ���ʵ����ע����Ϣ
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
