package uap.workflow.modeler;


import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.modeler.uecomponent.BpmnMenuBar;
import uap.workflow.modeler.utils.CreateElementUtils;
//import nc.ui.pub.flowdesigner.lookandfeel.PfOffice2003LookAndFeel;
import nc.vo.jcom.lang.StringUtil;


public class BpmnModelerStarter {

	private static Map<String, JFrame> map = new HashMap<String, JFrame>();
	
	private static final String  BANDEDITOR="blankeditor";
	
	protected  String processId=null;
	
	/**
	 * @param pk_busiOjbect
	 * @param container
	 */
	public void startModeler( String pkProcDef, final Container container) {
	JFrame frame =null;
		boolean isnewframe =false;
//		customLookAndFeel();
		final String key =StringUtil.isEmptyWithTrim(pkProcDef)?BANDEDITOR:pkProcDef;
		if (map.containsKey(key)) { // 编辑器已实例化，从map中获取其实例
			frame = map.get(key);
		} else { // 实例化编辑器，放入缓存map中
			isnewframe = true;
			BpmnModeler editor = new BpmnModeler(pkProcDef);    //流程设计器完整版
			//BpmnModeler editor = new BpmnModeler("ApproveModeler",pkProcDef);       //流程设计器审批版
			//frame = (editor.createFrame(container,new BpmnMenuBar(editor.getGraphComponent())));     //原版 菜单栏与主体分属两个panel的界面
			BpmnEditorPanel editorPanel= new BpmnEditorPanel(editor,new BpmnMenuBar(editor.getGraphComponent()));
			frame = editorPanel.createFrame(container);
			map.put(key, frame);
			editor.showGraph();
		}	
		frame.setVisible(true);
		if(isnewframe){
			//关闭时候清除映射在map的数据
			frame.addWindowListener(new WindowAdapter() {
				 public void windowClosed(WindowEvent e) {
					 map.remove(key);
					 if(container instanceof WorkflowsManager){
						//刷新列表
						((WorkflowsManager)container).onBtnRefresh();
						// 解锁
						List<String> lockedProcessPKs = ((WorkflowsManager)container).getLockedProcessPKs();
						for (Iterator<String> iter = lockedProcessPKs.iterator(); iter.hasNext();) {
							String lockedProcessPK = (String) iter.next();
							((WorkflowsManager)container).freeProcessPK(lockedProcessPK);
						}
						((WorkflowsManager)container).getLockedProcessPKs().remove(key);
					 }
				 }
			});
		}
		
	}
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
//	private void customLookAndFeel(){
//		try {
//			UIManager.setLookAndFeel(new PfOffice2003LookAndFeel("NC"));
//		} catch (Exception e) {
//			Logger.error(e.getMessage(),e);
//		}
//	}

}
