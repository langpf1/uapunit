package uap.workflow.ui.taskhandling;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.pub.app.message.PfLinkData;
import uap.workflow.pub.app.message.vo.MessageTypes;
import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.pub.app.message.vo.MessageinfoVO;
import uap.workflow.ui.util.PfUIDataCache;
import uap.workflow.ui.util.PfUtilUITools;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFConfig;
import nc.sfbase.client.ClientToolKit;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.FuncNodeStarter;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.table.VOTableModel;
import nc.ui.pub.filemanager.FileUtil;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.sm.power.FuncRegisterCacheAccessor;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.jcom.io.FileFilterFactoryAdapter;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.msg.SysMessageParam;
import nc.vo.sm.funcreg.FunRegisterConst;
import nc.vo.sm.funcreg.FuncRegisterVO;

/**
 * 消息中心工具类
 * 
 * @author leijun 2007-5-30
 */
public class MessagePanelUtils {

	/**************************************************
	 * 公告栏、预警信息 表格列的多语名称
	 **************************************************/
	public static final String[] getBulletinColNamesOfI18n() {
		return new String[] { MsgTableColumnInfo.PK.toString(), MsgTableColumnInfo.PRIORITY.toString(),
				MsgTableColumnInfo.STATUS.toString(), MsgTableColumnInfo.TITLE.toString(),
				MsgTableColumnInfo.SENDER.toString(), MsgTableColumnInfo.PUBLISHDATE.toString(),
				MsgTableColumnInfo.DEALDATE.toString() };
	}

	public static final String[] bulletinColumnFields = new String[] {
			MsgTableColumnInfo.PK.getTag(), MsgTableColumnInfo.PRIORITY.getTag(),
			MsgTableColumnInfo.STATUS.getTag(), MsgTableColumnInfo.TITLE.getTag(),
			MsgTableColumnInfo.SENDER.getTag(), MsgTableColumnInfo.PUBLISHDATE.getTag(),
			MsgTableColumnInfo.DEALDATE.getTag() };

	public static final int[] BULLETIN_DEFAULT_WIDTH = { 15, 60, 40, 250, 60, 110, 30 };

	/**************************************************
	 * 待办事务 表格列的多语名称
	 **************************************************/
	public static final String[] getMsgTableColNamesOfI18n() {
		return new String[] { MsgTableColumnInfo.PK.toString(), MsgTableColumnInfo.PRIORITY.toString(),
				MsgTableColumnInfo.STATUS.toString(), MsgTableColumnInfo.BILLTYPE.toString(),
				MsgTableColumnInfo.TITLE.toString(), MsgTableColumnInfo.SENDER.toString(),
				MsgTableColumnInfo.PUBLISHDATE.toString(), MsgTableColumnInfo.DEALDATE.toString(),
				MsgTableColumnInfo.MSGTYPE.toString() };
	}

	public static final String[] msgTableColField = new String[] { MsgTableColumnInfo.PK.getTag(),
			MsgTableColumnInfo.PRIORITY.getTag(), MsgTableColumnInfo.STATUS.getTag(),
			MsgTableColumnInfo.BILLTYPE.getTag(), MsgTableColumnInfo.TITLE.getTag(),
			MsgTableColumnInfo.SENDER.getTag(), MsgTableColumnInfo.PUBLISHDATE.getTag(),
			MsgTableColumnInfo.DEALDATE.getTag(), MsgTableColumnInfo.MSGTYPE.getTag() };

	public static final int[] WORKITEM_DEFAULT_WIDTH = { 15, 30, 40, 80, 300, 60, 80, 50, 50 };

