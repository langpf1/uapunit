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
 * ��Ϣ���Ĺ�����
 * 
 * @author leijun 2007-5-30
 */
public class MessagePanelUtils {

	/**************************************************
	 * ��������Ԥ����Ϣ ����еĶ�������
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
	 * �������� ����еĶ�������
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
	 * �򿪵���ά��UI
	 * @param parent
	 * @param msg
	 * @param tModel
	 * @return
	 */
	public static boolean openMaintainBillUI(Container parent, MessageVO msg, VOTableModel tModel) {
		String strBilltype = msg.getPk_billtype();
		String nodecode = null;
		if (strBilltype.startsWith(WorkflownoteVO.BIZ_NODE_PREFIX)) {
			//XXX:ʹ��[F]ǰ׺ֱ��ָ���ڵ��
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
			Logger.debug("::���ҵ�������ע����Զ���ڵ� findCustomNodeOfBilltype=" + customNode);

			nodecode = customNode;
			if (StringUtil.isEmptyWithTrim(customNode))
				nodecode = billtypeVO.getNodecode();
		}

		Logger.debug("openMaintainBillUI nodecode=" + nodecode);
		// �ص��Ƶ��ڵ㣬�޵�����
		// ����ʹ�ò�����صĽڵ�ţ����û�У���ʹ��bd_billtype.nodecode
		if (StringUtil.isEmptyWithTrim(nodecode)) {
			MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("101203",
					"UPP101203-000036")/* @res * "����" */, NCLangRes.getInstance().getStrByID("pfworkflow",
					"noNodecode")/* "��������û��ע��nodecode�ֶΣ����޸ģ� */);
			return false;
		} else {
			boolean canClose = SFClientUtil.closeFuncWindow(nodecode.trim());
			if (!canClose) {
				MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("_beans",
						"UPP_Beans-000053")/* ��ʾ */, NCLangRes.getInstance().getStrByID("pfworkflow",
						"NodeCodeOpened")/* "�ù��ܽڵ��Ѵ򿪣����ֹ��رպ��ٴ������Ϣ�� */);
				return false;
			}
			waitOpenLinkedUI(parent, msg, nodecode, true, tModel);
		}

		return true;
	}

	/**
	 * �򿪵�������UI
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
		Logger.debug("::���ҵ�������ע��Ľڵ� findCustomNodeOfBilltype=" + customNode);
		String nodecode = customNode;
		if (StringUtil.isEmptyWithTrim(customNode))
			nodecode = billtypeVO.getNodecode();

		String clsName = billtypeVO.getClassname();
		boolean bCheckPower = isNeedCheckPower(clsName);

		// ����ʹ�ò�����صĽڵ��
		if (StringUtil.isEmptyWithTrim(customNode)) {
			if (StringUtil.isEmptyWithTrim(clsName)) {
				Logger.debug("openApproveBillUI nodecode=" + nodecode);
				if (StringUtil.isEmptyWithTrim(nodecode)) {
					MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("101203",
							"UPP101203-000036")/* ���� */, NCLangRes.getInstance().getStrByID("pfworkflow",
							"noNodecodeAndClassName")/* ��������û��ע��nodecode�ֶκ�classname�ֶΣ����޸ģ� */);
					return false;
				} else {
					// ʹ��nodecode
					waitOpenLinkedUI(parent, msg, nodecode, bCheckPower, tModel);
				}
			} else {
				Logger.debug("openApproveBillUI clsName=" + clsName);
				// ʹ��classname
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
	 * �򿪹���UIʱ����ʾ�ȴ��Ի���
	 * @param parent 
	 * 
	 * @param msg
	 * @param funcode
	 * @param bCheckPower
	 * @param tModel
	 */
	private static void waitOpenLinkedUI(final Container parent, final MessageVO msg,
			final String funcode, boolean bCheckPower, final VOTableModel tModel) {
		// У��ڵ�Ȩ��
		if (bCheckPower || isMsgNeedCheckpower(msg))
			if (!checkFuncPower(funcode))
				return;

		// ����Ƿ�ΪWeb�ڵ�
		if (isWebNode(funcode))
			return;

		// leijun 2006-4-20 ��ʱ��������ʾ�ȴ��Ի���
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
						.getStrByID("pfworkflow", "UPPpfworkflow-000473")/* @res "���ڴ򿪵���UI�����Ե�..." */);
				try {
					dialog.start();
					Thread.sleep(500); // �ȴ�1��

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
	 * ����ù��ܽڵ��Ƿ�ΪWeb�ڵ�
	 * @param funCode ���ܽڵ����
	 * @return
	 */
	private static boolean isWebNode(String funCode) {
		if (!StringUtil.isEmptyWithTrim(funCode)) {
			FuncRegisterVO frVO = FuncRegisterCacheAccessor.getInstance().getFuncRegisterVOByFunCode(funCode);

			if (frVO == null) {
				Logger.error("����isWebNode()��ȡ�����ڵ�VO=" + funCode);
			} else if (FunRegisterConst.LFW_FUNC_NODE == frVO.getFun_property()) {
				MessageDialog.showErrorDlg(ClientToolKit.getApplet(), NCLangRes.getInstance().getStrByID(
						"sysframev5", "UPPsysframev5-000062")/* @res "����" */, NCLangRes.getInstance()
						.getStrByID("pfworkflow", "UPPpfworkflow-000001")/*���ɴ�Web�ڵ�.�ڵ��=*/
						+ funCode);
				return true;
			}
		} else {
			Logger.error("���󣺴򿪽ڵ�funCode=" + funCode);
		}
		return false;
	}

	/**
	 * �ж�ĳ���������Ƿ���Ҫ�ж�������ڵ�Ĵ�Ȩ��
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
						"sysframev5", "UPPsysframev5-000062")/* @res "����" */, NCLangRes.getInstance()
						.getStrByID("sysframev5", "UPPsysframev5-000095")/* @res "û�д򿪴˽ڵ��Ȩ��.�ڵ��=" */
						+ funCode);
				return false;
			}
		} else {
			Logger.error("���󣺴򿪽ڵ�funCode=" + funCode);
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
	 * �ж���Ϣ����չ�����Ƿ�Ϊ �Ƶ���ҵ��
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
	 * ������Ϣ��ҵ������Ϣ���� �򿪽ڵ�
	 * <li>ͬ����ʽ
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
			// ���޵�UI
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK());
			pflink.setBillType(msg.getPk_billtype());
			pflink.setUserObject(msg.getUserobject()); //XXX:����չ������
			pflink.setPkMessage(msg.getPrimaryKey());
			pflink.setPkOrg(msg.getCorpPK());
			pflink.setWorkflowtype(msg.getWorkflowtype());
			int iLinktype = ILinkType.LINK_TYPE_MAINTAIN;
			if (msg.getMsgType() == MessageTypes.MSG_TYPE_INFO)
				iLinktype = ILinkType.LINK_TYPE_QUERY;

			// yanke1+ 2011-5-16 ʹ��Dialog��ʽ�򿪱����صĵ��ݣ�������ʱ�ĵ��ݴ򿪷�ʽ����ͳһ��
