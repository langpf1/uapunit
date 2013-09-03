/**
 * 
 */
package uap.workflow.wfdplugin;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.menus.UIElement;

import sun.misc.BASE64Encoder;
import uap.workflow.wfdplugin.editor.LFWBrowserEditor;
import uap.workflow.wfdplugin.editor.PublicViewBrowserEditor;
import uap.workflow.wfdplugin.editor.ViewBrowserEditor;
import uap.workflow.wfdplugin.editor.WindowBrowserEditor;

/**
 * @author chouhl
 * 2011-11-9
 */
public class MozillaBrowser{
	
	public final static int WINDOW_URL = 1;
	
	public final static int VIEW_URL = 2;
	
	public final static int PUBLIC_VIEW_URL = 3;
	
	private Browser browser = null;
	
	private int type = 0;
	
	private MozillaBrowser mozilla = null;
	
	private String sessionId = null;
	
	private TreeItem treeItem = null;
	
	private LFWBrowserEditor editor = null;
	
	private String windowId = null;
	
	public MozillaBrowser(Composite parent, int type){
		browser = new Browser(parent, SWT.MOZILLA);
		this.type = type;
		mozilla = this;
	}
	
	public void createBrowser(){
		browser.setUrl(createURL(type));
		browser.addStatusTextListener(new StatusTextListener(){
			@Override
			public void changed(StatusTextEvent event) {
				if(event.text != null){
					EditorEvent ent = IEEventTranslateToolKit.decode(event.text);
					if(ent != null){
						try {
							Method method = MozillaBrowser.class.getMethod(ent.getEventType(), EditorEvent.class);
							method.invoke(mozilla, ent);
						} catch (Exception e) {
							//MainPlugin.getDefault().logError(e);
						}
					}
					execute("clearEventStatus();");
				}
			}
		});
		browser.addCloseWindowListener(new CloseWindowListener(){
			@Override
			public void close(WindowEvent event) {
				System.out.println(event.data);
			}
		});
	}
	
	public void execute(String script){
		browser.execute(script);
	}

	private String createURL(int type){
		StringBuffer sURL = new StringBuffer();
		//String ctx = LfwCommonTool.getLfwProjectCtx(LFWAMCPersTool.getCurrentProject());
		//String currentTIPath = LFWAMCPersTool.getCurrentFolderPath();
		sURL.append("http://localhost:8080/wfd/index.html");
//		sURL.append("http://ufpark/");
		//sURL.append("model=nc.uap.lfw.pa.PaEditorPageModel");
		//sURL.append("&eclipse=1");
		//sURL.append("&ModePhase=" + ModePhase.eclipse);

		//sURL.append("&projectName=" + ctx);
		//sURL.append("&pagemetaPath=" + currentTIPath);
		switch(type){
			case VIEW_URL:
				/*
				LfwWindow window = LFWAMCPersTool.getCurrentPageMeta();
				if(window.getComponentId() == null||window.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
					windowId = window.getId();
				else windowId = window.getComponentId() + "." + window.getId();
				String viewId = LFWAMCPersTool.getCurrentWidget().getId();
				sURL.append("&windowId=" + windowId);
				sURL.append("&viewId=" + viewId);
				*/
				break;
			case PUBLIC_VIEW_URL:
				/*
				windowId = WEBPersConstants.DEFAULT_WINDOW_ID;
				viewId = LFWAMCPersTool.getCurrentWidget().getId();
				sURL.append("&windowId=" + windowId);
				sURL.append("&viewId=" + viewId);
				*/
				break;
			case WINDOW_URL:
				/*
				window = LFWAMCPersTool.getCurrentPageMeta();
				if(window.getComponentId() == null||window.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
					windowId = window.getId();
				else windowId = window.getComponentId() + "." + window.getId();
				sURL.append("&windowId=" + windowId);
				*/
				break;
			default:
				break;
		}
		//sURL.append("&ms=" + System.currentTimeMillis());
		//sURL.append("&lfw_enterParam="+URLEncoder.encode(md5encoder("yonyou2012"+timeStr())));
		String url = sURL.toString();
		//MainPlugin.getDefault().logInfo(url);
		return url;
	}
	
