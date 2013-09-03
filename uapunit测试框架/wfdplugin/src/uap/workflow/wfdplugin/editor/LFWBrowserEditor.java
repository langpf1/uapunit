/**
 * 
 */
package uap.workflow.wfdplugin.editor;

import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import uap.workflow.wfdplugin.MozillaBrowser;
import uap.workflow.wfdplugin.listener.EventEditorHandler;

/**
 * @author chouhl
 * 2011-11-17
 */
public abstract class LFWBrowserEditor extends LFWBaseEditor {
	
	private IEditorSite site;
	
	private IEditorInput input;
	
	private MozillaBrowser mozilla = null;
	
	public abstract MozillaBrowser createMozillaBrowser(Composite parent);
	
	public void execute(String script){
		mozilla.execute(script);
	}
	
	public String getSessionId(){
		return mozilla.getSessionId();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.site = site;
		this.input = input;
	}
	
	@Override
	public void doSaveAs() {
		setDirtyFalse();
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void setFocus() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		initializeGraphicalViewer();
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout());
		mozilla = createMozillaBrowser(comp);
		mozilla.createBrowser();
		setEventHandler(new EventEditorHandler());
		setDirtyFalse();
	}
	
	@Override
	protected void initializeGraphicalViewer() {
	}

/*
  	public LFWAbstractViewPage createViewPage() {
 		return new LFWBrowserViewPage();
	}
*/
	
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()){
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}
	
	@Override
	public IEditorInput getEditorInput() {
		return input;
	}
	@Override
	public IEditorSite getEditorSite() {
		return site;
	}

	public MozillaBrowser getMozilla() {
		return mozilla;
	}

	public void setMozilla(MozillaBrowser mozilla) {
		this.mozilla = mozilla;
	}

	public void setSite(IEditorSite site) {
		this.site = site;
	}
	
	public IEditorSite getSite() {
		return site;
	}

	public void setInput(IEditorInput input) {
		this.input = input;
	}
	
	public IEditorInput getInput() {
		return input;
	}

	/*
	public static LFWBrowserEditor getActiveEditor(){
		IWorkbenchPage page = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = null;
		if(page != null){
			editor = page.getActiveEditor();
		}
		if(editor != null && editor instanceof LFWBrowserEditor){
			return (LFWBrowserEditor)editor;
		}else {
			return null;
		}
	}
	 */	

}
