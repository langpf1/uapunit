package uap.workflow.admin;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.app.vo.RetBackWfVo;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 流程平台调用的工作流机服务
 * 流程运行时的相关服务接口
 */
public interface IWorkflowMachine {
	
	/**
	 * 根据子流程PK得到父流程PK
	 * @param subProcessInstancePK
	 * @return
	 * @throws DAOException
	 */
	String findParentProcessInstancePK(String subProcessInstancePK) throws DAOException;

	/**
	 * 单据启动/驱动审批流、工作流时，检查待办工作项
	 * @param actionName    动作编码，见<code>IPFActionName</code>
	 * @param billType      单据类型PK
	 * @param currentDate   审核日期
	 * @param billvo        单据聚合VO
	 * @param hmPfExParams  扩展参数
	 * @return 工作项VO
	 * @throws BusinessException
	 */
	public WorkflownoteVO checkWorkFlow(String actionName, String billType, AggregatedValueObject billvo, HashMap hmPfExParams) throws BusinessException;
	
	/**
	 * UNAPPROVE时弃审的处理，仅支持逐级弃审； ROLLBACK时回退的处理
	 * @param paraVo
	 * @return
	 * @throws BusinessException
	 */
	public RetBackWfVo backCheckFlow(PfParameterVO paraVo) throws BusinessException;

	/**
	 * DELETE时删除审批流的处理
	 * @param billType  单据类型PK
	 * @param billId    单据ID
	 * @param checkMan  审核人PK
	 * @throws BusinessException
	 */
	public void deleteCheckFlow(String billType, String billId, AggregatedValueObject billVO, String checkMan) throws BusinessException;

	/**
	 * 前进工作流处理，返回当前单据状态 向引擎发送任务,并根据单据审批状态做相应处理
	 * 
	 * @param paraVo 工作流参数VO
	 * @return       单据审批状态
	 * @throws BusinessException
	 */
	public int signalWorkflow(PfParameterVO paraVo) throws BusinessException;

	/**
	 * @param actionName
	 * @param billOrTranstype
	 * @param workflowVo
	 * @param billvo
	 * @param userObj
	 * @param eParam
	 * @return
	 * @throws BusinessException
	 */
	public Object processSingleBillFlow_RequiresNew(String actionName, String billOrTranstype, WorkflownoteVO workflowVo, AggregatedValueObject billvo, Object userObj, HashMap eParam)
			throws BusinessException;
	
	/**
	 * 新启一个事务，启动审批流或工作流
	 * <li>动作编码为SAVE启动审批流；为START启动工作流
	 * @modifier xry 2011-6 修改返回参数
	 * @param paraVo             工作流参数VO
	 * @param m_methodReturnHas
	 * @param hmPfExParams       扩展参数
	 * @return boolean[0]， true表示启动工作流，否则返回false，boolean[1] 审批流使用
	 * @throws BusinessException
	 */
	public boolean[] sendWorkFlowOnSave_RequiresNew(PfParameterVO paraVo, Hashtable m_methodReturnHas, HashMap hmPfExParams) throws BusinessException;
	
	/**
	 * 改派待办消息（工作项） 
	 * @param billId 单据ID
	 * @param pk_workflownote 当前工作项PK
	 * @param checkman 当前工作项的处理人
	 * @param appointee 接收者（用户）
	 * @throws BusinessException
	 */
	public void appointWorkitem(String billId, String pk_workflownote, String checkman, String userID)
			throws BusinessException;

	/**
	 * 前加签，增加审批人员
	 * @param noteVO
	 * @throws BusinessException
	 */
	public void beforeAddSign(WorkflownoteVO noteVO) throws BusinessException;
	
	/**
	 * 后加签，增加环节
	 * @param noteVO
	 * @throws BusinessException
	 */
	public void afterAddSign(WorkflownoteVO noteVO) throws BusinessException;
	
	
	/**
	 * 改派
	 * @param noteVo
	 * @param turnUserPks
	 * @throws BusinessException
	 */
	public void delegateTask(WorkflownoteVO noteVo,List<String> turnUserPks) throws BusinessException;
}
