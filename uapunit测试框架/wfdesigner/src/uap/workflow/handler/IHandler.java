package uap.workflow.handler;

import javax.servlet.http.HttpServletRequest;

import uap.workflow.parameter.IParameter;

/**
  * IHandler�ӿ� 
  * @author 
  */

public interface IHandler{

	public IParameter handle(HttpServletRequest request);

}