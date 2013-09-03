package uap.workflow.app.impl;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.oid.OidGenerator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import uap.workflow.admin.IWFDefineService;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.monitor.ProcessRouteRes;

/**
 * �����������߼���BS�ӿ�ʵ��
 * 
 * @author �׾� 2005-8
 */
public class WFDefineServiceImpl implements IWFDefineService {

	public void deleteDefinitionByPK(String pk, String pk_group)
			throws BusinessException {

	}

	public IProcessDefinition findDefinitionByGUID(String guid)
			throws BusinessException {
		return ProcessDefinitionUtil.getProDefByProDefId(guid);
	}

	
	public IProcessDefinition findDefinitionByPrimaryKey(String pk)
			throws BusinessException {
		return ProcessDefinitionUtil.getProDefByProDefPk(pk);
	}

	public IActivity getActivityByTaskPK(String pk_task)throws BusinessException
	{
		TaskInstanceVO task = NCLocator.getInstance().lookup(ITaskInstanceQry.class).getTaskInsVoByPk(pk_task);
		IProcessDefinition processDefinition = ProcessDefinitionUtil.getProDefByProDefPk(task.getPk_process_def());
		return processDefinition.findActivity(task.getActivity_id());
	}
	
	public IActivity getActivity(String pk_process_def, String activity_id)throws BusinessException
	{
		IProcessDefinition processDefinition = ProcessDefinitionUtil.getProDefByProDefPk(pk_process_def);
		return processDefinition.findActivity(activity_id);
	}

	public IProcessDefinition[] findDefinitionsWithoutContent(
			String pk_group, String billType, boolean bIncludePackage,
			int mainWorkflowtype) throws BusinessException {
		return null;
	}

	public IProcessDefinition[] findPackageByBillType(String pkCorp,
			String billType) throws BusinessException {
		return null;
	}

	public String findPrimaryKeyByGuid(String guid) throws BusinessException {
		return null;
	}

	public LinkedList findProcessDefsOfInstance(String procInstancePK,
			String activityDefID) throws BusinessException {
		return null;
	}

	public boolean hasValidProcessDef(String pk_group, String billType,
			String pkOrg, String operator,int emendEnum) throws BusinessException {
		return false;
	}

	//xry TODO:
	public IProcessDefinition matchProcessDefitionAccordingBiz(String pk_group, String billType,
			String pkOrg, String operator,int emendEnum) throws BusinessException {
		IProcessDefinition process_def = null;
		IProcessDefinition[] processDefs = ProcessDefinitionUtil.getProcessDefVOAccordingBiz(pk_group, billType);
		if(processDefs == null || processDefs.length == 0)
		{
			return process_def;
		}
		if(processDefs.length>1)
		{
			throw new PFBusinessException("��ǰ����ƥ������������������ϣ�");
		}
		return processDefs[0];
	}

	public boolean hasProcessInstances(String proc_defPK)
			throws BusinessException {
		return false;
	}

	public boolean hasRunningProcessInstances(String proc_defPK)
			throws BusinessException {
		return false;
	}

	/**
	 * ����һ�������嵽���ݿ��У����������е����й��̶��� <li>��������������Ѿ����ڣ�����°�����
	 */
	public String savePackageWithoutProcesses(IProcessDefinition def_vo)
			throws BusinessException {
		if (def_vo == null)
			return null;
		String returnPK = null;
		return returnPK;
	}

//	/**
//	 * @param processPK
//	 * @param sublfowInRange
//	 * @return
//	 * @throws BusinessException
//	 */
//	public String[] updateSubProcessValidationAutomatically(String processPK,
//			String sublfowInRange) throws BusinessException {
//		return new String[] { null, null };
//	}

	/**
	 * ����,���ʹ�ñ��淽��
	 */
	public String[] updateProcessValidation(String processPK,
			String sublfowInRange, boolean onUse, boolean isInbatchMode)
			throws BusinessException {
		return new String[] { null, null };
	}

	/**
	 * @param pk_group
	 * @return
	 * @throws PFBusinessException
	 */
	public int syncSubflowDefState(String pk_group) throws PFBusinessException {
		int rows = 0;
		do {
			rows = doSubflowDefState(pk_group);
		} while (rows > 0);
		return rows;
	}

