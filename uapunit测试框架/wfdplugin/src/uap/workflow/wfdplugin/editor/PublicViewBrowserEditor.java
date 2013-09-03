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
public class PublicViewBrowserEditor extends LFWBrowserEditor {
	
	public PublicViewBrowserEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		//getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	@Override
	public MozillaBrowser createMozillaBrowser(Composite parent) {
		MozillaBrowser mozilla = new MozillaBrowser(parent, MozillaBrowser.PUBLIC_VIEW_URL);
		//mozilla.setTreeItem(((PublicViewEditorInput)getEditorInput()).getDirTreeItem());
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
		//setUimeta(LFWAMCPersTool.getCurrentUIMeta());
		//setView((LfwView)((PublicViewEditorInput)getEditorInput()).getWidget().clone());
		//setPageMetaId(WEBPersConstants.DEFAULT_WINDOW_ID);
		//setWidgetId(((PublicViewEditorInput)getEditorInput()).getWidget().getId());
	}

	protected void editMenuManager(IMenuManager manager) {
	}

	/*
	public static PublicViewBrowserEditor getActiveEditor(){
		IEditorPart editor = LFWBrowserEditor.getActiveEditor();
		if(editor != null && editor instanceof PublicViewBrowserEditor){
			return (PublicViewBrowserEditor)editor;
		}else {
			return null;
		}
	}
	 */
}
