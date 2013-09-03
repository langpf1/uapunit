package uap.workflow.engine.utils;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.data.PaginationInfo;
import uap.workflow.engine.bridge.ProcessInstanceBridge;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.itf.IProcessInstanceQry;
import uap.workflow.engine.vos.ProcessInstanceVO;
public class ProcessInstanceUtil {
	public static IProcessInstance getProcessInstance(String processInstancePk) {
		IProcessInstanceQry proInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceQry.class);
		ProcessInstanceVO proInsVo = proInsQry.getProInsVo(processInstancePk);
		ProcessInstanceEntity proIns = new ProcessInstanceBridge().convertM2T(proInsVo);
		return proIns;
	}

	public static ProcessInstanceVO[] getProcessInstanceVOs(String form_instance_versionPK) {
		IProcessInstanceQry proInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceQry.class);
		ProcessInstanceVO[] proInsVo = proInsQry.getProcessInstanceVOs(form_instance_versionPK);
		return proInsVo;
	}
	
	/**
	 * 取流程实例的制单人
	 * @param form_instance_versionPK
	 * @param bizObejectKey
	 * @return
	 */
	public static String getProInsStartupPerson(String form_instance_versionPK, String bizObejectKey) {
		IProcessInstanceQry proInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceQry.class);
		ProcessInstanceVO proInsVO = proInsQry.getProcessInstanceVO(form_instance_versionPK,bizObejectKey);
		if(proInsVO != null)
			return proInsVO.getPk_starter();
		return null;
	}

	public static String getProcessDefinitionOfProcessInstance(String form_instance_versionPK, String bizObejectKey) {
		IProcessInstanceQry proInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceQry.class);
		ProcessInstanceVO proInsVO = proInsQry.getProcessInstanceVO(form_instance_versionPK,bizObejectKey);
		if(proInsVO != null)
			return proInsVO.getPk_starter();
		return null;
	}

	public static List<ProcessInstanceVO> getAllProInsByPage(String sqlCondition, PaginationInfo page) throws DAOException {
		IProcessInstanceQry proInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceQry.class);
		List<ProcessInstanceVO> proInsVo = proInsQry.getAllProcessInstanceVOs(sqlCondition,page);
		return proInsVo;	
	}

	public static int getAllProInsNumber(String table) {
		IProcessInstanceQry proInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceQry.class);
		int proInsNumber = proInsQry.getAllProcessInsNumber(table);
		return proInsNumber;
	}
}
