package uap.workflow.ui.participant.editors;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.ml.NCLangRes;

public class PFUserGroupRefModel extends RefModelWithExtInfo{
	public PFUserGroupRefModel() {
		setRefNodeName("用户组");
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
		setFieldName(new String[] {NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000053")/*用户组编码*/,NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000054")/*用户组名称*/});
		setHiddenFieldCode(new String[] { "pk_usergroup" });
		setPkFieldCode("pk_usergroup");
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		defaultClause ="where  pk_group='" + pk_group+"' ";			 
		setTableName("sm_usergroup");
		setWherePart(defaultClause);
		resetFieldName();
	}
	
}
