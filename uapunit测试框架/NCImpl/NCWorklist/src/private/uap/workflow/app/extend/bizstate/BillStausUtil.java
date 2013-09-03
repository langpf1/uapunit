package uap.workflow.app.extend.bizstate;

import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.ui.ml.NCLangRes;
import nc.vo.pub.AggregatedValueObject;
import uap.workflow.app.exception.PFRuntimeException;

public class BillStausUtil
{
	public int queryBillStatus(AggregatedValueObject billVo)
	{
		IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(billVo, IFlowBizItf.class);
		if (fbi == null)
			throw new PFRuntimeException(NCLangRes.getInstance().getStrByID("pfworkflow1", "PfUtilClient-000000")/*Ԫ����ʵ��û���ṩҵ��ӿ�IFlowBizItf��ʵ����*/);
		return fbi.getApproveStatus();
	}
}