	/**
	 * @return
	 * @throws PFBusinessException
	 */
	private int doSubflowDefState(String pk_group) throws PFBusinessException {
		String sql = " update pub_wf_def  set validity = 1 where processdefid in  (select processdefid from pub_wfdef_ref where exists ( select 1 from pub_wf_def where pk_wf_def = pub_wfdef_ref.pk_wf_def and validity = 1)) and (workflow_type = 3 or workflow_type = 5 )and validity != 1 and pk_group = ?";
		// String stopsql =
		// "   update pub_wf_def a set validation = 2 where not exists (select 1 from pub_wfdef_ref b , pub_wf_def c where b.processdefid = a.processdefid and b.pk_wf_def = c.pk_Wf_def and c.validation = 1 ) and (workflow_type = 3 or workflow_type = 5 ) and pk_group = ? and validation = 1 ";
		String stopsql = " update pub_wf_def  set validity = 0 where not exists (select 1 from pub_wfdef_ref b  where b.processdefid = pub_wf_def.processdefid and exists ( select 1 from pub_wf_def c where c.pk_wf_def = b.pk_wf_def and (c.validity = 1 or c.validity =2 )) ) and (workflow_type = 3 or workflow_type = 5 )  and pk_group = ? and validity != 0 ";

		int iUpdated = 0; // �����˶��ٸ����̶���///
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			// �������̶����Blob�ֶ�
			SQLParameter para = new SQLParameter();
			para.addParam(pk_group);// /
			iUpdated = jdbc.executeUpdate(sql, para);
			iUpdated += jdbc.executeUpdate(stopsql, para);

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		} finally {
			if (persist != null)
				persist.release();
		}

