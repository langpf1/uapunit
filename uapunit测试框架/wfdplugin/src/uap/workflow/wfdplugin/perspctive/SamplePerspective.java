package uap.workflow.wfdplugin.perspctive;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import uap.workflow.wfdplugin.view.ExplorerView;
/**
 * The activator class controls the plug-in life cycle
 */
public class SamplePerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorArea);
		left.addView(ExplorerView.class.getName());
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		
		layout.addActionSet("myplugin.actionSet");
	}
}
