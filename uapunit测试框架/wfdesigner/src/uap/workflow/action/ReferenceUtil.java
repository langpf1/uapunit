package uap.workflow.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import uap.workflow.parameter.IParameter;

public class ReferenceUtil implements IAction {

	private int pageNum = 0;
	private int pageSize = 10;
	private String refType = "";
	@SuppressWarnings("restriction")
	@Override
	public void perform(IParameter parameter) {
		pageNum = Integer.parseInt(parameter.getRequest().getParameter("pageNum").toString());
		pageSize = Integer.parseInt(parameter.getRequest().getParameter("pageSize").toString());
		refType = parameter.getRequest().getParameter("refType").toString();
		
		StringBuffer json = new StringBuffer();
		queryData(json, refType, pageNum, pageSize);
		
		HttpServletResponse response = parameter.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		try {
			ServletOutputStream stream = response.getOutputStream();
			String out = json.toString();
			stream.write(out.getBytes("UTF-8"));
			stream.flush();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void queryData(StringBuffer json, String refType, int pageNumber, int pageSize){
		
		BaseDAO dao = new BaseDAO();//NCLocator.getInstance().lookup(BaseDAO.class);
		try {
			Collection result = dao.retrieveByClause(
				Class.forName(refType), "", "code", new String[]{"pk_vid","code","name"});
			int rowsCount = result.size();
			StringBuffer buffer = toJson(result);
			json.append("{total:" + rowsCount + ",rows:[" + buffer.toString() + "]}");
		}catch(Exception e){
			json.append("{error:true, message=" + e.getMessage() + "}");
		}
	}
	
	private StringBuffer toJson(Collection result){
		StringBuffer buffer = new StringBuffer();
		Iterator iterator  = result.iterator();
		while(iterator.hasNext()){
			if (buffer.length() > 0)
				buffer.append(",\n");
			buffer.append("{id:").append("'id.toString()'").append(",code:").append("'code.toString()'").append(",name:").append("'name.toString()'").append("}");
			iterator.next();  
		}
		return buffer;
	}
	
	class ReferenceType{
		
		private Hashtable<String, String> refType = new Hashtable<String, String>();
		
		public ReferenceType(){
			refType.put("nc.vo.org.GroupVO", "id-name-code");
			refType.put("nc.vo.sm.UserVO", "id-name-code");
			refType.put("nc.vo.vorg.FinanceOrgVersionVO", "id-name-code");
			refType.put("nc.vo.pub.billtype.BilltypeVO", "id-name-code");
			refType.put("nc.vo.org.DeptVO'", "id-name-code");
			refType.put("nc.vo.org.SalesOrgVO", "id-name-code");
			refType.put("nc.vo.bd.address.AddressVO", "id-name-code");
			refType.put("nc.vo.bd.psn.PsndocVO", "id-name-code");
			refType.put("nc.vo.bd.cust.CustomerVO", "id-name-code");
			refType.put("nc.vo.bd.currtype.CurrtypeVO", "id-name-code");
			refType.put("nc.vo.bd.bankaccount.BankAccSubVO", "id-name-code");
			refType.put("nc.vo.arap.receivable.ReceivableBillItemVO", "id-name-code");
			refType.put("nc.vo.resa.costcenter.CostCenterVO", "id-name-code");
			refType.put("nc.vo.bd.countryzone.CountryZoneVO", "id-name-code");
			refType.put("nc.vo.bd.cust.CustomerVO", "id-name-code");
			refType.put("nc.vo.bd.cashflow.CashflowVO", "id-name-code");
			
			}
		
		public String getReferenceType(String type){
			return refType.get(type);
		}
	}
	
}
