package uap.workflow.bizimpl.listener;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.bizimpl.bizinvocation.PfParameterVO;
import uap.workflow.bizitf.actioninvocation.IPfRun;
import uap.workflow.engine.core.ExtExecutionListener;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.session.WorkflowContext;

public class ListenerBizImplExtend  extends ExtExecutionListener{

	private PfParameterVO BuildPfParameterVOFromFormInfoCtx(IBusinessKey formInfo) {
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_billType = formInfo.getBillType();
		//paraVo.m_businessType = formInfo.getTranstype();
		//paraVo.m_pkOrg = formInfo.getPkorg();
		paraVo.m_billNo = formInfo.getBillNo();
		paraVo.m_billId = formInfo.getBillId();
		//paraVo.m_billVersionPK = formInfo.getBillVersionPK();
		//AggregatedValueObject[] objects = new AggregatedValueObject[formInfo.getBillVos().length];
		//int i = 0;
		//for(Object obj : formInfo.getBillVos()){
		//	objects[i++] = (AggregatedValueObject)obj;
		//}
		//paraVo.m_preValueVos = objects;
		//paraVo.m_pkGroup = formInfo.getPk_group();
		//paraVo.m_workFlow.checknote = formInfo.getApproveNote();
		return paraVo;
	}

	private void executeCompatible(ListenerDefinition definition, IActivityInstance execution) {
		try {
			Object clzInstance = Class.forName(definition.getImplementation()).newInstance();
			IBusinessKey ctx = WorkflowContext.getCurrentBpmnSession().getBusinessObject();
			
			PfParameterVO paraVo = BuildPfParameterVOFromFormInfoCtx(ctx);
			if (clzInstance instanceof IPfRun) {
				((IPfRun) clzInstance).runComClass(paraVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notify(IActivityInstance execution) throws Exception {
		executeCompatible(this.getListenerDefinition(), execution);
		
	}
}
