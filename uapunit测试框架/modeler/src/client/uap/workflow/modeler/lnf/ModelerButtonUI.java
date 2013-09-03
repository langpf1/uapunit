package uap.workflow.modeler.lnf;


import java.awt.Rectangle;
import java.awt.Shape;



import nc.ui.plaf.basic.UIButtonUI;

public class ModelerButtonUI extends UIButtonUI {
	
	
	protected Shape createShapeByPosType(Rectangle rect) {
		return rect;
	}
}
