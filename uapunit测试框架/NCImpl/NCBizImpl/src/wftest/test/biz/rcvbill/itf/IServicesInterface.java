package test.biz.rcvbill.itf;

import nc.vo.pub.BusinessException;
import test.biz.rcvbill.vo.AggTestRcvBill;
import test.biz.rcvbill.vo.TestRcvBillDetail;
import test.biz.rcvbill.vo.TestRcvBillMaster;

public interface IServicesInterface {
	void SaveMaster(TestRcvBillMaster vo) throws BusinessException;
	void SaveDetail(TestRcvBillDetail vo) throws BusinessException;
	TestRcvBillMaster getMasterByID(String ID) throws BusinessException;
	TestRcvBillDetail getDetailByID(String ID) throws BusinessException;
	AggTestRcvBill getPO(String ID) throws BusinessException;
	AggTestRcvBill[] getPO() throws BusinessException;
	AggTestRcvBill SavePO(AggTestRcvBill po) throws BusinessException;
	AggTestRcvBill[] SavePOByWorkflow(Object[] pos) throws BusinessException;
	
}
