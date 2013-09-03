package uap.workflow.app.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.billtype2.ExtendedClassEnum;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.UserRoleVO;
import uap.workflow.app.impl.ActionEnvironment;
import uap.workflow.app.notice.NoticeReceiverType;
import uap.workflow.app.notice.ReceiverVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.notice.IPfMsgCustomReceiver;
import uap.workflow.pub.app.notice.PfSysVariable;
import uap.workflow.pub.app.notice.SysVariableValueGetter;

/**
 * @author chengsc
 * 
 */
public class MsgReceiverUtil {

	// /**
	// * @param task
	// * @param msginfo
	// * @throws BusinessException
	// */
	// public String insertFlowMsg(WFActivityContext ctx, String pk_wf_instace,
	// String defid, String pk_wf_task, String runnerID, UfFlowMessageEx
	// msginfo) throws BusinessException {
	//
	// try {
	//
	// BaseDAO dao = new BaseDAO();
	// SequenceGenerator sg = new SequenceGenerator();
	// WorkflowMsgVO msg = new WorkflowMsgVO();
	// msg.setRunnerid(runnerID);
	// msg.setPk_wf_msg(sg.generate());
	// msg.setActivitydefid(defid);
	// msg.setPk_wf_instance(pk_wf_instace);
	// msg.setContent(msginfo.getMsgcontent().getMessage());
	// msg.setPk_wf_task(pk_wf_task);
	// msg.setIsCheck(0);
	// msg.setBilltype(ctx.getCurrentTask().getWorknoteVO().getPk_billtype());
	// msg.setSenddate(new UFDateTime(System.currentTimeMillis()));
	// msg.setIsbind("N");
	// String pk = dao.insertVO(msg);
	//
	// return pk;
	//
	// } catch (Exception e) {
	// throw new BusinessException(e);//
	// }
	//
	// }
	//
	// /**
	// * @param pk_wf_task
	// * @param checknote
	// * @param senderman 发送人
	// * @throws BusinessException
	// */
	// private void procFlowByMsg(String pk_wf_task, String checknote,String
	// senderman) throws BusinessException {
	// String billid=null;
	// try {
	// //2011-7-4 wcj add checkman
	// Collection workNote = new
	// BaseDAO().retrieveByClause(WorkflownoteVO.class, " pk_wf_task = '" +
	// pk_wf_task + "' and checkman='" + senderman + "'");
	// WorkflownoteVO worknoteVO = (WorkflownoteVO) workNote.toArray(new
	// WorkflownoteVO[0])[0];
	// WFTask task = getTaskByTaskPk(pk_wf_task, worknoteVO);
	// // task.setAutoCompleted(true);
	// task.setMessageGenerated(true);
	// task.setSenderman(senderman);
	// task.setTopic(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
	// "wfRunner-0000")/*签收消息*/);
	// worknoteVO.setChecknote(checknote);
	// billid =task.getBillID();
	// PfParameterVO paraVo = fetchParaVO(task.getBillType(),task.getBillID());
	// // 1.获得单据聚合VO
	// IPFConfig bsConfig = (IPFConfig)
	// NCLocator.getInstance().lookup(IPFConfig.class.getName());
	// AggregatedValueObject billVo =
	// bsConfig.queryBillDataVO(task.getBillType(), task.getBillID());
	// if (billVo == null)
	// throw new
	// PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
	// "PfEmailSendTask-000001")/*错误：根据单据类型和单据ID获取不到单据聚合VO*/);
	// paraVo.m_preValueVo = billVo;
	// paraVo.m_billType = task.getBillType();
	// paraVo.m_billNo = task.getBillNO();
	// paraVo.m_billId = task.getBillID();
	// paraVo.m_pkOrg = task.getPk_org();
	// paraVo.m_operator = senderman;
	// paraVo.m_workFlow = worknoteVO;
	// paraVo.m_pkGroup = InvocationInfoProxy.getInstance().getGroupId();
	//
	// ActionEnvironment.getInstance().putParaVo(billid,paraVo);
	// WfTaskManager.getInstance().acceptTaskFromBusi(task);
	// EngineService engine = new EngineService();
	// int status = -10;
	// int iCurrentWfType = worknoteVO.getWorkflow_type();
	// status = engine.queryFlowStatus(paraVo.m_billId, paraVo.m_billType,
	// iCurrentWfType, worknoteVO.getApproveresult());
	// if (status == IPfRetCheckInfo.PASSING) {
	// // 流程结束后将所有未完成的活动实例置为无效
	// engine.updateUnfinishedActInstancesToInefficient(
	// task.getBillID(), task.getBillType(),
	// iCurrentWfType);
	// worknoteVO.setActiontype(IPFActionName.TERMINATE);
	//
	// ///取流程实例的制单人
	// if(task.getWfProcessInstancePK() != null) {
	// EnginePersistence persistenceDmo = new EnginePersistence();
	// ProcessInstance instance;
	// try {
	// instance =
	// persistenceDmo.loadProcessInstance(task.getWfProcessInstancePK());
	// paraVo.m_makeBillOperator = instance.getBillMaker();
	// } catch (DbException e) {
	// Logger.error(e.getMessage(), e);
	// }
	// }
	// // 如果审批通过,则给制单人发送非业务消息
	// WorknoteManager noteMgr = new WorknoteManager();
	// String msgStr = "";
	// msgStr = WorkflowTypeEnum.isWorkflowInstance(iCurrentWfType) ?
	// "{UPPpfworkflow-000701}"/* "工作流处理完毕" */
	// : "{UPPpfworkflow-000273}"/* "审批通过" */;
	// noteMgr.sendMessageToBillMaker(msgStr, paraVo);
	//
	// }
	// //虽说下面的语句在没有设置worknoteVO.setActiontype(IPFActionName.TERMINATE);没有效果，也应该这么写
	// //因为消息签收也要回写单据状态信息。
	// AbstractBusiStateCallback absc = new PFBusiStateOfMeta();
	// absc.execApproveState(paraVo, status);
	// } catch (Exception e) {
	// throw new BusinessException(e);
	// }finally{
	// ActionEnvironment.getInstance().putParaVo(billid,null);
	// }
	// }
	//
	// private PfParameterVO fetchParaVO(String billtype,String billid) throws
	// BusinessException{
	// PfParameterVO paramvo =new PfParameterVO();
	// BilltypeVO vo =PfDataCache.getBillType(billtype);
	// String component_name =vo.getComponent();
	// IComponent componet
	// =MDBaseQueryFacade.getInstance().getComponentByName(component_name);
	// //String ref_param
	// =componet.getPrimaryBusinessEntity().getBizInterfaceMapInfo("nc.itf.uap.pf.metadata.IFlowBizItf").get("billid");
	// //String sql =ref_param+"= '"+billid+"' ";
	// try {
	// //NCObject[]
	// objs=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class).queryBillOfNCObjectByCond(Class.forName(componet.getPrimaryBusinessEntity().getFullClassName()),
	// sql, false);
	// NCObject
	// obj=NCLocator.getInstance().lookup(IMDPersistenceQueryService.class).queryBillOfNCObjectByPK(Class.forName(componet.getPrimaryBusinessEntity().getFullClassName()),
	// billid);
	// if(obj!=null){
	// paramvo.ncobject=obj;
	// }
	// } catch (ClassNotFoundException e) {
	// Logger.error(e.getMessage(), e);
	// }
	// return paramvo;
	// }
	//
	//
	// public void sendMessage(WFTask task) throws BusinessException {
	// WFActivityContext wfActContext = new WFActivityContext(task);
	// UfFlowMessageEx msg = (UfFlowMessageEx)
	// wfActContext.getCurrentActivity();
	// if (task.isMessageGenerated()){
	// //2011-7-12 wcj ，需要设置消息标题和内容，因为标题格式不一致，暂时这样改，需要在考虑
	// if (task.isMessageGenerated()){ //如果是签收消息
	// String title = msg.getMsgcontent().getTitle();
	// if (title.isEmpty()) msg.setMsgtitle(task.getTopic());
	// String content = msg.getMessage();
	// if (content.isEmpty()) msg.setMessage(task.getTopic());
	// }
	// }
	// Collection<PfpluginVO> coPlugins = msg.getFlowMsgConfig().values();
	// if (coPlugins.size() == 0) {
	// Logger.debug("该活动没有配置插件，无需执行。活动名称=" +
	// wfActContext.getCurrentActivity().getName());
	// // return;
	// }
	// // String pk = insertFlowMsg(wfActContext,
	// wfActContext.getWfProcessInstancePK(), msg.getId(), task.getTaskPK(),
	// task.getRunnerid(), msg);
	// FlowMsgControlItem item = new FlowMsgControlItem();
	// item.setNeedCheck(msg.isNeedCheck());
	// // item.setPk_wf_msg(pk);
	// item.setPk_wf_task(task.getTaskPK());
	//
	// PluginContext pc = new PluginContext();
	// pc.setMsgCtrlItem(item);
	// pc.setCoPlugins(coPlugins);
	// pc.setParavo(ActionEnvironment.getInstance().getParaVo(wfActContext.getCurrentTask().getBillID()));
	// pc.setTask(wfActContext.getCurrentTask());
	// new PluginActionExecute(pc).doPluginAction();
	// }
	//
	// private WFTask getTaskByTaskPk(String pk_wf_task, WorkflownoteVO notevo)
	// throws BusinessException {
	// try {
	// WFTask task = new TaskManagerDMO().getTaskByPK(pk_wf_task);
	// task.setWorknoteVO(notevo);
	// task.setStatus(WfTaskOrInstanceStatus.Finished.getIntValue());
	// task.setBillType(task.getWorknoteVO().getPk_billtype());
	// task.setModifyTime(new
	// UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
	// task.setBillID(task.getWorknoteVO().getBillid());
	// task.setBillNO(task.getWorknoteVO().getBillno());//
	// task.setOperator(task.getWorknoteVO().getCheckman());
	//
	// return task;
	// } catch (Exception e) {
	// throw new BusinessException(e);
	// }
	// }
	//
	// /**
	// * @param pk_user
	// * @param pk_wf_task
	// * @param pk_wf_msg
	// * @throws BusinessException
	// */
	//
	// @SuppressWarnings("unchecked")
	// private boolean procMsg(String pk_user, String pk_wf_task, String
	// pk_wf_msg,String pk_billtype) throws BusinessException {
	// PersistenceManager persist = null;
	// try {
	// persist = PersistenceManager.getInstance();
	// JdbcSession jdbc = persist.getJdbcSession();
	// String msgsql =
	// " select pk_checkflow from pub_workflownote where pk_wf_task = ? ";
	// SQLParameter para = new SQLParameter();
	// para.addParam(pk_wf_task);
	//
	// ArrayList<String> worknotePks = (ArrayList<String>)
	// jdbc.executeQuery(msgsql, para, new ColumnListProcessor());
	//
	// if(worknotePks != null && worknotePks.size() > 0)
	// return true;
	//
	// return false;
	// } catch (Exception e) {
	// throw new BusinessException(e);
	// } finally {
	// if (persist != null) {
	// persist.release();
	// }
	// }
	// }
	//
	// @SuppressWarnings("unchecked")
	// public void procCheckedMsg(String pk_user, String pk_wf_task, String
	// pk_wf_msg, MessageVO vo, String checknote) throws BusinessException {
	//
	//
	// if(procMsg(pk_user, pk_wf_task, pk_wf_msg,vo.getPk_billtype())){
	// procFlowByMsg(pk_wf_task, checknote,pk_user);
	// Logger.info("用户" + pk_user + "签收了消息，签收意见:" + checknote);
	// }
	// // deleteMsg(vo);
	// }
	//
	// /**
	// * @throws BusinessException
	// */
	// @SuppressWarnings("unchecked")
	// public void procCheckedMsg1(String pk_user, String pk_wf_task, String
	// pk_wf_msg) throws BusinessException {
	//
	// try {
	// BaseDAO dao = new BaseDAO();
	// Collection workNote = dao.retrieveByClause(WorkflownoteVO.class,
	// " pk_wf_task = '" + pk_wf_task + "'");
	// Collection refmsgs = dao.retrieveByClause(MessageinfoVO.class,
	// " pk_wf_msg = '" + pk_wf_msg + "'");
	// if (refmsgs.size() == 1) {
	//
	// try {
	// WFTask task = new TaskManagerDMO().getTaskByPK(pk_wf_task);
	// task.setWorknoteVO((WorkflownoteVO) workNote.toArray(new
	// WorkflownoteVO[0])[0]);
	//
	// task.setStatus(WfTaskOrInstanceStatus.Finished.getIntValue());
	// // task.setApproveResult("Y");
	// task.setBillType(task.getWorknoteVO().getPk_billtype());
	// task.setModifyTime(new
	// UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
	// task.setBillID(task.getWorknoteVO().getBillid());
	// task.setBillNO(task.getWorknoteVO().getBillno());//
	// task.setOperator(task.getWorknoteVO().getSenderman());
	// task.setAutoCompleted(true);
	// task.setMessageGenerated(true);
	// WfTaskManager.getInstance().acceptTaskFromBusi(task);// ////
	//
	// } catch (EngineException ex) {
	// Logger.debug("结束消息活动发送" + ex.getMessage());
	// }
	//
	// dao.executeUpdate("update pub_wf_msg set isCheck = 1 where pk_wf_msg = '"
	// + pk_wf_msg + "'");
	// dao.deleteByClause(MessageinfoVO.class, " pk_wf_msg = '" + pk_wf_msg +
	// "'");
	//
	// } else if (refmsgs.size() > 1) {
	// dao.deleteByClause(MessageinfoVO.class, " pk_wf_msg = '" + pk_wf_msg +
	// "' and checkman = '" + pk_user + "'");
	//
	// }
	//
	// } catch (Exception e) {
	// throw new BusinessException(e);
	// }
	//
	// }
	//
	public String[] getMessageReceivers(ReceiverVO[] receivers, TaskInstanceVO task) throws BusinessException {
		HashSet<String> alRevs = new HashSet<String>();
		for (ReceiverVO receiverVO : receivers) {
			int iType = receiverVO.getType();
			if (iType == NoticeReceiverType.ROLE.getIntValue()) {
				/** 接收者类型为“组” * */
				IRoleManageQuery roleQry = NCLocator.getInstance().lookup(IRoleManageQuery.class);
				UserRoleVO[] userRoleVos = roleQry.queryUserRoleVOByRoleID(new String[] { receiverVO.getPK() });
				for (UserRoleVO userRoleVO : userRoleVos) {

					alRevs.add(userRoleVO.getCuserid());
				}
			} else if (iType == NoticeReceiverType.USER.getIntValue()) {
				/** 接收者类型为“用户” * */
				alRevs.add(receiverVO.getPK());
			} else if (iType == NoticeReceiverType.SYSTEM.getIntValue()) {
				/** 接收者类型为系统变量* */
				alRevs.addAll(findUserOfSysVariables(receiverVO, task.getPk_form_ins_version()));
			} else if (iType == NoticeReceiverType.CUSTOM.getIntValue()) {
				/** 接收者类型为自定义 * */
				alRevs.addAll(findUserOfCustomReceivers(receiverVO, task.getPk_form_ins_version()));
			}
		}
		return alRevs.toArray(new String[0]);
	}

