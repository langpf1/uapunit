package uap.workflow.app.impl;

import java.util.HashMap;
import java.util.Hashtable;

import nc.vo.pub.compiler.PfParameterVO;


/**
 * 流程平台动作触发后的环境信息
 * 
 * @author wzhy 2004-2-21
 * @modifier leijun 2008-7 修改为缓存用途
 * @modifier leijun 2008-12 使用ThreadLocal防止并发
 */
public class ActionEnvironment {
	private static ActionEnvironment cen = new ActionEnvironment();

	private ThreadLocal<HashMap<String, Hashtable>> hmMethodReturns = new ThreadLocal<HashMap<String, Hashtable>>() {
		protected HashMap<String, Hashtable> initialValue() {
			return new HashMap<String, Hashtable>();
		}
	};

	//以单据ID为key,索引paraVO和methodReturn
	private ThreadLocal<HashMap<String, PfParameterVO>> hmParaVOs = new ThreadLocal<HashMap<String, PfParameterVO>>() {
		protected HashMap<String, PfParameterVO> initialValue() {
			return new HashMap<String, PfParameterVO>();
		}
	};

	public static ActionEnvironment getInstance() {
		return cen;
	}

	private ActionEnvironment() {
		super();
	}

	public void putParaVo(String billId, PfParameterVO paraVo) {
		if (paraVo == null)
			this.hmParaVOs.get().remove(billId);
		else
			this.hmParaVOs.get().put(billId, paraVo);
	}

	public PfParameterVO getParaVo(String billId) {
		return hmParaVOs.get().get(billId);
	}

	public void putMethodReturn(String billId, Hashtable htReturn) {
		if (htReturn == null)
			this.hmMethodReturns.get().remove(billId);
		else
			this.hmMethodReturns.get().put(billId, htReturn);
	}

	public Hashtable getMethodReturn(String billId) {
		return hmMethodReturns.get().get(billId);
	}

}