package uap.workflow.bizimpl.bizinvocation;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
//import test.biz.praybill.vo.AggTestPrayBill;
import test.biz.praybill.vo.AggTestPrayBill;
import uap.workflow.app.vochange.IPfExchangeService;
import uap.workflow.app.vochange.PfExchangeServiceImpl;

public class VOExchange {

	public Map<String, AggregatedValueObject[]> GenerateBillProxy(String destBills){
		String [] splits = destBills.split(";");
		return generateBill("", splits, null, null,null);
	}

	public Map<String, AggregatedValueObject[]> generateBill(String srcBillType, String[] destBills, 
		AggregatedValueObject srcBillVO, Object userObj,PfParameterVO srcParaVo){
	
		Map<String, AggregatedValueObject[]> destVos = new HashMap<String, AggregatedValueObject[]>();
		
		String destBillType = null;
		
		for (int i = 0; i < destBills.length; i++) { // 遍历被驱动的动作
		
			destBillType = destBills[i];

			// 进行VO数据转换
			IPfExchangeService exchangeService = new PfExchangeServiceImpl(); 
				//NCLocator.getInstance().lookup(IPfExchangeService.class);
			try {
				srcParaVo = new PfParameterVO();
				srcParaVo.m_billId = srcBillVO.getParentVO().getPrimaryKey();
				srcParaVo.m_billType = srcBillType;
				srcParaVo.m_preValueVo = srcBillVO;
				srcParaVo.m_preValueVos = new AggregatedValueObject[]{srcBillVO};
				destVos.put(destBillType, 
						exchangeService.runChangeDataAry(
								srcBillType, 
								destBillType, 
								new AggregatedValueObject[] { srcBillVO }, 
								srcParaVo));
			} catch (BusinessException e) {
				e.printStackTrace();
			}

		}
		return destVos;
	}
	
	public Map<String, AggregatedValueObject[]> generateBillAccordingObject(String srcBillType, String[] destBills, 
			AggregatedValueObject srcBillVO){
		
			Map<String, AggregatedValueObject[]> destVos = new HashMap<String, AggregatedValueObject[]>();
			
			String destBillType = null;
			AggregatedValueObject[] aggs = null;
			for (int i = 0; i < destBills.length; i++) { // 遍历被驱动的动作
			
				destBillType = destBills[i];
				try {
					if (destBillType.equals("test.biz.rcvbill.vo.TestRcvBillMaster")){
						//执行单元测试需要注掉，kongxla
						aggs = AggTestPrayBill.toRcvBill(srcBillVO);
					}else /*if (destBillType.equals(""))*/{
						//执行单元测试需要注掉，kongxla
						aggs = AggTestPrayBill.toPO(srcBillVO);
					}
					destVos.put(destBillType, aggs);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return destVos;
		}
	
	
	public AggregatedValueObject[] getVOExchangedResult(String billType, Object vos){
		if (vos != null && (vos instanceof Map) && billType != null && !billType.isEmpty()){
			AggregatedValueObject[] billVOs = (AggregatedValueObject[])((Map)vos).get(billType);
			return billVOs;
		}
		return null;
	}
}
