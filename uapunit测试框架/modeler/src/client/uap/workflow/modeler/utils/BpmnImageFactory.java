package uap.workflow.modeler.utils;

import javax.swing.ImageIcon;

import nc.uitheme.ui.ThemeResourceCenter;
/**
 * ThemeResourceCenter��װ�࣬ȡ��bpmnͼ��ı�ݹ����� 
 * */
public class BpmnImageFactory {
	
	private BpmnImageFactory(){
		
	}
	private String PATH_IMAGE="themeres/control/workflow/";
	
	public static BpmnImageFactory instance;
	
	public static BpmnImageFactory getInstance(){
		if(instance==null){
			instance = new BpmnImageFactory();
		}
		return instance;
	}
	
	public ImageIcon getImageIcon(String imageURL){
		return ThemeResourceCenter.getInstance().getImage(PATH_IMAGE+imageURL);
	}

}
