package uap.workflow.pub.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.md.common.AssociationKind;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.ICardinality;
import nc.md.model.access.javamap.AggVOStyle;
import nc.md.model.access.javamap.IBeanStyle;
import nc.md.model.type.IType;
import nc.md.persist.designer.vo.ClassVO;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pf.pub.BasedocTempVO;
import nc.vo.pf.pub.BasedocVO;
import nc.vo.pf.pub.FunctionVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.change.PublicHeadVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pfflow.BillActionTypeEnum;
import nc.vo.pub.pfflow.BillactionVO;
import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.exception.PFRuntimeException;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.vo.IPFConfigInfo;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 平台的基础类
 * 
 * @author 樊冠军 2002-4-16
 * @modifier leijun 2006-8-4 增加一些环境常量 .
 */
public class PfUtilBaseTools {
	/**
	 * 动作处理的扩展参数
	 */
	public static final String PARAM_FLOWPK = "flowdefpk"; // 流程定义PK

	public static final String PARAM_NO_LOCK = "nolockandconsist"; // 无需加锁和一致性校验

	public static final String PARAM_NOFLOW = "nosendmessage"; // 不交互，不启动流程、不触发流程

	public static final String PARAM_SILENTLY = "silently"; // 不交互，但启动流程、触发流程，按默认交互规则处理

	public static final String PARAM_NOTE_CHECKED = "notechecked"; // 工作项是否已检查

	public static final String PARAM_RELOAD_VO = "reload_vo"; // 重新加载VO

	public static final String PARAM_BATCH = "batch"; // 是否批处理
	
	//工作项，为供应链增加，批处理时他们把一批单据分多次传入，但是又不想弹多次审批处理框
	//平台根据这个参数来判断，如果参数中传了workflownotevo，则不再弹框，直接从这个对象中获取用户审批处理意见
	public static final String PARAM_WORKNOTE = "worknote"; 
	
	//不管有没有流程定义，总是需要弹框，为预算开发部（asked by qiaoye）增加
	public static final String PARAM_NOTSILENT = "notsilent";
	
	//工作流在某一环节是否需要执行动作驱动，资产管理开发部(zhangzhij) 
	public static final String PARAM_DRIVEACTION="driveaction";
	
	//记录单据当前的审核时间，便于在弃审时候恢复
	public static final String PARAM_PREAPPROVEDATE="preapprovedate";

	/**
	 * 从单据函数数组中获取可用于VO交换的自定义函数
	 * <li>functionnote必须以"<"开头
	 * <li>函数的参数中不能包含VO参数
	 * 
	 * @param alFuncs
	 * @return
	 */
	public static UserDefineFunction[] changeFunctionVOs(ArrayList<FunctionVO> alFuncs) {
		Vector<UserDefineFunction> vec = new Vector<UserDefineFunction>();
		try {
			for (FunctionVO fVO : alFuncs) {
				if (!fVO.getFunctionnote().startsWith("<"))
					continue;

				if ((fVO.getArguments() != null) && (fVO.getArguments().indexOf(".") > 0)) // 表示不是函数
					continue;

				UserDefineFunction temp = new UserDefineFunction();
				temp.setClassName(fVO.getClassname());
				temp.setMethodName(fVO.getMethodname());
				temp.setFunctionNote(fVO.getFunctionnote());
				temp.setReturnType(parseTypeClass(fVO.getReturntype()));

				String[] par = getParameters(fVO);
				if (par != null) {
					Class[] classes = new Class[par.length];
					String[] argNames = new String[par.length];
					for (int j = 0; j < par.length; j++) {
						classes[j] = parseTypeClass(par[j].substring(par[j].indexOf(":") + 1));
						argNames[j] = par[j].substring(0, par[j].indexOf(":"));
					}
					temp.setArgNames(argNames);
					temp.setArgTypes(classes);
				}

				vec.addElement(temp);
			}

		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
		}

		UserDefineFunction[] functions = new UserDefineFunction[vec.size()];
		vec.copyInto(functions);
		return functions;
	}

