package uap.workflow.vo;

import java.util.HashMap;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IPfRetCheckInfo;

/**
 * 回退工作流返回的数据
 * 与RetBackWfVo一样，解除workflowapp包与NC的联系引入ReturnBackWfVo
 */
public class ReturnBackWfVo extends ValueObject {
	//回退状态
	private int backState = IPfRetCheckInfo.NOSTATE;

	//回退审批人
	private String preCheckMan = null;

	//是否由审批态转为非审批态
	private UFBoolean isFinish = new UFBoolean(false);
	
	/** 当前活动关联的一些扩展属性 */
	private HashMap relaProperties = new HashMap();

	public UFBoolean getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(UFBoolean isFinish) {
		this.isFinish = isFinish;
	}

	public ReturnBackWfVo() {
		super();
	}

	public String getEntityName() {
		return null;
	}

	public void validate() throws ValidationException {
	}

	public int getBackState() {
		return backState;
	}

	public void setBackState(int backState) {
		this.backState = backState;
	}

	public String getPreCheckMan() {
		return preCheckMan;
	}

	public void setPreCheckMan(String preCheckMan) {
		this.preCheckMan = preCheckMan;
	}
	
	/**
	 * 得到当前活动相关的流程属性，目前有
	 * <li>XPDLNames.WORKFLOW_GADGET - 单据组件及其实参
	 * <li>XPDLNames.EDITABLE_PROPERTIES - 可编辑属性
	 * <li>XPDLNames.ENABLE_BUTTON - 可用按钮
	 * @return
	 */
	public HashMap getRelaProperties() {
		return relaProperties;
	}
}
