package uap.workflow.app.participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import uap.workflow.nc.participant.RoleParticipantType;
import uap.workflow.nc.participantfilter.ParticipantFilterFactory;
import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.reslet.application.receiveData.Role;
import uap.workflow.restlet.application.RuntimeConstants;
import uap.workflow.app.participant.IParticipant;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.constant.IRoleConst;
import nc.vo.uap.rbac.excp.RbacException;
import nc.vo.uap.rbac.role.RoleVO;

public class ParticipantServiceImpl implements IParticipantService{
	
	
	public List<String> getUsers(ParticipantContext participantContext)
	{
		IParticipantAdapter participantAdapter = null;
		IParticipantFilter participantFilter = null;
		List<String> allUserIds= new ArrayList<String>();
		List<String> hsUserIds= null;
		List<IParticipant> participants = participantContext.getParticipants();
		for(IParticipant participant : participants)
		{
			// 先取用户列表， 跟是否有过滤无关
			try {
				participantAdapter = ParticipantFactory.getInstance().getParticipantAdapter(participant.getParticipantType());
				List<IParticipant> newParticipants = new ArrayList<IParticipant>();
				newParticipants.add(participant);
				participantContext.setParticipants(newParticipants);
				hsUserIds = participantAdapter.findUsers(participantContext);
				if (hsUserIds == null || hsUserIds.size() == 0)
					throw new ParticipantException(NCLangResOnserver.getInstance()
							.getStrByID("pfworkflow", "UPPpfworkflow-000259")/**
					 * @res
					 *      "找不到执行人"
					 */
					);
			} catch (ParticipantException e) {
				Logger.error(e.getMessage(), e);
				throw new ParticipantException(NCLangResOnserver.getInstance()
						.getStrByID("platformapp",
								"PfParticipantServiceImpl-000000", null,
								new String[] { e.getMessage() })/*
																 * 查询参与者所有用户时出现异常={0}
																 */);
			}
	
			IParticipantFilterType filterType = participant.getParticipantFilterType();
			if (filterType != null)
			{
				//限定模式是 指定组织 或 无限定时， 不需要进行过滤 pf为null
				///???没有对应变量
				///participantFilter = ParticipantFilterFactory.getInstance().getParticipantFilter(filterType,  task.getBillType());
				participantFilter = ParticipantFilterFactory.getInstance().getParticipantFilter(filterType,  "");
			}
			if (participantFilter != null) {
				// 使用限定模式 过滤出待指派的用户
				hsUserIds = filterUsers(participantContext, participant, participantFilter, hsUserIds);
				if (hsUserIds == null || hsUserIds.size() == 0)
					throw new ParticipantException(NCLangResOnserver.getInstance()
							.getStrByID("platformapp",
									"PfParticipantServiceImpl-000001")/* 参与者限定后无执行人 */);
	
			}
			allUserIds.addAll(hsUserIds);
		}
		return allUserIds;
	}
	
	

	private List<String> filterUsers(ParticipantContext participantContext, IParticipant participant, 
			IParticipantFilter pf, List<String> list) 
	{
		ParticipantFilterContext pfc = new ParticipantFilterContext();
		pfc.setParticipant(participant);
		pfc.setSenderman(participantContext.getTask().getOwner());
		///???没有对应变量
		///pfc.setBillEntity(task.getInObject());
		pfc.setUserList(list);

		///???没有对应变量
		/*
		// wcj 2011-8-22
		// 处理限定的来源，目前针对代理人
		// 会签情况下，应该按照所有参与者的限定进行处理
		FilterSourceMgr filter = new FilterSourceMgr();
		pfc.setFilterSource(filter.getFilterSource(task));
		if(pfc.getParticipantType().equals(OrganizeUnitTypes.RESPONSIBILITY.toString())){		
			try {
				pfc.setFilterOrgs4Responsibility(NCLocator.getInstance().lookup(IWorkflowAdmin.class).findFilterOrgs4Responsibility(task));
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				Logger.error(e.getMessage());
			}
		}
		*/

		// yanke1 2011-6-13
		// 此处try块中将抛出FlowNextException 后续代码中将根据是否异常来决定程序运行流程
		// 此处若捕获FlowNextException并包装为BusinessException，则后续代码中无法正常流转
		// 因此，修改为单独捕获FlowNextException并直接抛出
		///???没有对应变量
		/*
		try {
			IWorkflowMachine machine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			if (machine != null && task.getWfProcessInstancePK() != null)
				pfc.setBillmaker(machine.getBillMaker(task
						.getWfProcessInstancePK()));
			*/
			return pf.filterUsers(pfc);
			/*
		} 
		catch (FlowNextException e) {
			throw e;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new ParticipantException(NCLangResOnserver.getInstance()
					.getStrByID("platformapp",
							"PfParticipantServiceImpl-000002", null,
							new String[] { e.getMessage() })/* 参与者限定出现异常={0} * /);
		}
		*/
	}

