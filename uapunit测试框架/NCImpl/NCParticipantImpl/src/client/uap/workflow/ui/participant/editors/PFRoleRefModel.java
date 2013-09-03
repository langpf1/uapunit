package uap.workflow.ui.participant.editors;

import nc.bs.framework.common.InvocationInfoProxy;

/**
 * ����ƽ̨ ��ɫ����
 * @author dingxm
 *
 */
public class PFRoleRefModel extends RefModelWithExtInfo {	
	public PFRoleRefModel() {
		setRefNodeName("��ɫ");
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
				nc.ui.ml.NCLangRes.getInstance().getStrByID("101612", "UPT101612-000001")/* @res "��ɫ����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("101612", "UPT101612-000012") /* @res "��ɫ����" */});
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
