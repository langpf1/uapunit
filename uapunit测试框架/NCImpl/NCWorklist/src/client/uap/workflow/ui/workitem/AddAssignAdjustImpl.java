package uap.workflow.ui.workitem;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.wfengine.core.util.CoreUtilities;
import nc.vo.wfengine.definition.IApproveflowConst;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.XPDLNames;
import uap.workflow.vo.WorkflownoteVO;

/**
 * ��ǩʵ����
 * @author 
 *
 */
@SuppressWarnings("unchecked")
public class AddAssignAdjustImpl implements IApplicationRuntimeAdjust {
	private WorkflownoteVO m_workflow;

	/**
	 * ִ�м�ǩ
	 */
	public WorkflownoteVO adjust(ApplicationRuntimeAdjustContext context) throws BusinessException {
		/* xry TODO:
		m_workflow = context.getWorkFlow();
		String processDefPK = m_workflow.getTaskInstanceVO().getPk_Process_Def();
		String processInstPK = m_workflow.getTaskInstanceVO().getPk_Process_Instance();
		String activityDefPK = m_workflow.getTaskInstanceVO().getActivityID();

		IUAPQueryBS dao = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection<ApproveFlowAdjustVO> adjustVoCol = dao.retrieveByClause(ApproveFlowAdjustVO.class,
				"pk_wf_instance='" + processInstPK + "'");
		ApproveFlowAdjustVO vo;
		if (adjustVoCol == null || adjustVoCol.size() == 0) {
			vo = new ApproveFlowAdjustVO();
			vo.setPk_wf_instance(processInstPK);
		} else
			vo = adjustVoCol.iterator().next();

		IProcessDefinition wp = null;
		try {
			/* xry TODO:
			if (StringUtil.isEmptyWithTrim(vo.getContent()))
				wp = PfDataCache.getWorkflowProcess(processDefPK, processInstPK);
			else
				wp = UfXPDLParser.getInstance().parseProcess(vo.getContent());
			 * / 
			IActivity activity = wp.findActivity(activityDefPK);

			int style = context.getStyle();
			List<OrganizeUnit> orgUnits = (List<OrganizeUnit>) context.getUserObject();

			switch (style) {
				case ADDASSIGN_STYLE_SERIAL:
					addBySerial(wp, activity, orgUnits);
					break;
				case ADDASSIGN_STYLE_PARALLEL:
					addByParallel(wp, activity, orgUnits);
					break;
				case ADDASSIGN_STYLE_COOPERATION:
					addByCooperation(wp, activity, orgUnits);
					break;
				//				case ADDASSIGN_STYLE_NOTICE:
				//					addByNotice(wp,activity,orgUnits);
				//					break;
			}

			//���²�������ͼ
			reLayoutProcessGrap(wp);

			//���л�wpΪxpdl
			String content = UfXPDLSerializer.getInstance().serialize(wp);
			vo.setContent(content);

			//����У���������
			NCLocator.getInstance().lookup(IWorkflowDefine.class).saveProcessAfterAdjust(vo, wp);
			PfDataCache.synchronizeWorkflowProcess(processInstPK, wp);
			PfDataCache.synchronizeWorkflowProcess(processDefPK, null);
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
		*/
		return null;
	}

	/**
	 * ���м�ǩ
	 * @param wp
	 * @param orgUnits
	 */
	private void addBySerial(IProcessDefinition wp, IActivity currentAct, List<OrganizeUnit> orgUnits) {
		/*
		List<IActivity> acts = createActivityWithParticipant(wp, currentAct, orgUnits);

		String actDefPK = currentAct.getId();

		List allTrans = wp.getTransitions();

		List<Transition> successTrans = getSuccessTransitionBySrcActPK(wp, actDefPK);

		List<Transition> newTrans = new ArrayList<Transition>();

		//�������������ڲ�ת��
		for (int i = 1; i < acts.size(); i++) {
			Transition tran = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
			tran.setFromActivity(acts.get(i - 1));
			tran.setToActivity(acts.get(i));
			// ���Ĭ����չ����
			CoreUtilities.genDefaultExtAttr4Transition(tran, true);

			newTrans.add(tran);
		}

		//���ɵ�ǰ�����һ�����ӻ��·��
		Transition firstTran = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
		firstTran.setFromActivity(currentAct);
		firstTran.setFrom(currentAct.getId());
		firstTran.setToActivity(acts.get(0));
		firstTran.setTo(acts.get(0).getId());
		// ���Ĭ����չ����
		CoreUtilities.genDefaultExtAttr4Transition(firstTran, true);
		newTrans.add(firstTran);

		//�����������һ�������ǰ��ĺ�̻��·��
		IActivity fromAct = acts.get(acts.size() - 1);
		for (int i = 0; i < successTrans.size(); i++) {
			Transition successTran = successTrans.get(i);
			IActivity toAct = successTran.getToActivity();
			Transition tran = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
			tran.setFromActivity(fromAct);
			tran.setFrom(fromAct.getId());
			tran.setToActivity(toAct);
			tran.setTo(toAct.getId());
			CoreUtilities.genDefaultExtAttr4Transition(tran, true);
			newTrans.add(tran);
		}

		//ɾ����ǰ���ԭ���·��
		allTrans.removeAll(successTrans);
		//������·��
		allTrans.addAll(newTrans);

		if (successTrans.size() == 0) {
			//��ǰ�����������
			setEndActivity(wp, actDefPK, fromAct.getId());
		}
		*/
	}

