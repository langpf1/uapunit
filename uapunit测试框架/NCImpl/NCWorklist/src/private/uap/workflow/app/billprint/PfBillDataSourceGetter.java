package uap.workflow.app.billprint;

import uap.workflow.app.client.PfUtilTools;
import uap.workflow.pub.util.PfDataCache;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.IPrintDataGetter;
import nc.ui.pub.print.IDataSource;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;

public class PfBillDataSourceGetter {

	public static IDataSource getDataSourceOf(String billType, String billid) throws BusinessException {
		BilltypeVO btVO = PfDataCache.getBillType(billType);
		String referClzName = btVO.getReferclassname();

		IDataSource ds = null;

		// ��ʹ�õ�����ע�������Դ��ȡ����ȡ
		if (!StringUtil.isEmptyWithTrim(referClzName)) {
			Object o = PfUtilTools.instantizeObject(billType, referClzName.trim());
			if (o instanceof IPrintDataGetter) {
				String checkman = InvocationInfoProxy.getInstance().getUserId();
				
				try {
					ds = ((IPrintDataGetter) o).getPrintDs(billid, billType, checkman);
				} catch (BusinessException e) {
					Logger.error("��ȡ���ݴ�ӡ����Դ����: " + e.getMessage() + ", ���õ���ʵ��vo����Ԫ��������Դ", e);
				}
			}
		}
		
		// yk1 2012-4-20
		// ���ݵĴ�ӡģ�����û��ʹ�õ���ʵ���Ԫ��������
		// ���磺�ͻ����뵥�Ĵ�ӡģ��ʹ�õ��ǿͻ�ʵ���Ԫ�������õ�
		// ��������£�ʹ��Ԫ��������Դ��ʽ�߲�ͨ
		// ������Ҫȡ���ݱ����������ʼ�֪ͨ��������Ĵ���ȡ��������Դ����ô�Ͳ�ȡ��
		// ���������ʼ�֪ͨʱ�Ͳ����ӱ�����
//		// ��ȡ��������ôȡ���ݵ�ʵ��
//		// ����Ԫ��������Դ
//		if (ds == null) {
//			Object busiObj = NCLocator.getInstance().lookup(IPFConfig.class).queryBillDataVO(billType, billid);
//			ds = new PfBillDataSource(new Object[] { busiObj });
//		}
		
		return ds;
	}
	
}
