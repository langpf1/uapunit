package uap.workflow.bpmn2.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/*    */ @XmlAccessorType(XmlAccessType.FIELD)
		 @XmlType(name="TimerEventDefinition")
/*    */ public class TimerEventDefinition extends EventDefinition
/*    */ {
/*    */   public String timeDate;
/*    */   public String timeDuration;
/*    */   public String timeCycle;
/*    */ 
/*    */   public String getTimeDate()
/*    */   {
/* 10 */     return this.timeDate;
/*    */   }
/*    */   public void setTimeDate(String timeDate) {
/* 13 */     this.timeDate = timeDate;
/*    */   }
/*    */   public String getTimeDuration() {
/* 16 */     return this.timeDuration;
/*    */   }
/*    */   public void setTimeDuration(String timeDuration) {
/* 19 */     this.timeDuration = timeDuration;
/*    */   }
/*    */   public String getTimeCycle() {
/* 22 */     return this.timeCycle;
/*    */   }
/*    */   public void setTimeCycle(String timeCycle) {
/* 25 */     this.timeCycle = timeCycle;
/*    */   }
/*    */ }

/* Location:           E:\dev tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer.model_5.9.1.jar
 * Qualified Name:     org.activiti.designer.bpmn2.model.TimerEventDefinition
 * JD-Core Version:    0.5.4
 */