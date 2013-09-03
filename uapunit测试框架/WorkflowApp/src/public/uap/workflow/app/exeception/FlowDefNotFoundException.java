package uap.workflow.app.exeception;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 流程平台-提交时找不到匹配的流程定义
 * 
 * @author guowl
 * @since 6.0
 * 
 */
public class FlowDefNotFoundException extends PFBusinessException {

	private final static String errMsg = NCLangRes4VoTransl.getNCLangRes()
			.getStrByID("pfworkflow1", "FlowDefNotFoundException-000000")/*
																		 * 单据没有对应的审批流定义
																		 * ,
																		 * 任何人都可审批通过
																		 * !
																		 */;

	public FlowDefNotFoundException() {
		super(errMsg);
	}

}
