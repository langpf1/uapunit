package uap.workflow.ui.workitem;

public class ApplicationRuntimeAdjustFactory {
	private ApplicationRuntimeAdjustFactory(){}
	
	public static IApplicationRuntimeAdjust createAdjust(int type){
		switch(type){
			case IApplicationRuntimeAdjust.ADJUST_TYPE_ADDASSIGN:
				return new AddAssignAdjustImpl();
			default:
				return null;
		}
	}
}
