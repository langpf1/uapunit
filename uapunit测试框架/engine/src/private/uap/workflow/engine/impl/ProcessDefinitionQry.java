package uap.workflow.engine.impl;
import java.util.Collection;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import uap.workflow.bizitf.exception.BizException;
import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.vos.ProcessDefinitionVO;
public class ProcessDefinitionQry implements IProcessDefinitionQry {
	@Override
	public ProcessDefinitionVO getProDefVoByPk(String proDefPk) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			BizProcessServer.getInstance().start();
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ProcessDefinitionVO.class, "pk_prodef='" + proDefPk + "'");
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ProcessDefinitionVO) vos[0];
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@Override
	public ProcessDefinitionVO getProDefVoById(String proDefId) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ProcessDefinitionVO.class, "prodef_id='" + proDefId + "'");
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ProcessDefinitionVO) vos[0];
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}

	@Override
	public ProcessDefinitionVO[] getProcessDefVOAccordingBiz(String pk_group, String bizObjectKey) {
		try {
			WfBaseDAO dao = new WfBaseDAO();
			BizProcessServer.getInstance().start();
			String condition = "pk_group='" + pk_group + "' and pk_bizobject='" + bizObjectKey + "' and validity= " 
				+ ProcessDefinitionStatusEnum.Valid.getIntValue();
			
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ProcessDefinitionVO.class, condition);
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ProcessDefinitionVO[]) vos;
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@Override
	public ProcessDefinitionVO[] getAllProcessDef(String pk_group) {

		String[] fields = new String[]{"PK_PRODEF", "PRODEF_ID", "PRODEF_DESC", "PRODEF_NAME", "PRODEF_VERSION", "PK_ORG","TS","VALIDITY","PK_BIZOBJECT","pk_prodefgroup"};
		String condition = " pk_group='" + pk_group + "' ";
		try{
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Collection<ProcessDefinitionVO> vos = query.retrieveByClause(ProcessDefinitionVO.class, condition, fields);
			return vos.toArray(new ProcessDefinitionVO[0]);
		}catch(BusinessException e){
			throw new BizException(e);
		}
	}
	@Override
	public ProcessDefinitionVO[] getProcessDefVOByProdefGroup(String pk_prodefgroup) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			BizProcessServer.getInstance().start();
			SuperVO[] vos = (SuperVO[]) dao.queryByCondition(ProcessDefinitionVO.class, "pk_prodefgroup='" + pk_prodefgroup + "'");
			if (vos == null || vos.length == 0) {
				return null;
			} else {
				return (ProcessDefinitionVO[]) vos;
			}
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	
	}
}
