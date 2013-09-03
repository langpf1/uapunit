package uap.workflow.ui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.bs.bd.cache.CacheProxy;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.bs.pf.pub.PfDataCache;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.itf.org.IOrgMetaDataIDConst;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.pf.IPFTemplate;
import nc.itf.uap.pf.IPfExchangeService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.org.cache.IOrgUnitPubService_C;
import nc.sfbase.client.ClientToolKit;
import nc.ui.dbcache.DBCacheQueryFacade;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.multilang.PfMultiLangUtil;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.ActionClientParams;
import nc.ui.pub.pf.IUINodecodeSearcher;
import nc.ui.wfengine.sheet.swing.BaseDialog;
import nc.vo.bd.accessor.IBDData;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.GroupVO;
import nc.vo.org.OrgVO;
import nc.vo.pf.pub.BasedocVO;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pf.pub.util.ArrayUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pfflow.SignVO;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.pfflow03.BillsourceVO;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.uap.pf.TemplateParaVO;
import nc.vo.uap.rbac.role.RoleVO;

/**
 * 流程平台UI端工具类
 *
 * @author 樊冠军 2002-4-16
 * @modifier leijun 2008-3 增加一些工具方法
 */
public class PfUtilUITools  {

	private static IPfExchangeService exchangeService;

	public static UIRefPane getUIRefPane(BasedocVO vo) {
		UIRefPane ref = new UIRefPane();
		if (vo.getRefNodeName() != null) {
			ref.setRefNodeName(vo.getRefNodeName());
		}
		return ref;
	}

	/**
	 * 根据列名查询列的位置索引
	 * @param identifier
	 * @param tcm
	 * @return
	 */

	public static int getColumnIndex(Object identifier, TableColumnModel tcm) {
		Enumeration enumeration = tcm.getColumns();
		TableColumn aColumn;
		int index = 0;
		boolean isFound = false;

		while (enumeration.hasMoreElements()) {
			aColumn = (TableColumn) enumeration.nextElement();
			if (identifier.equals(aColumn.getIdentifier())) {
				isFound = true;
				break;
			}
			index++;
		}
		if (isFound)
			return index;
		else
			return -1;
	}

	/**
	 * 动作执行前提示 
	 * @param acp UI端动作处理的上下文
	 * @return
	 * @throws Exception
	 */
	public static boolean beforeAction(ActionClientParams acp) {

		String strBilltype = acp.getBillType();
		Container uiContainer = acp.getUiContainer();
		String strActionCode = acp.getActionCode();
		//String uiBeforeClz = acp.getUiBeforeClz();

		//1.执行前提示
		Logger.debug("*动作执行前提示");
		boolean isContinue = hintBeforeAction(uiContainer, strActionCode, strBilltype);
		if (!isContinue) {
			Logger.debug("*不进行执行前提示");
			return false;
		}

		//2.动作执行前处理
		//Logger.debug("*动作执行前处理");

		return true;
	}

	/**
	 * 动作执行前提示
	 * @param uiContainer
	 * @param actionName
	 * @param billType
	 * @return 提示后是否继续动作 
	 */
	private static boolean hintBeforeAction(Container uiContainer, String actionName, String billType) {
		Logger.debug("判断是否进行动作前提示");
		String hintString = null;
		String actionType = null;
		if (actionName.length() > 20)
			actionType = actionName.substring(0, actionName.length() - 20);
		String hintRes = "Dshowhint" + billType + actionType;
		hintString = NCLangRes.getInstance().getStrByID("pub_billaction", hintRes);

		// 如果获取不到资源,则不进行动作提示
		hintString = ((hintString == null) ? "" : hintString.trim());
		if (!(hintString.equals("") || hintString.equals(hintRes))) {
			if (MessageDialog.showYesNoDlg(
					uiContainer,
					NCLangRes.getInstance().getStrByID("pfworkflow1",
							"PfUtilUITools-000000")/* 动作执行前提示 */, hintString) != UIDialog.ID_YES)
				return false;
		}
		return true;
	}

