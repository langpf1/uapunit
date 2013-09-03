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
 * 审批流、工作流管理及监控接口
 */
public interface IWorkflowAdmin {
	
	public UFBoolean hasRunningProcess(String billId, String billType, String flowType);

	/**
	 * 终止某单据的审批流程或工作流程 实例
	 * <li>必须是进行中的流程
	 * <li>会调用单据组件的回退方法，以便业务回滚
	 * 
	 * @param billid
	 * @param billtype
	 * @param billNo
	 * @param iWorkflowtype 主流程类型，见<code>WorkflowTypeEnum.Approveflow</code>和<code>WorkflowTypeEnum.Workflow</code>
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
	* 挂起流程实例 
	 * @param billid
	 * @param billtype
	 * @param billNo
	 * @param iWorkflowtype 主流程类型，见<code>WorkflowTypeEnum.Approveflow</code>和<code>WorkflowTypeEnum.Workflow</code>
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
	 * 实例恢复 
	 * @param billid
	 * @param billtype
	 * @param billNo
	 * @param iWorkflowtype 主流程类型，见<code>WorkflowTypeEnum.Approveflow</code>和<code>WorkflowTypeEnum.Workflow</code>
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

	/**邮件催办 
	 * @param noteVO
	 * @throws BusinessException
	 */
	public void mailUrgency(WorkflownoteVO noteVO) throws BusinessException;
	
	
	/**
	 * 保存/更新一个流程的流程时限和环节时限
	 * @param mainPk_wf_instance 主流程pk
	 * @param settings
	 * @throws BusinessException
	 */
	public void updateFlowTimeSetting(String mainPk_wf_instance, FlowTimeSettingVO[] settings) throws BusinessException;
	
	/**
	 * 查询一个流程的流程时限和环节时限
	 * @param mainPk_wf_instance 
	 * @return
	 * @throws BusinessException
	 */
	public FlowTimeSettingVO[] getFlowTimeSetting(String mainPk_wf_instance) throws BusinessException;
	
	
	/**
	 * 查询工作项的超期情况
	 * @param pk_checkflow
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, FlowOverdueVO> getWorknoteOverdueBatch(String[] pk_checkflows) throws BusinessException;
	
	/**
	 * 查询流程实例的超期情况
	 * @param pk_wf_instance
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, FlowOverdueVO> getFlowInstanceOverdue(String[] pk_wf_instances) throws BusinessException;
	
	/** 抄送：分为邮件和信息两种途径
	 * @param  noteVO 当前工作项
	 * @param note 标题和批语
	 * @throws BusinessException
	 * */
	public void cpySendByMailAndMsg(WorkflownoteVO noteVO,String[] titleAndNote) throws BusinessException;
	
	/**流程实例跟踪
	 * @param worknoteVO 当前工作项
	 * @param supervisor 
	 * @param flag  添加或者删除跟踪标志位
	 * @throws BusinessException
	 * */
	public void trackWFinstance(WorkflownoteVO worknoteVO,String supervisor,boolean flag) throws BusinessException;
	
	/**流程实例是否被跟踪
	 * @param pk 流程实例PK
	 * @param supervisor 监控人pk
	 * @return 
	 * @throws BusinessException
	 * */
	public boolean isAlreadyTracked(String pk,String supervisor)throws BusinessException;
	
	
	public ArrayList<String> findFilterOrgs4Responsibility(TaskInstanceVO task) throws BusinessException;
}
