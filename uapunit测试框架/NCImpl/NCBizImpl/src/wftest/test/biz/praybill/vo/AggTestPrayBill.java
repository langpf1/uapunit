package test.biz.praybill.vo;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import test.biz.purchaseorder.vo.AggTestPO;
import test.biz.purchaseorder.vo.TestPODetail;
import test.biz.purchaseorder.vo.TestPOMaster;
import test.biz.rcvbill.vo.AggTestRcvBill;
import test.biz.rcvbill.vo.TestRcvBillDetail;
import test.biz.rcvbill.vo.TestRcvBillMaster;

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:
 * @author 
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
@nc.vo.annotation.AggVoInfo(parentVO = "test.biz.purchaseorder.TestPOMaster")
public class  AggTestPrayBill extends AggregatedValueObject {

	private CircularlyAccessibleValueObject masterVO;
	private CircularlyAccessibleValueObject[] detailVO;
	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return detailVO;
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return masterVO;
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		detailVO = children;
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parentVO) {
		this.masterVO = parentVO;
	}
 
	public static AggregatedValueObject[] toPO(AggregatedValueObject agg){
		
		AggTestPrayBill aggVO = (AggTestPrayBill)agg;
		AggregatedValueObject resultAgg = null;
		List<AggregatedValueObject> resultAggs = new ArrayList<AggregatedValueObject>();
		TestPOMaster masterPO = new TestPOMaster();
		
		//copy header
		TestPrayBillMaster masterPB = (TestPrayBillMaster)aggVO.getParentVO();
		masterPO.setSum(masterPB.getSum());
		masterPO.setSupplier(masterPB.getSupplier());
		resultAgg = new AggTestPO();
		resultAgg.setParentVO(masterPO);
		
		List<TestPODetail> detailPOs = new ArrayList<TestPODetail>();
		TestPODetail detailPO = null; 
		for(TestPrayBillDetail detail : (TestPrayBillDetail[])aggVO.getChildrenVO()){
			detailPO = new TestPODetail();
			detailPO.setMateriel(detail.getMateriel());
			detailPO.setPrice(detail.getPrice());
			detailPO.setQuanlity(detail.getQuanlity());
			detailPOs.add(detailPO);
		}
		resultAgg.setChildrenVO(detailPOs.toArray(new TestPODetail[0]));
		resultAggs.add(resultAgg);
		
		return resultAggs.toArray(new AggregatedValueObject[0]);
	}
	
	public static AggregatedValueObject[] toRcvBill(AggregatedValueObject agg){
		
		AggTestPrayBill aggVO = (AggTestPrayBill)agg;
		AggregatedValueObject resultAgg = null;
		List<AggregatedValueObject> resultAggs = new ArrayList<AggregatedValueObject>();
		TestRcvBillMaster masterPO = new TestRcvBillMaster();
		
		//copy header
		TestPrayBillMaster masterPB = (TestPrayBillMaster)aggVO.getParentVO();
		masterPO.setSum(masterPB.getSum());
		masterPO.setSupplier(masterPB.getSupplier());
		resultAgg = new AggTestRcvBill();
		resultAgg.setParentVO(masterPO);
		
		List<TestRcvBillDetail> detailPOs = new ArrayList<TestRcvBillDetail>();
		TestRcvBillDetail detailPO = null; 
		for(TestPrayBillDetail detail : (TestPrayBillDetail[])aggVO.getChildrenVO()){
			detailPO = new TestRcvBillDetail();
			detailPO.setMateriel(detail.getMateriel());
			detailPO.setPrice(detail.getPrice());
			detailPO.setQuanlity(detail.getQuanlity());
			detailPOs.add(detailPO);
		}
		resultAgg.setChildrenVO(detailPOs.toArray(new TestRcvBillDetail[0]));
		resultAggs.add(resultAgg);
		
		return resultAggs.toArray(new AggregatedValueObject[0]);
	}

}
