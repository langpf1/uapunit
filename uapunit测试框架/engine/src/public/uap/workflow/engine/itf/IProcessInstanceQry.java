package uap.workflow.engine.itf;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.vo.pub.SuperVO;

import uap.workflow.engine.vos.ProcessInstanceVO;
public interface IProcessInstanceQry {
	ProcessInstanceVO getProInsVo(String proInsPk);
	ProcessInstanceVO getProcessInstanceVO(String form_instance_versionPK, String bizObejectKey);
	ProcessInstanceVO[] getProcessInstanceVOs(String form_instance_versionPK);
	List<ProcessInstanceVO> getAllProcessInstanceVOs(String sql, PaginationInfo page) throws DAOException;
	int getAllProcessInsNumber(String table);
}
