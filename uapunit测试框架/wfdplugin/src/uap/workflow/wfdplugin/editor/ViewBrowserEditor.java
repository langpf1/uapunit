/**
 * 
 */
package uap.workflow.wfdplugin.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;

import uap.workflow.wfdplugin.MozillaBrowser;

/**
 * @author chouhl
 *
 */
public class ViewBrowserEditor extends LFWBrowserEditor {
	
	public ViewBrowserEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		//getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	public MozillaBrowser createMozillaBrowser(Composite parent){
		MozillaBrowser mozilla = new MozillaBrowser(parent, MozillaBrowser.VIEW_URL);
		//mozilla.setTreeItem(((WidgetEditorInput)getEditorInput()).getPmTreeItem());
		mozilla.setEditor(this);
		return mozilla;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		//LFWAMCConnector.updateViewSessionCache(getView(), getUimeta(), getElementMap());
		//LFWAMCConnector.saveWidgetAndUIMetaFromSessionCache(getSessionId(), getPageMetaId(), getWidgetId(), getNodePath());
		getMozilla().changeSaveStatus();
//		getMozilla().execute("saveForEclipse();");
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		/*
		setUimeta(LFWAMCPersTool.getCurrentUIMeta());
		setView((LfwView)((WidgetEditorInput)getEditorInput()).getWidget().clone());
		LfwWindow pageMeta = ((WidgetEditorInput)getEditorInput()).getPagemeta();
		String pageId = null;
		if(pageMeta.getComponentId()==null||pageMeta.getComponentId().equals("")||pageMeta.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
			pageId = pageMeta.getId();
		else pageId = pageMeta.getComponentId() + "." + pageMeta.getId();
		setPageMetaId(pageId);
		setWidgetId(((WidgetEditorInput)getEditorInput()).getWidget().getId());
		*/
	}
/*
	public static ViewBrowserEditor getActiveEditor(){
		IEditorPart editor = LFWBrowserEditor.getActiveEditor();
		if(editor instanceof ViewBrowserEditor){
			return (ViewBrowserEditor)editor;
		}else {
			return null;
		}
	}
*/	
	@Override
	protected void editMenuManager(IMenuManager manager) {
		
	}
}



