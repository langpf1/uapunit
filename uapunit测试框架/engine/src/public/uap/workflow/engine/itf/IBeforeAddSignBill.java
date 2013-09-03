package uap.workflow.engine.itf;
import uap.workflow.engine.vos.BeforeAddSignUserVO;
import uap.workflow.engine.vos.BeforeAddSignVO;
public interface IBeforeAddSignBill {
	void deleteBeforeAddSignVoByTaskPk(String taskPk);
	void saveBeforeAddSignVo(BeforeAddSignVO addSignVo);
	void deleteBeforeAddSignUserVo(String pk_beforeaddsign, String userPk, String order_str);
	void updateBeforeAddSignUserVo(BeforeAddSignUserVO userVo);
}
