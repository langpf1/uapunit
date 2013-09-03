package uap.workflow.restlet.application;

import java.text.ParseException;
import java.util.Map;

import org.json.JSONObject;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import uap.workflow.reslet.application.receiveData.LogInfo;

public class LogoutResource extends ServerResource {
	@Post
	  public void logout(LogInfo loginInfo) throws ParseException {	    
	    if(getRequest().getCookies().getFirst("zhai") != null){
	    	getRequest().getCookies().removeFirst("zhai");
	    }
	 }
}
