package uap.workflow.bpmn2.model.event;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.model.ExecutionListener;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ThrowEvent")
public class ThrowEvent extends Event
{
	private static final long serialVersionUID = 6343515278919126930L;
	@XmlTransient
	protected List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();
	 
	public List<ExecutionListener> getExecutionListeners() {
		return this.executionListeners;
	}

	public void setExecutionListeners(List<ExecutionListener> executionListeners) {
		this.executionListeners = executionListeners;
	}
}
