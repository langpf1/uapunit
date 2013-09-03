package uap.workflow.nc.participant.wfusergroup;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 流程用户组类型的枚举
 * @author yuyonga
 */
public enum WfUserGroupType {
	RoleGroup("04"),
	Role("03"),
	UserGroup("02"),
	User("01");
	
	//枚举的整型值
	private String strValue;
	
	/**
	 * 枚举的构造方法
	 * @param intValue
	 */
	private WfUserGroupType(String strValue) {
		this.strValue = strValue;
	}

	public String getValue() {
		return this.strValue;
	}
	
	public static WfUserGroupType fromIntValue(String strValue) {
		if(strValue.equals("04")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000000")/*角色组*/)) {
			return RoleGroup;
		}else if(strValue.equals("03")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000411")/*角色*/)) {
			return Role;
		}else if(strValue.equals("02")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000001")/*用户组*/)) {
			return UserGroup;
		}else if(strValue.equals("01")||strValue.equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000412")/*用户*/)){
			return User;
		}else
			return null;
	}
	
	public String toString() {
		if(getValue().equals("04")) {
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000000")/*角色组*/;
		}else if(getValue().equals("03")){
				return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000411")/*角色*/;
	    }else if(getValue().equals("02")){
	    	    return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfUserGroupType-000001")/*用户组*/;
	    }else if(getValue().equals("01")){
	    	    return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000412")/*用户*/;
	    }else{
		        return "UnknownStatus";
	    }
	}
}
