package uap.workflow.itf;

import java.util.Map;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.vo.WorkflownoteVO;

public interface IWorkflowServiceUtil {
	Object start(Object billvo, Map<String, Object> customData) throws BusinessException;
	Object forward(WorkflownoteVO noteVO, Object billvo, Map<String, Object> customData) throws BusinessException;
	Object reject(Object billvo, Map<String, Object> customData) throws BusinessException;
	Object backword(Object billvo, Map<String, Object> customData)throws BusinessException;
	TaskInstanceVO[] getToDoWorkitems(String billId, String userPK) throws BusinessException;
}
