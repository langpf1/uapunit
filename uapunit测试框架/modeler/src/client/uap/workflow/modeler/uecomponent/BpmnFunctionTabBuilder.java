package uap.workflow.modeler.uecomponent;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.logging.Logger;
import nc.uap.formula.NCFormulaHelper;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.formulaedit.AbstractTabBuilder;
import nc.ui.pub.formulaedit.FormulaWordSorter;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.change.UserDefineFunction;
import nc.vo.pf.pub.FunctionVO;
import nc.vo.pub.formulaedit.FormulaItem;
import nc.vo.pub.formulaset.FormulaParseFather;
import uap.workflow.bpmn2.model.FlowElement;

class BpmnFunctionTabBuilder extends AbstractTabBuilder {
	private HashMap<Object, Object> hsFormulaItems = null;
	private FlowElement source;
	private FlowElement target;
	
	public BpmnFunctionTabBuilder(FormulaWordSorter fws) {
		this(fws,null,null);
	}
	
	public BpmnFunctionTabBuilder(FormulaWordSorter fws,FlowElement source,FlowElement target){
		super(fws);
		this.source =source;
		this.target =target;
	}
	
	private ArrayList<FunctionVO> getFunctionVOs(){
		//��Դ��Ŀ��userTask���� Billtype,�ݴ�ȡ���ݺ���
		ArrayList<FunctionVO> alFuncVOs = PfUIDataCache.getFunctionsOfBilltype("");
		ArrayList<FunctionVO> alDestFuncVOs = PfUIDataCache.getFunctionsOfBilltype("");
		alFuncVOs.addAll(alDestFuncVOs);
		return alFuncVOs;
	}
	
	private FormulaParseFather getFormulaParser() {
		FormulaParseFather fpf = NCFormulaHelper.getEfficientButUnsafeFormulaParser("bpmnConditionExpression_formularule");
		return fpf;
	}
 
	@Override
	public HashMap getHSFormulaItems() {
		if (hsFormulaItems == null) {
			hsFormulaItems = new HashMap<Object, Object>();

			//��ѯ��Դ���ݺ�Ŀ�ĵ��ݵ����п����ڽ����ĵ��ݺ���
			try {
				
				
				UserDefineFunction[] udfs = PfUtilBaseTools.changeFunctionVOs(getFunctionVOs());
				for (UserDefineFunction udf : udfs) {
					String fName = udf.getFunctionNote().substring(1, udf.getFunctionNote().length() - 1)
							+ "(" + udf.getMethodName() + ")";
					hsFormulaItems.put(fName, new FormulaItem(fName, getInputSign(udf), fName));

					//XXX:�ѵ��ݺ������õ���ʽ�������У���У�鹫ʽ
					getFormulaParser().setSelfMethod(udf.getClassName(), udf.getMethodName(),
							udf.getReturnType(), udf.getArgTypes());
				}
				
				String fName = "ȡ�������";
				hsFormulaItems.put(fName, new FormulaItem(fName, "getExchangedResult(,)", "ȡ�������(�������������������)"));
//
//				//XXX:�ѵ��ݺ������õ���ʽ�������У���У�鹫ʽ
//				getFormulaParser().setSelfMethod("uap.workflow.bizimpl.bizinvocation.VOExchange", "getVOExchangedResult",
//						AggregatedValueObject[].class, new Class[]{String.class, Object.class});
				//getFormulaParser().addFunction("getExchangedResult", new exNcInnerFunction());



			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return hsFormulaItems;

	}

	@Override
	public String getTabName() {
		return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000585")/*���ݺ���*/;
	}

	private String getInputSign(UserDefineFunction udf) {
		String methodName = udf.getMethodName();

		int ll = udf.getArgNames().length;
		StringBuffer sb = new StringBuffer();
		String after = null;
		
		if (ll > 0) {
			sb.append("(");
			for (int i = 0; i < ll; i++) {
				sb.append(" ,");
			}
			if (sb.length() == 1) {
				sb.append(" )");
				
				after = sb.toString();
			} else {
				after = sb.substring(0, sb.length() - 1) + ")";
			}
		}

		methodName += after;

		return methodName;
	}

}