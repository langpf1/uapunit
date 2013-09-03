package uap.workflow.bpmn2.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PropEditor {
	// 特殊属性的属性编辑器
	String value();
}
