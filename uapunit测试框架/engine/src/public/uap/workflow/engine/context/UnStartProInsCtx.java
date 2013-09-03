package uap.workflow.engine.context;
import uap.workflow.app.core.FlowInfoCtx;
/**
 * 
 * @author tianchw
 * 
 */
public abstract class UnStartProInsCtx extends FlowInfoCtx {
	private static final long serialVersionUID = -3612481724040667694L;
	protected String proDefPk = null;
	protected String proDefId = null;
	@Override
	public void check() {
		// if (StringUtils.isBlank(proDefPk)) {
		// throw new WorkflowRuntimeException("请提供流程定义Pk");
		// }
	}
	public String getProDefPk() {
		return proDefPk;
	}
	public String getProDefId() {
		return proDefId;
	}
	abstract public void setProDefId(String proDefId);
	abstract public void setProDefPk(String proDefPk);
}
