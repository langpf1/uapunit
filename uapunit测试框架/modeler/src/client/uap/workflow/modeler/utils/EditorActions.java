/*
 * $Id: EditorActions.java,v 1.6 2009/12/08 19:52:50 gaudenz Exp $
 * Copyright (c) 2001-2009, JGraph Ltd
 * 
 * All rights reserved.
 * 
 * See LICENSE file for license details. If you are unable to locate
 * this file please contact info (at) jgraph (dot) com.
 */
package uap.workflow.modeler.utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.ws.gen.util.StringUtil;
import nc.ui.ls.MessageBox;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import org.w3c.dom.Document;
import uap.workflow.app.config.NoticeTypeFactory;
import uap.workflow.app.config.ParticipantFilterTypeFactory;
import uap.workflow.app.config.ParticipantTypeFactory;
import uap.workflow.app.config.TaskHandlingTypeFactory;
import uap.workflow.app.notice.INoticeType;
import uap.workflow.app.participant.IParticipantFilterType;
import uap.workflow.app.participant.IParticipantType;
import uap.workflow.app.taskhandling.ITaskHandlingType;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.FlowNode;
import uap.workflow.bpmn2.model.Lane;
import uap.workflow.bpmn2.model.Pool;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.bpmn2.model.SubProcess;
import uap.workflow.bpmn2.parser.ProcessDefinitionsManager;
import uap.workflow.designer.exports.BPMN20ExportMarshaller;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.modeler.BasicBpmnGraphEditor;
import uap.workflow.modeler.BpmnEditorPanel;
import uap.workflow.modeler.BpmnModeler;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.uecomponent.BpmnMenuBar;
import uap.workflow.modeler.uecomponent.EditorRuler;
import com.mxgraph.analysis.mxDistanceCostFunction;
import com.mxgraph.analysis.mxGraphAnalysis;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
/**
 * @author Administrator
 * 
 */
