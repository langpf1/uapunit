package uap.workflow.bizimpl.bizaction;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.BusinessException;
import uap.workflow.bizimpl.BizAction;
import uap.workflow.bizimpl.BizActionType;
import uap.workflow.bizimpl.BizObjectType;
import uap.workflow.bizitf.bizaction.IBizActionHelper;
import uap.workflow.bizitf.exception.BizException;


public class BizActionHelper implements IBizActionHelper {

	/*
	 *	取业务对象类型分组列表
	 *	Vector 考虑带顺序 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getObjectTypeGroupList() throws BizException
	{
		//查找出来的单据类型要按按领域排序。
		String sql = "select distinct systemcode from bd_billtype " +
			" order by nodecode,systemcode,pk_billtypecode";

		Vector<String> groups = new Vector<String>();
		try {
			IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<Object[]> ret = (List<Object[]>) qry.executeQuery(sql,
					new ArrayListProcessor());

			for (int i = 0; i < ret.size(); i++) {
				groups.add(ret.get(i).toString());
			}
			return groups;
			
		} catch (BusinessException e) {
			throw new BizException(e);
		}
	}
	
	/*
	 * 取所有业务对象类型列表
	 * @see uap.workflow.bizitf.bizaction.IBizActionHelper#getAllObjectTypeList()
	 */
	@Override
	public Vector<String> getAllObjectTypeList()  throws BizException
	{
		return getObjectTypeAction("1=1");
	}

	/*
	 * 按照分组取业务对象类型列表
	 * @see uap.workflow.bizitf.bizaction.IBizActionHelper#getObjectTypeListByGroup(java.lang.String)
	 */
	@Override
	public Vector<String> getObjectTypeListByGroup(String group) throws BizException
	{
		return getObjectTypeAction("systemcode='" + group + "'");
	}

	@SuppressWarnings("unchecked")
	private Vector<String> getObjectTypeAction(String condition) throws BizException
	{
		Vector<String> vos = new Vector<String>();
		String sql = "select pk_billtypecode from bd_billtype ";
		sql += " where " + condition;
		sql += " order by systemcode, pk_billtypecode";
		try{
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<Object[]> list = (List<Object[]>)query.executeQuery(sql, new ArrayListProcessor());
			for (int i = 0; i < list.size(); i++){
				vos.add(list.get(i).toString());
			}
		}catch(BusinessException e){
			throw new BizException(e);
		}
		return vos;
	}


	/*
	 * 取所有业务操作列表
	 * @see uap.workflow.bizitf.bizaction.IBizActionHelper#getAllBizActionList()
	 * 
	 */
	@Override
	public Vector<BizAction> getAllBizActionList() throws BizException
	{
		//TODO 性能问题和客户端缓存问题
		return getBizActionList(" 1=1 ");
	}

	/*
	 * 按照分组取业务操作列表
	 * @see uap.workflow.bizitf.bizaction.IBizActionHelper#getBizActionListByGroup(java.lang.String)
	 */
	@Override
	public Vector<BizAction> getBizActionListByGroup(String group) throws BizException
	{
		return getBizActionList(" t.systemcode='"+ group +"'");
	}

	/*
	 * 按照对象类型取业务操作列表
	 * @see uap.workflow.bizitf.bizaction.IBizActionHelper#getBizActionListByObjectType(java.lang.String)
	 */
	@Override
	public Vector<BizAction> getBizActionListByObjectType(String objectType) throws BizException
	{
		return getBizActionList(" t.pk_billtypecode='"+ objectType + "'");
	}

	/*
	 * 按照业务操作编码区业务操作
	 * @see uap.workflow.bizitf.bizaction.IBizActionHelper#getBizActionByAction(java.lang.String, java.lang.String)
	 */
	@Override
	public BizAction getBizActionByAction(String objectType, String action){
		String condition =" t.pk_billtypecode='"+ objectType + "' and a.a.PK_BILLACTION = '" + action + "'" ;
		return getBizActionList(condition).get(0);
	}
	
	@SuppressWarnings("unchecked")
	private Vector<BizAction> getBizActionList(String condition) throws BizException
	{
		//TODO 客户端缓存
		Vector<BizAction> bas = new Vector<BizAction>();
	
		String sql = "select t.SYSTEMCODE, t.PK_BILLTYPEID, t.PARENTBILLTYPE, t.PK_BILLTYPECODE, t.BILLTYPENAME, "+
			"a.ACTIONNOTE, a.PK_BILLACTION, a.PK_BILLACTION"+
			" from BD_BILLTYPE t inner join PUB_BILLACTION a on a.PK_BILLTYPE = t.PK_BILLTYPE ";
		sql += " where " + condition;
		sql += " order by SYSTEMCODE, PK_BILLTYPECODE";
		try{
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<Object> list = (List<Object>)query.executeQuery(sql, new ArrayListProcessor());
			BizAction bizAction = null;
			for (Iterator<Object> iter = list.iterator(); iter.hasNext();) {
				
				Object[] row = (Object[]) iter.next();
				
				bizAction = new BizAction(
						new BizObjectType(row[0].toString(),row[2].toString(),row[3].toString(),row[4].toString(),row[1].toString()), 
						new BizActionType(row[5].toString(),row[6].toString(),row[7].toString()));
				bas.add((BizAction)bizAction);
			}
		}catch(BusinessException e){
			throw new BizException(e);
		}
		return bas;
	}
	
	@Override
	public Boolean MatchActionToActivity(BizAction bizAction, Object activity) {
		// TODO Auto-generated method stub
		return null;
	}


}
