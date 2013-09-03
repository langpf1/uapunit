package uap.workflow.modeler.refmodels;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.vo.ml.NCLangRes4VoTransl;

public class BizActionRefGridTreeModel extends AbstractRefGridTreeModel {
	public BizActionRefGridTreeModel() {
		super();
		reset();
	}

	@Override
	public void reset() {
		setRefNodeName("ҵ���������ţ�");
		setRootVisible(false);
		setClassTableName("bd_billtype");
		setClassFatherField("parentbilltype");
		setClassChildField("pk_billtypeid");
		setClassFieldCode(new String[] { "pk_billtypecode", "billtypename", "pk_billtypeid", "parentbilltype" });
		setClassJoinField("pk_billtypeid");
		//�������ͻ��߱������µĽ�������
		setClassWherePart("(istransaction='Y' and pk_group ='"+InvocationInfoProxy.getInstance().getGroupId()+"') or istransaction='N'");

		// *��������������Ӧ����
		setFieldCode(new String[] { "actiontype", "actionnote" });
		setFieldName(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003279")/*
																											 * @
																											 * res
																											 * "����"
																											 */, NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001155") /*
																																														 * @
																																														 * res
																																														 * "����"
																																														 */});
		setHiddenFieldCode(new String[] { "pk_billaction","pk_billtypeid"});
		setPkFieldCode("pk_billaction");
		
		setRefCodeField("actiontype");
		setRefNameField( "actionnote");
		
		setTableName("pub_billaction");
		setDocJoinField("pk_billtypeid");
		// �ǹ���Ա���ͻ�����ͨҵ���ɫ����
		resetFieldName();
	}


}