	public static AggregatedValueObject[] createArrayWithBilltype(String destBillOrTranstype, int size) throws BusinessException {
		// 得到目的单据的聚合VO类名称
		String destBillVoClzName = null;
		IBusinessEntity destBE = PfMetadataTools.queryMetaOfBilltype(destBillOrTranstype);
		IBeanStyle bs = destBE.getBeanStyle();
		if (bs instanceof AggVOStyle) {
			destBillVoClzName = ((AggVOStyle) bs).getAggVOClassName();
		} else
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000009")/*单据实体必须符合聚合VO的样式*/);

		AggregatedValueObject[] retDestVOs;
		try {
			retDestVOs = (AggregatedValueObject[]) Array.newInstance(Class.forName(destBillVoClzName), size);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000034", null, new String[]{e.getMessage()})/*初始化目的单据VO数组出现异常：{0}*/);
		}
		return retDestVOs;
	}

	/**
	 * 从单据VO或动作处理返回值中获取单据ID和No信息
	 * 
	 * @param singleBillEntity
	 *            单个单据实体对象
	 * @param retObj
	 *            动作处理返回值
	 */
	public static void fetchBillId(PfParameterVO paravo, Object singleBillEntity, Object retObj) {
		// 从动作脚本执行后的返回值中获得单据Id
		paravo.m_billVersionPK = getBillID(retObj);
		/***********************************************************************
		 * 如果单据是在SAVE动作脚本中推式保存,则需要再次获取billNo lj+ 2005-3-10
		 **********************************************************************/
		// 获取单据主实体中平台相关信息
		PublicHeadVO standHeadVo = new PublicHeadVO();
		// 根据元数据模型信息来获取
		getHeadInfoByMeta(standHeadVo, singleBillEntity, paravo.m_billType);

		paravo.m_billNo = standHeadVo.billNo;
		if (paravo.m_billVersionPK == null)
			// 如果从返回值中获取的billId为空,则取单据VO中的单据PK
			paravo.m_billVersionPK = standHeadVo.pkBillVersion;
	}
	
	public static Object[] composeResultAry(Object tmpRet, int length, int i, Object[] ret) {
		if(ret == null) {
			if(tmpRet != null){
				if (tmpRet.getClass().getName().startsWith("[") && ((Object[]) tmpRet).length > 0) {
					ret = (Object[]) Array.newInstance(((Object[]) tmpRet)[0].getClass(), length);
				} else {
					ret = (Object[]) Array.newInstance(tmpRet.getClass(), length);
				}
			}
		}
		if(ret != null) {
			if(tmpRet != null)
				ret[i] = tmpRet.getClass().getName().startsWith("[") ? ((Object[]) tmpRet)[0] : tmpRet;
			else
				ret[i] = null;	
		}
		return ret;
	}

