package uap.workflow.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uap.workflow.action.IAction;
import uap.workflow.controller.ActionControllerFactory;
import uap.workflow.handler.IHandler;
import uap.workflow.parameter.IParameter;

/**
  * servlet×Ü¿ØÖÆÀà
  */
public class ControllerServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4442397463551836919L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		process(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		process(request, response);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response){
		String actionCode = request.getParameter(IParameter.ACTION);
		if(actionCode == null || actionCode.equals(""))
			return;
		
		IHandler handler = ActionControllerFactory.getInstance().getHandler(actionCode);
		IParameter parameter = handler.handle(request);
		parameter.setResponse(response);
		parameter.setParameter(IParameter.ACTION, actionCode);
		
		IAction action = ActionControllerFactory.getInstance().getAction(actionCode);
		action.perform(parameter);
	}
}
