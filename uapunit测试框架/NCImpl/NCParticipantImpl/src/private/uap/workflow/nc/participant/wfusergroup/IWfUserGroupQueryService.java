package uap.workflow.nc.participant.wfusergroup;

import uap.workflow.engine.core.ITask;
import nc.vo.pub.BusinessException;

/**
 * �����û����ѯ����
 * @author guowl
 *
 */
public interface IWfUserGroupQueryService {

	public WFUserGroupDetailVO[] getUserGroupDetailVOByParentPK(String pk)throws BusinessException;
	
	public WFUserGroupVO getUserGroupVOByPK(String pk) throws BusinessException;
	
	public String[] queryUsersOfWfUserGroup(String wfUserGrouppk, String orgBelongOrg, ITask task) throws BusinessException;
	
	/**
	 * ��ѯ���е�ϵͳ���� ���롢����
	 * @return
	 * @throws BusinessException
	 */
	public WFUserGroupVO[] queryAllUserGroup() throws BusinessException;
	/**
	 * ����������ѯ�����û���
	 * @return 
	 * @throws BusinessException
	 * */
	public WFUserGroupVO[] queryUserGroupByClause(String clause)  throws BusinessException;
}
