package uap.workflow.app.vochange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pf.change.BillMappingConvertor;
import nc.bs.pf.change.BillSplitHelper;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.bs.pf.pub.ExchangeRuleVOListProcessor;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.busiflow.ClassifyContext;
import nc.itf.uap.pf.busiflow.IPfBusiflowService;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.ExchangeRuleVO;
import nc.vo.pf.change.ExchangeSplitVO;
import nc.vo.pf.change.ExchangeVO;
import nc.vo.pf.change.PFExchangeUtil;
import nc.vo.pf.change.RuleTypeEnum;
import nc.vo.pf.change.SplitItemVO;
import nc.vo.pf.pub.FunctionVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.change.PublicHeadVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.vo.IPFConfigInfo;
import uap.workflow.pub.util.PfDataCache;
import uap.workflow.pub.util.PfUtilBaseTools;

public class PfExchangeServiceImpl implements IPfExchangeService {

	
	@Override
	public AggregatedValueObject runChangeData(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject srcBillVO, PfParameterVO paraVo)
			throws BusinessException {
		if (srcBillVO == null)
			return null;

		Logger.debug(">>开始单据VO交换=" + srcBillOrTranstype + "到" + destBillOrTranstype);
		long start = System.currentTimeMillis();

		// 从数据库查询交换规则
		String group = null;
		if(paraVo != null && paraVo.m_pkGroup != null)
			group = paraVo.m_pkGroup;
		else
			group = InvocationInfoProxy.getInstance().getGroupId();
		
		ExchangeVO chgVo = findVOConversionFromDB(srcBillOrTranstype, destBillOrTranstype,
				srcBillVO, group);
		
		//得到一个初始化的交换执行器
		BillMappingConvertor bmc = createChangeImpl(srcBillOrTranstype, destBillOrTranstype, paraVo);		
		initBMC(bmc, chgVo);

		// 运行交换（不支持分单）
		AggregatedValueObject[] destBillVOs = bmc.retChangeBusiVOs(srcBillOrTranstype, destBillOrTranstype, new AggregatedValueObject[]{srcBillVO});
		AggregatedValueObject destBillVO = null;
		if(destBillVOs != null && destBillVOs.length >0 )
			destBillVO = destBillVOs[0];

		Logger.debug(">>结束单据VO交换=" + srcBillOrTranstype + "到" + destBillOrTranstype + ",耗时="
				+ (System.currentTimeMillis() - start) + "ms");
		return destBillVO;
	}

	@Override
	public AggregatedValueObject[] runChangeDataAry(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO paraVo)
			throws BusinessException {
		return runChangeDataAryNeedClassify(srcBillOrTranstype, destBillOrTranstype, sourceBillVOs, paraVo, PfButtonClickContext.NoClassify);
	}
	
