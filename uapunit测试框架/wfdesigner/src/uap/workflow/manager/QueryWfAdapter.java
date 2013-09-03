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
    private int pageNumber = 1;//Ĭ����ʾ��һҳ
    private int pageSize = 30;//Ĭ�ϵĵ�һҳ��10����¼
	@SuppressWarnings("restriction")
	public int perform(StringWriter w,String item, IParameter parameter) throws DAOException {
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		StringBuffer buffer = new StringBuffer();
		int returnValue = 0; String sql = null;
		PaginationInfo page = new PaginationInfo();//�����̨��ҳ���ҵ�"ҳ"
		page.setPageSize(this.pageSize);
		page.setPageIndex(this.pageNumber-1);
		if(item.equalsIgnoreCase("wfins")){
			String start = parameter.getRequest().getParameter("start");
			String pkform = parameter.getRequest().getParameter("pkform");
			String pkproins = parameter.getRequest().getParameter("pkproins");
			//����sql��������
			sql = creatSqlCondition(start,pkform,pkproins);
			List<ProcessInstanceVO> vos  = ProcessInstanceUtil.getAllProInsByPage(sql,page);
			returnValue = ProcessInstanceUtil.getAllProInsNumber("wf_proins");//�õ����ű������м�¼������
			int length = (vos.size() >  pageSize) ? pageSize : vos.size();
			for (int i = 0; i < length; i++) {
				if (buffer.length() > 1)
					buffer.append(",\n");
				buffer.append("{'id':'");
				buffer.append(i);
				buffer.append("','pk_starter':'");//������
				buffer.append(vos.get(i).getPk_starter());
				buffer.append("','startdate':'");//��ʼʱ��
				buffer.append(vos.get(i).getStartdate());
				buffer.append("','enddate':'");//����ʱ��
				buffer.append(vos.get(i).getEnddate());
				buffer.append("','pk_form_ins':'");//���ݱ��
				buffer.append(vos.get(i).getPk_form_ins());
				buffer.append("','pk_proins':'");//����ʵ��pk
				buffer.append(vos.get(i).getPk_proins());
				//buffer.append("','result':'");//����ʵ���Ƿ�ͨ��
				//buffer.append("");
				buffer.append("','state_proins':'");//����ʵ����״̬
				buffer.append(vos.get(i).getState_proins());
				buffer.append("'}");
			}	
		}
		if(item.equalsIgnoreCase("workitem")){
			ITaskInstanceQry qry = NCLocator.getInstance().lookup(ITaskInstanceQry.class);
			String sql1 = "select * from wf_task";
			returnValue = ProcessInstanceUtil.getAllProInsNumber("wf_task");//�õ����ű������м�¼������
			List<TaskInstanceVO> taskInstanceVos = qry.getTaskInsVoBySql(sql1,page);	
			int length = (taskInstanceVos.size() > pageNumber * pageSize) ? pageNumber * pageSize : taskInstanceVos.size();
			for (int i = (pageNumber - 1) * pageSize; i < length; i++) {
				if (buffer.length() > 1)
					buffer.append(",\n");
				buffer.append("{'id':'");
				buffer.append(i);
				buffer.append("',pk_creater:'");//������
				buffer.append(taskInstanceVos.get(i).getPk_creater());
    			buffer.append("','startdate':'");//��ʼʱ��
				buffer.append(taskInstanceVos.get(i).getStartdate());
//				buffer.append("','enddate':'");//����ʱ��
//				buffer.append(vos[i].getEnddate());
//				buffer.append("','pk_form_ins':'");//���ݱ��
//				buffer.append(vos[i].getPk_form_ins());
//				buffer.append("','pk_proins':'");//����ʵ��pk
//				buffer.append(vos[i].getPk_proins());
//				//buffer.append("','result':'");//����ʵ���Ƿ�ͨ��
//				//buffer.append("");
//				buffer.append("','state_proins':'");//����ʵ����״̬
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
