package uap.workflow.restlet.application;

import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.AuthenticationInfo;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.SecretVerifier;
import org.restlet.security.Verifier;
import org.restlet.service.StatusService;
import org.restlet.Context;
import com.google.gson.Gson;

import uap.workflow.reslet.application.receiveData.LogInfo;

public class LoginResource extends ServerResource {
	 protected ChallengeAuthenticator authenticator;
//	 @Override
//	public void doInit(){
//		super.init(getContext(), getRequest(), getResponse());
//		ChallengeResponse challengeresponse = getRequest().getChallengeResponse();
//		if(challengeresponse != null){
//			String username = challengeresponse.getIdentifier();
//			String password = new String (getRequest().getChallengeResponse().getSecret());
//			//从数据库中查找密码，假设就是密码就是用户名
//			if(username == null){
//				getResponse().setEntity("用户名为空", MediaType.TEXT_PLAIN);
//			}
//			
//		}
//	
//	}
	@Post
	  public JSONObject handellogin(Representation entity){	
		try {
			Form form = getRequest().getResourceRef().getQueryAsForm();	
		    JSONObject loginreturn = new JSONObject();
		    JsonRepresentation jsonRepresention = new JsonRepresentation(entity);
			Gson gson = new Gson();
			String json = jsonRepresention.getText();
			
			AuthenticationInfo authenticationInfo = null ;
			getResponse().setAuthenticationInfo(authenticationInfo);
			LogInfo loginInfo = new LogInfo();
			loginInfo = gson.fromJson(json, LogInfo.class);
			String logincondition = null;
			if (loginInfo.getName() == null) {
				logincondition = "No user id supplied";
				authenticateError(logincondition);
				loginreturn.put("returnvalue", logincondition);
				return loginreturn;
			}

			if (loginInfo.getPassword() == null) {
				
				logincondition = "No password supplied";
				authenticateError(logincondition);
				loginreturn.put(loginInfo.getName(), logincondition);
				return loginreturn;
			}
			if (getRequest().getCookies().getFirst("zhai") != null) {
				logincondition = "No password supplied";
				authenticateError(logincondition);
				loginreturn.put(loginInfo.getName(), logincondition);
				return loginreturn;
			}
			if (loginInfo.getPassword().equalsIgnoreCase(loginInfo.getName())) {
				logincondition = "log in success";
				loginreturn.put("name",loginInfo.getName());
				loginreturn.put("message", logincondition);
				return loginreturn;
			} else {
				authenticateError(logincondition);
				return loginreturn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

	private void authenticateError(String logincondition) {
	
		Status status = new Status(403);
		getResponse().setStatus(status, logincondition);
	}
//	public void initializeAuthentication() {
//	    Verifier verifier = new SecretVerifier() {
//
//	      @Override
//	      public boolean verify(String username, char[] password) throws IllegalArgumentException {
//	        boolean verified = checkPassword(username, new String(password));
//	        return verified;
//	      }
//
//		
//	    };
//	   authenticator = new ChallengeAuthenticator(null, true, ChallengeScheme.HTTP_BASIC,
//	          "Activiti Realm") {
//	      
//	      @Override
//	      protected boolean authenticate(Request request, Response response) {
//	        if (request.getChallengeResponse() == null) {
//	          return false;
//	        } else {
//	          return super.authenticate(request, response);
//	        }
//	      }
//	    };
//	    authenticator.setVerifier(verifier);
//	  }
//	
//	private boolean checkPassword(String username, String string) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	public String authenticate(Request request, Response response) {
//	    if (!request.getClientInfo().isAuthenticated()) {
//	      authenticator.challenge(response, false);
//	      return null;
//	    }
//	    return request.getClientInfo().getUser().getIdentifier();
//	  }
//	  
//	  @Override
//	  public StatusService getStatusService() {
//	    return activitiStatusService;
//	  }
//	
}
