package uap.workflow.modeler.uecomponent;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.TransferHandler;

import uap.workflow.modeler.bpmn.editor.handler.ActionFactory;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import nc.ui.pub.flowdesigner.editor.ColorConstants;
import nc.ui.pub.flowdesigner.editor.CustomToolBar;
import uap.workflow.modeler.utils.EditorActions.NewAction;
import uap.workflow.modeler.utils.EditorActions.OpenAction;
import uap.workflow.modeler.utils.EditorActions.OpenFromFileAction;
import uap.workflow.modeler.utils.EditorActions.SaveAction;
import uap.workflow.modeler.utils.EditorActions.PrintAction;
import uap.workflow.modeler.utils.EditorActions.DeleteAction;
import uap.workflow.modeler.utils.EditorActions.HistoryAction;
import uap.workflow.modeler.utils.EditorActions.FontStyleAction;
import uap.workflow.modeler.utils.EditorActions.ColorAction;
import uap.workflow.modeler.utils.EditorActions.AlignCellsAction;

public class BpmnToolBar extends CustomToolBar{

	private boolean ignoreZoomChange = false;
	
	public BpmnToolBar(mxGraphComponent graphComponent,int orientation) {
		super(orientation);
		setFloatable(false);
		setBorder(BorderFactory.createLineBorder(ColorConstants.TOOLBAR_BORDERCOLOR));
		setPreferredSize(new Dimension((int) getSize().getWidth(),26));
		JButton jb= add(ActionFactory.bind(graphComponent,getDisplayName("new"), "(Ctrl+N)",new NewAction(), "/com/mxgraph/examples/swing/images/new.png"));
		jb.getUI().toString();
		add(ActionFactory.bind(graphComponent,getDisplayName("open"),"(Ctrl+O)", new OpenAction(), "/com/mxgraph/examples/swing/images/open.png"));
		add(ActionFactory.bind(graphComponent,getDisplayName("save"),"(Ctrl+S)", new SaveAction(false), "/com/mxgraph/examples/swing/images/save.png"));

		addSeparator();
		add(ActionFactory.bind(graphComponent,getDisplayName("print"),"(Ctrl+P)", new PrintAction(), "/com/mxgraph/examples/swing/images/print.png"));

		addSeparator();
		add(ActionFactory.bind(graphComponent,getDisplayName("zoomIn"), "(Ctrl+Plus)", mxGraphActions.getZoomInAction(), "/com/mxgraph/examples/swing/images/zoomin.gif"));
		add(ActionFactory.bind(graphComponent,getDisplayName("zoomOut"), "(Ctrl+Sub)", mxGraphActions.getZoomOutAction(), "/com/mxgraph/examples/swing/images/zoomout.gif"));
		
		addSeparator();

		add(ActionFactory.bind(graphComponent,getDisplayName("cut"),"(Ctrl+X)" ,TransferHandler.getCutAction(), "/com/mxgraph/examples/swing/images/cut.png"));
		add(ActionFactory.bind(graphComponent,getDisplayName("copy"),"(Ctrl+C)" ,TransferHandler.getCopyAction(), "/com/mxgraph/examples/swing/images/copy.png"));
		add(ActionFactory.bind(graphComponent,getDisplayName("paste"), "(Ctrl+V)",TransferHandler.getPasteAction(), "/com/mxgraph/examples/swing/images/paste.png"));

		addSeparator();

		add(ActionFactory.bind(graphComponent,getDisplayName("delete"), "(Delete)",new DeleteAction("delete"), "/com/mxgraph/examples/swing/images/delete.png"));

		addSeparator();

		add(ActionFactory.bind(graphComponent,getDisplayName("undo"),"(Ctrl+Z)", new HistoryAction(true), "/com/mxgraph/examples/swing/images/undo.png"));
		add(ActionFactory.bind(graphComponent,getDisplayName("redo"),"(Ctrl+Y)", new HistoryAction(false), "/com/mxgraph/examples/swing/images/redo.png"));

		addSeparator();
		
		add(generateFontCombox(graphComponent));
		
		add(generateSizeCombox(graphComponent));
		add(ActionFactory.bind(graphComponent,getDisplayName("bold"),"(Ctrl+B)", new FontStyleAction(true), "/com/mxgraph/examples/swing/images/bold.png"));
		add(ActionFactory.bind(graphComponent,getDisplayName("italic"),"(Ctrl+I)" ,new FontStyleAction(false), "/com/mxgraph/examples/swing/images/italic.png"));
		addSeparator();
		
		add(ActionFactory.bind(graphComponent,getDisplayName("font"), new ColorAction("Font", mxConstants.STYLE_FONTCOLOR),"/com/mxgraph/examples/swing/images/fontcolor.png"));
        add(ActionFactory.bind(graphComponent,getDisplayName("stroke"), new ColorAction("Stroke", mxConstants.STYLE_STROKECOLOR),"/com/mxgraph/examples/swing/images/linecolor.png"));
        add(ActionFactory.bind(graphComponent,getDisplayName("fill"), new ColorAction("Fill", mxConstants.STYLE_FILLCOLOR),"/com/mxgraph/examples/swing/images/fillcolor.png"));
        addSeparator();
        
        add(ActionFactory.bind(graphComponent,getDisplayName("alignleft"), new AlignCellsAction(mxConstants.ALIGN_LEFT),"/com/mxgraph/examples/swing/images/alignleft.png"));
        add(ActionFactory.bind(graphComponent,getDisplayName("alignhcenter"), new AlignCellsAction(mxConstants.ALIGN_CENTER), "/com/mxgraph/examples/swing/images/alignhcenter.png"));
        addSeparator();
        add(ActionFactory.bind(graphComponent,getDisplayName("alignright"),new AlignCellsAction(mxConstants.ALIGN_RIGHT),"/com/mxgraph/examples/swing/images/alignright.png"));
        add(ActionFactory.bind(graphComponent,getDisplayName("aligntop"), new AlignCellsAction(mxConstants.ALIGN_TOP),"/com/mxgraph/examples/swing/images/aligntop.png"));
        addSeparator();
        add(ActionFactory.bind(graphComponent,getDisplayName("alignbottom"), new AlignCellsAction(mxConstants.ALIGN_BOTTOM), "/com/mxgraph/examples/swing/images/alignbottom.png"));
        add(ActionFactory.bind(graphComponent,getDisplayName("alignvcenter"), new AlignCellsAction(mxConstants.ALIGN_MIDDLE), "/com/mxgraph/examples/swing/images/alignvcenter.png"));
		hideBorder(false);
	}

