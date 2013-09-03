package uap.workflow.wfdplugin.view;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.contextmenubar.ContextMenuEditorInput;
import nc.lfw.editor.datasets.core.DatasetsEditor;
import nc.lfw.editor.datasets.core.DatasetsEditorInput;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.MenubarEditorInput;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.button.ButtonEditor;
import nc.uap.lfw.button.ButtonEditorInput;
import nc.uap.lfw.chart.core.ChartEditor;
import nc.uap.lfw.chart.core.ChartEditorInput;
import nc.uap.lfw.combodata.core.ComboDataEditor;
import nc.uap.lfw.combodata.core.ComboDataEidtorInput;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.LFWAMCTreeItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.ContextMenuComp;
//import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.ImageButtonComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.data.RefDataset;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.page.config.RefNodeConf;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.core.refnode.SelfDefRefNode;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.editor.common.editor.LFWPVTreeItem;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
//import nc.uap.lfw.excel.core.ExcelEditor;
//import nc.uap.lfw.excel.core.ExcelEditorInput;
import nc.uap.lfw.form.core.FormEditor;
import nc.uap.lfw.form.core.FormEditorInput;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.grid.core.GridEditorInput;
import nc.uap.lfw.iframe.IFrameEditor;
import nc.uap.lfw.iframe.IFrameEditorInput;
import nc.uap.lfw.image.ImageEditor;
import nc.uap.lfw.image.ImageEditorInput;
import nc.uap.lfw.label.LabelEditor;
import nc.uap.lfw.label.LabelEditorInput;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.linkcomp.LinkCompEditor;
import nc.uap.lfw.linkcomp.LinkCompEditroInput;
import nc.uap.lfw.perspective.action.RefreshLFWExplorerTreeAction;
import nc.uap.lfw.perspective.editor.DataSetEitorInput;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWBusinessCompnentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWComboTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWContextMenuTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDSTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMdDirTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMenubarCompTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWMetadataTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWProjectTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefNodeTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWSeparateTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWVirtualDirTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;
import nc.uap.lfw.progressbar.ProgressBarEditor;
import nc.uap.lfw.progressbar.ProgressBarEditorInput;
import nc.uap.lfw.refnode.core.RefNodeEditor;
import nc.uap.lfw.refnode.core.RefNodeEditorInput;
import nc.uap.lfw.refnoderel.LFWRefNodeRelTreeItem;
import nc.uap.lfw.refnoderel.RefnoderelEditor;
import nc.uap.lfw.refnoderel.RefnoderelEditorInput;
import nc.uap.lfw.selfdefcomp.core.SelfDefCompEditor;
import nc.uap.lfw.selfdefcomp.core.SelfDefCompEditorInput;
import nc.uap.lfw.textcomp.TextCompEditor;
import nc.uap.lfw.textcomp.TextCompEditorInput;
import nc.uap.lfw.toolbar.ToolBarEditor;
import nc.uap.lfw.toolbar.ToolBarEditorInput;
import nc.uap.lfw.tree.core.TreeEditor;
import nc.uap.lfw.tree.core.TreeEditorInput;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * lfw浏览器视图
 * 
 * @author zhangxya
 * 
 */
public class LFWExplorerTreeView extends ViewPart {
	protected TreeViewer treeView = null;
	protected Map<String, String> editoridMap = new HashMap<String, String>();

	// 继承LFWExplorerTreeView的viewid
	public static String EXTEND_VIEWID;

	public void createPartControl(Composite parent) {
		ViewForm vf = new ViewForm(parent, SWT.NONE);
		vf.setLayout(new FillLayout());
		ToolBar tb = new ToolBar(vf, SWT.FLAT);

		RefreshLFWExplorerTreeAction refresh = new RefreshLFWExplorerTreeAction();
		ToolBarManager tbm = new ToolBarManager(tb);
		tbm.add(refresh);
		dealOthersToolBar(tbm);
		tbm.update(true);

		treeView = new TreeViewer(vf, SWT.SINGLE | SWT.H_SCROLL | SWT.H_SCROLL);
		Tree tree = treeView.getTree();
		LFWPersTool.setTree(tree);
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				Tree tree = treeView.getTree();
				TreeItem[] tis = tree.getSelection();
				if (tis == null || tis.length == 0)
					return;
				TreeItem ti = tis[0];
				manager.removeAll();
				if (ti instanceof LFWBasicTreeItem) {
					((LFWBasicTreeItem) ti).addMenuListener(manager);
				} else
					othersTreeItemMenuListener(ti, manager);
				/* 在portal浏览器中对已有Menu进行扩展 */
				treeItemMenuListenerExtends(ti, manager);
			}
		});
//		tree.add
		Menu menu = menuManager.createContextMenu(tree);
		tree.setMenu(menu);
		initTree();
		tree.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				Tree tree = treeView.getTree();
				TreeItem[] tis = tree.getSelection();
				if (tis == null || tis.length == 0)
					return;
				TreeItem ti = tis[0];
				if (ti instanceof LFWBasicTreeItem) {
					((LFWBasicTreeItem) ti).mouseDoubleClick();
				} else
					otherMouseDoubleClick(ti);
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseUp(e);
			}
		});
		tree.addListener(SWT.Expand, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				TreeItem root = (TreeItem)event.item;
				if (root instanceof LFWDirtoryTreeItem) {
					((LFWDirtoryTreeItem) root).expandTree();
				}
				if (root instanceof LFWPVTreeItem) {
					((LFWPVTreeItem) root).expandTree();
				}
			}
		});
		vf.setTopRight(tb);
		vf.setContent(treeView.getControl());
	}

	/**
	 * 处理其它鼠标双击事件
	 * 
	 * @param ti
	 */
	protected void otherMouseDoubleClick(TreeItem ti) {

	}

	/**
	 * 处理其它树节点右键菜单
	 * 
	 * @param ti
	 * @param manager
	 */
	protected void othersTreeItemMenuListener(TreeItem ti, IMenuManager manager) {

	}

	/**
	 * 在子类中扩展树节点的menuListener
	 * 
	 */
	protected void treeItemMenuListenerExtends(TreeItem ti, IMenuManager manager) {

	}

	/**
	 * 处理Explorer工具栏
	 * 
	 * @param tbm
	 */
	protected void dealOthersToolBar(ToolBarManager tbm) {

	}

	/**
	 * 更新公共数据集
	 * 
	 * @param tis
	 */
	public void refreshPubTreeItem(TreeItem[] tis) {
		TreeItem ti = tis[0];
		IProject project = LFWPersTool.getCurrentProject();
		LFWDirtoryTreeItem direcTreeItem = (LFWDirtoryTreeItem) ti;
		LFWProjectTreeItem projectTreeItem = (LFWProjectTreeItem) direcTreeItem
				.getParentItem();
		File file = projectTreeItem.getFile();
		// 公共参照
//		if (direcTreeItem.getType().equals(
//				LFWDirtoryTreeItem.PARENT_PUB_REF_FOLDER)) {
//			LFWExplorerTreeBuilder lfwExplorer = LFWExplorerTreeBuilder
//					.getInstance();
//			File dsRefNodeFile = new File(file, WEBProjConstants.PUBLIC_REFNODE);
//			lfwExplorer.initRefNodeSubTree(direcTreeItem, dsRefNodeFile,
//					project.getLocation().toString());
//		} else if (direcTreeItem.getType().equals(
//				LFWDirtoryTreeItem.PARENT_PUB_DS_FOLDER)) {
//			LFWExplorerTreeBuilder lfwExplorer = LFWExplorerTreeBuilder
//					.getInstance();
//			File dsFile = new File(file, WEBProjConstants.PUBLIC_DATASET);
//			lfwExplorer.initDsSubTree(direcTreeItem, dsFile, project
//					.getLocation().toString());
//		}
	}

	/**
	 * 刷新节点文件夹下内容
	 * 
	 * @param tis
	 */
	public void refreshDirtoryTreeItem() {
		IProject project = LFWPersTool.getCurrentProject();
		LFWPageMetaTreeItem ti = LFWPersTool.getCurrentPageMetaTreeItem();
		if(ti == null){
			ti = LFWAMCPersTool.getDefaultWindowTreeItem(null);
		}
		TreeItem[] treeItems = ti.getItems();
		for (int i = 0; i < treeItems.length; i++) {
			TreeItem treeItem = treeItems[i];
			treeItem.dispose();
		}
//		ti.setPm(null);
		// 加载子节点
		LFWExplorerTreeBuilder.getInstance().initSubNodeTree(ti,
				LFWPersTool.getCurrentProject());
		File file = ti.getFile();

		
		
		// 解析pageMeta
		LfwWindow pagemeta = ti.getPm();
		if (pagemeta == null)
			return;
		ti.setPm(pagemeta);
		ViewConfig[] widgetlist = pagemeta.getViewConfigs();
		if (widgetlist.length > 0) {
			String view = "[" + LFWTool.getViewText(ti) + "] "; //$NON-NLS-1$ //$NON-NLS-2$
			for (int i = 0; i < widgetlist.length; i++) {
				LfwView widget = pagemeta.getWidget(widgetlist[i].getId());
				File widgetfile = new File(file.getPath() + "/" //$NON-NLS-1$
						+ widget.getId());
				LFWWidgetTreeItem widgetTreeItem = new LFWWidgetTreeItem(ti, widgetfile, widget,
						view + widgetfile.getName());
				// TreeItem[] widgetTreeItems = new TreeItem[1];
				// widgetTreeItems[0] = widgetTreeItem;
				widgetTreeItem.setId(widget.getId());
				refreshWidgetTreeItem(widgetTreeItem);
			}
		}
		//加载控制节点
		String controlName = pagemeta.getControllerClazz();
		if(controlName!=null&&!"".equals(controlName)){ //$NON-NLS-1$
			File filectrl = ((LFWBasicTreeItem)ti).getFile();
			File controlFile = new File(filectrl,pagemeta.getSourcePackage());
			LFWDirtoryTreeItem ctrlNode = new LFWDirtoryTreeItem(ti,controlFile,M_perspective.LFWExplorerTreeView_2); 
			ctrlNode.setType(LFWDirtoryTreeItem.METADATA_FOLDER);
			controlName = controlName.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
			IFile appControl = project.getFile(pagemeta.getSourcePackage()+controlName+".java"); //$NON-NLS-1$
			LFWMetadataTreeItem metadata = new LFWMetadataTreeItem(ctrlNode,appControl,appControl.getName());	
			metadata.setMdFile(appControl);
			LFWDirtoryTreeItem mdNode = (LFWDirtoryTreeItem) metadata;
			mdNode.setType(LFWDirtoryTreeItem.METADATA);	
		}
	}
	
	/**
	 * 刷新节点文件夹下内容
	 * 
	 * @param tis
	 */
	public void refreshDirtoryTreeItem(LFWPageMetaTreeItem ti) {
		if(ti == null){
			ti = LFWPersTool.getCurrentPageMetaTreeItem();
		}
		if(ti == null){
			ti = LFWAMCPersTool.getDefaultWindowTreeItem(null);
		}
		TreeItem[] treeItems = ti.getItems();
		for (int i = 0; i < treeItems.length; i++) {
			TreeItem treeItem = treeItems[i];
			treeItem.dispose();
		}
		ti.setPm(null);
		// 加载子节点
		LFWExplorerTreeBuilder.getInstance().initSubNodeTree(ti,
				LFWPersTool.getCurrentProject());
		File file = ti.getFile();

		// 解析pageMeta
		LfwWindow pagemeta = ti.getPm();
		if (pagemeta == null)
			return;
		ti.setPm(pagemeta);
		ViewConfig[] widgetlist = pagemeta.getViewConfigs();
		if (widgetlist.length > 0) {
			String view = "[" + LFWTool.getViewText(ti) + "] "; //$NON-NLS-1$ //$NON-NLS-2$
			for (int i = 0; i < widgetlist.length; i++) {
				LfwView widget = pagemeta.getWidget(widgetlist[i].getId());
				File widgetfile = new File(file.getPath() + "/" //$NON-NLS-1$
						+ widget.getId());
				LFWWidgetTreeItem widgetTreeItem = null;
				widgetTreeItem = new LFWWidgetTreeItem(ti, widgetfile, widget,
						view + widgetfile.getName());
				// TreeItem[] widgetTreeItems = new TreeItem[1];
				// widgetTreeItems[0] = widgetTreeItem;
				refreshWidgetTreeItem(widgetTreeItem);
			}
		}

		// containers
//		ViewContainers viewContainers = pagemeta.getViewConinters();
//		Map<String, WebComponent> viewContainerMap = viewContainers
//				.getContainerMap();
//		if (viewContainerMap.size() > 0) {
//			LFWSeparateTreeItem containerD = createFolder(ti, file,
//					WEBProjConstants.CONTAINER);
//			for (Iterator<String> itcm = viewContainerMap.keySet().iterator(); itcm
//					.hasNext();) {
//				WebComponent webCompnent = viewContainerMap.get(itcm.next());
//				if (webCompnent instanceof TabLayout) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD, LFWWebComponentTreeItem.TAB_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_TAB);
//				} else if (webCompnent instanceof CardLayout) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD,
//							LFWWebComponentTreeItem.CARDLAYOUT_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_CARDLAYOUT);
//				} else if (webCompnent instanceof PanelLayout) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD,
//							LFWWebComponentTreeItem.PANELLAYOUT_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_PANELLAYOUT);
//				} else if (webCompnent instanceof OutlookbarComp) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD,
//							LFWWebComponentTreeItem.OUTLOOKBAR_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_OUTLOOKBAR);
//				}
//			}
//		}
	}
	/**
	 * 刷新app文件夹下内容
	 * 
	 * @param tis
	 */
	public void refreshAppItem() {
		IProject project = LFWPersTool.getCurrentProject();
		TreeItem ti = LFWPersTool.getCurrentTreeItem();
		Application app = LFWAMCPersTool.getCurrentApplication();
		String controlName = app.getControllerClazz();
		if(controlName!=null&&!"".equals(controlName)){ //$NON-NLS-1$
			File file = ((LFWBasicTreeItem)ti).getFile();
			File applicationFile = new File(file,app.getSourcePackage());
			LFWDirtoryTreeItem ctrlNode = new LFWDirtoryTreeItem(ti,applicationFile,M_perspective.LFWExplorerTreeView_2); 
			ctrlNode.setType(LFWDirtoryTreeItem.METADATA_FOLDER);
			controlName = controlName.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
			IFile appControl = project.getFile(app.getSourcePackage()+controlName+".java"); //$NON-NLS-1$
			LFWMetadataTreeItem metadata = new LFWMetadataTreeItem(ctrlNode,appControl,appControl.getName());	
			metadata.setMdFile(appControl);
			LFWDirtoryTreeItem mdNode = (LFWDirtoryTreeItem) metadata;
			mdNode.setType(LFWDirtoryTreeItem.METADATA);	
		}
		if(app.getEventConfList().size()>0){
			LFWDirtoryTreeItem eventNode = new LFWDirtoryTreeItem(ti,null,M_perspective.LFWExplorerTreeView_3); 
			eventNode.setType(LFWDirtoryTreeItem.METADATA_FOLDER);
			for(EventConf event:app.getEventConfList()){
				String eControlName = event.getControllerClazz();
				if(eControlName!=null&&!"".equals(eControlName)){ //$NON-NLS-1$
					eControlName = eControlName.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
					IFile appControl = project.getFile(app.getSourcePackage()+eControlName+".java"); //$NON-NLS-1$
					LFWMetadataTreeItem metadata = new LFWMetadataTreeItem(eventNode,appControl,appControl.getName());	
					metadata.setMdFile(appControl);
					LFWDirtoryTreeItem mdNode = (LFWDirtoryTreeItem) metadata;
					mdNode.setType(LFWDirtoryTreeItem.METADATA);	
				}
			}
		}
		if(app.getWindowList().size()>0){
			Map<String,String> map = new HashMap<String, String>();
			for(LfwWindow pm:app.getWindowList()){
				String id = pm.getId();
				String name = pm.getCaption();				
				map.put(id, name);
			}
			String path = LFWAMCPersTool.getProjectPath()+"/web/html/nodes";
			File dir = new File(path);
			LFWDirtoryTreeItem windowNode = new LFWDirtoryTreeItem(ti,
					dir, WEBPersConstants.WINDOW);
			windowNode.setType(LFWDirtoryTreeItem.WINDOW_FOLDER);
			BusinessComponent businessC = new BusinessComponent();
			String bcpPath = LFWAMCPersTool.getProjectPath();
			String bcpName = bcpPath.substring(bcpPath.lastIndexOf("/")+1,bcpPath.length());
			businessC.setName(bcpName);
			windowNode.setBcp(businessC);
//			LFWExplorerTreeBuilder builder = LFWExplorerTreeBuilder.getInstance();
//			for(File subFile:dir.listFiles()){
//				if(map.containsKey(subFile.getName())){
//					builder.scanWindowDir(windowNode,subFile,map,ILFWTreeNode.WINDOW,WEBPersConstants.AMC_WINDOW_FILENAME,project);
//				}		
//			}	
			scanSubDir(map,dir,windowNode);
			
		}
		
	}
	public void scanSubDir(Map map,File folder,LFWDirtoryTreeItem windowNode){
		for(File subFile:folder.listFiles()){
			if(subFile.isFile()) continue;
			if(map.containsKey(subFile.getName())){
				LFWExplorerTreeBuilder builder = LFWExplorerTreeBuilder.getInstance();
				IProject project = LFWPersTool.getCurrentProject();
				builder.scanWindowDir(windowNode,subFile,map,ILFWTreeNode.WINDOW,WEBPersConstants.AMC_WINDOW_FILENAME,project);
			}
			else{
				scanSubDir(map, subFile, windowNode);
			}
		}
	}
	

	/**
	 * 加载菜单管理器项
	 * 
	 * @param ti
	 * @param file
	 * @param pagemeta
	 */
