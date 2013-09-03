package uap.workflow.reslet.application.receiveData;

import uap.workflow.engine.vos.ProcessDefinitionVO;

public class CreatProdefJsonData {

	public ProcessDefinitionVO[] creatprodefjson(ProcessDefinitionVO[] prodefarray) {
			for(int i = 0; i < prodefarray.length; i++ ){
			ProcessDefinitionVO prodef = new ProcessDefinitionVO();
			prodef.setPk_bizobject(prodefarray[i].getPk_bizobject());
			prodef.setPk_prodef(prodefarray[i].getPk_prodef());
			prodef.setProdef_id(prodefarray[i].getProdef_id());
			prodef.setProdef_name(prodefarray[i].getProdef_name());
			prodef.setProdef_version(prodefarray[i].getProdef_version());
			prodef.setStatus(prodefarray[i].getStatus());
//			prodef.setTime(prodefarray[i].getTs().toString());	
			prodefarray[i] = prodef;
		}
		return prodefarray;
	}

}