	@Override
	public AggregatedValueObject[] runChangeDataAryNeedClassify(String srcBillOrTranstype,
			String destBillOrTranstype, AggregatedValueObject[] sourceBillVOs, PfParameterVO paraVo, int classifyMode)
			throws BusinessException {
		Logger.debug(">>开始单据VO批量交换=" + srcBillOrTranstype + "到" + destBillOrTranstype);
		long start = System.currentTimeMillis();
		
		String group = null;
		if(paraVo != null && paraVo.m_pkGroup != null)
			group = paraVo.m_pkGroup;
		else
			group = InvocationInfoProxy.getInstance().getGroupId();
		
		//先对来源单据按不同的目的交易类型分组
		Map<String,AggregatedValueObject[]> vosWithDesttype = new HashMap<String,AggregatedValueObject[]>();
		if(!PfUtilBaseTools.isTranstype(destBillOrTranstype)){
			//目的传的是单据类型，则可能需要分组
			ClassifyContext context = new ClassifyContext();
			context.setClassifyMode(classifyMode);
			context.setDestBilltype(destBillOrTranstype);
			context.setSrcBilltype(srcBillOrTranstype);
			context.setPk_group(group);
			context.setSrcBillVOs(sourceBillVOs);
//			IPfBusiflowService busiflowService = new PfBusiflowServiceImpl();
//			vosWithDesttype = busiflowService.fillRetVOsWithDestTrantype(context);
		}else{
			vosWithDesttype = new HashMap<String, AggregatedValueObject[]>();
			vosWithDesttype.put(destBillOrTranstype, sourceBillVOs);
		}

		//对不同目的交易类型分别进行VO交换
		ArrayList<AggregatedValueObject> destBillVOAry = new ArrayList<AggregatedValueObject>();
		
		for (Iterator iterator = vosWithDesttype.keySet().iterator(); iterator.hasNext();) {
			String desttype = (String) iterator.next();
			ArrayList<AggregatedValueObject> tmpDestBillVOAry = changeDataWithDesttype(
					srcBillOrTranstype, desttype, vosWithDesttype.get(desttype), paraVo);
			destBillVOAry.addAll(tmpDestBillVOAry);
		}
		// 得到目的单据的聚合VO数组
		AggregatedValueObject[] retDestVOs = PfUtilBaseTools.createArrayWithBilltype(destBillOrTranstype, destBillVOAry.size());
		retDestVOs = destBillVOAry.toArray(retDestVOs);

		//最后的合单处理
		retDestVOs = new BillSplitHelper().splitBill(retDestVOs);
		Logger.debug(">>结束单据VO批量交换=" + srcBillOrTranstype + "到" + destBillOrTranstype + ",耗时="
				+ (System.currentTimeMillis() - start) + "ms");
		return retDestVOs;

	}

	private ArrayList<AggregatedValueObject> changeDataWithDesttype(
			String srcBillOrTranstype, String destBillOrTranstype,
			AggregatedValueObject[] sourceBillVOs, PfParameterVO paraVo)
			throws BusinessException {
		//1、先对来源VO数组进行分组，按“业务类型+单据类型+交易类型”分
		HashMap<String, ArrayList<AggregatedValueObject>> srcVOMap = new HashMap<String, ArrayList<AggregatedValueObject>>();
		splitSrcVOsByBusiTransType(srcBillOrTranstype, sourceBillVOs, srcVOMap);
		
		//2、从数据库读取VO交换规则
		HashMap<ExchangeVO, ArrayList<AggregatedValueObject>> ruleVOMap = new HashMap<ExchangeVO, ArrayList<AggregatedValueObject>>();
		String group = null;
		if(paraVo != null && paraVo.m_pkGroup != null)
			group = paraVo.m_pkGroup;
		else
			group = InvocationInfoProxy.getInstance().getGroupId();
		findVOConversionFromDBAry(destBillOrTranstype,srcVOMap, ruleVOMap, group);
		
		//3、对每一个交换规则，分别执行交换
		ArrayList<AggregatedValueObject> destBillVOAry = new ArrayList<AggregatedValueObject>();
		Iterator<ExchangeVO> iter = ruleVOMap.keySet().iterator();
		
		//性能问题： BillMappingConvertor 的构造放在while循环外围 zhouzhenga
		BillMappingConvertor bmc =createChangeImpl(srcBillOrTranstype, destBillOrTranstype, paraVo);
		
		while(iter.hasNext()){
			ExchangeVO chgVO = iter.next();
			ArrayList<AggregatedValueObject> tmpSrcVOs = ruleVOMap.get(chgVO);
			if(tmpSrcVOs == null || tmpSrcVOs.size() == 0)
				continue;
			
			//得到一个初始化的交换执行器
//			BillMappingConvertor bmc = createChangeImpl(srcBillOrTranstype, destBillOrTranstype, chgVO, paraVo);
			initBMC(bmc, chgVO);
			
			// 运行交换
			AggregatedValueObject[] destBillVOs = bmc.retChangeBusiVOs( srcBillOrTranstype,
					destBillOrTranstype,tmpSrcVOs.toArray(new AggregatedValueObject[0]));
			if(destBillVOs != null)
				destBillVOAry.addAll(Arrays.asList(destBillVOs));
		}
		return destBillVOAry;
	}
	
