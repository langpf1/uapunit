package uap.workflow.engine.itf;
import uap.workflow.engine.vos.AssignmentVO;
public interface IAssignmentQry {
	AssignmentVO[] getAssignmentVos(String pk_proins, String activity_id);
	AssignmentVO getAssignmentVo(String pk_proins, String activity_id, String order_str);
}
