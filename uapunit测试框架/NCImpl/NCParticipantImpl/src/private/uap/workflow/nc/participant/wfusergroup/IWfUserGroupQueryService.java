package uap.workflow.nc.participant.wfusergroup;

import uap.workflow.engine.core.ITask;
import nc.vo.pub.BusinessException;

/**
 * 流程用户组查询服务
 * @author guowl
 *
 */
public interface IWfUserGroupQueryService {

	public WFUserGroupDetailVO[] getUserGroupDetailVOByParentPK(String pk)throws BusinessException;
	
	public WFUserGroupVO getUserGroupVOByPK(String pk) throws BusinessException;
	
	public String[] queryUsersOfWfUserGroup(String wfUserGrouppk, String orgBelongOrg, ITask task) throws BusinessException;
	
	/**
	 * 查询所有的系统类型 编码、名称
	 * @return
	 * @throws BusinessException
	 */
	public WFUserGroupVO[] queryAllUserGroup() throws BusinessException;
	/**
	 * 根据条件查询流程用户组
	 * @return 
	 * @throws BusinessException
	 * */
	public WFUserGroupVO[] queryUserGroupByClause(String clause)  throws BusinessException;
}