	public static String getLoginGroup() {
		GroupVO gVO = WorkbenchEnvironment.getInstance().getGroupVO();
		String pkGroup = gVO == null ? null : gVO.getPk_group();
		return pkGroup;
	}

	/**
	 * 获得当前登录的数据源
	 * @return
	 */
	public static String getLoginDs() {
		return InvocationInfoProxy.getInstance().getUserDataSource();
	}

	/**
	 * 获得当前登录的用户PK
	 * @return
	 */
	public static String getLoginUser() {
		return InvocationInfoProxy.getInstance().getUserId();
	}

	/**
	 * 流程图展示的父对话框
	 * @param parent
	 * @param title
	 * @param centerComp
	 */
	public static boolean showDialog(Component parent, String title, Component centerComp) {
		BaseDialog dialog = null;
		Window window = SwingUtilities.windowForComponent(parent);
		if (window instanceof Frame) {
			dialog = new BaseDialog((Frame) window, title, true);
		} else {
			dialog = new BaseDialog((Dialog) window, title, true);
		}

		dialog.setDialogMode(BaseDialog.CLOSE_DIALOG);
		dialog.getBanner().setVisible(false);

		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(centerComp, BorderLayout.CENTER);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		if (dialog.ask()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 进行前台VO交换 <li>不支持分单
	 * 
	 * @param srcBillOrTranstype 源单据或交易类型
	 * @param destBillOrTranstype 目的单据或交易类型
	 * @param srcBillVO 源单据聚合VO
	 * @return 目的单据聚合VO
	 * @throws BusinessException
	 * @deprecated
	 */
	public static AggregatedValueObject runChangeData(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject srcBillVO) throws BusinessException {
		return getExchangeService().runChangeData(srcBillOrTranstype, destBillOrTranstype, srcBillVO, null);
	}

	/**
	 * 运行前台VO数组交换 <li>支持分单
	 * 
	 * @param sourceBillType 源单据类型PK
	 * @param destBillType 目的单据类型PK
	 * @param sourceBillVOs 源单据聚合VO数组
	 * @return 目的单据聚合VO数组
	 * @throws BusinessException
	 * @deprecated
	 */
	public static AggregatedValueObject[] runChangeDataAry(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs) throws BusinessException {
		return getExchangeService().runChangeDataAry(srcBillOrTranstype, destBillOrTranstype, sourceBillVOs, null);
	}
	
	private static IPfExchangeService getExchangeService() {
		if(exchangeService==null)
			exchangeService = NCLocator.getInstance().lookup(IPfExchangeService.class);
		return exchangeService;
	}

	/**
	 * 根据单据类型、单据ID等信息 查询其对应的节点号，算法：
	 * <li>1.查找插件（bd_billtype.def3字段注册的类必须实现下接口）
	 * <li>2.如果无插件，则返回null
	 * <li>3.如果插件没有实现接口，则返回null
	 * <li>4.使用插件返回的节点号
	 * 
	 * @param pkBilltype 单据类型PK
	 * @param lqd 联接数据，搜索插件需要的
	 */
	public static String findCustomNodeOfBilltype(BilltypeVO btVO, ILinkQueryData lqd) {
		Logger.debug("::查找自定义节点 findCustomNodeOfBilltype=" + btVO.getPk_billtypecode());

		//WARN::bd_billtype.def3字段注册的类必须实现下接口
		String strClassName = btVO.getDef3();
		Logger.debug("::bd_billtype.def3=" + strClassName);
		if (StringUtil.isEmptyWithTrim(strClassName)) {
			return null;
		} else {
			Object obj = null;
			try {
				Class c = Class.forName(strClassName);
				obj = c.newInstance();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
			if (obj instanceof IUINodecodeSearcher) {
				Logger.debug("::bd_billtype.def3实现了接口IUINodecodeSearcher");
				return ((IUINodecodeSearcher) obj).findNodecode(lqd);
			} else
				return null;
		}

	}

	/**
	 * 根据单据类型查询其对应的节点号，算法：
	 * <li>1.查找插件（bd_billtype.def3字段注册的类必须实现下接口）
	 * <li>2.如果无插件，则使用bd_billtype.nodecode
	 * <li>3.如果插件没有实现接口，则使用bd_billtype.nodecode
	 * <li>4.使用插件返回的节点号
	 * 
	 * @param pkBilltype 单据类型PK
	 * @param lqd 联接数据，搜索插件需要的
	 */
	public static String findNodecodeOfBilltype(String pkBilltype, ILinkQueryData lqd) {
		BilltypeVO btVO = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(pkBilltype).buildPkGroup(getLoginGroup()));
		if (btVO == null)
			return null;

		//WARN::bd_billtype.def3字段注册的类必须实现下接口
		String strClassName = btVO.getDef3();
		if (strClassName == null) {
			return btVO.getNodecode();

		} else {
			Object obj = null;
			try {
				Class c = Class.forName(strClassName);
				obj = c.newInstance();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
			if (obj instanceof IUINodecodeSearcher) {
				return ((IUINodecodeSearcher) obj).findNodecode(lqd);
			} else
				return btVO.getNodecode();
		}
	}

	/**
	 * 更新某个单据类型的nodecode属性，并更新前台的缓存
	 * @param pkBilltype
	 * @param newNodecode
	 * @throws BusinessException
	 */
	public static void updateNodecode(String pkBilltype, String newNodecode) throws BusinessException {
		BilltypeVO btVO = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(pkBilltype).buildPkGroup(getLoginGroup()));
		if (btVO == null)
			return;

		btVO.setNodecode(newNodecode);
		//NCLocator.getInstance().lookup(IPFMetaModel.class).updateBilltypeVO(btVO);
		NCLocator.getInstance().lookup(IVOPersistence.class).updateVO(btVO);

		// 更新平台客户端缓存
		CacheProxy.fireDataUpdated(PfDataCache.STR_BILLTYPEINFO, null);
	}

	/**
	 * 根据角色PK获得角色代码，名称
	 * @param pkOrg
	 * @return
	 * @throws BusinessException
	 */
	public static String[] getRoleInfoByPK(String pkOrg) {
		RoleVO roleVO = null;
		try {
			roleVO = (RoleVO) ((IUAPQueryBS) (NCLocator.getInstance().lookup(IUAPQueryBS.class)))
					.retrieveByPK(RoleVO.class, pkOrg);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		if (roleVO == null) { return new String[] { "", "" }; }
		return new String[] { roleVO.getRole_code(), roleVO.getRole_name() };
	}

	/**
	 * 根据组织PK获得组织ID,名称,从前台表查，减少远程连接数
	 * @param pkOrg
	 * @return
	 * @throws BusinessException
	 */
	public static String[] getOrgInfoByPK(String pkOrg) {
		if(StringUtil.isEmptyWithTrim(pkOrg)) {
			return new String[] {"", ""};
		}
		String sql = "select code, name from org_orgs where pk_org=?";
		SQLParameter param = new SQLParameter();
		param.addParam(pkOrg);
		List res = (List) DBCacheQueryFacade.runQueryByPk(sql, param, new ArrayListProcessor());
		if (res != null && res.size() != 0) {
			Object[] arrs = (Object[]) res.get(0);
			return new String[] { (String) arrs[0], (String) arrs[1] };
		}
		return null;
	}
	
	/**
	 * 根据PK得到多个组织
	 */
	public static OrgVO[] getOrgsByPKs(String[] pkOrgs) {
		IOrgUnitPubService_C orgService = NCLocator.getInstance().lookup(IOrgUnitPubService_C.class);
		try {
			return orgService.getOrgs(pkOrgs, new String[] { OrgVO.PK_ORG, OrgVO.NAME, OrgVO.NAME2, OrgVO.NAME3, OrgVO.NAME4, OrgVO.NAME5, OrgVO.NAME6 });
		} catch (BusinessException e) {
			throw new BusinessRuntimeException(e.getMessage(), e);
		}
	}	

	/**
	 * 检查vals里是否有重复的项。
	 * 创建日期：(01-7-20 9:58:16)
	 * @return false-无重复
	 * @param vals org.omg.CORBA.Object
	 */
	public static boolean checkDuplicate(Object[] vals) {
		//为空，认为不重复
		if (vals == null)
			return false;
		//有null,认为重复
		for (int i = 0; i < vals.length; i++) {
			if (vals[i] == null)
				return true;
		}

		java.util.Hashtable ht = new java.util.Hashtable();
		for (int i = 0; i < vals.length; i++) {
			ht.put(vals[i], Integer.valueOf(i));
		}
		if (ht.size() == vals.length)
			return false; //有重复
		return true;//无重复
	}

	/**
	 * @param ufBool
	 * @return
	 */
	public static Boolean getBoolean(UFBoolean ufBool) {
		if (ufBool == null)
			return Boolean.FALSE;
		return Boolean.valueOf(ufBool.booleanValue());
	}

	/**
	 * 业务类型面板
	 */
	public static UIPanel getTopBusiPanel(BillbusinessVO selectedBillbusiVO) {
		// 定义组件
		UIPanel pnl = new UIPanel();
		UIPanel pnlLeft = new UIPanel();

		pnl.setLayout(new BorderLayout());
		pnlLeft.setLayout(new BorderLayout());

		UILabel lblBusiness = new UILabel();
		lblBusiness.setILabelType(4);

		pnlLeft.setPreferredSize(new Dimension(306, 27));

		//从VO中取值
		if (selectedBillbusiVO != null) {
			lblBusiness.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*业务类型:*/
					+ selectedBillbusiVO.getBusitypename());
		} else {
			lblBusiness
					.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*业务类型:*/);
		}

		//添加Label
		pnl.add(pnlLeft, "Center");

		UIPanel pnBlank = new UIPanel();
		pnBlank.setPreferredSize(new Dimension(20, 55));
		//	pnlLeft.add(pnBlank,"West");
		pnlLeft.add(lblBusiness, "Center");

		return pnl;
	}

	/**
	 * 根据单据-业务VO，构造一个Panel面板
	 */
	public static UIPanel getTopPanel(BillbusinessVO billbusiVO) {
		// 定义组件
		UIPanel pnl = new UIPanel();
		UIPanel pnlLeft = new UIPanel();
		UIPanel pnlRight = new UIPanel();
		pnl.setLayout(new BorderLayout());
		pnlLeft.setLayout(new BorderLayout());
		pnlRight.setLayout(new FlowLayout(FlowLayout.LEADING));
		UILabel lblBusiness = new UILabel();
		UILabel lblBill = new UILabel();
		UILabel lblTranstype = new UILabel(NCLangRes.getInstance().getStrByID("101202", "UPP101202-000142")+": ");
		//UPP101202-000037
		pnlLeft.setPreferredSize(new Dimension(306, 27));

		//从VO中取值
		if (billbusiVO != null) {
			lblBill.setText(NCLangRes.getInstance().getStrByID("101202", "UPP101202-000036")/*单据类型:*/
					+ PfMultiLangUtil.getMultiBilltypeName(billbusiVO.getPk_billtype()));
			lblBusiness.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*业务类型:*/
					+ billbusiVO.getBusitypename());
			lblTranstype.setText(NCLangRes.getInstance().getStrByID("101202", "UPP101202-000142")+": "
					+ (StringUtil.isEmptyWithTrim(billbusiVO.getTranstypename()) ? "" : PfMultiLangUtil.getMultiBilltypeName(billbusiVO.getTranstype())));
		} else {
			lblBill.setText(NCLangRes.getInstance().getStrByID("101202", "UPP101202-000036")/*单据类型:*/);
			lblBusiness
					.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*业务类型:*/);
		}

