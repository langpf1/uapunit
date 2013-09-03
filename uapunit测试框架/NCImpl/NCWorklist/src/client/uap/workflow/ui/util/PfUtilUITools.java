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
 * ����ƽ̨UI�˹�����
 *
 * @author ���ھ� 2002-4-16
 * @modifier leijun 2008-3 ����һЩ���߷���
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
	 * ����������ѯ�е�λ������
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
	 * ����ִ��ǰ��ʾ 
	 * @param acp UI�˶��������������
	 * @return
	 * @throws Exception
	 */
	public static boolean beforeAction(ActionClientParams acp) {

		String strBilltype = acp.getBillType();
		Container uiContainer = acp.getUiContainer();
		String strActionCode = acp.getActionCode();
		//String uiBeforeClz = acp.getUiBeforeClz();

		//1.ִ��ǰ��ʾ
		Logger.debug("*����ִ��ǰ��ʾ");
		boolean isContinue = hintBeforeAction(uiContainer, strActionCode, strBilltype);
		if (!isContinue) {
			Logger.debug("*������ִ��ǰ��ʾ");
			return false;
		}

		//2.����ִ��ǰ����
		//Logger.debug("*����ִ��ǰ����");

		return true;
	}

	/**
	 * ����ִ��ǰ��ʾ
	 * @param uiContainer
	 * @param actionName
	 * @param billType
	 * @return ��ʾ���Ƿ�������� 
	 */
	private static boolean hintBeforeAction(Container uiContainer, String actionName, String billType) {
		Logger.debug("�ж��Ƿ���ж���ǰ��ʾ");
		String hintString = null;
		String actionType = null;
		if (actionName.length() > 20)
			actionType = actionName.substring(0, actionName.length() - 20);
		String hintRes = "Dshowhint" + billType + actionType;
		hintString = NCLangRes.getInstance().getStrByID("pub_billaction", hintRes);

		// �����ȡ������Դ,�򲻽��ж�����ʾ
		hintString = ((hintString == null) ? "" : hintString.trim());
		if (!(hintString.equals("") || hintString.equals(hintRes))) {
			if (MessageDialog.showYesNoDlg(
					uiContainer,
					NCLangRes.getInstance().getStrByID("pfworkflow1",
							"PfUtilUITools-000000")/* ����ִ��ǰ��ʾ */, hintString) != UIDialog.ID_YES)
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
	 * ��õ�ǰ��¼������Դ
	 * @return
	 */
	public static String getLoginDs() {
		return InvocationInfoProxy.getInstance().getUserDataSource();
	}

	/**
	 * ��õ�ǰ��¼���û�PK
	 * @return
	 */
	public static String getLoginUser() {
		return InvocationInfoProxy.getInstance().getUserId();
	}

	/**
	 * ����ͼչʾ�ĸ��Ի���
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
	 * ����ǰ̨VO���� <li>��֧�ֵַ�
	 * 
	 * @param srcBillOrTranstype Դ���ݻ�������
	 * @param destBillOrTranstype Ŀ�ĵ��ݻ�������
	 * @param srcBillVO Դ���ݾۺ�VO
	 * @return Ŀ�ĵ��ݾۺ�VO
	 * @throws BusinessException
	 * @deprecated
	 */
	public static AggregatedValueObject runChangeData(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject srcBillVO) throws BusinessException {
		return getExchangeService().runChangeData(srcBillOrTranstype, destBillOrTranstype, srcBillVO, null);
	}

	/**
	 * ����ǰ̨VO���齻�� <li>֧�ֵַ�
	 * 
	 * @param sourceBillType Դ��������PK
	 * @param destBillType Ŀ�ĵ�������PK
	 * @param sourceBillVOs Դ���ݾۺ�VO����
	 * @return Ŀ�ĵ��ݾۺ�VO����
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
	 * ���ݵ������͡�����ID����Ϣ ��ѯ���Ӧ�Ľڵ�ţ��㷨��
	 * <li>1.���Ҳ����bd_billtype.def3�ֶ�ע��������ʵ���½ӿڣ�
	 * <li>2.����޲�����򷵻�null
	 * <li>3.������û��ʵ�ֽӿڣ��򷵻�null
	 * <li>4.ʹ�ò�����صĽڵ��
	 * 
	 * @param pkBilltype ��������PK
	 * @param lqd �������ݣ����������Ҫ��
	 */
	public static String findCustomNodeOfBilltype(BilltypeVO btVO, ILinkQueryData lqd) {
		Logger.debug("::�����Զ���ڵ� findCustomNodeOfBilltype=" + btVO.getPk_billtypecode());

		//WARN::bd_billtype.def3�ֶ�ע��������ʵ���½ӿ�
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
				Logger.debug("::bd_billtype.def3ʵ���˽ӿ�IUINodecodeSearcher");
				return ((IUINodecodeSearcher) obj).findNodecode(lqd);
			} else
				return null;
		}

	}

	/**
	 * ���ݵ������Ͳ�ѯ���Ӧ�Ľڵ�ţ��㷨��
	 * <li>1.���Ҳ����bd_billtype.def3�ֶ�ע��������ʵ���½ӿڣ�
	 * <li>2.����޲������ʹ��bd_billtype.nodecode
	 * <li>3.������û��ʵ�ֽӿڣ���ʹ��bd_billtype.nodecode
	 * <li>4.ʹ�ò�����صĽڵ��
	 * 
	 * @param pkBilltype ��������PK
	 * @param lqd �������ݣ����������Ҫ��
	 */
	public static String findNodecodeOfBilltype(String pkBilltype, ILinkQueryData lqd) {
		BilltypeVO btVO = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(pkBilltype).buildPkGroup(getLoginGroup()));
		if (btVO == null)
			return null;

		//WARN::bd_billtype.def3�ֶ�ע��������ʵ���½ӿ�
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
	 * ����ĳ���������͵�nodecode���ԣ�������ǰ̨�Ļ���
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

		// ����ƽ̨�ͻ��˻���
		CacheProxy.fireDataUpdated(PfDataCache.STR_BILLTYPEINFO, null);
	}

	/**
	 * ���ݽ�ɫPK��ý�ɫ���룬����
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
	 * ������֯PK�����֯ID,����,��ǰ̨��飬����Զ��������
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
	 * ����PK�õ������֯
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
	 * ���vals���Ƿ����ظ����
	 * �������ڣ�(01-7-20 9:58:16)
	 * @return false-���ظ�
	 * @param vals org.omg.CORBA.Object
	 */
	public static boolean checkDuplicate(Object[] vals) {
		//Ϊ�գ���Ϊ���ظ�
		if (vals == null)
			return false;
		//��null,��Ϊ�ظ�
		for (int i = 0; i < vals.length; i++) {
			if (vals[i] == null)
				return true;
		}

		java.util.Hashtable ht = new java.util.Hashtable();
		for (int i = 0; i < vals.length; i++) {
			ht.put(vals[i], Integer.valueOf(i));
		}
		if (ht.size() == vals.length)
			return false; //���ظ�
		return true;//���ظ�
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
	 * ҵ���������
	 */
	public static UIPanel getTopBusiPanel(BillbusinessVO selectedBillbusiVO) {
		// �������
		UIPanel pnl = new UIPanel();
		UIPanel pnlLeft = new UIPanel();

		pnl.setLayout(new BorderLayout());
		pnlLeft.setLayout(new BorderLayout());

		UILabel lblBusiness = new UILabel();
		lblBusiness.setILabelType(4);

		pnlLeft.setPreferredSize(new Dimension(306, 27));

		//��VO��ȡֵ
		if (selectedBillbusiVO != null) {
			lblBusiness.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*ҵ������:*/
					+ selectedBillbusiVO.getBusitypename());
		} else {
			lblBusiness
					.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*ҵ������:*/);
		}

		//���Label
		pnl.add(pnlLeft, "Center");

		UIPanel pnBlank = new UIPanel();
		pnBlank.setPreferredSize(new Dimension(20, 55));
		//	pnlLeft.add(pnBlank,"West");
		pnlLeft.add(lblBusiness, "Center");

		return pnl;
	}

	/**
	 * ���ݵ���-ҵ��VO������һ��Panel���
	 */
	public static UIPanel getTopPanel(BillbusinessVO billbusiVO) {
		// �������
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

		//��VO��ȡֵ
		if (billbusiVO != null) {
			lblBill.setText(NCLangRes.getInstance().getStrByID("101202", "UPP101202-000036")/*��������:*/
					+ PfMultiLangUtil.getMultiBilltypeName(billbusiVO.getPk_billtype()));
			lblBusiness.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*ҵ������:*/
					+ billbusiVO.getBusitypename());
			lblTranstype.setText(NCLangRes.getInstance().getStrByID("101202", "UPP101202-000142")+": "
					+ (StringUtil.isEmptyWithTrim(billbusiVO.getTranstypename()) ? "" : PfMultiLangUtil.getMultiBilltypeName(billbusiVO.getTranstype())));
		} else {
			lblBill.setText(NCLangRes.getInstance().getStrByID("101202", "UPP101202-000036")/*��������:*/);
			lblBusiness
					.setText("   " + NCLangRes.getInstance().getStrByID("101202", "UPP101202-000037")/*ҵ������:*/);
		}

		//���Label
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
	 * ���ĳ���ż��Ŷ����ҵ�����͡� 
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
	 * ���ƽ̨ע������бȽϲ�����
	 */
	public static nc.vo.pub.pfflow.SignVO[] findAllSigns() throws Exception {
		Collection co = NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveAll(SignVO.class);
		return (SignVO[]) co.toArray(new SignVO[] {});
	}

	/**
	 * ��ѯĳ��˾Ϊĳҵ�����ͽ��е���������
	 * <li>ͬʱ��ȡ�˵������͵�i18n����
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
	 * ��ȡ���������е������ͻ������͵�����
	 * <li>������ǰ̨�������ͻ���
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
	 * ��õ�ǰҵ���������õ����е���VO
	 * <li>ͬʱ��ȡ�˵������͵�i18n����
	 * 
	 * @param pk_busitype java.lang.String
	 */
	public static BillbusinessVO[] findAllBillbusinessVOs(String pk_busitype) {
		return findAllBillbusinessVOs(pk_busitype, null);
	}

	/**
	 * ���������� ĳ���ݵ������������ݣ����ε��ݣ�
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
	 * ������е�ҵ�����͡�
	 * �������ڣ�(01-7-18 10:46:29)
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
	 * ��ѯ������ ĳ���ݻ������͵�������Դ����
	 * @param billbusiVO
	 * @return
	 */
	public static BillbusinessVO[] findAllSource(BillbusinessVO billbusiVO) {
		BillsourceVO[] arySourceVOs = null;

		//��ѯĳ�����У�ĳ���ݻ������͵���Դ��ϵ
		BillsourceVO condVO = new BillsourceVO();
		condVO.setPk_businesstype(billbusiVO.getPk_businesstype());
		String billOrTranstype = StringUtil.isEmptyWithTrim(billbusiVO.getTranstype()) ? billbusiVO
				.getPk_billtype() : billbusiVO.getTranstype(); //���ݻ�������
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