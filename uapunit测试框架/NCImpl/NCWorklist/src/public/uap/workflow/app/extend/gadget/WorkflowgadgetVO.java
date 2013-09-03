package uap.workflow.app.extend.gadget;

import java.util.ArrayList;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * ���������ע���pub_workflowgadget��Ӧ��VO
 * 
 * @author leijun 2008-8
 * @since 5.5 
 */
public class WorkflowgadgetVO extends SuperVO {
	private String classname;

	private String pk_workflowgadget;

	private UFDateTime ts;

	private String billtype;

	private Integer dr;

	private String gadget_name;

	private String resid;

	//�����ݿ��ֶΣ��ӱ���Ϣ
	private ArrayList<WfGadgetBodyVO> bodys;

	public void addBodyVO(WfGadgetBodyVO bodyVO) {
		if (bodys == null)
			bodys = new ArrayList<WfGadgetBodyVO>();

		bodys.add(bodyVO);
	}

	public ArrayList<WfGadgetBodyVO> getBodys() {
		if (bodys == null)
			bodys = new ArrayList<WfGadgetBodyVO>();

		return bodys;
	}

	/**
	 * ����classname��Getter����.
	 * ��������:2008-08-20 11:08:40
	 * @return String
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * ����classname��Setter����.
	 * ��������:2008-08-20 11:08:40
	 * @param newClassname String
	 */
	public void setClassname(String newClassname) {
		this.classname = newClassname;
	}

	/**
	 * ����pk_workflowgadget��Getter����.
	 * ��������:2008-08-20 11:08:40
	 * @return String
	 */
	public String getPk_workflowgadget() {
		return pk_workflowgadget;
	}

	/**
	 * ����pk_workflowgadget��Setter����.
	 * ��������:2008-08-20 11:08:40
	 * @param newPk_workflowgadget String
	 */
	public void setPk_workflowgadget(String newPk_workflowgadget) {
		this.pk_workflowgadget = newPk_workflowgadget;
	}

	/**
	 * ����ts��Getter����.
	 * ��������:2008-08-20 11:08:40
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * ����ts��Setter����.
	 * ��������:2008-08-20 11:08:40
	 * @param newTs UFDateTime
	 */
	public void setTs(UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * ����billtype��Getter����.
	 * ��������:2008-08-20 11:08:40
	 * @return String
	 */
	public String getBilltype() {
		return billtype;
	}

	/**
	 * ����billtype��Setter����.
	 * ��������:2008-08-20 11:08:40
	 * @param newBilltype String
	 */
	public void setBilltype(String newBilltype) {
		this.billtype = newBilltype;
	}

	/**
	 * ����dr��Getter����.
	 * ��������:2008-08-20 11:08:40
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * ����dr��Setter����.
	 * ��������:2008-08-20 11:08:40
	 * @param newDr Integer
	 */
	public void setDr(Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * ����gadget_name��Getter����.
	 * ��������:2008-08-20 11:08:40
	 * @return String
	 */
	public String getGadget_name() {
		return gadget_name;
	}

	/**
	 * ����gadget_name��Setter����.
	 * ��������:2008-08-20 11:08:40
	 * @param newGadget_name String
	 */
	public void setGadget_name(String newGadget_name) {
		this.gadget_name = newGadget_name;
	}

	/**
	 * ����resid��Getter����.
	 * ��������:2008-08-20 11:08:40
	 * @return String
	 */
	public String getResid() {
		return resid;
	}

	/**
	 * ����resid��Setter����.
	 * ��������:2008-08-20 11:08:40
	 * @param newResid String
	 */
	public void setResid(String newResid) {
		this.resid = newResid;
	}

	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2008-08-20 11:08:40
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
		return null;
	}

	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2008-08-20 11:08:40
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
		return "pk_workflowgadget";
	}

	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2008-08-20 11:08:40
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "pub_workflowgadget";
	}

	/**
	* ����Ĭ�Ϸ�ʽ����������.
	*
	* ��������:2008-08-20 11:08:40
	*/
	public WorkflowgadgetVO() {
		super();
	}

	public String toString() {
		if (StringUtil.isEmptyWithTrim(getResid()))
			return getGadget_name();

		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID("workflowgadget", getResid());
		if (getResid().equals(strName)) {
			//ȡ����������Դ��
			strName = getGadget_name();
		}
		return strName;
	}

	/**
	 * �����Լ�����������ʵ��
	 * @param oldVO
	 * @return
	 */
	public static WorkflowgadgetVO copySelfWithoutBody(WorkflowgadgetVO oldVO) {
		WorkflowgadgetVO newVO = new WorkflowgadgetVO();
		newVO.setBilltype(oldVO.getBilltype());
		newVO.setClassname(oldVO.getClassname());
		newVO.setGadget_name(oldVO.getGadget_name());
		newVO.setPk_workflowgadget(oldVO.getPrimaryKey());
		newVO.setResid(oldVO.getResid());
		return newVO;
	}

	/**
	 * �����Լ���������ʵ��
	 * @param oldVO
	 * @return
	 */
	public static WorkflowgadgetVO copySelfWithBody(WorkflowgadgetVO oldVO) {
		WorkflowgadgetVO newVO = new WorkflowgadgetVO();
		newVO.setBilltype(oldVO.getBilltype());
		newVO.setClassname(oldVO.getClassname());
		newVO.setGadget_name(oldVO.getGadget_name());
		newVO.setPk_workflowgadget(oldVO.getPrimaryKey());
		newVO.setResid(oldVO.getResid());

		for (WfGadgetBodyVO oldBody : oldVO.getBodys()) {
			newVO.addBodyVO((WfGadgetBodyVO) oldBody.clone());
		}
		return newVO;
	}
}
