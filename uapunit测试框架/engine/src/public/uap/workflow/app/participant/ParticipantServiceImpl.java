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
			// ��ȡ�û��б� ���Ƿ��й����޹�
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
					 *      "�Ҳ���ִ����"
					 */
					);
			} catch (ParticipantException e) {
				Logger.error(e.getMessage(), e);
				throw new ParticipantException(NCLangResOnserver.getInstance()
						.getStrByID("platformapp",
								"PfParticipantServiceImpl-000000", null,
								new String[] { e.getMessage() })/*
																 * ��ѯ�����������û�ʱ�����쳣={0}
																 */);
			}
	
			IParticipantFilterType filterType = participant.getParticipantFilterType();
			if (filterType != null)
			{
				//�޶�ģʽ�� ָ����֯ �� ���޶�ʱ�� ����Ҫ���й��� pfΪnull
				///???û�ж�Ӧ����
				///participantFilter = ParticipantFilterFactory.getInstance().getParticipantFilter(filterType,  task.getBillType());
				participantFilter = ParticipantFilterFactory.getInstance().getParticipantFilter(filterType,  "");
			}
			if (participantFilter != null) {
				// ʹ���޶�ģʽ ���˳���ָ�ɵ��û�
				hsUserIds = filterUsers(participantContext, participant, participantFilter, hsUserIds);
				if (hsUserIds == null || hsUserIds.size() == 0)
					throw new ParticipantException(NCLangResOnserver.getInstance()
							.getStrByID("platformapp",
									"PfParticipantServiceImpl-000001")/* �������޶�����ִ���� */);
	
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
		///???û�ж�Ӧ����
		///pfc.setBillEntity(task.getInObject());
		pfc.setUserList(list);

		///???û�ж�Ӧ����
		/*
		// wcj 2011-8-22
		// �����޶�����Դ��Ŀǰ��Դ�����
		// ��ǩ����£�Ӧ�ð������в����ߵ��޶����д���
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
		// �˴�try���н��׳�FlowNextException ���������н������Ƿ��쳣������������������
		// �˴�������FlowNextException����װΪBusinessException��������������޷�������ת
		// ��ˣ��޸�Ϊ��������FlowNextException��ֱ���׳�
		///???û�ж�Ӧ����
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
							new String[] { e.getMessage() })/* �������޶������쳣={0} * /);
		}
		*/
	}

	/**
	 * @author zhailzh
	 * ���ݲ����ߵ����͵õ��������µ����еĲ����ߵ���Ϣ
	 * */

	@Override
	public List<Participant> getAllUsersbyType(Participant currentparticipant) {
		//�ƹ���ȫ����
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
	/*��nc��ORG�е�userת��Ϊparticipant�����ǡ��ü���user*/
	private void adaptorParticipantlist(List<Participant> participantlist, UserVO temp) {		
		Participant particiantTemp = new Participant();
		particiantTemp.setName(temp.getUser_name());
		particiantTemp.setParticipantID(temp.getPrimaryKey());
		participantlist.add(particiantTemp);
	}

//	// ��ѯ��ǰ�����µ�����ҵ��Ԫ
//	private Vector<OrgVO> getOrgsOfThisGroup(String group) {
//		Vector<OrgVO> m_vecOrgsOfThisGroup = new Vector<OrgVO>();
//		try {
//			IOrgUnitQryService corpService = (IOrgUnitQryService) NCLocator.getInstance().lookup(IOrgUnitQryService.class.getName());
//			OrgVO[] aryCorps = corpService.queryAllOrgUnitVOsByGroupID(group, true, true);//��½�ߣ�zhai��groupΪ������group
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
		// ��ʱû���õ�������ͣ���Ϊ�������role���ͣ��ڷ��ص������У��ʲ��ڷ��ص����������������
		IParticipantType participanttype = new RoleParticipantType();
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(), "ncc10".getBytes());// �ƹ���½

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
	 * ��װparticipant��role
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
