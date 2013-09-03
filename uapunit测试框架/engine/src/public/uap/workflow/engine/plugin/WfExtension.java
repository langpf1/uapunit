package uap.workflow.engine.plugin;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import uap.workflow.engine.utils.ClassUtil;
/**
 * 扩展
 * 
 * @author licza
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WfExtension")
public class WfExtension implements Serializable {
	private static final long serialVersionUID = -1878982475179428227L;
	/** 扩展名 **/
	@XmlAttribute
	protected String id;
	/** 类名 **/
	@XmlAttribute
	protected String classname;
	/** 名称 **/
	@XmlAttribute
	protected String title;
	protected String pointId;
	public Object newInstance() {
		Object ins = ClassUtil.loadClass(classname);
		return ins;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
}
