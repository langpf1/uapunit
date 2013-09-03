package uap.workflow.bizimpl.expression;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pf.pub.BillTypeCacheKey;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.uap.pf.metadata.IHeadBodyQueryItf;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBusinessEntity;
import nc.md.model.IComponent;
import nc.md.model.MetaDataException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.pf.PFRuntimeException;
import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.javax.ELContext;
import uap.workflow.engine.javax.ExtELResolver;
import uap.workflow.engine.session.WorkflowContext;

public class NCMetadataElResolver extends ExtELResolver {

	public VariableScope getVariableScope() {
		return variableScope;
	}
	public void setVariableScope(VariableScope variableScope) {
		this.variableScope = variableScope;
	}
	public NCMetadataElResolver() {
		
	}
	public NCMetadataElResolver(VariableScope variableScope) {
		this.variableScope = variableScope;
	}

	private Object getValueByMetaPath(ELContext context,
			AggregatedValueObject vo, String metaPath) throws MetaDataException {

		Object value = null;
		NCObject ncobject = NCObject.newInstance(vo); // TODO 需要处理Cache
		try {
			value = ncobject.getAttributeValue(metaPath);
			Object[] result = null;
			if (value instanceof Object[]){
				int length = ((Object[])value).length;
				Object[] obj = ((Object[])value);
				result = new Object[length];
				for(int i = 0; i < obj.length; i++){
					result[i] = ((NCObject)obj[i]).getContainmentObject();
				}
				context.setPropertyResolved(true);
				return result;
			}else{
				
				context.setPropertyResolved(true);
				return value;//ncobject.getContainmentObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// if(value instanceof Object[]) {
		// Object[] tempAry = (Object[])value;
		// value = tempAry.length==0?null:tempAry[0];
		// }
		//return value;
	}

	private IBusinessEntity queryMetaOfBilltype(String strTypeCode) throws BusinessException {
		IComponent comp = queryComponentOfBilltype(strTypeCode);
		if (comp == null)
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000028", null, new String[]{strTypeCode})/*找不到该单据或交易类型{0}关联的元数据组件*/);
		return comp.getPrimaryBusinessEntity();
	}

	/**
	 * 查询单据类型关联的元数据组件 
	 * @param strTypeCode 单据类型或交易类型
	 * @return
	 * @throws BusinessException
	 */
	public static IComponent queryComponentOfBilltype(String strTypeCode) throws BusinessException {

		//必须保证单据类型关联了元数据实体
		checkBilltypeRelatedMeta(strTypeCode);

		BilltypeVO btVO = PfDataCache.getBillType(new BillTypeCacheKey().buildBilltype(strTypeCode).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		String strComp = btVO.getComponent();
		if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			//如果为交易类型，则获取其单据类型关联的元数据组件，因为交易类型中没有这个信息
			btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(btVO.getParentbilltype()));
			strComp = btVO.getComponent();
		}

		if (StringUtil.isEmptyWithTrim(strComp))
			throw new PFBusinessException("找不到该单据或交易类型{0}关联的元数据组件");

		return MDBaseQueryFacade.getInstance().getComponentByName(strComp.trim());
	}

	public static void checkBilltypeRelatedMeta(String strTypeCode) {
		BilltypeVO btVO = PfDataCache.getBillTypeInfo(
				new BillTypeCacheKey().buildBilltype(strTypeCode).buildPkGroup(InvocationInfoProxy.getInstance().getGroupId()));
		if (btVO == null)
			throw new PFRuntimeException("流程平台缓存中不存在该单据或交易类型={0}");

		boolean hasMeta = false;
		if (btVO.getIstransaction() != null && btVO.getIstransaction().booleanValue()) {
			//如果为交易类型，则获取其单据类型关联的元数据组件，因为交易类型中没有这个信息
			btVO = PfDataCache.getBillTypeInfo(new BillTypeCacheKey().buildBilltype(btVO.getParentbilltype()));
			hasMeta = !StringUtil.isEmptyWithTrim(btVO.getComponent());
		} else
			hasMeta = !StringUtil.isEmptyWithTrim(btVO.getComponent());
		if (!hasMeta)
			throw new PFRuntimeException("单据或交易类型{0}没有关联元数据实体");
	}
	
	private AggregatedValueObject reloadBizData(String billType, String billID) throws Exception{
		// 1.先查询单据类型关联的元数据模型，使用业务接口IHeadBodyQueryItf查询单据VO
		IBusinessEntity be = null;
		try {
			be = queryMetaOfBilltype(billType);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		IHeadBodyQueryItf qi = NCObject.newInstance(be, null).getBizInterface(IHeadBodyQueryItf.class);
		if (qi != null) {
			try {
				return (AggregatedValueObject)qi.queryAggData(billType, billID);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		} else
			throw new Exception("单据实体没有实现IHeadBodyQueryItf，无法查询到单据子表VO");
		return null;

	}
	
	public Object getValue(ELContext context, Object base, Object propertyPath) {

		Object value = null;
		// 不是变量的话，应该是元数据上的继续按照NC业务取值，VO 上的属性

		Object object = null;
		IBusinessKey bizObject = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
		if (bizObject instanceof BizObjectImpl){
			object = ((IBusinessKey)bizObject).getBizObjects()[0];
		}else{
			return null;
		}

		if (object == null){	//当前环节没有数据，取流程的默认数据
			IBusinessKey formInfo = (IBusinessKey)variableScope.getVariable("APP_FORMINFO");
			try {
				object = reloadBizData(formInfo.getBillType(), formInfo.getBillId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (object == null)
			return null;
 
		AggregatedValueObject vo = null;

		if (object instanceof AggregatedValueObject)
			vo = (AggregatedValueObject) object;
		else
			return null;

		// 对a.b.c的子实体属性的解析
		try {
			if (vo.getClass().getSimpleName().equals(propertyPath)) {
				object = vo;
				context.setPropertyResolved(true);
			} else {
				value = getValueByMetaPath(context, vo, propertyPath.toString());
				if (!context.isPropertyResolved()){
					context.setPropertyResolved(true);
					return object;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return base.getClass();
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		return Object.class;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return true;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property,
			Object value) {
		// TODO Auto-generated method stub

	}

	// FormulaParseFather f = getFormulaParser();
	// f.setExpress(formula);
	// Double[] paramsB = new Double[] { 0.82545, 578478.0, 0.45645 };
	// Double[] paramsC = new Double[] { 500.24564, -45648.0, 21345.0 };
	// f.addVariable("b", paramsB);
	// f.addVariable("c", paramsC);
	// Object[][] result = f.getValueOArray();
}
