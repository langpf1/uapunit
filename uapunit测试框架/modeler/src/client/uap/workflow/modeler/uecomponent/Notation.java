package uap.workflow.modeler.uecomponent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Notation extends JLabel {
	private String notationGroup;

	public Notation(ImageIcon icon) {
		super(icon);
	}

	public String getNotationGroup() {
		return notationGroup;
	}

	public void setNotationGroup(String notationGroup) {
		this.notationGroup = notationGroup;
	}
	
	
}