	//
	// private PfParameterVO getParameterVO(WFTask task) throws
	// BusinessException{
	// Hashtable hashBilltypeToParavo = new Hashtable();
	// PfUtilBaseTools.getVariableValue(task.getBillType(), "CHECKMSG",
	// (AggregatedValueObject)task.getInObject(), null, null, null,
	// task.getWorknoteVO(), null, hashBilltypeToParavo);
	// return (PfParameterVO)hashBilltypeToParavo.get(task.getBillType());
	// }
	//
	/**
	 * 查询自定义接收者对应的所有用户
	 * 
	 * @param recVO
	 * @param alRevs
	 */
	private Set<String> findUserOfCustomReceivers(ReceiverVO recVO, String billId) {

		Set<String> rcvSet = new HashSet<String>();

		PfParameterVO paraVO = ActionEnvironment.getInstance().getParaVo(billId);

		ArrayList alBilltype2VO = PfDataCache.getBillType2Info(paraVO.m_billType,
				ExtendedClassEnum.MSGCONFIG_RECEIVER.getIntValue());

		if (alBilltype2VO.size() == 0)
			return rcvSet;

		Billtype2VO bt2VO = (Billtype2VO) alBilltype2VO.get(0);
		String checkClsName = bt2VO.getClassname();
		if (nc.vo.jcom.lang.StringUtil.isEmptyWithTrim(checkClsName))
			return rcvSet;

		try {
			Object objImpl = Class.forName(checkClsName).newInstance();
			if (objImpl instanceof IPfMsgCustomReceiver) {
				UserVO[] users = ((IPfMsgCustomReceiver) objImpl).queryUsers(recVO, paraVO);
				for (int j = 0; j < (users == null ? 0 : users.length); j++) {
					rcvSet.add(users[j].getPrimaryKey());
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

		return rcvSet;
	}

	//
	/**
	 * @param mrVO
	 * @param alRevs
	 */
	private Set<String> findUserOfSysVariables(ReceiverVO mrVO, String billId) {
		
		PfParameterVO paraVO = ActionEnvironment.getInstance().getParaVo(billId);

		Set<String> alRevs = new HashSet<String>();

		SysVariableValueGetter vgetter = PfSysVariable.instance().instanceValueGetter(mrVO.getCode(), paraVO);
		Object obj = vgetter.getValueOfVar(mrVO.getCode());
		if (obj == null)
			return alRevs;
		if (obj.getClass().isArray()) {
			// 返回的是接收人的ID数组
			String[] strIds = (String[]) obj;
			for (String strId : strIds) {
				alRevs.add(strId);
			}
		} else {
			// 返回的是接收人的ID
			String strId = String.valueOf(obj);
			alRevs.add(strId);
		}

		return alRevs;
	}

}
