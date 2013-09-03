package test.biz.praybill.itf;

import nc.vo.pub.BusinessException;
import test.biz.praybill.vo.AggTestPrayBill;
import test.biz.praybill.vo.TestPrayBillDetail;
import test.biz.praybill.vo.TestPrayBillMaster;
import test.biz.purchaseorder.vo.AggTestPO;

public interface IServicesInterface {
	void SaveMaster(TestPrayBillMaster vo) throws BusinessException;
	void SaveDetail(TestPrayBillDetail vo) throws BusinessException;
	TestPrayBillMaster getMasterByID(String ID) throws BusinessException;
	TestPrayBillDetail getDetailByID(String ID) throws BusinessException;
	AggTestPrayBill getPrayBill(String ID) throws BusinessException;
	AggTestPrayBill[] getPrayBill() throws BusinessException;
	AggTestPrayBill SavePrayBill(AggTestPrayBill po) throws BusinessException;
	AggTestPrayBill[] SavePrayBillByWorkflow(Object[] pos) throws BusinessException;
}