	private void setEndActivity(IProcessDefinition wp, String oldActId, String newActId) {
		String extAttrValue = "";
		extAttrValue = (String) wp.getProperty(XPDLNames.END_OF_WORKFLOW);
		if (extAttrValue.contains(oldActId)) {
			int index = extAttrValue.indexOf("@");
			String actId = extAttrValue.substring(0, index);
			extAttrValue = extAttrValue.replaceFirst(actId, newActId);
			wp.setProperty(XPDLNames.END_OF_WORKFLOW, extAttrValue);
		}
	}

	/**
	 * ���м�ǩ
	 * @param wp
	 * @param orgUnits
	 */
	private void addByParallel(IProcessDefinition wp, IActivity currentAct, List<OrganizeUnit> orgUnits) {
		/* xry TODO:
		List<IActivity> acts = createActivityWithParticipant(wp, currentAct, orgUnits);

		String actDefPK = currentAct.getId();

		List allTrans = null;//xry TODO:wp.getTransitions();

		List<ITransition> successTrans = getSuccessTransitionBySrcActPK(wp, actDefPK);

		List<ITransition> newTrans = new ArrayList<ITransition>();

		//����ת�����ƹ�ϵ
		List transitionRestrictions = null;//xry TODO:currentAct.getTransitionRestrictions();
		TransitionRestriction transitionRestriction = null;
		if (transitionRestrictions != null && transitionRestrictions.size() > 0) {
			transitionRestriction = (TransitionRestriction) transitionRestrictions.get(0);
		}
		//�����ǰ���̺�̽ڵ�Ϊ�����������һ��ڵ㣬�ۺ����ӽڵ�����ڵ㣬Ȼ��ʹ����ڵ��������ڵ�
		boolean isAddAggNode = false;
		IActivity aggAct = null;
		if (successTrans.size() > 1) {
			isAddAggNode = true;
			aggAct = null;//xry TODO:new ActivityEx(GuidUtils.generate(), "R", wp);
			//xry TODO:aggAct.setType("��");

			//��������ת��Լ��
			TransitionRestriction aggTranRest = new TransitionRestriction();
			aggTranRest.setJoin(new Join(SplitJoinType.AND));
			aggTranRest.setSplit(new Split(transitionRestriction.getSplit().getType()));
			/*xry TODO:
			aggAct.getTransitionRestrictions().add(aggTranRest);

			Route r = new Route();
			aggAct.setRoute(r);
			// Ĭ����ͼԪ��ͼ��
			aggAct.setIcon(ResManager.getResourceString("DefaultRouteCellIcon"));
			//���Ĭ����չ����
			CoreUtilities.fillDefaultExtAttr4Route(aggAct);

			wp.getActivities().add(aggAct);
			* /
		} else {
			IActivity toAct = successTrans.get(0).getDestination();
			List toActTransitionRestrictions = null;//xry TODO:toAct.getTransitionRestrictions();
			TransitionRestriction toActTransitionRestriction = null;
			if (toActTransitionRestrictions.size() == 0) {
				toActTransitionRestriction = new TransitionRestriction();
				toActTransitionRestriction.setJoin(new Join(SplitJoinType.AND));
				toActTransitionRestrictions.add(toActTransitionRestriction);
			} else {
				toActTransitionRestriction = (TransitionRestriction) toActTransitionRestrictions.get(0);
				toActTransitionRestriction.setJoin(new Join(SplitJoinType.AND));
			}
		}

		//���õ�ǰ�ĻԼ��Ϊ���롱�Ĺ�ϵ
		transitionRestriction.setSplit(new Split(SplitJoinType.AND));

		//���ӵ�ǰ����»��·��
		for (int i = 0; i < acts.size(); i++) {
			IActivity toAct = acts.get(i);
			Transition tran1 = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
			tran1.setFromActivity(currentAct);
			tran1.setFrom(currentAct.getId());
			tran1.setToActivity(toAct);
			tran1.setTo(toAct.getId());
			CoreUtilities.genDefaultExtAttr4Transition(tran1, true);
			newTrans.add(tran1);

			if (isAddAggNode) {
				Transition tran2 = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
				tran2.setFromActivity(toAct);
				tran2.setFrom(toAct.getId());
				tran2.setToActivity(aggAct);
				tran2.setTo(aggAct.getId());
				CoreUtilities.genDefaultExtAttr4Transition(tran2, true);
				newTrans.add(tran2);
			} else {
				Transition successTran = successTrans.get(0);
				Transition tran2 = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
				tran2.setFromActivity(toAct);
				tran2.setFrom(toAct.getId());
				tran2.setToActivity(successTran.getToActivity());
				tran2.setTo(successTran.getToActivity().getId());
				CoreUtilities.genDefaultExtAttr4Transition(tran2, true);
				newTrans.add(tran2);
			}
		}

		//������ӾۺϽڵ㣬�����ӾۺϽڵ����̻�Ĺ���
		if (isAddAggNode) {
			for (int j = 0; j < successTrans.size(); j++) {
				Transition successTran = successTrans.get(0);
				Transition tran = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
				tran.setFromActivity(aggAct);
				tran.setFrom(aggAct.getId());
				tran.setToActivity(successTran.getToActivity());
				tran.setTo(successTran.getToActivity().getId());
				CoreUtilities.genDefaultExtAttr4Transition(tran, true);
				newTrans.add(tran);
			}
		}

		//ɾ����ǰ���ԭ���·��
		allTrans.removeAll(successTrans);
		//������·��
		allTrans.addAll(newTrans);
		*/
	}

