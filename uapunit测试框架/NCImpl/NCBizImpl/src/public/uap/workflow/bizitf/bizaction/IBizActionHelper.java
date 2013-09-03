/**
 * 
 */
package uap.workflow.bizitf.bizaction;

import java.util.Vector;
import uap.workflow.bizimpl.BizAction;
import uap.workflow.bizitf.exception.BizException;


/**
 * @author 
 *
 */
public interface IBizActionHelper {
	
	
	/*
	 * 返回所有业务操作分组
	 */
	Vector<String> getObjectTypeGroupList() throws BizException;
	/*
	 * 按照对象类型返回业务类型
	 */
	Vector<String> getAllObjectTypeList() throws BizException;
	/*
	 * 按照分组返回业务类型
	 */
	Vector<String> getObjectTypeListByGroup(String group) throws BizException;
	
	
	
	/*
	 * 返回所有的对象类型及操作 
	 */
	Vector<BizAction> getAllBizActionList() throws BizException;
	/*
	 * 按照分组返回动作列表
	 */
	Vector<BizAction> getBizActionListByGroup(String group) throws BizException;
	/*
	 * 按照对象类型返回动作列表
	 */
	Vector<BizAction> getBizActionListByObjectType(String objectType) throws BizException;
	/*
	 * 返回对象类型对应的操作
	 */
	BizAction getBizActionByAction(String objectType, String action);	
	
	/*
	 * 按照对象操作类型匹配活动
	 */
	Boolean MatchActionToActivity(BizAction bizAction, Object activity);
}