	/**
	 * 根据PK从表中获取某字段值
	 * 
	 * @param tableName
	 *            表名
	 * @param pkFieldName
	 *            主键字段
	 * @param columnName
	 *            查询字段
	 * @param pks
	 *            主键值 数组
	 * @return 查询到的值数组
	 */
	public static HashMap<String, String> fetchValuesByPKs(String tableName, String pkFieldName, String columnName, HashSet<String> hsPK) {
		String sql = "select " + pkFieldName + "," + columnName + " from " + tableName;
		StringBuffer where = new StringBuffer();
		where.append(" where ");
		where.append(pkFieldName);
		where.append(" in(");

		for (String strPK : hsPK) {
			where.append("'");
			where.append(strPK);
			where.append("',");
		}

		sql += where.substring(0, where.length() - 1) + ") order by " + pkFieldName;

		List<Object[]> lResult = new ArrayList(0);
		HashMap<String, String> hmRet = new HashMap<String, String>();
		try {
			IUAPQueryBS uapqry = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			lResult = (List) uapqry.executeQuery(sql, new ArrayListProcessor());
			for (Object[] objs : lResult) {
				hmRet.put(objs[0].toString(), objs[1] == null ? null : objs[1].toString());
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}

		return hmRet;
	}

	/**
	 * 查询某单据类型的父单据类型
	 * <li>根据单据大类bd_billtype.billstyle
	 * 
	 * @param btVO
	 *            单据类型VO对象
	 * @return
	 */
	private static String findParentBilltypeByStyle(BilltypeVO btVO) {
		if (btVO.getBillstyle() == null)
			return null;
		String parentBilltype = PfDataCache.getBillTypeByStyle(btVO.getBillstyle().toString());
		if (parentBilltype == null || parentBilltype.equals(btVO.getPk_billtypecode()))
			// 单据大类不存在 或本身就是父单据类型
			return null;
		return parentBilltype;
	}

	/**
	 * 查询单据类型对应的实体的数据库表主键字段
	 * 
	 * @param billType
	 *            单据类型PK
	 * @return
	 * @throws BusinessException
	 */
	public static String findPkField(String billType) throws BusinessException {
		// 从元数据模型获取
		IBean bean = PfMetadataTools.queryMetaOfBilltype(billType);
		return bean.getTable().getPrimaryKeyName();
	}

	/**
	 * 获得所有基础档案VO。
	 */
	public static BasedocVO[] getAllBasedocVO() {
		BasedocTempVO[] vos;
		ClassVO[] classVOs = null;
		try {
			classVOs = queryAllRefInfoVOs();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

		// Convert the classVO the BasedocTempVO
		if (classVOs == null)
			return null;
		vos = new BasedocTempVO[classVOs.length];
		for (int i = 0; i < vos.length; i++) {
			vos[i] = new BasedocTempVO();
			vos[i].setDocName(classVOs[i].getDisplayName());
			vos[i].setDocPK(classVOs[i].getPrimaryKey());
			String refModelName = classVOs[i].getRefModelName();
			if (refModelName != null && refModelName.contains(";")) {
				// 取第一个（默认）参照名称
				vos[i].setRefNodeName(refModelName.split(";")[0]);
			} else {
				vos[i].setRefNodeName(refModelName);
			}
		}
		return vos;
	}

	private static ClassVO[] queryAllRefInfoVOs() throws BusinessException {
		return (ClassVO[]) NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(ClassVO.class, "help like '%DOC%' and classtype=" + IType.ENTITY + " and refmodelname is not null")
				.toArray(new ClassVO[0]);
	}

	/**
	 * 从动作脚本执行后的返回值中获得单据Id
	 * <li>返回值必须为ArrayList或String类型
	 * 
	 * @param tmpObj
	 */
	private static String getBillID(Object tmpObj) {
		Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000037")/*****从动作脚本执行后的返回值中获得单据Id开始*****/);
		String billId = null;
		if (tmpObj == null) {
			return billId;
		}

		if (tmpObj instanceof ArrayList) {
			ArrayList alRetObj = (ArrayList) tmpObj;
			Object[] retObjAry = alRetObj.toArray();
			if (retObjAry.length > 1 && retObjAry[1] instanceof ArrayList) {
				billId = ((ArrayList) retObjAry[1]).toArray()[0].toString();
				Logger.debug("获得单据Id:Array的第一维的数组的第0维为单据ID");
			} else {
				if (retObjAry[0] != null && !retObjAry[0].equals("")) {
					billId = retObjAry[0].toString();
					Logger.debug("获得单据Id:Array的第0维为单据ID");
				}
			}
		} else if (tmpObj instanceof String) {
			billId = (String) tmpObj;
			Logger.debug("直接从对象String取的ID");
		}
		Logger.debug("****从动作脚本执行后的返回值中获得单据Id=" + billId + "结束****");
		return billId;
	}

	/**
	 * 从单据实体中获取平台相关信息
	 * <li>根据单据实体的元数据模型
	 * 
	 * @param headvo
	 * @param singleBillEntity
	 *            单据实体对象，非数组
	 * @param billType
	 * @return 副产品是返回单据当前的审核时间
	 * @since 5.5
	 */
	public static int getHeadInfoByMeta(PublicHeadVO headvo, Object singleBillEntity, String billType) {
		headvo.billType = billType;

		IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(singleBillEntity, IFlowBizItf.class);
		if (fbi == null)
			throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000038")/*元数据实体没有提供业务接口IFlowBizItf的实现类*/);

		headvo.approveId = fbi.getApprover();
		headvo.billNo = fbi.getBillNo();
		headvo.businessType = fbi.getBusitype();
		headvo.pkOrg = fbi.getPkorg();
		headvo.operatorId = fbi.getBillMaker();
		headvo.pkBillId = fbi.getBillId();
		headvo.transType = fbi.getTranstype();
		headvo.pkBillVersion =fbi.getBillVersionPK();
		return fbi.getEmendEnum();
	}

	public static String[] getParameters(FunctionVO fVO) {
		if ((fVO == null) || (fVO.getArguments() == null))
			return null;

		String str = fVO.getArguments();
		if ((str.length() == 0) || (str.indexOf(".") > 0))
			return null;

		Vector<String> vec = new Vector<String>();
		while (str.indexOf(",") >= 0) {
			vec.addElement(str.substring(0, str.indexOf(",")));
			str = str.substring(str.indexOf(",") + 1);
		}
		vec.addElement(str);

		String[] res = new String[vec.size()];
		vec.copyInto(res);
		return res;
	}

	/**
	 * 根据交易类型或单据类型的编码返回真实的单据类型编码
	 * 
	 * @param strTypeCode
	 *            单据类型或交易类型PK
	 * @return
	 */
	public static String getRealBilltype(String strTypeCodestr) {
		// String strEditBilltype =
		BillTypeCacheKey strTypeCode = new BillTypeCacheKey().buildBilltype(strTypeCodestr).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId());
		String billtype = strTypeCode.getBilltype().trim();
		strTypeCode.buildBilltype(billtype);

		// XXX::根据缓存获取单据类型
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(strTypeCode);
		if (btVO == null)
			throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "PfUtilBaseTools-000000", null, new String[]{billtype})/*错误：流程平台缓存中不存在该单据或交易类型={0}*/);
		else if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			// 如果选中的是交易类型，则把其单据类型返回
			Logger.debug("获得单据类型或交易类型=" + strTypeCode + "的真实单据类型=" + billtype);
			billtype =  btVO.getParentbilltype();
		} 
		return billtype;

	}
	
	/**
	 * 根据交易类型或单据类型的PK返回真实的单据类型编码
	 * 
	 * @param strPK 单据类型或交易类型PK
	 * @return 真实的单据类型编码
	 */
	public static String getRealBilltypeByPK(String strPK){
		Collection<BilltypeVO> collbilltypes =PfDataCache.getBilltypes().values();
		for(BilltypeVO vo:collbilltypes){
			if(vo != null && vo.getPrimaryKey().equals(strPK)){
				if(vo.getIstransaction() != null && vo.getIstransaction().booleanValue()){
					return vo.getParentbilltype();
				}else{
					return vo.getPk_billtypecode();
				}
			}
		}
		throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "PfUtilBaseTools-000000", null, new String[]{strPK})/*错误：流程平台缓存中不存在该单据或交易类型={0}*/);
	}

	/**
	 * 根据单据类型查询单据主子表的类名称
	 * 
	 * @param pkBillType
	 *            单据类型PK
	 * @return 数组[0]=单据聚合Vo;数组[1]=单据主表Vo;数组[2]=单据子表Vo
	 * @throws BusinessException
	 */
	public static String[] getStrBillVo(String pkBillType) throws BusinessException {
		String[] retStrs = new String[3];
		// 使用元数据模型
		IBusinessEntity be = PfMetadataTools.queryMetaOfBilltype(pkBillType);
		IBeanStyle bs = be.getBeanStyle();
		if (bs instanceof AggVOStyle) {
			retStrs[0] = ((AggVOStyle) bs).getAggVOClassName();
		} else
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000009")/*单据实体必须符合聚合VO的样式*/);
		retStrs[1] = be.getFullClassName();
		// 取得主实体所有1:n组合的实体
		List<IBean> lRelatedBeans = be.getRelatedEntities(AssociationKind.Composite, ICardinality.ASS_ONE2MANY);

		// FIXME:取第一个组合的实体？
		IBean relatedBean = lRelatedBeans.size() > 0 ? lRelatedBeans.iterator().next() : null;
		retStrs[2] = relatedBean == null ? null : relatedBean.getFullClassName();

		return retStrs;
	}

	/**
	 * 获取类变量值
	 * @throws BusinessException 
	 */
	public static PfParameterVO getVariableValue(String billType, String actionName, AggregatedValueObject billvo, AggregatedValueObject[] billvos, Object userObj, Object[] userObjs,
			WorkflownoteVO worknoteVO, HashMap hmParam, Hashtable hashBilltypeToParavo) throws BusinessException {
		// 平台日志
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") START<<<");

		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_billType = billType;

		// 获取主表数据
		PublicHeadVO standHeadVo = new PublicHeadVO();
		AggregatedValueObject singleVO = null;
		if (billvo != null) {
			singleVO = billvo;
		} else if (billvos != null && billvos.length > 0) {
			singleVO = billvos[0];
		}

		// 根据元数据模型来获取
		int isEmend=  getHeadInfoByMeta(standHeadVo, singleVO, billType);

		// fgj为了实现无业务类型的工作流
		if (StringUtil.isEmptyWithTrim(standHeadVo.businessType))
			paraVo.m_businessType = IPFConfigInfo.STATEBUSINESSTYPE;
		else
			paraVo.m_businessType = standHeadVo.businessType;

		paraVo.m_billNo = standHeadVo.billNo;
		paraVo.m_billVersionPK = standHeadVo.pkBillVersion;
		paraVo.m_billId = standHeadVo.pkBillId;
		paraVo.m_pkOrg = standHeadVo.pkOrg;
		// paraVo.m_currentDate = currentDate;
		paraVo.m_makeBillOperator = standHeadVo.operatorId;
		paraVo.m_preValueVo = singleVO;
		paraVo.m_preValueVos = billvos;
		paraVo.m_standHeadVo = standHeadVo;
		paraVo.m_userObj = userObj;
		paraVo.m_userObjs = userObjs;
		paraVo.m_workFlow = worknoteVO;
		paraVo.m_pkGroup = InvocationInfoProxy.getInstance().getGroupId();
		paraVo.emendEnum =isEmend;
		if(hmParam==null){
			hmParam =new HashMap();
		}

		if (actionName != null && actionName.length() > 20) {
			paraVo.m_actionName = actionName.substring(0, actionName.length() - 20);
			paraVo.m_operator = actionName.substring(actionName.length() - 20);
		} else {
			paraVo.m_actionName = actionName;
			paraVo.m_operator = InvocationInfoProxy.getInstance().getUserId();
//			paraVo.m_operator = standHeadVo.approveId;
		}
