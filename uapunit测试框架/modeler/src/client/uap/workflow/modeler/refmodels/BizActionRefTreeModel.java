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
		setRefNodeName("ҵ���������ţ�");

		// *��������������Ӧ����
		setClassProperty();

		setFieldCode(new String[] { "pub_billaction.actiontype", "pub_billaction.actionnote" });
		setFieldName(new String[] { 
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003279") /* @res "����" */,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001155") /* @res "����" */
					});
		setHiddenFieldCode(new String[] { "pub_billaction.pk_billtypeid", "pub_billaction.pk_billaction"});
		
		setPkFieldCode("pk_billaction");
		setRefCodeField("actiontype");
		setRefNameField( "actionnote");
		setFatherField("pk_billtypeid");
		setChildField("actionnote");
		
		setRefTitle(NCLangRes.getInstance().getStrByID("sfapp", "mdmanage01-000020")/* Ԫ����ʵ�� */);
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
