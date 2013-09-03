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
 * 流程定义的相关服务接口
 */
public interface IWFDefineService {

	/**
	 * 是否有流程定义
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
	 * 按照业务规则匹配流程定义
	 * @param pk_group 集团
	 * @param billType 单据类型
	 * @param pkOrg 组织
	 * @param operator 参与者
	 * @param emendEnum
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition matchProcessDefitionAccordingBiz(String pk_group, String billType,
			String pkOrg, String operator,int emendEnum) throws BusinessException;

	/**
	 * 根据主键删除一条记录
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
	 * 根据任务PK取对应的活动定义
	 * @param pk_task
	 * @return
	 * @throws BusinessException
	 */
	public IActivity getActivityByTaskPK(String pk_task)throws BusinessException;

	/**
	 * 根据任务PK取对应的活动定义
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
	 * 根据流程定义pk以及流程实例pk查流程定义，之所以要根据流程实例pk查，是考虑到加签可能会动态改变流程定义
	 * 
	 * @param pk_wf_def
	 * @param pk_wf_instance
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, IProcessDefinition> findDefinitionByDefPkAndInstPk(
			String pk_wf_def, String pk_wf_instance) throws BusinessException;

	/**
	 * 查找某个集团下，某个单据或交易类型的流程定义，但不包含定义内容<BR>
	 * 
	 * @param pk_group
	 * @param billOrTranstype
	 * @param bIncludePackage
	 *            是否也返回包定义
	 * @param mainWorkflowtype
	 *            查询工作流还是审批流定义
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition[] findDefinitionsWithoutContent(
			String pk_group, String billOrTranstype, boolean bIncludePackage,
			int mainWorkflowtype) throws BusinessException;

	/**
	 * 返回与某个单据类型对应的包定义 <li>XXX::0..1条记录返回,因为一个业务+单据类型对应一个包定义
	 * 
	 * @param pk_group
	 * @param billOrTranstype
	 * @return
	 * @throws BusinessException
	 */
	public IProcessDefinition[] findPackageByBillType(String pk_group,
			String billOrTranstype) throws BusinessException;

	/**
	 * 根据GUID查找数据库中的主键PK <li>该定义必须是有效的 <li>一个Guid有且仅有一个有效的流程定义
	 * 
	 * @param guid
	 * @return
	 * @throws BusinessException
	 */
	public String findPrimaryKeyByGuid(String guid) throws BusinessException;

	/**
	 * 找到某流程实例的定义;如果是子流程,还需递归获取其父流程的定义内容
	 * 
	 * @param procInstancePK
	 * @param activityDefID
	 * @return LinkedList
	 * @throws BusinessException
	 */
	public LinkedList findProcessDefsOfInstance(String procInstancePK,
			String activityDefID) throws BusinessException;

	/**
	 * 判断流程定义是否存在流程实例（运行中/运行完成的）
	 * 
	 * @param proc_defPK
	 * @return
	 */
	public boolean hasProcessInstances(String proc_defPK) throws BusinessException;

	public boolean hasRunningProcessInstances(String proc_defPK) throws BusinessException;	
	/**
	 * 判断子流程定义是否被引用。
	 * */
	public boolean isWorkflowReferenced(String proc_defPK)
			throws BusinessException;

	/**
	 * 保存一个包定义到数据库中，不包含其中的所有过程定义<BR>
	 * 或者如果包定义已经存在，则更新包定义
	 * 
	 * @throws BusinessException
	 */
	public String savePackageWithoutProcesses(IProcessDefinition def_vo)
			throws BusinessException;

	/**
	 * 保存过程定义 或 更新过程定义
	 * 
	 * @param def_vo
	 * @return {定义对应的数据库记录主键PK,定义的guid}
	 *         <p>
	 *         修改人：雷军 2004-12-30 如果该定义已有流程实例，则旧定义置为无效，并插入新定义记录
	 *         <p>
	 *         修改人：雷军 2005-2-26 修改BUG,新生成的GUID还需要更新到XPDL内容中
	 */
	public String[] saveProcess(IProcessDefinition def_vo)
			throws BusinessException;

	/**
	 * 启用/停用一个流程
	 * 
	 * @param processPK
	 * @param onUse
	 *            判断是启用还是停用
	 * @return
	 * @throws BusinessException
	 */
	public String[] updateProcessValidation(String processPK,
			String sublfowInRange, boolean onUse, boolean isInbatchMode)
			throws BusinessException;

	/**
	 * 根据单据id和流程实例PK获取其流程定义的内容,还要获取流程中触发过的活动；<br>
	 * 再递归查询其所有子流程。 <li>用于流程轨迹的显示
	 * 
	 * @param billId
	 * @param procInstancePK
	 * @param iWfType
	 *            主流程类型，见<code>WorkflowTypeEnum.Approveflow</code>和
	 *            <code>WorkflowTypeEnum.Workflow</code>
	 * @return
	 * @throws BusinessException
	 */
	public ProcessRouteRes queryProcessRoute(String billId, String billType,
			String procInstancePK, int iWfType) throws BusinessException;

	public List<IActivity[]> queryFinishedActivities(String billId,
			String billType, int iWfType) throws BusinessException;

	/**
	 * 同步制单人或审批人的名称
	 * 
	 * @throws BusinessException
	 */
	public int syncParticipantNames() throws BusinessException;

	/**
	 * 查询某单据的审批流、工作流的流程图，并导出为PNG格式
	 * 
	 * @param billId
	 *            单据ID
	 * @param billType
	 *            单据类型PK，即编码
	 * @param iWorkflowtype
	 *            流程类型：1=审批流 3=工作流，见<code>IApproveflowConst
	 * @return
	 * @throws BusinessException
	 */
	public byte[] toPNGImage(String billId, String billType, int iWorkflowtype)
			throws BusinessException;

	/**
	 * 查询流程图，包含子流程
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
