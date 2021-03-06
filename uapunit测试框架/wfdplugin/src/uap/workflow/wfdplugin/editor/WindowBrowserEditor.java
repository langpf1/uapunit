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
public class WindowBrowserEditor extends LFWBrowserEditor{
	
	public WindowBrowserEditor(){
		super();
		setEditDomain(new DefaultEditDomain(this));
		//getEditDomain().setDefaultTool(new PaletteFactory.CustomSelectionTool());
	}
	
	@Override
	public void setFocus() {
		super.setFocus();
		//LFWPersTool.hideView(LFWTool.ID_LFW_VIEW_SHEET);
	}
	
	public MozillaBrowser createMozillaBrowser(Composite parent){
		MozillaBrowser mozilla = new MozillaBrowser(parent, MozillaBrowser.WINDOW_URL);
		//mozilla.setTreeItem(((WindowEditorInput)getEditorInput()).getPmTreeItem());
		mozilla.setEditor(this);
		return mozilla;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		//LFWAMCConnector.savePageMetaAndUIMetaFromSessionCache(getSessionId(), getPageMetaId(), getNodePath());
		getMozilla().changeSaveStatus();
//		getMozilla().execute("saveForEclipse();");
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		/*
		LfwWindow pageMeta = ((WindowEditorInput)getEditorInput()).getPagemeta();
		String pageId = null;
		if(pageMeta.getComponentId()==null||pageMeta.getComponentId().equals("")||pageMeta.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
			pageId = pageMeta.getId();
		else pageId = pageMeta.getComponentId() + "." + pageMeta.getId();
		setPageMetaId(pageId);
		*/
	}

	/*
	public static WindowBrowserEditor getActiveEditor(){
		IEditorPart editor = LFWBrowserEditor.getActiveEditor();
		if(editor != null && editor instanceof WindowBrowserEditor){
			return (WindowBrowserEditor)editor;
		}else {
			return null;
		}
	}
	 */
	@Override
	protected void editMenuManager(IMenuManager manager) {
		// TODO Auto-generated method stub
		
	}
}
