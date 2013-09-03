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
	
	//�ڼ�������-����VO
	private HashMap<Integer, Object> hm_index_billVO = new HashMap<Integer, Object>();
	//�ڼ�������-�쳣��Ϣ
	private HashMap<Integer, String> hm_index_errmsg = new HashMap<Integer, String>();
	
	public HashMap<Integer, String> getErrorMessageMap(){
		return this.hm_index_errmsg;
	}
	
	public String getErrorMessage() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Integer> iterator = hm_index_errmsg.keySet().iterator(); iterator.hasNext();) {
			int i = iterator.next();
			// ��ȡ��������
			PublicHeadVO headVo = new PublicHeadVO();
			PfUtilBaseTools.getHeadInfoByMeta(headVo, hm_index_billVO.get(i), null);
			
			sb.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PFBatchExceptionInfo-000000")/*����ִ��ʧ��>>���ݺ�=*/);
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
		Logger.debug("��̨��������������У�" + hm_index_errmsg.size() + "�ŵ��ݷ���ҵ���쳣��");
		return hm_index_errmsg.size() > 0;
	}

}
