package uap.workflow.parameter;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
  * DefaultParameter½Ó¿Ú 
  * @author 
  */

public class DefaultParameter implements IParameter{
	HashMap<String, Object> parameterMap = new HashMap<String, Object>();
	
	public Object getParameter(String key){
		return parameterMap.get(key);
	}
	
	public void setParameter(String key, Object value){
		parameterMap.put(key, value);
	}
	
	@SuppressWarnings("restriction")
	public HttpServletRequest getRequest(){
		HttpServletRequest request= null;
		if(parameterMap.containsKey(IParameter.REQUEST)){
			Object object = parameterMap.get(IParameter.REQUEST);
			if(object instanceof HttpServletRequest)
				request = (HttpServletRequest)object;
		}
		 return request;
	}
	
	public HttpServletResponse getResponse(){
		HttpServletResponse response = null;
		if(parameterMap.containsKey(IParameter.RESPONSE)){
			Object object = parameterMap.get(IParameter.RESPONSE);
			if(object instanceof HttpServletResponse)
				response = (HttpServletResponse)object;
		}
		return response;
	}

	public void setRequest(HttpServletRequest request) {
		parameterMap.put(IParameter.REQUEST, request);
	}

	public void setResponse(HttpServletResponse response) {
		parameterMap.put(IParameter.RESPONSE, response);
	}
}