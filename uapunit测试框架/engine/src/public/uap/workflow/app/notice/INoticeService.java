package uap.workflow.app.notice;

/** 
   ֪ͨ����ʱ�Ķ������ӿڣ�����ҵ��Ӵ˽ӿ�ʵ���Լ��ľ��������
   ��Ҫ�������ļ�������INoticeService�ľ���ʵ�������ĸ�   
 * @author 
 */

public interface INoticeService{

	/** 
	    ֪ͨ������Ϣ
	 */
	public void sendNotice(NoticeContext noticeContext);
}