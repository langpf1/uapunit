package uap.workflow.pub.app.notice;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.vo.WorkflownoteVO;



/**
 * 用于系统变量的 值取器 的基类
 * @author leijun 2007-4-28
 */
public class SysVariableValueGetter {

	private Object context;
	private IUAPQueryBS uapQry;

	public SysVariableValueGetter(Object context) {
		this.context = context;
	}

	/**
	 * 可重载
	 * @param code
	 * @return
	 */
	public Object getValueOfVar(String code) {
		//获取默认变量值
		Object objValue = getValueOfDefaultVar(code);
		return objValue;
	}

	private Object getValueOfDefaultVar(String code) {
		if (context instanceof PfParameterVO) {
			PfParameterVO paravo = (PfParameterVO) context;
			if (code.equals(PfSysVariable.DEFAULT_BILLMAKER)) {
				return paravo.m_makeBillOperator;
			} else if (code.equals(PfSysVariable.DEFAULT_ALLAPPROVER)) {
				Logger.debug("系统变量=查询该单据的所有已经审核过的审批人");
				try {
					String sql = "select checkman from pub_workflownote where billid=? and pk_billtype=?"
							+ " and actiontype<>'" + WorkflownoteVO.WORKITEM_TYPE_BIZ + "'"
							+ " and approveresult is not null and approvestatus<>"
							+ TaskInstanceStatus.Inefficient.getIntValue();
					IUAPQueryBS uapQry = (IUAPQueryBS) NCLocator.getInstance().lookup(
							IUAPQueryBS.class.getName());
					SQLParameter param = new SQLParameter();
					param.addParam(paravo.m_billId);
					param.addParam(paravo.m_billType);
					ArrayList<String> alRet = (ArrayList) uapQry.executeQuery(sql, param,
							new ColumnListProcessor());

					return alRet.toArray(new String[0]);
				} catch (BusinessException e) {
					Logger.error(e.getMessage(), e);
				}

			}else if(code.equals(PfSysVariable.DEFAULT_PREVIOUSAPPROVER)){
				Logger.debug("系统变量=查询该单据的上环节流程的实际处理人");
				String querySql="select checkman from pub_workflownote where pk_wf_task " 
			        +"in(select pk_wf_task from pub_wf_task where pk_wf_actinstance "
		            +"in(select src_actinstance from pub_wf_actinstancesrc where target_actinstance " 
		            +"in(select pk_wf_actinstance from pub_wf_task where pk_wf_task='"+paravo.m_workFlow.getPk_wf_task()+"'))) "
		            +"and actiontype<>'" + WorkflownoteVO.WORKITEM_TYPE_BIZ + "'";
				try {
					//抢占情况下，实际处理人不包括参与的人员
					if(isPreTaskIsProcessMode(paravo)){
						querySql+=" and approveresult is not null and approvestatus ="+TaskInstanceStatus.Finished.getIntValue();
					}
					ArrayList<String> alRet =(ArrayList<String>) getQueryService().executeQuery(querySql, new ColumnListProcessor());
					return alRet.toArray(new String[0]);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					Logger.error(e.getMessage(), e);
				}

			}else if(code.equals(PfSysVariable.DEFAULT_PREVIOUSPARTICIPATOR)){
				Logger.debug("系统变量=查询该单据的上环节流程参与者");
				String querySql="select checkman from pub_workflownote where pk_wf_task " 
						        +"in(select pk_wf_task from pub_wf_task where pk_wf_actinstance "
					            +"in(select src_actinstance from pub_wf_actinstancesrc where target_actinstance " 
					            +"in(select pk_wf_actinstance from pub_wf_task where pk_wf_task='"+paravo.m_workFlow.getPk_wf_task()+"'))) "
					            +"and actiontype<>'" + WorkflownoteVO.WORKITEM_TYPE_BIZ + "'";
				try {
					ArrayList<String> alRet =(ArrayList<String>) getQueryService().executeQuery(querySql, new ColumnListProcessor());
					return alRet.toArray(new String[0]);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					Logger.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}
	
	
	
	/**判断当前task的前一审批环节的task是否为抢占模式
	 * @return boolean
	 * */
	private boolean isPreTaskIsProcessMode(PfParameterVO paravo) throws BusinessException{
/*		String querySql="select tasktype from pub_wf_task where pk_wf_actinstance "
			            +"in(select src_actinstance from pub_wf_actinstancesrc where target_actinstance "
			            +"in(select pk_wf_actinstance from pub_wf_task where pk_wf_task='"+paravo.m_workFlow.getPk_wf_task()+"')) ";
			int processMode=WFTask.ProcessMode_Single_Together;
			ArrayList<Integer> alRet =(ArrayList<Integer>) getQueryService().executeQuery(querySql, new ColumnListProcessor());
			if(alRet!=null&&alRet.size()!=0){
				processMode=alRet.get(0);
			}
			return processMode==WFTask.ProcessMode_Race||processMode==WFTask.ProcessMode_Race_Assign;
*/
		return false;
	}
	
	private IUAPQueryBS getQueryService(){
		if(uapQry==null){
			uapQry = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return uapQry;
	}
}
