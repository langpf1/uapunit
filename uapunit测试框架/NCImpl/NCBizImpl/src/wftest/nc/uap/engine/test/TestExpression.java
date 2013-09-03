
/*
 * 	表达式的修改方法：在bpmnParser的parseSequenceFlowConditionExpression函数中，
 * 修改UelExpressionCondition对象为NCExpressionCondition
 * ProcessEngineConfigurationImpl定中的表达式管理器初始化为NCExpressionManager，
 */


package nc.uap.engine.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;
import nc.bs.framework.common.NCLocator;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import uap.workflow.bizimpl.BizAction;
import uap.workflow.bizimpl.BizActionType;
import uap.workflow.bizimpl.BizContext;
import uap.workflow.bizimpl.BizObjectType;
import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.el.ExtExpressionManager;
import uap.workflow.engine.service.ProcessEngine;
import uap.workflow.engine.service.ProcessEngineConfiguration;


public class TestExpression extends TestCase {

	class C{
		protected int property2;
		C(int value){
			property2 = value;
		}
		
		public Object getProperty(){
			return property2;
		}
	}
	class B{
		protected C[] cccc;
		
		B(){
			cccc = new C[3];
			cccc[0] = new C(10);
			cccc[1] = new C(11);
			cccc[2] = new C(12);
		}
		
		public Object[] getCcc(){
			return cccc;
		}
	}
	
	class ParentVO extends CircularlyAccessibleValueObject{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String Id;
		String Code;
		
		public String getId(){
			return Id;
		}
		
		public void setId(String id){
			Id = id;
		}
		
		public String getCode(){
			return Code;
		}
		public void setCode(String code){
			Code = code;
		}

		@Override
		public String[] getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getAttributeValue(String attributeName) {
			return null;
		}

		@Override
		public void setAttributeValue(String name, Object value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getEntityName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void validate() throws ValidationException {
			// TODO Auto-generated method stub
			
		}
	}
	class A extends AggregatedValueObject{
		/**
		 * 
		 */
		private static final long serialVersionUID = 11L;

		protected B bbb = new B();
	
		protected ParentVO parentVO = new ParentVO();
		public Object getBb()
		{
			return bbb;
		}

		@Override
		public CircularlyAccessibleValueObject[] getChildrenVO() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CircularlyAccessibleValueObject getParentVO() {
			return parentVO;
		}

		@Override
		public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setParentVO(CircularlyAccessibleValueObject parent) {
			parentVO = (ParentVO)parent;
		}
	}
	
	class Variables implements VariableScope {

		
		private Map<String, Object> variables = new HashMap<String, Object>();
		private Map<String, Object> variablesLocal = new HashMap<String, Object>();
		
		public String getBusinessKey() {
			return "ID0001";
		}
		public String getEventName() {
			return "EventName";
		}
		public String getId() {
			return "ID0001";
		}
		public String getProcessBusinessKey() {
			return "Test Process";
		}
		public String getProcessInstanceId() {
			return "ID0002";
		}
		public void createVariableLocal(String variableName, Object value) {
			variables.put("vo", "VOString");
		}
		public void createVariablesLocal(Map<String, ? extends Object> variables) {
			variablesLocal.put("@var", "VarString");
		}
		public Object getVariable(String variableName) {
			return variables.get(variableName);
		}
		public Object getVariableLocal(Object variableName) {
			return variablesLocal.get(variableName);
		}
		public Set<String> getVariableNames() {
			return variables.keySet();
		}
		public Set<String> getVariableNamesLocal() {
			return variablesLocal.keySet();
		}
		public Map<String, Object> getVariables() {
			return variables;
		}
		public Map<String, Object> getVariablesLocal() {
			return variablesLocal;
		}
		public boolean hasVariable(String variableName) {
			return variables.containsKey(variableName);
		}
		public boolean hasVariableLocal(String variableName) {
			return variablesLocal.containsKey(variableName);
		}
		public boolean hasVariables() {
			return !variables.isEmpty();
		}

		@Override
		public boolean hasVariablesLocal() {
			return !variablesLocal.isEmpty();
		}

		@Override
		public void removeVariable(String variableName) {
			variables.remove(variableName);
		}

		@Override
		public void removeVariableLocal(String variableName) {
			variablesLocal.remove(variableName);
		}

		@Override
		public void removeVariables() {
			variables.clear();
		}

		@Override
		public void removeVariablesLocal() {
			variablesLocal.clear();
		}

		@Override
		public void setVariable(String variableName, Object value) {
			variables.put(variableName,value);
		}

		@Override
		public Object setVariableLocal(String variableName, Object value) {
			return variablesLocal.put(variableName, value);
		}

		@Override
		public void setVariables(Map<String, ? extends Object> variables) {
			this.variables.putAll(variables);
		}
		public void setVariablesLocal(Map<String, ? extends Object> variables) {
			this.variablesLocal.putAll(variables);
		}
		public Set<String> collectVariableNames(Set<String> variableNames) {
			return null;
		}
		public Map<String, Object> collectVariables(
				HashMap<String, Object> variables) {
			return null;
		}
		

	}
	
	public void testExpressEvaluate() {
		
		Properties props = new Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		props.setProperty(NCLocator.TARGET_MODULE, "workflow");
		ProcessEngineConfiguration peConfig = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		ProcessEngine pe = peConfig.buildProcessEngine();
		Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl)peConfig);
		
		ExpressEvaluate();
		
		pe.close();
			
	}

	
	
	
	private void ExpressEvaluate(){

		ExtExpressionManager expressionManager = new ExtExpressionManager();
		Expression expression = expressionManager.createExpression(
				"${vacationApproved} and ${var + sum(vacationApproved + var) + sum(12.0,13.0,14.0)} tail ${20+A.bb.ccc[2].property+2}");

		Variables vars = new Variables();
		vars.setVariable("vacationApproved", "500");
		vars.setVariable("var", "300");
		BizContext bizContext = BizContext.construct(
			new BizAction(new BizObjectType("group", "10GY", "", "供应商", "10GY"), new BizActionType("SAVE","存","SAVE")), 
				"BILLID", "BILLCODE", new A(), null);
		vars.setVariable("bizContext", bizContext);
		
		Object object = expression.getValue(vars);
		System.out.println(object);

	}

}
