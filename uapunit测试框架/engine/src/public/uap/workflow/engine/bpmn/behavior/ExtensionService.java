package uap.workflow.engine.bpmn.behavior;

import java.util.List;

import uap.workflow.bpmn2.model.NameSpaceConst;
import uap.workflow.engine.bpmn.parser.FieldDeclaration;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.xml.Element;


public abstract class ExtensionService extends TaskActivityBehavior {
	protected IActivity activity = null;
	protected String type = null;
	protected String implementation = null;
	protected String className = null;
	protected String methodName = null;
	protected String resultVariable = null;
	//protected List<FieldDeclaration> fields;
	
	public void parseService(Element serviceTaskElement, IActivity activity/*,List<FieldDeclaration> fields*/) throws RuntimeException{
		this.activity = activity;
		parseExtensionService(serviceTaskElement);
		this.activity.setActivityBehavior(this);
		//this.fields = fields;
	}

	public void parseExtensionService(Element serviceTaskElement) throws RuntimeException{
		type = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "type");
		implementation = serviceTaskElement.attribute("implementation");
		className = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "class");
		methodName = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "method");
		resultVariable = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariable");
		if (resultVariable == null) {
			resultVariable = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariableName");
		}
	}
		
	public abstract void executeBehavior(IActivityInstance execution) throws Exception;
	
	@Override
	public void execute(IActivityInstance execution) throws Exception {
		executeBehavior(execution);
		super.execute(execution);
	}
}
