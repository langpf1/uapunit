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
 * ƽ̨�Ļ�����
 * 
 * @author ���ھ� 2002-4-16
 * @modifier leijun 2006-8-4 ����һЩ�������� .
 */
public class PfUtilBaseTools {
	/**
	 * �����������չ����
	 */
	public static final String PARAM_FLOWPK = "flowdefpk"; // ���̶���PK

	public static final String PARAM_NO_LOCK = "nolockandconsist"; // ���������һ����У��

	public static final String PARAM_NOFLOW = "nosendmessage"; // �����������������̡�����������

	public static final String PARAM_SILENTLY = "silently"; // �����������������̡��������̣���Ĭ�Ͻ���������

	public static final String PARAM_NOTE_CHECKED = "notechecked"; // �������Ƿ��Ѽ��

	public static final String PARAM_RELOAD_VO = "reload_vo"; // ���¼���VO

	public static final String PARAM_BATCH = "batch"; // �Ƿ�������
	
	//�����Ϊ��Ӧ�����ӣ�������ʱ���ǰ�һ�����ݷֶ�δ��룬�����ֲ��뵯������������
	//ƽ̨��������������жϣ���������д���workflownotevo�����ٵ���ֱ�Ӵ���������л�ȡ�û������������
	public static final String PARAM_WORKNOTE = "worknote"; 
	
	//������û�����̶��壬������Ҫ����ΪԤ�㿪������asked by qiaoye������
	public static final String PARAM_NOTSILENT = "notsilent";
	
	//��������ĳһ�����Ƿ���Ҫִ�ж����������ʲ���������(zhangzhij) 
	public static final String PARAM_DRIVEACTION="driveaction";
	
	//��¼���ݵ�ǰ�����ʱ�䣬����������ʱ��ָ�
	public static final String PARAM_PREAPPROVEDATE="preapprovedate";

