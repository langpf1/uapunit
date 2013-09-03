package uap.workflow.engine.itf;
import uap.workflow.engine.vos.AssignmentVO;
public interface IAssignmentBill {
	void insert(AssignmentVO[] vos);
	void update(AssignmentVO[] vos);
	void saveOrUpdate(AssignmentVO vos);
	boolean delete(String proInsPk, String activityId, String userPk);
	boolean delete(String pk_assignment);
}
