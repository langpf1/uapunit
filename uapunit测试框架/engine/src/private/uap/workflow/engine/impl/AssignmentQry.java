package uap.workflow.engine.impl;
import nc.bs.dao.DAOException;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IAssignmentQry;
import uap.workflow.engine.vos.AssignmentVO;
/*ָ���������ĳһ�ε�ִ����˵�ģ�����������ʵ����pk�������ָ���ǵ�ǰִ���˶���һ�����Ա��ȷ�������Ǻ���һ�����id���*/
public class AssignmentQry implements IAssignmentQry {
	@Override
	public AssignmentVO[] getAssignmentVos(String pk_proins, String activity_id) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String whereStr = "pk_proins='" + pk_proins + "' and activity_id='" + activity_id + "' order by order_str";
			AssignmentVO vos[] = (AssignmentVO[]) dao.queryByCondition(AssignmentVO.class, whereStr);
			if (vos == null || vos.length == 0) {
				return null;
			}
			return vos;
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@Override
	public AssignmentVO getAssignmentVo(String pk_proins, String activity_id, String order_str) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String whereStr = "pk_proins='" + pk_proins + "' and activity_id='" + activity_id + "' and order_str='" + order_str + "'";
			AssignmentVO[] vos = (AssignmentVO[]) dao.queryByCondition(AssignmentVO.class, whereStr);
			if (vos == null || vos.length == 0) {
				return null;
			}
			return vos[0];
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	public AssignmentVO getAssignmentVo(String pk_proins, String order_str) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String whereStr = "pk_proins='" + pk_proins +"' and order_str='" + order_str + "'";
			AssignmentVO[] vos = (AssignmentVO[]) dao.queryByCondition(AssignmentVO.class, whereStr);
			if (vos == null || vos.length == 0) {
				return null;
			}
			return vos[0];
		} catch (DAOException e) {
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
}
