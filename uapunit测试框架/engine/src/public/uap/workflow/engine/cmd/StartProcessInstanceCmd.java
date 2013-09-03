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
package uap.workflow.engine.cmd;
import java.io.Serializable;
import java.util.Map;

import nc.vo.pub.lang.UFBoolean;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.deploy.DeploymentCache;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.vos.AssignmentVO;
/**
 * @author Tom Baeyens
 */
public class StartProcessInstanceCmd<T> implements Command<IProcessInstance>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String processDefinitionPk;
	protected String processDefinitionId;
	protected Map<String, Object> variables;
	public StartProcessInstanceCmd(String proDefPk, String proDefId, Map<String, Object> variables) {
		this.processDefinitionPk = proDefPk;
		this.processDefinitionId = proDefId;
		this.variables = variables;
	}
	public IProcessInstance execute(CommandContext commandContext) {
		IProcessDefinition processDefinition = featchProcessDefinition();
		IProcessInstance processInstance = processDefinition.createProcessInstance();
		moveData(processInstance);
		updateCtx(processInstance);
		handlerAsssignInfo(processInstance);
		start(processInstance);
		return processInstance;
	}
	private void start(IProcessInstance processInstance) {
		if (variables == null) {
			processInstance.start();
		} else {
			processInstance.setVariables(variables);
			processInstance.start();
		}
	}
	private void moveData(IProcessInstance processInstance) {
		IBusinessKey formCtx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
		if (formCtx != null) {
			ProcessInstanceEntity proInsTity = (ProcessInstanceEntity) processInstance;
			proInsTity.setPk_form_ins(formCtx.getBillId());
			proInsTity.setPk_form_ins_version(formCtx.getBillId());
			//proInsTity.setPk_form_ins_version(formCtx.getBillVersionPK());
			//proInsTity.setPk_group(formCtx.getPk_group());
			//proInsTity.setPk_org(formCtx.getPkorg());
			proInsTity.setPk_bizobject(formCtx.getBillType());
			//proInsTity.setPk_biztrans(formCtx.getBusitype());
			proInsTity.asyn();
		}
	}
	private void updateCtx(IProcessInstance processInstance) {
		WorkflowContext.getCurrentBpmnSession().setProcessInstance(processInstance);
	}
	private IProcessDefinition featchProcessDefinition() {
		DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
		IProcessDefinition processDefinition = null;
		if (processDefinitionPk == null || processDefinitionPk.length() == 0) {
			processDefinition = deploymentCache.getProDefByProDefId(processDefinitionId);
		} else if (processDefinitionId == null || processDefinitionId.length() == 0) {
			processDefinition = deploymentCache.getProDefByProDefPk(processDefinitionPk);
		} else {
			throw new WorkflowException("processDefinitionKey and processDefinitionId are null");
		}
		return processDefinition;
	}
	private void handlerAsssignInfo(IProcessInstance processInstance) {
		FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		if (flowInfoCtx instanceof CommitProInsCtx || flowInfoCtx instanceof NextTaskInsCtx) {
			CommitProInsCtx commitProInsCtx = (CommitProInsCtx) flowInfoCtx;
			UserTaskRunTimeCtx[] nextInfos = commitProInsCtx.getNextInfo();
			if (nextInfos == null) {
				throw new WorkflowRuntimeException("无下一步的流程信息");
			}
			int length = nextInfos.length;
			for (int i = 0; i < length; i++) {
				UserTaskRunTimeCtx tmpCtx = nextInfos[i];
				AssignmentVO[] vos = new AssignmentVO[tmpCtx.getUserPks().length];
				for (int j = 0; j < tmpCtx.getUserPks().length; j++) {
					builderAssginInfo(processInstance, tmpCtx, vos, j);
				}
				WfmServiceFacility.getAssignmentBill().insert(vos);
			}
		}
	}
	private void builderAssginInfo(IProcessInstance processInstance, UserTaskRunTimeCtx tmpCtx, AssignmentVO[] vos, int j) {
		AssignmentVO assignInfo = new AssignmentVO();
		assignInfo.setActivity_id(tmpCtx.getActivityId());
		assignInfo.setPk_proins(processInstance.getProInsPk());
		assignInfo.setPk_user(tmpCtx.getUserPks()[j]);
		assignInfo.setSequence(UFBoolean.valueOf(tmpCtx.isSequence()));
		if (tmpCtx.isSequence()) {
			assignInfo.setOrder_str(String.valueOf(j));
		} else {
			assignInfo.setOrder_str(String.valueOf(0));
		}
		assignInfo.setDr(0);
		vos[j] = assignInfo;
	}
}
