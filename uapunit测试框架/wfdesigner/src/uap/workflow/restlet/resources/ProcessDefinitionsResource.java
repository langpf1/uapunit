package uap.workflow.restlet.resources;
import nc.vo.pub.BusinessException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import uap.workflow.engine.impl.ProcessDefinitionQry;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.reslet.application.receiveData.CreatProdefJsonData;
import uap.workflow.reslet.application.receiveData.ProcessDefinition;
public class ProcessDefinitionsResource extends ServerResource {
	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	/*初始化的动作*/
	public void doInit(){
		logger.info("processdefinition is init");
	}
	
	@Post
	public JSONObject getParticipant(Representation entity) throws BusinessException {
		return null;
	}	
	@Get
	public  JSONArray getprocessDefinition(){
		//TODO
		/**此处没有再使用NCLocator，以后确定是否使用这个*/
		String pk_group = "00012410000000000H12";
		ProcessDefinitionQry proDefQry  = new ProcessDefinitionQry();
		ProcessDefinitionVO[] prodeflist = proDefQry.getAllProcessDef(pk_group);
		CreatProdefJsonData creatprodefjson = new CreatProdefJsonData();
		prodeflist = creatprodefjson.creatprodefjson(prodeflist); 
		JSONArray prodeflistJson = null;
		try {
			Gson gson = new Gson();
			prodeflistJson = new JSONArray(gson.toJson(prodeflist));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prodeflistJson;
	} 
}
