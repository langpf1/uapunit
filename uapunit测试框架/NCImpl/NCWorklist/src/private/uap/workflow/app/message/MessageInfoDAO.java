package uap.workflow.app.message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import uap.workflow.pub.app.message.vo.CommonMessageVO;
import uap.workflow.pub.app.message.vo.MessageStatus;
import uap.workflow.pub.app.message.vo.MessageTypes;
import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.pub.app.message.vo.MessageinfoVO;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.vo.pub.lang.UFDateTime;


/**
 * MessageInfoVO ��DAO.<br>
 * As that deal with the No-Business Message.
 * 
 * @author huangzg 2006-3-28
 * @Modifier guowl 2008-4-1 Ϊʵ�ֹ�����Ϣ�ķ����޸������µķ���
 */

public class MessageInfoDAO {
	public MessageInfoDAO() {
		super();
	}

	/**
	 * �õ����ݽ����˵õ��ķ�ҵ����Ϣ,�����뵽��������Ԥ����.
	 * 
	 * @param userPK
	 * @param pk_group
	 * @param lastAccessTime
	 * @return Vector [0]=bulletin,[1]=pa,[2]=workflow
	 * @throws DbException
	 */
	public Vector getReceivedMsgs(String userPK, String pk_group, UFDateTime lastAccessTime)
			throws DbException {
		ArrayList ary = getRecivedMsgsCollection(userPK, pk_group, lastAccessTime);
		return separateInfoMsgs(ary);
	}

	// Array with MessageInfoVO
	public Vector separateInfoMsgs(ArrayList ary) {
		ArrayList aryBulletin = new ArrayList();
		ArrayList aryPA = new ArrayList();
		ArrayList aryWorkflow = new ArrayList();
		for (int i = 0; i < ary.size(); i++) {
			MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO((MessageinfoVO) ary.get(i));
			if (msgvo.getMsgType() == MessageTypes.MSG_TYPE_PA
					|| msgvo.getMsgType() == MessageTypes.MSG_TYPE_BG)
				aryPA.add(msgvo);
			else if (msgvo.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PUSH
					|| msgvo.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW_PULL
					|| msgvo.getMsgType() == MessageTypes.MSG_TYPE_BUSIFLOW
					|| msgvo.getMsgType() == MessageTypes.MSG_TYPE_INFO) {
				aryWorkflow.add(msgvo);
			} else
				aryBulletin.add(msgvo);
		}
		Vector vec = new Vector();
		vec.add(aryBulletin);
		vec.add(aryPA);
		vec.add(aryWorkflow);
		return vec;
	}

