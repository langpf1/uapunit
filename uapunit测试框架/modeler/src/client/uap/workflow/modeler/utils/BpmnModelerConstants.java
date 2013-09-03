package uap.workflow.modeler.utils;

import java.awt.Color;

import javax.swing.plaf.ColorUIResource;

import nc.ui.ml.NCLangRes;

public class BpmnModelerConstants {

	//颜色******************************************************************************
	public final static Color BACKGROUND_COLOR =new  ColorUIResource(0xB0, 0xC9, 0xE2);
	
	public final static Color BEGIN_COLOR = new ColorUIResource(0xce, 0xdd, 0xed);
	
	public final static Color END_COLOR = new ColorUIResource(0xb9, 0xcf, 0xe6);
	
	public static final String BACKGROUND_COLOR_KEY = "Panel.background";
	
	public static final Color TABLEGRID_COLOR =new  ColorUIResource(0xE7,0xF5,0xFC);
	
	public static final Color VALID_COLOR =Color.GREEN;
	
	public static final Color INVALID_COLOR=Color.RED;
	//***********************************************************************************
	
	
	
	
	public final static String TEXT_OUTHINTMESSAGE =NCLangRes.getInstance().getStrByID("pfgraph", "BasicGraphEditor-000000")/*输出提示*/;
	
	public final static String TEXT_CELLLIB=NCLangRes.getInstance().getStrByID("pfgraph", "GraphEditor-000000")/*图元库*/;
	
	public final static String TEXT_PROPERTYSHEET=NCLangRes.getInstance().getStrByID("101203", "UPP101203-000046")/*属性编辑器*/;
	
	//Insert***********************************************
	public final static int ROW_HIGHT=23;
	
	public final static int HOTSPOT_SIZE=18;
	
	public final static int INSERT_BORDER =10;
	
	public final static int INSERT_COMPONENT =25;
	
	public final static int COLUMN_2WIDTH=40;
	
	public final static int COLUMN_3WIDTH=50;
	
	public final static int COLUMN_4WIDTH=80;
	
	public final static int COLUMN_6WIDTH=120;
	
	public final static int COLUMN_12WIDTH=200;
	//*******************************************************
	
	public final static double DEFAULT_HOTSPOT=0.9;
	
	//属性类别***********************************************************************
	public static final String  CATEGORY_GENERAL="General";
	
	public static final String  CATEGORY_STYLE="Style";
	
	public static final String  CATEGORY_MAINCONFIG="Main Config";
	
	public static final String  CATEGORY_LISTENERS="Listeners";
	
	public static final String  CATEGORY_MULTIINSTANCE="Multi Instance";
	
	public static final String  CATEGORY_FORM="Form";
	
	public static final String CATEGORY_PROCESS="Process";
	
	public static final String CATEGORY_SIGNALS="Signals";
	
	public static final String CATEGORY_EXTENDS = "Extends";

	//public static final String CATEGORY_STYLES = "Styles";
//*****************************************************************************
	

}
