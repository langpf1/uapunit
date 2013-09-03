package uap.workflow.engine.impl;

import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.el.ExpressionManager;
import uap.workflow.engine.itf.IExpressionValidate;
import uap.workflow.engine.server.BizProcessServer;

public class ExpressionValidateImpl implements IExpressionValidate {

	@Override
	public String verifyExpressionValid(String expressionText) {
		BizProcessServer.getInstance().start();
		ProcessEngineConfigurationImpl engineCfg = BizProcessServer.getProcessEngineConfig();
		
		ExpressionManager exprMgr = engineCfg.getExpressionManager();
		Expression expression = null;
		try{
			expression = exprMgr.createExpression("${" + expressionText + "}");
			//expression.getValue(variableScope)
			return "OK";
		}catch(Exception e){
			return e.getMessage();
		}
	}

}
