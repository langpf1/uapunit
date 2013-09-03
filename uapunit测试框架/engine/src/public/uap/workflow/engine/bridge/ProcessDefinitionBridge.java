package uap.workflow.engine.bridge;
import nc.vo.pub.lang.UFBoolean;
import uap.workflow.engine.entity.ProcessDefinitionEntity;
import uap.workflow.engine.vos.ProcessDefinitionVO;
public class ProcessDefinitionBridge implements IBridge<ProcessDefinitionVO, ProcessDefinitionEntity> {
	@Override
	public ProcessDefinitionVO convertT2M(ProcessDefinitionEntity object) {
		if (object == null) {
			return null;
		}
		ProcessDefinitionVO proDefVo = new ProcessDefinitionVO();
		proDefVo.setProdef_id(object.getProDefId());
		proDefVo.setPk_prodef(object.getProDefPk());
		proDefVo.setProdef_name(object.getName());
		proDefVo.setProdef_desc(object.getDescription());
		proDefVo.setProdef_version(String.valueOf(object.getVersion()));
		proDefVo.setProcessxml(object.getResourceBytes());
		proDefVo.setDiagramimg(object.getDiagramBytes());
		proDefVo.setIspublic(new UFBoolean(object.isPublic()));
		proDefVo.setPk_group(object.getPk_group());
		proDefVo.setPk_bizobject(object.getPk_bizobject());
		proDefVo.setPk_biztrans(object.getPk_biztrans());
		proDefVo.setValidity(object.getValidity());
		return proDefVo;
	}
	@Override
	public ProcessDefinitionEntity convertM2T(ProcessDefinitionVO object) {
		if (object == null) {
			return null;
		}
		ProcessDefinitionEntity proDef = new ProcessDefinitionEntity(object.getProdef_id());
		proDef.setProDefPk(object.getPk_prodef());
		proDef.setName(object.getProdef_name());
		proDef.setDescription(object.getProdef_desc());
		proDef.setResourceBytes(object.getProcessxml());
		proDef.setDiagramBytes(object.getDiagramimg());
		proDef.setVersion(Integer.parseInt(object.getProdef_version()));
		if (object.getIspublic() != null) {
			proDef.setPublic(object.getIspublic().booleanValue());
		}
		proDef.setPk_group(object.getPk_group());
		proDef.setPk_bizobject(object.getPk_bizobject());
		proDef.setPk_biztrans(object.getPk_biztrans());
		proDef.setValidity(object.getValidity());
		return proDef;
	}
}
