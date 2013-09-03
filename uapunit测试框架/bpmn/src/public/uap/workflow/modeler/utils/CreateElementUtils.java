package uap.workflow.modeler.utils;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;

public class CreateElementUtils {
	
	private static int idSeed = 1; 
	
	public static int getIdSeed() {
		return idSeed;
	}

	public static void setIdSeed(int idSeed) {
		CreateElementUtils.idSeed = idSeed;
	}

	private Bpmn2MemoryModel model;
	public CreateElementUtils(Bpmn2MemoryModel model){
		this.model = model;
	}
	
	public CreateElementUtils(){
		
	}
	
	public String generateElementId(BaseElement baseElement){
		return "_" + String.valueOf(idSeed++); //GuidUtils.generate();
	}
	
	public String generateElementName(){
		return null;
	}
}
