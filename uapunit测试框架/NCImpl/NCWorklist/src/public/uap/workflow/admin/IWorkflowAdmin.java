package uap.workflow.admin;

import java.util.ArrayList;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.vo.FlowOverdueVO;
import uap.workflow.vo.FlowTimeSettingVO;
import uap.workflow.vo.WorkflownoteVO;

/**
 * ��������������������ؽӿ�
 */
public interface IWorkflowAdmin {
	
	public UFBoolean hasRunningProcess(String billId, String billType, String flowType);

	/**
	 * ��ֹĳ���ݵ��������̻������� ʵ��
	 * <li>�����ǽ����е�����
	 * <li>����õ�������Ļ��˷������Ա�ҵ��ع�
	 * 
	 * @param billid
	 * @param billtype
	 * @param billNo
	 * @param iWorkflowtype ���������ͣ���<code>WorkflowTypeEnum.Approveflow</code>��<code>WorkflowTypeEnum.Workflow</code>
	 * @return
	 * @throws BusinessException
	 */
	public void terminateWorkflow(String billid, String billtype, String billNo, int iWorkflowtype)
			throws BusinessException;
	
	/**
	 * @since v6.1
	 * @param context
	 * @throws BusinessException
	 */
	public void terminateWorkflow(WorkflowManageContext context) throws BusinessException;

	/**
	* ��������ʵ�� 
	 * @param billid
	 * @param billtype
	 * @param billNo
	 * @param iWorkflowtype ���������ͣ���<code>WorkflowTypeEnum.Approveflow</code>��<code>WorkflowTypeEnum.Workflow</code>
	 * @throws BusinessException
	 */
	public void suspendWorkflow(String billid, String billtype, String billNo, int iWorkflowtype)
			throws BusinessException;
	
	/**
	 * @since v6.1
	 * @param context
	 * @throws BusinessException
	 */
	public void suspendWorkflow(WorkflowManageContext context) throws BusinessException;

	/**
	 * ʵ���ָ� 
	 * @param billid
	 * @param billtype
	 * @param billNo
	 * @param iWorkflowtype ���������ͣ���<code>WorkflowTypeEnum.Approveflow</code>��<code>WorkflowTypeEnum.Workflow</code>
	 * @throws BusinessException
	 */
	public void resumeWorkflow(String billid, String billtype, String billNo, int iWorkflowtype)
			throws BusinessException;
	
	/**
	 * @since v6.1
	 * @param context
	 * @throws BusinessException
	 */
	public void resumeWorkflow(WorkflowManageContext context) throws BusinessException;

	/**�ʼ��߰� 
	 * @param noteVO
	 * @throws BusinessException
	 */
	public void mailUrgency(WorkflownoteVO noteVO) throws BusinessException;
	
	
	/**
	 * ����/����һ�����̵�����ʱ�޺ͻ���ʱ��
	 * @param mainPk_wf_instance ������pk
	 * @param settings
	 * @throws BusinessException
	 */
	public void updateFlowTimeSetting(String mainPk_wf_instance, FlowTimeSettingVO[] settings) throws BusinessException;
	
	/**
	 * ��ѯһ�����̵�����ʱ�޺ͻ���ʱ��
	 * @param mainPk_wf_instance 
	 * @return
	 * @throws BusinessException
	 */
	public FlowTimeSettingVO[] getFlowTimeSetting(String mainPk_wf_instance) throws BusinessException;
	
	
	/**
	 * ��ѯ������ĳ������
	 * @param pk_checkflow
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, FlowOverdueVO> getWorknoteOverdueBatch(String[] pk_checkflows) throws BusinessException;
	
	/**
	 * ��ѯ����ʵ���ĳ������
	 * @param pk_wf_instance
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, FlowOverdueVO> getFlowInstanceOverdue(String[] pk_wf_instances) throws BusinessException;
	
	/** ���ͣ���Ϊ�ʼ�����Ϣ����;��
	 * @param  noteVO ��ǰ������
	 * @param note ���������
	 * @throws BusinessException
	 * */
	public void cpySendByMailAndMsg(WorkflownoteVO noteVO,String[] titleAndNote) throws BusinessException;
	
	/**����ʵ������
	 * @param worknoteVO ��ǰ������
	 * @param supervisor 
	 * @param flag  ��ӻ���ɾ�����ٱ�־λ
	 * @throws BusinessException
	 * */
	public void trackWFinstance(WorkflownoteVO worknoteVO,String supervisor,boolean flag) throws BusinessException;
	
	/**����ʵ���Ƿ񱻸���
	 * @param pk ����ʵ��PK
	 * @param supervisor �����pk
	 * @return 
	 * @throws BusinessException
	 * */
	public boolean isAlreadyTracked(String pk,String supervisor)throws BusinessException;
	
	
	public ArrayList<String> findFilterOrgs4Responsibility(TaskInstanceVO task) throws BusinessException;
}
