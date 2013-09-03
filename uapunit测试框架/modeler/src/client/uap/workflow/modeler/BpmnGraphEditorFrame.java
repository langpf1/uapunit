package uap.workflow.modeler;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;

/*
 * @modifier yanke1 2011-3-23 �������˳��༭��ʱ��ʾ�Ƿ񱣴�Ĺ���
 */
public class BpmnGraphEditorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private BasicBpmnGraphEditor editor = null;
	private Container container =null;
	private BpmnEditorPanel mainPanel;
	public BpmnGraphEditorFrame(Container container,final Component componet, JMenuBar menuBar){
		this.container =container;
		if (componet instanceof BasicBpmnGraphEditor) {
			this.editor = (BasicBpmnGraphEditor) componet;
		}
		getContentPane().add(componet);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setJMenuBar(menuBar);
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 20);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 30);
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
		setTitle(NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditorFrame-000000")/*���̱༭��*/);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// ��editor�з����˸Ķ�����δ���棬����ʾ�Ƿ񱣴�����Ƴ�
				if (editor != null && editor.isModified()) {
					int result = MessageDialog.showYesNoCancelDlg(null, null,
							NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditorFrame-000001")/*���̶�����δ���棬�Ƿ�Ҫ����?*/);					
					switch (result) {
					case MessageDialog.ID_YES:
						editor.onSave();
						dispose();
						break;
					case MessageDialog.ID_NO:
						dispose();
						break;
					case MessageDialog.ID_CANCEL:
						break;
					default:
						break;
					}
				} else { // editor��δ�����Ķ���ֱ����ʾ�Ƿ�ȷ��
					if (MessageDialog.showOkCancelDlg(null, NCLangRes.getInstance().getStrByID("pfgraph", "validation")/*ȷ��*/, NCLangRes.getInstance().getStrByID("pfgraph", "EditorActions-000000")/*�Ƿ�ȷ�Ϲرձ༭��?*/) == MessageDialog.ID_OK) {
						dispose();
					}
				}

			}
		});
	}
	
	
	public BpmnGraphEditorFrame(Container container,final Component componet){
		this.container =container;
		if (componet instanceof BpmnEditorPanel) {
			this.mainPanel = (BpmnEditorPanel) componet;
		}
		getContentPane().add(mainPanel);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 20);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 30);
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
		setTitle(NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditorFrame-000000")/*���̱༭��*/);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// ��editor�з����˸Ķ�����δ���棬����ʾ�Ƿ񱣴�����Ƴ�
				if (editor != null && editor.isModified()) {
					int result = MessageDialog.showYesNoCancelDlg(null, null,
							NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditorFrame-000001")/*���̶�����δ���棬�Ƿ�Ҫ����?*/);					
					switch (result) {
					case MessageDialog.ID_YES:
						editor.onSave();
						dispose();
						break;
					case MessageDialog.ID_NO:
						dispose();
						break;
					case MessageDialog.ID_CANCEL:
						break;
					default:
						break;
					}
				} else { // editor��δ�����Ķ���ֱ����ʾ�Ƿ�ȷ��
					if (MessageDialog.showOkCancelDlg(null, NCLangRes.getInstance().getStrByID("pfgraph", "validation")/*ȷ��*/, NCLangRes.getInstance().getStrByID("pfgraph", "EditorActions-000000")/*�Ƿ�ȷ�Ϲرձ༭��?*/) == MessageDialog.ID_OK) {
						dispose();
					}
				}

			}
		});
	}
	
	
	protected JRootPane createRootPane() {
		KeyStroke esc =KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
		JRootPane rootpane =new JRootPane();
		ActionListener actionListener =new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//ѡ��״̬
				//mxCell select =new mxCell();
				//select.setStyle("select");
				//((CustomGraphComponent)editor.getGraphComponent()).setSelectCellfromLib(select);
				editor.getShapesPalette().selectSelector();
			}		
		};
		rootpane.registerKeyboardAction(actionListener, esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootpane;
	}

	public BpmnGraphEditorFrame(final Component componet, JMenuBar menuBar) {
		this(null,componet,menuBar);	
	}
}
