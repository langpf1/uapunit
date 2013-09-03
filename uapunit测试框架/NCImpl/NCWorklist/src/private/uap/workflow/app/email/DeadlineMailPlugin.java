package uap.workflow.app.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.message.PFMessageImpl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.pf.PfMailAndSMSUtil;
import nc.bs.pub.workflowqry.impl.DefaultWorknoteQuery;
import nc.itf.pub.workflowqry.IWorkflowQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IWorkflowAdmin;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pubitf.rbac.IUserPubService;
import nc.ui.pub.print.IDataSource;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.msg.MessageVO;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.sm.UserVO;
import nc.vo.wfengine.core.util.DurationUnit;
import nc.vo.wfengine.core.workflow.BasicWorkflowProcess;
import nc.vo.wfengine.core.workflow.MailModal;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nc.vo.workflow.admin.FlowOverdueVO;

/**
 * 扫描工作项表，实现期限提醒
 * 
 * @author leijun 2007-7-17
 * @modifier yanke1 2011-4-1 使用工作日历计算逾期时间
 */
public class DeadlineMailPlugin implements IPreAlertPlugin {

//	/*
//	 * 用来缓存主流程定义
//	 */
//	EngineService engineService = null;

	@Override
	public PreAlertObject executeTask(PreAlertContext context) throws BusinessException {

		// 取得预警部署的集团
		String group = context.getGroupId();

		Logger.debug(">>工作项超期预警插件开始执行=" + new UFDateTime(System.currentTimeMillis()));

		// 获得所有逾期工作项
		WorkitemTemp[] items = getOverdueWorkitem(group);

		if (items == null || items.length <= 0) {
			// 若没有超期工作项，则不发送任何消息
			// 此处返回PreAlertReturnType.RETURNNOTHING类型的消息
			return returnEmtpyObj();
		} else {
			// 有超期工作项
			return sendMsgToCheckmanAndReturnDataSource(items);
		}
	}

	private PreAlertObject returnEmtpyObj() {
		PreAlertObject obj = new PreAlertObject();
		obj.setReturnType(PreAlertReturnType.RETURNNOTHING);

		return obj;
	}

