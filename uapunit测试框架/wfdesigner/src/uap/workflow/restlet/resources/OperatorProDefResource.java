package uap.workflow.restlet.resources;
import nc.bs.dao.DAOException;
import nc.vo.pub.BusinessException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.restlet.data.CharacterSet;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.reslet.application.receiveData.OperatorProDef;

import com.google.gson.Gson;
public class OperatorProDefResource extends ServerResource {
	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	/*初始化的动作*/
	public void doInit(){
	}
	
	@Post
	public JSONObject getParticipant(Representation entity) throws BusinessException {
		CharacterSet characterSet = new CharacterSet("utf-8");
		entity.setCharacterSet(characterSet);
		OperatorProDef operatorprodef =  new OperatorProDef();
		Gson gson = new Gson();
		try {
			JsonRepresentation jsonRepresention = new JsonRepresentation(entity);
			String json = jsonRepresention.getText();
			operatorprodef =gson.fromJson(json, OperatorProDef.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (operatorprodef.getActioncode()) {
		case Activate:
			ProcessDefinitionUtil.setProcessDefinitionStatus(operatorprodef.getPk_prodef(),ProcessDefinitionStatusEnum.Valid);
			break;
		case Suspend:
			ProcessDefinitionUtil.setProcessDefinitionStatus(operatorprodef.getPk_prodef(), ProcessDefinitionStatusEnum.Suspend);
			break;
		case Terminate:
			ProcessDefinitionUtil.setProcessDefinitionStatus(operatorprodef.getPk_prodef(), ProcessDefinitionStatusEnum.Invalid);
			break;
		case Delete:
			if(iscandelete(operatorprodef)){
				WfmServiceFacility.getRepositoryService().createDeployment().deleteDeployment(operatorprodef.getPk_prodef(), true);
			}else{
				break;
			}
			break;
		default:
			break;
		}
		return null;
		
	}

	private boolean iscandelete(OperatorProDef operatorprodef) {
		boolean returnvalue = false ;
		try {
		 returnvalue = ProcessInstanceUtil.getAllProInsByPage("pk_prodef = '"+operatorprodef.getPk_prodef()+"'", null).isEmpty();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return returnvalue;
	}	
}
