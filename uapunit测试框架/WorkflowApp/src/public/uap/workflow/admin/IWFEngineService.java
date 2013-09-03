package uap.workflow.admin;

import java.util.HashMap;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.exception.DbException;
import nc.vo.pub.BusinessException;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.restlet.application.Pagination;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.ReturnBackWfVo;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 流程平台调用的工作流机服务
 * 流程运行时的相关服务接口
 */
public interface IWFEngineService {
	
	/**
	 * 根据子流程PK得到父流程PK
	 * @param subProcessInstancePK
	 * @return
	 * @throws DAOException
	 */
	String findParentProcessInstancePK(String subProcessInstancePK) throws DAOException;

	/**
	 * 收回、取消提交
	 * @param paraVo
	 */
	public ReturnBackWfVo cancelSubmitWorkflow(WFAppParameter paraVo) throws BusinessException;
	
	/**
	 * ROLLBACK时的工作流回退处理，UNAPPROVE时的审批流弃审处理, 只支持逐级回退
	 * @param paraVo
	 */
	public ReturnBackWfVo rollbackWorkflow(WFAppParameter paraVo) throws BusinessException;
	
	/**
	 * 启动审批流
	 * 
	 * @param paraVo
	 * @param hmPfExParams
	 */
	public boolean[] startWorkflow(WFAppParameter paraVo, HashMap hmPfExParams) throws BusinessException;	

	/**
	 * 前进工作流处理，返回当前单据状态 向引擎发送任务,并根据单据审批状态做相应处理
	 * 
	 * @param para 工作流参数VO
	 * @return       单据审批状态
	 * @throws BusinessException
	 */
	public int signalWorkflow(WFAppParameter para) throws BusinessException;

	/**
	 * 推动流程往下走
	 */
	public IFlowResponse forwardWorkflow(WFAppParameter paraVo, TaskInstanceVO taskVO);

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
	 * @param currentUserId
	 * @throws BusinessException
	 */
	public void delegateTask(WorkflownoteVO noteVo,List<String> turnUserPks, String currentUserId) throws BusinessException;

	public WorkflownoteVO getWorkflowItemsOnStart(WFAppParameter paraVo, int status) throws BusinessException;

	public WorkflownoteVO checkUnfinishedWorkitem(WFAppParameter paraVo) throws BusinessException, DbException; 	

	/**
	 * 根据taskpk取到TaskInstanceVO，并组装成WorkflownoteVO
	 * @param taskpk
	 * @return
	 * @throws BusinessException
	 */
	public WorkflownoteVO getWorkitem(String pk_task) throws BusinessException;
	
	/**
	 * 查找我发起的任务
	 * @param taskState 任务状态:(代办,已办,办结|待阅,已阅,阅毕)
	 * @param keyWord 关键词(title,a.activity_name,form_no模糊查询)
	 * @param bizObject 单据类型
	 * @param pk_user   pk_agenter或者pk_user
	 * @param wherePart where条件
	 * @return
	 */
	List<TaskInstanceVO> getTasks(int taskState,String keyWord, String bizObject, String pk_user, String wherePart, boolean isGetMyStartTask,Pagination page);
}
