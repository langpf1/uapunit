package uap.workflow.app.extend.gadget;

import java.util.ArrayList;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * 工作流组件注册表pub_workflowgadget对应的VO
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

	//非数据库字段，子表信息
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
	 * 属性classname的Getter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @return String
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * 属性classname的Setter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @param newClassname String
	 */
	public void setClassname(String newClassname) {
		this.classname = newClassname;
	}

	/**
	 * 属性pk_workflowgadget的Getter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @return String
	 */
	public String getPk_workflowgadget() {
		return pk_workflowgadget;
	}

	/**
	 * 属性pk_workflowgadget的Setter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @param newPk_workflowgadget String
	 */
	public void setPk_workflowgadget(String newPk_workflowgadget) {
		this.pk_workflowgadget = newPk_workflowgadget;
	}

	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @param newTs UFDateTime
	 */
	public void setTs(UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * 属性billtype的Getter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @return String
	 */
	public String getBilltype() {
		return billtype;
	}

	/**
	 * 属性billtype的Setter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @param newBilltype String
	 */
	public void setBilltype(String newBilltype) {
		this.billtype = newBilltype;
	}

	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @param newDr Integer
	 */
	public void setDr(Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * 属性gadget_name的Getter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @return String
	 */
	public String getGadget_name() {
		return gadget_name;
	}

	/**
	 * 属性gadget_name的Setter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @param newGadget_name String
	 */
	public void setGadget_name(String newGadget_name) {
		this.gadget_name = newGadget_name;
	}

	/**
	 * 属性resid的Getter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @return String
	 */
	public String getResid() {
		return resid;
	}

	/**
	 * 属性resid的Setter方法.
	 * 创建日期:2008-08-20 11:08:40
	 * @param newResid String
	 */
	public void setResid(String newResid) {
		this.resid = newResid;
	}

	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2008-08-20 11:08:40
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
		return null;
	}

	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2008-08-20 11:08:40
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
		return "pk_workflowgadget";
	}

	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2008-08-20 11:08:40
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "pub_workflowgadget";
	}

	/**
	* 按照默认方式创建构造子.
	*
	* 创建日期:2008-08-20 11:08:40
	*/
	public WorkflowgadgetVO() {
		super();
	}

	public String toString() {
		if (StringUtil.isEmptyWithTrim(getResid()))
			return getGadget_name();

		String strName = NCLangRes4VoTransl.getNCLangRes().getStrByID("workflowgadget", getResid());
		if (getResid().equals(strName)) {
			//取不到多语资源串
			strName = getGadget_name();
		}
		return strName;
	}

	/**
	 * 复制自己，不包括子实体
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
	 * 复制自己，包括子实体
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
