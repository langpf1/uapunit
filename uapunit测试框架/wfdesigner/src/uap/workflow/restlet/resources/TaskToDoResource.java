package uap.workflow.restlet.resources;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import uap.workflow.app.impl.WFEngineServiceImpl;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.reslet.application.receiveData.CreatTaskJsonData;
import uap.workflow.reslet.application.receiveData.GetUserVO;
import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.reslet.application.receiveData.WfWebTask;
import uap.workflow.restlet.application.Pagination;
import uap.workflow.restlet.application.RuntimeConstants;

import com.google.gson.Gson;
public class TaskToDoResource extends ServerResource {
	private Participant currentparticipant = new Participant();
	private String PageSize;
	private String PageNumber;
	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	public void doInit(){
	
	}
	
	@Get
	/*根据 TaskKind 得到不同类型的task 例如：已办，在办，待阅等*/
	public  JSONArray getTaskWaiting(){ 
	    long time0 = System.currentTimeMillis();
		getCurrentParticipant();
		CreatTaskJsonData creattaskjson = new CreatTaskJsonData();
		List<TaskInstanceVO> customers = new ArrayList<TaskInstanceVO>(); 
		WFEngineServiceImpl  wfEngineService  = new  WFEngineServiceImpl();
		List<WfWebTask> webtasklist = new ArrayList<WfWebTask>();
		Pagination page = creatPage();
		/* wherepart 的格式应该是 ：and 列名 = '' */
		long time2 = System.currentTimeMillis();
		customers = wfEngineService.getTasks(TaskInstanceStatus.Wait.getIntValue(), null, null,currentparticipant.getParticipantID(), null, false, page);
		long time3 = System.currentTimeMillis();
		if(customers != null &&!customers.isEmpty()){
			webtasklist = creattaskjson.creatWebTaskList(customers);	
		}
		long time4 = System.currentTimeMillis();
		JSONArray tasklistJson = null;
		try {
			Gson gson = new Gson();
			tasklistJson =new JSONArray(gson.toJson(webtasklist));
			tasklistJson.put(page.getTotalRecords());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 long time5 = System.currentTimeMillis();
		 logger.info("查询数据库,耗时=" + (time3 - time2) + "ms");
		 logger.info("构造数据,耗时=" + (time4 - time3) + "ms");
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