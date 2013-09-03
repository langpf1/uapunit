package uap.workflow.modeler.uecomponent;

public interface IAssemberPropertyData {
	//获得属性对话框的值
	public Object assemberData();
	//初始化属性对话框时候调用
	public void unassemberData(Object intializeData);
}
