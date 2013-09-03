package uap.workflow.modeler.utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
import java.util.Map;

import uap.workflow.bpmn2.model.Bpmn2MemoryModel;

/*    */ 
/*    */ public class ModelHandler
/*    */ {
/* 12 */   private static Map<String, Bpmn2MemoryModel> modelMap = new HashMap();
/*    */ 
/*    */   public static void addModel(String uri, Bpmn2MemoryModel model) {
/* 15 */     modelMap.put(uri, model);
/*    */   }
/*    */ 
/*    */   public static Bpmn2MemoryModel getModel(String uri) {
/* 19 */     return (Bpmn2MemoryModel)modelMap.get(uri);
/*    */   }
/*    */ 
/*    */   public static void removeModel(String uri) {
/* 23 */     modelMap.remove(uri);
/*    */   }
/*    */ 
/*    */   public static List<String> getModelURIList() {
/* 27 */     List modelURIList = new ArrayList();
/* 28 */     for (Bpmn2MemoryModel model : modelMap.values()) {
/* 29 */       modelURIList.add("/modelerDebug/src/main/resources/diagrams/MyProcess.bpmn");
/*    */     }
/* 31 */     return modelURIList;
/*    */   }
/*    */ }

/* Location:           E:\dev tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer.util_5.9.1.jar
 * Qualified Name:     org.activiti.designer.util.editor.ModelHandler
 * JD-Core Version:    0.5.4
 */