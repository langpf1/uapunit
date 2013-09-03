package uap.workflow.modeler.sheet.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import nc.vo.pub.lang.UFBoolean;

public class UFBooleanPropertyEditor extends AbstractPropertyEditor {
	 public UFBooleanPropertyEditor() {
		    editor = new JCheckBox();
		    ((JCheckBox)editor).setOpaque(false);
		    ((JCheckBox)editor).addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		          firePropertyChange(
		            ((JCheckBox)editor).isSelected() ? Boolean.FALSE : Boolean.TRUE,
		            ((JCheckBox)editor).isSelected() ? Boolean.TRUE : Boolean.FALSE);
		            ((JCheckBox)editor).transferFocus();
		        }
		      });
		  }
	 	 
		  public Object getValue() {
		    return ((JCheckBox)editor).isSelected() ? UFBoolean.TRUE : UFBoolean.FALSE;
		  }

		  public void setValue(Object value) {
		    ((JCheckBox)editor).setSelected(UFBoolean.TRUE.equals(value));
		  }

}
