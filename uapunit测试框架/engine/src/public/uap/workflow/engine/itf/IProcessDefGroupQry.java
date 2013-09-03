package uap.workflow.engine.itf;

import uap.workflow.engine.vos.ProcessDefGroupVO;

public interface IProcessDefGroupQry {
	ProcessDefGroupVO [] getAllProcessDefGroup();
	void insert(ProcessDefGroupVO vo);
}