	/**
	 * UI���ҳ���޸�
	 * @param event
	 */
	public void changeFileStatus(EditorEvent event){
		editor.setDirtyTrue();
		//updateElement();
	}
	/*
	private void updateElement(){
		Map<String, Object> map = LFWAMCPersTool.getElementFromSession(editor.getPageMetaId(), sessionId);
		if(editor instanceof ViewBrowserEditor || editor instanceof PublicViewBrowserEditor){
			LfwView view = LFWAMCPersTool.getViewFromSession(editor.getWidgetId(), editor.getPageMetaId(), sessionId, map);
			EventConf[] events = ViewEventTool.getAllEvents(view, null);
			if(events != null && events.length > 0){
				for(EventConf ec : events){
					if(ec.getEventStatus() == EventConf.ADD_STATUS){
						ec.setEventStatus(EventConf.NORMAL_STATUS);
					}else if(ec.getEventStatus() == EventConf.DEL_STATUS){
						ViewEventTool.removeEvent(view, ec.getName(), ec.getMethodName());
					}
				}
			}
			editor.setView(view);
			
			UIMeta uimeta = LFWAMCPersTool.getWidgetUIMetaFromSession(editor.getPageMetaId(), editor.getWidgetId(), sessionId, map);
			operateEvent(uimeta);
			Iterator<String> keys = editor.getElementMap().keySet().iterator();
			while(keys.hasNext()){
				UIElement element = uimeta.findChildById(keys.next());
				if(element != null){
					operateEvent(element);
				}
			}
			LFWTool.mergeUIMetaEvent(uimeta, editor.getElementMap());
			editor.setUimeta(uimeta);
		}
	}
	*/
	
	/**
	 * ui���ҳ�汣��
	 * @param event
	 */
	public void changeSaveStatus(EditorEvent event){
		changeSaveStatus();
	}
	
	public void changeSaveStatus(){
		try {
			editor.setDirtyFalse();
			/*
			LFWAMCPersTool.getCurrentProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			if(treeItem instanceof LFWPageMetaTreeItem){
				LFWExplorerTreeView.getLFWExploerTreeView(null).refreshDirtoryTreeItem((LFWPageMetaTreeItem)treeItem);
			}else if(treeItem instanceof LFWDirtoryTreeItem){
				RefreshPublicViewNodeAction refreshAMCNodeGroupAction = new RefreshPublicViewNodeAction((LFWDirtoryTreeItem)treeItem, ((LFWDirtoryTreeItem)treeItem).getFile(), LFWAMCPersTool.getCurrentProject());
				refreshAMCNodeGroupAction.run();
			}
			LFWBrowserEditor browserEditor = null;
			if(editor instanceof ViewBrowserEditor){
				browserEditor = (ViewBrowserEditor)editor;
			}else if(editor instanceof PublicViewBrowserEditor){
				browserEditor = (PublicViewBrowserEditor)editor;
			}
			if(browserEditor != null){
				LfwView viewConf = browserEditor.getView();
				EventConf[] events = ViewEventTool.getAllEvents(viewConf, null);
				if(events != null && events.length > 0){
					for(EventConf ec : events){
						if(ec.getEventStatus() == EventConf.ADD_STATUS){
							ec.setEventStatus(EventConf.NORMAL_STATUS);
						}else if(ec.getEventStatus() == EventConf.DEL_STATUS){
							ViewEventTool.removeEvent(viewConf, ec.getName(), ec.getMethodName());
						}
					}
				}
				UIMeta meta = browserEditor.getUimeta();
				operateEvent(meta);
				Iterator<String> keys = editor.getElementMap().keySet().iterator();
				while(keys.hasNext()){
					UIElement element = meta.findChildById(keys.next());
					if(element != null){
						operateEvent(element);
					}
				}
			}
			*/
		} catch (Exception e) {
			//MainPlugin.getDefault().logError(e.getMessage(), e);
		}
	}
	/*
	private void operateEvent(UIElement element){
		EventConf[] events = ViewEventTool.getAllEvents(null, element);
		if(events != null && events.length > 0){
			List<EventConf> list = new ArrayList<EventConf>();
			for(EventConf ec : events){
				if(ec.getEventStatus() == EventConf.ADD_STATUS){
					ec.setEventStatus(EventConf.NORMAL_STATUS);
					list.add(ec);
				}else if(ec.getEventStatus() == EventConf.NORMAL_STATUS){
					list.add(ec);
				}
			}
			element.removeAllEventConf();
			for(EventConf event : list){
				element.addEventConf(event);
			}
		}
	}
	*/
	/**
	 * ui���ҳ��sessionid
	 * @param event
	 */
	public void setSessionId(EditorEvent event){
		this.sessionId = event.getSource().get("sessionID");
	}
	