//		// 如果操作人为空则赋予操作人为制单人
//		if (StringUtil.isEmptyWithTrim(paraVo.m_operator))
//			paraVo.m_operator = paraVo.m_makeBillOperator;

		// XXX:如果有交易类型，则paraVo的billtype取交易类型
		if (!StringUtil.isEmptyWithTrim(standHeadVo.transType))
			paraVo.m_billType = standHeadVo.transType;

		paraVo.putCustomPropertyBatch(hmParam);

		// 平台日志
		Logger.debug(">>>billType=" + paraVo.m_billType + " busiType=" + paraVo.m_businessType);
		Logger.debug(">>>billMaker=" + paraVo.m_makeBillOperator + " operator=" + paraVo.m_operator);
		Logger.debug(">>>corpPK=" + paraVo.m_pkOrg + " billId=" + paraVo.m_billVersionPK);
		Logger.debug(">>>actionName=" + paraVo.m_actionName + " billNo=" + paraVo.m_billNo);
		// 设置单据类型-工作流参数VO
		if(hashBilltypeToParavo != null){
			String src_billtypePK =StringUtil.isEmptyWithTrim(paraVo.m_preValueVo.getParentVO().getPrimaryKey())?"":paraVo.m_preValueVo.getParentVO().getPrimaryKey();
			hashBilltypeToParavo.put(paraVo.m_billType+src_billtypePK, paraVo);
		}
			

		// 平台日志
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") END<<<");

		return paraVo;
	}
	
	

	/**
	 * 获取类变量值
	 */
	public static PfParameterVO getVariableValue(String billType, String actionName, AggregatedValueObject billvo, AggregatedValueObject[] billvos, Object userObj, Object[] userObjs,
			WorkflownoteVO worknoteVO, HashMap hmParam, Hashtable hashBilltypeToParavo, String src_billtyePK) {
		// 平台日志
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") START<<<");

		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_billType = billType;

		// 获取主表数据
		PublicHeadVO standHeadVo = new PublicHeadVO();
		AggregatedValueObject singleVO = null;
		if (billvo != null) {
			singleVO = billvo;
		} else if (billvos != null && billvos.length > 0) {
			singleVO = billvos[0];
		}

		// 根据元数据模型来获取
		int isEmend =getHeadInfoByMeta(standHeadVo, singleVO, billType);

		// fgj为了实现无业务类型的工作流
		if (StringUtil.isEmptyWithTrim(standHeadVo.businessType))
			paraVo.m_businessType = IPFConfigInfo.STATEBUSINESSTYPE;
		else
			paraVo.m_businessType = standHeadVo.businessType;

		paraVo.m_transType = standHeadVo.transType;
		paraVo.m_billNo = standHeadVo.billNo;
		paraVo.m_billVersionPK = standHeadVo.pkBillVersion;
		paraVo.m_billId = standHeadVo.pkBillId;
		paraVo.m_pkOrg = standHeadVo.pkOrg;
		paraVo.m_makeBillOperator = standHeadVo.operatorId;
		paraVo.m_preValueVo = singleVO;
		paraVo.m_preValueVos = billvos;
		paraVo.m_standHeadVo = standHeadVo;
		paraVo.m_userObj = userObj;
		paraVo.m_userObjs = userObjs;
		paraVo.m_workFlow = worknoteVO;
		paraVo.m_pkGroup = InvocationInfoProxy.getInstance().getGroupId();
		paraVo.emendEnum =isEmend;
		if(hmParam==null){
			hmParam =new HashMap();
		}
		
		if (actionName != null && actionName.length() > 20) {
			paraVo.m_actionName = actionName.substring(0, actionName.length() - 20);
			paraVo.m_operator = actionName.substring(actionName.length() - 20);
		} else {
			paraVo.m_actionName = actionName;
			paraVo.m_operator = InvocationInfoProxy.getInstance().getUserId();
//			paraVo.m_operator = standHeadVo.approveId;
		}
