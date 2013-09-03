package uap.workflow.test;

import java.io.Serializable;

/**
 * 
 * @author kongml
 * 
 */
public class CustomerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 522009239364956565L;
	// ������֯
	private String apply_org;
	// ���ݱ���
	private String billcode;
	//�������
	private int billno;
	// ��������
	private String billtype;
	// ������
	private String applicant;
	// ����ʱ��
	private String apply_time;
	// ����״̬
	private String apply_state;
	// ������֯
	private String org;
	// �ͻ�����
	private String code;
	// �ͻ�����
	private String name;
	// �ͻ����
	private String simple_code;
	// ������֯
	private String customer_grp;
	// ���ڵ���
	private String region_grp;
	// �ͻ�״̬
	private String customer_state;
	// �Ƿ�Ӧ��
	private boolean ispartner;
	// ����
	private String owner;
	// ע���ʽ�
	private int money;
	// �ͻ�˰��
	private String tax;
	// ��ҵ��ַ
	private String addr;

	// actioncode
	private String actioncode;

	public String getApply_org() {
		return apply_org;
	}

	public void setApply_org(String apply_org) {
		this.apply_org = apply_org;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getApply_time() {
		return apply_time;
	}

	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}

	public String getApply_state() {
		return apply_state;
	}

	public void setApply_state(String apply_state) {
		this.apply_state = apply_state;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSimple_code() {
		return simple_code;
	}

	public void setSimple_code(String simple_code) {
		this.simple_code = simple_code;
	}

	public String getCustomer_grp() {
		return customer_grp;
	}

	public void setCustomer_grp(String customer_grp) {
		this.customer_grp = customer_grp;
	}

	public String getRegion_grp() {
		return region_grp;
	}

	public void setRegion_grp(String region_grp) {
		this.region_grp = region_grp;
	}

	public String getCustomer_state() {
		return customer_state;
	}

	public void setCustomer_state(String customer_state) {
		this.customer_state = customer_state;
	}

	public boolean isIspartner() {
		return ispartner;
	}

	public void setIspartner(boolean ispartner) {
		this.ispartner = ispartner;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getActioncode() {
		return actioncode;
	}

	public void setActioncode(String actioncode) {
		this.actioncode = actioncode;
	}

	public CustomerVO toVO() {
		CustomerVO vo = new CustomerVO();
		vo.setAddr(this.getAddr());
		vo.setApplicant(this.getApplicant());
		vo.setApply_org(this.getApply_org());
		vo.setApply_state(this.getApply_state());
		vo.setApply_time(this.getApply_time());
		vo.setBillcode(this.getBillcode());
		vo.setBilltype(this.getBilltype());
		vo.setCode(this.getCode());
		vo.setCustomer_grp(this.getCustomer_grp());
		vo.setCustomer_state(this.getCustomer_state());
		vo.setIspartner(this.isIspartner());
		vo.setMoney(this.getMoney());
		vo.setName(this.getName());
		vo.setOrg(this.getOrg());
		vo.setOwner(this.getOwner());
		vo.setRegion_grp(vo.getRegion_grp());
		vo.setSimple_code(this.getSimple_code());
		vo.setTax(this.getTax());
		vo.setBillno(this.getBillno());
		return vo;
	}

	public String getBillcode() {
		return billcode;
	}

	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}

	public int getBillno() {
		return billno;
	}

	public void setBillno(int billno) {
		this.billno = billno;
	}

}
