package uap.workflow.engine.impl;

import java.util.Collection;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.vo.pub.BusinessException;
import uap.workflow.bizitf.exception.BizException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IProcessDefGroupQry;
import uap.workflow.engine.vos.ProcessDefGroupVO;

public class ProcessDefGroupQry implements IProcessDefGroupQry{

	@SuppressWarnings("unchecked")
	public ProcessDefGroupVO[] getAllProcessDefGroup() {
		String[] fields = new String[]{"pk_prodefgroup", "pk_parentgroup", "name" };
		try{
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			Collection<ProcessDefGroupVO> vos = query.retrieveByClause(ProcessDefGroupVO.class, null, fields);
			return vos.toArray(new ProcessDefGroupVO[0]);
		}catch(BusinessException e){
			throw new BizException(e);
		}
	
	}

	@Override
	public void insert(ProcessDefGroupVO vo) {
		// TODO Auto-generated method stub
		WfBaseDAO dao = new WfBaseDAO();
		try {
			dao.insertVo(vo);
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
}
