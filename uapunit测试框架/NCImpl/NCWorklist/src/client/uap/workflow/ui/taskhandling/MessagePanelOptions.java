package uap.workflow.ui.taskhandling;

import java.io.Serializable;

/**
 * ��Ϣ����һЩ������Ϣ
 * <li>��������ɸѡ����
 * <li>�������ĸ�ʽ��Ϣ
 * <li>ˢ������
 * 
 * @author leijun 2006-5-23
 * @modifier leijun 2008-12 ������ϢƬ���������ڵ�
 * @modifier leijun 2009-4  6.0�ϲ�Ϊһ��
 */
public class MessagePanelOptions implements Serializable {

//	/**
//	 * XXX:�ö�����Ҫ���浽���ؿͻ��ˣ�������ҪUID
//	 */
//	private static final long serialVersionUID = -2222L;
//
//	/**
//	 * ��Ϣ���ĵ�����������
//	 * <li>XXX:6.0�ϲ�Ϊһ�� 
//	 */
//	//public static final String MESSAGE_BULLETIN = "bulletin";
//	//public static final String MESSAGE_PA = "prealert";
//	public static final String MESSAGE_WORKLIST = "worklist";
//
//	/**
//	 * ˢ������
//	 */
//	private RefreshSetting refreshSetting = null;
//
//	/**
//	 * �����ʽ�趨
//	 */
//	private HashMap mapTableSettings = null;
//
//	/**
//	 * ɸѡ��
//	 */
//	private HashMap mapFilters = null;
//
//	/**
//	 * ��ǰ��Ϣ����е���ϢƬ���������ڵ㣨���ܴ���UserObject��
//	 */
//	private LetNode letInfo = null;
//
//	/**
//	 * �������.����С��
//	 * @deprecated 5.5
//	 */
//	private double[] areaScales = { 1.0, 1.0, 1.0 };
//
//	public static final double[] DEFAULT_AREA_SCALES = { 1.0, 1.0, 1.0 };
//
//	public MessagePanelOptions() {
//		super();
//		refreshSetting = new RefreshSetting();
//		mapTableSettings = new HashMap();
//		mapFilters = new HashMap();
//	}
//
//	public LetNode getLetInfo() {
//		return letInfo;
//	}
//
//	public void setLetInfo(LetNode letInfo) {
//		this.letInfo = letInfo;
//	}
//
//	public RefreshSetting getFreshSetting() {
//		return refreshSetting;
//	}
//
//	public void setRefreshSetting(RefreshSetting refresh) {
//		this.refreshSetting = refresh;
//	}
//
//	/**
//	 * ���ش������� �ı���趨��Ϣ
//	 * 
//	 * @return
//	 */
//	public TableStyleSetting getWorklistTableSetting() {
//		TableStyleSetting tss = (TableStyleSetting) mapTableSettings.get(MESSAGE_WORKLIST);
//		if (tss == null) {
//			tss = new TableStyleSetting();
//			mapTableSettings.put(MESSAGE_WORKLIST, tss);
//		}
//		return tss;
//	}
//
//	/**
//	 * ���ش������� ����Ϣɸѡ��
//	 * 
//	 * @return
//	 */
//	public MessageFilter getWorklistFilter() {
//		MessageFilter mf = (MessageFilter) mapFilters.get(MESSAGE_WORKLIST);
//		if (mf == null) {
//			mf = new MessageFilter();
//			mapFilters.put(MESSAGE_WORKLIST, mf);
//		}
//		return mf;
//	}
//
//	/**
//	 * ������Ϣ���ı�־���������ϢFilter
//	 * 
//	 * @param name �����name(Table name) ����Ǳ��ඨ��ļ�������
//	 * @author huangzg
//	 */
//	public MessageFilter getFilterByName(String name) {
//		MessageFilter mf = (MessageFilter) mapFilters.get(name);
//		if (mf == null) {
//			mf = new MessageFilter();
//			mapFilters.put(name, mf);
//		}
//		return mf;
//	}
//
//	/**
//	 * ������Ϣ���ı�־�����������
//	 * 
//	 * @param name �����name(Table name) ����Ǳ��ඨ��ļ�������
//	 * @author huangzg
//	 */
//	public TableStyleSetting getTableSettingByName(String name) {
//		TableStyleSetting tss = (TableStyleSetting) mapTableSettings.get(name);
//		if (tss == null) {
//			tss = new TableStyleSetting();
//			mapTableSettings.put(name, tss);
//		}
//		return tss;
//	}
//
//	public void setFilterByName(String name, MessageFilter filter) {
//		mapFilters.put(name, filter);
//	}
//
//	/**
//	 * �������û�����ı����,��Ϊˢ��
//	 * 
//	 * @param name
//	 * @param style
//	 */
//
//	public void setTableSettingByName(String name, TableStyleSetting style) {
//		mapTableSettings.put(name, style);
//	}
//
//	public double[] getAreaScales() {
//		return areaScales;
//	}
//
//	public void setAreaScales(double[] areaScales) {
//		this.areaScales = areaScales;
//	}

}