	/**
	 * Э����ǩ
	 * @param wp
	 * @param orgUnits
	 * @throws BusinessException 
	 */
	private void addByCooperation(IProcessDefinition wp, IActivity currentAct, List<OrganizeUnit> orgUnits)
			throws BusinessException {
		if (orgUnits.size() == 0)
			return;
		ArrayList<String> approve_userIds = new ArrayList<String>();
		for (int i = 0; i < orgUnits.size(); i++) {
			approve_userIds.add(orgUnits.get(i).getPk());
		}
		m_workflow.getTaskInstanceVO().setCreate_type(TaskInstanceCreateType.AfterAddSign.getIntValue());
		m_workflow.setExtApprovers(approve_userIds);

		//NCLocator.getInstance().lookup(IWorkflowAdmin.class).addApprover(m_workflow);

	}

	/**
	 * ֪���ǩ
	 * @param wp
	 * @param orgUnits
	 */
	//	private void addByNotice(IProcessDefinition wp,IActivity currentAct,List<OrganizeUnit> orgUnits){
	//		List<IActivity> acts = createActivityWithParticipant(wp,currentAct,orgUnits);
	//		
	//		List<Transition> newTrans = new ArrayList<Transition>();
	//		
	//		//�������������ڲ�ת��
	//		for(int i=0;i<acts.size();i++){
	//			Transition tran = new BasicTransitionEx(GuidUtils.generate(), "", "", "");
	//			tran.setFromActivity(currentAct);
	//			tran.setToActivity(acts.get(i));
	//			// ���Ĭ����չ����
	//			CoreUtilities.genDefaultExtAttr4Transition(tran,true);
	//			
	//			Map attrs = tran.getExtendedAttributes();
	//			attrs.put(XPDLNames.ROUTE_ISEXECUTABLE, "N");
	//			
	//			newTrans.add(tran);
	//		}
	//	}	

