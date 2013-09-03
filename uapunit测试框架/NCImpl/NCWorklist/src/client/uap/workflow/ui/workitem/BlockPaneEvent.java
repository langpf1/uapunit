package uap.workflow.ui.workitem;

import java.awt.event.ActionEvent;

/**
 * 区块事件类
 * @author zhangrui
 *
 */
public class BlockPaneEvent extends ActionEvent {
	private static final long serialVersionUID = -7146136882550512779L;
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	private Object value;
	
	public BlockPaneEvent(ActionEvent sourceEvent, Object source, String name, Object value) {
		super(source, sourceEvent.getID(), sourceEvent.getActionCommand(), sourceEvent.getWhen(), sourceEvent.getModifiers());
		this.name = name;
		this.value = value;
	}

}
