package uap.workflow.modeler.bpmn.editor.handler;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import nc.ui.pub.flowdesigner.editor.BasicGraphEditor;

public class ActionFactory {

	public static Action bind(final Object source, String name, String hint, final Action action, String iconUrl) {
		AbstractAction newaction = new AbstractAction(name + hint, (iconUrl != null) ? new ImageIcon(BasicGraphEditor.class.getResource(iconUrl)) : null) {
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(source, e.getID(), e.getActionCommand()));
			}
		};
		newaction.putValue(Action.SHORT_DESCRIPTION, name + hint);
		return newaction;
	}

	public static Action bind(final Object source, String name, final Action action) {
		return bind(source, name, action, null);
	}

	public static Action bind(final Object source, String name, final Action action, String iconUrl) {
		return bind(source, name, "", action, iconUrl);
	}
}
