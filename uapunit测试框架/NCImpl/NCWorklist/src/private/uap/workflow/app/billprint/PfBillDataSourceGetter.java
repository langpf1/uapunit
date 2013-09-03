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

		// 先使用单据上注册的数据源获取类来取
		if (!StringUtil.isEmptyWithTrim(referClzName)) {
			Object o = PfUtilTools.instantizeObject(billType, referClzName.trim());
			if (o instanceof IPrintDataGetter) {
				String checkman = InvocationInfoProxy.getInstance().getUserId();
				
				try {
					ds = ((IPrintDataGetter) o).getPrintDs(billid, billType, checkman);
				} catch (BusinessException e) {
					Logger.error("获取单据打印数据源出错: " + e.getMessage() + ", 改用单据实体vo构造元数据数据源", e);
				}
			}
		}
		
		// yk1 2012-4-20
		// 单据的打印模版可能没有使用单据实体的元数据来配
		// 比如：客户申请单的打印模版使用的是客户实体的元数据配置的
		// 此种情况下，使用元数据数据源方式走不通
		// 对于需要取单据表体的情况如邮件通知，若上面的代码取不到数据源，那么就不取了
		// 后续发送邮件通知时就不附加表体了
//		// 若取不到，那么取单据的实体
//		// 生成元数据数据源
//		if (ds == null) {
//			Object busiObj = NCLocator.getInstance().lookup(IPFConfig.class).queryBillDataVO(billType, billid);
//			ds = new PfBillDataSource(new Object[] { busiObj });
//		}
		
		return ds;
	}
	
}