	private PreAlertObject sendMsgToCheckmanAndReturnDataSource(WorkitemTemp[] items) {

		OverdueWorkitemDatasource ds = new OverdueWorkitemDatasource();

		for (WorkitemTemp item : items) {

			Object[] mailResult = null;

			// 向每个工作项的checkman发送email
			try {
				mailResult = sendEmailToCheckman(item);
			} catch (BusinessException e) {
				Logger.error(e);
				mailResult = new Object[] { false, e.getMessage() };
			}

			ds.add(item, mailResult);
		}

		PreAlertObject obj = new PreAlertObject();

		// TODO i18n
		obj.setMsgTitle(NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0000")/* 工作项超期预警 */);
		obj.setReturnType(PreAlertReturnType.RETURNDATASOURCE);
		obj.setReturnObj(ds);

		return obj;

	}

	class OverdueWorkitemDatasource implements IDataSource {

		private List<String> billno = new ArrayList<String>();
		private List<String> sender = new ArrayList<String>();
		private List<String> senddate = new ArrayList<String>();
		private List<String> checkman = new ArrayList<String>();
		private List<String> overdue = new ArrayList<String>();
		private List<String> msgnote = new ArrayList<String>();
		private List<String> mailresult = new ArrayList<String>();
		private List<String> mailinfo = new ArrayList<String>();

		private Map<String, List<String>> map = new HashMap<String, List<String>>();

		public OverdueWorkitemDatasource() {
			map.put("billno", billno);
			map.put("sender", sender);
			map.put("senddate", senddate);
			map.put("checkman", checkman);
			map.put("overdue", overdue);
			map.put("msgnote", msgnote);
			map.put("mailresult", mailresult);
			map.put("mailinfo", mailinfo);

		}

		public void add(WorkitemTemp item, Object[] mailResult) {
			billno.add(item.getBillno());
			sender.add(getUserNameByPk(item.getSenderman()));
			senddate.add(item.getSenddate().toString());
			checkman.add(getUserNameByPk(item.getCheckman()));
			overdue.add(String.valueOf(item.getOverdue().getOverdueDays()) + DurationUnit.DAY.toString());
			msgnote.add(MessageVO.getMessageNoteAfterI18N(item.getMessagenote()));
			mailresult.add(((Boolean) mailResult[0]) ? NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0001")/* 已发送 */
			: NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0002")/* 未发送 */);
			mailinfo.add(String.valueOf(mailResult[1]));
		}

		private Map<String, UserVO> userMap = new HashMap<String, UserVO>();

		private String getUserNameByPk(String pk) {
			if (!userMap.containsKey(pk)) {
				try {
					UserVO uvo = (UserVO) new BaseDAO().retrieveByPK(UserVO.class, pk);
					userMap.put(pk, uvo);
				} catch (DAOException e) {
					Logger.error(e.getMessage(), e);
					return "";
				}
			}

			UserVO uvo = userMap.get(pk);
			return uvo == null ? "" : uvo.getUser_name();
		}

		@Override
		public String[] getItemValuesByExpress(String itemExpress) {
			List<String> list = map.get(itemExpress);

			return list == null ? new String[] {} : list.toArray(new String[0]);
		}

		@Override
		public boolean isNumber(String itemExpress) {
			return false;
		}

		@Override
		public String[] getDependentItemExpressByExpress(String itemExpress) {
			return null;
		}

		@Override
		public String[] getAllDataItemExpress() {
			return new String[] { "billno", "sender", "senddate", "checkman", "overdue",

			"msgnote", "mailresult", "mailinfo" };
		}

		@Override
		public String[] getAllDataItemNames() {
			return new String[] { NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000477")/* 单据号 */,
					NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000202")/* 发送人 */,
					NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000429")/* 发送日期 */,
					NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000631")/* 审批人 */,
					NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0003")/* 超期时间 */,
					NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0004")/* 说明 */,
					NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0005")/* 邮件发送结果 */,
					NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0006") /* 邮件详细信息 */};
		}

		@Override
		public String getModuleName() {
			return null;
		}

	}

//	private EngineService getEngineService() {
//		if (engineService == null) {
//			engineService = new EngineService();
//		}
//		return engineService;
//	}

	/**
	 * 向逾期工作项的审批人发送email
	 * 
	 * @param wt
	 * @throws BusinessException
	 */
	private Object[] sendEmailToCheckman(WorkitemTemp wt) throws BusinessException {
		try {
			String strCheckman = wt.getCheckman();
			String strBillid = wt.getBillid();
			String strBIllno = wt.getBillno();
			String strBilltype = wt.getPk_billtype();

			String overdue = "";

			String strMessagenote = NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0007", null,
					new String[] { strBIllno, overdue })/*
														 * 超期提醒 : { 0 } 超期 { 1 }
														 */;
			String strActiontype = wt.getActiontype();
			String strProcessDefid = wt.getProcessdefid();

			BasicWorkflowProcess bwp = PfDataCache.getWorkflowProcess(strProcessDefid);

			if (bwp == null) {
				return new Object[] {
						false,
						(NCLangResOnserver.getInstance()
								.getStrByID("pfworkflow", "MailPlugin-0008", null, new String[] { strProcessDefid })/*
																													 * 找不到流程定义
																													 * ：
																													 * {
																													 * 0
																													 * }
																													 */) };
			}

			String strPrintTempletId = "";
			try {
				strPrintTempletId = bwp.getProcessHeader().getMailMobile().getPrintTempletid();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}

			MailModal mailModal = bwp.getMailModal();

			if (MailModal.NO_MAIL_INT == mailModal.getValue() || MailModal.BLANK_INT == mailModal.getValue()) {
				return new Object[] { false, NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0009") /* 该流程没有定义邮件功能 */};
			}

			Logger.debug(">>>发送邮件开始");

			IUserPubService userService = NCLocator.getInstance().lookup(IUserPubService.class);
			String pk_psndoc = userService.queryPsndocByUserid(strCheckman);

			if (StringUtil.isEmptyWithTrim(pk_psndoc)) {
				return new Object[] { false, NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0010") /* 审批人未关联人员档案 */};
			}

			IUAPQueryBS uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			PsndocVO psndoc = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class, pk_psndoc);
			String email = psndoc == null ? null : psndoc.getEmail();
			if (StringUtil.isEmptyWithTrim(email))
				return new Object[] { false, NCLangResOnserver.getInstance().getStrByID("pfworkflow", "MailPlugin-0011") /* 审批人未关联人员档案或所关联人员档案未配置电子邮件地址 */};

			if (MailModal.MAIL_INFO_INT == mailModal.getValue()) {
				// 检查单据类型注册的审批流检查类是否实现了打印数据源获取接口
				String billHtml = new PFMessageImpl().generateBillHtml(strBillid, strBilltype, strPrintTempletId, strCheckman);
				Logger.debug(">邮件地址=" + email);
				PfMailAndSMSUtil.sendEmailsWithoutApprove(billHtml, new String[] { email }, strMessagenote, strBillid);
			} else if (MailModal.MAIL_APPROVE_INT == mailModal.getValue()) {
				// 具有交互功能的邮件，必须单独发给每个用户

				// 检查单据类型注册的审批流检查类是否实现了打印数据源获取接口
				String billHtml = new PFMessageImpl().generateBillHtml(strBillid, strBilltype, strPrintTempletId, strCheckman);
				Logger.debug(">邮件地址=" + email);
				// XXX::对于驳回制单工作项，则不需交互功能的邮件
				if (WorkflownoteVO.WORKITEM_TYPE_MAKEBILL.equals(strActiontype))
					PfMailAndSMSUtil.sendEmailsWithoutApprove(billHtml, new String[] { email }, strMessagenote, strBillid);
				else {
					// 1.获得单据聚合VO
					IPFConfig bsConfig = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
					AggregatedValueObject billVo = bsConfig.queryBillDataVO(strBilltype, strBillid);
					if (billVo == null)
						throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow", "PfEmailSendTask-000001")/*
																																		 * 错误
																																		 * ：
																																		 * 根据单据类型和单据ID获取不到单据聚合VO
																																		 */);

					// 2.获得工作项并设置审批意见
					// XXX:获得指派信息
					Hashtable<String, PfParameterVO> hashBilltypeToParavo = new Hashtable<String, PfParameterVO>();
					PfUtilBaseTools.getVariableValue(strBilltype, IPFActionName.APPROVE + strCheckman, billVo, null, null, null, null,
							null, hashBilltypeToParavo);
					PfParameterVO paraVO = hashBilltypeToParavo.get(strBilltype);

					WorkflownoteVO wfVO = null;//getEngineService().checkUnfinishedWorkitem(paraVO, WorkflowTypeEnum.Approveflow.getIntValue());
					PfMailAndSMSUtil.sendEmailWithApprove(billHtml, email, strBillid, strBilltype, strMessagenote, strCheckman,
							wfVO == null ? null : wfVO.getTaskInfo().getAssignableInfos());
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e);
		}
		Logger.debug(">>>发送邮件结束");

		return new Object[] { true, "" };
	}

	/**
	 * 查询所有逾期工作项
	 * 
	 * @return
	 * @throws DAOException
	 */
	private WorkitemTemp[] getOverdueWorkitem(String pk_group) throws BusinessException {

		// 取得集团下所有未处理工作项
		// String sql =
		// "select a.senddate,a.senderman, a.checkman,a.billid, a.billno, a.pk_billtype,"
		// +
		// " a.messagenote,a.actiontype,b.pk_wf_instance, b.processdefid,b.activitydefid, "
		// + " c.timelimit, c.timeremind, a.pk_org  from pub_workflownote a "
		// + " left join pub_wf_task b on a.pk_wf_task=b.pk_wf_task "
		// +
		// "left outer join pub_wf_ist c on c.pk_wf_instance = b.pk_wf_instance"
		// + " and b.activitydefid = c.activitydefid"
		// + " where approvestatus=0 and pk_billtype <>'"
		// + MessageVO.NOT_BUSINESS_MSG + "' and actiontype<>'"
		// + WorkflownoteVO.WORKITEM_TYPE_BIZ + "'"
		// + (StringUtil.isEmptyWithTrim(pk_group) ? "" : " and a.pk_group='" +
		// pk_group + "'");

		IWorkflowQuery query = new DefaultWorknoteQuery();

		String[] pks = query
				.getPKsByCondition("approvestatus=0 and pk_billtype <>'" + MessageVO.NOT_BUSINESS_MSG + "' and actiontype<>'"
						+ WorkflownoteVO.WORKITEM_TYPE_BIZ + "'"
						+ (StringUtil.isEmptyWithTrim(pk_group) ? "" : " and pk_group='" + pk_group + "'"));

		IWorkflowAdmin admin = NCLocator.getInstance().lookup(IWorkflowAdmin.class);

		Map<String, FlowOverdueVO> overdueMap = admin.getWorknoteOverdueBatch(pks);

		for (String pk : pks) {
			FlowOverdueVO overdue = overdueMap.get(pk);

			if (!overdue.isNeedRemind())
				overdueMap.remove(pk);
		}

		pks = overdueMap.keySet().toArray(new String[0]);
		
		if (pks == null || pks.length == 0) {
			return null;
		}

		StringBuffer where = new StringBuffer();
		for (String pk : pks) {
			where.append(",'");
			where.append(pk);
			where.append("'");
		}
		String sqlWhere = where.substring(1);

		String sql = "select a.senddate,a.senderman, a.checkman,a.billid, a.billno, a.pk_billtype, a.messagenote,a.actiontype, a.pk_checkflow, b.pk_wf_instance, a.pk_org, b.processdefid,b.activitydefid from pub_workflownote a  left join pub_wf_task b on a.pk_wf_task=b.pk_wf_task where a.pk_checkflow in ("
				+ sqlWhere + ")";
		
		
		List<WorkitemTemp> list = (List<WorkitemTemp>) new BaseDAO().executeQuery(sql, new BeanListProcessor(WorkitemTemp.class));
		
		WorkitemTemp[] itemArray =  list.toArray(new WorkitemTemp[0]);
		
		for (WorkitemTemp item : itemArray ) {
			item.setOverdue(overdueMap.get(item.getPk_checkflow()));
		}
		
		return itemArray;

	}

//	/**
//	 * ResultSetProcess，用于查询所有逾期工作项
//	 * 
//	 * @author yanke1
//	 */
//	private class WorkitemTempProcessor extends BaseProcessor {
//
//		@Override
//		public Object processResultSet(ResultSet rs) throws SQLException {
//			List<WorkitemTemp> list = new ArrayList<WorkitemTemp>();
//			while (rs.next()) {
//
//				try {
//					String strSenddate = rs.getString("senddate");
//					String strSenderman = rs.getString("senderman");
//					String strCheckman = rs.getString("checkman");
//					String strBillid = rs.getString("billid");
//					String strBillNo = rs.getString("billno");
//					String strBilltype = rs.getString("pk_billtype");
//					String strMessagenote = rs.getString("messagenote");
//					String strActiontype = rs.getString("actiontype");
//
//					String strProcInstPK = rs.getString("pk_wf_instance");
//					String strProcessDefid = rs.getString("processdefid");
//					String activityDefId = rs.getString("activitydefid");
//
//					String pk_org = rs.getString("pk_org");
//
//					WorkitemTemp wt = new WorkitemTemp();
//					wt.setBillId(strBillid);
//					wt.setBillNo(strBillNo);
//					wt.setBilltype(strBilltype);
//					wt.setSenderman(strSenderman);
//					wt.setCheckman(strCheckman);
//					wt.setMessagenote(strMessagenote);
//					wt.setActiontype(strActiontype);
//					wt.setProcessDefId(strProcessDefid);
//					wt.setProcInstPK(strProcInstPK);
//					wt.setSendDate(strSenddate);
//					wt.setActivityDefId(activityDefId);
//					wt.setPk_org(pk_org);
//
//					list.add(wt);
//				} catch (Exception e) {
//					Logger.error(e.getMessage(), e);
//				}
//			}
//			return list;
//		}
//	}

	/*
	 * 临时类，用来保存处理超期任务需要的信息
	 */
	public static class WorkitemTemp {

		String senderman;

		String checkman;

		String billid;

		String billoo;

		String pk_billtype;

		String messagenote;

		String actiontype;

		String pk_wf_instance;
		
		String pk_checkflow;

		/* 流程定义id */
		String processdefid;

		/* 活动定义id */
		String activitydefid;

		UFDateTime senddate;

		// yanke1+2011-4-1
		String pk_org;

		// yanke1+ 2011-5-9
		FlowOverdueVO overdue;

		public String getSenderman() {
			return senderman;
		}

		public void setSenderman(String senderman) {
			this.senderman = senderman;
		}

		public String getCheckman() {
			return checkman;
		}

		public void setCheckman(String checkman) {
			this.checkman = checkman;
		}

		public String getBillid() {
			return billid;
		}

		public void setBillid(String billid) {
			this.billid = billid;
		}

		public String getBillno() {
			return billoo;
		}

		public void setBilloo(String billoo) {
			this.billoo = billoo;
		}

		public String getPk_billtype() {
			return pk_billtype;
		}

		public void setPk_billtype(String pk_billtype) {
			this.pk_billtype = pk_billtype;
		}

		public String getMessagenote() {
			return messagenote;
		}

		public void setMessagenote(String messagenote) {
			this.messagenote = messagenote;
		}

		public String getActiontype() {
			return actiontype;
		}

		public void setActiontype(String actiontype) {
			this.actiontype = actiontype;
		}

		public String getPk_wf_instance() {
			return pk_wf_instance;
		}

		public void setPk_wf_instance(String pk_wf_instance) {
			this.pk_wf_instance = pk_wf_instance;
		}

		public String getProcessdefid() {
			return processdefid;
		}

		public void setProcessdefid(String processdefid) {
			this.processdefid = processdefid;
		}

		public String getActivitydefid() {
			return activitydefid;
		}

		public void setActivitydefid(String activitydefid) {
			this.activitydefid = activitydefid;
		}

		public UFDateTime getSenddate() {
			return senddate;
		}

		public void setSenddate(UFDateTime senddate) {
			this.senddate = senddate;
		}

		public String getPk_org() {
			return pk_org;
		}

		public void setPk_org(String pk_org) {
			this.pk_org = pk_org;
		}

		public FlowOverdueVO getOverdue() {
			return overdue;
		}

		public void setOverdue(FlowOverdueVO overdue) {
			this.overdue = overdue;
		}

		public String getPk_checkflow() {
			return pk_checkflow;
		}

		public void setPk_checkflow(String pk_checkflow) {
			this.pk_checkflow = pk_checkflow;
		}

		public String getBilloo() {
			return billoo;
		}

	}
}
