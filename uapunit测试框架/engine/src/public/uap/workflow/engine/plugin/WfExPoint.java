package uap.workflow.engine.plugin;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * ��չ��
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WfExPoint")
public class WfExPoint implements Serializable {
	private static final long serialVersionUID = 8814481639614547685L;
	/** ��չ��Ψһ��ʶ **/
	@XmlAttribute
	protected String point;
	/** ��չ���� **/
	@XmlAttribute
	protected String title;
	/** ʵ����չ���� **/
	@XmlAttribute
	protected String classname;
	@XmlElement(name = "extension")
	protected List<WfExtension> extensionList = new ArrayList<WfExtension>();
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public List<WfExtension> getExtensionList() {
		return extensionList;
	}
	public void setExtensionList(List<WfExtension> extensionList) {
		this.extensionList = extensionList;
	}
}
