package uap.workflow.engine.itf;
import uap.workflow.engine.vos.EventSubscriptionVO;
public interface IEventSubscriptionBill {
	void asyn(EventSubscriptionVO vo);
	void delete(String pk_sub);
}
