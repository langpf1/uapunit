package uap.workflow.wfdplugin.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import uap.workflow.wfdplugin.editor.ViewBrowserEditor;
import uap.workflow.wfdplugin.editor.ViewEditorInput;
import uap.workflow.wfdplugin.view.ExplorerView;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class WorkflowAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public WorkflowAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		openEditor();
	}

	public static void openEditor() {
		ExplorerView view = ExplorerView.getLFWExploerTreeView(null);
		//LfwView widget = LFWPersTool.getCurrentWidget();
		//LfwWindow pagemeta = LFWPersTool.getCurrentPageMeta();
//		if(widget == null || view == null){
//			throw new LfwPluginException("当前Widget对象为空或lfw浏览器视图对象为空!");
//		}
		ViewEditorInput editorInput = new ViewEditorInput();
		//editorInput.setPmTreeItem(LFWAMCPersTool.getCurrentPageMetaTreeItem());
		IWorkbenchPage workbenchPage = view.getViewSite().getPage();
		IEditorReference[] ers = workbenchPage.getEditorReferences();
		IEditorPart editorPart = null;
//		for (int i = 0; i < ers.length; i++) {
//			editorPart = ers[i].getEditor(true);
//			if(editorPart instanceof ViewBrowserEditor){
//				ViewBrowserEditor wbEditor = (ViewBrowserEditor)editorPart;
//				LfwView widgetOpened = ((WidgetEditorInput)wbEditor.getEditorInput()).getWidget();
//				if (widget.getId().equals(widgetOpened.getId())) {
//					LfwWindow pageMeta = widget.getPagemeta();
//					LfwWindow pageMetaOpened = widgetOpened.getPagemeta();
//					if(pageMeta != null && pageMetaOpened != null && pageMeta.getId().equals(pageMetaOpened.getId())){
////						break;
//					}
//				}
//			}
//			editorPart = null;
//		}
		if (editorPart != null)
			workbenchPage.bringToTop(editorPart);
		else {
			try {
				String editorid = "uap.workflow.wfdplugin.editor.ViewBrowserEditor";
				workbenchPage.openEditor(editorInput, editorid);
			} catch (PartInitException e) {
				//MainPlugin.getDefault().logError(e.getMessage(), e);
			}
		}
	}
	
	
	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}