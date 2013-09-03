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
		setRefNodeName("业务动作（集团）");
		setRootVisible(false);
		setClassTableName("bd_billtype");
		setClassFatherField("parentbilltype");
		setClassChildField("pk_billtypeid");
		setClassFieldCode(new String[] { "pk_billtypecode", "billtypename", "pk_billtypeid", "parentbilltype" });
		setClassJoinField("pk_billtypeid");
		//单据类型或者本集团下的交易类型
		setClassWherePart("(istransaction='Y' and pk_group ='"+InvocationInfoProxy.getInstance().getGroupId()+"') or istransaction='N'");

		// *根据需求设置相应参数
		setFieldCode(new String[] { "actiontype", "actionnote" });
		setFieldName(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003279")/*
																											 * @
																											 * res
																											 * "编码"
																											 */, NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001155") /*
																																														 * @
																																														 * res
																																														 * "名称"
																																														 */});
		setHiddenFieldCode(new String[] { "pk_billaction","pk_billtypeid"});
		setPkFieldCode("pk_billaction");
		
		setRefCodeField("actiontype");
		setRefNameField( "actionnote");
		
		setTableName("pub_billaction");
		setDocJoinField("pk_billtypeid");
		// 是管理员类型或者普通业务角色类型
		resetFieldName();
	}


}
