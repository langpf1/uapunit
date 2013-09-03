package uap.workflow.ui.participant.editors;

import nc.bs.framework.common.InvocationInfoProxy;

/**
 * 流程平台 角色参照
 * @author dingxm
 *
 */
public class PFRoleRefModel extends RefModelWithExtInfo {	
	public PFRoleRefModel() {
		setRefNodeName("角色");
		init();
	}
	
	public PFRoleRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		init();
	}

	private void init() {
		setFieldCode(new String[] { "role_code", "role_name" });
		setFieldName(new String[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("101612", "UPT101612-000001")/* @res "角色编码" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("101612", "UPT101612-000012") /* @res "角色名称" */});
		setHiddenFieldCode(new String[] { "pk_role" });
		setPkFieldCode("pk_role");
		//		setRefCodeField("role_code");
		//		setRefNameField("role_name");
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		defaultClause ="where  pk_group='" + pk_group+"' ";			 
		setTableName("sm_role");
		setWherePart(defaultClause);
		resetFieldName();
	}
}
