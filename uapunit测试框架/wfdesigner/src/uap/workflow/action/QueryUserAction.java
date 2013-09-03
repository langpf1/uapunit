package uap.workflow.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import com.sun.net.httpserver.HttpContext;

import uap.workflow.oaadapter.QueryReceiveUserAdapter;
import uap.workflow.oaadapter.QueryUserAdapter;
import uap.workflow.parameter.IParameter;

public class QueryUserAction implements  IAction{

	
	@SuppressWarnings("restriction")
	@Override
	public void perform(IParameter parameter) {
		int rows=0;
		StringWriter w = new StringWriter();
		String kind = parameter.getRequest().getParameter("kind") ;
		String pageNumber = parameter.getRequest().getParameter("pageNumber");
		String pageSize = parameter.getRequest().getParameter("pageSize");
		String filterValue = parameter.getRequest().getParameter("filterValue") ;
		String ParticipanteKind = parameter.getRequest().getParameter("ParticipanteKind") ; 
		if(kind.equalsIgnoreCase("participante")){//参与人的选择
			QueryUserAdapter adaper = new QueryUserAdapter();
			if(pageNumber!=null){		 
				 adaper.setNum(Integer.parseInt(pageNumber));
			}
			if(pageSize!=null){	 
				 adaper.setSize(Integer.parseInt(pageSize));
			}
			if(ParticipanteKind!=null){
				adaper.setParticipanteKind(ParticipanteKind);
			}
			if(filterValue!=null){
				adaper.setFilter(filterValue);
			}
			rows = adaper.perform11(w);
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
		if(kind.equals("notice")){
			String receiveKind = parameter.getRequest().getParameter("selected");
			String nodeId = parameter.getRequest().getParameter("nodeId");
			QueryReceiveUserAdapter receivePeople = new QueryReceiveUserAdapter();
			rows = receivePeople.perform(receiveKind,nodeId,w);
			HttpServletResponse response = parameter.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(w.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
}

