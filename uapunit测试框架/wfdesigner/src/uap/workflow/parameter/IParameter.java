package uap.workflow.parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
  * IParameter½Ó¿Ú 
  * @author 
  */

public interface IParameter{
	Object getParameter(String key);
	void setParameter(String key, Object value);
	HttpServletRequest getRequest();
	HttpServletResponse getResponse();
	void setRequest(HttpServletRequest request);
	void setResponse(HttpServletResponse response);
	final String REQUEST = "request";
	final String RESPONSE = "response";
	final String ACTION = "action";
}