	/**
	 * ui���ҳ����ʾLFW������ͼ
	 * @param event
	 */
	public void showPropertiesView(EditorEvent event){
		//v61��ʾ�¼�
 		if(!(editor instanceof WindowBrowserEditor)){
 			/*
			LfwView widget = editor.getView();
			UIMeta uimeta = editor.getUimeta();
			if(widget != null){
				WebElement we = ViewEventTool.getWebElementById(widget, event.getSource().get("eleid"));
				if(we != null){//�ؼ�Ԫ��
//					LFWAMCPersTool.showView(LFWTool.ID_LFW_VIEW_SHEET);
					if(editor.getViewPage() != null){
						editor.getViewPage().setUiElement(null);
						editor.getViewPage().setUimeta(uimeta);
						editor.getViewPage().setWebElement(we);
//						EventConf[] confs = we.getEventConfs();
						editor.getViewPage().addEventPropertiesView(we.getEventConfs(), widget.getControllerClazz());
					}
				}else{
					if(uimeta != null){						
						UIElement child = uimeta.findChildById(event.getSource().get("uiid"));
						UIElement outlook = null;
						String outlookid = null;
						if(event.getSource().get("id") != null && event.getSource().get("id").length() >= 4){
							outlookid = "outlookbar" + event.getSource().get("id").substring(event.getSource().get("id").length() - 4);
							outlook = uimeta.findChildById(outlookid);
						}
						if(child != null){//����Ԫ��
							EventConf[] events = child.getEventConfs();
//							LFWAMCPersTool.showView(LFWTool.ID_LFW_VIEW_SHEET);
							if(editor.getViewPage() != null){
								editor.getViewPage().setWebElement(null);
								editor.getViewPage().setUimeta(uimeta);
								editor.getViewPage().setUiElement(child);
								editor.getElementMap().put(event.getSource().get("uiid"), child);
								editor.getViewPage().addEventPropertiesView(events, widget.getControllerClazz());
							}
						}else if(outlook != null){
							EventConf[] events = outlook.getEventConfs();
//							LFWAMCPersTool.showView(LFWTool.ID_LFW_VIEW_SHEET);
							if(editor.getViewPage() != null){
								editor.getViewPage().setWebElement(null);
								editor.getViewPage().setUimeta(uimeta);
								editor.getViewPage().setUiElement(outlook);
								editor.getElementMap().put(outlookid, outlook);
								editor.getViewPage().addEventPropertiesView(events, widget.getControllerClazz());
							}
						}else{
							if(editor.getViewPage() != null){
								editor.getViewPage().setWebElement(null);
								editor.getViewPage().setUimeta(null);
								editor.getViewPage().setUiElement(null);					
								editor.getViewPage().addEventPropertiesView(null, widget.getControllerClazz());
							}
						}
					}else{
						MainPlugin.getDefault().logError("UIMeta����Ϊ��!");
					}
				}
			}else{
				MainPlugin.getDefault().logError("Widget����Ϊ��!");
			}
			*/
		}
	}
	
	/**
	 * UI���ҳ���޸Ŀؼ�ID
	 * @param event
	 */
	public void triggerChangerId(EditorEvent event){
		/*
		ViewComponents vc = editor.getView().getViewComponents();
		WebComponent comp = vc.getComponent(event.getSource().get("oldId"));
		if(comp != null){
			comp.setId(event.getSource().get("newId"));
			vc.removeComponent(event.getSource().get("oldId"));
			vc.addComponent(comp);
		}
		*/
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void setEditor(LFWBrowserEditor editor) {
		this.editor = editor;
	}
	public String md5encoder(String str)
	{
		MessageDigest md5 = null;
		String value = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64Encoder = new BASE64Encoder();
			value = base64Encoder.encode(md5.digest(str.getBytes("UTF-8")));
		}catch(Exception e){
			
		}
		return value;
	}
	public String timeStr(){
		Date date = new Date();
		long time = date.getTime();
		String dateline = time+"";
		dateline = dateline.substring(0,6);
		return dateline;		
	}
	
}
