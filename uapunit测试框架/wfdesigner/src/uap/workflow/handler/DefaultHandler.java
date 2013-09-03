package uap.workflow.handler;

import javax.servlet.http.HttpServletRequest;

import uap.workflow.parameter.DefaultParameter;
import uap.workflow.parameter.IParameter;

/**
  * DefaultParameter½Ó¿Ú 
  * @author 
  */

public class DefaultHandler implements IHandler{
	private IParameter parameter = new DefaultParameter();

	public IParameter handle(HttpServletRequest request) {
		parameter.setRequest(request);
		//parameter.setParameter(key, value);
		return parameter;
	}

}