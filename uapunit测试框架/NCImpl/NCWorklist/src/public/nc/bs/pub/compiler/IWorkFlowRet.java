package nc.bs.pub.compiler;

/**
 * 只有脚本调用procActionFlow@@，且在流程审批进行中时才返回该对象；
 * 即单据在 非审批通过状态时 的单据动作脚本对procActionFlow调用的返回值。
 * 
 * @author fangj 2002-4-16
 * @modifier leijun 2008-2-26 将成员变量修改为通用单据实体对象，不再局限于聚合VO 
 */
public class IWorkFlowRet {
	/**
	 * 单据动作返回修改过的业务单据实体对象
	 */
	//public AggregatedValueObject m_inVo = null;
	public Object m_inVo = null;

	public IWorkFlowRet() {
		super();
	}
}