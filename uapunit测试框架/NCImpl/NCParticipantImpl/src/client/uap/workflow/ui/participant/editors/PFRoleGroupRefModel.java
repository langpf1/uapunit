package uap.workflow.ui.participant.editors;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.ml.NCLangRes;

public class PFRoleGroupRefModel extends RefModelWithExtInfo{

	public PFRoleGroupRefModel() {
		setRefNodeName("角色组");
		init();
	}

	public PFRoleGroupRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		init();
	}

	private void init() {
		setFieldCode(new String[] { "group_code", "group_name" });
		setFieldName(new String[] {NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000051")/*角色组编码*/,NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000052")/*角色组名称*/});
		setHiddenFieldCode(new String[] { "pk_role_group" });
		setPkFieldCode("pk_role_group");
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		defaultClause ="where  pk_group='" + pk_group+"' ";			 
		setTableName("sm_role_group");
		setWherePart(defaultClause);
		resetFieldName();
	}
}
