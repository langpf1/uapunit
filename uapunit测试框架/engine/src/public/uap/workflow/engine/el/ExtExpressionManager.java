package uap.workflow.engine.el;


import java.util.List;
import uap.workflow.engine.bpmn.data.ItemInstance;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.entity.VariableScopeImpl;
import uap.workflow.engine.javax.ArrayELResolver;
import uap.workflow.engine.javax.BeanELResolver;
import uap.workflow.engine.javax.CompositeELResolver;
import uap.workflow.engine.javax.DynamicBeanPropertyELResolver;
import uap.workflow.engine.javax.ELContext;
import uap.workflow.engine.javax.ELResolver;
import uap.workflow.engine.javax.ExpressionFactory;
import uap.workflow.engine.javax.ExtELResolver;
import uap.workflow.engine.javax.ListELResolver;
import uap.workflow.engine.javax.MapELResolver;
import uap.workflow.engine.javax.ValueExpression;
import uap.workflow.engine.juel.ExpressionFactoryImpl;
import uap.workflow.engine.server.BizProcessServer;

public class ExtExpressionManager extends ExpressionManager{

	protected ExpressionFactory expressionFactory;
	
	protected ELContext elContext = null;

	//CompositeELResolver elResolver = null;
	  
	public ExtExpressionManager() {
		expressionFactory = new ExpressionFactoryImpl();
	}

	public Expression createExpression(String expression) {
		ValueExpression valueExpression = expressionFactory.createValueExpression(new ExtELContext(null), expression, Object.class);
		Expression expr = new ExtExpression(valueExpression, this, expression); 
		return expr;
	}

	public ELContext getElContext(VariableScope variableScope) {
		if (variableScope instanceof VariableScopeImpl) {
			VariableScopeImpl variableScopeImpl = (VariableScopeImpl) variableScope;
			elContext = variableScopeImpl.getCachedElContext();
		}
	    
		if (elContext == null) {
			elContext = ncCreateElContext(variableScope);
			if (variableScope instanceof VariableScopeImpl) {
				((VariableScopeImpl)variableScope).setCachedElContext(elContext);
			}
		}

		return elContext;
	}
	
	protected ExtELContext ncCreateElContext(VariableScope variableScope) {
		ELResolver elResolver = createElResolver(variableScope);
		ExtELContext elContext = new ExtELContext(elResolver); 
		return elContext;
	}

	protected CompositeELResolver constructELResolver(VariableScope variableScope){
		CompositeELResolver elResolver = new CompositeELResolver();
		elResolver.add(new VariableScopeElResolver(variableScope));
		elResolver.add(new ArrayELResolver());
		elResolver.add(new ListELResolver());
		elResolver.add(new MapELResolver());
		elResolver.add(new DynamicBeanPropertyELResolver(ItemInstance.class, "getFieldValue", "setFieldValue")); //TODO: needs verification
		elResolver.add(new BeanELResolver());
		//elResolver.add(new NCMetadataElResolver(variableScope));
		return elResolver;
	}
	
	
	protected ELResolver createElResolver(VariableScope variableScope) {
		CompositeELResolver elResolver = constructELResolver(variableScope);
		addExtensionELResolver(elResolver, variableScope);
		return elResolver;
	}
	
	public void addExtensionELResolver(CompositeELResolver elResolver, VariableScope variableScope){
		List<ExtensionELResolveConfig> resolvers = BizProcessServer.getProcessEngineConfig().getExtensionconfig().getResolveExtension();
		for(ExtensionELResolveConfig resolver : resolvers){
			Class<?> clz = null;
			try {
				clz = Class.forName(resolver.getImplementationClass());
				ExtELResolver resolver1 =  (ExtELResolver)clz.newInstance();
				resolver1.setVariableScope(variableScope);
				elResolver.add(resolver1);				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