//	public void initMenusManagerItem(LFWDirtoryTreeItem ti, File file,
//			LfwWindow pagemeta) {
//		// 所有Menu项
//		ViewMenus viewMenus = pagemeta.getViewMenus();
//		LFWSeparateTreeItem menusItem = createFolder(ti, file,
//				WEBPersConstants.MENUS_MANAGER_CN);
//		// Map<String, MenubarComp> menuBars = viewMenus.getMenuBars();
//		MenubarComp[] menuBars = viewMenus.getMenuBars();
//		for (int i = 0; i < menuBars.length; i++) {
//			MenubarComp menubar = menuBars[i];
//			new LFWMenubarCompTreeItem(menusItem, menubar);
//		}
//	};

	/**
	 * 刷新菜单管理器项，并返回新增项
	 * 
	 * @param treeView
	 * @param projectPath
	 * @param pagemeta
	 * @param id
	 *            新增的菜单项ID
	 * @return 返回新增的菜单项
	 */
	public LFWMenubarCompTreeItem refreshMenusManagerItem(TreeViewer treeView,
			String projectPath, LfwWindow pagemeta, String id) {
		Tree tree = treeView.getTree();
		TreeItem[] tis = tree.getSelection();
		if (tis == null || tis.length == 0)
			return null;
		LFWSeparateTreeItem menusItem = (LFWSeparateTreeItem) tis[0];
		menusItem.removeAll();

		// 所有Menu项
//		ViewMenus viewMenus = pagemeta.getViewMenus();
		
		// 待返回的新增菜单项
		LFWMenubarCompTreeItem newItem = null;
//		MenubarComp[] menuBars = viewMenus.getMenuBars();
//		for (int i = 0; i < menuBars.length; i++) {
//			MenubarComp menubar = menuBars[i];
//			LFWMenubarCompTreeItem menubarTreeItem = new LFWMenubarCompTreeItem(
//					menusItem, menubar);
//			// menubarTreeItem.setPagemeta(pagemeta);
//			// menubarTreeItem.setProjectPath(projectPath);
//			if (null != id && id.equals(menubar.getId())) {
//				newItem = menubarTreeItem;
//			}
//		}
		return newItem;

	}

	public void refreshPubWidgetTreeItem(TreeItem ti) {
		LFWWidgetTreeItem widgetTreeItem = (LFWWidgetTreeItem) ti;
		File file = ((LFWWidgetTreeItem) ti).getFile();
		LfwView widget = widgetTreeItem.getWidget();
		detalWidgetTreeItem(ti, file, widget);
	}

	/**
	 * 刷新Widget下内容
	 * 
	 * @param tis
	 */
	public void refreshWidgetTreeItem(TreeItem ti) {
		TreeItem treeItem = ti;
		File file = ((LFWWidgetTreeItem) treeItem).getFile();
		String pageIdKey = ""; //$NON-NLS-1$
		while (treeItem.getParentItem() != null
				&& (treeItem instanceof LFWWidgetTreeItem || treeItem instanceof LFWDirtoryTreeItem)) {
			LFWDirtoryTreeItem parent = (LFWDirtoryTreeItem) treeItem
					.getParentItem();
			if ((parent.getText().equals(WEBPersConstants.NODEGROUP_CN)
					|| parent.getText().equals(WEBPersConstants.PUBLIC_REFNODE) || parent
					.getText().equals(WEBPersConstants.WINDOW)))
				break;
			pageIdKey = parent.getFile().getName() + "/" + pageIdKey; //$NON-NLS-1$
			treeItem = parent;

		}
		// ti = tis[0];
		if (pageIdKey.endsWith("/")) //$NON-NLS-1$
			pageIdKey = pageIdKey.substring(0, pageIdKey.length() - 1);

		String filePath = LFWPersTool.getProjectPath();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(WebConstant.PROJECT_PATH_KEY, filePath);
		paramMap.put(WebConstant.PAGE_ID_KEY, pageIdKey);
		String widgetId = file.getName();

		LfwWindow pm = LFWPersTool.getCurrentPageMeta();
		LfwView widget = pm.getWidget(widgetId);
		if(widget == null){
			widget = ((LFWWidgetTreeItem) ti).getWidget();
			pm.addWidget(widget);
		}
		// // 解析pageMeta
		// LfwWidget widget = dataProviderForDesign.getWidget(
		// paramMap, widgetId);
		((LFWWidgetTreeItem) ti).setWidget(widget);
		// 设置widget
		LFWPersTool.setLfwWidget(widget);
		if(widget.getRefId() != null && widget.getRefId().startsWith("..")){ //$NON-NLS-1$
			((LFWWidgetTreeItem) ti).setType(ILFWTreeNode.POOLREFNODEFOLDER);
			return;
		}else if(widget.getRefId() == null){
			((LFWWidgetTreeItem) ti).setType(ILFWTreeNode.POOLREFNODEFOLDER);
			return;
		}
		// viewModels
		detalWidgetTreeItem(ti, file, widget);
	}

	/**
	 * 更新widgetTreeItem
	 * 
	 * @param widgetTreeItem
	 */
	public void dealCurrentWidgetTreeItem(LFWWidgetTreeItem widgetTreeItem) {
		widgetTreeItem.removeAll();
		// widgetTreeItem.dispose();
		File file = widgetTreeItem.getFile();
		detalWidgetTreeItem(widgetTreeItem, file, widgetTreeItem.getWidget());
	}

	public void detalWidgetTreeItem(TreeItem ti, File file, LfwView widget) {
		ViewModels viewModels = widget.getViewModels();
		TreeItem parent = ti;
		LFWSeparateTreeItem contextD = createFolder(ti, file,
				WEBPersConstants.CONTEXT_EN);
		parent = contextD;

		LFWSeparateTreeItem datasetD = createFolder(parent, file, LFWTool
				.getWEBProjConstantValue("DATASET")); //$NON-NLS-1$
		// Map<String, Dataset> dsMap = viewModels.getDatasetMap();
		Dataset[] datasets = viewModels.getDatasets();

		// 设置widget中ds到公共池中
		LFWPersTool.setRefdatasets(datasets);
		for (int i = 0; i < datasets.length; i++) {
			for (int j = datasets.length - 1; j > i; j--) {
				if (datasets[i] instanceof IRefDataset
						&& !(datasets[j] instanceof IRefDataset)) {
					Dataset temp = datasets[i];
					datasets[i] = datasets[j];
					datasets[j] = temp;
				}
			}
		}

		for (int i = 0; i < datasets.length; i++) {
			new LFWDSTreeItem(datasetD, datasets[i], datasets[i].getId());
		}

		// //refnode
		LFWSeparateTreeItem refnodeD = createFolder(parent, file, LFWTool
				.getWEBProjConstantValue("REFNODE")); //$NON-NLS-1$
		IRefNode[] refnodes = viewModels.getRefNodes();

		// 设置refnodes
		if(refnodes!=null){
			LFWPersTool.setRefnodes(refnodes);
			for (int i = 0; i < refnodes.length; i++) {
				IRefNode refnode = refnodes[i];
				new LFWRefNodeTreeItem(refnodeD, refnode, LFWTool
						.getWEBProjConstantValue("COMPONENT_REFNODE")); //$NON-NLS-1$
			}
		}

		// refnodeRelation
		RefNodeRelations refnodeRelations = widget.getViewModels()
				.getRefNodeRelations();
		if (refnodeRelations != null) {
			Map<String, RefNodeRelation> refnodeRels = refnodeRelations
					.getRefnodeRelations();
			RefNodeRelation[] rels = refnodeRels.values().toArray(
					new RefNodeRelation[0]);
			for (int i = 0; i < rels.length; i++) {
				RefNodeRelation refnodeRel = rels[i];
				new LFWRefNodeRelTreeItem(refnodeD, refnodeRel, LFWTool
						.getWEBProjConstantValue("COMPONENT_REFNODERELATION")); //$NON-NLS-1$
			}
		}
		// //combodata
		LFWSeparateTreeItem combodateD = createFolder(parent, file, LFWTool
				.getWEBProjConstantValue("COMBODATA")); //$NON-NLS-1$
		ComboData[] combodata = viewModels.getComboDatas();
		// 设置widget中combodata
		LFWPersTool.setRefcomdatas(combodata);
		for (int i = 0; i < combodata.length; i++) {
			ComboData compdata = combodata[i];
			new LFWComboTreeItem(combodateD, compdata, LFWTool
					.getWEBProjConstantValue("COMPONENT_COMBODATA")); //$NON-NLS-1$
		}

		// containers
//		ViewContainers viewContainers = widget.getViewConinters();
//		Map<String, WebComponent> viewContainerMap = viewContainers
//				.getContainerMap();
//		if (viewContainerMap.size() > 0) {
//			LFWSeparateTreeItem containerD = createFolder(ti, file, LFWTool
//					.getWEBProjConstantValue("CONTAINER",
//							((LFWBasicTreeItem) ti).getLfwVersion()));
//			for (Iterator<String> itcm = viewContainerMap.keySet().iterator(); itcm
//					.hasNext();) {
//				WebComponent webCompnent = viewContainerMap.get(itcm.next());
//				if (webCompnent instanceof TabLayout) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD, LFWWebComponentTreeItem.TAB_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_TAB);
//				} else if (webCompnent instanceof CardLayout) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD,
//							LFWWebComponentTreeItem.CARDLAYOUT_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_CARDLAYOUT);
//				} else if (webCompnent instanceof PanelLayout) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD,
//							LFWWebComponentTreeItem.PANELLAYOUT_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_PANELLAYOUT);
//				} else if (webCompnent instanceof OutlookbarComp) {
//					LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//							containerD,
//							LFWWebComponentTreeItem.OUTLOOKBAR_NAME,
//							webCompnent);
//					newComp.setType(LFWWebComponentTreeItem.PARAM_OUTLOOKBAR);
//				}
//			}
//		}
		//component组件
		LFWSeparateTreeItem componentD = createFolder(ti, file, LFWTool
				.getWEBProjConstantValue("COMPONENTS")); //$NON-NLS-1$
		WebComponent[] grids = widget.getViewComponents().getComponentByType(
				GridComp.class);
		for (int i = 0; i < grids.length; i++) {
			geneWebCompTreeItem(componentD, grids[i]);
		}
		WebComponent[] forms = widget.getViewComponents().getComponentByType(
				FormComp.class);
		for (int i = 0; i < forms.length; i++) {
			geneWebCompTreeItem(componentD, forms[i]);
		}
		WebComponent[] buttons = widget.getViewComponents().getComponentByType(
				ButtonComp.class);
		for (int i = 0; i < buttons.length; i++) {
			geneWebCompTreeItem(componentD, buttons[i]);
		}

		WebComponent[] menubarComps = widget.getViewComponents()
				.getComponentByType(MenubarComp.class);
		for (int i = 0; i < menubarComps.length; i++) {
			geneWebCompTreeItem(componentD, menubarComps[i]);
		}
		WebComponent[] trees = widget.getViewComponents().getComponentByType(
				TreeViewComp.class);
		for (int i = 0; i < trees.length; i++) {
			geneWebCompTreeItem(componentD, trees[i]);
		}
		WebComponent[] images = widget.getViewComponents().getComponentByType(
				ImageComp.class);
		for (int i = 0; i < images.length; i++) {
			geneWebCompTreeItem(componentD, images[i]);
		}
		WebComponent[] labels = widget.getViewComponents().getComponentByType(
				LabelComp.class);
		for (int i = 0; i < labels.length; i++) {
			geneWebCompTreeItem(componentD, labels[i]);
		}
		WebComponent[] textcomps = widget.getViewComponents()
				.getComponentByType(TextComp.class);
		for (int i = 0; i < textcomps.length; i++) {
			geneWebCompTreeItem(componentD, textcomps[i]);
		}

//		WebComponent[] excelcomps = widget.getViewComponents()
//				.getComponentByType(ExcelComp.class);
//		for (int i = 0; i < excelcomps.length; i++) {
//			geneWebCompTreeItem(componentD, excelcomps[i]);
//		}
		// WebComponent[] textareas =
		// widget.getViewComponents().getComponentByType(TextAreaComp.class);
		// for (int i = 0; i < textareas.length; i++) {
		// geneWebCompTreeItem(componentD, textareas[i]);
		// }
		WebComponent[] iframes = widget.getViewComponents().getComponentByType(
				IFrameComp.class);
		for (int i = 0; i < iframes.length; i++) {
			geneWebCompTreeItem(componentD, iframes[i]);
		}
		WebComponent[] toolbars = widget.getViewComponents()
				.getComponentByType(ToolBarComp.class);
		for (int i = 0; i < toolbars.length; i++) {
			geneWebCompTreeItem(componentD, toolbars[i]);
		}
		WebComponent[] links = widget.getViewComponents().getComponentByType(
				LinkComp.class);
		for (int i = 0; i < links.length; i++) {
			geneWebCompTreeItem(componentD, links[i]);
		}

		WebComponent[] progressBars = widget.getViewComponents()
				.getComponentByType(ProgressBarComp.class);
		for (int i = 0; i < progressBars.length; i++) {
			geneWebCompTreeItem(componentD, progressBars[i]);
		}

		WebComponent[] chartcomps = widget.getViewComponents()
				.getComponentByType(ChartComp.class);
		for (int i = 0; i < chartcomps.length; i++) {
			geneWebCompTreeItem(componentD, chartcomps[i]);
		}

		WebComponent[] selfdefComps = widget.getViewComponents()
				.getComponentByType(SelfDefComp.class);
		for (int i = 0; i < selfdefComps.length; i++) {
			geneWebCompTreeItem(componentD, selfdefComps[i]);
		}

		ViewMenus viewMenus = widget.getViewMenus();
		// Map<String, MenubarComp> menuBars = viewMenus.getMenuBars();
		ContextMenuComp[] contextMenuss = viewMenus.getContextMenus();
		for (int i = 0; i < contextMenuss.length; i++) {
			ContextMenuComp contextMenu = contextMenuss[i];
			new LFWContextMenuTreeItem(
					componentD,
					LFWTool.getWEBProjConstantValue("CONTEXT_MENUCOMP_ELEMENT"), //$NON-NLS-1$
					contextMenu);
		}
		MenubarComp[] menubars = viewMenus.getMenuBars();
		for (int i = 0; i < menubars.length; i++) {
			MenubarComp menubar = menubars[i];
			new LFWMenubarCompTreeItem(
					componentD, 
					LFWTool.getWEBProjConstantValue("COMPONENT_MENUBAR"), //$NON-NLS-1$
					menubar);
		}
		
		//加载控制类
		LFWSeparateTreeItem controllNode = createFolder(ti, file,
				WEBPersConstants.CTRL_EN);
		String controlName = widget.getControllerClazz();
		if(controlName!=null && !"".equals(controlName)){ //$NON-NLS-1$
			IProject project = LFWPersTool.getCurrentProject();
			controlName = controlName.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
			IFile appControl = project.getFile(widget.getSourcePackage()+controlName+".java"); //$NON-NLS-1$
			LFWMetadataTreeItem metadata = new LFWMetadataTreeItem(controllNode,file,appControl.getName());	
			metadata.setMdFile(appControl);
			LFWDirtoryTreeItem mdNode = (LFWDirtoryTreeItem) metadata;
			mdNode.setType(LFWDirtoryTreeItem.METADATA);	
		}
		
	}

	private void geneWebCompTreeItem(LFWSeparateTreeItem componentD,
			WebComponent webCompnent) {
		if (webCompnent instanceof GridComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_GRID"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_GRID);
//		} else if (webCompnent instanceof ExcelComp) {
//			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
//					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_EXCEL"), webCompnent);
//			newComp.setType(LFWWebComponentTreeItem.PARAM_EXCEL);
		} else if (webCompnent instanceof FormComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_FORM"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_FORM);
		} else if (webCompnent instanceof ButtonComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_BUTTON"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_BUTTON);
		} else if (webCompnent instanceof MenubarComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_MENUBAR"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_MENUBAR);
		} else if (webCompnent instanceof TreeViewComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_TREE"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_TREE);
		} else if (webCompnent instanceof ImageComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_IMAGE"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_IMAGE);
		} else if (webCompnent instanceof LabelComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_LABEL"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_LABEL);
		} else if (webCompnent instanceof TextComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_TEXTCOMP"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_TEXTCOMP);
		}
		// else if(webCompnent instanceof TextAreaComp){
		// LFWWebComponentTreeItem newComp = new
		// LFWWebComponentTreeItem(componentD, COMPONENT_TEXTAREA ,webCompnent);
		// newComp.setType(LFWWebComponentTreeItem.PARAM_TEXTAREA);
		// }
		else if (webCompnent instanceof IFrameComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_IFRAMECOMP"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_IFRAME);
		} else if (webCompnent instanceof ToolBarComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_TOOLBAR"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_TOOLBAR);
		} else if (webCompnent instanceof LinkComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_LINKCOMP"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_LINK);
		} else if (webCompnent instanceof ProgressBarComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_PROGRESSBAR"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_PROGRESSBAR);
		} else if (webCompnent instanceof SelfDefComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_SELFDEFCOMP"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_SELFDEF);
		} else if (webCompnent instanceof ChartComp) {
			LFWWebComponentTreeItem newComp = new LFWWebComponentTreeItem(
					componentD, LFWTool.getWEBProjConstantValue("COMPONENT_CHART"), webCompnent); //$NON-NLS-1$
			newComp.setType(LFWWebComponentTreeItem.PARAM_CHART);
		} else {
			new LFWWebComponentTreeItem(componentD, "", webCompnent); //$NON-NLS-1$
		}
	}

	/**
	 * 打开ds编辑器
	 * 
	 * @param ds
	 */
	public void openDsEdit(LFWDSTreeItem ds) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(ds);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pageMetaTreeItem = LFWPersTool
				.getPagemetaTreeItem(ds);
		LfwWindow pagemeta = null;
		if (pageMetaTreeItem != null)
			pagemeta = pageMetaTreeItem.getPm();
		Dataset dsdata = (Dataset) ds.getData();
		DataSetEitorInput datasetEditorInput = new DataSetEitorInput(dsdata,
				lfwwidget, pagemeta);
		ds.setEditorInput(datasetEditorInput);
		String editorid = "nc.uap.lfw.perspective.editor.DataSetEditor"; //$NON-NLS-1$
		if (datasetEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorinputid = null;
		if (pagemeta == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "dataset" //$NON-NLS-1$
					+ dsdata.getId());
		else
			editorinputid = editoridMap.get(pagemeta.getId()
					+ lfwwidget.getId() + "dataset" + dsdata.getId()); //$NON-NLS-1$
		if (LFWPersTool.isBCPProject(LFWPersTool.getCurrentProject())) {
			LFWBusinessCompnentTreeItem busiComponent = LFWPersTool
					.getBusiCompTreeItem(ds);
			if (pagemeta == null)
				editorinputid = editoridMap.get(busiComponent.getText()
						+ lfwwidget.getId() + "dataset" + dsdata.getId()); //$NON-NLS-1$
			else
				editorinputid = editoridMap.get(busiComponent.getText()
						+ pagemeta.getId() + lfwwidget.getId() + "dataset" //$NON-NLS-1$
						+ dsdata.getId());
		}
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof DataSetEitorInput) {
					DataSetEitorInput dsEditorInput = (DataSetEitorInput) editorInput;
					Dataset dsnew = (Dataset) dsEditorInput.getCloneElement();
					LfwWindow pmnew = dsEditorInput.getPagemeta();
					LfwView widgetnew = dsEditorInput.getWidget();
					if (pmnew == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& dsnew.getId().equals(dsdata.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pagemeta.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& dsnew.getId().equals(dsdata.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(datasetEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (LFWPersTool.isBCPProject(LFWPersTool.getCurrentProject())) {
				LFWBusinessCompnentTreeItem busiComponent = LFWPersTool
						.getBusiCompTreeItem(ds);
				if (pagemeta == null)
					editoridMap.put(busiComponent.getText() + lfwwidget.getId()
							+ "dataset" + dsdata.getId(), busiComponent //$NON-NLS-1$
							.getText()
							+ lfwwidget.getId() + "dataset" + dsdata.getId()); //$NON-NLS-1$

				else
					editoridMap.put(busiComponent.getText() + pagemeta.getId()
							+ lfwwidget.getId() + "dataset" + dsdata.getId(), //$NON-NLS-1$
							busiComponent.getText() + pagemeta.getId()
									+ lfwwidget.getId() + "dataset" //$NON-NLS-1$
									+ dsdata.getId());
			} else {
				if (pagemeta == null)
					editoridMap.put(lfwwidget.getId() + "dataset" //$NON-NLS-1$
							+ dsdata.getId(), lfwwidget.getId() + "dataset" //$NON-NLS-1$
							+ dsdata.getId());
				else
					editoridMap.put(pagemeta.getId() + lfwwidget.getId()
							+ "dataset" + dsdata.getId(), pagemeta.getId() //$NON-NLS-1$
							+ lfwwidget.getId() + "dataset" + dsdata.getId()); //$NON-NLS-1$
			}
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(datasetEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开poolds
	 * 
	 * @param ds
	 */
	public void openPoolDsEdit(LFWDSTreeItem ds) {
		Dataset dsdata = (Dataset) ds.getData();
		DataSetEitorInput datasetEditorInput = new DataSetEitorInput(dsdata,
				null, null);
		String editorid = "nc.uap.lfw.perspective.editor.DataSetEditor"; //$NON-NLS-1$
		if (datasetEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorinputid = editoridMap.get("pooldataset" + dsdata.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof DataSetEitorInput) {
					DataSetEitorInput dsEditorInput = (DataSetEitorInput) editorInput;
					Dataset dsnew = (Dataset) dsEditorInput.getCloneElement();
					if (dsnew.getId().equals(dsdata.getId())) {
						editor = parts[i];
						break;
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(datasetEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			editoridMap.put("pooldataset" + dsdata.getId(), "pooldataset" //$NON-NLS-1$ //$NON-NLS-2$
					+ dsdata.getId());
			try {
				workbenchPage.openEditor(datasetEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开grid编辑器
	 * 
	 * @param gridComponent
	 */
	public void openGridEditor(LFWWebComponentTreeItem gridComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(gridComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pageMetaTreeItem = LFWPersTool
				.getPagemetaTreeItem(gridComponent);
		LfwWindow pagemeta = null;
		if (pageMetaTreeItem != null)
			pagemeta = pageMetaTreeItem.getPm();
		GridComp grid = (GridComp) gridComponent.getData();
		GridEditorInput gridEditorInput = new GridEditorInput(grid, lfwwidget,
				pagemeta);
		gridComponent.setEditorInput(gridEditorInput);
		String editorid = GridEditor.class.getName();
		if (gridEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		GridComp gridcomp = (GridComp) gridComponent.getData();
		String editorinputid = null;
		if (pagemeta == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "grid" //$NON-NLS-1$
					+ gridcomp.getId());
		else
			editorinputid = editoridMap.get(pagemeta.getId()
					+ lfwwidget.getId() + "grid" + gridcomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof GridEditorInput) {
					GridEditorInput gEditorInput = (GridEditorInput) editorInput;
					GridComp gridnew = (GridComp) gEditorInput
							.getCloneElement();
					LfwWindow pmnew = gEditorInput.getPagemeta();
					LfwView widgetnew = gEditorInput.getWidget();
					if (pmnew == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& gridnew.getId().equals(gridcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pagemeta.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& gridnew.getId().equals(gridcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(gridEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pagemeta == null)
				editoridMap.put(lfwwidget.getId() + "grid" + gridcomp.getId(), //$NON-NLS-1$
						lfwwidget.getId() + "grid" + gridcomp.getId()); //$NON-NLS-1$
			else
				editoridMap.put(pagemeta.getId() + lfwwidget.getId() + "grid" //$NON-NLS-1$
						+ gridcomp.getId(), pagemeta.getId()
						+ lfwwidget.getId() + "grid" + gridcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(gridEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openExcelEditor(LFWWebComponentTreeItem excelComponent) {
//		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
//				.getWidgetTreeItem(excelComponent);
//		LfwWidget lfwwidget = widgetTreeItem.getWidget();
//		LFWPersTool.setLfwWidget(lfwwidget);
//		LFWPageMetaTreeItem pageMetaTreeItem = LFWPersTool
//				.getPagemetaTreeItem(excelComponent);
//		PageMeta pagemeta = null;
//		if (pageMetaTreeItem != null)
//			pagemeta = pageMetaTreeItem.getPm();
//		ExcelComp excel = (ExcelComp) excelComponent.getData();
//		ExcelEditorInput excelEditorInput = new ExcelEditorInput(excel,
//				lfwwidget, pagemeta);
//		excelComponent.setEditorInput(excelEditorInput);
//		String editorid = ExcelEditor.class.getName();
//		if (excelEditorInput == null || editorid == null)
//			return;
//		IWorkbenchPage workbenchPage = getViewSite().getPage();
//		IEditorPart editor = null;
//		ExcelComp excelcomp = (ExcelComp) excelComponent.getData();
//		String editorinputid = null;
//		if (pagemeta == null)
//			editorinputid = editoridMap.get(lfwwidget.getId() + "excel"
//					+ excelcomp.getId());
//		else
//			editorinputid = editoridMap.get(pagemeta.getId()
//					+ lfwwidget.getId() + "excel" + excelcomp.getId());
//		if (editorinputid != null) {
//			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
//			for (int i = 0; i < parts.length; i++) {
//				IEditorInput editorInput = (IEditorInput) parts[i]
//						.getEditorInput();
//				if (editorInput instanceof ExcelEditorInput) {
//					ExcelEditorInput gEditorInput = (ExcelEditorInput) editorInput;
//					ExcelComp gridnew = (ExcelComp) gEditorInput
//							.getCloneElement();
//					PageMeta pmnew = gEditorInput.getPagemeta();
//					LfwWidget widgetnew = gEditorInput.getWidget();
//					if (pmnew == null) {
//						if (widgetnew.getId().equals(lfwwidget.getId())
//								&& gridnew.getId().equals(excelcomp.getId())) {
//							editor = parts[i];
//							break;
//						}
//					} else {
//						if (pmnew.getId().equals(pagemeta.getId())
//								&& widgetnew.getId().equals(lfwwidget.getId())
//								&& gridnew.getId().equals(excelcomp.getId())) {
//							editor = parts[i];
//							break;
//						}
//					}
//				}
//			}
//			LFWPersTool.getLFWViewSheet();
//			if (editor != null)
//				workbenchPage.bringToTop(editor);
//			else {
//				try {
//					workbenchPage.openEditor(excelEditorInput, editorid);
//				} catch (PartInitException e1) {
//					MainPlugin.getDefault().logError(e1.getMessage(), e1);
//				}
//			}
//		} else {
//			if (pagemeta == null)
//				editoridMap.put(
//						lfwwidget.getId() + "excel" + excelcomp.getId(),
//						lfwwidget.getId() + "excel" + excelcomp.getId());
//			else
//				editoridMap.put(pagemeta.getId() + lfwwidget.getId() + "excel"
//						+ excelcomp.getId(), pagemeta.getId()
//						+ lfwwidget.getId() + "excel" + excelcomp.getId());
//			try {
//				LFWPersTool.getLFWViewSheet();
//				workbenchPage.openEditor(excelEditorInput, editorid);
//			} catch (PartInitException e1) {
//				MainPlugin.getDefault().logError(e1.getMessage(), e1);
//			}
//		}
	}

	/**
	 * 打开button编辑器
	 * 
	 * @param gridComponent
	 */
	public void openButtonEditor(LFWWebComponentTreeItem buttonComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(buttonComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		ButtonComp button = (ButtonComp) buttonComponent.getData();
		ButtonEditorInput buttonEditorInput = new ButtonEditorInput(button,
				lfwwidget, pm);
		buttonComponent.setEditorInput(buttonEditorInput);
		String editorid = ButtonEditor.class.getName();
		if (buttonEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		ButtonComp buttoncomp = (ButtonComp) buttonComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "button" //$NON-NLS-1$
					+ buttoncomp.getId());
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId()
					+ "button" + buttoncomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof ButtonEditorInput) {
					ButtonEditorInput bEditorInput = (ButtonEditorInput) editorInput;
					LfwWindow pmnew = bEditorInput.getPagemeta();
					LfwView widgetnew = bEditorInput.getWidget();
					ButtonComp buttonnew = (ButtonComp) bEditorInput
							.getCloneElement();
					if (pm == null||pmnew == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& buttonnew.getId().equals(buttoncomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pm.getId().equals(pmnew.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& buttonnew.getId().equals(buttoncomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(buttonEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "button" //$NON-NLS-1$
						+ buttoncomp.getId(), lfwwidget.getId() + "button" //$NON-NLS-1$
						+ buttoncomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "button" //$NON-NLS-1$
						+ buttoncomp.getId(), pm.getId() + lfwwidget.getId()
						+ "button" + buttoncomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(buttonEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openChartEditor(LFWWebComponentTreeItem chartComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(chartComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		ChartComp chart = (ChartComp) chartComponent.getData();
		ChartEditorInput chartEditorInput = new ChartEditorInput(chart,
				lfwwidget, pm);
		chartComponent.setEditorInput(chartEditorInput);
		String editorid = ChartEditor.class.getName();
		if (chartEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		ChartComp chartcomp = (ChartComp) chartComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "chart" + chartcomp.getId()); //$NON-NLS-1$
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId() + "chart" //$NON-NLS-1$
					+ chartcomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof ChartEditorInput) {
					ChartEditorInput bEditorInput = (ChartEditorInput) editorInput;
					LfwWindow pmnew = bEditorInput.getPagemeta();
					LfwView widgetnew = bEditorInput.getWidget();
					ChartComp chartnew = (ChartComp) bEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& chartnew.getId().equals(chartcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& chartnew.getId().equals(chartcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(chartEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(
						lfwwidget.getId() + "chart" + chartcomp.getId(), //$NON-NLS-1$
						lfwwidget.getId() + "chart" + chartcomp.getId()); //$NON-NLS-1$
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "chart" //$NON-NLS-1$
						+ chartcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "chart" + chartcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(chartEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openSelfdefEditor(LFWWebComponentTreeItem selfdefComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(selfdefComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		SelfDefComp selfdef = (SelfDefComp) selfdefComponent.getData();
		SelfDefCompEditorInput selfEditorInput = new SelfDefCompEditorInput(
				selfdef, lfwwidget, pm);
		selfdefComponent.setEditorInput(selfEditorInput);
		String editorid = SelfDefCompEditor.class.getName();
		if (selfEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		SelfDefComp selfdefcomp = (SelfDefComp) selfdefComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "selfdef" + selfdefcomp.getId()); //$NON-NLS-1$
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId() + "selfdef" //$NON-NLS-1$
					+ selfdefcomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof SelfDefCompEditorInput) {
					SelfDefCompEditorInput bEditorInput = (SelfDefCompEditorInput) editorInput;
					LfwWindow pmnew = bEditorInput.getPagemeta();
					LfwView widgetnew = bEditorInput.getWidget();
					SelfDefComp selfnew = (SelfDefComp) bEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& selfnew.getId().equals(selfdefcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& selfnew.getId().equals(selfdefcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(selfEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "selfdef" //$NON-NLS-1$
						+ selfdefcomp.getId(), lfwwidget.getId() + "selfdef" //$NON-NLS-1$
						+ selfdefcomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "selfdef" //$NON-NLS-1$
						+ selfdefcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "selfdef" + selfdefcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(selfEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openProgressBarEditor(LFWWebComponentTreeItem buttonComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(buttonComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		ProgressBarComp button = (ProgressBarComp) buttonComponent.getData();
		ProgressBarEditorInput buttonEditorInput = new ProgressBarEditorInput(
				button, lfwwidget, pm);
		buttonComponent.setEditorInput(buttonEditorInput);
		String editorid = ProgressBarEditor.class.getName();
		if (buttonEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		ProgressBarComp buttoncomp = (ProgressBarComp) buttonComponent
				.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "progressbar" //$NON-NLS-1$
					+ buttoncomp.getId());
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId() + "progressbar" //$NON-NLS-1$
					+ buttoncomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof ProgressBarEditorInput) {
					ProgressBarEditorInput bEditorInput = (ProgressBarEditorInput) editorInput;
					LfwWindow pmnew = bEditorInput.getPagemeta();
					LfwView widgetnew = bEditorInput.getWidget();
					ProgressBarComp buttonnew = (ProgressBarComp) bEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& buttonnew.getId().equals(buttoncomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& buttonnew.getId().equals(buttoncomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(buttonEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "progressbar" //$NON-NLS-1$
						+ buttoncomp.getId(), lfwwidget.getId() + "progressbar" //$NON-NLS-1$
						+ buttoncomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "progressbar" //$NON-NLS-1$
						+ buttoncomp.getId(), pm.getId() + lfwwidget.getId()
						+ "progressbar" + buttoncomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(buttonEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openRefNodeRelEditor(LFWRefNodeRelTreeItem refNodeRel) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(refNodeRel);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		RefNodeRelation rel = (RefNodeRelation) refNodeRel.getData();
		RefnoderelEditorInput refnodeRelEditorInput = new RefnoderelEditorInput(
				rel, lfwwidget, pm);
		refNodeRel.setEditorInput(refnodeRelEditorInput);
		String editorid = RefnoderelEditor.class.getName();
		if (refnodeRelEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		RefNodeRelation relcomp = (RefNodeRelation) refNodeRel.getData();
		String editorinputid = null;
		if (pm == null)
			editoridMap.get(lfwwidget.getId() + "refnodrel" + relcomp.getId()); //$NON-NLS-1$
		else
			editoridMap.get(pm.getId() + lfwwidget.getId() + "refnodrel" //$NON-NLS-1$
					+ relcomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof RefnoderelEditorInput) {
					RefnoderelEditorInput bEditorInput = (RefnoderelEditorInput) editorInput;
					LfwWindow pmnew = bEditorInput.getPagemeta();
					LfwView widgetnew = bEditorInput.getWidget();
					RefNodeRelation relnew = (RefNodeRelation) bEditorInput
							.getRefnodeRel();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& relnew.getId().equals(relcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& relnew.getId().equals(relcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(refnodeRelEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "refnodrel" //$NON-NLS-1$
						+ relcomp.getId(), lfwwidget.getId() + "refnodrel" //$NON-NLS-1$
						+ relcomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "refnodrel" //$NON-NLS-1$
						+ relcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "refnodrel" + relcomp.getId()); //$NON-NLS-1$
			try {
				workbenchPage.openEditor(refnodeRelEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openImageEditor(LFWWebComponentTreeItem imageComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(imageComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		ImageComp image = (ImageComp) imageComponent.getData();
		ImageEditorInput imageEditorInput = new ImageEditorInput(image,
				lfwwidget, pm);
		imageComponent.setEditorInput(imageEditorInput);
		String editorid = ImageEditor.class.getName();
		if (imageEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		ImageComp buttoncomp = (ImageComp) imageComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "image" + buttoncomp.getId()); //$NON-NLS-1$
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId() + "image" //$NON-NLS-1$
					+ buttoncomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof ImageEditorInput) {
					ImageEditorInput bEditorInput = (ImageEditorInput) editorInput;
					LfwWindow pmnew = bEditorInput.getPagemeta();
					LfwView widgetnew = bEditorInput.getWidget();
					ImageComp buttonnew = (ImageComp) bEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& buttonnew.getId().equals(buttoncomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& buttonnew.getId().equals(buttoncomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(imageEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "image" //$NON-NLS-1$
						+ buttoncomp.getId(), lfwwidget.getId() + "image" //$NON-NLS-1$
						+ buttoncomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "image" //$NON-NLS-1$
						+ buttoncomp.getId(), pm.getId() + lfwwidget.getId()
						+ "image" + buttoncomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(imageEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开imageButton
	 * 
	 * @param imageComponent
	 */
	public void openImageButtonEditor(LFWWebComponentTreeItem imageComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(imageComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = pmTreeItem.getPm();
		ImageComp image = (ImageComp) imageComponent.getData();
		ImageEditorInput imageEditorInput = new ImageEditorInput(image,
				lfwwidget, pm);
		imageComponent.setEditorInput(imageEditorInput);
		String editorid = ImageEditor.class.getName();
		if (imageEditorInput == null || editorid == null)
			return;
		// buttonEditorInput.setProjectPath(getProjectPath(buttonComponent));
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		ImageComp buttoncomp = (ImageComp) imageComponent.getData();
		String editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId()
				+ "image" + buttoncomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof ImageEditorInput) {
					ImageEditorInput bEditorInput = (ImageEditorInput) editorInput;
					ImageComp buttonnew = (ImageComp) bEditorInput
							.getCloneElement();
					if (buttonnew.getId().equals(buttoncomp.getId())) {
						editor = parts[i];
						break;
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(imageEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			editoridMap.put(pm.getId() + lfwwidget.getId() + "image" //$NON-NLS-1$
					+ buttoncomp.getId(), pm.getId() + lfwwidget.getId()
					+ "image" + buttoncomp.getId()); //$NON-NLS-1$
			try {
				workbenchPage.openEditor(imageEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openLabelEditor(LFWWebComponentTreeItem labelComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(labelComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		LabelComp label = (LabelComp) labelComponent.getData();
		LabelEditorInput labelEditorInput = new LabelEditorInput(label,
				lfwwidget, pm);
		labelComponent.setEditorInput(labelEditorInput);
		String editorid = LabelEditor.class.getName();
		if (labelEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		LabelComp labelcomp = (LabelComp) labelComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "label" + labelcomp.getId()); //$NON-NLS-1$
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId() + "label" //$NON-NLS-1$
					+ labelcomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof LabelEditorInput) {
					LabelEditorInput lEditorInput = (LabelEditorInput) editorInput;
					LfwWindow pmnew = lEditorInput.getPagemeta();
					LabelComp labelnew = (LabelComp) lEditorInput
							.getCloneElement();
					LfwView widgetnew = lEditorInput.getWidget();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& labelnew.getId().equals(labelcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& labelnew.getId().equals(labelcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(labelEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(
						lfwwidget.getId() + "label" + labelcomp.getId(), //$NON-NLS-1$
						lfwwidget.getId() + "label" + labelcomp.getId()); //$NON-NLS-1$
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "label" //$NON-NLS-1$
						+ labelcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "label" + labelcomp.getId()); //$NON-NLS-1$
			try {
				workbenchPage.openEditor(labelEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openLinkEditor(LFWWebComponentTreeItem linkComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(linkComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		LinkComp link = (LinkComp) linkComponent.getData();
		LinkCompEditroInput linkcompEditorInput = new LinkCompEditroInput(link,
				lfwwidget, pm);
		linkComponent.setEditorInput(linkcompEditorInput);
		String editorid = LinkCompEditor.class.getName();
		if (linkcompEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		LinkComp linkcomp = (LinkComp) linkComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "linkcomp" + linkcomp.getId()); //$NON-NLS-1$
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId() + "linkcomp" //$NON-NLS-1$
					+ linkcomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof LinkCompEditroInput) {
					LinkCompEditroInput lEditorInput = (LinkCompEditroInput) editorInput;
					LfwWindow pmnew = lEditorInput.getPagemeta();
					LfwView widgetnew = lEditorInput.getWidget();
					LinkComp linknew = (LinkComp) lEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& linknew.getId().equals(linkcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& linknew.getId().equals(linkcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(linkcompEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "linkcomp" //$NON-NLS-1$
						+ linkcomp.getId(), lfwwidget.getId() + "linkcomp" //$NON-NLS-1$
						+ linkcomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "linkcomp" //$NON-NLS-1$
						+ linkcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "linkcomp" + linkcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(linkcompEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openIFrameEditor(LFWWebComponentTreeItem iframecomponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(iframecomponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		IFrameComp iFrame = (IFrameComp) iframecomponent.getData();
		IFrameEditorInput iframeEditorInput = new IFrameEditorInput(iFrame,
				lfwwidget, pm);
		iframecomponent.setEditorInput(iframeEditorInput);
		String editorid = IFrameEditor.class.getName();
		if (iframeEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		IFrameComp iframecomp = (IFrameComp) iframecomponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "iframe" //$NON-NLS-1$
					+ iframecomp.getId());
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId()
					+ "iframe" + iframecomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof IFrameEditorInput) {
					IFrameEditorInput ifEditorInput = (IFrameEditorInput) editorInput;
					LfwWindow pmnew = ifEditorInput.getPagemeta();
					LfwView widgetnew = ifEditorInput.getWidget();
					IFrameComp iframenew = (IFrameComp) ifEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& iframenew.getId().equals(iframenew.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& iframenew.getId().equals(iframenew.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(iframeEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "iframe" //$NON-NLS-1$
						+ iframecomp.getId(), lfwwidget.getId() + "iframe" //$NON-NLS-1$
						+ iframecomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "iframe" //$NON-NLS-1$
						+ iframecomp.getId(), pm.getId() + lfwwidget.getId()
						+ "iframe" + iframecomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(iframeEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开form编辑器
	 * 
	 * @param formComponent
	 */
	public void openFormEditor(LFWWebComponentTreeItem formComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(formComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pageTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pageTreeItem != null)
			pm = pageTreeItem.getPm();
		FormComp formcomp = (FormComp) formComponent.getData();
		FormEditorInput formEditorInput = new FormEditorInput(formcomp,
				lfwwidget, pm);
		formComponent.setEditorInput(formEditorInput);
		String editorid = FormEditor.class.getName();
		if (formEditorInput == null || editorid == null)
			return;
		// formEditorInput.setProjectPath(getProjectPath(formComponent));
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "form" //$NON-NLS-1$
					+ formcomp.getId());
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId()
					+ "form" + formcomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof FormEditorInput) {
					FormEditorInput fEditorInput = (FormEditorInput) editorInput;
					LfwWindow pmnew = fEditorInput.getPagemeta();
					LfwView widgetnew = fEditorInput.getWidget();
					FormComp formnew = (FormComp) fEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& formnew.getId().equals(formcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& formnew.getId().equals(formcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(formEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "form" + formcomp.getId(), //$NON-NLS-1$
						lfwwidget.getId() + "form" + formcomp.getId()); //$NON-NLS-1$
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "form" //$NON-NLS-1$
						+ formcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "form" + formcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(formEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openToolbarEditor(LFWWebComponentTreeItem toolbarComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(toolbarComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pageTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pageTreeItem != null)
			pm = pageTreeItem.getPm();
		ToolBarComp toolbar = (ToolBarComp) toolbarComponent.getData();
		ToolBarEditorInput toolbarEditorInput = new ToolBarEditorInput(toolbar,
				lfwwidget, pm);
		toolbarComponent.setEditorInput(toolbarEditorInput);
		String editorid = ToolBarEditor.class.getName();
		if (toolbarEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorinputid = null;
		if (pm == null)
			editoridMap.get(lfwwidget.getId() + "toolbar" + toolbar.getId()); //$NON-NLS-1$
		else
			editoridMap.get(pm.getId() + lfwwidget.getId() + "toolbar" //$NON-NLS-1$
					+ toolbar.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof ToolBarEditorInput) {
					ToolBarEditorInput toolInput = (ToolBarEditorInput) editorInput;
					LfwWindow pmnew = toolInput.getPagemeta();
					LfwView widgetnew = toolInput.getWidget();
					ToolBarComp toolbarnew = (ToolBarComp) toolInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& toolbarnew.getId().equals(toolbar.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& toolbarnew.getId().equals(toolbar.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(toolbarEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(
						lfwwidget.getId() + "toolbar" + toolbar.getId(), //$NON-NLS-1$
						lfwwidget.getId() + "toolbar" + toolbar.getId()); //$NON-NLS-1$
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "toolbar" //$NON-NLS-1$
						+ toolbar.getId(), pm.getId() + lfwwidget.getId()
						+ "toolbar" + toolbar.getId()); //$NON-NLS-1$
			try {
				workbenchPage.openEditor(toolbarEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开文本框编辑器
	 * 
	 * @param textCompComponent
	 */
	public void openTextEditor(LFWWebComponentTreeItem textCompComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(textCompComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeTtem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeTtem != null)
			pm = pmTreeTtem.getPm();
		TextComp textComp = (TextComp) textCompComponent.getData();
		TextCompEditorInput textcompEditorInput = new TextCompEditorInput(
				textComp, lfwwidget, pm);
		textCompComponent.setEditorInput(textcompEditorInput);
		String editorid = TextCompEditor.class.getName();
		if (textcompEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		TextComp textcomp = (TextComp) textCompComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "textcomp" + textcomp.getId()); //$NON-NLS-1$
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId() + "textcomp" //$NON-NLS-1$
					+ textcomp.getId());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof TextCompEditorInput) {
					TextCompEditorInput textEditorInput = (TextCompEditorInput) editorInput;
					LfwWindow pmnew = textEditorInput.getPagemeta();
					LfwView widgetnew = textEditorInput.getWidget();
					TextComp textnew = (TextComp) textEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& textnew.getId().equals(textcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pm == null) {
							if (widgetnew.getId().equals(lfwwidget.getId())
									&& textnew.getId().equals(textcomp.getId())) {
								editor = parts[i];
								break;
							}
						} else {
							if (pmnew.getId().equals(pm.getId())
									&& widgetnew.getId().equals(
											lfwwidget.getId())
									&& textnew.getId().equals(textcomp.getId())) {
								editor = parts[i];
								break;
							}
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(textcompEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "textcomp" //$NON-NLS-1$
						+ textcomp.getId(), lfwwidget.getId() + "textcomp" //$NON-NLS-1$
						+ textcomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "textcomp" //$NON-NLS-1$
						+ textcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "textcomp" + textcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(textcompEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开树编辑器
	 * 
	 * @param gridComponent
	 */
	public void openTreeEditor(LFWWebComponentTreeItem treeComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(treeComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		TreeViewComp tree = (TreeViewComp) treeComponent.getData();
		TreeEditorInput treeEditorInput = new TreeEditorInput(tree, lfwwidget,
				pm);
		treeComponent.setEditorInput(treeEditorInput);
		String editorid = TreeEditor.class.getName();
		if (treeEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		TreeViewComp treecomp = (TreeViewComp) treeComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "tree" //$NON-NLS-1$
					+ treecomp.getId());
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId()
					+ "tree" + treecomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof TreeEditorInput) {
					TreeEditorInput treeviewEditorInput = (TreeEditorInput) editorInput;
					LfwWindow pmnew = treeviewEditorInput.getPagemeta();
					LfwView widgetnew = treeviewEditorInput.getWidget();
					TreeViewComp treenew = (TreeViewComp) treeviewEditorInput
							.getCloneElement();
					if (pm == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& treenew.getId().equals(treecomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& treenew.getId().equals(treecomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(treeEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "tree" + treecomp.getId(), //$NON-NLS-1$
						lfwwidget.getId() + "tree" + treecomp.getId()); //$NON-NLS-1$
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "tree" //$NON-NLS-1$
						+ treecomp.getId(), pm.getId() + lfwwidget.getId()
						+ "tree" + treecomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(treeEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开下拉数据编辑器
	 * 
	 * @param treeComponent
	 */
	public void openComboEditor(LFWComboTreeItem comboDataComponent) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(comboDataComponent);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		ComboData comb = (ComboData) comboDataComponent.getData();
		ComboDataEidtorInput comboDataEditorInput = new ComboDataEidtorInput(
				comb, lfwwidget, pm);
		comboDataComponent.setEditorInput(comboDataEditorInput);
		String editorid = ComboDataEditor.class.getName();
		if (comboDataEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		ComboData combcomp = (ComboData) comboDataComponent.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "combodata" //$NON-NLS-1$
					+ combcomp.getId());
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId()
					+ "combodata" + combcomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof ComboDataEidtorInput) {
					ComboDataEidtorInput comboEditorInput = (ComboDataEidtorInput) editorInput;
					LfwWindow pmnew = comboEditorInput.getPagemeta();
					LfwView widgetnew = comboEditorInput.getWidget();
					ComboData combonew = (ComboData) comboEditorInput
							.getCloneElement();
					if (pmnew == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& combonew.getId().equals(combcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& combonew.getId().equals(combcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(comboDataEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(lfwwidget.getId() + "combodata" //$NON-NLS-1$
						+ combcomp.getId(), lfwwidget.getId() + "combodata" //$NON-NLS-1$
						+ combcomp.getId());
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "combodata" //$NON-NLS-1$
						+ combcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "combodata" + combcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(comboDataEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开参照节点
	 * 
	 * @param comboDataComponent
	 */
	public void openRefNodeEditor(LFWRefNodeTreeItem refnodeTreeItem) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(refnodeTreeItem);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		LFWPersTool.setLfwWidget(lfwwidget);
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		BaseRefNode refnode = (BaseRefNode) refnodeTreeItem.getData();
		RefNodeEditorInput refnodeEditorInput = new RefNodeEditorInput(refnode,
				lfwwidget, pm);
		refnodeTreeItem.setEditorInput(refnodeEditorInput);
		String editorid = RefNodeEditor.class.getName();
		if (refnodeEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		BaseRefNode refcomp = (BaseRefNode) refnodeTreeItem.getData();
		String editorinputid = null;
		if (pm == null)
			editorinputid = editoridMap.get(lfwwidget.getId() + "refnode" //$NON-NLS-1$
					+ refcomp.getId());
		else
			editorinputid = editoridMap.get(pm.getId() + lfwwidget.getId()
					+ "refnode" + refcomp.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof RefNodeEditorInput) {
					RefNodeEditorInput refEditorInput = (RefNodeEditorInput) editorInput;
					LfwWindow pmnew = refEditorInput.getPagemeta();
					LfwView widgetnew = refEditorInput.getWidget();
					BaseRefNode refnew = (BaseRefNode) refEditorInput.getCloneElement();
					if (pmnew == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& refnew.getId().equals(refcomp.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& refnew.getId().equals(refcomp.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(refnodeEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put(
						lfwwidget.getId() + "refnode" + refcomp.getId(), //$NON-NLS-1$
						lfwwidget.getId() + "refnode" + refcomp.getId()); //$NON-NLS-1$
			else
				editoridMap.put(pm.getId() + lfwwidget.getId() + "refnode" //$NON-NLS-1$
						+ refcomp.getId(), pm.getId() + lfwwidget.getId()
						+ "refnode" + refcomp.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(refnodeEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	/**
	 * 打开公共池中参照节点
	 * 
	 * @param ds
	 */
	public void openPoolRefNodeEdit(LFWRefNodeTreeItem refnodeTreeItem) {
		RefNodeConf refnode = (RefNodeConf) refnodeTreeItem.getData();
		RefNodeEditorInput refnodeEditorInput = new RefNodeEditorInput(refnode,
				null, null);
		refnodeEditorInput.setFromPool(true);
		refnodeTreeItem.setEditorInput(refnodeEditorInput);
		String editorid = "nc.uap.lfw.refnode.core.RefNodeEditor"; //$NON-NLS-1$
		if (refnodeEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorinputid = editoridMap.get("poolrefnode" + refnode.getId()); //$NON-NLS-1$
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof RefNodeEditorInput) {
					RefNodeEditorInput refEditorInput = (RefNodeEditorInput) editorInput;
					BaseRefNode refnodenew = (BaseRefNode) refEditorInput
							.getCloneElement();
					if (refnodenew.getId().equals(refnode.getId())) {
						editor = parts[i];
						break;
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(refnodeEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			editoridMap.put("poolrefnode" + refnode.getId(), "poolrefnode" //$NON-NLS-1$ //$NON-NLS-2$
					+ refnode.getId());
			try {
				workbenchPage.openEditor(refnodeEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	// 打开datasets编辑器
	public void openDatasetsEditor(LFWSeparateTreeItem treeItem) {
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(treeItem);
		LfwView lfwwidget = widgetTreeItem.getWidget();
		// getWidget(widgetTreeItem);
		LFWPersTool.setLfwWidget(lfwwidget);
		DatasetsEditorInput datasetsEditorInput = new DatasetsEditorInput(
				treeItem);
		String editorid = DatasetsEditor.class.getName();
		if (datasetsEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		File file = (File) treeItem.getData();
		String editorinputid = editoridMap.get(file.getPath() + "datasets" //$NON-NLS-1$
				+ file.toString());
		if (editorinputid != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = (IEditorInput) parts[i]
						.getEditorInput();
				if (editorInput instanceof DatasetsEditorInput) {
					DatasetsEditorInput gEditorInput = (DatasetsEditorInput) editorInput;
					File filenew = (File) gEditorInput.getSeparateTreeItem()
							.getData();
					String name = (String) filenew.toString();
					if (file.toString().equals(name)) {
						editor = parts[i];
						break;
					}
				}
			}
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(datasetsEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			editoridMap.put(file.getPath() + "datasets" + file.toString(), file //$NON-NLS-1$
					.getPath()
					+ "datasets" + file.toString()); //$NON-NLS-1$
			try {
				workbenchPage.openEditor(datasetsEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}
	}

	public void openMenubarEditor(LFWMenubarCompTreeItem treeItem) {
		MenubarComp menubar = null;
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool
				.getWidgetTreeItem(treeItem);
		LfwView lfwwidget = null;
		if (widgetTreeItem != null)
			lfwwidget = widgetTreeItem.getWidget();
		MenubarComp menubarOld = (MenubarComp) treeItem.getData();
		LFWPageMetaTreeItem pageMetaTreeItem = LFWPersTool
				.getPagemetaTreeItem(treeItem);
		LfwWindow pm = null;
		if (pageMetaTreeItem != null)
			pm = pageMetaTreeItem.getPm();
		LfwView widget = LFWPersTool.getCurrentWidget();
//		if (treeItem.isFromWidget()) {
//			menubar = widget.getViewMenus().getMenuBar(menubarOld.getId());
//		} else {
//			menubar = pm.getViewMenus().getMenuBar(menubarOld.getId());
//		}
		MenubarEditorInput menubarEditorInput = new MenubarEditorInput(menubar,
				pm);
		if (treeItem.isFromWidget())
			menubarEditorInput.setWidget(widget);
		String editorid = MenubarEditor.class.getName();
		if (menubarEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		MenubarComp menubarComp = ((MenubarComp) treeItem.getData());
		String editorInputId = null;
		if (pm == null)
			editorInputId = editoridMap.get("menubar_" + widget.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
					+ menubarComp.getId());
		else
			editorInputId = editoridMap.get("menubar_" + pm.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
					+ menubarComp.getId());
		if (editorInputId != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = parts[i].getEditorInput();
				if (editorInput instanceof MenubarEditorInput) {
					MenubarEditorInput mbEditorInput = (MenubarEditorInput) editorInput;
					MenubarComp mbnew = (MenubarComp) mbEditorInput
							.getElement();
					LfwWindow pmnew = mbEditorInput.getPagemeta();
					LfwView widgetnew = mbEditorInput.getWidget();
					if (pmnew == null) {
						if (widgetnew.getId().equals(lfwwidget.getId())
								&& menubarComp.getId().equals(mbnew.getId())) {
							editor = parts[i];
							break;
						}
					} else {
						if (pmnew.getId().equals(pm.getId())
								&& widgetnew.getId().equals(lfwwidget.getId())
								&& menubarComp.getId().equals(mbnew.getId())) {
							editor = parts[i];
							break;
						}
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(menubarEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put("menubar_" + widget.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
						+ menubarComp.getId(), "menubar_" + widget.getId() //$NON-NLS-1$
						+ "_" + menubarComp.getId()); //$NON-NLS-1$
			else
				editoridMap.put("menubar_" + pm.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
						+ menubarComp.getId(), "menubar_" + pm.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
						+ menubarComp.getId());
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(menubarEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}

	}

	/**
	 * 打开右键菜单
	 * 
	 * @param treeItem
	 */
	public void openContextMenuEditor(LFWContextMenuTreeItem treeItem) {
		// MenubarEditorInput menubarEditorInput = new
		// MenubarEditorInput(treeItem);
		ContextMenuComp oldMenubar = (ContextMenuComp) treeItem.getData();
		LfwView widget = LFWPersTool.getCurrentWidget();
		LFWPageMetaTreeItem pmTreeItem = LFWPersTool
				.getCurrentPageMetaTreeItem();
		LfwWindow pm = null;
		if (pmTreeItem != null)
			pm = pmTreeItem.getPm();
		ContextMenuComp menubar = null;
		menubar = widget.getViewMenus().getContextMenu(oldMenubar.getId());
//		if(menubar == null)
//			menubar = pm.getViewMenus().getContextMenu(oldMenubar.getId());
		ContextMenuEditorInput menubarEditorInput = new ContextMenuEditorInput(
				menubar, widget, pm);
		String editorid = ContextMenuEditor.class.getName();
		if (menubarEditorInput == null || editorid == null)
			return;
		IWorkbenchPage workbenchPage = getViewSite().getPage();
		IEditorPart editor = null;
		String editorInputId = null;
		if (pm == null){
			editorInputId = editoridMap.get("contextmenubar_" + widget.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
					+ menubar.getId());
		}
		else{
			editorInputId = editoridMap.get("contextmenubar_" + pm.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
					+ widget.getId() + "_" + menubar.getId()); //$NON-NLS-1$
		}
		if (editorInputId != null) {
			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
			for (int i = 0; i < parts.length; i++) {
				IEditorInput editorInput = parts[i].getEditorInput();
				if (editorInput instanceof ContextMenuEditorInput) {
					ContextMenuEditorInput mbEditorInput = (ContextMenuEditorInput) editorInput;
					ContextMenuComp mbnew = (ContextMenuComp) mbEditorInput
							.getElement();
					if (menubar.getId().equals(mbnew.getId())) {
						editor = parts[i];
						break;
					}
				}
			}
			LFWPersTool.getLFWViewSheet();
			if (editor != null)
				workbenchPage.bringToTop(editor);
			else {
				try {
					workbenchPage.openEditor(menubarEditorInput, editorid);
				} catch (PartInitException e1) {
					MainPlugin.getDefault().logError(e1.getMessage(), e1);
				}
			}
		} else {
			if (pm == null)
				editoridMap.put("contextmenubar_" + widget.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
						+ menubar.getId(), "contextmenubar_" //$NON-NLS-1$
						+ widget.getId() + "_" + menubar.getId()); //$NON-NLS-1$
			else
				editoridMap.put("contextmenubar_" + pm.getId() + "_" //$NON-NLS-1$ //$NON-NLS-2$
						+ widget.getId() + "_" + menubar.getId(), //$NON-NLS-1$
						"contextmenubar_" + pm.getId() + "_" + widget.getId() //$NON-NLS-1$ //$NON-NLS-2$
								+ "_" + menubar.getId()); //$NON-NLS-1$
			try {
				LFWPersTool.getLFWViewSheet();
				workbenchPage.openEditor(menubarEditorInput, editorid);
			} catch (PartInitException e1) {
				MainPlugin.getDefault().logError(e1.getMessage(), e1);
			}
		}

	}

//	// 打开pageflow编辑器
//	@SuppressWarnings("deprecation")
//	public void openPageFlowEditor(LFWPageFlowTreeItem treeItem) {
//		PageFlow pageFlow = (PageFlow) treeItem.getData();
//		PageFlowEditorInput editorinput = new PageFlowEditorInput(pageFlow);
//		String editorid = PageFlowEditor.class.getName();
//		if (editorinput == null || editorid == null)
//			return;
//		IWorkbenchPage workbenchPage = getViewSite().getPage();
//		IEditorPart editor = null;
//		String editorinputid = editoridMap.get("pageflow" + pageFlow.getName());
//		if (editorinputid != null) {
//			IEditorPart[] parts = LFWTool.getEditors(workbenchPage);
//			for (int i = 0; i < parts.length; i++) {
//				IEditorInput editorInput = (IEditorInput) parts[i]
//						.getEditorInput();
//				if (editorInput instanceof PageFlowEditorInput) {
//					PageFlowEditorInput pfEditorInput = (PageFlowEditorInput) editorInput;
//					PageFlow pfnew = (PageFlow) pfEditorInput.getPageFlow();
//					String name = pfnew.getName();
//					if (pageFlow.getName().equals(name)) {
//						editor = parts[i];
//						break;
//					}
//				}
//			}
//			if (editor != null)
//				workbenchPage.bringToTop(editor);
//			else {
//				try {
//					workbenchPage.openEditor(editorinput, editorid);
//				} catch (PartInitException e1) {
//					MainPlugin.getDefault().logError(e1.getMessage(), e1);
//				}
//			}
//		} else {
//			editoridMap.put("pageflow" + pageFlow.getName(), "pageflow"
//					+ pageFlow.getName());
//			try {
//				workbenchPage.openEditor(editorinput, editorid);
//			} catch (PartInitException e1) {
//				MainPlugin.getDefault().logError(e1.getMessage(), e1);
//			}
//		}
//	}

	Map<String, LFWSeparateTreeItem> separateMap = new HashMap<String, LFWSeparateTreeItem>();

	// 创建文件夹
	public LFWSeparateTreeItem createFolder(TreeItem tree, File file,
			String name) {
		File f = new File(file, name);
		LFWSeparateTreeItem dirtory = new LFWSeparateTreeItem(tree, f);
		return dirtory;
	}

	// 树节点时候打开过
	public boolean hasOpened(TreeItem treeItem) {
		if (treeItem instanceof LFWPageMetaTreeItem) {
			TreeItem[] pmItems = treeItem.getItems();
			if (pmItems.length > 0) {
				for (int i = 0; i < pmItems.length; i++) {
					if (pmItems[i] instanceof LFWPageMetaTreeItem)
						continue;
					else
						return true;
				}

				return false;
			} else
				return false;

		} else {
			TreeItem[] items = treeItem.getItems();
			if (items.length > 0)
				return true;
			else
				return false;
		}
	}

	public void setFocus() {
		treeView.getControl().setFocus();

	}

	/**
	 * 增加目录
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addDirTreeNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();// (File)
		// selTI.getData();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录。"); //$NON-NLS-1$
		File f = new File(parentFile, dirName);
		if (!f.exists()) {
			f.mkdirs();
		}
		TreeItem ti = new LFWDirtoryTreeItem(selTI, f);
		// 设置类型
		((LFWDirtoryTreeItem) ti).setType(LFWDirtoryTreeItem.NODE_FOLDER);
		//guoweic: 菜单显示有问题
		selTI.setExpanded(true);
		return ti;
	}

	public TreeItem addVirtualTreeNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();// (File)
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		List<String> allTreeItems = LFWPersTool.getAllVirtualTreeItems();
		if (allTreeItems.contains(dirName))
			throw new Exception("已经存在ID为" + dirName + "的虚拟目录!"); //$NON-NLS-1$ //$NON-NLS-2$
		File f = new File(parentFile, dirName);
		if (!f.exists()) {
			f.mkdirs();
		}
		TreeItem ti = new LFWVirtualDirTreeItem(selTI, f);
		selTI.setExpanded(true);
		return ti;
	}

	public TreeItem addPageMetaTreeNode(String appId, String appName)
			throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();// (File)
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		if (appId != null
				&& (appId.equals("login") || appId.equals("main") //$NON-NLS-1$ //$NON-NLS-2$
						|| appId.equals("quickdesk") //$NON-NLS-1$
						|| appId.equals("reference") || appId //$NON-NLS-1$
						.equals("userprefs"))) //$NON-NLS-1$
			throw new Exception("已经存在ID为" + appId + "的节点!"); //$NON-NLS-1$ //$NON-NLS-2$
		String projectPath = LFWPersTool.getProjectPath();
		String[] projPaths = new String[1];
		projPaths[0] = projectPath;
		Map<String, String>[] appNamesMap = NCConnector.getPageNames(projPaths);
		if (appNamesMap != null && appNamesMap.length > 0) {
			Map<String, String> appNames = appNamesMap[0];
			if (appNames != null && appNames.size() > 0) {
				Iterator<String> itwd = appNames.keySet().iterator();
				while (itwd.hasNext()) {
					String applicationId = (String) itwd.next();
					if (applicationId.indexOf("/") != -1) { //$NON-NLS-1$
						applicationId = applicationId.substring(applicationId
								.indexOf("/")); //$NON-NLS-1$
					}
					if (applicationId.equals(appId)) {
						throw new Exception("已经存在ID为" + appId + "的节点!"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		}
		File f = new File(parentFile, appId);
		if (!f.exists()) {
			f.mkdirs();
		}
		TreeItem ti = new LFWPageMetaTreeItem(selTI, f, appName + "[" + appId //$NON-NLS-1$
				+ "]"); //$NON-NLS-1$
		((LFWPageMetaTreeItem) ti).setId(appId);
		((LFWPageMetaTreeItem) ti).setItemName(appName);
		selTI.setExpanded(true);
		return ti;
	}

	/**
	 * 在Windows（文件夹）节点下增加Window节点
	 * 
	 * @param windowId
	 * @param windowName
	 * @param treeItem
	 * @return
	 * @author chouhl
	 * @throws Exception
	 */
	public TreeItem addWindowTreeNode(String windowId, String windowName,IProject project, TreeItem currentTreeItem) throws Exception {
		TreeItem selTI = LFWPersTool.getWindowDirectoryTreeItem(project, currentTreeItem);
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		File f = new File(parentFile, windowId);
		if (!f.exists()) {
			f.mkdirs();
		}
		LFWPageMetaTreeItem ti = new LFWPageMetaTreeItem(selTI, f, windowName + "[" + windowId + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		ti.setId(windowId);
		ti.setItemName(windowName);
		selTI.setExpanded(true);
		return ti;
	}

	/**
	 * 增加节点
	 * @param amcId
	 * @param amcName
	 * @param itemType
	 * @return
	 * @throws Exception
	 */
	public TreeItem addAMCTreeNode(String amcId, String amcName, String itemType) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem selTI = tree.getSelection()[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		File f = new File(parentFile, amcId);
		if (!f.exists()) {
			f.mkdirs();
		}
		TreeItem ti = null;
		if (ILFWTreeNode.APPLICATION.equals(itemType)) {
			ti = new LFWApplicationTreeItem(selTI, f, amcName + "[" + amcId	+ "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else if(ILFWTreeNode.WINDOW.equals(itemType)){
			ti = new LFWPageMetaTreeItem(selTI, f, amcName + "[" + amcId + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else {
			ti = new LFWAMCTreeItem(selTI, f, amcName + "[" + amcId + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(ti instanceof LFWBasicTreeItem){
			((LFWBasicTreeItem)ti).setId(amcId);
			((LFWBasicTreeItem)ti).setItemName(amcName);
		}
		selTI.setExpanded(true);
		return ti;
	}
	
	/**
	 * 增加View节点
	 * @param viewId
	 * @param widget
	 * @return
	 */
	public TreeItem addViewTreeNode(LfwView widget){
		Tree tree = treeView.getTree();
		TreeItem selTI = tree.getSelection()[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		File f = new File(parentFile, widget.getId());
		if (!f.exists()) {
			f.mkdirs();
		}
		LfwWindow pm = LFWPersTool.getCurrentPageMeta();
		pm.addWidget(widget);
		TreeItem ti = new LFWWidgetTreeItem(selTI, f, widget, "[" + LFWTool.getViewText(selTI) + "] " + widget.getId()); //$NON-NLS-1$ //$NON-NLS-2$
		refreshWidgetTreeItem(ti);
		if(ti instanceof LFWBasicTreeItem){
			((LFWBasicTreeItem)ti).setId(widget.getId());
		}
		selTI.setExpanded(true);
		return ti;
	}

	/**
	 * 增加ds节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addDSTreeNode(String dirName,String caption) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(selTI);
		LfwView widget = widgetTreeItem.getWidget();
		Dataset originalds = widget.getViewModels().getDataset(dirName);
		if (originalds != null)
			throw new Exception("此片段下已经存在ID为" + dirName + "的数据集!"); //$NON-NLS-1$ //$NON-NLS-2$
		Dataset ds = new Dataset();
		ds.setId(dirName);
		ds.setCaption(caption);
		TreeItem newdsTreeItem = new LFWDSTreeItem(selTI, ds, ds.getId());
		selTI.setExpanded(true);
		return newdsTreeItem;
	}

	/**
	 * 新增contextMenubar
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addContextMenubarTreeNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(selTI);
		LfwView widget = widgetTreeItem.getWidget();
		ContextMenuComp originalds = widget.getViewMenus().getContextMenu(
				dirName);
		if (originalds != null)
			throw new Exception("此片段下已经存在ID为" + dirName + "的右键菜单!"); //$NON-NLS-1$ //$NON-NLS-2$
		ContextMenuComp contextMenubar = new ContextMenuComp();
		contextMenubar.setId(dirName);
		LFWContextMenuTreeItem contextMenubarTreeItem = new LFWContextMenuTreeItem(
				selTI, 
				LFWTool.getWEBProjConstantValue("CONTEXT_MENUCOMP_ELEMENT"), //$NON-NLS-1$
				contextMenubar);
		selTI.setExpanded(true);
		return contextMenubarTreeItem;
	}

	/**
	 * 增加refdsds节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addRefDSTreeNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录。"); //$NON-NLS-1$
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(selTI);
		LfwView widget = widgetTreeItem.getWidget();
		Dataset originalds = widget.getViewModels().getDataset(dirName);
		if (originalds != null)
			throw new Exception("此片段下已经存在ID为" + dirName + "的数据集!"); //$NON-NLS-1$ //$NON-NLS-2$
		RefDataset refds = new RefDataset();
		refds.setId(dirName);
		TreeItem newdsTreeItem = new LFWDSTreeItem(selTI, refds, refds.getId());
		selTI.setExpanded(true);
		return newdsTreeItem;
	}

	public TreeItem addPoolDSTreeNode(String dirName) throws Exception {
//		List<String> allPooldsIds = LFWPersTool.getAllPoolDsId();
//		if (allPooldsIds.contains(dirName))
//			throw new Exception("已经存在ID为" + dirName + "的公共数据集!");
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		// if (selTIs == null || selTIs.length == 0)
		// throw new Exception("请选中一个节点。");
		TreeItem selTI = selTIs[0];
		String fullName = dirName;
		Dataset ds = new Dataset();
		ds.setId(fullName);
		TreeItem newdsTreeItem = null;
		LFWDirtoryTreeItem rootTreeItem = getPubPoolDsTreeItem();
		if (fullName.indexOf(".") != -1) { //$NON-NLS-1$
			String foldername = fullName.substring(0, fullName.indexOf(".")); //$NON-NLS-1$
			String leftfolder = fullName.substring(fullName.indexOf(".") + 1); //$NON-NLS-1$
			while (leftfolder != null) {
				String newRootPath = rootTreeItem.getFile().getPath().replace(
						"公共数据集", "web/pagemeta/public/dspool"); //$NON-NLS-1$ //$NON-NLS-2$
				File newfile = new File(newRootPath, foldername);
				if (!newfile.exists() || !(newfile.isDirectory())) {
					newfile.mkdir();
				}
				LFWDirtoryTreeItem treeItem = null;
				TreeItem[] childTreeItems = rootTreeItem.getItems();
				for (int i = 0; i < childTreeItems.length; i++) {
					if (childTreeItems[i] instanceof LFWDirtoryTreeItem) {
						LFWDirtoryTreeItem child = (LFWDirtoryTreeItem) childTreeItems[i];
						if (child.getText().equals(foldername)) {
							treeItem = child;
							break;
						}
					}
				}
				if (treeItem == null) {
					treeItem = new LFWDirtoryTreeItem(rootTreeItem, newfile);
					treeItem.setType(LFWDirtoryTreeItem.POOLDSFOLDER);
				}
				rootTreeItem = treeItem;
				if (leftfolder.indexOf(".") != -1) { //$NON-NLS-1$
					foldername = leftfolder.substring(0, leftfolder
							.indexOf(".")); //$NON-NLS-1$
					leftfolder = leftfolder
							.substring(leftfolder.indexOf(".") + 1); //$NON-NLS-1$

				} else {
					newdsTreeItem = new LFWDSTreeItem(rootTreeItem, ds,
							leftfolder);
					leftfolder = null;
				}
			}
		} else
			newdsTreeItem = new LFWDSTreeItem(rootTreeItem, ds, ds.getId());
		selTI.setExpanded(true);
		return newdsTreeItem;
	}

	/**
	 * 得到公共数据集Item
	 * 
	 * @return
	 */
	private LFWDirtoryTreeItem getPubPoolDsTreeItem() {
		TreeItem[] treeItems = treeView.getTree().getItems();
		IProject project = LFWPersTool.getCurrentProject();
		LFWProjectTreeItem projectTreeItem = null;
		for (int i = 0; i < treeItems.length; i++) {
			LFWProjectTreeItem projectItem = (LFWProjectTreeItem) treeItems[i];
			if (projectItem.getProjectModel().getJavaProject().equals(project)) {
				projectTreeItem = projectItem;
				break;
			}
		}
		LFWDirtoryTreeItem nodeTreeItem = null;
		TreeItem[] nodesTreeItems = null;
		LFWBusinessCompnentTreeItem busiCompTreeItem = null;
		if (LFWPersTool.isBCPProject(project)) {
			TreeItem currComp = LFWPersTool.getCurrentBusiCompTreeItem();
			TreeItem[] compTreeItems = projectTreeItem.getItems();
			for (int i = 0; i < compTreeItems.length; i++) {
				TreeItem treeItem = compTreeItems[i];
				if (treeItem.getText().equals(currComp.getText())) {
					busiCompTreeItem = (LFWBusinessCompnentTreeItem) treeItem;
					break;
				}
			}
		}
		if (busiCompTreeItem != null)
			nodesTreeItems = busiCompTreeItem.getItems();
		else
			nodesTreeItems = projectTreeItem.getItems();
		for (int i = 0; i < nodesTreeItems.length; i++) {
			TreeItem treeItem = nodesTreeItems[i];
			if (treeItem.getText().equals(WEBPersConstants.PUBLIC_DATASET)) {
				nodeTreeItem = (LFWDirtoryTreeItem) treeItem;
				break;
			}
		}
		return nodeTreeItem;
	}

	/**
	 * 公共片段
	 * 
	 * @return
	 */
//	private LFWDirtoryTreeItem getPubPoolWidgetTreeItem() {
//		TreeItem[] treeItems = treeView.getTree().getItems();
//		IProject project = LFWPersTool.getCurrentProject();
//		LFWProjectTreeItem projectTreeItem = null;
//		for (int i = 0; i < treeItems.length; i++) {
//			LFWProjectTreeItem projectItem = (LFWProjectTreeItem) treeItems[i];
//			if (projectItem.getProjectModel().getJavaProject().equals(project)) {
//				projectTreeItem = projectItem;
//				break;
//			}
//		}
//		LFWDirtoryTreeItem nodeTreeItem = null;
//		TreeItem[] nodesTreeItems = null;
//		LFWBusinessCompnentTreeItem busiCompTreeItem = null;
//		if (LFWPersTool.isBCPProject(project)) {
//			TreeItem currComp = LFWPersTool.getCurrentBusiCompTreeItem();
//			TreeItem[] compTreeItems = projectTreeItem.getItems();
//			for (int i = 0; i < compTreeItems.length; i++) {
//				TreeItem treeItem = compTreeItems[i];
//				if (treeItem.getText().equals(currComp.getText())) {
//					busiCompTreeItem = (LFWBusinessCompnentTreeItem) treeItem;
//					break;
//				}
//			}
//		}
//		if (busiCompTreeItem != null)
//			nodesTreeItems = busiCompTreeItem.getItems();
//		else
//			nodesTreeItems = projectTreeItem.getItems();
//		for (int i = 0; i < nodesTreeItems.length; i++) {
//			TreeItem treeItem = nodesTreeItems[i];
//			if (treeItem.getText().equals(WEBProjConstants.PUBLIC_WIDGET)) {
//				nodeTreeItem = (LFWDirtoryTreeItem) treeItem;
//				break;
//			}
//		}
//		return nodeTreeItem;
//	}

	private LFWDirtoryTreeItem getPubPoolRefNodeTreeItem() {
		TreeItem[] treeItems = treeView.getTree().getItems();
		IProject project = LFWPersTool.getCurrentProject();
		LFWProjectTreeItem projectTreeItem = null;
		for (int i = 0; i < treeItems.length; i++) {
			LFWProjectTreeItem projectItem = (LFWProjectTreeItem) treeItems[i];
			if (projectItem.getProjectModel().getJavaProject().equals(project)) {
				projectTreeItem = projectItem;
				break;
			}
		}
		LFWDirtoryTreeItem nodeTreeItem = null;
		TreeItem[] nodesTreeItems = null;
		LFWBusinessCompnentTreeItem busiCompTreeItem = null;
		if (LFWPersTool.isBCPProject(project)) {
			TreeItem currComp = LFWPersTool.getCurrentBusiCompTreeItem();
			TreeItem[] compTreeItems = projectTreeItem.getItems();
			for (int i = 0; i < compTreeItems.length; i++) {
				TreeItem treeItem = compTreeItems[i];
				if (treeItem.getText().equals(currComp.getText())) {
					busiCompTreeItem = (LFWBusinessCompnentTreeItem) treeItem;
					break;
				}
			}
		}
		if (busiCompTreeItem != null)
			nodesTreeItems = busiCompTreeItem.getItems();
		else
			nodesTreeItems = projectTreeItem.getItems();
		for (int i = 0; i < nodesTreeItems.length; i++) {
			TreeItem treeItem = nodesTreeItems[i];
			if (treeItem.getText().equals(WEBPersConstants.PUBLIC_REFNODE)) {
				nodeTreeItem = (LFWDirtoryTreeItem) treeItem;
				break;
			}
		}
		return nodeTreeItem;
	}

	/**
	 * 增加公共池中的参照节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addPoolRefNode(String dirName) throws Exception {
		List<String> allPoolrefNodeIds = LFWPersTool.getAllPoolRefNodeId();
		if (allPoolrefNodeIds.contains(dirName))
			throw new Exception("已经存在ID为" + dirName + "的公共参照!"); //$NON-NLS-1$ //$NON-NLS-2$
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		TreeItem selTI = selTIs[0];
		String fullName = dirName;
		BaseRefNode refnode = new BaseRefNode();
		refnode.setId(fullName);
		TreeItem newdrefnodeTreeItem = null;
		// 公告参照节点
		LFWDirtoryTreeItem rootTreeItem = getPubPoolRefNodeTreeItem();
		if (fullName.indexOf(".") != -1) { //$NON-NLS-1$
			String foldername = fullName.substring(0, fullName.indexOf(".")); //$NON-NLS-1$
			String leftfolder = fullName.substring(fullName.indexOf(".") + 1); //$NON-NLS-1$
			while (leftfolder != null) {
				String newRootPath = rootTreeItem.getFile().getPath().replace(
						"公共参照", "web/pagemeta/public/refnodes"); //$NON-NLS-1$ //$NON-NLS-2$
				File newfile = new File(newRootPath, foldername);
				if (!newfile.exists() || !(newfile.isDirectory())) {
					newfile.mkdir();
				}
				LFWDirtoryTreeItem treeItem = null;
				TreeItem[] childTreeItems = rootTreeItem.getItems();
				for (int i = 0; i < childTreeItems.length; i++) {
					if (childTreeItems[i] instanceof LFWDirtoryTreeItem) {
						LFWDirtoryTreeItem child = (LFWDirtoryTreeItem) childTreeItems[i];
						if (child.getText().equals(foldername)) {
							treeItem = child;
							break;
						}
					}
				}
				if (treeItem == null) {
					treeItem = new LFWDirtoryTreeItem(rootTreeItem, newfile);
					treeItem.setType(LFWDirtoryTreeItem.POOLREFNODEFOLDER);
				}
				rootTreeItem = treeItem;
				if (leftfolder.indexOf(".") != -1) { //$NON-NLS-1$
					foldername = leftfolder.substring(0, leftfolder
							.indexOf(".")); //$NON-NLS-1$
					leftfolder = leftfolder
							.substring(leftfolder.indexOf(".") + 1); //$NON-NLS-1$

				} else {
					newdrefnodeTreeItem = new LFWRefNodeTreeItem(rootTreeItem,
							refnode, WEBPersConstants.COMPONENT_REFNODE);
					// new LFWRefNodeTreeItem(rootTreeItem, refnode,
					// leftfolder);
					leftfolder = null;
				}
			}
		} else
			newdrefnodeTreeItem = new LFWRefNodeTreeItem(rootTreeItem, refnode,
					WEBPersConstants.COMPONENT_REFNODE);
		// newdrefnodeTreeItem = new LFWRefNodeTreeItem(rootTreeItem,refnode,
		// refnode.getId());
		selTI.setExpanded(true);
		return newdrefnodeTreeItem;

		// Tree tree = treeView.getTree();
		// TreeItem[] selTIs = tree.getSelection();
		// if (selTIs == null || selTIs.length == 0)
		// throw new Exception("请选中一个节点!");
		// TreeItem selTI = selTIs[0];
		// TreeItem origiranlTI = selTI;
		// File parentFile = ((ILFWTreeNode) selTI).getFile();
		// if (parentFile.isFile())
		// throw new Exception("不能在文件下新建目录!");
		// TreeItem[] treeItems = selTI.getItems();
		// List<String> refNames = new ArrayList<String>();
		// for (int i = 0; i < treeItems.length; i++) {
		// TreeItem treeItem = treeItems[i];
		// if(treeItem instanceof LFWRefNodeTreeItem){
		// RefNodeConf ref = (RefNodeConf) treeItem.getData();
		// refNames.add(ref.getId());
		// }
		// }
		//		
		// String fullName = dirName;
		//		
		// if(selTI instanceof LFWDirtoryTreeItem){
		// LFWDirtoryTreeItem dir = (LFWDirtoryTreeItem) selTI;
		// if(!dir.getType().equals(LFWDirtoryTreeItem.PARENT_PUB_REF_FOLDER)){
		// fullName = selTI.getText() + "." + dirName;
		// while (selTI.getParentItem() != null && (selTI instanceof
		// LFWDirtoryTreeItem)) {
		// LFWDirtoryTreeItem parent =
		// (LFWDirtoryTreeItem)selTI.getParentItem();
		// if ((parent.getText().equals(NODEGROUP_CN) ||
		// parent.getText().equals(PUBLIC_REFNODE)))
		// break;
		// fullName = parent.getFile().getName() + "." + fullName;
		// selTI = parent;
		// }
		// }
		// }
		// if(refNames.contains(fullName))
		// throw new Exception("此目录下已经存在在ID为" + dirName + "的参照!");
		// RefNode refnode = new RefNode();
		// refnode.setId(fullName);
		// LFWRefNodeTreeItem newTreeItem = new LFWRefNodeTreeItem(origiranlTI,
		// refnode, "[参照]");
		// selTI.setExpanded(true);
		// return newTreeItem;
	}

	/**
	 * 增加View - Control 节点下子节点
	 * @param dirName
	 * @param webComponent
	 * @param type
	 * @return
	 */
	public TreeItem addControlChildTreeNode(String dirName, WebComponent webComponent,String constantName, String type) throws Exception{
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0){
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		}
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		if (parentFile.isFile()){
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		}
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(selTI);
		LfwView widget = widgetTreeItem.getWidget();
		WebComponent oriWebCom = widget.getViewComponents().getComponent(dirName);
		if (oriWebCom != null){
			throw new Exception("此片段下已经存在ID为" + dirName + "组件!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		webComponent.setId(dirName);
		LFWWebComponentTreeItem webCompTreeItem = new LFWWebComponentTreeItem(
				selTI, 
				LFWTool.getWEBProjConstantValue(constantName), 
				webComponent);
		webCompTreeItem.setType(type);
		selTI.setExpanded(true);
		return webCompTreeItem;
	}
	
	/**
	 * 增加节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addGridTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new GridComp(), "COMPONENT_GRID", LFWWebComponentTreeItem.PARAM_GRID); //$NON-NLS-1$
	}

	/**
	 * 新建excel树节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
//	public TreeItem addExcelTreeNode(String dirName) throws Exception {
//		return addControlChildTreeNode(dirName, new ExcelComp(), "COMPONENT_EXCEL", LFWWebComponentTreeItem.PARAM_EXCEL);
//	}

	public TreeItem addButtonTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new ButtonComp(), "COMPONENT_BUTTON", LFWWebComponentTreeItem.PARAM_BUTTON); //$NON-NLS-1$
	}

	public TreeItem addChartTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new ChartComp(), "COMPONENT_CHART", LFWWebComponentTreeItem.PARAM_CHART); //$NON-NLS-1$
	}

	public TreeItem addSelfdefTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new SelfDefComp(), "COMPONENT_SELFDEFCOMP", LFWWebComponentTreeItem.PARAM_SELFDEF); //$NON-NLS-1$
	}

	public TreeItem addProgressBarTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new ProgressBarComp(), "COMPONENT_PROGRESSBAR", LFWWebComponentTreeItem.PARAM_PROGRESSBAR); //$NON-NLS-1$
	}

	public TreeItem addLabelTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new LabelComp(), "COMPONENT_LABEL", LFWWebComponentTreeItem.PARAM_LABEL); //$NON-NLS-1$
	}

	public TreeItem addLinkTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new LinkComp(), "COMPONENT_LINKCOMP", LFWWebComponentTreeItem.PARAM_LINK); //$NON-NLS-1$
	}

	public TreeItem addImageTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new ImageComp(), "COMPONENT_IMAGE", LFWWebComponentTreeItem.PARAM_IMAGE); //$NON-NLS-1$
	}

	public TreeItem addImageButtonTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new ImageButtonComp(), "COMPONENT_IMAGE", LFWWebComponentTreeItem.PARAM_IMAGEBUTTON); //$NON-NLS-1$
	}

	public TreeItem addIFrameTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new IFrameComp(), "COMPONENT_IFRAMECOMP", LFWWebComponentTreeItem.PARAM_IFRAME); //$NON-NLS-1$
	}

	/**
	 * 增加Form节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addFormTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new FormComp(), "COMPONENT_FORM", LFWWebComponentTreeItem.PARAM_FORM); //$NON-NLS-1$
	}

	public TreeItem addToolbarTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new ToolBarComp(), "COMPONENT_TOOLBAR", LFWWebComponentTreeItem.PARAM_TOOLBAR); //$NON-NLS-1$
	}

	public TreeItem addTextCompTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new TextComp(), "COMPONENT_TEXTCOMP", LFWWebComponentTreeItem.PARAM_TEXTCOMP); //$NON-NLS-1$
	}

	/**
	 * 新建treen节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addTreeNode(String dirName) throws Exception {
		return addControlChildTreeNode(dirName, new TreeViewComp(), "COMPONENT_TREE", LFWWebComponentTreeItem.PARAM_TREE); //$NON-NLS-1$
	}

	/**
	 * 新建下拉数据节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addComboNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(selTI);
		LfwView widget = widgetTreeItem.getWidget();
		ComboData oriComboData = widget.getViewModels().getComboData(dirName);
		if (oriComboData != null)
			throw new Exception("此片段下已经存在ID为" + dirName + "下拉数据!"); //$NON-NLS-1$ //$NON-NLS-2$
		ComboData staticcombo = new StaticComboData();
		staticcombo.setId(dirName);
		LFWComboTreeItem newTreeItem = new LFWComboTreeItem(selTI, staticcombo,
				LFWTool.getWEBProjConstantValue("COMPONENT_COMBODATA")); //$NON-NLS-1$
		selTI.setExpanded(true);
		return newTreeItem;
	}

	/**
	 * 增加参照节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addRefNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(selTI);
		LfwView widget = widgetTreeItem.getWidget();
		IRefNode originalrefnode = widget.getViewModels().getRefNode(dirName);
		if (originalrefnode != null)
			throw new Exception("此片段下已经存在名字为" + dirName + "的参照!"); //$NON-NLS-1$ //$NON-NLS-2$
		SelfDefRefNode refnode = new SelfDefRefNode();
		refnode.setId(dirName);
		// 新建时给refnode设置默认pagemeta
//		refnode.setPagemeta(WEBProjConstants.REFNODE_PAGEMETA);
		LFWRefNodeTreeItem newTreeItem = new LFWRefNodeTreeItem(selTI, refnode,
				LFWTool.getWEBProjConstantValue("COMPONENT_REFNODE")); //$NON-NLS-1$
		selTI.setExpanded(true);
		return newTreeItem;
	}

	/**
	 * 增加参照关系
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addRefNodeRelation(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建目录!"); //$NON-NLS-1$
		LFWWidgetTreeItem widgetTreeItem = LFWPersTool.getWidgetTreeItem(selTI);
		LfwView widget = widgetTreeItem.getWidget();
		if (widget.getViewModels().getRefNodeRelations() == null) {
			RefNodeRelations refs = new RefNodeRelations();
			widget.getViewModels().setRefnodeRelations(refs);
		}
		RefNodeRelation refnoderel = widget.getViewModels()
				.getRefNodeRelations().getRefNodeRelation(dirName);
		if (refnoderel != null) {
			throw new Exception("此片段下已经存在名字为" + dirName + "的参照!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		RefNodeRelation refNodeRel = new RefNodeRelation();
		refNodeRel.setId(dirName);
		LFWRefNodeRelTreeItem newTreeItem = new LFWRefNodeRelTreeItem(selTI, refNodeRel, 
				LFWTool.getWEBProjConstantValue("COMPONENT_REFNODERELATION")); //$NON-NLS-1$
		selTI.setExpanded(true);
		return newTreeItem;
	}

	/**
	 * 增加widgets树节点
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public TreeItem addWidgetsTreeNode(String dirName) throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		File parentFile = ((ILFWTreeNode) selTI).getFile();// (File)
		// selTI.getData();
		if (parentFile.isFile())
			throw new Exception("不能在文件下新建Widgets!"); //$NON-NLS-1$
		File f = new File(parentFile, dirName);
		if (!f.exists()) {
			f.mkdirs();
		}
		selTI.setExpanded(true);
		// return ti;
		return null;
	}

	public void deleteSelectedTreeNode() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		if (selTI instanceof ILFWTreeNode) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, "确认", "确实要删除" //$NON-NLS-1$ //$NON-NLS-2$
					+ selTI.getText() + "吗？")) //$NON-NLS-1$
				return;
			if (selTI instanceof LFWPageMetaTreeItem) {
				LFWPageMetaTreeItem pmTreeItem = (LFWPageMetaTreeItem) selTI;
				LfwWindow pm = (LfwWindow) pmTreeItem.getPm();
				if (pm == null)
					return;
//				if (pm.getExtendAttribute(ExtAttrConstants.FUNC_CODE) != null
//						&& pm.getExtendAttribute(ExtAttrConstants.FUNC_CODE)
//								.getValue() != null) {
//					String funnode = (String) pm.getExtendAttribute(
//							ExtAttrConstants.FUNC_CODE).getValue();
//					NCConnector.deleteFunNode(funnode);
//				}
			} else if (selTI instanceof LFWVirtualDirTreeItem) {
				TreeItem[] treeItems = selTI.getItems();
				if (treeItems != null && treeItems.length > 0) {
					MessageDialog.openError(null, "提示", "此虚拟目录下存在其他信息,不能删除!"); //$NON-NLS-1$ //$NON-NLS-2$
					return;
				}
			}
			((ILFWTreeNode) selTI).deleteNode();
		}
		closeOpenedEidtor(selTI);

	}

	/**
	 * 删除选中ds
	 * 
	 * @throws Exception
	 */
	public void deleteSelectedDsNode() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		if (selTI instanceof LFWDSTreeItem) {
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, "确认", "确实要删除" //$NON-NLS-1$ //$NON-NLS-2$
					+ selTI.getText() + "吗？")) //$NON-NLS-1$
				return;
			// closeOpenedEidtor(selTI);
			((LFWDSTreeItem) selTI).deleteNode((LFWDSTreeItem) selTI);
		}
		closeOpenedEidtor(selTI);
	}

	protected void closeOpenedEidtor(TreeItem treeItem) {
		// 删除节点后关闭编辑器
		IWorkbenchPage page = getViewSite().getPage();
		IEditorInput input = ((LFWBasicTreeItem) treeItem).getEditorInput();
		if (null != input) {
			IEditorPart editor = page.findEditor(input);
			if (null != editor) {
				page.closeEditor(editor, false);
			}
		}
	}

	/**
	 * 删除新增时增加的节点
	 * 
	 * @throws Exception
	 */
	public void deleteNewNode() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("没有可以删除的节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		closeOpenedEidtor(selTI);
		selTI.dispose();
	}

	/**
	 * 删除选中node
	 * 
	 * @throws Exception
	 */
	public void deleteSelectedWebComponentNode() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点。"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		if (selTI instanceof LFWWebComponentTreeItem) {
			// if(lfwbasic.getType().equals(LFWWebComponentTreeItem.PARAM_GRID)){
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, "确认", "确实要删除" //$NON-NLS-1$ //$NON-NLS-2$
					+ selTI.getText() + "吗？")) //$NON-NLS-1$
				return;
			// closeOpenedEidtor(selTI);
			((LFWWebComponentTreeItem) selTI)
					.deleteNode((LFWWebComponentTreeItem) selTI);
			// }
		}
		closeOpenedEidtor(selTI);

	}

	public void deleteSelectedRefNode() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		if (selTI instanceof LFWRefNodeTreeItem) {
			// LFWRefNodeTreeItem refTreeItem = (LFWRefNodeTreeItem)selTI;
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, "确认", "确实要删除" //$NON-NLS-1$ //$NON-NLS-2$
					+ selTI.getText() + "吗？")) //$NON-NLS-1$
				return;
			((LFWRefNodeTreeItem) selTI).deleteNode((LFWRefNodeTreeItem) selTI);
			// }
		}
		closeOpenedEidtor(selTI);
	}

	public void deleteSelectedRefNodeRel() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		if (selTI instanceof LFWRefNodeRelTreeItem) {
			// LFWRefNodeTreeItem refTreeItem = (LFWRefNodeTreeItem)selTI;
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, "确认", "确实要删除" //$NON-NLS-1$ //$NON-NLS-2$
					+ selTI.getText() + "吗？")) //$NON-NLS-1$
				return;
			((LFWRefNodeRelTreeItem) selTI)
					.deleteNode((LFWRefNodeRelTreeItem) selTI);
			// }
		}
		closeOpenedEidtor(selTI);
	}

	public void deleteSelectedComboNode() throws Exception {
		Tree tree = treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		if (selTIs == null || selTIs.length == 0)
			throw new Exception("请选中一个节点!"); //$NON-NLS-1$
		TreeItem selTI = selTIs[0];
		if (selTI instanceof LFWComboTreeItem) {
			// LFWComboTreeItem comboTreeItem = (LFWComboTreeItem)selTI;
			Shell shell = new Shell(Display.getCurrent());
			if (!MessageDialog.openConfirm(shell, "确认", "确实要删除" //$NON-NLS-1$ //$NON-NLS-2$
					+ selTI.getText() + "吗？")) //$NON-NLS-1$
				return;
			((LFWComboTreeItem) selTI).deleteNode((LFWComboTreeItem) selTI);
			// }
		}
		closeOpenedEidtor(selTI);
	}

	protected void initTree() {
		LFWExplorerTreeBuilder.getInstance().buildTree(treeView.getTree());
	}

	public void refreshTree() {
		Tree tree = treeView.getTree();
		tree.removeAll();
//		createTreeView();
		initTree();
	}
	private void createTreeView() {
		if(treeView != null){
			TreeItem[] items = treeView.getTree().getItems();
			disposeItems(items);
		}
		Tree tree = treeView.getTree();
		tree.removeAll();
	}

	private void disposeItems(TreeItem[] items) {
		if(items == null)
			return;
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];
			disposeItems(item.getItems());
			Image img = item.getImage();
			if(img != null){
//				System.out.println("-------------------------------+" + item.getText());
				img.dispose();
			}
		}
	}

	public Tree getExplorerTree() {
		return treeView.getTree();
	}

	private static final String LFW_LABEL = M_perspective.LFWExplorerTreeView_0;
	private static final String PORTAL_LABEL = M_perspective.LFWExplorerTreeView_1;
	
	public static LFWExplorerTreeView getLFWExploerTreeView(IWorkbenchPage page) {
		if (page == null) {
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage() != null) {
				page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage();
			}
		}
		if (page != null) {
			LFWExplorerTreeView view = (LFWExplorerTreeView) page
					.findView(LFWExplorerTreeView.class.getCanonicalName());
			// 取继承自 LFWExplorerTreeView 的view
			if (view == null && LFWExplorerTreeView.EXTEND_VIEWID != null) {
				view = (LFWExplorerTreeView) page
						.findView(LFWExplorerTreeView.EXTEND_VIEWID);
			}
			if (view == null) {
				if(LFW_LABEL.equals(page.getLabel()) || PORTAL_LABEL.equals(page.getLabel())){
					try {
						view = (LFWExplorerTreeView) page
								.showView(LFWExplorerTreeView.class
										.getCanonicalName());
					} catch (PartInitException e) {
						// e.printStackTrace();
					}
				}
			}
			return view;
		} else
			return null;
	}

	public TreeViewer getTreeView() {
		return treeView;
	}

	public Map<String, String> getEditoridMap() {
		return editoridMap;
	}
}
