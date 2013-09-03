package uap.workflow.modeler.refmodels;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.ml.NCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;

public class BizActionRefTreeModel extends AbstractRefTreeModel {

	public BizActionRefTreeModel() {
		super();
		reset();
	}

	public void reset() {
		setRefNodeName("业务动作（集团）");

		// *根据需求设置相应参数
		setClassProperty();

		setFieldCode(new String[] { "pub_billaction.actiontype", "pub_billaction.actionnote" });
		setFieldName(new String[] { 
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003279") /* @res "编码" */,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001155") /* @res "名称" */
					});
		setHiddenFieldCode(new String[] { "pub_billaction.pk_billtypeid", "pub_billaction.pk_billaction"});
		
		setPkFieldCode("pk_billaction");
		setRefCodeField("actiontype");
		setRefNameField( "actionnote");
		setFatherField("pk_billtypeid");
		setChildField("actionnote");
		
		setRefTitle(NCLangRes.getInstance().getStrByID("sfapp", "mdmanage01-000020")/* 元数据实体 */);
		setTableName("pub_billaction");
		setOrderPart("actiontype");
		resetFieldName();
	}

	private void setClassProperty() {

		setClassFieldCode(new String[] { "pk_billtypecode", "billtypename", "pk_billtypeid" });
		setClassJoinField("pk_billtypeid");
		setClassTableName("bd_billtype");
		setClassFatherField("parentbilltype");
		setClassChildField("pk_billtypedid");
		
	}

	@Override
	protected String getEnvWherePart() {
		return "";
	}
}
