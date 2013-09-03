/*    */package uap.workflow.designer.exports;
/*    */
/*    */import java.util.List;
import javax.xml.stream.XMLStreamWriter;

import uap.workflow.bpmn2.model.TaskListener;
/*    */
/*    */public class TaskListenerExport
/*    */implements ActivityNamespaceConstants
/*    */{
	/*    */public static void createTaskListenerXML(List<TaskListener> listenerList, boolean writeExtensionsElement, XMLStreamWriter xtw)
	/*    */throws Exception
	/*    */{
		/* 29 */if ((listenerList == null) || (listenerList.size() == 0)) {
			/* 30 */return;
			/*    */}
		/* 32 */if (writeExtensionsElement) {
			/* 33 */xtw.writeStartElement("extensionElements");
			/*    */}
		/* 45 */if (writeExtensionsElement)
			/* 46 */xtw.writeEndElement();
		/*    */}
	/*    */}
/*
 * Location: E:\dev
 * tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer
 * .export.bpmn20_5.9.1.jar Qualified Name:
 * org.activiti.designer.export.bpmn20.export.TaskListenerExport JD-Core
 * Version: 0.5.4
 */