	/**
	 * @author zhailzh
	 * 根据参与者的类型得到该类型下的所有的参与者的信息
	 * */

	@Override
	public List<Participant> getAllUsersbyType(Participant currentparticipant) {
		//绕过安全机制
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		List<Participant> userlist = null;
		userlist =  getAllOperatoruser(currentparticipant);
		return userlist;
	}



	private List<Participant> getAllOperatoruser(Participant currentparticipant) {
		
	    IUserManageQuery iCorpService = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO currentUsers = getUserByuserpk(currentparticipant.getParticipantID(),iCorpService);
		
		List<Participant>  participantlist = new ArrayList<Participant>();
		UserVO[] temp = null;
		try {
			temp = iCorpService.queryAllUsersByOrg(currentUsers.getPk_org());
//			temp = iCorpService.queryUserByClause(arg0)
			for (int i = 0; i < (temp == null ? 0 : temp.length); i++) {
				adaptorParticipantlist(participantlist,temp[i]);
				}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return participantlist;
	}
	/*由nc的ORG中的user转会为participant，就是“裁剪”user*/
	private void adaptorParticipantlist(List<Participant> participantlist, UserVO temp) {		
		Participant particiantTemp = new Participant();
		particiantTemp.setName(temp.getUser_name());
		particiantTemp.setParticipantID(temp.getPrimaryKey());
		participantlist.add(particiantTemp);
	}

//	// 查询当前集团下的所有业务单元
//	private Vector<OrgVO> getOrgsOfThisGroup(String group) {
//		Vector<OrgVO> m_vecOrgsOfThisGroup = new Vector<OrgVO>();
//		try {
//			IOrgUnitQryService corpService = (IOrgUnitQryService) NCLocator.getInstance().lookup(IOrgUnitQryService.class.getName());
//			OrgVO[] aryCorps = corpService.queryAllOrgUnitVOsByGroupID(group, true, true);//登陆者：zhai，group为其所在group
//			for (int i = 0; i < (aryCorps == null ? 0 : aryCorps.length); i++) {
//				m_vecOrgsOfThisGroup.addElement(aryCorps[i]);
//			}
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//		}
//		return m_vecOrgsOfThisGroup;
//	}
	
	@Override
	public List<Role> getRolesByType(Role currentparticipant) {
		List<Role> rolelist = new ArrayList<Role>();
		// 暂时没有用到这个类型，因为请求的是role类型，在返回的数据中，故不在返回的数据中再添加类型
		IParticipantType participanttype = new RoleParticipantType();
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(), "ncc10".getBytes());// 绕过登陆

		RoleVO[] roles = null;
		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO user = getUserByuserpk(currentparticipant.getRoleID(), iCorpService);
		try {
			roles = NCLocator.getInstance().lookup(IRoleManageQuery.class)
					.queryRoleByOrg(user.getPk_org(), IRoleConst.BUSINESS_TYPE);
		} catch (RbacException e) {
			e.printStackTrace();
		}
		if (roles.length > 0) {
			rolelist = adaptorRolelist(rolelist, participanttype, roles);
		}
		return rolelist;
	}

	/**
	 * 组装participant的role
	 */
	private List<Role> adaptorRolelist(List<Role> rolelist, IParticipantType participanttype, RoleVO[] roles) {
		for(RoleVO role : roles){
			Role pariticipantrole = new Role();
			pariticipantrole.setRoleID(role.getRole_code());
			pariticipantrole.setName(role.getRole_name());
			rolelist.add(pariticipantrole);
		}
		return rolelist;
	}



	private UserVO getUserByuserpk(String userpara, IUserManageQuery iCorpService){
		UserVO aa = new UserVO();
		try {
			aa = iCorpService.getUser(userpara);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return aa;
	}
	
}
