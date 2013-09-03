package uap.workflow.modeler.editors;

import java.awt.Container;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.ui.pub.beans.UIDialog;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.engine.cfg.ExtensionPropertyConfig;
import uap.workflow.engine.itf.IExtensionConfig;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.uecomponent.BpmnPropertyEditingDlg;
import uap.workflow.modeler.uecomponent.ExtendEditorDlg;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class ExtendEditor extends AbstractDailogPropertyEditor {

	@Override
	protected void doButtonClick() {
	//	((ExtendEditorDlg)dlg).SetPropertys(getValue());
		dlg.showModal();
	}

	@Override
	protected UIDialog initializeDlg(Container parent) {
		String className=null;
		IExtensionConfig config = NCLocator.getInstance().lookup(IExtensionConfig.class);
		ExtensionPropertyConfig[] properties = config.getExtensionPropertyConfig();
		for(ExtensionPropertyConfig property : properties){
			if (property.getNotationType().equalsIgnoreCase(this.getOwnerObject().getClass().getSimpleName())){
				className=property.getClassImpl();
			}
		}
		try {
			Class<?> extendProperty= Class.forName(className);
			Class[]   types=new   Class[4]; 
			types[0]=Container.class;
			types[1]=PropertyEditorSupport.class;
			types[2]=Process.class;
			types[3]=BaseElement.class;
			Constructor   constructor   =   extendProperty.getConstructor(types); 
			BpmnGraphComponent graphComponent = BpmnGraphComponent.getGraphComponentObject();
			Process process = graphComponent.getMainProcess();
			return (UIDialog) (constructor.newInstance(parent,this,process,this.getOwnerObject()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}