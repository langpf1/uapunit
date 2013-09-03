package uap.workflow.nc.participant.wfusergroup;

import java.util.Collection;
import java.util.HashSet;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.UserGroupVO;
import nc.vo.uap.rbac.role.RoleGroupVO;
import nc.vo.uap.rbac.role.RoleVO;
import uap.workflow.app.participant.ParticipantFilterContext;
import uap.workflow.engine.core.ITask;

public class WfUserGroupQueryServiceImpl implements IWfUserGroupQueryService {

	@Override
	public WFUserGroupDetailVO[] getUserGroupDetailVOByParentPK(String pk)
			throws BusinessException {
		WFUserGroupDetailVO[] vos = null;
		try{
			UserGroupDMO dmo = new UserGroupDMO();
			vos = dmo.getUserGroupDetailVOBySuperPK(pk);
		}
		catch(Exception e)
		{
			Logger.error(e.getMessage(),e);
			throw new BusinessException(e.getMessage());
		}
		return vos;
	}

	@Override
	public WFUserGroupVO getUserGroupVOByPK(String pk) throws BusinessException {
		WFUserGroupVO[] vos = null;
		WFUserGroupVO vo = null;
		try{
			UserGroupDMO dmo = new UserGroupDMO();
			vos = dmo.getUserGroupVO(pk);
			if(vos.length>0) {
				vo = vos[0];
			}
		}
		catch(Exception e)
		{
			Logger.error(e.getMessage(),e);
			throw new BusinessException(e.getMessage());
		}
		return vo;	
	}
	
	public WFUserGroupDetailVO[] getUserGroupDetailVO(WFUserGroupVO vo) throws BusinessException {
		WFUserGroupDetailVO[] vos = null;
		try{
			UserGroupDMO dmo = new UserGroupDMO();
			vos = dmo.getUserGroupDetailVO(vo);

		}catch(Exception e)
		{
			Logger.error(e.getMessage(),e);
			throw new BusinessException(e.getMessage());
		}
		return vos;
	}
	
