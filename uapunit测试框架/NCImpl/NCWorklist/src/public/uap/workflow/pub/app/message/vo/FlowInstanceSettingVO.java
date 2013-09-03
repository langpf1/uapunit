package uap.workflow.pub.app.message.vo;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * 旧的流程实例期限VO（对应表pub_wf_ist）
 *
 * @deprecated since v61
 */
public class FlowInstanceSettingVO extends ValueObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name; //activity
	String id;
	int timeLimit;
	int timeRemind;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getTimeRemind() {
		return timeRemind;
	}

	public void setTimeRemind(int timeRemind) {
		this.timeRemind = timeRemind;
	}

	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
