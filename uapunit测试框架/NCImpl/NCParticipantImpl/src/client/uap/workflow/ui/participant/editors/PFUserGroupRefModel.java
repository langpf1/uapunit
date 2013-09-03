package uap.workflow.ui.participant.editors;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.ml.NCLangRes;

public class PFUserGroupRefModel extends RefModelWithExtInfo{
	public PFUserGroupRefModel() {
		setRefNodeName("�û���");
		init();
	}
	
	public PFUserGroupRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		init();
	}

	private void init() {
		setFieldCode(new String[] { "groupcode", "groupname" });
		setFieldName(new String[] {NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000053")/*�û������*/,NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000054")/*�û�������*/});
		setHiddenFieldCode(new String[] { "pk_usergroup" });
		setPkFieldCode("pk_usergroup");
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		defaultClause ="where  pk_group='" + pk_group+"' ";			 
		setTableName("sm_usergroup");
		setWherePart(defaultClause);
		resetFieldName();
	}
	
}