	/**
	 * ����ѡ�����Ա�ͽ�ɫ�����
	 * @param wp
	 * @param orgUnits
	 * @return
	 */
	private List<IActivity> createActivityWithParticipant(IProcessDefinition wp, IActivity currentAct,
			List<OrganizeUnit> orgUnits) {
		List<IActivity> acts = new ArrayList<IActivity>();
		/* xry TODO:
		Map srcExtAttrs = currentAct.getExtendedAttributes();

		for (int i = 0; i < orgUnits.size(); i++) {
			OrganizeUnit orgUnit = orgUnits.get(i);
			/** 1�������������ϢVO* /
			int orgUnitType = orgUnit.getOrgUnitType();
			String OrganizeUnitType = null;
			if (orgUnitType == ParticipantType.ROLE_INT) {
				OrganizeUnitType = "ROLE";
			} else {
				OrganizeUnitType = "OPERATOR";
			}
			Participant partVo = new BasicParticipantEx(GuidUtils.generate(), orgUnit.getName(),
					ParticipantType.HUMAN);
			//��Ҫ����֯Ԫ�ص�PK������ ���浽XPDL��Participant����չ������
			partVo.getExtendedAttributes().put(XPDLNames.ORGANIZE_UNIT_PK, orgUnit.getPk());
			partVo.getExtendedAttributes().put(XPDLNames.ORGANIZE_UNIT_TYPE, OrganizeUnitType);
			partVo.getExtendedAttributes().put(XPDLNames.ORGANIZE_UNIT_BELONGORG, orgUnit.getPkOrg());

			wp.getParticipants().add(partVo);

			/** 2��������ϢVO* /
			GenericActivityEx actVo = new GenericActivityEx(GuidUtils.generate(), partVo.getName(), wp);
			actVo.setPerformer(partVo.getId());
			actVo.setPerformerName(partVo.getName());
			actVo.getTransitionRestrictions().clear();

			TransitionRestriction transitionRestriction = new TransitionRestriction();
			transitionRestriction.setJoin(new Join());
			transitionRestriction.setSplit(new Split());
			actVo.getTransitionRestrictions().add(transitionRestriction);

			//��չ����
			Map extAttrs = actVo.getExtendedAttributes();
			String newDimen = "0;0;0;0";
			extAttrs.put(XPDLNames.DIMENSION, newDimen);
			extAttrs.put(XPDLNames.TEXT_POSITION, srcExtAttrs.get(XPDLNames.TEXT_POSITION));
			extAttrs.put(XPDLNames.BACKGROUND, srcExtAttrs.get(XPDLNames.BACKGROUND));

			if (orgUnitType == ParticipantType.ROLE_INT) {
				actVo.setIcon("nc/ui/wfengine/designer/resources/role.gif");
				extAttrs.put(XPDLNames.RACE_MODAL, RaceModal.RACE.getValue());
			} else {
				actVo.setIcon("nc/ui/wfengine/designer/resources/user.gif");
			}

			// ��ֵӦ�ó�������
			ToolSet toolset = new ToolSet();
			Tool tool = new Tool(ActivityTypeEnum.Checkbill.getTag());
			tool.setToolType(ToolType.APPLICATION);
			toolset.getTools().add(tool);
			actVo.setImplementation(toolset);
			actVo.setAppId(ActivityTypeEnum.Checkbill.getTag());
			actVo.setAppName(ActivityTypeEnum.Checkbill.toString());
			actVo.setType("�����");

			// ���Ĭ������
			CoreUtilities.fillDefaultExtAttr4CheckBillActivity(actVo);
			//�Զ����������Բ��ܹ�ѡ
			actVo.getExtendedAttributes().put(XPDLNames.AUTO_APPROVE, "false");

			wp.getActivities().add(actVo);

			acts.add(actVo);
		}
		*/
		return acts;
	}

	private List<ITransition> getSuccessTransitionBySrcActPK(IProcessDefinition wp, String srcActPK) {
		List<ITransition> trans = new ArrayList<ITransition>();

		/*TODO:
		List allTrans = wp.getTransitions();
		for (int i = 0; i < allTrans.size(); i++) {
			ITransition tran = (ITransition) allTrans.get(i);
			if (srcActPK.equals(tran.getFrom())) {
				trans.add(tran);
			}
		}
		*/
		return trans;
	}

	/**
	 * ���²�������ͼ
	 * @param wp
	 */
	private void reLayoutProcessGrap(IProcessDefinition wp) {
		/* xry TODO:
		List allTrans = wp.getTransitions();

		//�޸�·��Ϊ"�Զ�����"
		for (int i = 0; i < allTrans.size(); i++) {
			ITransition tran = (ITransition) allTrans.get(i);
			tran.setProperty(XPDLNames.ROUTING_TYPE, RoutingType.SIMPLE.getTag());
		}

		List tempActs = new ArrayList();

		//�Ƶ��ڵ�λ�ñ��ֲ��䣬����Ľڵ����Ƶ��ڵ���Ϊ���ս��е���
		IActivity startAct = wp.getInitial();
		tempActs.add(startAct);

		List<IActivity> startActs = new ArrayList<IActivity>();
		startActs.add(startAct);

		//��ȡ��ʼ�ڵ��x��y���꣬��Ϊ�������̵Ĳο�����
		String dimension = (String) startAct.getProperty(XPDLNames.DIMENSION);
		String[] dims = dimension.split(";");
		int x = Integer.parseInt(dims[0]);
		int y = Integer.parseInt(dims[1]);

		//���²���
		reLayoutSuccessActivity(wp, x, y, startActs, tempActs);
		*/
	}

