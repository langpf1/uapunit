package uap.workflow.engine.bridge;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.entity.VariableInstanceEntity;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.io.IoUtil;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.variable.VariableType;
import uap.workflow.engine.variable.VariableTypes;
import uap.workflow.engine.vos.VariableInstanceVO;
public class VariableInstanceBridge implements IBridge<VariableInstanceVO, VariableInstanceEntity> {
	@Override
	public VariableInstanceVO convertT2M(VariableInstanceEntity object) {
		VariableInstanceVO varIns = new VariableInstanceVO();
		varIns.setCode(object.getName());
		varIns.setLongvalue(String.valueOf(object.getLongValue()));
		varIns.setDoublevalue(String.valueOf(object.getDoubleValue()));
		varIns.setTextvalue(String.valueOf(object.getTextValue()));
		varIns.setTextvalue2(String.valueOf(object.getTextValue2()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream ois = null;
		try {
			ois = new ObjectOutputStream(baos);
			ois.writeObject(object.getCachedValue());
			varIns.setObjectvalue(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IoUtil.closeSilently(ois);
		}
		varIns.setPk_process_instance(object.getProcessInstanceId());
		varIns.setPk_activity_instance(object.getExecutionId());
		varIns.setPk_task(object.getTaskId());
		varIns.setPk_variable(object.getId());
		return varIns;
	}
	@Override
	public VariableInstanceEntity convertM2T(VariableInstanceVO object) {
		VariableInstanceEntity varEntity = new VariableInstanceEntity();
		varEntity.setExecutionId(object.getPk_activity_instance());
		varEntity.setProcessInstanceId(object.getPk_process_instance());
		varEntity.setTaskId(object.getPk_task());
		VariableTypes vartype = ((ProcessEngineConfigurationImpl) BizProcessServer.processEngineConfig).getVariableTypes();
		VariableType dateType = null;
		if (object.getLongvalue() != null && !object.getLongvalue().equalsIgnoreCase("null")) {
			varEntity.setLongValue(Long.parseLong(object.getLongvalue()));
			dateType = vartype.findVariableType(varEntity.getLongValue());
		} else if (object.getDoublevalue() != null && !object.getDoublevalue().equalsIgnoreCase("null")) {
			varEntity.setDoubleValue(Double.parseDouble(object.getDoublevalue()));
			dateType = vartype.findVariableType(varEntity.getDoubleValue());
		} else if (object.getTextvalue() != null && !object.getTextvalue().equalsIgnoreCase("null")) {
			varEntity.setTextValue(object.getTextvalue());
			dateType = vartype.findVariableType(String.valueOf(varEntity.getTextValue()));
		} else if (object.getTextvalue2() != null && !object.getTextvalue2().equalsIgnoreCase("null")) {
			varEntity.setTextValue2(object.getTextvalue2());
			dateType = vartype.findVariableType(varEntity.getTextValue2());
		} else if (object.getObjectvalue() != null) {
			ByteArrayInputStream bais = new ByteArrayInputStream(object.getObjectvalue());
			Object deserializedObject;
			try {
				deserializedObject = new ObjectInputStream(bais).readObject();
			} catch (Exception e) {
				throw new WorkflowRuntimeException(e);
			}
			varEntity.setCachedValue(deserializedObject);
			dateType = vartype.findVariableType(varEntity.getCachedValue());
		}
		varEntity.setType(dateType);
		varEntity.setId(object.getPk_variable());
		varEntity.setName(object.getCode());
		return varEntity;
	}
}