	/**
	 * 打开单据维护UI
	 * @param parent
	 * @param msg
	 * @param tModel
	 * @return
	 */
	public static boolean openMaintainBillUI(Container parent, MessageVO msg, VOTableModel tModel) {
		String strBilltype = msg.getPk_billtype();
		String nodecode = null;
		if (strBilltype.startsWith(WorkflownoteVO.BIZ_NODE_PREFIX)) {
			//XXX:使用[F]前缀直接指定节点号
			nodecode = strBilltype.substring(WorkflownoteVO.BIZ_NODE_PREFIX.length());
		} else {
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK());
			pflink.setBillType(msg.getPk_billtype());
			pflink.setUserObject(IPFActionName.SAVE);
			pflink.setPkMessage(msg.getPrimaryKey());
			pflink.setWorkflowtype(msg.getWorkflowtype());
			BilltypeVO billtypeVO = PfUIDataCache.getBillType(new BillTypeCacheKey().buildBilltype(strBilltype).buildPkGroup(PfUtilUITools.getLoginGroup()));
			String customNode = PfUtilUITools.findCustomNodeOfBilltype(billtypeVO, pflink);
			Logger.debug("::查找单据类型注册的自定义节点 findCustomNodeOfBilltype=" + customNode);

			nodecode = customNode;
			if (StringUtil.isEmptyWithTrim(customNode))
				nodecode = billtypeVO.getNodecode();
		}

