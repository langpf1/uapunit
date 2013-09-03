package uap.workflow.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import nc.vo.pub.BusinessException;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.monitor.ProcessRouteRes;

/**
 * ���̶������ط���ӿ�
 */
public interface IWFDefineService {

	/**
	 * �Ƿ������̶���
	 * 
	 * @param pk_group
	 * @param billType
	 * @param mainWorkflowtype
	 * @return
	 * @throws BusinessException
	 */
	public boolean hasValidProcessDef(String pk_group, String billType,
			String pkOrg, String operator,int emendEnum)
			throws BusinessException;

	/**
	 * ����ҵ�����ƥ�����̶���
	 * @param pk_group ����
	 * @param billType ��������
	 * @param pkOrg ��֯
	 * @param operator ������
	 * @param emendEnum
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition matchProcessDefitionAccordingBiz(String pk_group, String billType,
			String pkOrg, String operator,int emendEnum) throws BusinessException;

	/**
	 * ��������ɾ��һ����¼
	 * 
	 * @param pk_wf_def
	 * @throws Exception
	 */
	public void deleteDefinitionByPK(String pk_wf_def, String pk_group)
			throws BusinessException;

	/**
	 * @param pk_wf_def
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition findDefinitionByPrimaryKey(String pk_wf_def)
			throws BusinessException;

	/**
	 * ��������PKȡ��Ӧ�Ļ����
	 * @param pk_task
	 * @return
	 * @throws BusinessException
	 */
	public IActivity getActivityByTaskPK(String pk_task)throws BusinessException;

	/**
	 * ��������PKȡ��Ӧ�Ļ����
	 * @param pk_task
	 * @return
	 * @throws BusinessException
	 */
	public IActivity getActivity(String pk_process_def, String activity_id)throws BusinessException;
	
	/**
	 * @param pk_wf_def
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition findDefinitionByGUID(String pk_wf_def)
			throws BusinessException;

	/**
	 * �������̶���pk�Լ�����ʵ��pk�����̶��壬֮����Ҫ��������ʵ��pk�飬�ǿ��ǵ���ǩ���ܻᶯ̬�ı����̶���
	 * 
	 * @param pk_wf_def
	 * @param pk_wf_instance
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, IProcessDefinition> findDefinitionByDefPkAndInstPk(
			String pk_wf_def, String pk_wf_instance) throws BusinessException;

	/**
	 * ����ĳ�������£�ĳ�����ݻ������͵����̶��壬����������������<BR>
	 * 
	 * @param pk_group
	 * @param billOrTranstype
	 * @param bIncludePackage
	 *            �Ƿ�Ҳ���ذ�����
	 * @param mainWorkflowtype
	 *            ��ѯ��������������������
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition[] findDefinitionsWithoutContent(
			String pk_group, String billOrTranstype, boolean bIncludePackage,
			int mainWorkflowtype) throws BusinessException;

	/**
	 * ������ĳ���������Ͷ�Ӧ�İ����� <li>XXX::0..1����¼����,��Ϊһ��ҵ��+�������Ͷ�Ӧһ��������
	 * 
	 * @param pk_group
	 * @param billOrTranstype
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition[] findPackageByBillType(String pk_group,
			String billOrTranstype) throws BusinessException;

	/**
	 * ����GUID�������ݿ��е�����PK <li>�ö����������Ч�� <li>һ��Guid���ҽ���һ����Ч�����̶���
	 * 
	 * @param guid
	 * @return
	 * @throws BusinessException
	 */
	public String findPrimaryKeyByGuid(String guid) throws BusinessException;

	/**
	 * �ҵ�ĳ����ʵ���Ķ���;�����������,����ݹ��ȡ�丸���̵Ķ�������
	 * 
	 * @param procInstancePK
	 * @param activityDefID
	 * @return LinkedList
	 * @throws BusinessException
	 */
	public LinkedList findProcessDefsOfInstance(String procInstancePK,
			String activityDefID) throws BusinessException;

	/**
	 * �ж����̶����Ƿ��������ʵ����������/������ɵģ�
	 * 
	 * @param proc_defPK
	 * @return
	 */
	public boolean hasProcessInstances(String proc_defPK) throws BusinessException;

	public boolean hasRunningProcessInstances(String proc_defPK) throws BusinessException;	
	/**
	 * �ж������̶����Ƿ����á�
	 * */
	public boolean isWorkflowReferenced(String proc_defPK)
			throws BusinessException;

	/**
	 * ����һ�������嵽���ݿ��У����������е����й��̶���<BR>
	 * ��������������Ѿ����ڣ�����°�����
	 * 
	 * @throws BusinessException
	 */
	public String savePackageWithoutProcesses(IProcessDefinition def_vo)
			throws BusinessException;

	/**
	 * ������̶��� �� ���¹��̶���
	 * 
	 * @param def_vo
	 * @return {�����Ӧ�����ݿ��¼����PK,�����guid}
	 *         <p>
	 *         �޸��ˣ��׾� 2004-12-30 ����ö�����������ʵ������ɶ�����Ϊ��Ч���������¶����¼
	 *         <p>
	 *         �޸��ˣ��׾� 2005-2-26 �޸�BUG,�����ɵ�GUID����Ҫ���µ�XPDL������
	 */
	public String[] saveProcess(IProcessDefinition def_vo)
			throws BusinessException;

	/**
	 * ����/ͣ��һ������
	 * 
	 * @param processPK
	 * @param onUse
	 *            �ж������û���ͣ��
	 * @return
	 * @throws BusinessException
	 */
	public String[] updateProcessValidation(String processPK,
			String sublfowInRange, boolean onUse, boolean isInbatchMode)
			throws BusinessException;

	/**
	 * ���ݵ���id������ʵ��PK��ȡ�����̶��������,��Ҫ��ȡ�����д������Ļ��<br>
	 * �ٵݹ��ѯ�����������̡� <li>�������̹켣����ʾ
	 * 
	 * @param billId
	 * @param procInstancePK
	 * @param iWfType
	 *            ���������ͣ���<code>WorkflowTypeEnum.Approveflow</code>��
	 *            <code>WorkflowTypeEnum.Workflow</code>
	 * @return
	 * @throws BusinessException
	 */
	public ProcessRouteRes queryProcessRoute(String billId, String billType,
			String procInstancePK, int iWfType) throws BusinessException;

	public List<IActivity[]> queryFinishedActivities(String billId,
			String billType, int iWfType) throws BusinessException;

	/**
	 * ͬ���Ƶ��˻������˵�����
	 * 
	 * @throws BusinessException
	 */
	public int syncParticipantNames() throws BusinessException;

	/**
	 * ��ѯĳ���ݵ���������������������ͼ��������ΪPNG��ʽ
	 * 
	 * @param billId
	 *            ����ID
	 * @param billType
	 *            ��������PK��������
	 * @param iWorkflowtype
	 *            �������ͣ�1=������ 3=����������<code>IApproveflowConst
	 * @return
	 * @throws BusinessException
	 */
	public byte[] toPNGImage(String billId, String billType, int iWorkflowtype)
			throws BusinessException;

	/**
	 * ��ѯ����ͼ������������
	 * @param billId
	 * @param billType
	 * @param iWorkflowtype
	 * @return
	 * @throws BusinessException
	 */
	public List<byte[]> toPNGImagesWithSubFlow(String billId, String billType,
			int iWorkflowtype) throws BusinessException;

	/**
	 * @param pk_group
	 * @throws BusinessException
	 */
	public void syncSubflowState(String pk_group) throws BusinessException;

	public HashSet<String> queryWfInstanceDefid(String insql)
			throws BusinessException;
}
