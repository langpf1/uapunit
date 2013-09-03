package uap.workflow.restlet.resources;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.vo.pub.BusinessException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import uap.workflow.app.participant.BasicParticipant;
import uap.workflow.app.participant.IParticipant;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.reslet.application.receiveData.GetUserVO;
import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.reslet.application.receiveData.Role;

import com.google.gson.Gson;
public class RoleResource extends ServerResource {
	private Role role = new Role();
	private Logger logger = Logger.getLogger(this.getClass());
	@Get
	/*根据 TaskKind 得到不同类型的task 得到单据的信息*/
	public  JSONArray getparticipant(){
		//模拟登陆，绕开安全机制，待验证
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(), "ncc10".getBytes());
		getCurrentParticipant();
		List<Role> rolelist  = null ;
    	rolelist = WfmServiceFacility.getIParticipantService().getRolesByType(role);
		Gson gson = new Gson();
		JSONArray participantlistJson = null;
		try {
			participantlistJson = new JSONArray(gson.toJson(rolelist));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return participantlistJson; 
	} 
	
	private void getCurrentParticipant() {
		String username = getRequest().getCookies().getFirstValue("name");
		GetUserVO getuser = new GetUserVO();
		String userID = getuser.getUserIDByusername(username);
		String roleID = getuser.getUserRole(username);
		role.setName(username);
		role.setRoleID(roleID == null? userID:roleID);
	}
}

