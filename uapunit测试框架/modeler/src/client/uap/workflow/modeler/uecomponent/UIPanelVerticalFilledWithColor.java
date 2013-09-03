package uap.workflow.modeler.uecomponent;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import uap.workflow.modeler.utils.BpmnModelerConstants;

import nc.ui.pub.beans.UIPanel;

public class UIPanelVerticalFilledWithColor extends UIPanel {
	
	private Color beginColor;
	private Color endColor;
	
	public UIPanelVerticalFilledWithColor(Color beginColor,Color endColor){
		this.beginColor =beginColor;
		this.endColor=endColor;
	}
	
	
	public UIPanelVerticalFilledWithColor(Color beginColor){
		this.beginColor =beginColor;
	}
	
	public void paintComponents(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if(endColor==null||beginColor.equals(endColor)){
			Color color =beginColor;
			g2d.setColor(color);			
		}else{
			GradientPaint paint = new GradientPaint(0, 0, beginColor, 0, getHeight(), endColor);
			g2d.setPaint(paint);
		}
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}
}
