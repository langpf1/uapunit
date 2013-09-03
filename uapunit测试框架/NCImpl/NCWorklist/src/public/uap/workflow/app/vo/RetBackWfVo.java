package uap.workflow.app.vo;

import java.util.HashMap;

import uap.workflow.vo.ReturnBackWfVo;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IPfRetCheckInfo;

/**
 * ���˹��������ص�����
 * @author fangj 2005-2-2
 */
public class RetBackWfVo extends ValueObject {
	//����״̬
	private int backState = IPfRetCheckInfo.NOSTATE;

	//����������
	private String preCheckMan = null;

	//�Ƿ�������̬תΪ������̬
	private UFBoolean isFinish = new UFBoolean(false);
	
	/** ��ǰ�������һЩ��չ���� */
	private HashMap relaProperties = new HashMap();

	public UFBoolean getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(UFBoolean isFinish) {
		this.isFinish = isFinish;
	}

	public RetBackWfVo() {
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
	 * �õ���ǰ���ص��������ԣ�Ŀǰ��
	 * <li>XPDLNames.WORKFLOW_GADGET - �����������ʵ��
	 * <li>XPDLNames.EDITABLE_PROPERTIES - �ɱ༭����
	 * <li>XPDLNames.ENABLE_BUTTON - ���ð�ť
	 * @return
	 */
	public HashMap getRelaProperties() {
		return relaProperties;
	}
	
	public static RetBackWfVo fromReturnBackWfVo(ReturnBackWfVo returnBackWfVo)
	{
		RetBackWfVo retBackWfVo = new RetBackWfVo();
		retBackWfVo.setBackState(returnBackWfVo.getBackState());
		retBackWfVo.setIsFinish(returnBackWfVo.getIsFinish());
		retBackWfVo.setPreCheckMan(returnBackWfVo.getPreCheckMan());
		return retBackWfVo;
	}
}
