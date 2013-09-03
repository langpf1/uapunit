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
 * ����ƽ̨���õĹ�����������
 * ��������ʱ����ط���ӿ�
 */
public interface IWorkflowMachine {
	
	/**
	 * ����������PK�õ�������PK
	 * @param subProcessInstancePK
	 * @return
	 * @throws DAOException
	 */
	String findParentProcessInstancePK(String subProcessInstancePK) throws DAOException;

	/**
	 * ��������/������������������ʱ�������칤����
	 * @param actionName    �������룬��<code>IPFActionName</code>
	 * @param billType      ��������PK
	 * @param currentDate   �������
	 * @param billvo        ���ݾۺ�VO
	 * @param hmPfExParams  ��չ����
	 * @return ������VO
	 * @throws BusinessException
	 */
	public WorkflownoteVO checkWorkFlow(String actionName, String billType, AggregatedValueObject billvo, HashMap hmPfExParams) throws BusinessException;
	
	/**
	 * UNAPPROVEʱ����Ĵ�����֧�������� ROLLBACKʱ���˵Ĵ���
	 * @param paraVo
	 * @return
	 * @throws BusinessException
	 */
	public RetBackWfVo backCheckFlow(PfParameterVO paraVo) throws BusinessException;

	/**
	 * DELETEʱɾ���������Ĵ���
	 * @param billType  ��������PK
	 * @param billId    ����ID
	 * @param checkMan  �����PK
	 * @throws BusinessException
	 */
	public void deleteCheckFlow(String billType, String billId, AggregatedValueObject billVO, String checkMan) throws BusinessException;

	/**
	 * ǰ���������������ص�ǰ����״̬ �����淢������,�����ݵ�������״̬����Ӧ����
	 * 
	 * @param paraVo ����������VO
	 * @return       ��������״̬
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
	 * ����һ����������������������
	 * <li>��������ΪSAVE������������ΪSTART����������
	 * @modifier xry 2011-6 �޸ķ��ز���
	 * @param paraVo             ����������VO
	 * @param m_methodReturnHas
	 * @param hmPfExParams       ��չ����
	 * @return boolean[0]�� true��ʾ���������������򷵻�false��boolean[1] ������ʹ��
	 * @throws BusinessException
	 */
	public boolean[] sendWorkFlowOnSave_RequiresNew(PfParameterVO paraVo, Hashtable m_methodReturnHas, HashMap hmPfExParams) throws BusinessException;
	
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
	 * @throws BusinessException
	 */
	public void delegateTask(WorkflownoteVO noteVo,List<String> turnUserPks) throws BusinessException;
}
