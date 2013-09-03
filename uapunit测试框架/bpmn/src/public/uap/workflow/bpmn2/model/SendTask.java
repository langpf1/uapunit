package uap.workflow.bpmn2.model;
/*    */ 
/*    */ public class SendTask extends Task
/*    */ {
/*    */   public String to;
/*    */   public String subject;
/*    */   public String text;
/*    */   public String html;
/*    */   public String from;
/*    */   public String cc;
/*    */   public String bcc;
/*    */   public String charset;
/*    */ 
/*    */   public String getTo()
/*    */   {
/* 15 */     return this.to;
/*    */   }
/*    */   public void setTo(String to) {
/* 18 */     this.to = to;
/*    */   }
/*    */   public String getSubject() {
/* 21 */     return this.subject;
/*    */   }
/*    */   public void setSubject(String subject) {
/* 24 */     this.subject = subject;
/*    */   }
/*    */   public String getText() {
/* 27 */     return this.text;
/*    */   }
/*    */   public void setText(String text) {
/* 30 */     this.text = text;
/*    */   }
/*    */   public String getHtml() {
/* 33 */     return this.html;
/*    */   }
/*    */   public void setHtml(String html) {
/* 36 */     this.html = html;
/*    */   }
/*    */   public String getFrom() {
/* 39 */     return this.from;
/*    */   }
/*    */   public void setFrom(String from) {
/* 42 */     this.from = from;
/*    */   }
/*    */   public String getCc() {
/* 45 */     return this.cc;
/*    */   }
/*    */   public void setCc(String cc) {
/* 48 */     this.cc = cc;
/*    */   }
/*    */   public String getBcc() {
/* 51 */     return this.bcc;
/*    */   }
/*    */   public void setBcc(String bcc) {
/* 54 */     this.bcc = bcc;
/*    */   }
/*    */   public String getCharset() {
/* 57 */     return this.charset;
/*    */   }
/*    */   public void setCharset(String charset) {
/* 60 */     this.charset = charset;
/*    */   }

public int getTaskType() {
	return 3;
}
@Override
public String provideBeanInfoClass() {
	return "uap.workflow.modeler.bpmn2.beaninfos.MailTaskBeanInfo";
}
/*    */ }

/* Location:           E:\dev tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer.model_5.9.1.jar
 * Qualified Name:     org.activiti.designer.bpmn2.model.MailTask
 * JD-Core Version:    0.5.4
 */