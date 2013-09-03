package uap.workflow.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.DAOException;

import uap.workflow.action.IAction;
import uap.workflow.parameter.IParameter;

public class QueryWf implements  IAction{

	
	@SuppressWarnings("restriction")
	@Override
	public void perform(IParameter parameter) {
		int rows=0;
		StringWriter w = new StringWriter();
		String item = parameter.getRequest().getParameter("item");
		String pageNumber = parameter.getRequest().getParameter("pageNumber");
		String pageSize = parameter.getRequest().getParameter("pageSize");
		QueryWfAdapter adaper = new QueryWfAdapter();
		if(!pageNumber.equalsIgnoreCase("null")&& !pageSize.equalsIgnoreCase("null")){
			adaper.setPageNumber(Integer.parseInt(pageNumber));
			adaper.setPageSize(Integer.parseInt(pageSize));	
		}
		try {
			rows = adaper.perform(w,item,parameter);
		} catch (DAOException e1) {
			e1.printStackTrace();
		}
		HttpServletResponse response = parameter.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print("{'total':"+rows+",'rows':"+w.toString()+"}");
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

