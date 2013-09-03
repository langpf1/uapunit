/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.bizimpl.bizinvocation;

import java.lang.reflect.Method;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.bizitf.exception.BizException;
import uap.workflow.bpmn2.model.NameSpaceConst;
import uap.workflow.engine.bpmn.behavior.ExtensionService;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.xml.Element;

/**
 * {@link ActivityBehavior} used when 'delegateExpression' is used for a
 * serviceTask.
 * 
 */
public class GenerateBillActivityBehavior extends ExtensionService {

	protected String destBillTypes;
	protected String destBillTypeVarName;

	public GenerateBillActivityBehavior(){
		
	}
	public GenerateBillActivityBehavior(String resultVariable, String billTypes, String destBillNames) {
		this.resultVariable = resultVariable;
		this.destBillTypes = billTypes;
		this.destBillTypeVarName = destBillNames;
	}

	// @Override
	public void signal(IActivityInstance execution, String signalName, Object signalData) throws Exception {
		// Object delegate = expression.getValue(execution);
		// if (delegate instanceof SignallableActivityBehavior) {
		// ((SignallableActivityBehavior) delegate).signal(execution,
		// signalName, signalData);
		// }
		super.signal(execution, signalName, signalData);
		return;
	}
	private Object executeExchangeByBill(IActivityInstance execution,
			String srcBillType, 
			String[] destBills, 
			AggregatedValueObject srcBillVO, 
			Object userObj, 
			PfParameterVO rcParaVo) throws Exception{
		
		String className = "uap.workflow.bizimpl.bizinvocation.VOExchange";
		Class<?> clz = Class.forName(className);
		Object objExchange = clz.newInstance();
		Class<?>[] parameters = new Class<?>[]{String.class, String[].class, AggregatedValueObject.class, Object.class, PfParameterVO.class};
		Method method = clz.getMethod("generateBill", parameters);
		Object result = method.invoke(objExchange, new Object[]{srcBillType, destBills, srcBillVO, userObj, rcParaVo});
		return result;
	}
	
	private Object callVOExchangeByObject(IActivityInstance execution,
			String srcBillType, String[] destBills, AggregatedValueObject srcBillVO) throws Exception{
		String className = "uap.workflow.bizimpl.bizinvocation.VOExchange";
		Class<?> clz;
		clz = Class.forName(className);
		Object objExchange = clz.newInstance();
		Class<?>[] parameters = new Class<?>[]{String.class, String[].class, AggregatedValueObject.class};
		Method method = clz.getMethod("generateBillAccordingObject", parameters);
		Object result = method.invoke(objExchange, new Object[]{srcBillType, destBills, srcBillVO});
		return result;
	}

	public void parseExtensionService(Element serviceTaskElement) throws RuntimeException {
		resultVariable = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariableName");
		if (resultVariable == null)
			resultVariable = "generatedResult";
		Element elementExtendElements = serviceTaskElement.element("extensionElements");
		if (elementExtendElements != null) {
			Element elementField = elementExtendElements.elementNS(NameSpaceConst.BIZEX_URL, "field");
			if (elementField != null) {
				destBillTypeVarName = elementField.attribute("name");
				destBillTypes = elementField.attribute("expression");
			}
		}
		if (destBillTypes == null && destBillTypeVarName == null) {
			throw new BizException("MUST set dest bill type in 'fieldExtension' ");
		//} else {
		//	activity.setActivityBehavior(this);
				//new ServiceTaskGenerateBillActivityBehavior(resultVariable, destBillTypes, destBillTypeVarName));
		}

	}
	@Override
	public void executeBehavior(IActivityInstance execution) throws Exception {
		try{
			IProcessInstance procInstance = execution.getProcessInstance();
			IBusinessKey bizObject = (IBusinessKey)procInstance.getVariable("APP_FORMINFO");
			String srcBillType = bizObject.getBillType();
			String[] destBills;
			if (destBillTypes.isEmpty()){
				destBills = (String[])execution.getVariable(destBillTypeVarName);
			}else{
				Expression billTypesExpression = BizProcessServer.getProcessEngineConfig().getExpressionManager().createExpression("${"+destBillTypes+"}");
				String billTypes = (String) billTypesExpression.getValue(execution);
				destBills = billTypes.split(",");
			}
			AggregatedValueObject srcBillVO = null;
			if (bizObject instanceof PfParameterVO)
				srcBillVO = (AggregatedValueObject)((PfParameterVO)bizObject).m_billEntity;
			else if (bizObject instanceof BizObjectImpl){
				srcBillVO = (AggregatedValueObject)((BizObjectImpl)bizObject).getBizObjects()[0];
			}else
				throw new Exception("生单对象传递不正确！");
			Object userObj = null;
			PfParameterVO srcParaVo = null;
			Object result = null;
			if (srcBillType == null){
				result = executeExchangeByBill(execution, srcBillType,destBills,srcBillVO,userObj, srcParaVo);
			}else{
				result = callVOExchangeByObject(execution, srcBillType, destBills, srcBillVO);
			}
			execution.setVariable(resultVariable, result);
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}
