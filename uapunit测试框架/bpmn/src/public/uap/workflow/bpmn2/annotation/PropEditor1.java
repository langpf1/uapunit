package uap.workflow.bpmn2.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PropEditor1 {
	//�������Ե����Ա༭��
	String value();
	Class<?> type();
}
