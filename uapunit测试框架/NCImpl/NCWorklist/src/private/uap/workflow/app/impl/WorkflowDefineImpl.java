package uap.workflow.app.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.vo.pub.BusinessException;
import uap.workflow.admin.IWFDefineService;
import uap.workflow.admin.IWorkflowDefine;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.monitor.ProcessRouteRes;

/**
 * �����������߼���BS�ӿ�ʵ��
 * 
 * @author �׾� 2005-8
 */
public class WorkflowDefineImpl implements IWorkflowDefine {

	public void deleteDefinitionByPK(String pk, String pk_group)
			throws BusinessException {
		NCLocator.getInstance().lookup(IWFDefineService.class).deleteDefinitionByPK(pk, pk_group);
	}

	public IProcessDefinition findDefinitionByGUID(String guid)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).findDefinitionByGUID(guid);
	}

	public IProcessDefinition findDefinitionByPrimaryKey(String pk)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).findDefinitionByPrimaryKey(pk);
	}

	public IProcessDefinition[] findDefinitionsWithoutContent(
			String pk_group, String billType, boolean bIncludePackage,
			int mainWorkflowtype) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).findDefinitionsWithoutContent(pk_group, billType, bIncludePackage, mainWorkflowtype);
	}

	public IProcessDefinition[] findPackageByBillType(String pkCorp,
			String billType) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).findPackageByBillType(pkCorp, billType);
	}

	public String findPrimaryKeyByGuid(String guid) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).findPrimaryKeyByGuid(guid);
	}

	public LinkedList findProcessDefsOfInstance(String procInstancePK,
			String activityDefID) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).findProcessDefsOfInstance(procInstancePK, activityDefID);
	}

	public boolean hasValidProcessDef(String pk_group, String billType,
			String pkOrg, String operator,int emendEnum) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).hasValidProcessDef(pk_group, billType, pkOrg, operator, emendEnum);
	}

	//xry TODO:
	public IProcessDefinition matchProcessDefitionAccordingBiz(String pk_group, String billType,
			String pkOrg, String operator,int emendEnum) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).matchProcessDefitionAccordingBiz(pk_group, billType, pkOrg, operator, emendEnum);
	}

	public boolean hasProcessInstances(String proc_defPK)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).hasProcessInstances(proc_defPK);
	}

	public boolean hasRunningProcessInstances(String proc_defPK)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).hasRunningProcessInstances(proc_defPK);
	}

	/**
	 * ����һ�������嵽���ݿ��У����������е����й��̶��� <li>��������������Ѿ����ڣ�����°�����
	 */
	public String savePackageWithoutProcesses(IProcessDefinition def_vo)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).savePackageWithoutProcesses(def_vo);
	}

//	/**
//	 * @param processPK
//	 * @param sublfowInRange
//	 * @return
//	 * @throws BusinessException
//	 */
//	public String[] updateSubProcessValidationAutomatically(String processPK,
//			String sublfowInRange) throws BusinessException {
//		return NCLocator.getInstance().lookup(IWorkflowDefineService.class).;
//	}

	/**
	 * ����,���ʹ�ñ��淽��
	 */
	public String[] updateProcessValidation(String processPK,
			String sublfowInRange, boolean onUse, boolean isInbatchMode)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).updateProcessValidation(processPK, sublfowInRange, onUse, isInbatchMode);
	}

//	/**
//	 * @return
//	 * @throws PFBusinessException
//	 */
//	public int doSubflowDefState(String pk_group) throws PFBusinessException {
//		return NCLocator.getInstance().lookup(IWorkflowDefineService.class).;
//	}

//	/**
//	 * @param pk_wf_def
//	 * @param sublist
//	 * @return
//	 * @throws PFBusinessException
//	 */
//	public int addFlowRefinfo(String pk_wf_def, ArrayList<String> sublist)
//			throws PFBusinessException {
//		return NCLocator.getInstance().lookup(IWorkflowDefineService.class).;
//	}

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
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).saveProcess(def_vo);
	}

	public ProcessRouteRes queryProcessRoute(String billId, String billType,
			String procInstancePK, int iWorkflowtype) throws BusinessException{
		return NCLocator.getInstance().lookup(IWFDefineService.class).queryProcessRoute(billId, billType, procInstancePK, iWorkflowtype);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.itf.uap.pf.IWorkflowDefine#syncParticipantNames()
	 */
	public int syncParticipantNames() throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).syncParticipantNames();
	}

	public byte[] toPNGImage(String billId, String billType, int iWorkflowtype)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).toPNGImage(billId, billType, iWorkflowtype);
	}

	public void syncSubflowState(String pk_group) throws BusinessException {
		// TODO Auto-generated method stub
		NCLocator.getInstance().lookup(IWFDefineService.class).syncSubflowState(pk_group);
	}

	public HashSet<String> queryWfInstanceDefid(String insql)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).queryWfInstanceDefid(insql);
	}

	public HashMap<String, IProcessDefinition> findDefinitionByDefPkAndInstPk(
			String pk_wf_def, String pk_wf_instance) throws BusinessException{
		return NCLocator.getInstance().lookup(IWFDefineService.class).findDefinitionByDefPkAndInstPk(pk_wf_def, pk_wf_instance);
	}

	public boolean isWorkflowReferenced(String proc_defPK)
			throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).isWorkflowReferenced(proc_defPK);
	}

	public List<IActivity[]> queryFinishedActivities(String billId,
			String billType, int iWfType) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).queryFinishedActivities(billId, billType, iWfType);
	}

	public List<byte[]> toPNGImagesWithSubFlow(String billId, String billType,
			int iWorkflowtype) throws BusinessException {
		return NCLocator.getInstance().lookup(IWFDefineService.class).toPNGImagesWithSubFlow(billId, billType, iWorkflowtype);
	}
}
