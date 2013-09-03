package uap.workflow.app.participant;
import java.util.ArrayList;
import java.util.List;
/**
 * ����������ʱ���޶�ģʽ������
 * 
 * @author
 */
public class ParticipantFilterContext {
	IParticipant participant;
	// /������֯PK��һ��Ϊ���Ż�˾PK
	String belongOrg;
	// / ����������������û�PK����
	@SuppressWarnings("rawtypes")
	ArrayList userIdsOfParticipant;
	// /����ʵ�壬һ��Ϊ���ݾۺ�VO
	Object billEntity;
	// / ������PK������ǰ������
	String senderman;
	// / �Ƶ���PK
	String billmaker;
	// / �Ƿ�Ϊָ��ʱ�Ĺ���
	boolean isForDispatch;
	// /�Ѿ����ڵ��û��б�
	List<String> userList;
	// /ְ��Ҫ�õ�����֯�޶�
	ArrayList<String> orgs4Responsibility;
	public void setParticipant(IParticipant participant) {
		this.participant = participant;
	}
	public IParticipant getParticipant() {
		return this.participant;
	}
	public ArrayList<String> getFilterOrgs4Responsibility() {
		return orgs4Responsibility;
	}
	public void setFilterOrgs4Responsibility(ArrayList<String> orgs4Responsibility) {
		this.orgs4Responsibility = orgs4Responsibility;
	}
	public String getBelongOrg() {
		return belongOrg;
	}
	public void setBelongOrg(String belongOrg) {
		this.belongOrg = belongOrg;
	}
	public boolean isForDispatch() {
		return isForDispatch;
	}
	public void setForDispatch(boolean isForDispatch) {
		this.isForDispatch = isForDispatch;
	}
	@SuppressWarnings("rawtypes")
	public ArrayList getUserIdsOfParticipant() {
		return userIdsOfParticipant;
	}
	public void setUserIdsOfParticipant(@SuppressWarnings("rawtypes") ArrayList userIdsOfParticipant) {
		this.userIdsOfParticipant = userIdsOfParticipant;
	}
	public Object getBillEntity() {
		return billEntity;
	}
	public void setBillEntity(Object billEntity) {
		this.billEntity = billEntity;
	}
	public String getSenderman() {
		return senderman;
	}
	public void setSenderman(String senderman) {
		this.senderman = senderman;
	}
	public String getBillmaker() {
		return billmaker;
	}
	public void setBillmaker(String billmaker) {
		this.billmaker = billmaker;
	}
	public List<String> getUserList() {
		return userList;
	}
	public void setUserList(List<String> list) {
		this.userList = list;
	}
	private ArrayList<String> filterSource = null;
	public void setFilterSource(ArrayList<String> _filterSource) {
		filterSource = _filterSource;
	}
	public ArrayList<String> getFilterSource() {
		return filterSource;
	}
}