//		// 如果操作人为空则赋予操作人为制单人
//		if (StringUtil.isEmptyWithTrim(paraVo.m_operator))
//			paraVo.m_operator = paraVo.m_makeBillOperator;

		// XXX:如果有交易类型，则paraVo的billtype取交易类型
		if (!StringUtil.isEmptyWithTrim(standHeadVo.transType))
			paraVo.m_billType = standHeadVo.transType;

		paraVo.putCustomPropertyBatch(hmParam);

		// 平台日志
		Logger.debug(">>>billType=" + paraVo.m_billType + " busiType=" + paraVo.m_businessType);
		Logger.debug(">>>billMaker=" + paraVo.m_makeBillOperator + " operator=" + paraVo.m_operator);
		Logger.debug(">>>corpPK=" + paraVo.m_pkOrg + " billId=" + paraVo.m_billVersionPK);
		Logger.debug(">>>actionName=" + paraVo.m_actionName + " billNo=" + paraVo.m_billNo);

		// 设置单据类型-工作流参数VO
		if(hashBilltypeToParavo != null)
			hashBilltypeToParavo.put(paraVo.m_billType+src_billtyePK, paraVo);

		// 平台日志
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") END<<<");

		return paraVo;
	}
	
	

	/**
	 * 判断某单据动作编码是否为"审批"动作
	 * <li>即以"APPROVE"开头
	 * 
	 * @param actionName
	 * @return
	 */
	public static boolean isApproveAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()==null ) {
			int leng = IPFActionName.APPROVE.length();
			return actionName.length() >= leng && actionName.toUpperCase().substring(0, leng).equals(IPFActionName.APPROVE);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.SignalApproveflow.getIntValue();
		}
	}

	private static BillactionVO getBillActionVO(String actionName, String billtype) {
		Map<String, BillactionVO> actionMap = PfDataCache.getBillactionVOMap(billtype);
		return actionMap==null ? null: actionMap.get(actionName);
	}

	/**
	 * 判断某单据动作编码是否为删除动作
	 * 
	 * @param actionName
	 *            动作编码
	 * @return
	 */
	public static boolean isDeleteAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()== null ) {
			return actionName.endsWith(IPFActionName.DEL_DELETE) || actionName.endsWith(IPFActionName.DEL_DISCARD) || actionName.endsWith(IPFActionName.DEL_SOBLANKOUT);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.Delete.getIntValue();
		}
	}

	/**
	 * 判断某单据动作编码是否为"回退"动作
	 * <li>是否以ROLLBACK开头
	 * 
	 * @param actionName
	 *            动作编码，见<code>IPFActionName</code>
	 * @return
	 */
	public static boolean isRollbackAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()== null ) {
			int leng = IPFActionName.ROLLBACK.length();
			return actionName.length() >= leng && actionName.toUpperCase().substring(0, leng).equals(IPFActionName.ROLLBACK);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.RollbackWorkflow.getIntValue();
		}
		
	}

	/**
	 * 判断某单据动作编码是否为"提交"或"编辑"动作
	 * <li>即以"SAVE"或"EDIT"开头
	 * 
	 * @param actionName
	 *            动作编码
	 * @return
	 */
	public static boolean isSaveAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()==null) {
			int leng = IPFActionName.SAVE.length();
			return actionName.length() >= leng && (actionName.toUpperCase().substring(0, leng).equals(IPFActionName.SAVE) || actionName.toUpperCase().substring(0, leng).equals(IPFActionName.EDIT));
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.StartApproveflow.getIntValue();
		}
		
	}

	/**
	 * 判断某单据动作编码是否为"执行工作流"动作
	 * <li>是否以SIGNAL开头
	 * 
	 * @param actionName
	 *            动作编码
	 * @return
	 */
	public static boolean isSignalAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()== null) {
			int leng = IPFActionName.SIGNAL.length();
			return actionName.length() >= leng && actionName.toUpperCase().substring(0, leng).equals(IPFActionName.SIGNAL);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.SignalWorkflow.getIntValue();
		}
	}

	/**
	 * 判断某单据动作编码是否为"启动工作流"动作
	 * <li>是否以START结尾
	 * 
	 * @param actionName
	 *            动作编码
	 * @return
	 */
	public static boolean isStartAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()== null) {
			int leng = IPFActionName.START.length();
			return actionName.length() >= leng && actionName.toUpperCase().substring(0, leng).equals(IPFActionName.START);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.StartWorkflow.getIntValue();
		}
		
	}

	/**
	 * 根据编码（即PK）判定是否为交易类型
	 * 
	 * @param strTypeCode
	 *            单据类型(或交易类型)PK
	 * @return
	 */
	public static boolean isTranstype(String strTypeCode) {
		// XXX::根据缓存获取单据类型(或交易类型)VO
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(strTypeCode).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		if (btVO!= null && btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue())
			return true;
		return false;
	}

	/**
	 * 判断某单据动作编码是否为"弃审"动作
	 * <li>是否以UNAPPROVE开头
	 * 
	 * @param actionName
	 *            动作编码，见<code>IPFActionName</code>
	 * @return
	 */
	public static boolean isUnapproveAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()==null) {
			int leng = IPFActionName.UNAPPROVE.length();
			return actionName.length() >= leng && actionName.toUpperCase().substring(0, leng).equals(IPFActionName.UNAPPROVE);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.RollbackApproveflow.getIntValue();
		}
	}

	/**
	 * 判断新旧值是否改变了
	 * 
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public static boolean isValueChanged(Object oldValue, Object newValue) {
		boolean isChange = false;
		if (oldValue != null && !oldValue.equals(newValue))
			isChange = true;
		if (newValue != null && !newValue.equals(oldValue))
			isChange = true;

		return isChange;
	}

	/**
	 * 返回数据类型串 对应的类
	 * 
	 * @param dataType
	 *            数据类型
	 * @return
	 */
	public static Class parseTypeClass(String dataType) {
		if (dataType.toUpperCase().equals("STRING")) {
			return String.class;
		} else if (dataType.toUpperCase().equals("UFDATE")) {
			return UFDate.class;
		} else if (dataType.toUpperCase().equals("UFDOUBLE")) {
			return UFDouble.class;
		} else if (dataType.toUpperCase().equals("DOUBLE")) {
			return UFDouble.class;
		} else if (dataType.toUpperCase().equals("UFBOOLEAN")) {
			return UFBoolean.class;
		} else if (dataType.toUpperCase().equals("BOOLEAN")) {
			return UFBoolean.class;
		} else if (dataType.toUpperCase().equals("UFDATETIME")) {
			return UFDateTime.class;
		} else if (dataType.toUpperCase().equals("INTEGER")) {
			return Integer.class;
		} else if (dataType.toUpperCase().equals("ARRAYLIST")) {
			return java.util.ArrayList.class;
		} else if (dataType.toUpperCase().equals("OBJECT")) {
			return java.lang.Object.class;
		} else if (dataType.toUpperCase().equals("STRING[]")) {
			return String[].class;
		} else if (dataType.toUpperCase().equals("UFDATE[]")) {
			return UFDate[].class;
		} else if (dataType.toUpperCase().equals("UFDOUBLE[]")) {
			return UFDouble[].class;
		} else if (dataType.toUpperCase().equals("UFBOOLEAN[]")) {
			return UFBoolean[].class;
		} else if (dataType.toUpperCase().equals("UFDATETIME[]")) {
			return UFDateTime[].class;
		} else if (dataType.toUpperCase().equals("INTEGER[]")) {
			return Integer[].class;
		} else if (dataType.toUpperCase().equals("OBJECT[]"))
			return java.lang.Object[].class;
		return null;
	}

	/**
	 * 初始化单据聚合VO数组
	 * 
	 * @param strBillVoClassName
	 *            单据聚合VO类名称
	 * @param voLen
	 *            实例个数
	 * @return
	 * @throws BusinessException
	 */
	public static AggregatedValueObject[] pfInitVos(String strBillVoClassName, int voLen) throws BusinessException {
		AggregatedValueObject[] retVos;
		try {
			retVos = (AggregatedValueObject[]) java.lang.reflect.Array.newInstance(Class.forName(strBillVoClassName), voLen);
		} catch (Exception e) {
			throw new PFBusinessException(e.getMessage(), e);
		}
		return retVos;
	}

	/**
	 * 查询所有已经审批完成的审批人
	 * 
	 * @param billId
	 * @param billType
	 * @return 以','分隔的审批人用户名串
	 */
	public static String queryAllCheckers(String billId, String billType) {
		String ret = null;
		StringBuffer strAllCheckers = new StringBuffer();
		try {
			WorkflownoteVO[] noteVOs = null;
			///todo:
			//noteVOs = NCLocator.getInstance().lookup(IPFWorkflowQry.class).queryWorkitems(billId, billType, WorkflowTypeEnum.Approveflow.getIntValue(), 1);
			for (int i = 0; i < (noteVOs == null ? 0 : noteVOs.length); i++) {
				strAllCheckers.append(noteVOs[i].getCheckname());
				strAllCheckers.append(",");
			}
			if (strAllCheckers.length() > 0)
				ret = strAllCheckers.substring(0, strAllCheckers.length() - 1);

			Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000039", null, new String[]{billId,strAllCheckers.toString()})/*>>获得单据ID{0}的所有审批人={1}*/);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return ret;
	}

	/**
	 * 查询单据类型（或交易类型）的同源单据类型
	 * <li>以倒序方式返回
	 * 
	 * @param billType
	 *            单据类型（或交易类型）
	 * @return
	 * @since 5.5
	 */
	public static HashSet<String> querySimilarTypes(String billType) {
		HashSet<String> hsRet = new HashSet<String>();
		hsRet.add(billType);
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(billType).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			// 为交易类型，则找到其所属的单据类型
			BilltypeVO parentTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype( btVO.getParentbilltype()).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
			hsRet.add(parentTypeVO.getPk_billtypecode());
			// 再找所属单据类型的父单据类型
			String parentBilltype = findParentBilltypeByStyle(parentTypeVO);
			if (parentBilltype != null) {
				// 存在父单据类型
				hsRet.add(parentBilltype);
			}
		} else {
			// 为单据类型
			String parentBilltype = findParentBilltypeByStyle(btVO);
			if (parentBilltype != null) {
				// 存在父单据类型
				hsRet.add(parentBilltype);
			}
		}
		return hsRet;
	}

	/**
	 * 判断某单据动作编码是否为"取消提交"动作
	 * <li>是否以RECALL开头
	 * 
	 * @param actionName
	 *            动作编码，见<code>IPFActionName</code>
	 * @return
	 */
	public static boolean isRecallAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()==null) {
			int leng = IPFActionName.RECALL.length();
			return actionName.length() >= leng && actionName.toUpperCase().substring(0, leng).equals(IPFActionName.RECALL);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.CancelSubmitWorkflow.getIntValue();
		}
	}

	public static boolean isUnSaveAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		BillactionVO actionVo = getBillActionVO(actionName, billtype);
		if(actionVo == null || actionVo.getAction_type()==null) {
			int leng = IPFActionName.UNSAVE.length();
			return actionName.length() >= leng && actionName.toUpperCase().substring(0, leng).equals(IPFActionName.UNSAVE);
		}else {
			return actionVo.getAction_type()==BillActionTypeEnum.CancelSubmitApproveflow.getIntValue();
		}
	}

	public static boolean isSignalFlowAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		return isSignalAction(actionName, billtype)||isApproveAction(actionName, billtype);
	}

	public static boolean isStartFlowAction(String actionName, String billtype) {
		if(actionName != null && actionName.length() > 20){
			actionName = actionName.substring(0, actionName.length() - 20);
		}
		return isStartAction(actionName, billtype)||isSaveAction(actionName, billtype);
	}

}