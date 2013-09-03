package uap.workflow.bpmn2.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * value ="name";   更新名称 
 * value ="task";   更新Task
 * value=“gateWay”; 更新gateWay
 * */
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeChangeMonitor {
	String value();
}