	private String getBusiTransTypeOfBillVO(String srcBillOrTranstype,
			AggregatedValueObject srcVO) {
		// 获取单据主实体中平台相关信息
		PublicHeadVO standHeadVo = new PublicHeadVO();
		//根据元数据模型信息来获取
		PfUtilBaseTools.getHeadInfoByMeta(standHeadVo, srcVO, srcBillOrTranstype);
	
		String busitype = null;
		if (StringUtil.isEmptyWithTrim(standHeadVo.businessType))
			busitype = IPFConfigInfo.STATEBUSINESSTYPE;
		else
			busitype = standHeadVo.businessType;
	
		String strType = busitype + "," + standHeadVo.billType;
		if(standHeadVo.transType!= null && !standHeadVo.billType.equals(standHeadVo.transType))
			strType = strType + "," + standHeadVo.transType;
		return strType;
	}
	
	private void splitSrcVOsByBusiTransType(String srcBillOrTranstype,
			AggregatedValueObject[] sourceBillVOs,
			HashMap<String, ArrayList<AggregatedValueObject>> srcVOMap) {
		for (AggregatedValueObject srcVO : sourceBillVOs) {
			String strType = getBusiTransTypeOfBillVO(srcBillOrTranstype, srcVO);
			if(!srcVOMap.containsKey(strType)){
				ArrayList<AggregatedValueObject> voList = new ArrayList<AggregatedValueObject>();
				srcVOMap.put(strType, voList);
			}
			srcVOMap.get(strType).add(srcVO);
		}
	}
	
//	/**
//	 * 产生交换执行器，并初始化它
//	 * @param srcBillOrTranstype
//	 * @param destBillOrTranstype
//	 * @param busitype
//	 * @return
//	 * @throws BusinessException
//	 */
//	private BillMappingConvertor createChangeImpl(String srcBillOrTranstype,
//			String destBillOrTranstype, ExchangeVO chgVo, PfParameterVO paraVo) throws BusinessException {
//		//XXX:后台的公式解析器
//		BillMappingConvertor bmc = new BillMappingConvertor(formulaParse);
//		//设置交换规则和分单规则
//		initBMCWithRules(srcBillOrTranstype, destBillOrTranstype, bmc, chgVo);
//		//初始化系统变量	
//		initConversionEnv(paraVo, bmc);
//
//		//载入自定义函数到公式解析器中
//		bmc.initFormulaParse();
//		return bmc;
//	}
	
	
	private BillMappingConvertor createChangeImpl(String srcBillOrTranstype,
			String destBillOrTranstype, PfParameterVO paraVo) throws BusinessException {
		
		BillMappingConvertor bmc = new BillMappingConvertor(new nc.bs.pub.formulaparse.FormulaParse());		
		//载入自定义函数到公式解析器中
		initBMCWithRules2(srcBillOrTranstype, destBillOrTranstype,bmc);
		//初始化系统变量	
		initConversionEnv(paraVo, bmc);		
		//
		
		
		bmc.initFormulaParse();
		
		return bmc;
	}
	
	
	/**
	 * while循环内调用
	 * */
	private void initBMC(BillMappingConvertor bmc,ExchangeVO chgVo){
		
		ArrayList<String[]> aRules = new ArrayList<String[]>();
		ArrayList<String[]> mRules = new ArrayList<String[]>();
		ArrayList<String> fRules = new ArrayList<String>();
		for (ExchangeRuleVO ruleVO : chgVo.getRuleVOList()) {
			if (ruleVO.getRuleType() == RuleTypeEnum.ASSIGN.toInt()) {
				aRules.add(new String[] { ruleVO.getDest_attr(), ruleVO.getRuleData() });
			} else if (ruleVO.getRuleType() == RuleTypeEnum.MOVE.toInt()) {
				mRules.add(new String[] { ruleVO.getDest_attr(), ruleVO.getRuleData() });
			} else if (ruleVO.getRuleType() == RuleTypeEnum.FORMULA.toInt()) {
				fRules.add(ruleVO.getDest_attr() + "->" + ruleVO.getRuleData());
			}
		}
		
		bmc.setAssignRules(aRules.toArray(new String[0][0]));
		bmc.setMoveRules(mRules.toArray(new String[0][0]));
		bmc.setFormulaRules(fRules.toArray(new String[0]));
		
		bmc.getSplitVOList().clear(); //已选的分单规则
		bmc.getSplitVOList().addAll(chgVo.getSplitItemVOList());
		
		bmc.setBackClass(chgVo.getBackClass());
		bmc.setFrontClass(chgVo.getFrontClass());
		bmc.setReserveBackClass(chgVo.getReserveBackClass());
		bmc.setReserveFrontClass(chgVo.getReserveFrontClass());
	}
	
	
	private void initBMCWithRules2(String srcBillOrTranstype, String destBillOrTranstype, BillMappingConvertor bmc){
		// 可使用来源、目的单据的单据函数
		ArrayList<FunctionVO> alSrcFuncVOs = PfDataCache.getFunctionsOfBilltype(srcBillOrTranstype);
		ArrayList<FunctionVO> alDestFuncVOs = PfDataCache.getFunctionsOfBilltype(destBillOrTranstype);
		ArrayList<FunctionVO> alFuncVOs =new ArrayList<FunctionVO>();
		alFuncVOs.addAll(alSrcFuncVOs);
		alFuncVOs.addAll(alDestFuncVOs);
		bmc.setUserDefineFunctions(PfUtilBaseTools.changeFunctionVOs(alFuncVOs));		
		bmc.setSourceBilltype(srcBillOrTranstype);
		BilltypeVO billtypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(destBillOrTranstype).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()) );
		if(billtypeVO.getIstransaction() != null && billtypeVO.getIstransaction().booleanValue()) {
			bmc.setDestTranstype(destBillOrTranstype);
			bmc.setDestBilltype(billtypeVO.getParentbilltype());
		}	else
			bmc.setDestBilltype(destBillOrTranstype);
	}

	
	/**
	 * 初始化VO交换用的环境变量
	 * @param tmpVar
	 */
	private void initConversionEnv(PfParameterVO paraVo, BillMappingConvertor bmc ) {
		//当前登陆日期
		bmc.setSysDate(new UFDate().toString());
		// 当前登陆操作员PK
		if(paraVo == null || paraVo.m_operator == null)
			bmc.setSysOperator(InvocationInfoProxy.getInstance().getUserId());
		else
			bmc.setSysOperator(paraVo.m_operator);
		//当前登陆集团
		bmc.setSysGroup(InvocationInfoProxy.getInstance().getGroupId());
		//当前系统时间
		bmc.setSysTime(new UFDateTime(new Date()).toString());
		
		//当前业务日期
		bmc.setBuziDate(new UFDate(InvocationInfoProxy.getInstance().getBizDateTime()));
		
		//当前业务时间
		bmc.setBuziTime(new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));

	}
	
	/**
	 * 获得当前登录的数据源
	 * @return
	 */
	public String getLoginDs() {
		return InvocationInfoProxy.getInstance().getUserDataSource();
	}

	@Override
	public ExchangeVO queryMostSuitableExchangeVO(String srcType,
			String destType, String pkBusitype, String pkgroup,
			ExchangeVO excludeVO) throws BusinessException {
		String wsql = "(pk_busitype ='~' "; 
		if(!StringUtil.isEmptyWithTrim(pkBusitype)) {
			wsql += " or pk_busitype='" + pkBusitype + "'";		
		}
		wsql +=	") and (pk_group ='~' ";
		if(!StringUtil.isEmptyWithTrim(pkgroup)) {
			wsql+=" or pk_group='" + pkgroup + "'";
		}
		wsql+=") ";
		// 查询出能够匹配的VO交换规则集合
		String billTypeWhereSQL = PFExchangeUtil.getBlurMatchExchangeByBillTypeSQL(srcType, destType);
		Collection<ExchangeVO> coRet = NCLocator.getInstance().lookup(IUAPQueryBS.class)
				.retrieveByClause(ExchangeVO.class, wsql + billTypeWhereSQL);

		// 冒泡出其中一个最合适的交换
		ExchangeVO resultVO = PFExchangeUtil.getMostExactExchangeVO(coRet, excludeVO);
		
		ExchangeVO returnvo = loadExchangeVOWithDetail(resultVO);	
		return returnvo;
	}
	
	public ExchangeVO loadExchangeVOWithDetail(ExchangeVO resultVO) throws BusinessException {
		if (resultVO != null) {
			//resultVO中可能已经附带了分单和交换信息，先clear
			resultVO.getRuleVOList().clear();
			resultVO.getSplitVOList().clear();
			resultVO.getSplitItemVOList().clear();
			// 查询出交换规则、分单规则
			//
			String sql ="select "+ExchangeRuleVOListProcessor.getFieldString()+" from pub_vochange_b where pk_vochange ='"+resultVO.getPrimaryKey()+"'";
			List<ExchangeRuleVO> coRules =(List<ExchangeRuleVO>) new BaseDAO().executeQuery(sql, new ExchangeRuleVOListProcessor());
			
//			Collection<ExchangeRuleVO> coRules = NCLocator.getInstance().lookup(IUAPQueryBS.class)
//					.retrieveByClause(ExchangeRuleVO.class, "pk_vochange='" + resultVO.getPrimaryKey() + "'");
			resultVO.getRuleVOList().addAll(coRules);
			Collection<ExchangeSplitVO> coSplitVOs = NCLocator.getInstance().lookup(IUAPQueryBS.class)
					.retrieveByClause(ExchangeSplitVO.class, "pk_vochange='" + resultVO.getPrimaryKey() + "'");
			resultVO.getSplitVOList().addAll(coSplitVOs);
			HashMap<String, Integer> splitTimespaceMap = new HashMap<String, Integer>();
			for (ExchangeSplitVO exchangeSplitVO : coSplitVOs) {
				splitTimespaceMap.put(exchangeSplitVO.getPk_vosplititem(), exchangeSplitVO.getTimespace());
			}
			Collection<SplitItemVO> coSplitItemVos = NCLocator.getInstance().lookup(IUAPQueryBS.class)
					.retrieveByClause(SplitItemVO.class, "pk_vosplititem in (select pk_vosplititem from pub_vochange_s where pk_vochange='" + resultVO.getPrimaryKey() + "')");
			for (SplitItemVO splitItemVO : coSplitItemVos) {
				Integer timeSpace = splitTimespaceMap.get(splitItemVO.getPk_vosplititem());
				splitItemVO.setTimeSpace(timeSpace == null ? 0:timeSpace.intValue());
			}
			resultVO.getSplitItemVOList().addAll(coSplitItemVos);
		}
		return resultVO;
	}

	/**
	 * 比较exchangeVO是否比resultVO更合适(busitype,pk_group,srcTranstype,destTranstype)
	 * 
	 * @param resultVO
	 * @param exchangeVO
	 * @return
	 */
	private boolean isLastSuitable(ExchangeVO resultVO, ExchangeVO exchangeVO) {
		if (!isEqual(resultVO.getSrc_transtype(), exchangeVO.getSrc_transtype())) {
			if (!StringUtil.isEmptyWithTrim(resultVO.getSrc_transtype()))
				return false;
			return true;
		} else if (!isEqual(resultVO.getDest_transtype(), exchangeVO.getDest_transtype())) {
			if (!StringUtil.isEmptyWithTrim(resultVO.getDest_transtype()))
				return false;
			return true;
		} else if (StringUtil.isEmptyWithTrim(resultVO.getPk_busitype())) {// 根据busitype或者
			// pkgroup判断
			if (!StringUtil.isEmptyWithTrim(resultVO.getPk_group())
					&& StringUtil.isEmptyWithTrim(exchangeVO.getPk_group()))
				return false;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断两个字符串是否相等(NULL 和 "" 认为相等)
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	private static boolean isEqual(String str1, String str2) {
		if (!StringUtil.isEmptyWithTrim(str1)) {
			if (StringUtil.isEmptyWithTrim(str2))
				return false;
			return str1.trim().equals(str2.trim());
		} else {
			if (StringUtil.isEmptyWithTrim(str2))
				return true;
			return false;
		}
	}
	
	/**
	 * 从数据库读取VO交换规则
	 * 
	 * @param srcBillOrTranstype
	 * @param destBillOrTranstype
	 * @param sourceBillVO
	 * @param pk_group
	 * @param bmc
	 * @throws BusinessException
	 */
	public ExchangeVO findVOConversionFromDB(String srcBillOrTranstype, String destBillOrTranstype,
			AggregatedValueObject sourceBillVO, String pk_group) throws BusinessException {

		String busi_trans_type = getBusiTransTypeOfBillVO(srcBillOrTranstype, sourceBillVO);
		String[] typeAry = busi_trans_type.split(",");
		String busitype = typeAry[0];
		
		// 从数据库查询交换规则
		ExchangeVO chgVo = queryMostSuitableExchangeVO(srcBillOrTranstype, destBillOrTranstype,
				busitype, pk_group, null);
		if (chgVo == null)
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow", "PfExchangeServiceImpl-0000", null, new String[]{srcBillOrTranstype,destBillOrTranstype})/*找不到{0}到{1}的单据交换规则*/);

		return chgVo;
	}
	
	private void findVOConversionFromDBAry(String destBillOrTranstype,
			HashMap<String, ArrayList<AggregatedValueObject>> srcVOMap,
			HashMap<ExchangeVO, ArrayList<AggregatedValueObject>> ruleVOMap, String group) throws BusinessException {
		if(srcVOMap == null)
			return;
		String types = null;
		String[] aryType = null;
		String busitype = null;
		String srcBillOrTranstype = null;
		if(ruleVOMap == null)
			ruleVOMap = new HashMap<ExchangeVO, ArrayList<AggregatedValueObject>>();
		/**
		 * 避免重复从数据库中查询相同的VO交换
		 * */
		HashMap<String,ExchangeVO> VOchangeCacheMap =new HashMap<String,ExchangeVO>();
			
		for (Iterator<String> iter = srcVOMap.keySet().iterator(); iter.hasNext();) {
			types = iter.next();
			aryType = types.split(",");
			busitype = aryType[0];
			srcBillOrTranstype = aryType.length > 2 ? aryType[2] : aryType[1];
			String key =fetchVOchangeMapKey(srcBillOrTranstype, destBillOrTranstype, busitype, group, null);
			ExchangeVO chgVo =null;
			if(VOchangeCacheMap.containsKey(key)){
				chgVo =VOchangeCacheMap.get(key);
			}else{
				// 从数据库查询交换规则
				chgVo= queryMostSuitableExchangeVO(srcBillOrTranstype, destBillOrTranstype, busitype, group, null);
				if (chgVo == null)
					throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("busitype", "busitypehint-000013", null, new String[]{srcBillOrTranstype,destBillOrTranstype})/*找不到{0}到{1}的单据交换规则*/);
				VOchangeCacheMap.put(key, chgVo);
			}
			// 将单据加入到Map里
			if(ruleVOMap.containsKey(chgVo)) {
				ruleVOMap.get(chgVo).addAll(srcVOMap.get(types));
			} else {
				ruleVOMap.put(chgVo, (ArrayList<AggregatedValueObject>)srcVOMap.get(types).clone());
			}
		}
	}

	/**
	 * @return String key
	 * */
	private String fetchVOchangeMapKey(String srcType, String destType, String pkBusitype, String pkgroup, ExchangeVO excludeVO){
		String key = (StringUtil.isEmptyWithTrim(srcType)?"":srcType)
			+(StringUtil.isEmptyWithTrim(destType)?"":destType)
			+(StringUtil.isEmptyWithTrim(pkBusitype)?"":pkBusitype)
			+(StringUtil.isEmptyWithTrim(pkgroup)?"":pkgroup)
			+(excludeVO==null?"":String.valueOf(excludeVO.getPk_vochange()));
		return key;
	}
	
	

}
