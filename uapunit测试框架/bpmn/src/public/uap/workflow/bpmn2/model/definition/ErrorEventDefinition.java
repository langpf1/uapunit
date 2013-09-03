package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/*    */ @XmlAccessorType(XmlAccessType.FIELD)
		 @XmlType(name="ErrorEventDefinition")
/*    */ public class ErrorEventDefinition extends EventDefinition
/*    */ {
		   @XmlAttribute(name="errorRef")
/*    */   public String errorCode;
/*    */ 
/*    */   public String getErrorCode()
/*    */   {
/*  8 */     return this.errorCode;
/*    */   }
/*    */   public void setErrorCode(String errorCode) {
/* 11 */     this.errorCode = errorCode;
/*    */   }
/*    */ }

/* Location:           E:\dev tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer.model_5.9.1.jar
 * Qualified Name:     org.activiti.designer.bpmn2.model.ErrorEventDefinition
 * JD-Core Version:    0.5.4
 */