public class EditorActions {
	/**
	 * 
	 * @param e
	 * @return Returns the graph for the given action event.
	 */
	public static final BasicBpmnGraphEditor getEditor(ActionEvent e) {
		if (e.getSource() instanceof Component) {
			Component component = (Component) e.getSource();
			while (component != null && !(component instanceof BasicBpmnGraphEditor)) {
				component = component.getParent();
			}
			return (BasicBpmnGraphEditor) component;
		}
		return null;
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleRulersItem extends JCheckBoxMenuItem {
		/**
		 * 
		 */
		public ToggleRulersItem(final BasicBpmnGraphEditor editor, String name) {
			super(name);
			setSelected(editor.getGraphComponent().getColumnHeader() != null);
			addActionListener(new ActionListener() {
				/**
				 * 
				 */
				public void actionPerformed(ActionEvent e) {
					mxGraphComponent graphComponent = editor.getGraphComponent();
					if (graphComponent.getColumnHeader() != null) {
						graphComponent.setColumnHeader(null);
						graphComponent.setRowHeader(null);
					} else {
						graphComponent.setColumnHeaderView(new EditorRuler(graphComponent, EditorRuler.ORIENTATION_HORIZONTAL));
						graphComponent.setRowHeaderView(new EditorRuler(graphComponent, EditorRuler.ORIENTATION_VERTICAL));
					}
				}
			});
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleGridItem extends JCheckBoxMenuItem {
		/**
		 * 
		 */
		public ToggleGridItem(final BasicBpmnGraphEditor editor, String name) {
			super(name);
			setSelected(editor.getGraphComponent().getGraph().isGridEnabled());
			addActionListener(new ActionListener() {
				/**
				 * 
				 */
				public void actionPerformed(ActionEvent e) {
					mxGraphComponent graphComponent = editor.getGraphComponent();
					mxGraph graph = graphComponent.getGraph();
					boolean enabled = !graph.isGridEnabled();
					graph.setGridEnabled(enabled);
					graphComponent.setGridVisible(enabled);
					graphComponent.repaint();
					setSelected(enabled);
				}
			});
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleOutlineItem extends JCheckBoxMenuItem {
		/**
		 * 
		 */
		public ToggleOutlineItem(final BasicBpmnGraphEditor editor, String name) {
			super(name);
			setSelected(true);
			addActionListener(new ActionListener() {
				/**
				 * 
				 */
				public void actionPerformed(ActionEvent e) {
					final mxGraphOutline outline = editor.getGraphOutline();
					outline.setVisible(!outline.isVisible());
					outline.revalidate();
					SwingUtilities.invokeLater(new Runnable() {
						/*
						 * (non-Javadoc)
						 * 
						 * @see java.lang.Runnable#run()
						 */
						public void run() {
							if (outline.getParent() instanceof JSplitPane) {
								if (outline.isVisible()) {
									((JSplitPane) outline.getParent()).setDividerLocation(editor.getHeight() - 300);
									((JSplitPane) outline.getParent()).setDividerSize(6);
								} else {
									((JSplitPane) outline.getParent()).setDividerSize(0);
								}
							}
						}
					});
				}
			});
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ExitAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			if (editor != null && editor.isModified() && (MessageDialog
							.showOkCancelDlg(null, NCLangRes.getInstance().getStrByID("pfgraph", "validation")/* 确认 */, 
									NCLangRes.getInstance().getStrByID("pfgraph", "EditorActions-000000")/** 是否确认关闭编辑器*/) == MessageDialog.ID_OK)) {
				editor.exit();
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class StylesheetAction extends AbstractAction {
		/**
		 * 
		 */
		protected String stylesheet;
		/**
		 * 
		 */
		public StylesheetAction(String stylesheet) {
			this.stylesheet = stylesheet;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				mxGraph graph = graphComponent.getGraph();
				mxCodec codec = new mxCodec();
				Document doc = mxUtils.loadDocument(EditorActions.class.getResource(stylesheet).toString());
				if (doc != null) {
					codec.decode(doc.getDocumentElement(), graph.getStylesheet());
					graph.refresh();
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ZoomPolicyAction extends AbstractAction {
		/**
		 * 
		 */
		protected int zoomPolicy;
		/**
		 * 
		 */
		public ZoomPolicyAction(int zoomPolicy) {
			this.zoomPolicy = zoomPolicy;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				graphComponent.setPageVisible(true);
				graphComponent.setZoomPolicy(zoomPolicy);
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class GridStyleAction extends AbstractAction {
		/**
		 * 
		 */
		protected int style;
		/**
		 * 
		 */
		public GridStyleAction(int style) {
			this.style = style;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				graphComponent.setGridStyle(style);
				graphComponent.repaint();
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class GridColorAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				Color newColor = JColorChooser.showDialog(graphComponent, mxResources.get("gridColor"), graphComponent.getGridColor());
				if (newColor != null) {
					graphComponent.setGridColor(newColor);
					graphComponent.repaint();
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ScaleAction extends AbstractAction {
		/**
		 * 
		 */
		protected double scale;
		/**
		 * 
		 */
		public ScaleAction(double scale) {
			this.scale = scale;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				double scale = this.scale;
				if (scale == 0) {
					String value = (String) JOptionPane.showInputDialog(graphComponent, mxResources.get("value"), mxResources.get("scale") + " (%)", JOptionPane.PLAIN_MESSAGE, null, null, "");
					if (value != null) {
						scale = Double.parseDouble(value.replace("%", "")) / 100;
					}
				}
				if (scale > 0) {
					graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class PageSetupAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				PrinterJob pj = PrinterJob.getPrinterJob();
				PageFormat format = pj.pageDialog(graphComponent.getPageFormat());
				if (format != null) {
					graphComponent.setPageFormat(format);
					graphComponent.zoomAndCenter();
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class PrintAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				PrinterJob pj = PrinterJob.getPrinterJob();
				if (pj.printDialog()) {
					PageFormat pf = graphComponent.getPageFormat();
					Paper paper = new Paper();
					paper.setSize(pf.getWidth(), pf.getHeight());
					double margin = 36;
					paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
					pf.setPaper(paper);
					pj.setPrintable(graphComponent, pf);
					try {
						pj.print();
					} catch (PrinterException e2) {}
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class SaveAction extends AbstractAction {
		protected boolean draft = false;
		protected String lastDir = null;
		public SaveAction(boolean draft) {
			this.draft = draft;
		}
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			if (editor != null) {
				editor.stopCellEditing();
				BPMN20ExportMarshaller marshaller = new BPMN20ExportMarshaller();
				Bpmn2MemoryModel model=Bpmn2MemoryModel.constructOutputModel(editor.getGraphComponent());
				if(!valid(model.getMainProcess()))
					model=null;
				marshaller.marshallDiagram(model, "", false, draft);
				editor.setModified(false);
			}
		}					
	}
	@SuppressWarnings("serial")
	public static class SaveFileAction extends AbstractAction {
		protected String lastDir = null;
		public SaveFileAction() {
		}
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			if (editor != null) {
				String wd = (lastDir != null) ? lastDir : System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(wd);
			/*	FileFilter defaultFilter = new FileFilter(){
					@Override
					public boolean accept(File arg0) {
						return false;
					}
					@Override
					public String getDescription() {
						return "*.xml";
					}
				};*/
				FileFilter nameFilter = new FileNameExtensionFilter("*.xml", ".xml", "xml");
				fc.addChoosableFileFilter(nameFilter);
				//fc.addChoosableFileFilter(defaultFilter);
				int rc = fc.showSaveDialog(null);
		
				if (rc == JFileChooser.APPROVE_OPTION) {
					lastDir = fc.getSelectedFile().getParent();
					FileWriter writer = null;
					try {
						Bpmn2MemoryModel model = Bpmn2MemoryModel.constructOutputModel(editor.getGraphComponent());
						if(!valid(model.getMainProcess()))
							model=null;
						String processDefinitions = ProcessDefinitionsManager.toXml(model,false);
						File objectsFile = new File(fc.getSelectedFile().getAbsolutePath());
						FileOutputStream fos = new FileOutputStream(objectsFile);
						ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
						OutputStreamWriter outStream = null;
						outStream = new OutputStreamWriter(arrayStream, "UTF-8");
						outStream.write(processDefinitions);
						outStream.flush();
						arrayStream.writeTo(fos);
						arrayStream.flush();
						fos.close();
						outStream.close();
						arrayStream.close();
					}catch(Exception ew){
						ew.printStackTrace();
					} finally {
						if (writer != null)
							try {
								writer.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					}

					editor.setModified(false);
					editor.setCurrentFile(fc.getSelectedFile());
				}
			}
		}
	}

	public static class SelectAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			BasicBpmnGraphEditor editor = getEditor(e);
			mxCell select = new mxCell();
			select.setStyle("select");
			((BpmnGraphComponent) editor.getGraphComponent()).getSelectedEntry();
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class SelectShortestPathAction extends AbstractAction {
		/**
		 * 
		 */
		protected boolean directed;
		/**
		 * 
		 */
		public SelectShortestPathAction(boolean directed) {
			this.directed = directed;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				mxGraph graph = graphComponent.getGraph();
				mxIGraphModel model = graph.getModel();
				Object source = null;
				Object target = null;
				Object[] cells = graph.getSelectionCells();
				for (int i = 0; i < cells.length; i++) {
					if (model.isVertex(cells[i])) {
						if (source == null) {
							source = cells[i];
						} else if (target == null) {
							target = cells[i];
						}
					}
					if (source != null && target != null) {
						break;
					}
				}
				if (source != null && target != null) {
					int steps = graph.getChildEdges(graph.getDefaultParent()).length;
					Object[] path = mxGraphAnalysis.getInstance().getShortestPath(graph, source, target, new mxDistanceCostFunction(), steps, directed);
					graph.setSelectionCells(path);
				} else {
					JOptionPane.showMessageDialog(graphComponent, mxResources.get("noSourceAndTargetSelected"));
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class SelectSpanningTreeAction extends AbstractAction {
		/**
		 * 
		 */
		protected boolean directed;
		/**
		 * 
		 */
		public SelectSpanningTreeAction(boolean directed) {
			this.directed = directed;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				mxGraph graph = graphComponent.getGraph();
				mxIGraphModel model = graph.getModel();
				Object parent = graph.getDefaultParent();
				Object[] cells = graph.getSelectionCells();
				for (int i = 0; i < cells.length; i++) {
					if (model.getChildCount(cells[i]) > 0) {
						parent = cells[i];
						break;
					}
				}
				Object[] v = graph.getChildVertices(parent);
				Object[] mst = mxGraphAnalysis.getInstance().getMinimumSpanningTree(graph, v, new mxDistanceCostFunction(), directed);
				graph.setSelectionCells(mst);
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleDirtyAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				graphComponent.showDirtyRectangle = !graphComponent.showDirtyRectangle;
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleImagePreviewAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				graphComponent.getGraphHandler().setImagePreview(!graphComponent.getGraphHandler().isImagePreview());
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleConnectModeAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				mxConnectionHandler handler = graphComponent.getConnectionHandler();
				handler.setHandleEnabled(!handler.isHandleEnabled());
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleCreateTargetItem extends JCheckBoxMenuItem {
		/**
		 * 
		 */
		public ToggleCreateTargetItem(final BasicBpmnGraphEditor editor, String name) {
			super(name);
			setSelected(true);
			addActionListener(new ActionListener() {
				/**
				 * 
				 */
				public void actionPerformed(ActionEvent e) {
					mxGraphComponent graphComponent = editor.getGraphComponent();
					if (graphComponent != null) {
						mxConnectionHandler handler = graphComponent.getConnectionHandler();
						handler.setCreateTarget(!handler.isCreateTarget());
						setSelected(handler.isCreateTarget());
					}
				}
			});
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class PromptPropertyAction extends AbstractAction {
		/**
		 * 
		 */
		protected Object target;
		/**
		 * 
		 */
		protected String fieldname, message;
		/**
		 * 
		 */
		public PromptPropertyAction(Object target, String message) {
			this(target, message, message);
		}
		/**
		 * 
		 */
		public PromptPropertyAction(Object target, String message, String fieldname) {
			this.target = target;
			this.message = message;
			this.fieldname = fieldname;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof Component) {
				try {
					Method getter = target.getClass().getMethod("get" + fieldname);
					Object current = getter.invoke(target);
					// TODO: Support other atomic types
					if (current instanceof Integer) {
						Method setter = target.getClass().getMethod("set" + fieldname, new Class[] { int.class });
						String value = (String) JOptionPane.showInputDialog((Component) e.getSource(), "Value", message, JOptionPane.PLAIN_MESSAGE, null, null, current);
						if (value != null) {
							setter.invoke(target, Integer.parseInt(value));
						}
					}
				} catch (Exception ex) {
					Logger.error(ex.getMessage(), ex);
				}
			}
			// Repaints the graph component
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				graphComponent.repaint();
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class TogglePropertyItem extends JCheckBoxMenuItem {
		/**
		 * 
		 */
		public TogglePropertyItem(Object target, String name, String fieldname) {
			this(target, name, fieldname, false);
		}
		/**
		 * 
		 */
		public TogglePropertyItem(Object target, String name, String fieldname, boolean refresh) {
			this(target, name, fieldname, refresh, null);
		}
		/**
		 * 
		 */
		public TogglePropertyItem(final Object target, String name, final String fieldname, final boolean refresh, ActionListener listener) {
			super(name);
			// Since action listeners are processed last to first we add the
			// given
			// listener here which means it will be processed after the one
			// below
			if (listener != null) {
				addActionListener(listener);
			}
			addActionListener(new ActionListener() {
				/**
				 * 
				 */
				public void actionPerformed(ActionEvent e) {
					execute(target, fieldname, refresh);
				}
			});
			PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * java.beans.PropertyChangeListener#propertyChange(java.beans
				 * .PropertyChangeEvent)
				 */
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equalsIgnoreCase(fieldname)) {
						update(target, fieldname);
					}
				}
			};
			if (target instanceof mxGraphComponent) {
				((mxGraphComponent) target).addPropertyChangeListener(propertyChangeListener);
			} else if (target instanceof mxGraph) {
				((mxGraph) target).addPropertyChangeListener(propertyChangeListener);
			}
			update(target, fieldname);
		}
		/**
		 * 
		 */
		public void update(Object target, String fieldname) {
			try {
				Method getter = target.getClass().getMethod("is" + fieldname);
				Object current = getter.invoke(target);
				if (current instanceof Boolean) {
					setSelected(((Boolean) current).booleanValue());
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		/**
		 * 
		 */
		public void execute(Object target, String fieldname, boolean refresh) {
			try {
				Method getter = target.getClass().getMethod("is" + fieldname);
				Method setter = target.getClass().getMethod("set" + fieldname, new Class[] { boolean.class });
				Object current = getter.invoke(target);
				if (current instanceof Boolean) {
					boolean value = !((Boolean) current).booleanValue();
					setter.invoke(target, value);
					setSelected(value);
				}
				if (refresh) {
					mxGraph graph = null;
					if (target instanceof mxGraph) {
						graph = (mxGraph) target;
					} else if (target instanceof mxGraphComponent) {
						graph = ((mxGraphComponent) target).getGraph();
					}
					graph.refresh();
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class HistoryAction extends AbstractAction {
		/**
		 * 
		 */
		protected boolean undo;
		/**
		 * 
		 */
		public HistoryAction(boolean undo) {
			this.undo = undo;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			if (editor != null) {
				if (undo) {
 					editor.getUndoManager().undo();
				} else {
					editor.getUndoManager().redo();
				}
			}
		}
	}
	@SuppressWarnings("serial")
	public static class HelpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			if (editor != null) {
				editor.about();
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class FontStyleAction extends AbstractAction {
		/**
		 * 
		 */
		protected boolean bold;
		/**
		 * 
		 */
		public FontStyleAction(boolean bold) {
			this.bold = bold;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				Component editorComponent = null;
				if (graphComponent.getCellEditor() instanceof mxCellEditor) {
					editorComponent = ((mxCellEditor) graphComponent.getCellEditor()).getEditor();
				}
				if (editorComponent instanceof JEditorPane) {
					JEditorPane editorPane = (JEditorPane) editorComponent;
					int start = editorPane.getSelectionStart();
					int ende = editorPane.getSelectionEnd();
					String text = editorPane.getSelectedText();
					if (text == null) {
						text = "";
					}
					try {
						HTMLEditorKit editorKit = new HTMLEditorKit();
						HTMLDocument document = (HTMLDocument) editorPane.getDocument();
						document.remove(start, (ende - start));
						editorKit.insertHTML(document, start, ((bold) ? "<b>" : "<i>") + text + ((bold) ? "</b>" : "</i>"), 0, 0, (bold) ? HTML.Tag.B : HTML.Tag.I);
					} catch (Exception ex) {
						Logger.error(ex.getMessage(), ex);
					}
					editorPane.requestFocus();
					editorPane.select(start, ende);
				} else {
					mxIGraphModel model = graphComponent.getGraph().getModel();
					model.beginUpdate();
					try {
						graphComponent.stopEditing(false);
						graphComponent.getGraph().toggleCellStyleFlags(mxConstants.STYLE_FONTSTYLE, (bold) ? mxConstants.FONT_BOLD : mxConstants.FONT_ITALIC);
					} finally {
						model.endUpdate();
					}
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class WarningAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				Object[] cells = graphComponent.getGraph().getSelectionCells();
				if (cells != null && cells.length > 0) {
					String warning = JOptionPane.showInputDialog(mxResources.get("enterWarningMessage"));
					for (int i = 0; i < cells.length; i++) {
						graphComponent.setCellWarning(cells[i], warning);
					}
				} else {
					JOptionPane.showMessageDialog(graphComponent, mxResources.get("noCellSelected"));
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class NewAction extends AbstractAction {
		/**
		 * 
		 */
		protected String lastDir = null;
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			if (editor != null) {
				if (editor.isModified() ) {
					int style=JOptionPane.showConfirmDialog(editor, NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000025")/** 取消本次的修改* ，* 继续吗	* */); 
					if(style== JOptionPane.YES_OPTION)
					{

					//	BasicBpmnGraphEditor editor = getEditor(e);
						if (editor != null) {
							String wd = (lastDir != null) ? lastDir : System.getProperty("user.dir");
							JFileChooser fc = new JFileChooser(wd);
							FileFilter nameFilter = new FileNameExtensionFilter("*.xml", ".xml", "xml");
							fc.addChoosableFileFilter(nameFilter);
							//fc.addChoosableFileFilter(defaultFilter);
							int rc = fc.showSaveDialog(null);
					
							if (rc == JFileChooser.APPROVE_OPTION) {
								lastDir = fc.getSelectedFile().getParent();
								FileWriter writer = null;
								try {
									Bpmn2MemoryModel model = Bpmn2MemoryModel.constructOutputModel(editor.getGraphComponent());
									if(!valid(model.getMainProcess()))
										model=null;
									String processDefinitions = ProcessDefinitionsManager.toXml(model,false);
									File objectsFile = new File(fc.getSelectedFile().getAbsolutePath());
									FileOutputStream fos = new FileOutputStream(objectsFile);
									ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
									OutputStreamWriter outStream = null;
									outStream = new OutputStreamWriter(arrayStream, "UTF-8");
									outStream.write(processDefinitions);
									outStream.flush();
									arrayStream.writeTo(fos);
									arrayStream.flush();
									fos.close();
									outStream.close();
									arrayStream.close();
								}catch(Exception ew){
									ew.printStackTrace();
								} finally {
									if (writer != null)
										try {
											writer.close();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
								}

								editor.setModified(false);
								editor.setCurrentFile(fc.getSelectedFile());
							}
						}
						editor.setModified(false);
						editor.setCurrentFile(null);
						editor.exit();
						// 重新启动的eitor
						//new BpmnModelerStarter().startModeler(null, editor.getParent());
						//BpmnModeler editor2=new BpmnModeler("ApproveModeler",null);  //流程设计器审批版
						BpmnModeler editor2 = new BpmnModeler(null);   //流程设计器完整版			
						BpmnEditorPanel editorPanel= new BpmnEditorPanel(editor2,new BpmnMenuBar(editor.getGraphComponent()));
						JFrame frame = editorPanel.createFrame(editor2.getParent());
						editor2.showGraph();
						frame.setVisible(true);
					}
					else if(style== JOptionPane.NO_OPTION)
					{
						editor.setModified(false);
						editor.setCurrentFile(null);
						editor.exit();
						// 重新启动的eitor
						//new BpmnModelerStarter().startModeler(null, editor.getParent());
						//BpmnModeler editor2=new BpmnModeler("ApproveModeler",null);  //流程设计器审批版
						BpmnModeler editor2 = new BpmnModeler(null);   //流程设计器完整版		
						BpmnEditorPanel editorPanel= new BpmnEditorPanel(editor2,new BpmnMenuBar(editor.getGraphComponent()));
						JFrame frame = editorPanel.createFrame(editor2.getParent());
						editor2.showGraph();
						frame.setVisible(true);
					}
				}
				else{
					editor.setModified(false);
					editor.setCurrentFile(null);
					editor.exit();
					// 重新启动的eitor
					//new BpmnModelerStarter().startModeler(null, editor.getParent());
					//BpmnModeler editor2=new BpmnModeler("ApproveModeler",null);  //流程设计器审批版
					BpmnModeler editor2 = new BpmnModeler(null);   //流程设计器完整版
					BpmnEditorPanel editorPanel= new BpmnEditorPanel(editor2,new BpmnMenuBar(editor.getGraphComponent()));
					JFrame frame = editorPanel.createFrame(editor2.getParent());
					editor2.showGraph();
					frame.setVisible(true);
				}
			}
		}
	}
	@SuppressWarnings("serial")
	public static class DeleteAction extends AbstractAction {
		/**
		 * 
		 * @param name
		 */
		public DeleteAction(String name) {
			super(name);
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			mxGraph graph = null;
			if (source instanceof mxGraphComponent) {
				graph = ((mxGraphComponent) source).getGraph();
			}
			if (graph != null) {
				graph.removeCells(null, false);
			}
		}
	}
	/**
	 * 
	 * */
	@SuppressWarnings("serial")
	public static class SearchAction extends AbstractAction {
		private JTextField field = null;
		public SearchAction(JTextField field) {
			this.field = field;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			if (editor != null) {
				editor.getObjectManager().searchElement(field.getText().trim());
			}
		}
	}
	public static class SearchAction2 extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			// if (e.getSource() instanceof mxGraphComponent) {
			// BasicBpmnGraphEditor editor = getEditor(e);
			//
			// GraphSearchDlgMaker.getInstance().showSearchDlag(editor);
			// }
		}
	}
	
	/**
	 * 
	 */
	public static class OpenFromFileAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		protected String lastDir ="";
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e) {
			BasicBpmnGraphEditor editor = getEditor(e);
			Reader reader = null;
			if (editor != null && !editor.isModified()) {

				String wd = (lastDir != null) ? lastDir : System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(wd);
				FileFilter nameFilter = new FileNameExtensionFilter("*.xml", ".xml", "xml");
				fc.addChoosableFileFilter(nameFilter);
				int rc = fc.showDialog(null, mxResources.get("openFile"));
		
				if (rc == JFileChooser.APPROVE_OPTION) {
					lastDir = fc.getSelectedFile().getParent();
		
					try {
						reader = new FileReader(new File(fc.getSelectedFile().getAbsolutePath()));
						Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(reader);
						Bpmn2MemoryModel.addToGraphModel(editor.getGraphComponent(), model);
						//防止打开之前的文件，然后进行添加时出现id重复
						avoidIdRepeat(model);
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					} finally {
						if (reader != null)
							try {
								reader.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					}

					editor.setModified(false);
					editor.setCurrentFile(fc.getSelectedFile());
				}
			}
			if(editor != null && editor.isModified())
			{
				int style= JOptionPane.showConfirmDialog(editor,NCLangRes.getInstance().getStrByID("pfgraph","graphhint-000025")/*取消本次的修改，继续吗？*/); 
				if(style== JOptionPane.YES_OPTION)
				{
                    //保存
						String wd = (lastDir != null) ? lastDir : System.getProperty("user.dir");
						JFileChooser fc = new JFileChooser(wd);
						FileFilter nameFilter = new FileNameExtensionFilter("*.xml", ".xml", "xml");
						fc.addChoosableFileFilter(nameFilter);
						int rc = fc.showSaveDialog(null);
						if (rc == JFileChooser.APPROVE_OPTION) {
							FileWriter writer = null;
							try {
								Bpmn2MemoryModel model = Bpmn2MemoryModel.constructOutputModel(editor.getGraphComponent());
								if(!valid(model.getMainProcess()))
									model=null;
								String processDefinitions = ProcessDefinitionsManager.toXml(model,false);
								File objectsFile = new File(fc.getSelectedFile().getAbsolutePath());
								FileOutputStream fos = new FileOutputStream(objectsFile);
								ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
								OutputStreamWriter outStream = null;
								outStream = new OutputStreamWriter(arrayStream, "UTF-8");
								outStream.write(processDefinitions);
								outStream.flush();
								arrayStream.writeTo(fos);
								arrayStream.flush();
								fos.close();
								outStream.close();
								arrayStream.close();
							}catch(Exception ew){
								ew.printStackTrace();
							} finally {
								if (writer != null)
									try {
										writer.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
							}
							editor.setModified(false);
							editor.setCurrentFile(fc.getSelectedFile());
						}
				    //打开
					int rc2 = fc.showDialog(null, mxResources.get("openFile"));
					if (rc2 == JFileChooser.APPROVE_OPTION) {
						try {
							reader = new FileReader(new File(fc.getSelectedFile().getAbsolutePath()));
							Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(reader);
							Bpmn2MemoryModel.addToGraphModel(editor.getGraphComponent(), model);
							avoidIdRepeat(model);
						} catch (FileNotFoundException ex) {
							ex.printStackTrace();
						} finally {
							if (reader != null)
								try {
									reader.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
						}

						editor.setModified(false);
						editor.setCurrentFile(fc.getSelectedFile());
					}		
				}
				else if(style== JOptionPane.NO_OPTION)
				{
					String wd = (lastDir != null) ? lastDir : System.getProperty("user.dir");
					JFileChooser fc = new JFileChooser(wd);
					FileFilter nameFilter = new FileNameExtensionFilter("*.xml", ".xml", "xml");
					fc.addChoosableFileFilter(nameFilter);
					int rc = fc.showDialog(null, mxResources.get("openFile"));
			
					if (rc == JFileChooser.APPROVE_OPTION) {
						lastDir = fc.getSelectedFile().getParent();
			
						try {
							reader = new FileReader(new File(fc.getSelectedFile().getAbsolutePath()));
							Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(reader);
							Bpmn2MemoryModel.addToGraphModel(editor.getGraphComponent(), model);
							avoidIdRepeat(model);
						} catch (FileNotFoundException ex) {
							ex.printStackTrace();
						} finally {
							if (reader != null)
								try {
									reader.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
						}

						editor.setModified(false);
						editor.setCurrentFile(fc.getSelectedFile());
					}	
				}
			}
			editor.getGraphOutline().setTripleBuffered(!editor.getGraphOutline().isTripleBuffered());
			editor.getGraphOutline().repaint();
		}
	}
	
	public static void avoidIdRepeat(Bpmn2MemoryModel model)
	{
		//防止打开之前的文件，然后进行添加时出现id重复
		int max=1;
		for(int i=0;i<model.getClipboard().size();i++) {
		if(Integer.valueOf(((String)model.getClipboard().get(i).getId()).substring(1)).intValue()>max)
		 {
			max=Integer.valueOf(((String)model.getClipboard().get(i).getId()).substring(1)).intValue();
		 }
		}
         if(CreateElementUtils.getIdSeed()<=max)
        	 CreateElementUtils.setIdSeed(max+1);
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class OpenAction extends AbstractAction {

		protected String lastDir;
		
		public void actionPerformed(ActionEvent e) {
              new InputPkDialog(e);
			// if (!editor.isModified() || JOptionPane.showConfirmDialog(editor,
			// NCLangRes.getInstance().getStrByID("pfgraph",
			// "graphhint-000025")/*取消本次的修改，继续吗？*/) == JOptionPane.YES_OPTION) {
			// mxGraph graph = mxGraphActions.getGraph(e);
			//
			// if (graph != null) {
			// String wd = (lastDir != null) ? lastDir :
			// System.getProperty("user.dir");
			//
			// JFileChooser fc = new JFileChooser(wd);
			//
			// // Adds file filter for supported file format
			// FileFilter defaultFilter = new DefaultFileFilter(".mxe",
			// "mxGraph Editor " + mxResources.get("file") + " (.mxe)");
			// fc.addChoosableFileFilter(defaultFilter);
			//
			// int rc = fc.showDialog(null, mxResources.get("openFile"));
			//
			// if (rc == JFileChooser.APPROVE_OPTION) {
			// lastDir = fc.getSelectedFile().getParent();
			//
			// try {
			// Document document =
			// mxUtils.parseXml(mxUtils.readFile(fc.getSelectedFile().getAbsolutePath()));
			//
			// mxCodec codec = new mxCodec(document);
			// codec.decode(document.getDocumentElement(), graph.getModel());
			//
			// editor.setModified(false);
			// editor.setCurrentFile(fc.getSelectedFile());
			// } catch (IOException e1) {
			// Logger.error(e1.getMessage(), e1);
			// }
			//
			// }
			// }
			// }
			// }
		}
		public class InputPkDialog extends UIDialog implements ActionListener
		{
			private static final long serialVersionUID = 1L;
			private JLabel pkLabel;
			private JTextArea pkText;
			private JButton ok;
			private JButton cancel;
			private ActionEvent e;
			@SuppressWarnings("deprecation")
			public InputPkDialog(ActionEvent e)
			{
				this.e=e;
				pkLabel=new JLabel("流程PK：");
				pkLabel.setBounds( 35, 20,60, 20);
				pkText=new JTextArea();
				pkText.setBounds( 88, 20,70, 20);
				ok=new JButton("确定");
				ok.setBounds( 38, 56,40, 20);
				ok.addActionListener(this);
				cancel=new JButton("取消");
				cancel.setBounds( 94, 56,40, 20);
				this.setTitle("打开流程");
				this.setLocation(550,250);
				this.setSize(180, 105);
			    this.setLayout(null);
				this.add(pkLabel);
				this.add(pkText);
				this.add(ok);
				this.add(cancel);
				cancel.addActionListener(this);
				this.setVisible(true);
			}
			@Override
			public void actionPerformed(ActionEvent eInput) {
				if(eInput.getSource()==ok)
				{
					BasicBpmnGraphEditor editor = getEditor(e);
					if (editor != null) {
						Map<String, IParticipantType> ptypes = ParticipantTypeFactory.getInstance().getTypes();
						Map<String, IParticipantFilterType> pfiltertypes = ParticipantFilterTypeFactory.getInstance().getFilterTypes();
						Map<String, INoticeType> pnoticetypes = NoticeTypeFactory.getInstance().getTypes();
						Map<String, ITaskHandlingType> ptasktypes = TaskHandlingTypeFactory.getInstance().getTypes();
						Reader reader = null;
						try {
						//	String filePath = "C:\\MyProcess_1.bpmn";
						//	reader = new FileReader(new File(filePath));
							String proDefPk=null;
							if(pkText.getText()!=null&&pkText.getText().length()!=0)
							    proDefPk = pkText.getText();
							else
								proDefPk ="0001AA1000000001DBH8";
							IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
							ProcessDefinitionVO proDef = proDefQry.getProDefVoByPk(proDefPk);
							Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(new StringReader(proDef.getProcessStr()));
							model.getProcesses().get(0).setProcessDefinitionPk(proDefPk);
							Bpmn2MemoryModel.addToGraphModel(editor.getGraphComponent(), model);
							avoidIdRepeat(model);
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
							if (reader != null)
								try {
									reader.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						}
					}
				}
				this.dispose();
			}
			
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ToggleAction extends AbstractAction {
		/**
		 * 
		 */
		protected String key;
		/**
		 * 
		 */
		protected boolean defaultValue;
		/**
		 * 
		 * @param key
		 */
		public ToggleAction(String key) {
			this(key, false);
		}
		/**
		 * 
		 * @param key
		 */
		public ToggleAction(String key, boolean defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = mxGraphActions.getGraph(e);
			if (graph != null) {
				graph.toggleCellStyles(key, defaultValue);
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class SetLabelPositionAction extends AbstractAction {
		/**
		 * 
		 */
		protected String labelPosition, alignment;
		/**
		 * 
		 * @param key
		 */
		public SetLabelPositionAction(String labelPosition, String alignment) {
			this.labelPosition = labelPosition;
			this.alignment = alignment;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = mxGraphActions.getGraph(e);
			if (graph != null && !graph.isSelectionEmpty()) {
				graph.getModel().beginUpdate();
				try {
					// Checks the orientation of the alignment to use the
					// correct constants
					if (labelPosition.equals(mxConstants.ALIGN_LEFT) || labelPosition.equals(mxConstants.ALIGN_CENTER) || labelPosition.equals(mxConstants.ALIGN_RIGHT)) {
						graph.setCellStyles(mxConstants.STYLE_LABEL_POSITION, labelPosition);
						graph.setCellStyles(mxConstants.STYLE_ALIGN, alignment);
					} else {
						graph.setCellStyles(mxConstants.STYLE_VERTICAL_LABEL_POSITION, labelPosition);
						graph.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN, alignment);
					}
				} finally {
					graph.getModel().endUpdate();
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class SetStyleAction extends AbstractAction {
		/**
		 * 
		 */
		protected String value;
		/**
		 * 
		 * @param key
		 */
		public SetStyleAction(String value) {
			this.value = value;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = mxGraphActions.getGraph(e);
			if (graph != null && !graph.isSelectionEmpty()) {
				graph.setCellStyle(value);
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class KeyValueAction extends AbstractAction {
		/**
		 * 
		 */
		protected String key, value;
		/**
		 * 
		 * @param key
		 */
		public KeyValueAction(String key) {
			this(key, null);
		}
		/**
		 * 
		 * @param key
		 */
		public KeyValueAction(String key, String value) {
			this.key = key;
			this.value = value;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = mxGraphActions.getGraph(e);
			if (graph != null && !graph.isSelectionEmpty()) {
				graph.setCellStyles(key, value);
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class PromptValueAction extends AbstractAction {
		/**
		 * 
		 */
		protected String key, message;
		/**
		 * 
		 * @param key
		 */
		public PromptValueAction(String key, String message) {
			this.key = key;
			this.message = message;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof Component) {
				mxGraph graph = mxGraphActions.getGraph(e);
				if (graph != null && !graph.isSelectionEmpty()) {
					String value = (String) JOptionPane.showInputDialog((Component) e.getSource(), mxResources.get("value"), message, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if (value != null) {
						if (value.equals(mxConstants.NONE)) {
							value = null;
						}
						graph.setCellStyles(key, value);
					}
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class AlignCellsAction extends AbstractAction {
		/**
		 * 
		 */
		protected String align;
		/**
		 * 
		 * @param key
		 */
		public AlignCellsAction(String align) {
			this.align = align;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = mxGraphActions.getGraph(e);
			if (graph != null && !graph.isSelectionEmpty()) {
				graph.alignCells(align);
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class AutosizeAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = mxGraphActions.getGraph(e);
			if (graph != null && !graph.isSelectionEmpty()) {
				graph.updateCellSize(graph.getSelectionCell());
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class ColorAction extends AbstractAction {
		/**
		 * 
		 */
		protected String name, key;
		/**
		 * 
		 * @param key
		 */
		public ColorAction(String name, String key) {
			this.name = name;
			this.key = key;
		}
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				mxGraph graph = graphComponent.getGraph();
				if (!graph.isSelectionEmpty()) {
					Color newColor = JColorChooser.showDialog(graphComponent, name, null);
					if (newColor != null) {
						graph.setCellStyles(key, mxUtils.hexString(newColor));
					}
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class BackgroundImageAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				String value = (String) JOptionPane.showInputDialog(graphComponent, mxResources.get("backgroundImage"), "URL", JOptionPane.PLAIN_MESSAGE, null, null,
						"http://www.callatecs.com/images/background2.JPG");
				if (value != null) {
					if (value.length() == 0) {
						graphComponent.setBackgroundImage(null);
					} else {
						graphComponent.setBackgroundImage(new ImageIcon(mxUtils.loadImage(value)));
					}
					// Forces a repaint of the outline
					graphComponent.getGraph().repaint();
				}
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class BackgroundAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				Color newColor = JColorChooser.showDialog(graphComponent, mxResources.get("background"), null);
				if (newColor != null) {
					graphComponent.getViewport().setOpaque(false);
					graphComponent.setBackground(newColor);
				}
				// Forces a repaint of the outline
				graphComponent.getGraph().repaint();
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class PageBackgroundAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				Color newColor = JColorChooser.showDialog(graphComponent, mxResources.get("pageBackground"), null);
				if (newColor != null) {
					graphComponent.setPageBackgroundColor(newColor);
				}
				// Forces a repaint of the component
				graphComponent.repaint();
			}
		}
	}
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public static class StyleAction extends AbstractAction {
		/**
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof mxGraphComponent) {
				mxGraphComponent graphComponent = (mxGraphComponent) e.getSource();
				mxGraph graph = graphComponent.getGraph();
				String initial = graph.getModel().getStyle(graph.getSelectionCell());
				String value = (String) JOptionPane.showInputDialog(graphComponent, mxResources.get("style"), mxResources.get("style"), JOptionPane.PLAIN_MESSAGE, null, null, initial);
				if (value != null) {
					graph.setCellStyle(value);
				}
			}
		}
	}
	
	@SuppressWarnings("serial")
	public static class HideLableAction extends AbstractAction {
			public void actionPerformed(ActionEvent e) {
				mxGraph graph = mxGraphActions.getGraph(e);
				mxCell root=(mxCell) graph.getDefaultParent();
				setLableVisible(graph,root);
			}
			
			private void setLableVisible(mxGraph graph,mxCell parent)
			{
				for(int i=0;i<parent.getChildCount();i++)
				{
					if((parent.getChildAt(i).getValue() instanceof Pool)||(parent.getChildAt(i).getValue() instanceof Lane)||(parent.getChildAt(i).getValue() instanceof SubProcess))
					{
						if(parent.getChildAt(i).getValue() instanceof FlowNode)
						{
						String name=((FlowElement)(((mxCell)parent.getChildAt(i)).getValue())).getName();
						String id=((FlowElement)(((mxCell)parent.getChildAt(i)).getValue())).getId();
						String total=name;
						if(total==null)
							total="("+id+")";
						if(total!=null&&total.charAt(total.length()-1)!=')')
						total=total+"("+id+")";
						((FlowElement)(((mxCell)parent.getChildAt(i)).getValue())).setName(total);
						graph.setSelectionCell(parent.getChildAt(i));
						graph.setCellStyles("noLabel", "0");
						}
						setLableVisible(graph,(mxCell)parent.getChildAt(i));
					}
					else if(parent.getChildAt(i).getValue()instanceof FlowElement)
					{
						String name=((FlowElement)(((mxCell)parent.getChildAt(i)).getValue())).getName();
						String id=((FlowElement)(((mxCell)parent.getChildAt(i)).getValue())).getId();
						String total=name;
						if(total==null)
							total="("+id+")";
						if(total!=null&&total.charAt(total.length()-1)!=')')
						   total=total+"("+id+")";
						((FlowElement)(((mxCell)parent.getChildAt(i)).getValue())).setName(total);
						graph.setSelectionCell(parent.getChildAt(i));
						graph.setCellStyles("noLabel", "0");
					}
				}
			}
	}
	
	private static boolean valid(Process process) 
	{
		boolean valid=true;
		if (StringUtil.isEmptyOrNull(process.getObjectType()))
		{
			MessageBox.showMessageDialog("提示", "对象类型不能为空。");
			valid=false;
		}
		return valid;
	}
}
