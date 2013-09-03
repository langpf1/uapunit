package test.biz.purchaseorder.itf;

import nc.vo.pub.BusinessException;
import test.biz.purchaseorder.vo.AggTestPO;
import test.biz.purchaseorder.vo.TestPODetail;
import test.biz.purchaseorder.vo.TestPOMaster;

public interface IServicesInterface {
	void SaveMaster(TestPOMaster vo) throws BusinessException;
	void SaveDetail(TestPODetail vo) throws BusinessException;
	TestPOMaster getMasterByID(String ID) throws BusinessException;
	TestPODetail getDetailByID(String ID) throws BusinessException;
	AggTestPO getPO(String ID) throws BusinessException;
	AggTestPO[] getPO() throws BusinessException;
	AggTestPO SavePO(AggTestPO po) throws BusinessException;
	AggTestPO[] SavePO(AggTestPO[] pos) throws BusinessException;
	AggTestPO[] SavePOByWorkflow(Object[] pos) throws BusinessException;
	
}