//			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, iLinktype, openMode, true);
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, iLinktype, IFuncWindow.WINDOW_TYPE_DLG, true);
		} else if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW) {
			// ҵ������Ϣ����V51Ϊ������Ϣ
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK()); // ������ID
			pflink.setBillType(msg.getPk_billtype()); // ����������
			pflink.setSourceBillType(msg.getPk_srcbilltype()); // ���ε�������
			pflink.setPkOrg(msg.getCorpPK()); // ��˾
			pflink.setPkMessage(msg.getPrimaryKey());
			//pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_QUERY,
					openMode, true);
		} else if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PUSH) {
			// ��ʽ��Ϣ����
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK()); // ������ID
			pflink.setBillType(msg.getPk_billtype()); // ����������
			pflink.setSourceBillType(msg.getPk_srcbilltype()); // ���ε�������
			pflink.setPkMessage(msg.getPrimaryKey());
			//pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_MAINTAIN,
					IFuncWindow.WINDOW_TYPE_DLG, true);
		} else if (msg.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PULL) {
			// ��ʽ��Ϣ����
			PfLinkData pflink = new PfLinkData();
			// pflink.setBillID(msg.getBillPK()); // ���ε���ID
			pflink.setSourceBillID(msg.getBillPK()); // ���ε���ID
			pflink.setBillType(msg.getPk_billtype()); // ����������
			pflink.setSourceBillType(msg.getPk_srcbilltype()); // ���ε�������
			pflink.setPkMessage(msg.getPrimaryKey());
			//pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_ADD,
					IFuncWindow.WINDOW_TYPE_DLG, true);
		} else {
			// ��������
			PfLinkData pflink = new PfLinkData();
			pflink.setBillID(msg.getBillPK()); // ����ID
			pflink.setBillType(msg.getPk_billtype()); // ��������
			pflink.setPkOrg(msg.getCorpPK()); // �Ƶ���˾
			pflink.setPkMessage(msg.getPrimaryKey());
			pflink.setWorkflowtype(msg.getWorkflowtype());
			isOpenSucess = openNodeUI(parent, funcode.trim(), pflink, ILinkType.LINK_TYPE_APPROVE,
					IFuncWindow.WINDOW_TYPE_DLG, true);
		}
		return isOpenSucess;
	}

	/**
	 * �����SFClientUtil��
	 * @param parent 
	 * 
	 * @param funCode
	 * @param pflink
	 * @param bCheckPower �Ƿ���ƽڵ�Ȩ��
	 * @param bSyncOpen �Ƿ�ͬ����ʽ��
	 */
	private static boolean openNodeUI(Container parent, String funCode, PfLinkData pflink,
			int iLinkType, int iOpenMode, boolean bSyncOpen) {
		boolean isOpenSucess = true;

		FuncRegisterVO frVO = SFClientUtil.findFRVOFromMenuTree(funCode);
		if (frVO == null)
			// ���û��Ȩ�ޣ��Ҳ����Ȩ�ޣ���ӹ���ע����л�ȡ�ڵ���Ϣ
			frVO = FuncRegisterCacheAccessor.getInstance().getFuncRegisterVOByFunCode(funCode);


		if (frVO != null) {
			switch (iOpenMode) {
				// ���ֲ�ͬ�Ĵ�ģʽ������ͬ����ʽ��
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
			Logger.error("����openNodeUI()��ȡ�����ڵ�VO=" + funCode);
		}

		return isOpenSucess;
	}

	/**
	 * �����������ϵ�δ����Ϣ����
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
	 * ��ȡ�ļ����ݣ������ļ�ѹ������ֽ����顣
	 * @param localPath �����ı����ļ�·����
	 * @return ���ذ����ļ����ݵ�ѹ������ֽ�����
	 * @throws BusinessException
	 */
	public static byte[] readFile(String localPath) throws BusinessException {
		//����·��Ϊ�գ��򷵻�
		if (StringUtil.isEmptyWithTrim(localPath))
			return null;

		//У�鱾��·���Ƿ���Ч:���·�������ڣ����׳��쳣
		if (!FileUtil.isFileExisted(localPath))
			throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000672", null, new String[] { localPath })/*"���ز����ڸ��ļ���" + localPath*/);

		//У���ļ���С
		File file = new File(localPath);
		if (!validateSize(file))
			throw new PFBusinessException(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000662")/*"������С��������!"*/);

		//������ļ�ѹ������ֽ�����
		ByteArrayOutputStream bout = null;
		GZIPOutputStream gzout = null;
		FileInputStream fin = null;
		try {
			bout = new ByteArrayOutputStream();
			gzout = new GZIPOutputStream(bout);
			fin = new FileInputStream(new File(localPath));
			byte[] buffer = new byte[1024];
			int num;
			//ÿ�ζ�1024���ֽ�
			while ((num = fin.read(buffer)) != -1) {
				gzout.write(buffer, 0, num);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		} finally {
			//�ŵ�finally��ȷ�����ر�
			closeOutFile(bout);
			closeOutFile(gzout);
			closeInFile(fin);
		}
		return bout.toByteArray();
	}

	/**
	 * �ر������ļ���
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
	 * �ر�����ļ���
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
	 * ���ļ�����·����ȡ���ļ���
	 * @param localPath
	 * @return
	 */
	public static String getFileName(String localPath) {
		//�������ϴ��ı����ļ�·����ȡ���ļ���
		int index = localPath.lastIndexOf(defaultSeperator);
		if (index == -1)
			index = localPath.lastIndexOf("/");
		String fileName = localPath.substring(index + 1);
		return fileName;
	}

	/**
	 * ѡ���ļ�
	 * @param dlgType���ļ�ѡ��������
	 */
	public static String chooseFile(Component component, int dlgType) {
		UIFileChooser fileChooser = new UIFileChooser();
		if (dlgType == JFileChooser.OPEN_DIALOG) {
			//�ļ�ѡ��ʽ,ֻ��ѡ���ļ�
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//�Ƿ��ṩ"�����ļ�"����
			fileChooser.setAcceptAllFileFilterUsed(true);
			//�����ļ�������
			fileChooser.setFileFilter(FileFilterFactoryAdapter.suffixFileFilter(new String[] { ".doc",
					".txt", ".pdf", ".xls" }));
			fileChooser.setApproveButtonText(NCLangRes.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000661")/*"�ϴ�����"*/);
		} else if (dlgType == JFileChooser.SAVE_DIALOG) {
			//ֻ��ѡ���ļ���
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(FileFilterFactoryAdapter.directoryFileFilter());
			fileChooser.setApproveButtonText(NCLangRes.getInstance()
					.getStrByID("common", "UC001-0000001")/*"����"*/);
		}

		//�Ƿ�֧�ֶ�ѡ
		fileChooser.setMultiSelectionEnabled(false);

		int value = fileChooser.showDialog(component, null);
		if (value != JFileChooser.APPROVE_OPTION)
			return null;
		return fileChooser.getSelectedFile().getAbsolutePath();
	}

	/**
	 * У���ļ���С
	 * 
	 * @param file
	 * @return��true��ʾ�ļ���С������ķ�Χ�ڣ����򷵻�false
	 */
	private static boolean validateSize(File file) {
		//ȡ�����������ļ���С
		float maxSize = getMaxSize();
		//ȡ�����ϴ����ļ���С
		long len = file.length();
		long fileMBs = len / (1024 * 1024);

		if (fileMBs < maxSize)
			return true;
		else
			return false;
	}

	/**
	 * �������ļ��е��ļ��������
	 * @return�������ȡ�ã����أ����򷵻�Ĭ��ֵ
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
		//��������ļ��еò�������ȡĬ��ֵ
		if (Math.abs(configSize) < 1e-6)
			configSize = maxSize;
		return configSize;
	}

	/**
	 * ��ȡ��Ϣ�����е��ļ����ݣ����Ѹ�������д��ָ���ļ��С�
	 * @param pkMsg ����
	 * @param fileName ������
	 * @param parent UI
	 * @throws BusinessException
	 */
	public static void writeFile(String pkMsg, String fileName, Container parent)
			throws BusinessException {
		//1.ѡ���ļ�����·��
		String localPath = chooseFile(parent, JFileChooser.SAVE_DIALOG);
		if (StringUtil.isEmptyWithTrim(localPath))
			return;

		String filePath = "";
		File dirFile = new File(localPath);
		//2.���·�������ڣ��ȴ���
		if (!dirFile.exists()) {
			dirFile.mkdirs();
			filePath = convertFilePath(localPath, fileName);
		} else {
			//�ж�ָ��·�����Ƿ����ͬ���ļ�
			filePath = convertFilePath(localPath, fileName);
			if (FileUtil.isFileExisted(filePath)) {
				if (MessageDialog.ID_YES != MessageDialog.showYesNoDlg(parent, NCLangRes.getInstance()
						.getStrByID("pfworkflow", "UPPpfworkflow-000227")/* @res "��ʾ" */, NCLangRes
						.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000670")/*"�ļ��Ѵ��ڣ��Ƿ񸲸ǣ�"*/))
					return;
			}
		}
		//3. ����ļ����ݲ�д�뱾��
		byte[] fileBytes = getFileContent(pkMsg);
		writeBytesToFile(filePath, fileBytes);

		String strPath = filePath;
		if (strPath.indexOf("\\") >= 0)
			strPath = StringUtil.replaceAllString(strPath, "\\", "/");

		MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000227")/* @res "��ʾ" */, NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000671", null, new String[] { strPath })/*"�ļ����سɹ���·��Ϊ��" + filePath*/);
	}

	/**
	 * ��ȡ��Ϣ�����е��ļ����ݣ������ֽ�����
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
	 * �Ѹ�������д��ָ���ļ���	 
	 * @param localPath: ���ؾ���·��
	 * @param fileContent���ļ�����
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
					"UPPpfworkflow-000673")/*"�����ļ���������"*/
					+ "\n" + e.getMessage());
		} finally {
			closeInFile(gzin);
			closeOutFile(bufOut);
		}
	}

	/**
	 * ����·�����ļ�����֯���ļ�������·����
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