		Logger.debug("openMaintainBillUI nodecode=" + nodecode);
		// 回到制单节点，修单任务
		// 优先使用插件返回的节点号，如果没有，则使用bd_billtype.nodecode
		if (StringUtil.isEmptyWithTrim(nodecode)) {
			MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("101203",
					"UPP101203-000036")/* @res * "错误" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"noNodecode")/* "单据类型没有注册nodecode字段，请修改！ */);
			return false;
		} else {
			boolean canClose = SFClientUtil.closeFuncWindow(nodecode.trim());
			if (!canClose) {
				MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("_beans",
						"UPP_Beans-000053")/* 提示 */, NCLangRes.getInstance().getStrByID("pfworkflow",
						"NodeCodeOpened")/* "该功能节点已打开，请手工关闭后再处理该消息！ */);
				return false;
			}
			waitOpenLinkedUI(parent, msg, nodecode, true, tModel);
		}

		return true;
	}

	/**
	 * 打开单据审批UI
	 * @param parent
	 * @param msg
	 * @param tModel
	 * @return
	 */
	public static boolean openApproveBillUI(Container parent, MessageVO msg, VOTableModel tModel) {
		BilltypeVO billtypeVO = PfUIDataCache.getBillType( new BillTypeCacheKey().buildBilltype(msg.getPk_billtype()).buildPkGroup(PfUtilUITools.getLoginGroup()) );
		PfLinkData pflink = new PfLinkData();
		pflink.setBillID(msg.getBillPK());
		pflink.setBillType(msg.getPk_billtype());
		pflink.setUserObject(IPFActionName.APPROVE);
		pflink.setPkMessage(msg.getPrimaryKey());
		pflink.setWorkflowtype(msg.getWorkflowtype());
		String customNode = PfUtilUITools.findCustomNodeOfBilltype(billtypeVO, pflink);
		Logger.debug("::查找单据类型注册的节点 findCustomNodeOfBilltype=" + customNode);
		String nodecode = customNode;
		if (StringUtil.isEmptyWithTrim(customNode))
			nodecode = billtypeVO.getNodecode();

		String clsName = billtypeVO.getClassname();
		boolean bCheckPower = isNeedCheckPower(clsName);

		// 优先使用插件返回的节点号
		if (StringUtil.isEmptyWithTrim(customNode)) {
			if (StringUtil.isEmptyWithTrim(clsName)) {
				Logger.debug("openApproveBillUI nodecode=" + nodecode);
				if (StringUtil.isEmptyWithTrim(nodecode)) {
					MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("101203",
							"UPP101203-000036")/* 错误 */, NCLangRes.getInstance().getStrByID("pfworkflow",
							"noNodecodeAndClassName")/* 单据类型没有注册nodecode字段和classname字段，请修改！ */);
					return false;
				} else {
					// 使用nodecode
					waitOpenLinkedUI(parent, msg, nodecode, bCheckPower, tModel);
				}
			} else {
				Logger.debug("openApproveBillUI clsName=" + clsName);
				// 使用classname
				clsName = clsName.trim();
				if (clsName.startsWith("<Y>") || clsName.startsWith("<N>") || clsName.startsWith("<X>")) {
					clsName = clsName.substring(3);
				}
				waitOpenLinkedUI(parent, msg, clsName, bCheckPower, tModel);
			}
		} else {
			Logger.debug("openApproveBillUI customNode=" + customNode);
			waitOpenLinkedUI(parent, msg, customNode, bCheckPower, tModel);
		}
		return true;
	}

	/**
	 * 打开关联UI时，显示等待对话框
	 * @param parent 
	 * 
	 * @param msg
	 * @param funcode
	 * @param bCheckPower
	 * @param tModel
	 */
	private static void waitOpenLinkedUI(final Container parent, final MessageVO msg,
			final String funcode, boolean bCheckPower, final VOTableModel tModel) {
		// 校验节点权限
		if (bCheckPower || isMsgNeedCheckpower(msg))
			if (!checkFuncPower(funcode))
				return;

		// 检查是否为Web节点
		if (isWebNode(funcode))
			return;

		// leijun 2006-4-20 耗时操作，显示等待对话框
		final Runnable openRun = new Runnable() {
			public void run() {
				if (syncOpenLinkedUI(parent, msg, funcode) && tModel != null)
					tModel.removeVO(msg);
			}
		};
		Runnable waitRun = new Runnable() {
			public void run() {
				BannerDialog dialog = new BannerDialog(parent);
				dialog.setStartText(NCLangRes.getInstance()
						.getStrByID("pfworkflow", "UPPpfworkflow-000473")/* @res "正在打开单据UI，请稍等..." */);
				try {
					dialog.start();
					Thread.sleep(500); // 等待1秒

					SwingUtilities.invokeAndWait(openRun);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				} finally {
					dialog.end();
				}
			}
		};
		new Thread(waitRun).start();
	}

	/**
	 * 交验该功能节点是否为Web节点
	 * @param funCode 功能节点编码
	 * @return
	 */
	private static boolean isWebNode(String funCode) {
		if (!StringUtil.isEmptyWithTrim(funCode)) {
			FuncRegisterVO frVO = FuncRegisterCacheAccessor.getInstance().getFuncRegisterVOByFunCode(funCode);

			if (frVO == null) {
				Logger.error("错误：isWebNode()获取不到节点VO=" + funCode);
			} else if (FunRegisterConst.LFW_FUNC_NODE == frVO.getFun_property()) {
				MessageDialog.showErrorDlg(ClientToolKit.getApplet(), NCLangRes.getInstance().getStrByID(
						"sysframev5", "UPPsysframev5-000062")/* @res "错误" */, NCLangRes.getInstance()
						.getStrByID("pfworkflow", "UPPpfworkflow-000001")/*不可打开Web节点.节点号=*/
						+ funCode);
				return true;
			}
		} else {
			Logger.error("错误：打开节点funCode=" + funCode);
		}
		return false;
	}

	/**
	 * 判定某单据类型是否需要判定其关联节点的打开权限
	 * 
	 * @param clsName
	 * @return
	 * @author leijun 2007-1-11
	 */
	private static boolean isNeedCheckPower(String clsName) {
		if (StringUtil.isEmptyWithTrim(clsName)) {
			return true;
		} else {
			clsName = clsName.trim();
			if (clsName.startsWith("<X>"))
				return false;
		}
		return true;
	}

	private static boolean checkFuncPower(String funCode) {
		if (!StringUtil.isEmptyWithTrim(funCode)) {
			FuncRegisterVO frVO = SFClientUtil.findFRVOFromWorkbenchEnvironment(funCode);
			if (frVO == null) {
				MessageDialog.showErrorDlg(ClientToolKit.getApplet(), NCLangRes.getInstance().getStrByID(
						"sysframev5", "UPPsysframev5-000062")/* @res "错误" */, NCLangRes.getInstance()
						.getStrByID("sysframev5", "UPPsysframev5-000095")/* @res "没有打开此节点的权限.节点号=" */
						+ funCode);
				return false;
			}
		} else {
			Logger.error("错误：打开节点funCode=" + funCode);
			return false;
		}

		return true;
	}

	private static boolean isMsgNeedCheckpower(MessageVO msg) {
		if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW)
			return true;
		if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PUSH)
			return true;
		if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PULL)
			return true;

		return false;
	}

	/**
	 * 判断消息的扩展代码是否为 制单或业务
	 * @param actionTypeCode
	 * @return
	 */
	public static boolean isMakebillOrBiz(String actionTypeCode) {
		if (actionTypeCode == null)
			return false;
		if (actionTypeCode.startsWith(WorkflownoteVO.WORKITEM_TYPE_MAKEBILL)
				|| actionTypeCode.startsWith(WorkflownoteVO.WORKITEM_TYPE_BIZ))
			return true;
		return false;
	}

	/**
	 * 审批消息、业务流消息处理 打开节点
	 * <li>同步方式
	 * @param parent 
	 * 
	 * @param msg
	 * @param funcode
	 */
	private static boolean syncOpenLinkedUI(Container parent, MessageVO msg, String funcode) {
		boolean isOpenSucess = false;
		int openMode = msg.isForceDialogOpen() ? IFuncWindow.WINDOW_TYPE_DLG : SFClientUtil
				.getFuncnodeOpenMode();
		if (isMakebillOrBiz(msg.getActionTypeCode())) {
			// 打开修单UI
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK());
			pflink.setBillType(msg.getPk_billtype());
			pflink.setUserObject(msg.getUserobject()); //XXX:将扩展对象传入
			pflink.setPkMessage(msg.getPrimaryKey());
			pflink.setPkOrg(msg.getCorpPK());
			pflink.setWorkflowtype(msg.getWorkflowtype());
			int iLinktype = ILinkType.LINK_TYPE_MAINTAIN;
			if (msg.getMsgType() == MessageTypes.MSG_TYPE_INFO)
				iLinktype = ILinkType.LINK_TYPE_QUERY;

			// yanke1+ 2011-5-16 使用Dialog方式打开被驳回的单据（与审批时的单据打开方式进行统一）
//			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, iLinktype, openMode, true);
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, iLinktype, IFuncWindow.WINDOW_TYPE_DLG, true);
		} else if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW) {
			// 业务流消息处理，V51为上游消息
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK()); // 本单据ID
			pflink.setBillType(msg.getPk_billtype()); // 本单据类型
			pflink.setSourceBillType(msg.getPk_srcbilltype()); // 上游单据类型
			pflink.setPkOrg(msg.getCorpPK()); // 公司
			pflink.setPkMessage(msg.getPrimaryKey());
			//pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_QUERY,
					openMode, true);
		} else if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PUSH) {
			// 推式消息处理
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK()); // 本单据ID
			pflink.setBillType(msg.getPk_billtype()); // 本单据类型
			pflink.setSourceBillType(msg.getPk_srcbilltype()); // 上游单据类型
			pflink.setPkMessage(msg.getPrimaryKey());
			//pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_MAINTAIN,
					IFuncWindow.WINDOW_TYPE_DLG, true);
		} else if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PULL) {
			// 拉式消息处理
			PfLinkData pflink = new PfLinkData();
			// pflink.setBillID(msg.getBillPK()); // 上游单据ID
			pflink.setSourceBillID(msg.getBillPK()); // 上游单据ID
			pflink.setBillType(msg.getPk_billtype()); // 本单据类型
			pflink.setSourceBillType(msg.getPk_srcbilltype()); // 上游单据类型
			pflink.setPkMessage(msg.getPrimaryKey());
			//pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_ADD,
					IFuncWindow.WINDOW_TYPE_DLG, true);
		} else {
			// 审批处理
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK()); // 单据ID
			pflink.setBillType(msg.getPk_billtype()); // 单据类型
			pflink.setPkOrg(msg.getCorpPK()); // 制单公司
			pflink.setPkMessage(msg.getPrimaryKey());
			pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_APPROVE,
					IFuncWindow.WINDOW_TYPE_DLG, true);
		}
		return isOpenSucess;
	}

	/**
	 * 借鉴了SFClientUtil类
	 * @param parent 
	 * 
	 * @param funCode
	 * @param pflink
	 * @param bCheckPower 是否控制节点权限
	 * @param bSyncOpen 是否同步方式打开
	 */
	private static boolean openNodeUI(Container parent, String funCode, PfLinkData pflink,
			int iLinkType, int iOpenMode, boolean bSyncOpen) {
		boolean isOpenSucess = true;

		FuncRegisterVO frVO = SFClientUtil.findFRVOFromMenuTree(funCode);
		if (frVO == null)
			// 如果没有权限，且不检查权限，则从功能注册表中获取节点信息
			frVO = FuncRegisterCacheAccessor.getInstance().getFuncRegisterVOByFunCode(funCode);


		if (frVO != null) {
			switch (iOpenMode) {
				// 三种不同的打开模式，都以同步方式打开
				case IFuncWindow.WINDOW_TYPE_FRAME:
					FuncNodeStarter.openLinkedFrame(frVO, iLinkType, pflink, null, bSyncOpen, null);
					break;
				case IFuncWindow.WINDOW_TYPE_DLG:
					int height = ClientToolKit.getUserHeight()-40;
					int width = ClientToolKit.getUserWidth()-40;
					FuncletWindowLauncher.openFuncNodeDialog(parent, frVO,new FuncletInitData(iLinkType, pflink),null, true, bSyncOpen, new Dimension(width,height));
//					FuncNodeStarter.openLinkedDialog(frVO, iLinkType, pflink, parent, bSyncOpen, true, null);
					break;
				case IFuncWindow.WINDOW_TYPE_TAB:
					FuncNodeStarter.openLinkedTabbedPane(frVO, iLinkType, pflink, parent, bSyncOpen, null);
					break;
				default:
					break;
			}
		} else {
			isOpenSucess = false;
			Logger.error("错误：openNodeUI()获取不到节点VO=" + funCode);
		}

		return isOpenSucess;
	}

	/**
	 * 更新主界面上的未读消息条数
	 */
	public static void updateUncheckedCount() {
		int iCount = calculateCount(MessageRepository.getInstance().getAlBulletinMsgs())
				+ calculateCount(MessageRepository.getInstance().getAlWorkList())
				+ calculateCount(MessageRepository.getInstance().getAlPaMsgs());
		MessageCountLabel.getInstance().setCount(iCount);
	}

	private static int calculateCount(ArrayList<MessageVO> alBulletinMsgs) {
		int iSize = 0;
		for (MessageVO msgVO : alBulletinMsgs) {
			boolean ischeck = msgVO.isCheck().booleanValue();
			if (!ischeck)
				iSize++;
		}
		return iSize;
	}

	private static String defaultSeperator = "\\";

	private static float maxSize = 5f;

	/**
	 * 读取文件内容，返回文件压缩后的字节数组。
	 * @param localPath 完整的本地文件路径名
	 * @return 返回包含文件内容的压缩后的字节数组
	 * @throws BusinessException
	 */
	public static byte[] readFile(String localPath) throws BusinessException {
		//本地路径为空，则返回
		if (StringUtil.isEmptyWithTrim(localPath))
			return null;

		//校验本地路径是否有效:如果路径不存在，则抛出异常
		if (!FileUtil.isFileExisted(localPath))
			throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000672", null, new String[] { localPath })/*"本地不存在该文件：" + localPath*/);

		//校验文件大小
		File file = new File(localPath);
		if (!validateSize(file))
			throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000662")/*"附件大小超过限制!"*/);

		//获得新文件压缩后的字节数组
		ByteArrayOutputStream bout = null;
		GZIPOutputStream gzout = null;
		FileInputStream fin = null;
		try {
			bout = new ByteArrayOutputStream();
			gzout = new GZIPOutputStream(bout);
			fin = new FileInputStream(new File(localPath));
			byte[] buffer = new byte[1024];
			int num;
			//每次读1024个字节
			while ((num = fin.read(buffer)) != -1) {
				gzout.write(buffer, 0, num);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		} finally {
			//放到finally中确保被关闭
			closeOutFile(bout);
			closeOutFile(gzout);
			closeInFile(fin);
		}
		return bout.toByteArray();
	}

	/**
	 * 关闭输入文件流
	 * @param fin
	 */
	private static void closeInFile(InputStream fin) {
		try {
			if (fin != null)
				fin.close();
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 关闭输出文件流
	 * @param bout
	 */
	private static void closeOutFile(OutputStream bout) {
		try {
			if (bout != null)
				bout.close();
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 从文件完整路径中取得文件名
	 * @param localPath
	 * @return
	 */
	public static String getFileName(String localPath) {
		//解析欲上传的本地文件路径，取得文件名
		int index = localPath.lastIndexOf(defaultSeperator);
		if (index == -1)
			index = localPath.lastIndexOf("/");
		String fileName = localPath.substring(index + 1);
		return fileName;
	}

	/**
	 * 选择文件
	 * @param dlgType，文件选择框的类型
	 */
	public static String chooseFile(Component component, int dlgType) {
		UIFileChooser fileChooser = new UIFileChooser();
		if (dlgType == JFileChooser.OPEN_DIALOG) {
			//文件选择方式,只能选择文件
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//是否提供"所有文件"过滤
			fileChooser.setAcceptAllFileFilterUsed(true);
			//设置文件过滤器
			fileChooser.setFileFilter(FileFilterFactoryAdapter.suffixFileFilter(new String[] { ".doc",
					".txt", ".pdf", ".xls" }));
			fileChooser.setApproveButtonText(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000661")/*"上传附件"*/);
		} else if (dlgType == JFileChooser.SAVE_DIALOG) {
			//只能选择文件夹
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(FileFilterFactoryAdapter.directoryFileFilter());
			fileChooser.setApproveButtonText(NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000001")/*"保存"*/);
		}

		//是否支持多选
		fileChooser.setMultiSelectionEnabled(false);

		int value = fileChooser.showDialog(component, null);
		if (value != JFileChooser.APPROVE_OPTION)
			return null;
		return fileChooser.getSelectedFile().getAbsolutePath();
	}

	/**
	 * 校验文件大小
	 * 
	 * @param file
	 * @return：true表示文件大小在容许的范围内，否则返回false
	 */
	private static boolean validateSize(File file) {
		//取得容许的最大文件大小
		float maxSize = getMaxSize();
		//取得欲上传的文件大小
		long len = file.length();
		long fileMBs = len / (1024 * 1024);

		if (fileMBs < maxSize)
			return true;
		else
			return false;
	}

	/**
	 * 从配置文件中得文件最大限制
	 * @return：如果能取得，返回；否则返回默认值
	 */
	private static float getMaxSize() {
		IPFConfig pfc = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
		float configSize = 0f;
		try {
			SysMessageParam smp = pfc.getSysMsgParam();
			configSize = smp.getMsgFileMaxSize();
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		//如果配置文件中得不到，则取默认值
		if (Math.abs(configSize) < 1e-6)
			configSize = maxSize;
		return configSize;
	}

	/**
	 * 获取消息附件中的文件内容，并把附件内容写到指定文件中。
	 * @param pkMsg 主键
	 * @param fileName 附件名
	 * @param parent UI
	 * @throws BusinessException
	 */
	public static void writeFile(String pkMsg, String fileName, Container parent)
			throws BusinessException {
		//1.选择文件保存路径
		String localPath = chooseFile(parent, JFileChooser.SAVE_DIALOG);
		if (StringUtil.isEmptyWithTrim(localPath))
			return;

		String filePath = "";
		File dirFile = new File(localPath);
		//2.如果路径不存在，先创建
		if (!dirFile.exists()) {
			dirFile.mkdirs();
			filePath = convertFilePath(localPath, fileName);
		} else {
			//判断指定路径下是否存在同名文件
			filePath = convertFilePath(localPath, fileName);
			if (FileUtil.isFileExisted(filePath)) {
				if (MessageDialog.ID_YES != MessageDialog.showYesNoDlg(parent, NCLangRes.getInstance()
						.getStrByID("pfworkflow", "UPPpfworkflow-000227")/* @res "提示" */, NCLangRes
						.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000670")/*"文件已存在，是否覆盖？"*/))
					return;
			}
		}
		//3. 获得文件内容并写入本地
		byte[] fileBytes = getFileContent(pkMsg);
		writeBytesToFile(filePath, fileBytes);

		String strPath = filePath;
		if (strPath.indexOf("\\") >= 0)
			strPath = StringUtil.replaceAllString(strPath, "\\", "/");

		MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000227")/* @res "提示" */, NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000671", null, new String[] { strPath })/*"文件下载成功，路径为：" + filePath*/);
	}

	/**
	 * 获取消息附件中的文件内容，返回字节数组
	 * @param pkMsg
	 * @return
	 * @throws BusinessException
	 */
	private static byte[] getFileContent(String pkMsg) throws BusinessException {
		IUAPQueryBS uapQry = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		MessageinfoVO infoVO = (MessageinfoVO) uapQry.retrieveByPK(MessageinfoVO.class, pkMsg,
				new String[] { "filecontent" });
		return (byte[]) infoVO.getFilecontent();
	}

	/**
	 * 把附件内容写到指定文件中	 
	 * @param localPath: 本地绝对路径
	 * @param fileContent：文件内容
	 * @throws BusinessException
	 */
	private static void writeBytesToFile(String localPath, byte[] fileContent)
			throws BusinessException {
		GZIPInputStream gzin = null;
		BufferedOutputStream bufOut = null;
		try {
			gzin = new GZIPInputStream(new ByteArrayInputStream(fileContent));
			bufOut = new BufferedOutputStream(new FileOutputStream(localPath));
			byte[] buf = new byte[1024];
			int num;
			while ((num = gzin.read(buf)) != -1) {
				bufOut.write(buf, 0, num);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000673")/*"下载文件发生错误！"*/
					+ "\n" + e.getMessage());
		} finally {
			closeInFile(gzin);
			closeOutFile(bufOut);
		}
	}

	/**
	 * 根据路径和文件名组织成文件的完整路径名
	 * @param path
	 * @param fileName
	 * @return
	 */
	private static String convertFilePath(String path, String fileName) {
		String filePath = "";
		if (path.endsWith("/") || path.endsWith("\\"))
			filePath = path + fileName;
		else
			filePath = path + File.separator + fileName;
		return filePath;
	}

	public static boolean isApprove(String actionTypeCode) {
		if (actionTypeCode == null)
			return false;
		if (actionTypeCode.startsWith(WorkflownoteVO.WORKITEM_TYPE_APPROVE))
			return true;
		return false;
	}
	
	public static boolean isMakeBill(String actionTypeCode) {
		if (StringUtil.isEmptyWithTrim(actionTypeCode)) {
			return false;
		}
		
		boolean is = actionTypeCode.startsWith(WorkflownoteVO.WORKITEM_TYPE_MAKEBILL);
		return is;
	}
	
	public static boolean isBiz(String actionTypeCode) {
		if (StringUtil.isEmptyWithTrim(actionTypeCode)) {
			return false;
		}
		
		boolean is = actionTypeCode.startsWith(WorkflownoteVO.WORKITEM_TYPE_BIZ);
		return is;
	}

}
