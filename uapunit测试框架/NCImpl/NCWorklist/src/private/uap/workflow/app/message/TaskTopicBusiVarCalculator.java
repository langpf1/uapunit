package uap.workflow.app.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.message.templet.bs.AbstractBusiVarCalculater;
import nc.ui.pf.multilang.PfMultiLangUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.msg.WorkflowMsgTempVar;
import nc.vo.pf.pub.FunctionVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.impl.ActionEnvironment;
import uap.workflow.pub.app.message.WorkitemMsgContext;
import uap.workflow.pub.util.PfDataCache;

/**
 * 用来解析工作任务消息模版中的业务函数变量
 * 
 * @author yanke1 2011-9-28
 * 
 */
public class TaskTopicBusiVarCalculator extends AbstractBusiVarCalculater {

	private static final String NULL_STR = new String("N/A");

	Map<String, FunctionVO> funcMap = new HashMap<String, FunctionVO>();
	Map<String, String> varMap = new HashMap<String, String>();
	
	String billid = null;

	public TaskTopicBusiVarCalculator(WorkitemMsgContext context) {

		String billtype = context.getBillType();
		String billno = context.getBillno();
		String sender = context.getSender();
		String actionType = context.getActionType();
		String checknote = context.getCheckNote();
		String agentInfo = context.getAgent();
		String billid = context.getBillid();
		String approveResult = context.getResult();

		this.billid = billid;

		initVarMap(sender, agentInfo, actionType, approveResult, checknote, billno);
		initFuncMap(billtype);
	}

	private void initFuncMap(String billType) {

		BilltypeVO bvo = PfDataCache.getBillType(billType);

		String sql = "";

		if (bvo == null)
			return;
		if (!StringUtil.isEmptyWithTrim(bvo.getParentbilltype())) {
			sql += "pk_billtype='" + billType + "' or pk_billtype='" + bvo.getParentbilltype() + "' ";
		} else {
			sql += "pk_billtype='" + billType + "' ";
		}

		sql += " and substring(functionnote,1,1)!='<' and (isnull(iscomp, 'N') = 'N')";

		IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			Collection<FunctionVO> ret = qry.retrieveByClause(FunctionVO.class, sql);
			for (FunctionVO funcVO : ret) {

				funcMap.put(funcVO.getCode(), funcVO);
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	private void initVarMap(String sender, String agentInfo, String actionType, String approveResult, String checknote,
			String billno) {
		varMap.put(WorkflowMsgTempVar.SENDER, StringUtil.isEmptyWithTrim(sender) ? NULL_STR : sender);
		varMap.put(WorkflowMsgTempVar.AGENT, StringUtil.isEmptyWithTrim(agentInfo) ? NULL_STR : agentInfo);
		varMap.put(WorkflowMsgTempVar.BILL_NO, StringUtil.isEmptyWithTrim(billno) ? NULL_STR : billno);
		varMap.put(WorkflowMsgTempVar.ACTION_TYPE, StringUtil.isEmptyWithTrim(actionType) ? NULL_STR : actionType);
		varMap.put(WorkflowMsgTempVar.RESULT, StringUtil.isEmptyWithTrim(approveResult) ? NULL_STR : approveResult);
		varMap.put(WorkflowMsgTempVar.CHECKNOTE, StringUtil.isEmptyWithTrim(checknote) ? NULL_STR : checknote);
	}

	private String getVarValue(String express) {
		if (StringUtil.isEmptyWithTrim(express)) {
			return "";
		} else if (express.equals(WorkflowMsgTempVar.SENDER)) {
			String cuserid = varMap.get(express);
			
			if (cuserid == NULL_STR) {
				return cuserid;
			} else {
				return getUserName(cuserid);
			}
		} else if (express.equals(WorkflowMsgTempVar.AGENT)) {
			String agentId = varMap.get(express);
			
			if (agentId == NULL_STR) {
				return "";
			} else {
				return "{agent}" + getUserName(agentId);
			}
		} else {
			return varMap.get(express);
		}
	}

	private String getUserName(String cuserid) {
		nc.vo.sm.UserVO userVo = null;
		try {
			// 查找操作人的姓名
			userVo = NCLocator.getInstance().lookup(IUserManageQuery.class).getUser(cuserid);
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new RuntimeException(NCLangResOnserver.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000358" /*
																	 * @res
																	 * "查找操作人错误:"
																	 */) + ex.getMessage());
		}

		String userName = NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000359" /*
																										 * @
																										 * res
																										 * "无法识别的人"
																										 */);
		if (userVo != null) {
			userName = PfMultiLangUtil.getSuperVONameOfCurrentLang(userVo, "user_name");
			
		}

		return userName;
	}

	@Override
	public String calculateValue(String express) {
		if (varMap.containsKey(express)) {
			return getVarValue(express);
		}

		if (funcMap.containsKey(express)) {
			FunctionVO funcVO = funcMap.get(express);

			Object funcResult = executeFunction(billid, funcVO);
			if (funcResult != null) {
				return String.valueOf(funcResult);
			}
		}

		return "";
	}

	private Object executeFunction(String billId, FunctionVO functionVO) {
		Object returnObj = null;
		PfParameterVO paraVO = ActionEnvironment.getInstance().getParaVo(billId);

		try {
			returnObj = PfUtilTools.parseFunction(functionVO.getFunctionnote(), functionVO.getClassname(),
					functionVO.getMethodname(), functionVO.getArguments(), paraVO);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return returnObj;
	}

}