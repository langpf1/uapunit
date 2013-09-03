package uap.workflow.engine.bridge;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ProcessInstanceVO;
public class ProcessInstanceBridge implements IBridge<ProcessInstanceVO, IProcessInstance> {
	@Override
	public ProcessInstanceVO convertT2M(IProcessInstance object) {
		ProcessInstanceEntity processInstanceEntity = (ProcessInstanceEntity) object;
		ProcessInstanceVO proInsVo = new ProcessInstanceVO();
		proInsVo.setPk_starter(processInstanceEntity.getPk_starter());
		proInsVo.setPk_proins(processInstanceEntity.getPk_proins());
		proInsVo.setStartdate(processInstanceEntity.getStartdatetime());
		proInsVo.setEnddate(processInstanceEntity.getEnddatetime());
		proInsVo.setDuedate(processInstanceEntity.getDuedatetime());
		proInsVo.setPk_form_ins(processInstanceEntity.getPk_business());
		proInsVo.setPk_group(processInstanceEntity.getPk_group());
		proInsVo.setPk_org(processInstanceEntity.getPk_org());
		proInsVo.setPk_prodef(object.getProcessDefinition().getProDefPk());
		proInsVo.setState_proins(processInstanceEntity.getState_proins().getIntValue());
		proInsVo.setPk_form_ins(processInstanceEntity.getPk_form_ins());
		proInsVo.setPk_form_ins_version(processInstanceEntity.getPk_form_ins_version());
		proInsVo.setPk_bizobject(processInstanceEntity.getPk_bizobject());
		proInsVo.setPk_biztrans(processInstanceEntity.getPk_biztrans());
		if (processInstanceEntity.getParentProcessInstance() != null) {
			proInsVo.setPk_parent(processInstanceEntity.getParentProcessInstance().getProInsPk());
		}
		if (processInstanceEntity.getSuperActivityInstance() != null) {
			proInsVo.setPk_super(processInstanceEntity.getSuperActivityInstance().getActInsPk());
		}
		return proInsVo;
	}
	@Override
	public ProcessInstanceEntity convertM2T(ProcessInstanceVO object) {
		ProcessInstanceEntity proIns = new ProcessInstanceEntity();
		proIns.setProInsVo(object);
		proIns.setPk_starter(object.getPk_starter());
		proIns.setPk_proins(object.getPk_proins());
		proIns.setStartdatetime(object.getStartdate());
		proIns.setEnddatetime(object.getEnddate());
		proIns.setDuedatetime(object.getDuedate());
		proIns.setPk_business(object.getPk_form_ins());
		proIns.setPk_group(object.getPk_group());
		proIns.setPk_org(object.getPk_org());
		proIns.setProcessDefinition(ProcessDefinitionUtil.getProDefByProDefPk(object.getPk_prodef()));
		return proIns;
	}
}
