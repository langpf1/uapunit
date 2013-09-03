package uap.workflow.reslet.application.receiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.impl.WorkflowInstanceQry;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ActivityInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;

public class CreatTaskJsonData {
	private Logger logger = Logger.getLogger(this.getClass());
	private Map<String,UserVO> userlist = new HashMap<String,UserVO>();
	public List<WfWebTask> creatWebTaskList(List<TaskInstanceVO> taskvolist) {
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
		
		List<WfWebTask> tasklist = new ArrayList<WfWebTask>();
		IBusinessKey bizObject = new BizObjectImpl();
		long time0 = System.currentTimeMillis();
		for(TaskInstanceVO task : taskvolist){
			
			WfWebTask  tasktemp = new WfWebTask();
			tasktemp.setTaskID(task.getPk_task());
			tasktemp.setPk_activity_instance(task.getPk_activity_instance());
			tasktemp.setPk_task(task.getPk_task());
			tasktemp.setProcess_def_name(task.getProcess_def_name());
			tasktemp.setPk_form_ins_version(task.getPk_form_ins_version());
			tasktemp.setPk_bizobject((task.getPk_bizobject()));
//			tasktemp.setPk_bizobject("�ͻ����뵥");
			tasktemp.setForm_no(task.getForm_no());
			
			tasktemp.setBegindate(task.getStartdate().toString());//////
//			tasktemp.setDutedate(task.getDutedate().toString());////////
//			tasktemp.setFinishdate(task.getFinishdate().toString());///////
			
			tasktemp.setOpinion(task.getOpinion());
			if(task.getPk_owner() != null)
				tasktemp.setPk_owner(getUserNameByuserpk(task.getPk_owner(),iCorpService));
			if(task.getPk_creater() != null)
				tasktemp.setPk_creater(getUserNameByuserpk(task.getPk_creater(),iCorpService));
			if(task.getPk_executer() != null)
				tasktemp.setPk_executer(getUserNameByuserpk(task.getPk_executer(),iCorpService));
			tasktemp.setActivity_name(task.getActivity_name());
			
			tasktemp.setState_task(task.getState_task());
//			tasktemp.setPk_group(task.getPk_group());
			tasktemp.setPk_group("����������");
			
//			bizObject.setBillType(task.getPk_bizobject());
			bizObject.setBillType("�ͻ����뵥");
			bizObject.setBillNo(task.getForm_no());
			
			
			IProcessDefinition  processDefinition= ProcessDefinitionUtil.getProDefByProDefPk(task.getPk_process_def());
			Map<String, TaskDefinition>  taskDefinitions = processDefinition.getTaskDefinitions();
			TaskDefinition taskDefinition = taskDefinitions.get(task.getActivity_id());
			/**������Ա�������ѡ�� ��[2]���أ�[3]��ǩ��[4]���ɣ�[5]ָ��
			 * Ĭ�ϵ��������ȫ������ѡ���
			 *  ���� 
			 *  REJECT(2),
			 *  ��ǩ 
			 *  ADDSIGN(3),
			 *  ���� *
			 *  DESIGNATE(4),
			 *  ָ��
			 *  REASSIGNMENT(5);
			 * */
			tasktemp.getOperators().add(new Integer(ActionCode.APPROVE.getIntValue()));
			tasktemp.getOperators().add(new Integer(ActionCode.UNAPPROVE.getIntValue())); 
			if(!(taskDefinition).isCanReject()){//����
                	tasktemp.getOperators().add(new Integer(ActionCode.REJECT.getIntValue()));
                	WorkflowInstanceQry wfinsqry = new WorkflowInstanceQry() ;
                	ActivityInstanceVO[] activityins = wfinsqry.getActInsVoByProInsPk(task.getPk_process_instance());
                	for(ActivityInstanceVO activity : activityins){
                		if(activity.getIsexe().booleanValue()){
                			if(taskDefinitions.get(activity.getPort_id())!= null){
                				RejectNode rejectnode = new RejectNode();
                    			rejectnode.setRejectID(activity.getPort_id());
                    			rejectnode.setRejectName(taskDefinitions.get(activity.getPort_id()).getNameExpression().getExpressionText());
                    			tasktemp.getRejectNodes().add(rejectnode);
                			}
                		}
                	}
				}
                if(!taskDefinition.isAddSign()){//��ǩ
                	tasktemp.getOperators().add(new Integer(ActionCode.ADDSIGN.getIntValue()));
				}
                if(!taskDefinition.isDelegate()){//����
                	tasktemp.getOperators().add(new Integer(ActionCode.REASSIGN.getIntValue()));
				}
                if(taskDefinition.isAssign()){//ָ�ɣ�Ĭ���ǲ�ָ�ɵ�
                	tasktemp.getOperators().add(new Integer(ActionCode.ASSIGN.getIntValue()));
				}
               
			tasklist.add(tasktemp);
		}
		 long time1 = System.currentTimeMillis();
		 logger.info("��ѯ���ݿ�������������,��ʱ=" + (time1 - time0) + "ms");
		return tasklist;
	}

	private String getUserNameByuserpk(String pk_owner, IUserManageQuery iCorpService){
		UserVO user = new UserVO();
		String username = null;
		try {
			if(userlist.containsKey(pk_owner)){
				 username = userlist.get(pk_owner).getUser_name();
			}else{
				user = iCorpService.getUser(pk_owner);
				userlist.put(user.getPrimaryKey(), user);
				username =  user.getUser_name();
			}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return username;
	}
	

}
