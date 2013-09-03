package uap.workflow.restlet.resources;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.reslet.application.receiveData.CreatTaskJsonData;
import uap.workflow.reslet.application.receiveData.GetUserVO;
import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.reslet.application.receiveData.WfWebTask;
import uap.workflow.restlet.application.Pagination;
import uap.workflow.restlet.application.RuntimeConstants;

import com.google.gson.Gson;
public class TaskProcessingResource extends ServerResource {
	private Participant currentparticipant = new Participant();
	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	/*初始化的动作*/
	public void doInit(){
		
	}
	
	@Get
	/*根据 TaskKind 得到不同类型的task 例如：已办，在办，待阅等*/
	public  JSONArray getTask(){ 
		getCurrentParticipant();
		CreatTaskJsonData creattaskjson = new CreatTaskJsonData();
		List<TaskInstanceVO> customers = new ArrayList<TaskInstanceVO>();
	
		List<WfWebTask> webtasklist = new ArrayList<WfWebTask>();
		// 模拟登陆，绕过安全机制
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(), "ncc10".getBytes());
		/* wherepart 的格式应该是 ：and 列名 = '' */
		Pagination page  = new Pagination();
		page.setPageNumber(1);
		page.setPageSize(10);
		customers =TaskUtil.getTasks(TaskInstanceStatus.Run.getIntValue(), null, null,currentparticipant.getParticipantID(), null, true,page);
		webtasklist = creattaskjson.creatWebTaskList(customers);
		JSONArray tasklistJson = null;
		try {
			Gson gson = new Gson();
			tasklistJson =new JSONArray(gson.toJson(webtasklist));
			tasklistJson.put(page.getTotalRecords());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasklistJson ; 
	} 
	private Pagination creatPage() {
		Pagination page  = new Pagination();
		if(getQuery().getFirstValue(RuntimeConstants.PageNumber.getStringValue()) != null){
			page.setPageNumber(Integer.parseInt(getQuery().getFirstValue(RuntimeConstants.PageNumber.getStringValue())));
		}
		if(getQuery().getFirstValue(RuntimeConstants.PageSize.getStringValue()) != null){
			page.setPageSize(Integer.parseInt(getQuery().getFirstValue(RuntimeConstants.PageSize.getStringValue())));
		}
		return page;
	} 
	private void getCurrentParticipant() {
		String username = getRequest().getCookies().getFirstValue("name");
		currentparticipant.setName(username);
		GetUserVO getuser = new GetUserVO();
		String userid = getuser.getUserIDByusername(username);
		currentparticipant.setParticipantID(userid);
	}
}