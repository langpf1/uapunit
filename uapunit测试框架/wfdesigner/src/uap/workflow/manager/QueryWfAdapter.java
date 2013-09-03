package uap.workflow.manager;

import java.io.StringWriter;
import java.util.List;

import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.parameter.IParameter;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.uap.lfw.core.data.PaginationInfo;

public class QueryWfAdapter {
    private int pageNumber = 1;//默认显示第一页
    private int pageSize = 30;//默认的第一页有10条记录
	@SuppressWarnings("restriction")
	public int perform(StringWriter w,String item, IParameter parameter) throws DAOException {
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		StringBuffer buffer = new StringBuffer();
		int returnValue = 0; String sql = null;
		PaginationInfo page = new PaginationInfo();//构造后台分页查找的"页"
		page.setPageSize(this.pageSize);
		page.setPageIndex(this.pageNumber-1);
		if(item.equalsIgnoreCase("wfins")){
			String start = parameter.getRequest().getParameter("start");
			String pkform = parameter.getRequest().getParameter("pkform");
			String pkproins = parameter.getRequest().getParameter("pkproins");
			//构造sql语句的条件
			sql = creatSqlCondition(start,pkform,pkproins);
			List<ProcessInstanceVO> vos  = ProcessInstanceUtil.getAllProInsByPage(sql,page);
			returnValue = ProcessInstanceUtil.getAllProInsNumber("wf_proins");//得到这张表中所有记录的条数
			int length = (vos.size() >  pageSize) ? pageSize : vos.size();
			for (int i = 0; i < length; i++) {
				if (buffer.length() > 1)
					buffer.append(",\n");
				buffer.append("{'id':'");
				buffer.append(i);
				buffer.append("','pk_starter':'");//发起人
				buffer.append(vos.get(i).getPk_starter());
				buffer.append("','startdate':'");//开始时间
				buffer.append(vos.get(i).getStartdate());
				buffer.append("','enddate':'");//结束时间
				buffer.append(vos.get(i).getEnddate());
				buffer.append("','pk_form_ins':'");//单据编号
				buffer.append(vos.get(i).getPk_form_ins());
				buffer.append("','pk_proins':'");//流程实例pk
				buffer.append(vos.get(i).getPk_proins());
				//buffer.append("','result':'");//流程实例是否通过
				//buffer.append("");
				buffer.append("','state_proins':'");//流程实例的状态
				buffer.append(vos.get(i).getState_proins());
				buffer.append("'}");
			}	
		}
		if(item.equalsIgnoreCase("workitem")){
			ITaskInstanceQry qry = NCLocator.getInstance().lookup(ITaskInstanceQry.class);
			String sql1 = "select * from wf_task";
			returnValue = ProcessInstanceUtil.getAllProInsNumber("wf_task");//得到这张表中所有记录的条数
			List<TaskInstanceVO> taskInstanceVos = qry.getTaskInsVoBySql(sql1,page);	
			int length = (taskInstanceVos.size() > pageNumber * pageSize) ? pageNumber * pageSize : taskInstanceVos.size();
			for (int i = (pageNumber - 1) * pageSize; i < length; i++) {
				if (buffer.length() > 1)
					buffer.append(",\n");
				buffer.append("{'id':'");
				buffer.append(i);
				buffer.append("',pk_creater:'");//发送人
				buffer.append(taskInstanceVos.get(i).getPk_creater());
    			buffer.append("','startdate':'");//开始时间
				buffer.append(taskInstanceVos.get(i).getStartdate());
//				buffer.append("','enddate':'");//结束时间
//				buffer.append(vos[i].getEnddate());
//				buffer.append("','pk_form_ins':'");//单据编号
//				buffer.append(vos[i].getPk_form_ins());
//				buffer.append("','pk_proins':'");//流程实例pk
//				buffer.append(vos[i].getPk_proins());
//				//buffer.append("','result':'");//流程实例是否通过
//				//buffer.append("");
//				buffer.append("','state_proins':'");//流程实例的状态
//				buffer.append(vos[i].getState_proins());
				buffer.append("'}");
			}	
			//returnValue = vos.length;
		}
		w.append("[\n");
		w.append(buffer.toString());
		w.append("\n]");
		return returnValue;
	}
	private String creatSqlCondition(String start, String pkform, String pkproins) {
		String sql=null;
		if(start != null&& start.length() != 0){sql = " pk_starter='" + start + "'";}
		if(pkform != null&& pkform.length() != 0){sql = sql+" and pk_form_ins='" + pkform + "'";}
		if(pkproins != null&& pkproins.length() != 0){sql = sql+" and pk_proins='" + pkproins + "'";}
		return sql;
	}
	protected int getPageNumber() {
		return pageNumber;
	}
	protected void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	protected int getPageSize() {
		return pageSize;
	}
	protected void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
