package uap.workflow.engine.session;
/*当前环境下的执行人，组织，集团    zhai-z*/
public abstract class WorkflowRuntimeEnviroment {
	public static final ThreadLocal<WorkflowSysVar> bpmnContext = new ThreadLocal<WorkflowSysVar>();
	public static WorkflowSysVar getSysVar() {
		return bpmnContext.get();
	}
	public static void setSysVar(WorkflowSysVar sysVar) {
		bpmnContext.set(sysVar);
	}
	public static void removerSysVar() {
		bpmnContext.remove();
	}
	public void initSysVar() {
		WorkflowRuntimeEnviroment.getSysVar().setPk_group(this.getPk_group());
		WorkflowRuntimeEnviroment.getSysVar().setPk_org(this.getPk_org());
		WorkflowRuntimeEnviroment.getSysVar().setPk_user(this.getPk_user());
	}
	abstract public String getPk_group();
	abstract public String getPk_org();
	abstract public String getPk_user();
	public class WorkflowSysVar {
		private String pk_group;
		private String pk_org;
		private String pk_user;
		public String getPk_group() {
			return pk_group;
		}
		public void setPk_group(String pk_group) {
			this.pk_group = pk_group;
		}
		public String getPk_org() {
			return pk_org;
		}
		public void setPk_org(String pk_org) {
			this.pk_org = pk_org;
		}
		public String getPk_user() {
			return pk_user;
		}
		public void setPk_user(String pk_user) {
			this.pk_user = pk_user;
		}
	}
}
