package uap.workflow.app.extend.action;

import java.util.HashSet;

import nc.bs.logging.Logger;
import nc.bs.pub.compiler.IPfRun;
import nc.jdbc.framework.exception.DbException;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.BusiclassVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.extend.BusiclassDMO;
import uap.workflow.app.vo.IPFConfigInfo;
import uap.workflow.pub.util.PfUtilBaseTools;

/**
 * 动作脚本执行器 
 * 
 * @author fangj 2005-01-24 15:20 
 * @modifier leijun 2005-12-31 禁止使用Class.forName()实例化其它模块的类
 */
public class PFRunClass {

	public PFRunClass() {
		super();
	}

	/**
	 * 查询pub_busiclass表中注册的默认动作脚本类
	 * @param billType 单据类型或交易类型
	 * @param actionName 动作编码
	 * @return
	 * @throws BusinessException
	 * @throws DbException
	 */
	private BusiclassVO queryDefaultActionScriptClass(String billType, String actionName)
			throws BusinessException, DbException {
		Logger.debug("从表pub_busiclass查询单据的默认动作脚本类开始......");
		HashSet<String> hsRet = PfUtilBaseTools.querySimilarTypes(billType);
		BusiclassDMO dmo = new BusiclassDMO();

		for (String strTypeCode : hsRet) {
			BusiclassVO bClassVO = dmo.queryByCond(UFBoolean.FALSE, null, strTypeCode, actionName, null);
			if (bClassVO != null) {
				Logger.debug("从表pub_busiclass查询单据的默认动作脚本类结束......" + bClassVO);
				return bClassVO;
			}
		}
		throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFRunClass-0000")/*从表pub_busiclass中找不到该单据的默认动作脚本类实例*/);
	}

	/**
	 * 从pub_busiclass表中查找注册的动作类，查找算法：
	 * <li>1.找流程配置定制过的动作脚本类，若没有，则
	 * <li>2.找默认动作脚本类，若没有，则
	 * <li>3.找父单据类型的默认动作脚本类
	 * 
	 * @param actionName 动作编码,比如"SAVE","EDIT"
	 * @param isBefore FIXME:一般都为FALSE?
	 * @param pfVo 工作流参数VO
	 * @return 
	 * @throws BusinessException
	 */
	private BusiclassVO findActionScriptClass(String actionName, UFBoolean isBefore,
			PfParameterVO pfVo) throws BusinessException {
		Logger.debug("从表pub_busiclass查询单据动作脚本类开始......");
		Logger.debug("*********************************************");
		Logger.debug("* actionName=" + actionName);
		Logger.debug("* billType=" + pfVo.m_billType);
		Logger.debug("* busiType=" + pfVo.m_businessType);
		Logger.debug("*********************************************");

		BusiclassVO bClassVO = null;
		try {
			BusiclassDMO dmo = new BusiclassDMO();
			if (isBefore.booleanValue()) {
				//可为空 XXX:目前5.5不可能走的分支？
				bClassVO = dmo.queryByCond(isBefore, null, pfVo.m_billType, actionName, null);
			} else {
				//1.查找流程配置定制过的动作脚本类（有业务类型属性就不用考虑公司属性）
				//bClassVO = dmo.queryByCond(isBefore, pfVo.m_coId, pfVo.m_billType, actionName, pfVo.m_businessType);
				bClassVO = dmo
						.queryByCond(isBefore, null, pfVo.m_billType, actionName, pfVo.m_businessType);
				if (bClassVO == null)
					//2.查找默认动作脚本类
					bClassVO = queryDefaultActionScriptClass(pfVo.m_billType, actionName);
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFRunClass-0001", null, new String[]{e.getMessage()})/*从表pub_busiclass查询动作脚本类发生数据库异常={0}*/);
		}

		Logger.debug("从表pub_busiclass查询单据动作脚本类结束......");
		return bClassVO;
	}

	/**
	 * 动作脚本类实例化后 执行
	 * 
	 * @param pfVo
	 * @param isBefore
	 * @param actionName 动作编码,比如"SAVE","EDIT"
	 * @return
	 */
	public Object runComBusi(PfParameterVO pfVo, UFBoolean isBefore, String actionName)
			throws BusinessException {
		Logger.debug("动作脚本执行开始.....");

		//查找动作脚本在pub_busiclass表注册的信息
		BusiclassVO busiClassVo = findActionScriptClass(actionName, isBefore, pfVo);
		String strClassNameNoPackage = busiClassVo.getClassname().trim();
		String className = IPFConfigInfo.ActionPack + "." + strClassNameNoPackage;
		pfVo.m_actionName = actionName;

		Object instOfActionscript = PfUtilTools.findBizImplOfBilltype(busiClassVo.getPk_billtype(),	className);
		Object actionReturnObj = ((IPfRun) instOfActionscript).runComClass(pfVo);

		Logger.debug("动作脚本执行完成....." + instOfActionscript);
		return actionReturnObj;
	}
}