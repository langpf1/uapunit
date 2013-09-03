package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/*    */ @XmlAccessorType(XmlAccessType.FIELD)
		 @XmlType(name="SignalEventDefinition")
/*    */ public class SignalEventDefinition extends EventDefinition
/*    */ {
		   @XmlAttribute
/*    */   public String signalRef;
/*    */ 
/*    */   public String getSignalRef()
/*    */   {
/*  8 */     return this.signalRef;
/*    */   }
/*    */ 
/*    */   public void setSignalRef(String signalRef) {
/* 12 */     this.signalRef = signalRef;
/*    */   }
/*    */ }

/* Location:           E:\dev tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer.model_5.9.1.jar
 * Qualified Name:     org.activiti.designer.bpmn2.model.SignalEventDefinition
 * JD-Core Version:    0.5.4
 */