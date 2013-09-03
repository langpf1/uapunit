package uap.workflow.nc.participant.wfusergroup;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * �����û������͵�ö��
 * @author yuyonga
 */
public enum WfUserGroupType {
	RoleGroup("04"),
	Role("03"),
	UserGroup("02"),
	User("01");
	
	//ö�ٵ�����ֵ
	private String strValue;
	
	/**
	 * ö�ٵĹ��췽��
	 * @param intValue
	 */
	private WfUserGroupType(String strValue) {
		this.strValue = strValue;
	}

	public String getValue() {
		return this.strValue;
	}
	
	public static WfUserGroupType fromIntValue(String strValue) {
		if(strValue.equals("04")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000000")/*��ɫ��*/)) {
			return RoleGroup;
		}else if(strValue.equals("03")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000411")/*��ɫ*/)) {
			return Role;
		}else if(strValue.equals("02")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000001")/*�û���*/)) {
			return UserGroup;
		}else if(strValue.equals("01")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000412")/*�û�*/)){
			return User;
		}else
			return null;
	}
	
	public String toString() {
		if(getValue().equals("04")) {
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000000")/*��ɫ��*/;
		}else if(getValue().equals("03")){
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000411")/*��ɫ*/;
	    }else if(getValue().equals("02")){
	    	    return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000001")/*�û���*/;
	    }else if(getValue().equals("01")){
	    	    return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000412")/*�û�*/;
	    }else{
		        return "UnknownStatus";
	    }
	}
}
