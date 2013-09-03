package uap.workflow.engine.utils;
import java.util.ArrayList;
import java.util.List;

import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.orgs.FlowUserDesc;
import uap.workflow.engine.rejectsgy.RejectSgyManager;
public class RejectUserTaskInfoUtil {
	/**
	 * 获取回退活动信息
	 * 
	 * @param pktask
	 * @param formVo
	 * @return
	 */
	public static List<UserTaskPrepCtx> getRejectHumActInfo(ITask task) {
		IActivity[] humActs = RejectSgyManager.getInstance().getRejectRange(task);
		if (humActs == null || humActs.length == 0) {
			return null;
		}
		IActivity humAct = null;
		FlowUserDesc[] userVos = null;
		List<UserTaskPrepCtx> list = new ArrayList<UserTaskPrepCtx>();
		UserTaskPrepCtx ctx = null;
		for (int i = 0; i < humActs.length; i++) {
			humAct = humActs[i];
			userVos = RejectSgyManager.getInstance().getRejectUsersByTaskAndHumAct(task, humAct);
			ctx = new UserTaskPrepCtx();
			if (userVos == null || userVos.length == 0) {
				continue;
			}
			if (userVos.length == 1) {
				ctx.setAssign(false);
			} else {
				ctx.setAssign(true);
			}
			ctx.setActivityId(humAct.getId());
			ctx.setActivityName((String) humAct.getProperty("name"));
			ctx.setUserPks(null);
			ctx.setUserNames(null);
			list.add(ctx);
		}
		return list;
	}
	public static List<UserTaskPrepCtx> getRejectHumActInfo(String taskPk) {
		ITask task = TaskUtil.getTaskByTaskPk(taskPk);
		return getRejectHumActInfo(task);
	}
	protected static String getUserPks(FlowUserDesc[] userVos) {
		if (userVos == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		FlowUserDesc userVo = null;
		for (int i = 0; i < userVos.length; i++) {
			userVo = userVos[i];
			buffer.append(userVo.getPk_flowuser()).append(",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}
	protected static String getUserNames(FlowUserDesc[] userVos) {
		if (userVos == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		FlowUserDesc userVo = null;
		for (int i = 0; i < userVos.length; i++) {
			userVo = userVos[i];
			buffer.append(userVo.getName()).append(",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}
}
