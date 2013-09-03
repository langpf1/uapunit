package uap.workflow.app.message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.pub.app.message.vo.CommonMessageVO;
import uap.workflow.pub.app.message.vo.MessageStatus;
import uap.workflow.pub.app.message.vo.MessageTypes;
import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.pub.app.message.vo.UserNameObject;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.UserVO;


/**
 * ��Ϣ���������pub_workflownote �����ݷ�����
 * 
 * @author ���ھ� 2001-4-22 9:49:34
 * @modifier leijun 2005-10-10 ���䵽NC5
 * @modifier leijun 2005-12-29 �����Ӿ�����ʹ����������
 */
public class PFMessageDMO {

	public PFMessageDMO() {
		super();
	}

	/**
	 * ����������������ġ�����ɾ����־������Ϊ��ɾ����̬
	 * 
	 * @param pks
	 * @return
	 * @throws DbException
	 * @deprecated �����ã�
	 */
	public void delReceivedMessages(String[] pks) throws DbException {
		if (pks == null || pks.length == 0)
			return;
		String sql = "update pub_workflownote set receivedeleteflag='Y' where approvestatus!="
				+ TaskInstanceStatus.Started.getIntValue() + " and pk_checkflow=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			SQLParameter para = new SQLParameter();
			for (int i = 0; i < pks.length; i++) {
				para.clearParams();
				para.addParam(pks[i]);
				jdbc.addBatch(sql, para);
			}
			jdbc.executeBatch();

		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ����ĳ��˾�ڵ������û���������������
	 * 
	 * @param pkCorp
	 * @return
	 * @throws DbException
	 */
	public UserNameObject[] getAllUser(String pkCorp) throws BusinessException {// DbException
		// {
		// ���Ʊ�2006-02-23�޸ģ�������ʹ��sm_userandcorp���ʹ��sm_user_role����Ϊ����IUserManageQuery��queryAllUsersByRight
		IUserManageQuery iUserQry = (IUserManageQuery) NCLocator.getInstance().lookup(
				IUserManageQuery.class.getName());
		UserVO[] users = iUserQry.queryAllUsersByOrg(pkCorp);
		if (users == null || users.length == 0)
			return null;
		UserNameObject[] ret = new UserNameObject[users.length];
		for (int i = 0; i < users.length; i++) {
			ret[i] = new UserNameObject(users[i].getUser_name());
			ret[i].setUserPK(users[i].getPrimaryKey());
			ret[i].setUserCode(users[i].getUser_code());
		}
		return ret;
	}

	class Workflownote2MessageVOProcessor extends BaseProcessor {

		//������PK
		private String checkman = null;

		public Workflownote2MessageVOProcessor(String userPK) {
			super();
			checkman = userPK;
		}

		protected final String getSelectSql() {
			String sql = "select a.pk_checkflow,a.pk_billtype,a.billno,a.actiontype,a.senderman,"
					+ "c.user_name,a.ischeck,a.checknote,a.senddate,a.dealdate,a.messagenote,a.pk_org,a.billid,a.priority,a.approvestatus,a.userobject,a.workflow_type"
					+ " from pub_workflownote a left join sm_user c on a.senderman=c.cuserid";
			return sql;
		}

		@Override
		public Object processResultSet(ResultSet rs) throws SQLException {

			ArrayList al = new ArrayList();
			while (rs.next()) {
				MessageVO msgvo = new MessageVO();
				msgvo.setCheckerCode(checkman);
				//pk_checkflow
				String key = rs.getString(1);
				msgvo.setPrimaryKey(key == null ? null : key.trim());
				//pk_billtype & msgtype
				String billTypeCode = rs.getString(2);
				msgvo.setPk_billtype(billTypeCode.trim());
				msgvo.setMsgType(MessageTypes.MSG_TYPE_APPROVE);
				//billno
				String billNO = rs.getString(3);
				msgvo.setBillNO(billNO == null ? null : billNO.trim());
				//actiontype
				String actionTypeCode = rs.getString(4);
				msgvo.setActionTypeCode(actionTypeCode == null ? null : actionTypeCode.trim());
				//senderman
				String senderPK = rs.getString(5);
				msgvo.setSenderCode(senderPK == null ? null : senderPK.trim());
				//user_name
				String senderName = rs.getString(6);
				msgvo.setSenderName(senderName == null ? null : senderName.trim());
				//ischeck
				String isCheck = rs.getString(7);
				msgvo.setIsCheck(new UFBoolean(isCheck == null ? "N" : isCheck.trim()));
				//checknote
				String checkNote = rs.getString(8);
				msgvo.setCheckNote(checkNote == null ? null : checkNote.trim());
				//senddate
				String sendDateTime = rs.getString(9);
				msgvo.setSendDateTime(sendDateTime == null ? null : new UFDateTime(sendDateTime.trim()));
				//dealdate
				String dealDateTime = rs.getString(10);
				if (dealDateTime == null) {
					msgvo.setDealDateTime(null);
					//msgvo.setIsCheck(UFBoolean.FALSE);
				} else {
					msgvo.setDealDateTime(new UFDateTime(dealDateTime.trim()));
					//msgvo.setIsCheck(UFBoolean.TRUE);
				}

				//messagenote
				String messageNote = rs.getString(11);
				msgvo.setMessageNote(messageNote == null ? null : messageNote.trim());
				//pk_corp
				String pkCorp = rs.getString(12);
				msgvo.setCorpPK(pkCorp == null ? null : pkCorp.trim());

				//billid
				String billID = rs.getString(13);
				msgvo.setBillPK(billID == null ? null : billID.trim());
				//priority
				int priority = rs.getInt(14);
				msgvo.setPriority(priority);
				//approvestatus
				int approvestatus = rs.getInt(15);
				if (approvestatus == TaskInstanceStatus.Finished.getIntValue())
					//XXX:�����ٴθ�ischeck��ֵ
					msgvo.setIsCheck(UFBoolean.TRUE);
				//userobject XXX:5.5��������
				String userobj = rs.getString(16);
				msgvo.setUserobject(userobj);
				//workflow_type
				int iWfType = rs.getInt(17);
				msgvo.setWorkflowtype(iWfType);

				al.add(msgvo);
			}
			return al;

		}

	}

	/**
	 * ��ѯ����������
	 * <li>����Ϣ��ʱ������������ϴη���ʱ��
	 * <li>û��ɾ����
	 * 
	 * @param userPK �û�����
	 * @param lastAccessTime �ϴη��ʵ�ʱ���
	 * @return
	 * @throws DbException
	 */
	public ArrayList queryWorkitems(String userPK, UFDateTime lastAccessTime) throws DbException {

		Workflownote2MessageVOProcessor wiProcessor = new Workflownote2MessageVOProcessor(userPK);

		String sql = wiProcessor.getSelectSql()
				+ " where a.checkman=? and (isnull(cast(a.approvestatus as char),'~')='~' or a.approvestatus!="
				+ TaskInstanceStatus.Inefficient.getIntValue()
				+ ") and a.ts>=? and a.receivedeleteflag='N'" + " order by a.senddate desc";

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

			ArrayList lResult = (ArrayList) jdbc.executeQuery(sql, para, wiProcessor);
			return lResult;
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ��ѯ��ɾ���� ����ĳ�˵�����������
	 * 
	 * @param userPK
	 * @param lastAccessTime
	 * @return
	 * @throws DbException
	 */
	public ArrayList queryDeletedItems(String userPK, UFDateTime lastAccessTime) throws DbException {

		Workflownote2MessageVOProcessor wiProcessor = new Workflownote2MessageVOProcessor(userPK);

		String sql = wiProcessor.getSelectSql()
				+ " where a.checkman=? and a.ts>=? and a.receivedeleteflag='Y'";

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

			ArrayList lResult = (ArrayList) jdbc.executeQuery(sql, para, wiProcessor);
			return lResult;
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * �����ҵ����Ϣ��������������Ϣ
	 * 
	 * @param msg
	 * @param pkcorp 
	 * @throws DbException
	 */
	public void insertCommonMessage(CommonMessageVO msg, String pkcorp) throws DbException {
		ArrayList al = new ArrayList();
		al.add(msg);
		MessageInfoDAO dao = new MessageInfoDAO();
		dao.insertAryCommonMessage(al, pkcorp);
	}

	/**
	 * ���·�ҵ����Ϣ��������������Ϣ
	 * @author guowl
	 * @param msg
	 * @param pkcorp 
	 * @throws DbException
	 */
	public void updateCommonMessage(MessageVO msg) throws DbException {

		MessageInfoDAO dao = new MessageInfoDAO();
		dao.updateCommonMessage(msg);
	}

	public void insertCommonMessagesAry(ArrayList aryCommonMsg, String pkcorp) throws DbException {
		if (aryCommonMsg != null) {
			MessageInfoDAO dao = new MessageInfoDAO();
			dao.insertAryCommonMessage(aryCommonMsg, pkcorp);
		}
	}

	/**
	 * ����Ϣ��־Ϊ���Ѵ���<br>
	 * Mainly deal the Approve Message.
	 * 
	 * @param checkFlowPK java.lang.String ��Ϣ����
	 * @param dealTime UFDateTime ����ʱ��
	 */
	public void signMessageDeal(String checkFlowPK, UFDateTime dealTime) throws DbException {
		//String sql = "update pub_workflownote set ischeck='Y',approvestatus=?,dealdate=? where pk_checkflow=? ";
		String sql = "update pub_workflownote set ischeck='Y',dealdate=? where pk_checkflow=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			SQLParameter para = new SQLParameter();
			//para.addParam(WfTaskOrInstanceStatus.Finished.getIntValue());
			para.addParam(dealTime.toString());
			para.addParam(checkFlowPK);
			jdbc.executeUpdate(sql, para);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * �����������������־Ϊ��δ���� <br>
	 * Mainly deal the Approve Message.
	 * 
	 * @param checkFlowPK java.lang.String ����
	 * @throws DbException
	 */
	public void signMessageUndeal(String checkFlowPK) throws DbException {
		String sql = "update pub_workflownote set ischeck='N', approvestatus=?,dealdate=null where pk_checkflow=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(TaskInstanceStatus.Started.getIntValue());
			para.addParam(checkFlowPK);
			jdbc.executeUpdate(sql, para);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * �������������¹�����ġ�����ɾ����־�� Ϊ��ɾ����̬��ԭ
	 *  
	 * @param alNotePKs ��������
	 * @param iDeleteOrRestore 0-ɾ��;1-��ԭ
	 */
	public void signWorkitems(ArrayList alNotePKs, int iDeleteOrRestore) throws DbException {
		if (alNotePKs == null || alNotePKs.size() == 0)
			return;
		String sql = "";
		if (iDeleteOrRestore == 0)
			//sql = "update pub_workflownote set receivedeleteflag='Y',dr=1 where pk_checkflow=? ";
			sql = "update pub_workflownote set receivedeleteflag='Y' where pk_checkflow=? ";
		else
			//sql = "update pub_workflownote set receivedeleteflag='N',dr=0 where pk_checkflow=? ";
			sql = "update pub_workflownote set receivedeleteflag='N' where pk_checkflow=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			for (int i = 0; i < alNotePKs.size(); i++) {
				para.clearParams();
				para.addParam(alNotePKs.get(i).toString());
				jdbc.addBatch(sql, para);
			}
			jdbc.executeBatch();
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * �������������¹�����ġ�����ɾ����־�� Ϊ��ɾ����̬ 
	 * @param alInfoPKs ��������
	 * @param iDeleteOrRestore 0-ɾ��;1-��ԭ
	 * @return ����Ϣ�к���Ԥ������Ϣ
	 */
	public ArrayList signMessageinfos(ArrayList alInfoPKs, int iDeleteOrRestore) throws DbException {
		if (alInfoPKs == null || alInfoPKs.size() == 0)
			return null;
		ArrayList aryPAMsg = new ArrayList();
		String sql = "";
		if (iDeleteOrRestore == 0)
			//sql = "update pub_messageinfo set receivedeleteflag='Y',dr=1 where pk_messageinfo=? ";
			sql = "update pub_messageinfo set receivedeleteflag='Y' where pk_messageinfo=? ";
		else
			//sql = "update pub_messageinfo set receivedeleteflag='N',dr=0 where pk_messageinfo=? ";
			sql = "update pub_messageinfo set receivedeleteflag='N' where pk_messageinfo=? ";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			SQLParameter para = new SQLParameter();
			for (int i = 0; i < alInfoPKs.size(); i++) {
				para.clearParams();
				para.addParam(alInfoPKs.get(i).toString());
				jdbc.addBatch(sql, para);
			}
			jdbc.executeBatch();

		} finally {
			if (persist != null)
				persist.release();
		}
		return aryPAMsg;
	}

	/**
	 * Accroding the PK_roles to query the userpks ,and then compose the
	 * UserNameObject[],which format is such as 'pk01','pk02'
	 * 
	 * @deprecated Ӧ���������ķ�����
	 */
	public UserNameObject[] getAllUserByRolePKs(String roles) throws BusinessException {
		IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select distinct cuserid from sm_user_role where pk_role in(" + roles + ")";
		List aryResult = (List) qryBS.executeQuery(sql, new BaseProcessor() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ArrayList ary = new ArrayList();
				while (rs.next()) {
					UserNameObject oneUser = new UserNameObject(null);
					oneUser.setUserPK(rs.getString(1).trim());
					ary.add(oneUser);
				}
				return ary;
			}
		});

		return (UserNameObject[]) aryResult.toArray(new UserNameObject[aryResult.size()]);
	}

	/**
	 * @deprecated  pub_messageinfo �Ѿ�ɾ��
	 * ���ĳҵ������Ϣ����������Ϊ���Ѵ���״̬
	 * @param hsBillId ����ID����
	 * @param checkman ������PK
	 * @param billtype ����������
	 * @param srcBilltype Դ�������� 
	 * @throws DbException 
	 */
	public void completeWorkitem(HashSet hsBillId, String checkman, String billtype,
			String srcBilltype) throws DbException {
		String sql = "update pub_messageinfo set messagestate=?,dealdate=? "
				+ "where billid=? and checkman=? and pk_billtype=? and pk_srcbilltype=? and type in ("
				+ MessageTypes.MSG_TYPE_BUSIFLOW_PUSH + "," + MessageTypes.MSG_TYPE_BUSIFLOW_PULL + ","
				+ MessageTypes.MSG_TYPE_BUSIFLOW + "," + MessageTypes.MSG_TYPE_INFO + ")";

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			for (Iterator iter = hsBillId.iterator(); iter.hasNext();) {
				String billId = (String) iter.next();
				SQLParameter para = new SQLParameter();
				para.addParam(MessageStatus.STATE_CHECKED);
				para.addParam(new UFDateTime(new Date()).toString());

				para.addParam(billId);
				para.addParam(checkman);
				para.addParam(billtype);
				para.addParam(srcBilltype);

				jdbc.addBatch(sql, para);
			}

			jdbc.executeBatch();
		} finally {
			if (persist != null)
				persist.release();
		}
	}

//	private void setWorkflownoteFields(MessageFilter filter) {
//		if (filter.isWorkflownoteFiltered())
//			filter.setStatusField("a.approvestatus");
//		else
//			filter.setStatusField("a.ischeck");
//		filter.setPriorityField("a.priority");
//		filter.setSenderField("a.senderman");
//		filter.setSenddateField("a.senddate");
//		filter.setDealdateField("a.dealdate");
//		filter.setTitleField("a.messagenote");
//		filter.setBilltypeField("a.pk_billtype");
//	}
//
//	/**
//	 * ���ݹ�������ȡ������
//	 * <li>����ͨ������IPFMessage.insertBizMsgs���͵�ҵ����Ϣ
//	 * @param userPK
//	 * @param worklistFilter
//	 * @return
//	 * @throws DbException 
//	 */
//	public ArrayList queryWorkitems(String userPK, MessageFilter worklistFilter) throws DbException {
//
//		Workflownote2MessageVOProcessor wiProcessor = new Workflownote2MessageVOProcessor(userPK);
//
//		String sql = wiProcessor.getSelectSql()
//				+ " where a.checkman=? and (isnull(cast(a.approvestatus as char),'~')='~' or a.approvestatus!="
//				+ WfTaskOrInstanceStatus.Inefficient.getIntValue() + ") and a.receivedeleteflag='N' ";
//
//		String msgrefsql = " and (a.ismsgbind = 'N' or  not exists (select 1 from pub_wfmsgref where pub_wfmsgref.pk_worknote = a.pk_checkflow ))";
//		sql+=msgrefsql;//
//		
//		//����pub_workflownote���6�������ֶ�
//		setWorkflownoteFields(worklistFilter);
//
//		String filterWhereSql = MessageInfoDAO.contactFilterSql(worklistFilter);
//		String orderSql = " order by senddate desc";
//
//		String sql2 = null;
//		if (filterWhereSql.length() == 0)
//			sql2 = sql + orderSql;
//		else
//			sql2 = sql + "and" + filterWhereSql + orderSql;
//
//		PersistenceManager persist = null;
//		try {
//			persist = PersistenceManager.getInstance();
//			JdbcSession jdbc = persist.getJdbcSession();
//			SQLParameter para = new SQLParameter();
//			para.addParam(userPK);
//
//			ArrayList lResult = (ArrayList) jdbc.executeQuery(sql2, para, wiProcessor);
//			return lResult;
//		} finally {
//			if (persist != null)
//				persist.release();
//		}
//
//	}

}