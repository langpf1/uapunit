package uap.workflow.bizimpl.bizinvocation;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.compiler.IPfRun;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.bizimpl.BizContext;
import uap.workflow.bizitf.exception.BizException;

public class RunActionClass {

	protected BizContext bizContext;
	
	public RunActionClass(BizContext context){
		this.bizContext = context;
	}
	
	public Object runAction() throws BizException{
		PfParameterVO pfVO = new PfParameterVO();
		pfVO.m_billEntities = bizContext.getBizEntities();
		pfVO.m_billEntity = bizContext.getBizEntity();
		pfVO.m_billId = bizContext.getBillID();
		pfVO.m_billNo = bizContext.getBillCode();
		pfVO.m_billType = bizContext.getAction().getBizObjectType().getTransType();
		
		//查找动作脚本在pub_busiclass表注册的信息
		String strClassNameNoPackage = findActionScriptClass(bizContext.getAction().getBizActionType().getAction(), 
				bizContext.getAction().getBizObjectType().getTransType());
		String className = 	"nc.bs.pub.action" + "." + strClassNameNoPackage;
		pfVO.m_actionName = bizContext.getAction().getBizActionType().getAction();
		
		Object instOfActionscript = nc.bs.framework.core.util.ObjectCreator.newInstance(className);
		Object actionReturnObj = null;
		try{
			actionReturnObj = ((IPfRun)instOfActionscript).runComClass(pfVO);
		}catch(Exception e){
			throw new BizException(e.getMessage());
		}
		
		Logger.debug("动作脚本执行完成....." + instOfActionscript);
		return actionReturnObj;
		
	}
	
	@SuppressWarnings("unchecked")
	private String findActionScriptClass(String billType, String actionName){

		String sql = "select classname from pub_busiclass ";
		sql += " where actiontype = '"+ bizContext.getAction().getBizActionType().getAction() +
			"' and pk_billtype = '"+bizContext.getAction().getBizObjectType()+"'";
		try{
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<Object[]> list = (List<Object[]>)query.executeQuery(sql, new ArrayListProcessor());
			if (list.size() < 1)
				return "";
			else
				return list.get(0).toString();
		}catch(Exception e){
			throw new BizException(e);
		}
	}

}
