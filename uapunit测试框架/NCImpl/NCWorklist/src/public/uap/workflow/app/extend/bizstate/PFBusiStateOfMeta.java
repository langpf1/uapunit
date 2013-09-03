package uap.workflow.app.extend.bizstate;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.md.model.IBean;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.pub.util.ArrayUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.vo.IPfRetCheckInfo;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.core.WorkflowTypeEnum;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 审批流状态回写，使用元数据模型
 * 
 * @author leijun 2008-3
 * @since 5.5
 * 
 * @modifier leijun 2008-11 更新VO状态时，自动从数据库查询出TS字段并赋值到VO中
 * @modifier leijun 2010-1 增加 审批状态回写调用业务处理
 */
public class PFBusiStateOfMeta extends AbstractBusiStateCallback {

	public PFBusiStateOfMeta() {
		super();
	}

	@Override
	protected String[] getTableInfo(String billType) throws BusinessException {
		IBean bean = PfMetadataTools.queryMetaOfBilltype(billType);

		return new String[] { bean.getTable().getName(), bean.getTable().getPrimaryKeyName() };
	}

	@Override
	protected void changeStatusWhenApprove(PfParameterVO paraVo, int approveStatus) throws Exception {
		WorkflownoteVO worknoteVO = paraVo.m_workFlow;
		if (worknoteVO == null)
			worknoteVO = new WorkflownoteVO();
		
		setPreValueVos(paraVo);
		// 将当前审批状态回写到VO，并返回
		int iRetStatus = changeVoStatusWhenApprove(paraVo, approveStatus);

		//leijun+2010-1 审批状态回写
		callCheckClass(paraVo, approveStatus, paraVo.m_operator,null);

		//XXX:查询单据主表的Ts leijun+2008-11
		for (int start = 0, end = paraVo.m_preValueVos.length; start < end; start++){
			refreshHeadTs(paraVo.m_preValueVos[start]);
		}
		
	}

	/**
	 * 审批状态回写调用业务处理
	 * @param paraVo
	 * @param approveStatus
	 * @param checkman 
	 * @throws BusinessException
	 * @since 6.0
	 */
	private void callCheckClass(PfParameterVO paraVo, int approveStatus, String checkman,UFDateTime lastPostTacheBizDate)
			throws BusinessException {
		Logger.debug(">>callCheckClass() called");

		//获得bd_billtype.checkclassname注册的业务类实例
		Object checkObj = PfUtilTools.getBizRuleImpl(paraVo.m_billType);

		Logger.debug("bd_billtype.checkclassname的实例=" + checkObj);
		if (checkObj instanceof ICheckStatusCallback) {
			WorkflownoteVO noteVO = paraVo.m_workFlow;
			if (noteVO == null)
				noteVO = new WorkflownoteVO();
			for (int start = 0, end = paraVo.m_preValueVos.length; start < end; start++) {
				CheckStatusCallbackContext cscc = new CheckStatusCallbackContext();
				cscc.setBillVo(paraVo.m_preValueVos[start]);
				cscc.setApproveId(checkman);
				cscc.setCheckNote(noteVO.getChecknote());
				if(lastPostTacheBizDate==null){
					cscc.setApproveDate(new UFDate(InvocationInfoProxy.getInstance().getBizDateTime()).toString());
				}else{
					cscc.setApproveDate(lastPostTacheBizDate.toString());
				}
				cscc.setCheckStatus(approveStatus);
				TaskInstanceVO ti = noteVO.getTaskInstanceVO();
				boolean isReject = false;// TODO: ti == null ? false : (ti.getTask() == null ? false : ti.getTask().isBackToFirstActivity());
				cscc.setReject(isReject);
				boolean isTerminate = noteVO.getActiontype()!=null && noteVO.getActiontype().equals(IPFActionName.TERMINATE);
				cscc.setTerminate(isTerminate);

				switch (approveStatus) {
					case IPfRetCheckInfo.GOINGON:
					case IPfRetCheckInfo.PASSING:
					case IPfRetCheckInfo.NOPASS:
						//asdfsadf
						break;
					case IPfRetCheckInfo.NOSTATE:
					case IPfRetCheckInfo.COMMIT:
						cscc.setApproveId(null);
						cscc.setApproveDate(null);
						break;
					default:
						break;
				}

				((ICheckStatusCallback) checkObj).callCheckStatus(cscc);
			}
			
		} else {
			Logger.warn("未实现状态回写接口=" + ICheckStatusCallback.class.getName());
		}
	}