	/**
	 * �ӵ��ݺ��������л�ȡ������VO�������Զ��庯��
	 * <li>functionnote������"<"��ͷ
	 * <li>�����Ĳ����в��ܰ���VO����
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

				if ((fVO.getArguments() != null) && (fVO.getArguments().indexOf(".") > 0)) // ��ʾ���Ǻ���
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
		// �õ�Ŀ�ĵ��ݵľۺ�VO������
		String destBillVoClzName = null;
		IBusinessEntity destBE = PfMetadataTools.queryMetaOfBilltype(destBillOrTranstype);
		IBeanStyle bs = destBE.getBeanStyle();
		if (bs instanceof AggVOStyle) {
			destBillVoClzName = ((AggVOStyle) bs).getAggVOClassName();
		} else
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000009")/*����ʵ�������Ͼۺ�VO����ʽ*/);

		AggregatedValueObject[] retDestVOs;
		try {
			retDestVOs = (AggregatedValueObject[]) Array.newInstance(Class.forName(destBillVoClzName), size);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000034", null, new String[]{e.getMessage()})/*��ʼ��Ŀ�ĵ���VO��������쳣��{0}*/);
		}
		return retDestVOs;
	}

	/**
	 * �ӵ���VO����������ֵ�л�ȡ����ID��No��Ϣ
	 * 
	 * @param singleBillEntity
	 *            ��������ʵ�����
	 * @param retObj
	 *            ����������ֵ
	 */
	public static void fetchBillId(PfParameterVO paravo, Object singleBillEntity, Object retObj) {
		// �Ӷ����ű�ִ�к�ķ���ֵ�л�õ���Id
		paravo.m_billVersionPK = getBillID(retObj);
		/***********************************************************************
		 * �����������SAVE�����ű�����ʽ����,����Ҫ�ٴλ�ȡbillNo lj+ 2005-3-10
		 **********************************************************************/
		// ��ȡ������ʵ����ƽ̨�����Ϣ
		PublicHeadVO standHeadVo = new PublicHeadVO();
		// ����Ԫ����ģ����Ϣ����ȡ
		getHeadInfoByMeta(standHeadVo, singleBillEntity, paravo.m_billType);

		paravo.m_billNo = standHeadVo.billNo;
		if (paravo.m_billVersionPK == null)
			// ����ӷ���ֵ�л�ȡ��billIdΪ��,��ȡ����VO�еĵ���PK
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
	 * ����PK�ӱ��л�ȡĳ�ֶ�ֵ
	 * 
	 * @param tableName
	 *            ����
	 * @param pkFieldName
	 *            �����ֶ�
	 * @param columnName
	 *            ��ѯ�ֶ�
	 * @param pks
	 *            ����ֵ ����
	 * @return ��ѯ����ֵ����
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
	 * ��ѯĳ�������͵ĸ���������
	 * <li>���ݵ��ݴ���bd_billtype.billstyle
	 * 
	 * @param btVO
	 *            ��������VO����
	 * @return
	 */
	private static String findParentBilltypeByStyle(BilltypeVO btVO) {
		if (btVO.getBillstyle() == null)
			return null;
		String parentBilltype = PfDataCache.getBillTypeByStyle(btVO.getBillstyle().toString());
		if (parentBilltype == null || parentBilltype.equals(btVO.getPk_billtypecode()))
			// ���ݴ��಻���� ������Ǹ���������
			return null;
		return parentBilltype;
	}

	/**
	 * ��ѯ�������Ͷ�Ӧ��ʵ������ݿ�������ֶ�
	 * 
	 * @param billType
	 *            ��������PK
	 * @return
	 * @throws BusinessException
	 */
	public static String findPkField(String billType) throws BusinessException {
		// ��Ԫ����ģ�ͻ�ȡ
		IBean bean = PfMetadataTools.queryMetaOfBilltype(billType);
		return bean.getTable().getPrimaryKeyName();
	}

	/**
	 * ������л�������VO��
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
				// ȡ��һ����Ĭ�ϣ���������
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
	 * �Ӷ����ű�ִ�к�ķ���ֵ�л�õ���Id
	 * <li>����ֵ����ΪArrayList��String����
	 * 
	 * @param tmpObj
	 */
	private static String getBillID(Object tmpObj) {
		Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000037")/*****�Ӷ����ű�ִ�к�ķ���ֵ�л�õ���Id��ʼ*****/);
		String billId = null;
		if (tmpObj == null) {
			return billId;
		}

		if (tmpObj instanceof ArrayList) {
			ArrayList alRetObj = (ArrayList) tmpObj;
			Object[] retObjAry = alRetObj.toArray();
			if (retObjAry.length > 1 && retObjAry[1] instanceof ArrayList) {
				billId = ((ArrayList) retObjAry[1]).toArray()[0].toString();
				Logger.debug("��õ���Id:Array�ĵ�һά������ĵ�0άΪ����ID");
			} else {
				if (retObjAry[0] != null && !retObjAry[0].equals("")) {
					billId = retObjAry[0].toString();
					Logger.debug("��õ���Id:Array�ĵ�0άΪ����ID");
				}
			}
		} else if (tmpObj instanceof String) {
			billId = (String) tmpObj;
			Logger.debug("ֱ�ӴӶ���Stringȡ��ID");
		}
		Logger.debug("****�Ӷ����ű�ִ�к�ķ���ֵ�л�õ���Id=" + billId + "����****");
		return billId;
	}

	/**
	 * �ӵ���ʵ���л�ȡƽ̨�����Ϣ
	 * <li>���ݵ���ʵ���Ԫ����ģ��
	 * 
	 * @param headvo
	 * @param singleBillEntity
	 *            ����ʵ����󣬷�����
	 * @param billType
	 * @return ����Ʒ�Ƿ��ص��ݵ�ǰ�����ʱ��
	 * @since 5.5
	 */
	public static int getHeadInfoByMeta(PublicHeadVO headvo, Object singleBillEntity, String billType) {
		headvo.billType = billType;

		IFlowBizItf fbi = PfMetadataTools.getBizItfImpl(singleBillEntity, IFlowBizItf.class);
		if (fbi == null)
			throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000038")/*Ԫ����ʵ��û���ṩҵ��ӿ�IFlowBizItf��ʵ����*/);

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
	 * ���ݽ������ͻ򵥾����͵ı��뷵����ʵ�ĵ������ͱ���
	 * 
	 * @param strTypeCode
	 *            �������ͻ�������PK
	 * @return
	 */
	public static String getRealBilltype(String strTypeCodestr) {
		// String strEditBilltype =
		BillTypeCacheKey strTypeCode = new BillTypeCacheKey().buildBilltype(strTypeCodestr).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId());
		String billtype = strTypeCode.getBilltype().trim();
		strTypeCode.buildBilltype(billtype);

		// XXX::���ݻ����ȡ��������
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(strTypeCode);
		if (btVO == null)
			throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "PfUtilBaseTools-000000", null, new String[]{billtype})/*��������ƽ̨�����в����ڸõ��ݻ�������={0}*/);
		else if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			// ���ѡ�е��ǽ������ͣ�����䵥�����ͷ���
			Logger.debug("��õ������ͻ�������=" + strTypeCode + "����ʵ��������=" + billtype);
			billtype =  btVO.getParentbilltype();
		} 
		return billtype;

	}
	
	/**
	 * ���ݽ������ͻ򵥾����͵�PK������ʵ�ĵ������ͱ���
	 * 
	 * @param strPK �������ͻ�������PK
	 * @return ��ʵ�ĵ������ͱ���
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
		throw new PFRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "PfUtilBaseTools-000000", null, new String[]{strPK})/*��������ƽ̨�����в����ڸõ��ݻ�������={0}*/);
	}

	/**
	 * ���ݵ������Ͳ�ѯ�������ӱ��������
	 * 
	 * @param pkBillType
	 *            ��������PK
	 * @return ����[0]=���ݾۺ�Vo;����[1]=��������Vo;����[2]=�����ӱ�Vo
	 * @throws BusinessException
	 */
	public static String[] getStrBillVo(String pkBillType) throws BusinessException {
		String[] retStrs = new String[3];
		// ʹ��Ԫ����ģ��
		IBusinessEntity be = PfMetadataTools.queryMetaOfBilltype(pkBillType);
		IBeanStyle bs = be.getBeanStyle();
		if (bs instanceof AggVOStyle) {
			retStrs[0] = ((AggVOStyle) bs).getAggVOClassName();
		} else
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000009")/*����ʵ�������Ͼۺ�VO����ʽ*/);
		retStrs[1] = be.getFullClassName();
		// ȡ����ʵ������1:n��ϵ�ʵ��
		List<IBean> lRelatedBeans = be.getRelatedEntities(AssociationKind.Composite, ICardinality.ASS_ONE2MANY);

		// FIXME:ȡ��һ����ϵ�ʵ�壿
		IBean relatedBean = lRelatedBeans.size() > 0 ? lRelatedBeans.iterator().next() : null;
		retStrs[2] = relatedBean == null ? null : relatedBean.getFullClassName();

		return retStrs;
	}

	/**
	 * ��ȡ�����ֵ
	 * @throws BusinessException 
	 */
	public static PfParameterVO getVariableValue(String billType, String actionName, AggregatedValueObject billvo, AggregatedValueObject[] billvos, Object userObj, Object[] userObjs,
			WorkflownoteVO worknoteVO, HashMap hmParam, Hashtable hashBilltypeToParavo) throws BusinessException {
		// ƽ̨��־
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") START<<<");

		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_billType = billType;

		// ��ȡ��������
		PublicHeadVO standHeadVo = new PublicHeadVO();
		AggregatedValueObject singleVO = null;
		if (billvo != null) {
			singleVO = billvo;
		} else if (billvos != null && billvos.length > 0) {
			singleVO = billvos[0];
		}

		// ����Ԫ����ģ������ȡ
		int isEmend=  getHeadInfoByMeta(standHeadVo, singleVO, billType);

		// fgjΪ��ʵ����ҵ�����͵Ĺ�����
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
//		// ���������Ϊ�����������Ϊ�Ƶ���
//		if (StringUtil.isEmptyWithTrim(paraVo.m_operator))
//			paraVo.m_operator = paraVo.m_makeBillOperator;

		// XXX:����н������ͣ���paraVo��billtypeȡ��������
		if (!StringUtil.isEmptyWithTrim(standHeadVo.transType))
			paraVo.m_billType = standHeadVo.transType;

		paraVo.putCustomPropertyBatch(hmParam);

		// ƽ̨��־
		Logger.debug(">>>billType=" + paraVo.m_billType + " busiType=" + paraVo.m_businessType);
		Logger.debug(">>>billMaker=" + paraVo.m_makeBillOperator + " operator=" + paraVo.m_operator);
		Logger.debug(">>>corpPK=" + paraVo.m_pkOrg + " billId=" + paraVo.m_billVersionPK);
		Logger.debug(">>>actionName=" + paraVo.m_actionName + " billNo=" + paraVo.m_billNo);
		// ���õ�������-����������VO
		if(hashBilltypeToParavo != null){
			String src_billtypePK =StringUtil.isEmptyWithTrim(paraVo.m_preValueVo.getParentVO().getPrimaryKey())?"":paraVo.m_preValueVo.getParentVO().getPrimaryKey();
			hashBilltypeToParavo.put(paraVo.m_billType+src_billtypePK, paraVo);
		}
			

		// ƽ̨��־
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") END<<<");

		return paraVo;
	}
	
	

	/**
	 * ��ȡ�����ֵ
	 */
	public static PfParameterVO getVariableValue(String billType, String actionName, AggregatedValueObject billvo, AggregatedValueObject[] billvos, Object userObj, Object[] userObjs,
			WorkflownoteVO worknoteVO, HashMap hmParam, Hashtable hashBilltypeToParavo, String src_billtyePK) {
		// ƽ̨��־
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") START<<<");

		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_billType = billType;

		// ��ȡ��������
		PublicHeadVO standHeadVo = new PublicHeadVO();
		AggregatedValueObject singleVO = null;
		if (billvo != null) {
			singleVO = billvo;
		} else if (billvos != null && billvos.length > 0) {
			singleVO = billvos[0];
		}

		// ����Ԫ����ģ������ȡ
		int isEmend =getHeadInfoByMeta(standHeadVo, singleVO, billType);

		// fgjΪ��ʵ����ҵ�����͵Ĺ�����
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
//		// ���������Ϊ�����������Ϊ�Ƶ���
//		if (StringUtil.isEmptyWithTrim(paraVo.m_operator))
//			paraVo.m_operator = paraVo.m_makeBillOperator;

		// XXX:����н������ͣ���paraVo��billtypeȡ��������
		if (!StringUtil.isEmptyWithTrim(standHeadVo.transType))
			paraVo.m_billType = standHeadVo.transType;

		paraVo.putCustomPropertyBatch(hmParam);

		// ƽ̨��־
		Logger.debug(">>>billType=" + paraVo.m_billType + " busiType=" + paraVo.m_businessType);
		Logger.debug(">>>billMaker=" + paraVo.m_makeBillOperator + " operator=" + paraVo.m_operator);
		Logger.debug(">>>corpPK=" + paraVo.m_pkOrg + " billId=" + paraVo.m_billVersionPK);
		Logger.debug(">>>actionName=" + paraVo.m_actionName + " billNo=" + paraVo.m_billNo);

		// ���õ�������-����������VO
		if(hashBilltypeToParavo != null)
			hashBilltypeToParavo.put(paraVo.m_billType+src_billtyePK, paraVo);

		// ƽ̨��־
		Logger.debug(">>>getVariableValue(" + actionName + "," + billType + ") END<<<");

		return paraVo;
	}
	
	

	/**
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"����"����
	 * <li>����"APPROVE"��ͷ
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
	 * �ж�ĳ���ݶ��������Ƿ�Ϊɾ������
	 * 
	 * @param actionName
	 *            ��������
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
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"����"����
	 * <li>�Ƿ���ROLLBACK��ͷ
	 * 
	 * @param actionName
	 *            �������룬��<code>IPFActionName</code>
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
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"�ύ"��"�༭"����
	 * <li>����"SAVE"��"EDIT"��ͷ
	 * 
	 * @param actionName
	 *            ��������
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
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"ִ�й�����"����
	 * <li>�Ƿ���SIGNAL��ͷ
	 * 
	 * @param actionName
	 *            ��������
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
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"����������"����
	 * <li>�Ƿ���START��β
	 * 
	 * @param actionName
	 *            ��������
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
	 * ���ݱ��루��PK���ж��Ƿ�Ϊ��������
	 * 
	 * @param strTypeCode
	 *            ��������(��������)PK
	 * @return
	 */
	public static boolean isTranstype(String strTypeCode) {
		// XXX::���ݻ����ȡ��������(��������)VO
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(strTypeCode).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		if (btVO!= null && btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue())
			return true;
		return false;
	}

	/**
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"����"����
	 * <li>�Ƿ���UNAPPROVE��ͷ
	 * 
	 * @param actionName
	 *            �������룬��<code>IPFActionName</code>
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
	 * �ж��¾�ֵ�Ƿ�ı���
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
	 * �����������ʹ� ��Ӧ����
	 * 
	 * @param dataType
	 *            ��������
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
	 * ��ʼ�����ݾۺ�VO����
	 * 
	 * @param strBillVoClassName
	 *            ���ݾۺ�VO������
	 * @param voLen
	 *            ʵ������
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
	 * ��ѯ�����Ѿ�������ɵ�������
	 * 
	 * @param billId
	 * @param billType
	 * @return ��','�ָ����������û�����
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

			Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000039", null, new String[]{billId,strAllCheckers.toString()})/*>>��õ���ID{0}������������={1}*/);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return ret;
	}

	/**
	 * ��ѯ�������ͣ��������ͣ���ͬԴ��������
	 * <li>�Ե���ʽ����
	 * 
	 * @param billType
	 *            �������ͣ��������ͣ�
	 * @return
	 * @since 5.5
	 */
	public static HashSet<String> querySimilarTypes(String billType) {
		HashSet<String> hsRet = new HashSet<String>();
		hsRet.add(billType);
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(billType).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			// Ϊ�������ͣ����ҵ��������ĵ�������
			BilltypeVO parentTypeVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype( btVO.getParentbilltype()).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
			hsRet.add(parentTypeVO.getPk_billtypecode());
			// ���������������͵ĸ���������
			String parentBilltype = findParentBilltypeByStyle(parentTypeVO);
			if (parentBilltype != null) {
				// ���ڸ���������
				hsRet.add(parentBilltype);
			}
		} else {
			// Ϊ��������
			String parentBilltype = findParentBilltypeByStyle(btVO);
			if (parentBilltype != null) {
				// ���ڸ���������
				hsRet.add(parentBilltype);
			}
		}
		return hsRet;
	}

	/**
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"ȡ���ύ"����
	 * <li>�Ƿ���RECALL��ͷ
	 * 
	 * @param actionName
	 *            �������룬��<code>IPFActionName</code>
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