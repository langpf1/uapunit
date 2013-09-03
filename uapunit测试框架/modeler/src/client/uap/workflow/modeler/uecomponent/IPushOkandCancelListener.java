package uap.workflow.modeler.uecomponent;

/**
 * propertyEditor打开后以对话框形式展现，该接口用于对话框关闭时的操作
 * */
public interface IPushOkandCancelListener {
	
	public void pushOkStopEditing();
	
	public void pushCancelstopEditing();
}
