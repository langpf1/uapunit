package uap.workflow.app.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import uap.workflow.app.billprint.PfBillDataSourceGetter;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.pub.app.message.IPFMessage;
import uap.workflow.pub.app.message.PfMessageUtil;
import uap.workflow.pub.app.message.vo.CommonMessageVO;
import uap.workflow.pub.app.message.vo.MessageDateTimeVO;
import uap.workflow.pub.app.message.vo.MessageStatus;
import uap.workflow.pub.app.message.vo.MessageTypes;
import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.pub.app.message.vo.MessageinfoVO;
import uap.workflow.pub.app.message.vo.UserNameObject;
import uap.workflow.pub.app.mobile.PfMailAndSMSUtil;
import uap.workflow.pub.app.mobile.ShortMessageService;
import uap.workflow.pub.app.mobile.WirelessManager;
import uap.workflow.pub.app.mobile.vo.MobileMsg;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.ServerConfiguration;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.uap.print.IPrintEntry;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.message.util.IDefaultMsgConst;
import nc.message.util.MessageCenter;
import nc.message.vo.NCMessage;
import nc.ui.pf.multilang.PfMultiLangUtil;
import nc.ui.pub.print.IDataSource;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.pub.util.UserUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.sm.UserVO;

/**
 * ƽ̨��Ϣ����ʵ����
 * 
 * @author leijun 2005-9-5
 * @modifier guowl 2008-4-2 Ϊʵ�ֹ�����Ϣ�ķ����޸������µķ���
 */
public class PFMessageImpl implements IPFMessage {

