package uap.workflow.ui.taskhandling;

import java.io.Serializable;

/**
 * 消息面板的一些配置信息
 * <li>三个栏的筛选条件
 * <li>三个栏的格式信息
 * <li>刷新设置
 * 
 * @author leijun 2006-5-23
 * @modifier leijun 2008-12 增加消息片二叉树根节点
 * @modifier leijun 2009-4  6.0合并为一栏
 */
public class MessagePanelOptions implements Serializable {

//	/**
//	 * XXX:该对象需要缓存到本地客户端，所以需要UID
//	 */
//	private static final long serialVersionUID = -2222L;
//
//	/**
//	 * 消息中心的三栏：待办
//	 * <li>XXX:6.0合并为一栏 
//	 */
//	//public static final String MESSAGE_BULLETIN = "bulletin";
//	//public static final String MESSAGE_PA = "prealert";
//	public static final String MESSAGE_WORKLIST = "worklist";
//
//	/**
//	 * 刷新设置
//	 */
//	private RefreshSetting refreshSetting = null;
//
//	/**
//	 * 表格样式设定
//	 */
//	private HashMap mapTableSettings = null;
//
//	/**
//	 * 筛选器
//	 */
//	private HashMap mapFilters = null;
//
//	/**
//	 * 当前消息面板中的消息片二叉树根节点（不能带有UserObject）
//	 */
//	private LetNode letInfo = null;
//
//	/**
//	 * 区域比例.允许小数
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
//	 * 返回待办事务 的表格设定信息
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
//	 * 返回待办事务 的消息筛选器
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
//	 * 根据消息栏的标志来获得其消息Filter
//	 * 
//	 * @param name 这里的name(Table name) 最好是本类定义的几个常量
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
//	 * 根据消息栏的标志来获得其表格风格
//	 * 
//	 * @param name 这里的name(Table name) 最好是本类定义的几个常量
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
//	 * 重新设置缓存里的表格风格,即为刷新
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
