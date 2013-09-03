package uap.workflow.reslet.application.receiveData;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
/*
 *  @ author zhai  2013.4.3
 * */
public class WfWebTask {
	/**�����id*/
	private String taskID ;
	
	/*���������*/
	private String pk_task;
	
	/*�ʵ��pk*/
	private String pk_activity_instance;
	/*��ǰ���id*/
	private String activity_id;
	/*
	 * ���name
	 * */
	private String activity_name;
	/**
	 * ���̵�����
	 * */
	private String process_def_name;
	/**
	 * ���񴴽���
	 */
	private String pk_creater;
	/**
	 * ����������
	 */
	private String pk_owner;
	/**
	 * ����ʵ��ִ����
	 */
	private String pk_executer;
	/**
	 * ���������
	 */
	private String pk_agenter;
	/**
	 * ������ص�ǩ֤��������
	 */
	private String pk_myvisa;
	/*
	 * �Ƿ�ִ��
	 * */
	private UFBoolean isexec;
	/*
	 * �Ƿ�ͨ��
	 * */
	private UFBoolean ispass;
	/*
	 * ���״̬
	 * */
	private int state_task;
	/**
	 * ǰ��ǩ����
	 */
	private String beforeaddsign_times;
	/**
	 * ������������������ļ������ļ����Ѱ�����˻ؼ�������TaskInstanceStatusö��
	 */
	private int taskhandlState;
	/**
	 * ������
	 */
	private String finish;
	/**
	 * �������
	 */
	private int finish_type;
	/**
	 * ��������
	 */
	private int create_type;
	/**
	 * ���̵�����ʱ��
	 */
	private UFDateTime startdate;
	/**
	 * ���񴴽�ʱ��
	 */
	private String begindate;//
	/**
	 * ����򿪵�ʱ��
	 */
	private String signdate;//
	/**
	 * �������ʱ��
	 */
	private String finishdate;//
	/**
	 * ����Ԥ�����ʱ��
	 */
	private String dutedate;//
	/**
	 * ����Ӵ�������ɵĳ���ʱ��
	 */
	private String standingdate;//
	/*
	 * �������������������
	 * */
	private String opinion;//
	/**
	 * ��ֽ�������Ļ������ǲ�����ʽ�����
	 */
	private String scratch;
	
	/*
	 * ��֯
	 * */
	private String pk_group;
	private String pk_org;
	/**
	 * ������NC�ĵ��ݺ�
	 */
	private String form_no;//
	/**
	 * ������NC�ĵ�������
	 */
	private String pk_bizobject;//
    private String pk_form_ins_version;
	
	/**������Ա�������ѡ�� ��[2]���أ�[3]��ǩ��[4]���ɣ�[5]ָ��
	 * Ĭ�ϵ��������ȫ������ѡ���
	 *  ���� 
	 *  REJECT(2),
	 *  ��ǩ 
	 *  ADDSIGN(3),
	 *  ���� *
	 *  DESIGNATE(4),
	 *  ָ��
	 *  REASSIGNMENT(5);
	 * */
	private List<Integer> operators = new ArrayList<Integer>();
	////////////////////////////////////
	/**���Բ��صĻ���*/
	private List<RejectNode> rejectNodes = new ArrayList<RejectNode>();
	public String getPk_task() {
		return pk_task;
	}
	public void setPk_task(String pk_task) {
		this.pk_task = pk_task;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getPk_creater() {
		return pk_creater;
	}
	public void setPk_creater(String pk_creater) {
		this.pk_creater = pk_creater;
	}
	public String getPk_owner() {
		return pk_owner;
	}
	public void setPk_owner(String pk_owner) {
		this.pk_owner = pk_owner;
	}
	public String getPk_executer() {
		return pk_executer;
	}
	public void setPk_executer(String pk_executer) {
		this.pk_executer = pk_executer;
	}
	public String getPk_agenter() {
		return pk_agenter;
	}
	public void setPk_agenter(String pk_agenter) {
		this.pk_agenter = pk_agenter;
	}
	public String getPk_myvisa() {
		return pk_myvisa;
	}
	public void setPk_myvisa(String pk_myvisa) {
		this.pk_myvisa = pk_myvisa;
	}
	public UFBoolean getIsexec() {
		return isexec;
	}
	public void setIsexec(UFBoolean isexec) {
		this.isexec = isexec;
	}
	public UFBoolean getIspass() {
		return ispass;
	}
	public void setIspass(UFBoolean ispass) {
		this.ispass = ispass;
	}
	public int getState_task() {
		return state_task;
	}
	public void setState_task(int state_task) {
		this.state_task = state_task;
	}
	public String getBeforeaddsign_times() {
		return beforeaddsign_times;
	}
	public void setBeforeaddsign_times(String beforeaddsign_times) {
		this.beforeaddsign_times = beforeaddsign_times;
	}
	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	public int getFinish_type() {
		return finish_type;
	}
	public void setFinish_type(int finish_type) {
		this.finish_type = finish_type;
	}
	public int getCreate_type() {
		return create_type;
	}
	public void setCreate_type(int create_type) {
		this.create_type = create_type;
	}
	public UFDateTime getStartdate() {
		return startdate;
	}
	public void setStartdate(UFDateTime startdate) {
		this.startdate = startdate;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String string) {
		this.begindate = string;
	}
	public String getSigndate() {
		return signdate;
	}
	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
	public String getFinishdate() {
		return finishdate;
	}
	public void setFinishdate(String finishdate) {
		this.finishdate = finishdate;
	}
	public String getDutedate() {
		return dutedate;
	}
	public void setDutedate(String dutedate) {
		this.dutedate = dutedate;
	}
	public String getStandingdate() {
		return standingdate;
	}
	public void setStandingdate(String standingdate) {
		this.standingdate = standingdate;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getScratch() {
		return scratch;
	}
	public void setScratch(String scratch) {
		this.scratch = scratch;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getForm_no() {
		return form_no;
	}
	public void setForm_no(String form_no) {
		this.form_no = form_no;
	}
	public String getPk_bizobject() {
		return pk_bizobject;
	}
	public void setPk_bizobject(String pk_bizobject) {
		this.pk_bizobject = pk_bizobject;
	}
	public int getTaskhandlState() {
		return taskhandlState;
	}
	public void setTaskhandlState(int taskhandlState) {
		this.taskhandlState = taskhandlState;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setOperators(List<Integer> operators) {
		this.operators = operators;
	}
	public List<Integer> getOperators() {
		return operators;
	}
	public void setRejectNodes(List<RejectNode> rejectNodes) {
		this.rejectNodes = rejectNodes;
	}
	public List<RejectNode> getRejectNodes() {
		return rejectNodes;
	}
	public String getPk_activity_instance(){
		return pk_activity_instance;
	}
	public void setPk_activity_instance(String pk_activity_instance) {
		this.pk_activity_instance = pk_activity_instance;
		
	}
	public void setProcess_def_name(String process_def_name) {
		this.process_def_name = process_def_name;
	}
	public String getProcess_def_name() {
		return process_def_name;
	}
	public void setPk_form_ins_version(String pk_form_ins_version) {
		this.pk_form_ins_version = pk_form_ins_version;
	}
	public String getPk_form_ins_version() {
		return pk_form_ins_version;
	}
}