	public WFUserGroupVO[] queryAllUserGroup() throws BusinessException {
		Collection<WFUserGroupVO> vos = null;
		BaseDAO dao = new BaseDAO();
		try {
			vos = dao.retrieveAll(WFUserGroupVO.class);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return vos.toArray(new WFUserGroupVO[0]);
	}
	
	@Override
	public String[] queryUsersOfWfUserGroup(String wfUserGrouppk,
			String orgBelongOrg, ITask task) throws BusinessException {
		// 流程用户组的解析
		String[] list = null;
		WFUserGroupVO vo = UserGroupManager.getUserGroupVOByPK(wfUserGrouppk);
		if (vo.getDeftype().equals(
				Integer.valueOf(WfGroupType.DisperseType.getIntValue()).toString())) {
			// 离散用户
			list = getDisperseUserByWfUserGroupID(wfUserGrouppk, orgBelongOrg);
		} else {
			/*
			// 规则用户
			ParticipantFilterContext pfc = new ParticipantFilterContext();
			pfc.setParticipantId(task.getParticipantID());
			pfc.setParticipantType(task.getParticipantType());
			pfc.setSenderman(task.getSenderman());
			pfc.setBillEntity(task.getInObject());
			pfc.setForDispatch(false);
			// FIXME::查询出流程实例的制单人
			if(task.getWfProcessInstancePK() != null) {
				EnginePersistence persistenceDmo = new EnginePersistence();
				ProcessInstance instance;
				try {
					instance = persistenceDmo.loadProcessInstance(task
							.getWfProcessInstancePK());
					pfc.setBillmaker(instance.getBillMaker());
				} catch (DbException e) {
					Logger.error(e.getMessage(), e);
				}
			}
			list = getRuleUserByWfUserGroupID(wfUserGrouppk, orgBelongOrg, vo
					.getBilltype(), pfc, task.getPk_org(), task.getSenderman());
			*/
		}
		return list;
	}

	///prevParticipant 上一环节参与者
	private String[] getRuleUserByWfUserGroupID(String wfUserGrouppk,
			String pk_org, String billtype, ParticipantFilterContext context, 
			String pk_org_bill, String prevParticipant)
			throws BusinessException {
		WFUserGroupDetailVO[] detailVos = getUserGroupDetailVOByParentPK(wfUserGrouppk);
		if (detailVos == null || detailVos.length == 0)
			return null;

		/* 流程用户组规则型 子表pub_wfgroup_b中最多只有一条记录 */
		WFUserGroupDetailVO detailVO = detailVos[0];
		// context.set
		IWfUserGroupResolver wfUsergroupResolver = WfUserGroupResolverFactory
				.getInstance().getResolver(billtype, detailVO.getRule_code());
		HashSet<String> users = wfUsergroupResolver.queryUsers(billtype, context,
				detailVO.getRely_attribute(), pk_org_bill ,prevParticipant);
		return users.toArray(new String[0]);
	}

	private String[] getDisperseUserByWfUserGroupID(String pk, String pk_org)
			throws BusinessException {
		HashSet<String> list = new HashSet<String>();
		WFUserGroupDetailVO[] vos = getUserGroupDetailVOByParentPK(pk);
		for (int n = 0; n < vos.length; n++) {
			/** 得到该成员的PK */
			String pk_member = ((WFUserGroupDetailVO) vos[n]).getPk_member();
			/** 得到该成员的类型,是角色,还是角色组,还是用户和用户组 */
			String pk_type = ((WFUserGroupDetailVO) vos[n]).getRule_type();
			// USERTYPE = "01"; USERGROUPTYPE = "02"; ROLETYPE = "03";
			// ROLEGROUPTYPE = "04";
			/** 用户 */
			IUserManageQuery userManageQueryService = (IUserManageQuery) NCLocator
					.getInstance().lookup(IUserManageQuery.class.getName());
			IRoleManageQuery roleManageQueryService = (IRoleManageQuery) NCLocator
					.getInstance().lookup(IRoleManageQuery.class.getName());
			if (pk_type.equals(WfUserGroupType.User.getValue())) {
				UserVO user = userManageQueryService.getUser(pk_member);
				list.add(user.getCuserid());
			} else if (pk_type.equals(WfUserGroupType.UserGroup.getValue())) {
				UserGroupVO groupVO = (UserGroupVO) new BaseDAO().retrieveByPK(
						UserGroupVO.class, pk_member);
				if (groupVO != null) {
					UserVO[] users = userManageQueryService
							.queryAllUserinUserGroup(groupVO.getPk_usergroup(),
									true, false);
					for (int m = 0; m < users.length; m++) {
						list.add(users[m].getCuserid());
					}
				}
			} else if (pk_type.equals(WfUserGroupType.Role.getValue())) {
				UserVO[] users = userManageQueryService.queryUserByRole(
						pk_member, pk_org);
				for (int i = 0; i < users.length; i++) {
					list.add(users[i].getCuserid());
				}
			} else {
				RoleGroupVO rolegroups = roleManageQueryService
						.queryRoleGroupByID(pk_member);
				RoleVO[] roles = rolegroups.getRoles();
				for (int a = 0; a < roles.length; a++) {
					UserVO[] users = userManageQueryService.queryUserByRole(
							roles[a].getPk_role(), pk_org);
					for (int b = 0; b < users.length; b++) {
						list.add(users[b].getCuserid());
					}
				}
			}

		}
		return list.toArray(new String[0]);
	}

	@Override
	public WFUserGroupVO[] queryUserGroupByClause(String clause)
			throws BusinessException {
		Collection<WFUserGroupVO> vos = null;
		BaseDAO dao = new BaseDAO();
		try {
			vos = dao.retrieveByClause(WFUserGroupVO.class, clause);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return vos.toArray(new WFUserGroupVO[vos.size()]);
	}
}
