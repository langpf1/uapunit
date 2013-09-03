/**
 * $Id: EditorKeyboardHandler.java,v 1.1 2009/10/23 11:32:08 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package uap.workflow.modeler.editors;

import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import uap.workflow.modeler.utils.EditorActions;
import uap.workflow.modeler.utils.EditorActions.ToggleAction;

import nc.ui.pub.flowdesigner.editor.EditorActions.FontStyleAction;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;

/**
 * @author Administrator
 * 
 */
public class EditorKeyboardHandler extends mxKeyboardHandler
{

	/**
	 * 
	 * @param graphComponent
	 */
	public EditorKeyboardHandler(mxGraphComponent graphComponent)
	{
		super(graphComponent);
	}

	/**
	 * Return JTree's input map.
	 */
	protected InputMap getInputMap(int condition)
	{
		InputMap map = super.getInputMap(condition);

		if (map != null)
		{
			map.put(KeyStroke.getKeyStroke("control S"), "save");
			map.put(KeyStroke.getKeyStroke("control N"), "new");
			map.put(KeyStroke.getKeyStroke("control O"), "open");
			map.put(KeyStroke.getKeyStroke("control Z"), "undo");
			map.put(KeyStroke.getKeyStroke("control Y"), "redo");
			map.put(KeyStroke.getKeyStroke("control shift V"),"selectVertices");
			map.put(KeyStroke.getKeyStroke("control shift E"), "selectEdges");
			map.put(KeyStroke.getKeyStroke("control F"), "find");
			map.put(KeyStroke.getKeyStroke("control P"), "print");
			map.put(KeyStroke.getKeyStroke("F1"), "help");
			map.put(KeyStroke.getKeyStroke("control I"), "italic");
			map.put(KeyStroke.getKeyStroke("control B"), "bold");
			map.put(KeyStroke.getKeyStroke("control shift H"),"hide");
		}

		return map;
	}

	/**
	 * Return the mapping between JTree's input map and JGraph's actions.
	 */
	protected ActionMap createActionMap()
	{
		ActionMap map = super.createActionMap();
		map.put("save", new EditorActions.SaveAction(false));
		map.put("new", new EditorActions.NewAction());
		map.put("open", new EditorActions.OpenAction());
		map.put("undo", new EditorActions.HistoryAction(true));
		map.put("redo", new EditorActions.HistoryAction(false));
		map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
		map.put("selectEdges", mxGraphActions.getSelectEdgesAction());
		map.put("find", new EditorActions.SearchAction2());
		map.put("print",new EditorActions.PrintAction());
		map.put("help",new EditorActions.HelpAction());
		map.put( "italic",new FontStyleAction(false));
		map.put("bold",new FontStyleAction(true));
		map.put("hide",new EditorActions.HideLableAction());
		return map;
	}

}
