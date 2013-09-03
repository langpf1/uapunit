package uap.workflow.modeler.utils;

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIToggleButton;

public class ModelerComponetFactory {
	
	private static ModelerComponetFactory instance;

	private ModelerComponetFactory(){
		
	}
	
	public static ModelerComponetFactory instance(){
		if(instance==null){
			instance =new ModelerComponetFactory();
		}
		return instance;
	}
	

	
	public AbstractButton createButton(String text,Icon icon,boolean isToggleButton,boolean isBorder,String tooltipText){
		AbstractButton button =null;
		if(isToggleButton){
			button =new UIToggleButton(text, icon);
		}else{
			button =new UIButton(text, icon);
		}
		if(icon!=null){
			button.setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			button.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		}
		
		button.setToolTipText(tooltipText);
		if(!isBorder){
			button.setBorder(BorderFactory.createEmptyBorder());
		}
		return button;
		
	}
	
	public UIButton createButton(String text,Icon icon,boolean isBorder,String toolTipText){
		return (UIButton) createButton(text,icon,false,isBorder,toolTipText);
	}
	
	public UIToggleButton createJtoggleButton(String text,Icon icon,boolean isBorder,String toolTipText){
		return (UIToggleButton) createButton(text,icon,true,isBorder,toolTipText);
	}

}
