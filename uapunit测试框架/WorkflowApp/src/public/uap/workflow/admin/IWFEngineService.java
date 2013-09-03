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
 * ����ƽ̨���õĹ�����������
 * ��������ʱ����ط���ӿ�
 */
public interface IWFEngineService {
	
	/**
	 * ����������PK�õ�������PK
	 * @param subProcessInstancePK
	 * @return
	 * @throws DAOException
	 */
	String findParentProcessInstancePK(String subProcessInstancePK) throws DAOException;

	/**
	 * �ջء�ȡ���ύ
	 * @param paraVo
	 */
	public ReturnBackWfVo cancelSubmitWorkflow(WFAppParameter paraVo) throws BusinessException;
	
	/**
	 * ROLLBACKʱ�Ĺ��������˴���UNAPPROVEʱ��������������, ֻ֧���𼶻���
	 * @param paraVo
	 */
	public ReturnBackWfVo rollbackWorkflow(WFAppParameter paraVo) throws BusinessException;
	
	/**
	 * ����������
	 * 
	 * @param paraVo
	 * @param hmPfExParams
	 */
	public boolean[] startWorkflow(WFAppParameter paraVo, HashMap hmPfExParams) throws BusinessException;	

	/**
	 * ǰ���������������ص�ǰ����״̬ �����淢������,�����ݵ�������״̬����Ӧ����
	 * 
	 * @param para ����������VO
	 * @return       ��������״̬
	 * @throws BusinessException
	 */
	public int signalWorkflow(WFAppParameter para) throws BusinessException;

	/**
	 * �ƶ�����������
	 */
	public IFlowResponse forwardWorkflow(WFAppParameter paraVo, TaskInstanceVO taskVO);

	/**
	 * ���ɴ�����Ϣ������� 
	 * @param billId ����ID
	 * @param pk_workflownote ��ǰ������PK
	 * @param checkman ��ǰ������Ĵ�����
	 * @param appointee �����ߣ��û���
	 * @throws BusinessException
	 */
	public void appointWorkitem(String billId, String pk_workflownote, String checkman, String userID)
			throws BusinessException;

	/**
	 * ǰ��ǩ������������Ա
	 * @param noteVO
	 * @throws BusinessException
	 */
	public void beforeAddSign(WorkflownoteVO noteVO) throws BusinessException;
	
	/**
	 * ���ǩ�����ӻ���
	 * @param noteVO
	 * @throws BusinessException
	 */
	public void afterAddSign(WorkflownoteVO noteVO) throws BusinessException;
	
	/**
	 * ����
	 * @param noteVo
	 * @param turnUserPks
	 * @param currentUserId
	 * @throws BusinessException
	 */
	public void delegateTask(WorkflownoteVO noteVo,List<String> turnUserPks, String currentUserId) throws BusinessException;

	public WorkflownoteVO getWorkflowItemsOnStart(WFAppParameter paraVo, int status) throws BusinessException;

	public WorkflownoteVO checkUnfinishedWorkitem(WFAppParameter paraVo) throws BusinessException, DbException; 	

	/**
	 * ����taskpkȡ��TaskInstanceVO������װ��WorkflownoteVO
	 * @param taskpk
	 * @return
	 * @throws BusinessException
	 */
	public WorkflownoteVO getWorkitem(String pk_task) throws BusinessException;
	
	/**
	 * �����ҷ��������
	 * @param taskState ����״̬:(����,�Ѱ�,���|����,����,�ı�)
	 * @param keyWord �ؼ���(title,a.activity_name,form_noģ����ѯ)
	 * @param bizObject ��������
	 * @param pk_user   pk_agenter����pk_user
	 * @param wherePart where����
	 * @return
	 */
	List<TaskInstanceVO> getTasks(int taskState,String keyWord, String bizObject, String pk_user, String wherePart, boolean isGetMyStartTask,Pagination page);
}
