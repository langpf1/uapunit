package uap.workflow.restlet.resources;
import java.util.List;

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

import com.google.gson.Gson;
public class OperatorResource extends ServerResource {
	private Participant currentparticipant = new Participant();
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Get
	/*根据 TaskKind 得到不同类型的task 得到单据的信息*/
	public  JSONArray getparticipant(){
		getCurrentParticipant();
		List<Participant> participantlist  = null ;
		participantlist = WfmServiceFacility.getIParticipantService().getAllUsersbyType(currentparticipant);	
		Gson gson = new Gson();
		JSONArray participantlistJson = null;
		try {
			participantlistJson = new JSONArray(gson.toJson(participantlist));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return participantlistJson;   
	} 
	
	private void getCurrentParticipant() {
		String username = getRequest().getCookies().getFirstValue("name");
		currentparticipant.setName(username);
		GetUserVO getuser = new GetUserVO();
		String userid = getuser.getUserIDByusername(username);
		currentparticipant.setParticipantID(userid);
	}
	
}