		//添加Label
		pnl.add(pnlLeft, "West");
		pnl.add(pnlRight, "Center");
		UIPanel pnBlank = new UIPanel();
		pnBlank.setPreferredSize(new Dimension(20, 55));
		pnlLeft.add(pnBlank, "West");
		pnlLeft.add(lblBusiness, "Center");

		pnlRight.add(lblBill);
		pnlRight.add(lblTranstype);

		return pnl;
	}

	/**
	 * 获得某集团集团定义的业务类型。 
	 * @param pk_group
	 * @return
	 */
	public static BusitypeVO[] findAllBusitype(String pk_group) {
		try {
			IUAPQueryBS name = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			BusitypeVO condVO = new BusitypeVO();
			condVO.setPk_group(pk_group);
			Collection co = name.retrieve(condVO, false);
			return (BusitypeVO[]) co.toArray(new BusitypeVO[co.size()]);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获得平台注册的所有比较操作符
	 */
	public static nc.vo.pub.pfflow.SignVO[] findAllSigns() throws Exception {
		Collection co = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveAll(SignVO.class);
		return (SignVO[]) co.toArray(new SignVO[] {});
	}

	/**
	 * 查询某公司为某业务类型进行的流程配置
	 * <li>同时获取了单据类型的i18n名称
	 * 
	 * @param pk_busitype
	 * @param pk_corpid
	 * @return
	 */
	public static BillbusinessVO[] findAllBillbusinessVOs(String pk_busitype, String pk_corpid) {
		BillbusinessVO[] billbusiVOs = null;
		try {
			billbusiVOs = NCLocator.getInstance().lookup(IPFConfig.class).findBillbusinessVOs(
					pk_busitype, pk_corpid);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		return billbusiVOs;
	}

	/**
	 * 获取流程配置中单据类型或交易类型的名称
	 * <li>利用了前台单据类型缓存
	 * @param billbusiVOs
	 */
	private static void fetchNames(BillbusinessVO[] billbusiVOs) {
		for (BillbusinessVO billbusiVO : billbusiVOs) {
			String billType = billbusiVO.getPk_billtype();
			String transType = billbusiVO.getTranstype();
			//String bizType = billbusiVO.getDestbiztype();
			BilltypeVO btVO = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(billType).buildPkGroup(getLoginGroup()));
			billbusiVO.setBilltypename(Pfi18nTools.i18nBilltypeNameByVO(billType, btVO));
			if (!StringUtil.isEmptyWithTrim(transType)) {
				BilltypeVO transtypeVO = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(transType).buildPkGroup(getLoginGroup()));
				billbusiVO.setTranstypename(Pfi18nTools.i18nBilltypeNameByVO(transType,
						transtypeVO));
			}

		}
	}

	/**
	 * 获得当前业务类型配置的所有单据VO
	 * <li>同时获取了单据类型的i18n名称
	 * 
	 * @param pk_busitype java.lang.String
	 */
	public static BillbusinessVO[] findAllBillbusinessVOs(String pk_busitype) {
		return findAllBillbusinessVOs(pk_busitype, null);
	}

	/**
	 * 查找流程中 某单据的所有驱动单据（下游单据）
	 * @param billbusiVO
	 * @return
	 */
	public static BillbusinessVO[] findAllDrive(BillbusinessVO billbusiVO) {
		MessagedriveVO filtervo = new MessagedriveVO();
		MessagedriveVO[] vos = null;
		if (StringUtil.isEmptyWithTrim(billbusiVO.getTranstype()))
			filtervo.setPk_sourcebilltype(billbusiVO.getPk_billtype());
		else
			filtervo.setPk_sourcebilltype(billbusiVO.getTranstype());
		filtervo.setPk_sourcebusinesstype(billbusiVO.getPk_businesstype());
		filtervo.setPk_businesstype(billbusiVO.getPk_businesstype());
		//filtervo.setPk_corp(billVO.getPk_group());

		try {
			Collection co = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieve(filtervo, true);
			vos = (MessagedriveVO[]) co.toArray(new MessagedriveVO[co.size()]);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		if (vos == null || vos.length == 0)
			return null;

		BillbusinessVO[] rvo = new BillbusinessVO[vos.length];
		for (int i = 0; i < rvo.length; i++) {
			rvo[i] = new BillbusinessVO();
			rvo[i].setPk_billtype(vos[i].getPk_billtype());
			rvo[i].setPk_businesstype(billbusiVO.getPk_businesstype());
			rvo[i].setPk_group(billbusiVO.getPk_group());
		}
		Hashtable htt = new Hashtable();
		for (int i = 0; i < rvo.length; i++)
			htt.put(rvo[i].getPk_billtype(), rvo[i]);
		BillbusinessVO[] okVos = new BillbusinessVO[htt.size()];

		Enumeration e = htt.elements();
		int n = 0;
		while (e.hasMoreElements()) {
			okVos[n++] = (BillbusinessVO) e.nextElement();
		}

		fetchNames(okVos);
		return okVos;
	}

	/**
	 * 获得所有的业务类型。
	 * 创建日期：(01-7-18 10:46:29)
	 */
	public static BusitypeVO[] findAllSaleAndBuyBusitype(String pkCorp) {
		BusitypeVO[] vos = null;
		try {
			IPFMetaModel metaModel = (IPFMetaModel) NCLocator.getInstance().lookup(
					IPFMetaModel.class.getName());
			//vos = nc.ui.pub.pfflow00.BusitypeBO_Client.querySaleAndBuy(pkCorp);
			vos = metaModel.querySaleAndBuyBusitypes(pkCorp);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return vos;
	}

	/**
	 * 查询流程中 某单据或交易类型的所有来源单据
	 * @param billbusiVO
	 * @return
	 */
	public static BillbusinessVO[] findAllSource(BillbusinessVO billbusiVO) {
		BillsourceVO[] arySourceVOs = null;

		//查询某流程中，某单据或交易类型的来源关系
		BillsourceVO condVO = new BillsourceVO();
		condVO.setPk_businesstype(billbusiVO.getPk_businesstype());
		String billOrTranstype = StringUtil.isEmptyWithTrim(billbusiVO.getTranstype()) ? billbusiVO
				.getPk_billtype() : billbusiVO.getTranstype(); //单据或交易类型
		condVO.setPk_billtype(billOrTranstype);
		try {
			Collection<BillsourceVO> coSourceVOs = NCLocator.getInstance().lookup(IUAPQueryBS.class)
					.retrieve(condVO, true);
			arySourceVOs = coSourceVOs.toArray(new BillsourceVO[0]);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		if (arySourceVOs == null || arySourceVOs.length == 0)
			return null;

		BillbusinessVO[] retVos = new BillbusinessVO[arySourceVOs.length];
		for (int i = 0; i < retVos.length; i++) {
			retVos[i] = new BillbusinessVO();
			retVos[i].setPk_billtype(arySourceVOs[i].getReferbilltype());
			retVos[i].setPk_businesstype(billbusiVO.getPk_businesstype());
			retVos[i].setPk_group(billbusiVO.getPk_group());
		}

		fetchNames(retVos);
		return retVos;
	}

	public static void showFileInWeb(String dsName, String fileName) throws Exception {
		StringBuilder sbUrl = new StringBuilder(ClientToolKit.getServerURL())
				.append("service/ShowAlertFileServlet?");
		sbUrl.append("dsName=").append(dsName);
		sbUrl.append("&fileName=").append(fileName);//(URLEncoder.encode(path, "GBK"));

		URL url = new URL(sbUrl.toString());
		ClientToolKit.showDocument(url, "_blank");
	}

	public static String getTemplateId(int templateStyle, String pk_group, String funNode,
			String operator, String nodeKey) throws BusinessException {

		TemplateParaVO tempVo = new TemplateParaVO();
		tempVo.setFunNode(funNode);
		tempVo.setPk_Corp(pk_group);
		tempVo.setPk_orgUnit(pk_group);
		tempVo.setNodeKey(nodeKey);
		tempVo.setOperator(operator);
		tempVo.setTemplateType(templateStyle);
		return NCLocator.getInstance().lookup(IPFTemplate.class).getTemplateId(tempVo);
	}
}