package uap.workflow.handler;

import javax.servlet.http.HttpServletRequest;

import uap.workflow.parameter.IParameter;

/**
  * QueryUserHandler 
  * @author 
  */

@SuppressWarnings("restriction")
public class QueryUserHandler extends DefaultHandler {
	public IParameter handle(HttpServletRequest request) {	
		IParameter parameter = super.handle(request);
		//parameter.setParameter(key, value);
		return parameter;
	}

}