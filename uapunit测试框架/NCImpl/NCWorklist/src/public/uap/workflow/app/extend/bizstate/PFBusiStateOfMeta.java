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
 * ������״̬��д��ʹ��Ԫ����ģ��
 * 
 * @author leijun 2008-3
 * @since 5.5
 * 
 * @modifier leijun 2008-11 ����VO״̬ʱ���Զ������ݿ��ѯ��TS�ֶβ���ֵ��VO��
 * @modifier leijun 2010-1 ���� ����״̬��д����ҵ����
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
		// ����ǰ����״̬��д��VO��������
		int iRetStatus = changeVoStatusWhenApprove(paraVo, approveStatus);

		//leijun+2010-1 ����״̬��д
		callCheckClass(paraVo, approveStatus, paraVo.m_operator,null);

		//XXX:��ѯ���������Ts leijun+2008-11
		for (int start = 0, end = paraVo.m_preValueVos.length; start < end; start++){
			refreshHeadTs(paraVo.m_preValueVos[start]);
		}
		
	}

	/**
	 * ����״̬��д����ҵ����
	 * @param paraVo
	 * @param approveStatus
	 * @param checkman 
	 * @throws BusinessException
	 * @since 6.0
	 */
	private void callCheckClass(PfParameterVO paraVo, int approveStatus, String checkman,UFDateTime lastPostTacheBizDate)
			throws BusinessException {
		Logger.debug(">>callCheckClass() called");

		//���bd_billtype.checkclassnameע���ҵ����ʵ��
		Object checkObj = PfUtilTools.getBizRuleImpl(paraVo.m_billType);

		Logger.debug("bd_billtype.checkclassname��ʵ��=" + checkObj);
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
			Logger.warn("δʵ��״̬��д�ӿ�=" + ICheckStatusCallback.class.getName());
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
		//����ǰ����״̬��д��VO��������
		int iRetBackStatus = changeVoStatusWhenUnapprove(paraVo, preCheckman, iBackCheckState,lastPostTacheBizDate);		
		//����ǰ����״̬��д�����ݱ� 
		switch (iBackCheckState) {
			case IPfRetCheckInfo.GOINGON: {
				// �޸�Ϊ������
				//leijun+2010-1 ����״̬��д
				callCheckClass(paraVo, iBackCheckState, preCheckman,lastPostTacheBizDate);
				break;
			}
			case IPfRetCheckInfo.NOSTATE:
			case IPfRetCheckInfo.COMMIT: {
				// �޸�Ϊ����̬
				//leijun+2010-1 ����״̬��д
				callCheckClass(paraVo, iBackCheckState, null,lastPostTacheBizDate);
				break;
			}
			default:
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PFBusiStateOfMeta-000000")/*���Ϸ�������״̬*/);
		}

		//XXX:��ѯ���������Ts leijun+2008-11
		for (int start = 0, end = paraVo.m_preValueVos.length; start < end; start++){
			refreshHeadTs(paraVo.m_preValueVos[start]);
		}
	}

	protected int changeVoStatusWhenApprove(PfParameterVO paraVo, int iApproveStatus) {
		WorkflownoteVO worknoteVO = paraVo.m_workFlow;
		if (worknoteVO == null)
			worknoteVO = new WorkflownoteVO();

		// ����״̬��д
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
			// ����״̬��д
			IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(paraVo.m_preValueVos[start], IFlowBizItf.class);
			
			
			// yanke1 2011-9-21 ����ʱ�������
//			fbi.setApproveNote(worknoteVO.getChecknote());
			fbi.setApproveNote("");
			fbi.setApproveStatus(iApproveStatus);
			fbi.setApprover(preCheckman);
			fbi.setApproveDate(lastPostTacheBizDate);

		}		
		return firstBillapproveStatus;
	}
	
	/**
	 * ����ʱ�����ݵ�����ʱ��Ϊ�ϸ����ڵ����ʱ��
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

			// ȡ��ʱ��Ϊ�գ��ж��Ƿ���������
			if(StringUtil.isEmptyWithTrim(bizTimeStr)) {
				if(WorkflowTypeEnum.isSubFlow(paraVo.m_workFlow.getWorkflow_type())) {
					// ����������̣���ôȡ������
					processInstancePK = workFlowMachine.findParentProcessInstancePK(processInstancePK);
				} else {
					processInstancePK = null;
				}
			}
			
			//����и����̣��һ�û�ҵ�ʱ�䣬�������
		} while (!StringUtil.isEmptyWithTrim(processInstancePK) && StringUtil.isEmptyWithTrim(bizTimeStr));
		
		return StringUtil.isEmptyWithTrim(bizTimeStr) ? null : new UFDateTime(bizTimeStr);
	}
	
	/**
	 * ����PfParameterVO��ԭ����Voֵ���顣
	 * ��������ʱ��m_preValueVosΪnull��m_preValueVo��ֵ����ʱ�ı䵥��״̬����forѭ���д�
	 * */
	private void setPreValueVos(PfParameterVO paraVo){
		if(paraVo.m_preValueVos==null){
			paraVo.m_preValueVos =new AggregatedValueObject[1] ;
			paraVo.m_preValueVos[0]=paraVo.m_preValueVo;
		}
	}

}
