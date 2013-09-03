package uap.workflow.app.participant;
import java.util.ArrayList;
import java.util.List;
/**
 * 参与者运行时的限定模式上下文
 * 
 * @author
 */
public class ParticipantFilterContext {
	IParticipant participant;
	// /所属组织PK，一般为集团或公司PK
	String belongOrg;
	// / 活动参与者所包含的用户PK数组
	@SuppressWarnings("rawtypes")
	ArrayList userIdsOfParticipant;
	// /单据实体，一般为单据聚合VO
	Object billEntity;
	// / 发送人PK，即当前审批人
	String senderman;
	// / 制单人PK
	String billmaker;
	// / 是否为指派时的过滤
	boolean isForDispatch;
	// /已经存在的用户列表
	List<String> userList;
	// /职责要用到的组织限定
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