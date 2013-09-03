package uap.workflow.engine.itf;

import java.util.List;
import java.util.Map;

import uap.workflow.engine.form.FormData;
import uap.workflow.engine.form.FormProperty;

public interface IFormTransferService {

	Object getStartFormTemplate(String proDefPk);
	Object getTaskFormTemplate(String taskId);
	List<FormProperty> getStartFormProperties(String proDefPk);
	List<FormProperty> getTaskFormProperties(String taskId);
	
	void submitTaskFormData(String taskId, Map<String, String> properties);
	void submitStartFormData(String proDefPk, Map<String, String> properties); 
}