	/**
	 * ���²����Ƶ��ڵ�ĺ����ڵ�
	 * @param wp
	 * @param currentAct
	 * @param layoutedActs
	 */
	private void reLayoutSuccessActivity(IProcessDefinition wp, int _x_pre, int _y_center,
			List<IActivity> currentActs, List layoutedActs) {
		List<IActivity> successActs = new ArrayList<IActivity>();
		List<ITransition> successTrans = new ArrayList<ITransition>();
		for (IActivity currentAct : currentActs) {
			successTrans.addAll(getSuccessTransitionBySrcActPK(wp, currentAct.getId()));
		}

		if (successTrans.size() == 0) { return; }

		for (ITransition tran : successTrans) {
			IActivity toAct = tran.getDestination();
			if (!successActs.contains(toAct))
				successActs.add(toAct);
		}

		//���������Ӽ�ˮƽ����
		int x = _x_pre + horizontaSpaceLength;

		//������̻�Ĳ���
		int size = successActs.size();
		for (int i = 0; i < size; i++) {
			IActivity act = successActs.get(i);
			if (layoutedActs.contains(act)) {
				continue;
			} else {
				layoutedActs.add(act);
			}

			if (size % 2 == 0) {
				int intNum = (i + 1) / 2;
				int remainNum = (i + 1) % 2;
				int newY = _y_center;
				int changedSpace = verticalSpacheLength * (intNum + remainNum);
				if (remainNum > 0) {
					newY = _y_center - changedSpace;
				} else {
					newY = _y_center + changedSpace;
				}
				String newDimen = x + ";" + newY + ";" + IApproveflowConst.DEFAULT_APPROVE_WIDTH + ";"
						+ IApproveflowConst.DEFAULT_APPROVE_HEIGHT;
				act.setProperty(XPDLNames.DIMENSION, newDimen);
			} else {
				if (i == 0) {
					String newDimen = x + ";" + _y_center + ";" + IApproveflowConst.DEFAULT_APPROVE_WIDTH
							+ ";" + IApproveflowConst.DEFAULT_APPROVE_HEIGHT;
					act.setProperty(XPDLNames.DIMENSION, newDimen);
				} else {
					int intNum = i / 2;
					int remainNum = i % 2;
					int newY = _y_center;
					int changedSpace = verticalSpacheLength * (intNum + remainNum);
					if (remainNum > 0) {
						newY = _y_center - changedSpace;
					} else {
						newY = _y_center + changedSpace;
					}
					String newDimen = x + ";" + newY + ";" + IApproveflowConst.DEFAULT_APPROVE_WIDTH + ";"
							+ IApproveflowConst.DEFAULT_APPROVE_HEIGHT;
					act.setProperty(XPDLNames.DIMENSION, newDimen);
				}
			}
			/*//xry TODO:
			//���������ڵ�Ĳ���
			if (act.isExitActivity()) {
				adjustEndNode(wp, act.getId(), x, _y_center);
			}
			*/
		}

		//�������������
		reLayoutSuccessActivity(wp, x, _y_center, successActs, layoutedActs);
	}

	public void adjustEndNode(IProcessDefinition wp, String endActDefPK, int x,
			int _y_center) {
		// ��ȡWP�������չ����
		String extAttrValue = "";
		String key = "";
		key = XPDLNames.END_OF_WORKFLOW;
		if (key.indexOf(XPDLNames.END_OF_WORKFLOW) >= 0) {
			extAttrValue = (String) wp.getProperty(key);
			int index = extAttrValue.indexOf("@");
			String actId = extAttrValue.substring(0, index);
			if (endActDefPK.equals(actId)) {
				String[] infos = CoreUtilities.tokenize(extAttrValue, "@");
				String dimension = infos[2];
				String[] dims = dimension.split(";");

				int y = _y_center + verticalSpacheLength * 2;
				dimension = (x + 10) + ";" + y + ";" + dims[2] + ";" + dims[3];

				extAttrValue = infos[0] + "@" + infos[1] + "@" + dimension;

				for (int i = 3; i < infos.length; i++) {
					extAttrValue = extAttrValue + "@" + infos[i];
				}
				wp.setProperty(key, extAttrValue);
			}
		}
	}
	
	private final int horizontaSpaceLength = 100;
	private final int verticalSpacheLength = 50;
}
