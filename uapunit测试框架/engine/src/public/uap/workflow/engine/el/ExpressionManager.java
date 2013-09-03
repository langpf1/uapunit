/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.el;

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
import uap.workflow.engine.javax.ListELResolver;
import uap.workflow.engine.javax.MapELResolver;
import uap.workflow.engine.javax.ValueExpression;
import uap.workflow.engine.juel.ExpressionFactoryImpl;
/**
 * <p>
 * Central manager for all expressions.
 * </p>
 * <p>
 * Process parsers will use this to build expression objects that are stored in
 * the process definitions.
 * </p>
 * <p>
 * Then also this class is used as an entry point for runtime evaluation of the
 * expressions.
 * </p>
 * 
 * @author Tom Baeyens
 * @author Dave Syer
 * @author Frederik Heremans
 */
public class ExpressionManager {
	protected ExpressionFactory expressionFactory;
	// Default implementation (does nothing)
	protected ELContext parsingElContext = new ParsingElContext();
	public ExpressionManager() {
		// Use the ExpressionFactoryImpl in activiti build in version of juel,
		// with parametrised method expressions enabled
		expressionFactory = new ExpressionFactoryImpl();
	}
	public Expression createExpression(String expression) {
		ValueExpression valueExpression = expressionFactory.createValueExpression(parsingElContext, expression, Object.class);
		return new JuelExpression(valueExpression, this, expression);
	}
	public void setExpressionFactory(ExpressionFactory expressionFactory) {
		this.expressionFactory = expressionFactory;
	}
	public ELContext getElContext(VariableScope variableScope) {
		ELContext elContext = null;
		if (variableScope instanceof VariableScopeImpl) {
			VariableScopeImpl variableScopeImpl = (VariableScopeImpl) variableScope;
			elContext = variableScopeImpl.getCachedElContext();
		}
		if (elContext == null) {
			elContext = createElContext(variableScope);
			if (variableScope instanceof VariableScopeImpl) {
				((VariableScopeImpl) variableScope).setCachedElContext(elContext);
			}
		}
		return elContext;
	}
	protected WorkflowElContext createElContext(VariableScope variableScope) {
		ELResolver elResolver = createElResolver(variableScope);
		return new WorkflowElContext(elResolver);
	}
	protected ELResolver createElResolver(VariableScope variableScope) {
		CompositeELResolver elResolver = new CompositeELResolver();
		elResolver.add(new VariableScopeElResolver(variableScope));
		elResolver.add(new ArrayELResolver());
		elResolver.add(new ListELResolver());
		elResolver.add(new MapELResolver());
		elResolver.add(new DynamicBeanPropertyELResolver(ItemInstance.class, "getFieldValue", "setFieldValue")); // TODO:
																													// needs
																													// verification
		elResolver.add(new BeanELResolver());
		return elResolver;
	}
}
