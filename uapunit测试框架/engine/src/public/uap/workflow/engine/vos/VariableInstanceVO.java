package uap.workflow.engine.vos;
import nc.vo.pub.SuperVO;
public class VariableInstanceVO extends SuperVO {
	private static final long serialVersionUID = 281772047192838642L;
	private String pk_variable;
	private String code;
	private String pk_process_instance;
	private String pk_activity_instance;
	private String pk_task;
	private String longvalue;
	private String doublevalue;
	private String textvalue;
	private String textvalue2;
	private byte[] objectvalue;
	public String getPk_variable() {
		return pk_variable;
	}
	public void setPk_variable(String pk_variable) {
		this.pk_variable = pk_variable;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public byte[] getObjectvalue() {
		return objectvalue;
	}
	public void setObjectvalue(byte[] objectvalue) {
		this.objectvalue = objectvalue;
	}
	public String getPk_process_instance() {
		return pk_process_instance;
	}
	public void setPk_process_instance(String pk_process_instance) {
		this.pk_process_instance = pk_process_instance;
	}
	public String getPk_activity_instance() {
		return pk_activity_instance;
	}
	public void setPk_activity_instance(String pk_activity_instance) {
		this.pk_activity_instance = pk_activity_instance;
	}
	public String getPk_task() {
		return pk_task;
	}
	public void setPk_task(String pk_task) {
		this.pk_task = pk_task;
	}
	public String getLongvalue() {
		return longvalue;
	}
	public void setLongvalue(String longValue) {
		this.longvalue = longValue;
	}
	public String getDoublevalue() {
		return doublevalue;
	}
	public void setDoublevalue(String doubleValue) {
		this.doublevalue = doubleValue;
	}
	public String getTextvalue() {
		return textvalue;
	}
	public void setTextvalue(String textValue) {
		this.textvalue = textValue;
	}
	public String getTextvalue2() {
		return textvalue2;
	}
	public void setTextvalue2(String textValue2) {
		this.textvalue2 = textValue2;
	}
	@Override
	public String getPKFieldName() {
		return "pk_variable";
	}
	@Override
	public String getTableName() {
		return "wf_variable";
	}
}
