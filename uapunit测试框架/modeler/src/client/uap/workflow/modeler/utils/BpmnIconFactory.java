package uap.workflow.modeler.utils;

import nc.uitheme.ui.ThemeResourceCenter;

public class BpmnIconFactory {

	private BpmnIconFactory() {

	}

	public static BpmnIconFactory instance = new BpmnIconFactory();

	private static final String current_themere = ThemeResourceCenter.getInstance().getCurrTheme().getCode();

	private static final String PATH_ICON = "/themeroot/" + current_themere + "/themeres/control/activiti/";

	private static final String PNG_SUFFIX = ".png";

	private static final String GIF_SUFFIX = ".gif";
	
	public static String getIconURL(int iType, String element){
		if(element.equals("task")){
			return getTaskIconURL(iType);
		}else if(element.equals("gateWay")){
			return getGateWayIconURL(iType);
		}
		return null;
	}
	
	
	public static String getTaskIconURL(int iTaskType){
		switch (iTaskType) {
		case 0:
			return PATH_ICON + "type.user" + PNG_SUFFIX;
		case 1:
			return PATH_ICON + "type.script" + PNG_SUFFIX;
		case 2:
			return PATH_ICON + "type.service" + PNG_SUFFIX;
		case 3:
			return PATH_ICON + "type.mail" + PNG_SUFFIX;
		case 4:
			return PATH_ICON + "type.manual" + PNG_SUFFIX;
		case 5:
			return PATH_ICON + "type.receive" + PNG_SUFFIX;
		case 6:
			return PATH_ICON + "type.business.rule" + PNG_SUFFIX;
		default:
			return PATH_ICON + "type.user" + PNG_SUFFIX;
		}
	}

	public static String getTaskIconURL(BpmnTaskTypeEnum taskType) {
		return getTaskIconURL(taskType.getIntValue());
	}

	public static String getGateWayIconURL(int iGatewayType) {
		switch (iGatewayType) {
		case 0:
			return PATH_ICON + "type.gateway.parallel" + PNG_SUFFIX;
		case 1:
			return PATH_ICON + "type.gateway.exclusive" + PNG_SUFFIX;
		case 2:
			return PATH_ICON + "type.gateway.inclusive" + PNG_SUFFIX;
		case 3:
			return PATH_ICON + "type.gateway.event" + PNG_SUFFIX;
		default:
			return PATH_ICON + "type.gateway.parallel" + PNG_SUFFIX;
		}
	}
	public static String getGateWayIconURL(BpmnGatewayTypeEnum gatewayType){
		return getGateWayIconURL(gatewayType.getIntValue());
	}
}