	private JComboBox generateFontCombox(final mxGraphComponent graphComponent) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		List<String> fonts = new ArrayList<String>();
		fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));
		final JComboBox fontCombo = new JComboBox(fonts.toArray());
		fontCombo.setEditable(true);
		fontCombo.setMinimumSize(new Dimension(120, 20));
		fontCombo.setPreferredSize(new Dimension(120, 20));
		fontCombo.setMaximumSize(new Dimension(120, 20));
		fontCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String font = fontCombo.getSelectedItem().toString();

				if (font != null && !font.equals("-")) {
					mxGraph graph = graphComponent.getGraph();
					graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, font);
				}
			}
		});
		return fontCombo;
	}
	
	private JComboBox generateSizeCombox(final mxGraphComponent graphComponent) {
		final JComboBox sizeCombo = new JComboBox(new Object[] { "6pt", "8pt", "9pt", "10pt", "12pt", "14pt", "18pt",
				"24pt", "30pt", "36pt", "48pt", "60pt" });
		sizeCombo.setEditable(true);
		sizeCombo.setMinimumSize(new Dimension(65, 20));
		sizeCombo.setPreferredSize(new Dimension(65, 20));
		sizeCombo.setMaximumSize(new Dimension(65, 20));
		sizeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mxGraph graph = graphComponent.getGraph();
				graph.setCellStyles(mxConstants.STYLE_FONTSIZE, sizeCombo.getSelectedItem().toString()
						.replace("pt", ""));
			}
		});
		return sizeCombo;
	}
}
