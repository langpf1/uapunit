package uap.workflow.ui.participant.editors;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.ml.NCLangRes;

public class PFUserRefModel extends RefModelWithExtInfo {

	public PFUserRefModel() {
		setRefNodeName("用户");
		init();
	}

	public PFUserRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		init();
	}

	private void init() {
		setFieldCode(new String[] { "user_code", "user_name" });
		setFieldName(new String[] { NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000055")/*用户编码*/,NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000056")/*用户名称*/});
		setHiddenFieldCode(new String[] { "cuserid" });
		setPkFieldCode("cuserid");
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		defaultClause ="where  pk_group='" + pk_group+"' ";			 
		setTableName("sm_user");
		setWherePart(defaultClause);
		resetFieldName();
	}

}
