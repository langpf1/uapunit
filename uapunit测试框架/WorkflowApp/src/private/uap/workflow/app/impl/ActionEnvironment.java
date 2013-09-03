package uap.workflow.app.impl;

import java.util.HashMap;
import java.util.Hashtable;

import nc.vo.pub.compiler.PfParameterVO;


/**
 * ����ƽ̨����������Ļ�����Ϣ
 * 
 * @author wzhy 2004-2-21
 * @modifier leijun 2008-7 �޸�Ϊ������;
 * @modifier leijun 2008-12 ʹ��ThreadLocal��ֹ����
 */
public class ActionEnvironment {
	private static ActionEnvironment cen = new ActionEnvironment();

	private ThreadLocal<HashMap<String, Hashtable>> hmMethodReturns = new ThreadLocal<HashMap<String, Hashtable>>() {
		protected HashMap<String, Hashtable> initialValue() {
			return new HashMap<String, Hashtable>();
		}
	};

	//�Ե���IDΪkey,����paraVO��methodReturn
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