	@Override
	protected void changeStatusWhenUnapprove(PfParameterVO paraVo, String preCheckman,
			int iBackCheckState) throws Exception {

		setPreValueVos(paraVo);
		UFDateTime lastPostTacheBizDate =null;
		if(preCheckman!=null){
			lastPostTacheBizDate =getLastPostTacheUFDate(paraVo);
		}
		//将当前审批状态回写到VO，并返回
		int iRetBackStatus = changeVoStatusWhenUnapprove(paraVo, preCheckman, iBackCheckState,lastPostTacheBizDate);		
		//将当前审批状态回写到单据表 
		switch (iBackCheckState) {
			case IPfRetCheckInfo.GOINGON: {
				// 修改为进行中
				//leijun+2010-1 审批状态回写
				callCheckClass(paraVo, iBackCheckState, preCheckman,lastPostTacheBizDate);
				break;
			}
			case IPfRetCheckInfo.NOSTATE:
			case IPfRetCheckInfo.COMMIT: {
				// 修改为自由态
				//leijun+2010-1 审批状态回写
				callCheckClass(paraVo, iBackCheckState, null,lastPostTacheBizDate);
				break;
			}
			default:
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PFBusiStateOfMeta-000000")/*不合法的弃审状态*/);
		}

		//XXX:查询单据主表的Ts leijun+2008-11
		for (int start = 0, end = paraVo.m_preValueVos.length; start < end; start++){
			refreshHeadTs(paraVo.m_preValueVos[start]);
		}
	}

	protected int changeVoStatusWhenApprove(PfParameterVO paraVo, int iApproveStatus) {
		WorkflownoteVO worknoteVO = paraVo.m_workFlow;
		if (worknoteVO == null)
			worknoteVO = new WorkflownoteVO();

		// 审批状态回写
		int firstBillapproveStatus=-1;
		for(int start =0,end =paraVo.m_preValueVos.length;start<end;start++){
			IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(paraVo.m_preValueVos[start], IFlowBizItf.class);
			fbi.setApproveNote(worknoteVO.getChecknote());
			fbi.setApproveStatus(iApproveStatus);
			fbi.setApprover(paraVo.m_operator);
			fbi.setApproveDate(new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
			if(start==0)
				firstBillapproveStatus =iApproveStatus;
			Logger.error(iApproveStatus);
		}
		
		return firstBillapproveStatus;
	}

	protected int changeVoStatusWhenUnapprove(PfParameterVO paraVo, String preCheckman,
			int iApproveStatus,UFDateTime lastPostTacheBizDate) {
		WorkflownoteVO worknoteVO = paraVo.m_workFlow;
		if (worknoteVO == null)
			worknoteVO = new WorkflownoteVO();
		int firstBillapproveStatus=-1;
		for(int start =0,end =paraVo.m_preValueVos.length;start<end;start++){
			// 弃审状态回写
			IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(paraVo.m_preValueVos[start], IFlowBizItf.class);
			
			
			// yanke1 2011-9-21 弃审时清空批语
//			fbi.setApproveNote(worknoteVO.getChecknote());
			fbi.setApproveNote("");
			fbi.setApproveStatus(iApproveStatus);
			fbi.setApprover(preCheckman);
			fbi.setApproveDate(lastPostTacheBizDate);

		}		
		return firstBillapproveStatus;
	}
	
	/**
	 * 弃审时，单据的审批时间为上个环节的审核时间
	 * @throws BusinessException 
	 * */
	private UFDateTime getLastPostTacheUFDate(PfParameterVO paraVo) throws BusinessException{		
		String processInstancePK = paraVo.m_flowDefPK;
		if(processInstancePK == null && paraVo.m_workFlow!= null && paraVo.m_workFlow.getTaskInstanceVO()!=null)
		{
			processInstancePK = paraVo.m_workFlow.getTaskInstanceVO().getPk_process_def();
		}

		String bizTimeStr = null;
		IWorkflowMachine workFlowMachine = NCLocator.getInstance().lookup(IWorkflowMachine.class);
		do {
			String sql ="select finishdate from wf_task where pk_form_ins_version = ? and state_task= ? order by finishdate desc";
			SQLParameter param =new SQLParameter();
			param.addParam(paraVo.m_billVersionPK);
			param.addParam(TaskInstanceStatus.Finished.getIntValue());
			Object[] bizTimes =(Object[]) ((IUAPQueryBS) (NCLocator.getInstance().lookup(IUAPQueryBS.class))).executeQuery(sql,param, new ArrayProcessor());
			if(!ArrayUtil.isNull(bizTimes) && bizTimes[0] != null) {
				bizTimeStr = bizTimes[0].toString();
			}

			// 取得时间为空，判断是否是子流程
			if(StringUtil.isEmptyWithTrim(bizTimeStr)) {
				if(WorkflowTypeEnum.isSubFlow(paraVo.m_workFlow.getWorkflow_type())) {
					// 如果是子流程，那么取父流程
					processInstancePK = workFlowMachine.findParentProcessInstancePK(processInstancePK);
				} else {
					processInstancePK = null;
				}
			}
			
			//如果有父流程，且还没找到时间，则继续找
		} while (!StringUtil.isEmptyWithTrim(processInstancePK) && StringUtil.isEmptyWithTrim(bizTimeStr));
		
		return StringUtil.isEmptyWithTrim(bizTimeStr) ? null : new UFDateTime(bizTimeStr);
	}
	
	/**
	 * 设置PfParameterVO的原传入Vo值数组。
	 * 走审批流时候m_preValueVos为null，m_preValueVo有值。此时改变单据状态调用for循环有错
	 * */
	private void setPreValueVos(PfParameterVO paraVo){
		if(paraVo.m_preValueVos==null){
			paraVo.m_preValueVos =new AggregatedValueObject[1] ;
			paraVo.m_preValueVos[0]=paraVo.m_preValueVo;
		}
	}

}