	public void deleteCommonMsgByWhere(String whereSql) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		dao.deleteByClause(MessageinfoVO.class, whereSql);
	}

	public void deleteBizMsgByWhere(String whereSql) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Collection<WorkflownoteVO> colWorknote = dao.retrieveByClause(WorkflownoteVO.class, whereSql);
		WorkflownoteVO[] aryWorknote = colWorknote.toArray(new WorkflownoteVO[0]);
		dao.deleteVOArray(aryWorknote);
		PfMessageUtil.deleteMessagesOfWorknote(aryWorknote);
	}

	/**
	 * insert the common message,which mainly including the message of
	 * PreAlert,and P2P Message.<br>
	 * </strong>but do not incluing Approve message.<strong>
	 * 
	 * @see nc.itf.uap.pf.IPFMessage#insertCommonMsg(nc.vo.pub.msg.CommonMessageVO)
	 */
	public void insertCommonMsg(CommonMessageVO msg) throws BusinessException {
		insertCommonMsg(msg, null);
	}

	public void insertCommonMsg(CommonMessageVO msgVO, String pkcorp) throws BusinessException {
		UFDateTime date = new UFDateTime(new Date());
		msgVO.setSendDataTime(date);
		// �ָĳ��û��ͽ�ɫ���֣�Ȼ��ֱ�����Ϣ
		UserNameObject[] receivers = separateUserandRole(msgVO);
		if (receivers != null && receivers.length > 0) {
			PFMessageDMO dmo = new PFMessageDMO();
			msgVO.setReceiver(receivers);
			try {
				dmo.insertCommonMessage(msgVO, pkcorp);
			} catch (DbException e) {
				Logger.error(e.getMessage(), e);
				throw new PFBusinessException(e.getMessage());
			}
		}
	}

	public void insertCommonMsgs(CommonMessageVO[] msgVOs, String pkcorp) throws BusinessException {
		UFDateTime date = new UFDateTime(new java.util.Date());
		ArrayList alNeedSaved = new ArrayList();
		for (int i = 0; i < (msgVOs == null ? 0 : msgVOs.length); i++) {
			CommonMessageVO msg = msgVOs[i];
			msg.setSendDataTime(date);
			UserNameObject[] receivers = separateUserandRole(msgVOs[i]);
			if (receivers.length > 0) {
				msg.setReceiver(receivers);
				alNeedSaved.add(msg);
			}
		}
		// ����������Ϣ�����ݿ�
		PFMessageDMO dmo = new PFMessageDMO();
		try {
			dmo.insertCommonMessagesAry(alNeedSaved, pkcorp);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.itf.uap.pf.IPFMessage#insertCommonMsgAry(nc.vo.pub.msg.CommonMessageVO
	 * [])
	 */
	public void insertCommonMsgAry(CommonMessageVO[] cMsgVOs) throws BusinessException {
		UFDateTime date = new UFDateTime(new java.util.Date());
		ArrayList alNeedSaved = new ArrayList();
		for (int i = 0; i < (cMsgVOs == null ? 0 : cMsgVOs.length); i++) {
			CommonMessageVO msg = cMsgVOs[i];
			msg.setSendDataTime(date);
			UserNameObject[] receivers = separateUserandRole(cMsgVOs[i]);
			if (receivers.length > 0) {
				msg.setReceiver(receivers);
				alNeedSaved.add(msg);
			}
		}
		// ����������Ϣ�����ݿ�
		PFMessageDMO dmo = new PFMessageDMO();
		try {
			dmo.insertCommonMessagesAry(alNeedSaved, null);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	/**
	 * ���û��ͽ�ɫ���֣������û�Reciver
	 */
	private UserNameObject[] separateUserandRole(CommonMessageVO msg) throws BusinessException {
		if (msg.getReceiver() == null)
			return null;

		java.util.Vector vecUsers = new java.util.Vector();

		for (int i = 0; i < msg.getReceiver().length; i++) {
			UserNameObject unObj = msg.getReceiver()[i];
			if (unObj.isRole()) {
				// ��ȡ��ɫ��ĳ��˾�������û��������˾Ϊ�գ���ʾ��ȡ�ý�ɫ�����й�˾�������û�

				// XXX:ÿ����ɫ���뵥����ȡ���û�
				IUserManageQuery roleBS = (IUserManageQuery) NCLocator.getInstance().lookup(
						IUserManageQuery.class.getName());
				// V6�޸�
				// UserVO[] us = roleBS.getUsers(unObj.getUserPK(),
				// unObj.getPkcorp());
				UserVO[] us = roleBS.queryUserByRole(unObj.getUserPK(), null);
				us = UserUtil.filtDisableUsers(us);
				for (int j = 0; j < (us == null ? 0 : us.length); j++) {
					UserNameObject uno = new UserNameObject(us[j].getUser_name(), true);
					uno.setUserPK(us[j].getPrimaryKey());
					vecUsers.addElement(uno);
				}
			} else
				vecUsers.addElement(msg.getReceiver()[i]);
		}
		UserNameObject[] aryUsers = null;
		if (vecUsers.size() > 0) {
			aryUsers = new UserNameObject[vecUsers.size()];
			vecUsers.copyInto(aryUsers);
		} else {
			aryUsers = new UserNameObject[0];
		}

		return aryUsers;
	}

	/**
	 * ����ҵ����Ϣ���Ϊ�����
	 * 
	 * @see nc.itf.uap.pf.IPFMessage#signMessageDeal(java.lang.String)
	 * @modifier huangzg.��Ϊ��Ϣ��������
	 */
	public UFDateTime signMessageDeal(String nobizmsgPK, int msgType) throws BusinessException {
		UFDateTime serverTime = null;
		try {
			serverTime = new UFDateTime(new Date());
			switch (msgType) {
			case MessageTypes.MSG_TYPE_APPROVE:
				PFMessageDMO pfdmo = new PFMessageDMO();
				pfdmo.signMessageDeal(nobizmsgPK, serverTime);
				break;
			default:
				MessageInfoDAO dao = new MessageInfoDAO();
				dao.signMessageDeal(nobizmsgPK, serverTime);
				break;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		return serverTime;
	}

	/**
	 * ����ҵ����Ϣ���Ϊδ����
	 * 
	 * @see nc.itf.uap.pf.IPFMessage#signMessageUndeal(java.lang.String)
	 */
	public void signMessageUndeal(String nobizmsgPK, int msgType) throws BusinessException {
		try {
			switch (msgType) {
			case MessageTypes.MSG_TYPE_APPROVE:
				PFMessageDMO pfdmo = new PFMessageDMO();
				pfdmo.signMessageUndeal(nobizmsgPK);
				break;
			default:
				MessageInfoDAO dao = new MessageInfoDAO();
				dao.signMessageUnDeal(nobizmsgPK);
				break;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	public void sendMobileMessage(MobileMsg mobileVO) throws BusinessException {
		PfMailAndSMSUtil.sendSMS(mobileVO);
	}

	public void sendMobileMessage2(String[] userIds, String content) throws BusinessException {
		PfMailAndSMSUtil.sendSMS(userIds, content);
	}

	public MessageDateTimeVO getRecycleMsgs(String userPK, String pk_group, UFDateTime lastAccessTime)
			throws BusinessException {
		MessageDateTimeVO mdt = new MessageDateTimeVO();
		try {
			Date date = new Date();
			mdt.setAccessTime(new UFDateTime(date));
			// 1.��ѯ�����������������ͨ�����͵�֪ͨ
			PFMessageDMO pfmsgdmo = new PFMessageDMO();
			ArrayList alApprove = pfmsgdmo.queryDeletedItems(userPK, lastAccessTime);
			// 2.��ѯ������Ϣ��Ԥ����Ϣ�ͶԷ���Ϣ��������������Ϣ
			MessageInfoDAO midao = new MessageInfoDAO();
			ArrayList alInfos = midao.queryDeletedInfos(userPK, pk_group, lastAccessTime);
			// 3.����Ԥ����Ϣ�͹�����Ϣ��
			Vector vect = midao.separateInfoMsgs(alInfos);
			// ���빫������ʾ����Ϣ
			ArrayList alBulletins = (ArrayList) vect.get(0);
			mdt.setScrollMsgs((MessageVO[]) alBulletins.toArray(new MessageVO[alBulletins.size()]));
			// ����Ԥ������ʾ����Ϣ
			ArrayList alPAs = (ArrayList) vect.get(1);
			mdt.setPAMsgs((MessageVO[]) alPAs.toArray(new MessageVO[alPAs.size()]));
			// ���빤����������Ϣ
			ArrayList alPushAndPull = (ArrayList) vect.get(2);
			// �ϲ� ������ ��ʾ����Ϣ
			ArrayList alWorkitems = new ArrayList();
			alWorkitems.addAll(alApprove);
			alWorkitems.addAll(alPushAndPull);
			mdt.setWorkitems((MessageVO[]) alWorkitems.toArray(new MessageVO[alWorkitems.size()]));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		return mdt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.itf.uap.pf.IPFMessage#signMessageDelete(java.util.HashMap)
	 */
	public void signMessageDelete(HashMap hmDelete) throws BusinessException {
		Set keys = hmDelete.keySet();
		PFMessageDMO dmo = new PFMessageDMO();
		try {
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				Integer type = (Integer) iter.next();
				if (type.intValue() == MessageTypes.MSG_TYPE_APPROVE) {
					dmo.signWorkitems((ArrayList) hmDelete.get(type), 0);
				} else {// if (type.intValue() == MessageTypes.MSG_TYPE_INFO){
					// //huangzg���������
					dmo.signMessageinfos((ArrayList) hmDelete.get(type), 0);
				}
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	/**
	 * ����Ϣ��־Ϊ"�ѷ��"
	 */
	public UFDateTime signMessageSeal(HashMap hmSeal) throws BusinessException {
		UFDateTime serverTime = new UFDateTime(new Date());
		Set keys = hmSeal.keySet();
		try {
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				Integer type = (Integer) iter.next();
				if (type.intValue() == MessageTypes.MSG_TYPE_PUBLIC) {
					MessageInfoDAO dao = new MessageInfoDAO();
					dao.signMessageSeal((ArrayList) hmSeal.get(type), serverTime);
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		return serverTime;

	}

	/**
	 * ����Ϣ��־Ϊ"δ���"
	 */
	public UFDateTime signMessageUnSeal(HashMap hmSeal) throws BusinessException {
		UFDateTime serverTime = new UFDateTime(new Date());
		Set keys = hmSeal.keySet();
		try {
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				Integer type = (Integer) iter.next();
				if (type.intValue() == MessageTypes.MSG_TYPE_PUBLIC) {
					MessageInfoDAO dao = new MessageInfoDAO();
					dao.signMessageUnSeal((ArrayList) hmSeal.get(type), serverTime);
				}
			}

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		return serverTime;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.itf.uap.pf.IPFMessage#deleteMessages(java.util.HashMap)
	 */
	public void deleteMessages(HashMap hmDelete) throws BusinessException {
		Set keys = hmDelete.keySet();
		BaseDAO dao = new BaseDAO();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			Integer type = (Integer) iter.next();
			if (type.intValue() == MessageTypes.MSG_TYPE_APPROVE) {
				ArrayList alPKs = (ArrayList) hmDelete.get(type);
				dao.deleteByPKs(WorkflownoteVO.class, (String[]) alPKs.toArray(new String[alPKs.size()]));
			} else {
				ArrayList alPKs = (ArrayList) hmDelete.get(type);
				dao.deleteByPKs(MessageinfoVO.class, (String[]) alPKs.toArray(new String[alPKs.size()]));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.itf.uap.pf.IPFMessage#restoreDeleteMsgs(java.util.HashMap)
	 */
	public void restoreDeleteMsgs(HashMap hmRestore) throws BusinessException {
		Set keys = hmRestore.keySet();
		PFMessageDMO dmo = new PFMessageDMO();
		try {
			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				Integer type = (Integer) iter.next();
				if (type.intValue() == MessageTypes.MSG_TYPE_APPROVE) {
					dmo.signWorkitems((ArrayList) hmRestore.get(type), 1);
				} else {// if (type.intValue() == MessageTypes.MSG_TYPE_INFO){
					// //huangzg���������
					dmo.signMessageinfos((ArrayList) hmRestore.get(type), 1);
				}
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	public String readHTML(String paMsgURL) throws BusinessException {
		ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
		StringBuffer strBuffer = new StringBuffer();
		if (sc != null && !sc.isSingle()) {// ��Ⱥ������
			String server = sc.getServerRunningService("IPreAlertConfigService");
			String endUrl = sc.getServerEndpointURL(server);
			int index = endUrl.indexOf("/ServiceDispatcherServlet");
			endUrl = endUrl.substring(0, index);
			InputStream is = null;
			BufferedReader readin = null;
			try {
				URL url = new URL(endUrl + "/NCFindWeb?service=IPreAlertConfigService&filename=" + "PreAlart/Messages/"
						+ paMsgURL);
				is = url.openStream();
				readin = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line = "";
				while ((line = readin.readLine()) != null) {
					strBuffer.append(line);
				}
			} catch (MalformedURLException e) {
				Logger.error("URL ��ʽ����:" + e.getMessage(), e);
				String errorShow = "<html><title></title><font color=\"red\">"
						+ NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000909" /* �ļ� */)
						+ paMsgURL
						+ NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000910" /*
																										 * ��ʽ����
																										 * ,
																										 * ������鿴��־
																										 * !
																										 */)
						+ "</font></html>";
				strBuffer = new StringBuffer(errorShow);

			} catch (IOException e) {
				Logger.error("��ȡ�ļ�IO����:" + e.getMessage(), e);
				String errorShow = "<html><title></title><font color=\"red\">"
						+ NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000911" /* ��ȡ�ļ� */)
						+ paMsgURL
						+ NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000912" /*
																										 * ����
																										 * ,
																										 * ������鿴��־
																										 * !
																										 */)
						+ "</font></html>";
				strBuffer = new StringBuffer(errorShow);
			} finally {
				if (readin != null) {
					try {
						readin.close();
					} catch (IOException e) {
						Logger.error(e.getMessage(), e);
					}
				}
				if (null != is) {
					try {
						is.close();
					} catch (IOException e2) {
						Logger.error(e2.getMessage(), e2);
					}
				}
			}

		} else {// �����»��߷Ǽ�Ⱥ������
			String filesperator = System.getProperties().getProperty("file.separator");
			String PaWebUrl = RuntimeEnv.getInstance().getNCHome() + filesperator + "webapps" + filesperator + "nc_web"
					+ filesperator + "PreAlart" + filesperator + "Messages";
			String pamsgURI = PaWebUrl + filesperator + paMsgURL;
			BufferedReader readin = null;
			try {
				readin = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pamsgURI)), "UTF-8"));
				String line = "";
				while ((line = readin.readLine()) != null) {
					strBuffer.append(line);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				String errorShow = "<html><title></title><font color=\"red\">"
						+ NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000911" /* ��ȡ�ļ� */)
						+ paMsgURL
						+ NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000912" /*
																										 * ����
																										 * ,
																										 * ������鿴��־
																										 * !
																										 */)
						+ "</font></html>";
				strBuffer = new StringBuffer(errorShow);
				// throw new PFBusinessException(e.getMessage());
			} finally {
				if (null != readin) {
					try {
						readin.close();
					} catch (IOException e2) {
						Logger.error(e2.getMessage(), e2);
					}
				}
			}
		}
		return strBuffer.toString();
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * nc.itf.uap.pf.IPFMessage#insertBizMsgs(nc.vo.pub.workflownote.WorkflownoteVO
//	 * [])
//	 */
//	public void insertBizMsgs(WorkflownoteVO[] noteVOs) throws BusinessException {
//		if (noteVOs == null || noteVOs.length == 0)
//			return;
//
//		UFDateTime now = new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime());
//		for (int i = 0; i < noteVOs.length; i++) {
//			// XXX::����һЩƽ̨�������Ϣ
//			noteVOs[i].setActiontype(WorkflownoteVO.WORKITEM_TYPE_BIZ);
//			noteVOs[i].setIscheck("N");
//			noteVOs[i].setReceivedeleteflag(UFBoolean.FALSE);
//			noteVOs[i].setSenddate(now);
//			noteVOs[i].setApprovestatus(TaskInstanceStatus.Started.getIntValue());
//		}
//
//		// insert
//		BaseDAO dao = new BaseDAO();
//		String[] pk_details = dao.insertVOArray(noteVOs);
//		for (int i = 0; i < pk_details.length; i++) {
//			noteVOs[i].setPrimaryKey(pk_details[i]);
//		}
//		// insert
//		PfMessageUtil.sendMessageOfWorknoteBatch(noteVOs);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nc.itf.uap.pf.IPFMessage#insertPushOrPullMsgs(nc.vo.pub.msg.MessageinfoVO
	 * [], int)
	 */
	public void insertPushOrPullMsgs(MessageinfoVO[] infoVOs, int iPushOrPull) throws BusinessException {
		if (infoVOs == null || infoVOs.length == 0)
			return;

		UFDateTime now = new UFDateTime(new Date());
		for (int i = 0; i < infoVOs.length; i++) {
			// WARN::����һЩƽ̨�������Ϣ
			infoVOs[i].setMessagestate(Integer.valueOf(MessageStatus.STATE_NOT_CHECK));
			infoVOs[i].setType(Integer.valueOf(iPushOrPull));
			infoVOs[i].setContent(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000913" /* ҵ������ʽ����ʽ��Ϣ */));
			infoVOs[i].setPriority(Integer.valueOf(0));
			infoVOs[i].setSenddate(now);

		}

		// insert
		PfMessageUtil.insertBizMessages(infoVOs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.itf.uap.pf.IPFMessage#syncSendSMS(java.lang.String[][],
	 * java.lang.String[])
	 */
	public Object syncSendSMS(String[][] targetPhones, String[] msges) throws BusinessException {
		ShortMessageService sms = WirelessManager.getSMS();
		if (sms == null)
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000914" /*
											 * >>�Ҳ�����Ķ��ŷ���ʵ���࣬�޷����ͺͽ��ն���
											 */));

		if (!sms.initialize())
			// ���ŷ����ʼ��ʧ��
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000915" /*
											 * >>���ŷ����ʼ��ʧ��
											 */));

		HashMap hmRet = new HashMap();
		for (int i = 0; i < msges.length; i++) {
			String[] phones = targetPhones[i];
			for (int j = 0; j < phones.length; j++) {
				// ����Ҫ�ỰSID
				Object obj = sms.sendMessages(new String[] { phones[j] }, msges[i]);
				hmRet.put(phones[j], obj);
			}
		}

		return hmRet;
	}

	public void updateCommonMsg(MessageVO msgVO) throws BusinessException {
		PFMessageDMO dmo = new PFMessageDMO();
		try {
			dmo.updateCommonMessage(msgVO);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
	}

	@Override
	public String generateBillHtml(String billID, String billType, String printTempletid, String checkman)
			throws BusinessException {
		String billHtml = null;
		
		if (StringUtil.isEmptyWithTrim(printTempletid))
			return billHtml;
		
		IDataSource ds = PfBillDataSourceGetter.getDataSourceOf(billType, billID);

		if (ds == null) {
			return null;
		}

		try {
			IPrintEntry printEntry = (IPrintEntry) NCLocator.getInstance().lookup(IPrintEntry.class.getName());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			IDataSource[] dataSources = new IDataSource[] { ds };
			printEntry.exportHtml(dataSources, printTempletid, bos);
			billHtml = bos.toString();
		} catch (Throwable e) {
			// XXX::����־�쳣�����񲻻ع�
			Logger.error(">>��̨��ӡģ�����HTML����=" + e.getMessage(), e);
		}

		return billHtml;
	}

	public void sendEmail(String title, String content, String[] recEmails, String[] attachFiles)
			throws BusinessException {
		PfMailAndSMSUtil.sendEmail(title, content, recEmails, null, attachFiles);
	}

	public MessageDateTimeVO getFilteredReceivedMsgs(String userPK, String pk_group, HashMap hmFilters)
			throws BusinessException {
//		MessageDateTimeVO mdt = new MessageDateTimeVO();
//		// XXX:6.0�ϲ�Ϊһ��
//		// MessageFilter bulletinFilter = (MessageFilter)
//		// hmFilters.get(MessageVO.SPACE_IN_BULLETIN);
//		// MessageFilter paFilter = (MessageFilter)
//		// hmFilters.get(MessageVO.SPACE_IN_PREALERT);
//		MessageFilter worklistFilter = (MessageFilter) hmFilters.get(MessageVO.SPACE_IN_WORKLIST);
//		try {
//			mdt.setAccessTime(new UFDateTime(new Date()));
//			MessageInfoDAO midao = new MessageInfoDAO();
//			if (worklistFilter != null) {
//				// ��ѯ������Ϣ���Է���Ϣ
//				ArrayList alBulletins = midao.queryBulletinMsgs(userPK, pk_group, worklistFilter);
//				mdt.setScrollMsgs((MessageVO[]) alBulletins.toArray(new MessageVO[alBulletins.size()]));
//
//				// ��ѯԤ����Ϣ
//				ArrayList alPAs = midao.queryPaMsgs(userPK, pk_group, worklistFilter);
//				mdt.setPAMsgs((MessageVO[]) alPAs.toArray(new MessageVO[alPAs.size()]));
//
//				// ��ѯ֪ͨ��Ϣ
//				ArrayList alInfos = midao.queryInfoMsgs(userPK, pk_group, worklistFilter);
//				mdt.setInfoMsgs((MessageVO[]) alInfos.toArray(new MessageVO[alInfos.size()]));
//
//				// ��ѯ�����������������ͨ�����͵�֪ͨ
//				PFMessageDMO pfmsgdmo = new PFMessageDMO();
//
//				worklistFilter.setWorkflownoteFiltered(true);
//				ArrayList alApprove = pfmsgdmo.queryWorkitems(userPK, worklistFilter);
//
//				worklistFilter.setWorkflownoteFiltered(false);
//				ArrayList alPushAndPull = midao.queryPushAndPullMsgs(userPK, worklistFilter);
//				// �ϲ� ������ ��ʾ����Ϣ
//				ArrayList alWorkitems = new ArrayList();
//				alWorkitems.addAll(alApprove);
//				alWorkitems.addAll(alPushAndPull);
//				MessageVO[] voArray = (MessageVO[]) alWorkitems.toArray(new MessageVO[alWorkitems.size()]);
//				// �Ժϲ������������
//				java.util.Comparator c = new java.util.Comparator() {
//					public int compare(Object o1, Object o2) {
//						UFDateTime dt1 = ((MessageVO) o1).getSendDateTime();
//						UFDateTime dt2 = ((MessageVO) o2).getSendDateTime();
//						if (dt1 == null && dt2 == null)
//							return 0;
//						if (dt1 == null && dt2 != null)
//							return 1;
//						if (dt1 != null && dt2 == null)
//							return -1;
//						return dt2.compareTo(dt1);
//					}
//				};
//				java.util.Arrays.sort(voArray, c);
//				mdt.setWorkitems(voArray);
//			}
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
//					"UPPpfworkflow-000916" /*
//											 * >>��Ϣ��ѯ�����쳣
//											 */));
//		}
//		return mdt;
	return null;
	}

	public void insertCommonMsgAry_RequiresNew(CommonMessageVO[] msgVOs) throws BusinessException {
		insertCommonMsgAry(msgVOs);
	}

	@Override
	public void dealFlowCheckMsgs(String userPk, String pk_wf_task, String pk_wf_msg, MessageVO msg, String checknote)
			throws BusinessException {
		// new FlowMessageUtil().procCheckedMsg(userPk, pk_wf_task,
		// pk_wf_msg,msg,checknote);//
	}

	@Override
	public void dealFlowCheckMsgs(NCMessage ncmsg, String checkNote) throws BusinessException {

		BaseDAO dao = new BaseDAO();

		nc.message.vo.MessageVO msgVO = ncmsg.getMessage();

		String pk_wf_task = msgVO.getPk_detail();
		String flowMsgChecker = InvocationInfoProxy.getInstance().getUserId();
		UserVO uservo = (UserVO) dao.retrieveByPK(UserVO.class, flowMsgChecker);

		// FIXME: ��Ϊ�Ƶ��û�����ģ������Ҳ����Ƶ��û�
		// ��ѯ����ǩ����Ϣ�Ļ��ڵ������������û�
		String sql = "select distinct checkman from pub_workflownote where pk_wf_task ='" + pk_wf_task
				+ "'  and approvestatus = " + TaskInstanceStatus.Finished.getIntValue()
				+ " and actiontype not like '%" + WorkflownoteVO.WORKITEM_ADDAPPROVER_SUFFIX + "'";

		List<String> flowCheckedCuserid = (List<String>) dao.executeQuery(sql, new ColumnListProcessor());

		// �������������û�
		Map<String, List<String>> langUserMap = Pfi18nTools.classifyUsersByLangcode(flowCheckedCuserid
				.toArray(new String[0]));

		List<NCMessage> toBeSent = new ArrayList<NCMessage>();

		for (Iterator<String> langIt = langUserMap.keySet().iterator(); langIt.hasNext();) {
		
			// ����ÿ�����֣�����һ��NCMessage
			String langcode = langIt.next();
			List<String> users = langUserMap.get(langcode);

			StringBuffer sb = new StringBuffer();
			for (String id : users) {
				sb.append(",");
				sb.append(id);
			}

			// �������������û�id
			String flowCheckedUserStr = sb.substring(1);

			try {
				NCMessage langMsg = (NCMessage) ncmsg.clone();


				// ���������µ�ncmessage���⼰����
				String originLangcode = InvocationInfoProxy.getInstance().getLangCode();
				{
					InvocationInfoProxy.getInstance().setLangCode(langcode);

					String userName = PfMultiLangUtil.getSuperVONameOfCurrentLang(uservo, "user_name");


					// ���⣺ �û�������ǩ����Ϣ
					String subject = NCLangResOnserver.getInstance().getStrByID("pfworkflow1", "PFMessageImpl-000000",
							null, new String[] { userName })/* �û�{0}��ǩ����Ϣ */;

					StringBuffer content = new StringBuffer();

					// ���ݣ�
					// ���
					// �����
					// ԭ�ģ�
					// ��ԭ�ı��⡱
					// ��ԭ�����ݡ�
					content.append(NCLangResOnserver.getInstance().getStrByID("pfworkflow1", "PFMessageImpl-000001")/*
																													 * ǩ�����
																													 * ��
																													 */);
					content.append("\n");
					content.append(checkNote);
					content.append("\n==============================================\n");
					content.append(NCLangResOnserver.getInstance().getStrByID("pfworkflow1", "PFMessageImpl-000002")/*
																													 * ԭ��
																													 * ��
																													 */);
					content.append("\n");
					content.append(msgVO.getSubject());
					content.append("\n\n");
					content.append(msgVO.getContent());

					langMsg.getMessage().setSubject(subject);
					langMsg.getMessage().setContent(content.toString());
					langMsg.getMessage().setSender(flowMsgChecker);
					langMsg.getMessage().setReceiver(flowCheckedUserStr);
					langMsg.getMessage().setContenttype(WorkflownoteVO.FLOWMSG_AUTO);
					langMsg.getMessage().setMsgsourcetype(IDefaultMsgConst.NOTICE);
					langMsg.getMessage().setIsdelete(new UFBoolean(false));
					langMsg.getMessage().setIshandled(new UFBoolean(false));
					langMsg.getMessage().setSendtime(new UFDateTime());
					langMsg.getMessage().setPk_message(null);

					toBeSent.add(langMsg);

					InvocationInfoProxy.getInstance().setLangCode(originLangcode);
				}

			} catch (CloneNotSupportedException e) {
				Logger.error(e.getMessage(), e);
				continue;
			}
		}

		try {
			MessageCenter.sendMessage(toBeSent.toArray(new NCMessage[0]));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException();
		}

	}
}
