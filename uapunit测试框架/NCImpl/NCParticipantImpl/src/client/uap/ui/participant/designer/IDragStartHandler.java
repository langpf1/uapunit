package uap.ui.participant.designer;

/**
 * ��֯��������ק��ʼ �¼��Ĵ�����
 * <li>1.��Ҫ����BarFactory���ָ���ʼ״̬
 * <li>2.ʹ��BarFactory�������ק���ͣ������ݸ���ǰ�༭����GraphDropHandler
 * 
 * @author �׾� created on 2004-11-9
 */
public interface IDragStartHandler {
  /** �ƶ����͵���ק�����粻�����κμ�������֯��������ק */
  int DRAG_TYPE_MOVE = 0;
  /** �������͵���ק�����簴��Ctrl��������֯��������ק */
  int DRAG_TYPE_COPY = 1;
  /** �滻���͵���ק�����簴��Alt+Ctrl��������֯��������ק */
  int DRAG_TYPE_REPLACE = 2;

  public void dragStarting(int dragType);

}