	/**
	 * ��pub_messageinfo�������Ϣ
	 * <li>������Ϣ
	 * <li>�Է���Ϣ
	 * <li>������������Ϣ
	 * 
	 * @param pk_group
	 * @return return the ArrayList of MessageVO
	 * @throws DbException
	 */
	private ArrayList getRecivedMsgsCollection(String userPK, String pk_group,
			UFDateTime lastAccessTime) throws DbException {
		String sql = "select a.pk_messageinfo,a.senderman,b.user_name,a.checkman,a.pk_corp,a.type,a.messagestate,a.url,a.title,a.content,a.senddate,a.priority,a.dealdate,"
				+ "a.billid,a.billno,a.pk_billtype,a.pk_srcbilltype,a.pk_busitype,a.actiontype,a.titlecolor,a.filecontent "
				+ "from pub_messageinfo a left join sm_user b on a.senderman=b.cuserid "
				+ "where (checkman=? and a.ts>?  or (a.type=? and (a.pk_corp=? or a.pk_corp='0001'))) and (isnull(a.receivedeleteflag,'N')='N') order by senddate desc";

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(userPK);
			// ֻȡ���µ���Ϣ
			if (lastAccessTime == null)
				para.addParam("0");
			else
				para.addParam(lastAccessTime.toString());
			para.addParam(MessageTypes.MSG_TYPE_PUBLIC);
			para.addParam(pk_group);
			ArrayList aryResult = (ArrayList) jdbc.executeQuery(sql, para, new MessageInfoProcessor());
			return aryResult;
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ��ѯ��ɾ���� ����ĳ�û���֪ͨ��Ϣ
	 * 
	 * @param userPK
	 * @param pk_group
	 * @param lastAccessTime
	 * @return
	 * @throws DbException
	 */
	public ArrayList queryDeletedInfos(String userPK, String pk_group, UFDateTime lastAccessTime)
			throws DbException {
		String sql = "select a.pk_messageinfo,a.senderman,b.user_name,a.checkman,a.pk_corp,a.type,a.messagestate,a.url,a.title,a.content,a.senddate,a.priority,a.dealdate,"
				+ "a.billid,a.billno,a.pk_billtype,a.pk_srcbilltype,a.pk_busitype,a.actiontype,a.titlecolor,a.filecontent "
				+ "from pub_messageinfo a left join sm_user b on a.senderman=b.cuserid "
				+ "where (checkman=? and a.ts>? or (a.type=? and (a.pk_corp=? or a.pk_corp='0001'))) and a.receivedeleteflag='Y' order by senddate desc";

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(userPK);
			// ֻȡ���µ���Ϣ
			if (lastAccessTime == null)
				para.addParam("0");
			else
				para.addParam(lastAccessTime.toString());
			para.addParam(MessageTypes.MSG_TYPE_PUBLIC);
			para.addParam(pk_group);
			ArrayList aryResult = (ArrayList) jdbc.executeQuery(sql, para, new MessageInfoProcessor());
			return aryResult;
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * insert the Not-business message.
	 * 
	 * @param al is a Array incluing the CommonMessageVO.
	 * @throws DbException
	 */
	public void insertAryCommonMessage(ArrayList al, String pkcorp) throws DbException {

		String sql = "insert into pub_messageinfo (pk_messageinfo,senderman,checkman,type,messagestate,url,title,content,senddate,priority,pk_corp,pk_billtype,filecontent,actiontype,billid,titlecolor,needflowcheck,pk_wf_msg,pk_wf_task)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		// ��¼�Ѿ����͹����û�PK
		HashSet setHasAdded = new HashSet();
		for (Iterator iter = al.iterator(); iter.hasNext();) {
			//ÿ��CommonMessageVO��Ϊ�ǲ�ͬ����Ϣ������Ҫ����Ѿ����͹����û�PK
			setHasAdded.clear();
			
			CommonMessageVO msg = (CommonMessageVO) iter.next();
			String[] oids = new SequenceGenerator().generate(msg.getReceiver().length);
			PersistenceManager persist = null;
			try {
				persist = PersistenceManager.getInstance();
				JdbcSession jdbc = persist.getJdbcSession();

				for (int i = 0; i < msg.getReceiver().length; i++) {
					// ��֤����ͬһ���û�������ͬ����Ϣ
					if (!setHasAdded.contains(msg.getReceiver()[i].getUserPK())) {
						SQLParameter para = new SQLParameter();
						para.addParam(oids[i]);
						para.addParam(msg.getSender());
						para.addParam(msg.getReceiver()[i].getUserPK());

						para.addParam(msg.getType());

						para.addParam(MessageStatus.STATE_NOT_CHECK);
						para.addParam(msg.getMailAddress() == null ? null : msg.getMailAddress().trim());
						para.addParam(msg.getTitle());
						para.addParam(msg.getMessageContent());
						para.addParam(msg.getSendDataTime());
						para.addParam(msg.getPriority());
						para.addParam(pkcorp);
						para.addParam(msg.getBilltype());
						para.addBlobParam(msg.getFileContent());
						para.addParam(msg.getActionType());
						para.addParam(msg.getBillid());
						para.addParam(msg.getTitleColor());
						para.addParam(msg.isNeedFlowCheck()? "Y":"N");
						para.addParam(msg.getPk_wf_msg());
						para.addParam(msg.getPk_wf_task());
						jdbc.addBatch(sql, para);

						setHasAdded.add(msg.getReceiver()[i].getUserPK());
					}
				}// /{end for}
				// ����ִ��
				jdbc.executeBatch();
			} finally {
				if (persist != null)
					persist.release();
			}
		}// /{end for}
	}

	class MessageInfoProcessor extends BaseProcessor {

		public Object processResultSet(ResultSet rs) throws SQLException {
			if (null == rs) { return null; }
			ArrayList al = new ArrayList();
			while (rs.next()) {
				MessageinfoVO mivo = new MessageinfoVO();
				String key = rs.getString(1);
				mivo.setPrimaryKey(key == null ? null : key.trim());
				String sendman = rs.getString(2);
				mivo.setSenderman(sendman == null ? null : sendman.trim());
				String senderName = rs.getString(3);
				mivo.setSendermanName(senderName == null ? sendman : senderName.trim());
				// TODO �ѷ��������ֺͽ�������������Ϊһ��û����ʾʱ��ȡ����
				mivo.setCheckmanName(senderName == null ? null : senderName.trim());
				String checkman = rs.getString(4);
				mivo.setCheckman(checkman == null ? null : checkman.trim());
				String pkcorp = rs.getString(5);
				mivo.setPk_corp(pkcorp == null ? null : pkcorp.trim());
				int type = rs.getInt(6);
				mivo.setType(Integer.valueOf(type));
				mivo.setMessagestate(Integer.valueOf(rs.getInt(7)));

				String url = rs.getString(8);
				mivo.setUrl(url == null ? null : url.trim());
				String title = rs.getString(9);
				mivo.setTitle(title == null ? null : title.trim());
				String content = rs.getString(10);
				//mivo.setContent(content == null ? null : content.trim());
				mivo.setContent(content == null ? null : content);
				String senddate = rs.getString(11);
				mivo.setSenddate(senddate == null ? null : new UFDateTime(senddate.trim()));
				mivo.setPriority(Integer.valueOf(rs.getInt(12)));
				String dealdate = rs.getString(13);
				mivo.setDealdate(dealdate == null ? null : new UFDateTime(dealdate.trim()));

				// lj+2006-6-19
				// billid
				mivo.setBillid(rs.getString(14));
				// billno
				mivo.setBillno(rs.getString(15));
				// pk_billtype
				mivo.setPk_billtype(rs.getString(16));
				// pk_srcbilltype
				mivo.setPk_srcbilltype(rs.getString(17));
				//actiontype
				String actionTypeCode = rs.getString(18);
				mivo.setActiontype(actionTypeCode == null ? null : actionTypeCode.trim());

				// titlecolor
				mivo.setTitlecolor(rs.getString(19));

				// ���÷�����ΪԤ��ƽ̨ʱ�ķ�������
				if (type == MessageTypes.MSG_TYPE_PA && sendman.equals(MessageVO.getPAMutliLangName())) {
					mivo.setSendermanName(MessageVO.getPAMutliLangName());
				}
				//filecontent
				mivo.setFilecontent(rs.getBytes(20));
				mivo.setNeedFlowCheck("Y".equals(rs.getString(21)));
				mivo.setPk_wf_msg(rs.getString(22));
				mivo.setPk_wf_task(rs.getString(23));
				al.add(mivo);
			}
			return al;
		}
	}

	/**
	 * ������Ϣ����,������ɾ����־��һ��
	 * 
	 * @throws DbException
	 */

	public void deleteNoBizMessages(String[] pks) throws DbException {
		if (pks == null || pks.length == 0)
			return;
		String sql = "update pub_messageinfo set receivedeleteflag='Y' where pk_messageinfo=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			for (int i = 0; i < pks.length; i++) {
				SQLParameter para = new SQLParameter();
				para.addParam(pks[i]);
				jdbc.executeUpdate(sql, para);
			}
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ����������������Ϣ�ġ�����ɾ����־��Ϊ��ɾ����̬<br>
	 * 
	 * @param pks
	 */
	public void deleteRecSendMessages(ArrayList pks) throws DbException {
		if (pks == null || pks.size() == 0)
			return;
		//String sql = "update pub_messageinfo set receivedeleteflag='Y', dr=1 where pk_messageinfo=? ";
		String sql = "update pub_messageinfo set receivedeleteflag='Y' where pk_messageinfo=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			for (int i = 0; i < pks.size(); i++) {
				SQLParameter para = new SQLParameter();
				para.addParam(pks.get(i).toString());
				jdbc.executeUpdate(sql, para);
			}
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ����Ϣ���Ϊ�����
	 * 
	 * @throws DbException
	 */
	public void signMessageDeal(String noBizmsgpk, UFDateTime serverTime) throws DbException {
		updateMsgState(noBizmsgpk, MessageStatus.STATE_CHECKED, serverTime);
	}

	public void signMessageUnDeal(String noBizmsgpk) throws DbException {
		updateMsgState(noBizmsgpk, MessageStatus.STATE_NOT_CHECK, null);

	}

	/**
	 * ����Ϣ���Ϊ�ѷ��
	 * 
	 * @throws DbException
	 */
	public void signMessageSeal(ArrayList alInfoPKs, UFDateTime serverTime) throws DbException {
		updateMsgStateBatch(alInfoPKs, MessageStatus.STATE_SEALED, serverTime);
	}

	/**
	 * ����Ϣ���Ϊδ���
	 * 
	 * @throws DbException
	 */
	public void signMessageUnSeal(ArrayList alInfoPKs, UFDateTime serverTime) throws DbException {
		updateMsgStateBatch(alInfoPKs, MessageStatus.STATE_NOT_CHECK, serverTime);
	}

	/**
	 * ������Ϣ.
	 * 
	 * @param serverTime
	 */
	public void updateCommonMessage(MessageVO msg) throws DbException {
		String sql = "";
		//���ݸ����Ƿ��޸�����֯sql���
		if (msg.isAttachChanged())
			sql = "update pub_messageinfo set priority=?, title=?, content=?, url=?, filecontent=? "
					+ " where pk_messageinfo=? ";
		else
			sql = "update pub_messageinfo set priority=?, title=?, content=? "
					+ " where pk_messageinfo=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(msg.getPriority());
			para.addParam(msg.getTitle());
			para.addParam(msg.getMessageNote());
			if (msg.isAttachChanged()) {
				para.addParam(msg.getMailAddress());
				para.addBlobParam(msg.getFilecontent());
			}
			para.addParam(msg.getPrimaryKey());
			jdbc.executeUpdate(sql, para);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ������Ϣ�Ĵ���״̬ .���Ϊδ������ServerTimeΪnull	
	 * 
	 * @param serverTime
	 */
	private void updateMsgState(String noBizmsgpk, int state, UFDateTime serverTime)
			throws DbException {
		String sql = "";
		if (serverTime != null)
			sql = "update pub_messageinfo set messagestate=?, dealdate='" + serverTime.toString() + "'"
					+ " where pk_messageinfo=? ";
		else
			sql = "update pub_messageinfo set messagestate=? where pk_messageinfo=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(state);
			para.addParam(noBizmsgpk);
			jdbc.executeUpdate(sql, para);
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ����������Ϣ��״̬ .���Ϊδ�������δ�����ServerTimeΪnull
	 * 
	 * @author guowl 2008-4-1 
	 * @param serverTime
	 */
	private void updateMsgStateBatch(ArrayList alInfoPKs, int state, UFDateTime serverTime)
			throws DbException {
		String sql = "";
		if (serverTime != null)
			sql = "update pub_messageinfo set messagestate=?, dealdate='" + serverTime.toString() + "'"
					+ " where pk_messageinfo=? ";
		else
			sql = "update pub_messageinfo set messagestate=? where pk_messageinfo=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			SQLParameter para = new SQLParameter();
			for (int i = 0; i < alInfoPKs.size(); i++) {
				para.clearParams();
				para.addParam(state);
				para.addParam(alInfoPKs.get(i).toString());
				jdbc.addBatch(sql, para);
			}
			jdbc.executeBatch();

		} finally {
			if (persist != null)
				persist.release();
		}
		return;
	}

	/**
	 * ������Ϣ����,�����ɾ������Ϣ��
	 * 
	 * @throws DAOException
	 */
	public void phyDeleteAryByPKS(ArrayList aryMsgPKs) throws DAOException {
		if (aryMsgPKs != null) {
			String[] pks = ((String[]) aryMsgPKs.toArray(new String[aryMsgPKs.size()]));
			BaseDAO dao = new BaseDAO();
			dao.deleteByPKs(MessageinfoVO.class, pks);

		}
	}

	/**
	 * ����ɾ����Ϣ
	 * 
	 * @throws DAOException
	 */
	public void physicalDeleteMsg(String[] pks) throws DAOException {
		BaseDAO dao = new BaseDAO();
		dao.deleteByPKs(MessageinfoVO.class, pks);

	}

//	/**
//	 * ���ݹ�������ȡ����������Ϣ
//	 * <li>��������Ϣ�������Է���Ϣ�͹�����Ϣ(��˾����)
//	 * @param userPK
//	 * @param corppk
//	 * @param bulletinFilter
//	 * @return
//	 * @throws DbException 
//	 */
//	public ArrayList queryBulletinMsgs(String userPK, String corppk, MessageFilter bulletinFilter)
//			throws DbException {
//		//��������Ϣ�������Է���Ϣ�͹�����Ϣ(��˾����)
//		String bulletinSql = "select a.pk_messageinfo,a.senderman,b.user_name,a.checkman,a.pk_corp,a.type,a.messagestate,a.url,a.title,a.content,a.senddate,a.priority,a.dealdate,"
//				+ "a.billid,a.billno,a.pk_billtype,a.pk_srcbilltype,a.actiontype,a.titlecolor,a.filecontent "
//				+ "from pub_messageinfo a left join sm_user b on a.senderman=b.cuserid "
//				+ "where ((checkman=? and a.type="
//				+ MessageTypes.MSG_TYPE_P2P
//				+ ") or (a.type="
//				+ MessageTypes.MSG_TYPE_PUBLIC
//				+ " and a.messagestate<>"
//				+ MessageStatus.SEALED.getValue() /* �����ѯ���Ĺ��� */
//				+ " and (a.pk_corp=? or a.pk_corp='0001'))) and (isnull(a.receivedeleteflag,'N')='N') ";
//
//		//����pub_messageinfo���6�������ֶ�
//		setMessageinfoFields(bulletinFilter);
//
//		String filterWhereSql = contactFilterSql(bulletinFilter);
//		String orderSql = " order by senddate desc";
//
//		String sql2 = null;
//		if (filterWhereSql.length() == 0)
//			sql2 = bulletinSql + orderSql;
//		else
//			sql2 = bulletinSql + "and" + filterWhereSql + orderSql;
//		PersistenceManager persist = null;
//		try {
//			persist = PersistenceManager.getInstance();
//			JdbcSession jdbc = persist.getJdbcSession();
//			SQLParameter para = new SQLParameter();
//			para.addParam(userPK);
//			para.addParam(corppk);
//			ArrayList aryMsgInfo = (ArrayList) jdbc.executeQuery(sql2, para, new MessageInfoProcessor());
//
//			//��MessageinfoVO����ת��ΪMessageVO����
//			ArrayList aryResult = new ArrayList();
//			for (int i = 0; i < aryMsgInfo.size(); i++) {
//				MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO((MessageinfoVO) aryMsgInfo.get(i));
//				aryResult.add(msgvo);
//			}
//			return aryResult;
//		} finally {
//			if (persist != null)
//				persist.release();
//		}
//	}
//
//	private void setMessageinfoFields(MessageFilter filter) {
//		filter.setStatusField("a.messagestate");
//		filter.setPriorityField("a.priority");
//		filter.setSenderField("a.senderman");
//		filter.setSenddateField("a.senddate");
//		filter.setDealdateField("a.dealdate");
//		filter.setTitleField("a.title");
//		filter.setBilltypeField("a.pk_billtype");
//	}
//
//	/**
//	 * ���ݹ�������ƴ�ն����Where����
//	 * @param filter
//	 * @return
//	 */
//	public static String contactFilterSql(MessageFilter filter) {
//		String whereSql = "";
//		boolean bNeedAnd = false;
//
//		/**
//		 * ״̬:0:���� 1:δ���� 2:�Ѵ���
//		 */
//		if (filter.getStatus() == 1) {
//			if (filter.isWorkflownoteFiltered())
//				whereSql += " " + filter.getStatusField() + "="
//						+ WfTaskOrInstanceStatus.Started.getIntValue() + " and ischeck='N'";
//			else
//				whereSql += " " + filter.getStatusField() + "=" + MessageStatus.STATE_NOT_CHECK;
//			bNeedAnd = true;
//		} else if (filter.getStatus() == 2) {
//			if (filter.isWorkflownoteFiltered())
//				whereSql += " (" + filter.getStatusField() + "="
//						+ WfTaskOrInstanceStatus.Finished.getIntValue() + " or ischeck='Y')";
//			else
//				whereSql += " " + filter.getStatusField() + "=" + MessageStatus.STATE_CHECKED;
//			bNeedAnd = true;
//		}
//
//		/**
//		 * ���ȼ�:-1,0,1
//		 */
//		if (filter.getPriorityOne() != -2 && filter.getPriorityTwo() != -2) {
//			if (bNeedAnd)
//				whereSql += " and (" + filter.getPriorityField() + "=" + filter.getPriorityOne() + " or "
//						+ filter.getPriorityField() + "=" + filter.getPriorityTwo() + ")";
//			else {
//				whereSql += " (" + filter.getPriorityField() + "=" + filter.getPriorityOne() + " or "
//						+ filter.getPriorityField() + "=" + filter.getPriorityTwo() + ")";
//				bNeedAnd = true;
//			}
//		} else if (filter.getPriorityOne() != -2) {
//			if (bNeedAnd)
//				whereSql += " and " + filter.getPriorityField() + "=" + filter.getPriorityOne();
//			else {
//				whereSql += " " + filter.getPriorityField() + "=" + filter.getPriorityOne();
//				bNeedAnd = true;
//			}
//		} else if (filter.getPriorityTwo() != -2) {
//			if (bNeedAnd)
//				whereSql += " and " + filter.getPriorityField() + "=" + filter.getPriorityTwo();
//			else {
//				whereSql += " " + filter.getPriorityField() + "=" + filter.getPriorityTwo();
//				bNeedAnd = true;
//			}
//		}
//
//		/**
//		 * ��������
//		 */
//		if (filter.isFilterSendDate()) {
//			String sendDateBegin = filter.getSendDateBegin();
//			String sendDateEnd = filter.getSendDateEnd();
//
//			if (!StringUtil.isEmpty(sendDateBegin) && !StringUtil.isEmpty(sendDateEnd)) {
//				sendDateEnd += " 24:00:00";
//				if (bNeedAnd)
//					whereSql += " and " + filter.getSenddateField() + "<='" + sendDateEnd + "' and "
//							+ filter.getSenddateField() + ">='" + sendDateBegin + "'";
//				else {
//					whereSql += " " + filter.getSenddateField() + "<='" + sendDateEnd + "' and "
//							+ filter.getSenddateField() + ">='" + sendDateBegin + "'";
//					bNeedAnd = true;
//				}
//			} else if (!StringUtil.isEmpty(sendDateBegin)) {
//				if (bNeedAnd)
//					whereSql += " and " + filter.getSenddateField() + ">='" + sendDateBegin + "'";
//				else {
//					whereSql += " " + filter.getSenddateField() + ">='" + sendDateBegin + "'";
//					bNeedAnd = true;
//				}
//			} else if (!StringUtil.isEmpty(sendDateEnd)) {
//				sendDateEnd += " 24:00:00";
//				if (bNeedAnd)
//					whereSql += " and " + filter.getSenddateField() + "<='" + sendDateEnd + "'";
//				else {
//					whereSql += " " + filter.getSenddateField() + "<='" + sendDateEnd + "'";
//					bNeedAnd = true;
//				}
//			}
//		}
//		/**
//		 * ��������
//		 */
//		if (filter.isFilterDealDate()) {
//			String dealDateBegin = filter.getDealDateBegin();
//			String dealDateEnd = filter.getDealDateEnd();
//			if (!StringUtil.isEmpty(dealDateBegin) && !StringUtil.isEmpty(dealDateEnd)) {
//				dealDateEnd += " 24:00:00";
//				if (bNeedAnd)
//					whereSql += " and " + filter.getDealdateField() + "<='" + dealDateEnd + "' and "
//							+ filter.getDealdateField() + ">='" + dealDateBegin + "'";
//				else {
//					whereSql += " " + filter.getDealdateField() + "<='" + dealDateEnd + "' and "
//							+ filter.getDealdateField() + ">='" + dealDateBegin + "'";
//					bNeedAnd = true;
//				}
//			} else if (!StringUtil.isEmpty(dealDateBegin)) {
//				if (bNeedAnd)
//					whereSql += " and " + filter.getDealdateField() + ">='" + dealDateBegin + "'";
//				else {
//					whereSql += " " + filter.getDealdateField() + ">='" + dealDateBegin + "'";
//					bNeedAnd = true;
//				}
//			} else if (!StringUtil.isEmpty(dealDateEnd)) {
//				dealDateEnd += " 24:00:00";
//				if (bNeedAnd)
//					whereSql += " and " + filter.getDealdateField() + "<='" + dealDateEnd + "'";
//				else {
//					whereSql += " " + filter.getDealdateField() + "<='" + dealDateEnd + "'";
//					bNeedAnd = true;
//				}
//			}
//		}
//
//		/**
//		 * ������
//		 */
//		if (filter.isFilterSender()) {
//			if (bNeedAnd)
//				whereSql += " and " + filter.getSenderField() + "='" + filter.getSender() + "'";
//			else {
//				whereSql += " " + filter.getSenderField() + "='" + filter.getSender() + "'";
//				bNeedAnd = true;
//			}
//		}
//
//		/**
//		 * ��������
//		 */
//		if (filter.isFilterBilltype()) {
//			if (bNeedAnd)
//				whereSql += " and " + filter.getBilltypeField() + "='" + filter.getBilltype() + "'";
//			else {
//				whereSql += " " + filter.getBilltypeField() + "='" + filter.getBilltype() + "'";
//				bNeedAnd = true;
//			}
//		}
//
//		/**
//		 * ����
//		 */
//		if (filter.isFilterTitle()) {
//			String title = filter.getTitle();
//			// ���˿հ��ַ�--֧�ֶ�ؼ���ˢѡ
//			String[] titles = title.split("\\s+");
//			String tsql = "";
//			for (int i = 0; i < (titles == null ? 0 : titles.length); i++) {
//				tsql += " or " + filter.getTitleField() + " like '%" + titles[i] + "%'";
//			}
//			if (tsql != null) {
//				if (bNeedAnd)
//					whereSql += " and (" + tsql.substring(3) + ")";
//				else
//					whereSql += tsql.substring(3);
//			}
//		}
//		return whereSql;
//	}
//
//	/**
//	 * @deprecated pub_messageinfo���Ѿ�ɾ��
//	 * ����ɸѡ����ȡ����������ʽ��Ϣ
//	 * @param userPK
//	 * @param corppk
//	 * @param worklistFilter
//	 * @return
//	 * @throws DbException 
//	 */
//	public ArrayList queryPushAndPullMsgs(String userPK, MessageFilter worklistFilter)
//			throws DbException {
//
//		//������������Ϣ
//		String worklistSql = "select a.pk_messageinfo,a.senderman,b.user_name,a.checkman,a.pk_corp,a.type,a.messagestate,a.url,a.title,a.content,a.senddate,a.priority,a.dealdate,"
//				+ "a.billid,a.billno,a.pk_billtype,a.pk_srcbilltype,a.actiontype,a.titlecolor,a.filecontent "
//				+ "from pub_messageinfo a left join sm_user b on a.senderman=b.cuserid "
//				+ "where checkman=? and (a.type in ("
//				+ MessageTypes.MSG_TYPE_BUSIFLOW_PUSH
//				+ ","
//				+ MessageTypes.MSG_TYPE_BUSIFLOW_PULL
//				+ ","
//				+ MessageTypes.MSG_TYPE_BUSIFLOW
////				+ ","
////				+ MessageTypes.MSG_TYPE_INFO
//				+ ")) and (isnull(a.receivedeleteflag,'N')='N') ";
//
//		//����pub_messageinfo���6�������ֶ�
//		setMessageinfoFields(worklistFilter);
//
//		String filterWhereSql = MessageInfoDAO.contactFilterSql(worklistFilter);
//		String orderSql = " order by senddate desc";
//		String sql2 = null;
//		if (filterWhereSql.length() == 0)
//			sql2 = worklistSql + orderSql;
//		else
//			sql2 = worklistSql + "and" + filterWhereSql + orderSql;
//
//		PersistenceManager persist = null;
//		try {
//			persist = PersistenceManager.getInstance();
//			JdbcSession jdbc = persist.getJdbcSession();
//			SQLParameter para = new SQLParameter();
//			para.addParam(userPK);
//			//para.addParam(corppk);
//			ArrayList aryMsgInfo = (ArrayList) jdbc.executeQuery(sql2, para, new MessageInfoProcessor());
//
//			//��MessageinfoVO����ת��ΪMessageVO����
//			ArrayList aryResult = new ArrayList();
//			for (int i = 0; i < aryMsgInfo.size(); i++) {
//				MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO((MessageinfoVO) aryMsgInfo.get(i));
//				aryResult.add(msgvo);
//			}
//			return aryResult;
//		} finally {
//			if (persist != null)
//				persist.release();
//		}
//
//	}
//	
//	/**
//	 * ���ݹ�������ȡԤ��������Ϣ
//	 * @param userPK
//	 * @param corppk
//	 * @param paFilter
//	 * @return
//	 * @throws DbException 
//	 */
//	public ArrayList queryPaMsgs(String userPK, String corppk, MessageFilter paFilter)
//			throws DbException {
//		//Ԥ����Ϣ
//		String paSql = "select a.pk_messageinfo,a.senderman,b.user_name,a.checkman,a.pk_corp,a.type,a.messagestate,a.url,a.title,a.content,a.senddate,a.priority,a.dealdate,"
//				+ "a.billid,a.billno,a.pk_billtype,a.pk_srcbilltype,a.actiontype,a.titlecolor,a.filecontent "
//				+ "from pub_messageinfo a left join sm_user b on a.senderman=b.cuserid "
//				+ "where (checkman=? and a.type in("
//				+ MessageTypes.MSG_TYPE_PA
//				+ ","
//				+ MessageTypes.MSG_TYPE_BG
//				+ ")"
//				+ ") and (isnull(a.receivedeleteflag,'N')='N') ";
//
//		//����pub_messageinfo���6�������ֶ�
//		setMessageinfoFields(paFilter);
//
//		String filterWhereSql = contactFilterSql(paFilter);
//		String orderSql = " order by senddate desc";
//
//		String sql2 = null;
//		if (filterWhereSql.length() == 0)
//			sql2 = paSql + orderSql;
//		else
//			sql2 = paSql + "and" + filterWhereSql + orderSql;
//
//		PersistenceManager persist = null;
//		try {
//			persist = PersistenceManager.getInstance();
//			JdbcSession jdbc = persist.getJdbcSession();
//			SQLParameter para = new SQLParameter();
//			para.addParam(userPK);
//			//para.addParam(corppk);
//			ArrayList<MessageinfoVO> aryMsgInfo = (ArrayList<MessageinfoVO>) jdbc.executeQuery(sql2,
//					para, new MessageInfoProcessor());
//
//			//��MessageinfoVO����ת��ΪMessageVO����
//			ArrayList<MessageVO> aryResult = new ArrayList<MessageVO>();
//			for (int i = 0; i < aryMsgInfo.size(); i++) {
//				MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO(aryMsgInfo.get(i));
//				aryResult.add(msgvo);
//			}
//			return aryResult;
//		} finally {
//			if (persist != null)
//				persist.release();
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param fileName ������
//	 * @return ���ش���ָ��������������ϢPK
//	 * @throws DbException
//	 */
//	public ArrayList queryMsgsByAttach(String fileName) throws DbException {
//		String sqlCon = "url = '" + fileName + "'";
//		PersistenceManager persist = null;
//		try {
//			persist = PersistenceManager.getInstance();
//			ArrayList alPks = (ArrayList) persist.retrieveByClause(MessageinfoVO.class, sqlCon,
//					new String[] { "pk_messageinfo" });
//			return alPks;
//		} finally {
//			if (persist != null)
//				persist.release();
//		}
//
//	}
//
//	public ArrayList queryInfoMsgs(String userPK, String pk_group, MessageFilter worklistFilter)throws DbException {
//		//֪ͨ��Ϣ
//		String worklistSql = "select a.pk_messageinfo,a.senderman,b.user_name,a.checkman,a.pk_corp,a.type,a.messagestate,a.url,a.title,a.content,a.senddate,a.priority,a.dealdate,"
//				+ "a.billid,a.billno,a.pk_billtype,a.pk_srcbilltype,a.actiontype,a.titlecolor,a.filecontent,a.needflowcheck,a.pk_wf_msg,a.pk_wf_task "
//				+ "from pub_messageinfo a left join sm_user b on a.senderman=b.cuserid "
//				+ "where checkman=? and (a.type ="
//				+ MessageTypes.MSG_TYPE_INFO
//				+ ") and (isnull(a.receivedeleteflag,'N')='N') ";///
//
//		//����pub_messageinfo���6�������ֶ�
//		setMessageinfoFields(worklistFilter);
//
//		String filterWhereSql = MessageInfoDAO.contactFilterSql(worklistFilter);
//		String orderSql = " order by senddate desc";
//		String sql2 = null;
//		if (filterWhereSql.length() == 0)
//			sql2 = worklistSql + orderSql;
//		else
//			sql2 = worklistSql + "and" + filterWhereSql + orderSql;
//
//		PersistenceManager persist = null;
//		try {
//			persist = PersistenceManager.getInstance();
//			JdbcSession jdbc = persist.getJdbcSession();
//			SQLParameter para = new SQLParameter();
//			para.addParam(userPK);
//			//para.addParam(corppk);
//			ArrayList aryMsgInfo = (ArrayList) jdbc.executeQuery(sql2, para, new MessageInfoProcessor());
//
//			//��MessageinfoVO����ת��ΪMessageVO����
//			ArrayList aryResult = new ArrayList();
//			for (int i = 0; i < aryMsgInfo.size(); i++) {
//				MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO((MessageinfoVO) aryMsgInfo.get(i));
//				aryResult.add(msgvo);
//			}
//			return aryResult;
//		} finally {
//			if (persist != null)
//				persist.release();
//		}
//	}

}
