package uap.workflow.app.notice;

import java.util.List;
import java.util.Map;

/**
 * ֪ͨ�ӿ� 
 * ֪ͨ�����ʱ�Ľӿ�
 * @author 
 */

public interface INoticeDefinition{
	  //֪ͨ�Ľ�����
	  public List<ReceiverVO> getReceivers();
	  public void setReceivers(List<ReceiverVO> receivers);
	  
	  //֪ͨ������ģ��
	  public String getContentTemplate();
	  public void setContentTemplate(String contentTemplate);

	  //֪ͨ������
	  public INoticeType getNoticeType();
	  public void setNoticeType(INoticeType noticeType);
	  
	  //֪ͨ��ʱ��
	  public NoticeTimeTypeEnum getNoticeTime();
	  public void setNoticeTime(NoticeTimeTypeEnum noticeTime);

	  //֪ͨ�ķ�������
	  public String getCondition();
	  public void setCondition(String condition);

	  //���͵�֪ͨ�Ƿ���Ҫ��ִ
	  public boolean getHasReceipt();
	  public void setHasReceipt(boolean hasReceipt);

	  //ȡ֪ͨ��������չ����
	  public Map<String,Object> getProperties();
	
	  //ȡ֪ͨ����չ����
	  public Object getProperty(String key);
	
	  ///����֪ͨ����չ����
	  public void setProperty(String key, Object value);
}