package uap.workflow.engine.itf;
import uap.workflow.engine.vos.BeforeAddSignVO;
public interface IBeforeAddSignQry {
	BeforeAddSignVO[] getBeforeAddSignVoByTaskPk(String taskPk);
	BeforeAddSignVO getBeforeAddSignVoByTaskPkAndTime(String taskPk, String time);
	BeforeAddSignVO getBeofreAddSingVoByTaskAndTimeAndOrder(String taskPk, String time, String order, boolean isusered);
	BeforeAddSignVO getBeforeAddSingVoByAddSignPk(String addSignPk);
	String getMaxStateTimeByTaskPk(String taskPk);
}
