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
package uap.workflow.engine.bpmn.helper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import uap.workflow.engine.bpmn.behavior.AbstractBpmnActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ServiceTaskJavaDelegateActivityBehavior;
import uap.workflow.engine.bpmn.parser.FieldDeclaration;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ITaskListener;
import uap.workflow.engine.delegate.BpmnError;
import uap.workflow.engine.delegate.JavaDelegate;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.invocation.ExecutionListenerInvocation;
import uap.workflow.engine.invocation.TaskListenerInvocation;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.behavior.SignallableActivityBehavior;
import uap.workflow.engine.util.ReflectUtil;
/**
 * Helper class for bpmn constructs that allow class delegation.
 * 
 * This class will lazily instantiate the referenced classes when needed at
 * runtime.
 * 
 * @author Joram Barrez
 * @author Falko Menge
 */
public class ClassDelegate extends AbstractBpmnActivityBehavior implements ITaskListener, IInstanceListener {
	protected String className;
	protected List<FieldDeclaration> fieldDeclarations;
	protected IInstanceListener executionListenerInstance;
	protected ITaskListener taskListenerInstance;
	protected ActivityBehavior activityBehaviorInstance;
	public ClassDelegate(String className, List<FieldDeclaration> fieldDeclarations) {
		this.className = className;
		this.fieldDeclarations = fieldDeclarations;
	}
	public ClassDelegate(Class<?> clazz, List<FieldDeclaration> fieldDeclarations) {
		this(clazz.getName(), fieldDeclarations);
	}
	// Execution listener
	public void notify(IActivityInstance execution) throws Exception {
		if (executionListenerInstance == null) {
			executionListenerInstance = getExecutionListenerInstance();
		}
		Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ExecutionListenerInvocation(executionListenerInstance, execution));
	}
	protected IInstanceListener getExecutionListenerInstance() {
		Object delegateInstance = instantiateDelegate(className, fieldDeclarations);
		if (delegateInstance instanceof IInstanceListener) {
			return (IInstanceListener) delegateInstance;
		} else if (delegateInstance instanceof JavaDelegate) {
			return new ServiceTaskJavaDelegateActivityBehavior((JavaDelegate) delegateInstance);
		} else {
			throw new WorkflowException(delegateInstance.getClass().getName() + " doesn't implement " + IInstanceListener.class + " nor " + JavaDelegate.class);
		}
	}
	// Task listener
	public void notify(ITask delegateTask) {
		if (taskListenerInstance == null) {
			taskListenerInstance = getTaskListenerInstance();
		}
		try {
			Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new TaskListenerInvocation(taskListenerInstance, delegateTask));
		} catch (Exception e) {
			throw new WorkflowException("Exception while invoking TaskListener: " + e.getMessage(), e);
		}
	}
	protected ITaskListener getTaskListenerInstance() {
		Object delegateInstance = instantiateDelegate(className, fieldDeclarations);
		if (delegateInstance instanceof ITaskListener) {
			return (ITaskListener) delegateInstance;
		} else {
			throw new WorkflowException(delegateInstance.getClass().getName() + " doesn't implement " + ITaskListener.class);
		}
	}
	// Activity Behavior
	public void execute(IActivityInstance execution) throws Exception {
		if (activityBehaviorInstance == null) {
			activityBehaviorInstance = getActivityBehaviorInstance(execution);
		}
		try {
			activityBehaviorInstance.execute(execution);
		} catch (BpmnError error) {
			ErrorPropagation.propagateError(error, execution);
		}
	}
	// Signallable activity behavior
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		if (activityBehaviorInstance == null) {
			activityBehaviorInstance = getActivityBehaviorInstance(execution);
		}
		if (activityBehaviorInstance instanceof SignallableActivityBehavior) {
			((SignallableActivityBehavior) activityBehaviorInstance).signal(execution, signalName, signalData);
		} else {
			throw new WorkflowException("signal() can only be called on a " + SignallableActivityBehavior.class.getName() + " instance");
		}
	}
	protected ActivityBehavior getActivityBehaviorInstance(IActivityInstance execution) {
		Object delegateInstance = instantiateDelegate(className, fieldDeclarations);
		if (delegateInstance instanceof ActivityBehavior) {
			return determineBehaviour((ActivityBehavior) delegateInstance, execution);
		} else if (delegateInstance instanceof JavaDelegate) {
			return determineBehaviour(new ServiceTaskJavaDelegateActivityBehavior((JavaDelegate) delegateInstance), execution);
		} else {
			throw new WorkflowException(delegateInstance.getClass().getName() + " doesn't implement " + JavaDelegate.class.getName() + " nor " + ActivityBehavior.class.getName());
		}
	}
	// Adds properties to the given delegation instance (eg multi instance) if
	// needed
	protected ActivityBehavior determineBehaviour(ActivityBehavior delegateInstance, IActivityInstance execution) {
		if (hasMultiInstanceCharacteristics()) {
			multiInstanceActivityBehavior.setInnerActivityBehavior((AbstractBpmnActivityBehavior) delegateInstance);
			return multiInstanceActivityBehavior;
		}
		return delegateInstance;
	}
	// --HELPER METHODS (also usable by external classes)
	// ----------------------------------------
	public static Object instantiateDelegate(Class<?> clazz, List<FieldDeclaration> fieldDeclarations) {
		return instantiateDelegate(clazz.getName(), fieldDeclarations);
	}
	public static Object instantiateDelegate(String className, List<FieldDeclaration> fieldDeclarations) {
		Object object = ReflectUtil.instantiate(className);
		if (fieldDeclarations != null) {
			for (FieldDeclaration declaration : fieldDeclarations) {
				applyFieldDeclaration(declaration, object);
			}
		}
		return object;
	}
	private static void applyFieldDeclaration(FieldDeclaration declaration, Object target) {
		Method setterMethod = ReflectUtil.getSetter(declaration.getName(), target.getClass(), declaration.getValue().getClass());
		if (setterMethod != null) {
			try {
				setterMethod.invoke(target, declaration.getValue());
			} catch (IllegalArgumentException e) {
				throw new WorkflowException("Error while invoking '" + declaration.getName() + "' on class " + target.getClass().getName(), e);
			} catch (IllegalAccessException e) {
				throw new WorkflowException("Illegal acces when calling '" + declaration.getName() + "' on class " + target.getClass().getName(), e);
			} catch (InvocationTargetException e) {
				throw new WorkflowException("Exception while invoking '" + declaration.getName() + "' on class " + target.getClass().getName(), e);
			}
		} else {
			Field field = ReflectUtil.getField(declaration.getName(), target);
			if (field == null) {
				throw new WorkflowException("Field definition uses unexisting field '" + declaration.getName() + "' on class " + target.getClass().getName());
			}
			// Check if the delegate field's type is correct
			if (!fieldTypeCompatible(declaration, field)) {
				throw new WorkflowException("Incompatible type set on field declaration '" + declaration.getName() + "' for class " + target.getClass().getName() + ". Declared value has type "
						+ declaration.getValue().getClass().getName() + ", while expecting " + field.getType().getName());
			}
			ReflectUtil.setField(field, target, declaration.getValue());
		}
	}
	public static boolean fieldTypeCompatible(FieldDeclaration declaration, Field field) {
		if (declaration.getValue() != null) {
			return field.getType().isAssignableFrom(declaration.getValue().getClass());
		} else {
			// Null can be set any field type
			return true;
		}
	}
}
