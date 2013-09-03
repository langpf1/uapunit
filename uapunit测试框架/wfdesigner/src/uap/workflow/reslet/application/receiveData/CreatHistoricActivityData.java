package uap.workflow.reslet.application.receiveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

import uap.workflow.engine.vos.TaskInstanceVO;

public class CreatHistoricActivityData {
	private Map<String,UserVO> userlist = new HashMap<String,UserVO>();
	public List<HistoricActivity> creatHistoricTask(List<TaskInstanceVO> task ,List<HistoricActivity>  historicTaskList) {
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		sc.token("LGW".getBytes(),"ncc10".getBytes());
		IUserManageQuery iCorpService = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class);
		
		for(TaskInstanceVO taskinstance : task){
			HistoricActivity historictask = new HistoricActivity();
			historictask.setActivity_name(taskinstance.getActivity_name());
			historictask.setBegindate(taskinstance.getBegindate().toString());
			historictask.setComment(taskinstance.getOpinion());
			if(taskinstance.getFinishdate() != null)
				historictask.setFinishdate(taskinstance.getFinishdate().toString());
			if(taskinstance.getPk_executer() != null)
				historictask.setPk_executer(getUserNameByuserpk(taskinstance.getPk_executer(),iCorpService));
			historicTaskList.add(historictask);
		}
		return historicTaskList;
	}

//TODO   这个方法可以抽成一个类，独立出来，考虑到ORG模型的接口的不全，暂留。

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