		return iUpdated;
	}

	/**
	 * @param pk_wf_def
	 * @param sublist
	 * @return
	 * @throws PFBusinessException
	 */
	private int addFlowRefinfo(String pk_wf_def, ArrayList<String> sublist)
			throws PFBusinessException {

		int iUpdated = 0; // �����˶��ٸ����̶���
		PersistenceManager persist = null;
		String del = " delete from pub_wfdef_ref where pk_wf_def = ? ";
		String sqlUpdate = " insert into pub_wfdef_ref (pk_wfdefref, pk_wf_def,processdefid) values (?,?,?)";
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			SQLParameter para1 = new SQLParameter();
			para1.addParam(pk_wf_def);// /
			jdbc.executeUpdate(del, para1);
			// �������̶����Blob�ֶ�
			for (String subflowid : sublist) {
				SQLParameter para = new SQLParameter();
				para.addParam(OidGenerator.getInstance().nextOid());
				para.addParam(pk_wf_def);// /
				para.addParam(subflowid);
				jdbc.addBatch(sqlUpdate, para);

				iUpdated++;
			}

			// ִ����������
			jdbc.executeBatch();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		} finally {
			if (persist != null)
				persist.release();
		}

		return iUpdated;
	}

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
		if (def_vo == null)
			return null;
		String returnPK = null;
		String proc_def_guid = def_vo.getProDefId();
		return new String[] { returnPK, proc_def_guid };
	}
	
	
	private void changeFlowStatus2Invalidity(int flowtype, String oldPK) throws BusinessException{
	}
	
	/**
	 * ��������ʵ���������������°汾��
	 * ��ʵ�������������ø������� :�����µİ汾��������״̬���ɰ汾ͣ�ã���;���̰��ɰ汾�ߣ��������̰��°汾�ߡ�
	 * ��ʵ�������������ø������̣�������Ч��
	 * @param defvo
	 * @param oldPK
	 * @param newPk
	 * @throws BusinessException 
	 * */
	private void afterAddFlowinfo(IProcessDefinition defvo,String oldPK,String newPK) throws BusinessException{
	}
	
	
	private IProcessDefinition changeSubflowId(IProcessDefinition defvo ,String oldSubId,String newSubId){
		return defvo;
	}
	
	private IProcessDefinition processToSuperVo(IProcessDefinition process, IProcessDefinition processvo) {

		return processvo;
	} 
	
	
	private  IProcessDefinition superVoToProcess(IProcessDefinition def_vo){
		IProcessDefinition process = null;
		return process;
	}
	

	/**
	 * ��֤ͬ���������²�����ͬ�Ƶ��˵����̶���
	 * 
	 * @param def_vo
	 * @param isInsert
	 * @throws DAOException
	 * @throws PFBusinessException
	 */
	private void checkDuplicate(IProcessDefinition def_vo, boolean isInsert){
	}

	public ProcessRouteRes queryProcessRoute(String billId, String billType,
			String procInstancePK, int iWorkflowtype){
		return new ProcessRouteRes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.itf.uap.pf.IWorkflowDefine#syncParticipantNames()
	 */
	public int syncParticipantNames() throws BusinessException {
		return 0;
	}

	public byte[] toPNGImage(String billId, String billType, int iWorkflowtype)
			throws BusinessException {
		ByteArrayOutputStream baos = null;
		baos = new ByteArrayOutputStream();
		return baos.toByteArray();
	}

	// only for test
	public byte[] toPNGImage3(String billId, String billType, int iWorkflowtype)
			throws BusinessException {
		return null;
	}

	@Override
	public void syncSubflowState(String pk_group) throws BusinessException {
		// TODO Auto-generated method stub
		syncSubflowDefState(pk_group);
	}

	@Override
	public HashSet<String> queryWfInstanceDefid(String insql)
			throws BusinessException {
		
		if (insql.endsWith(",")) {
			insql = insql.substring(0, insql.length() - 1);
		}
		
		if (insql.startsWith(",")) {
			insql = insql.substring(1);
		}

		IUAPQueryBS queryitf = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		HashSet<String> instacncesPks = (HashSet<String>) queryitf
				.executeQuery(
				"select b.processdefid from pub_wf_instance a, pub_wf_def b where a.processdefid = b.pk_wf_def and a.processdefid in ("
						+ insql + " )  ", new BaseProcessor() { // and
							// validation
							// = 1

							@Override
							public Object processResultSet(ResultSet rs)
									throws SQLException {
								// TODO Auto-generated method stub
								HashSet<String> instacncewp = new HashSet<String>();
								while (rs.next()) {
									instacncewp.add(rs.getString(1));
								}
								return instacncewp;
							}

						});
		return instacncesPks;
	}

	@Override
	public HashMap<String, IProcessDefinition> findDefinitionByDefPkAndInstPk(
			String pk_wf_def, String pk_wf_instance) throws BusinessException{
		// ��ȡ��ǰ����ʵ��PK
		HashMap<String, IProcessDefinition> map = new HashMap<String, IProcessDefinition>();
		IProcessDefinition bwp = null;
		IProcessDefinition processDefinition = ProcessDefinitionUtil.getProDefByProDefPk(pk_wf_def);
		map.put(pk_wf_def, processDefinition);
//		if (pk_wf_instance != null) {
//			Context.getCommandContext().getProcessDefinitionManager().getProDefByProDefPk(pk_wf_def);
//			map.put(pk_wf_def, bwp);
//		}
		return map;
	}

	@Override
	public boolean isWorkflowReferenced(String proc_defPK)
			throws BusinessException {
		String sql = "select 1 from pub_wfdef_ref a where a.processdefid in (select b.processdefid from pub_wf_def b where pk_wf_def=?)";
		SQLParameter para = new SQLParameter();
		para.addParam(proc_defPK);
		Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class)
				.executeQuery(sql, para, new ColumnProcessor(1));
		if (obj == null)
			return false;
		return true;
	}

	@Override
	public List<IActivity[]> queryFinishedActivities(String billId,
			String billType, int iWfType) throws BusinessException {
		ProcessRouteRes route = queryProcessRoute(billId, billType, null,
				iWfType);

		return getActivitesOfRoutesRecursively(new ProcessRouteRes[] { route });
	}

	private List<IActivity[]> getActivitesOfRoutesRecursively(
			ProcessRouteRes[] routes) {
		if (routes == null || routes.length == 0)
			return null;

		List<IActivity[]> actsList = new ArrayList<IActivity[]>();

		return actsList;
	}

	private IActivity[] getActivitiesOfProcessRoute(ProcessRouteRes route) {
		List<IActivity> actList = new ArrayList<IActivity>();
		IActivity[] actArray = actList.toArray(new IActivity[0]);
		return actArray;
	}
	
	@Override
	public List<byte[]> toPNGImagesWithSubFlow(String billId, String billType,
			int iWorkflowtype) throws BusinessException {
		ProcessRouteRes route = queryProcessRoute(billId, billType, null,
				iWorkflowtype);

		return toPNGImageRecursively(new ProcessRouteRes[] { route });
	}

	private List<byte[]> toPNGImageRecursively(ProcessRouteRes[] routes) {
		if (routes == null || routes.length == 0)
			return null;

		List<byte[]> list = new ArrayList<byte[]>();

		for (ProcessRouteRes route : routes) {
			byte[] bytes = toPNGImage(route);

			if (bytes != null && bytes.length > 0)
				list.add(bytes);

			List<byte[]> subList = toPNGImageRecursively(route
					.getSubProcessRoute());
			if (subList != null && subList.size() > 0)
				list.addAll(subList);
		}

		return list;
	}

	private byte[] toPNGImage(ProcessRouteRes route) {
		ByteArrayOutputStream baos = null;
		baos = new ByteArrayOutputStream(); 
		return baos.toByteArray();
	}
}
