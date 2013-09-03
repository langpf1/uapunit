package uap.workflow.app.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.logging.Logger;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.change.PublicHeadVO;

@SuppressWarnings("serial")
public class PFBatchExceptionInfo implements Serializable {
	
	//第几个单据-单据VO
	private HashMap<Integer, Object> hm_index_billVO = new HashMap<Integer, Object>();
	//第几个单据-异常信息
	private HashMap<Integer, String> hm_index_errmsg = new HashMap<Integer, String>();
	
	public HashMap<Integer, String> getErrorMessageMap(){
		return this.hm_index_errmsg;
	}
	
	public String getErrorMessage() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Integer> iterator = hm_index_errmsg.keySet().iterator(); iterator.hasNext();) {
			int i = iterator.next();
			// 获取主表数据
			PublicHeadVO headVo = new PublicHeadVO();
			PfUtilBaseTools.getHeadInfoByMeta(headVo, hm_index_billVO.get(i), null);
			
			sb.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PFBatchExceptionInfo-000000")/*单据执行失败>>单据号=*/);
			sb.append(headVo.billNo);
			sb.append(">>" + hm_index_errmsg.get(i).trim());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public void putErrorMessage(int i, Object billvo, String errMsg) {
		hm_index_errmsg.put(i, errMsg);
		hm_index_billVO.put(i, billvo);
	}
	
	public boolean hasError() {
		Logger.debug("后台动作批处理过程中，" + hm_index_errmsg.size() + "张单据发生业务异常。");
		return hm_index_errmsg.size() > 0;
	}

}
