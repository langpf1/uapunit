package uap.ui.participant.designer;

/**
 * 组织机构树拖拽开始 事件的处理器
 * <li>1.主要用于BarFactory来恢复初始状态
 * <li>2.使用BarFactory来标记拖拽类型，并传递给当前编辑器的GraphDropHandler
 * 
 * @author 雷军 created on 2004-11-9
 */
public interface IDragStartHandler {
  /** 移动类型的拖拽，比如不按下任何键进行组织机构的拖拽 */
  int DRAG_TYPE_MOVE = 0;
  /** 拷贝类型的拖拽，比如按下Ctrl键进行组织机构的拖拽 */
  int DRAG_TYPE_COPY = 1;
  /** 替换类型的拖拽，比如按下Alt+Ctrl键进行组织机构的拖拽 */
  int DRAG_TYPE_REPLACE = 2;

  public void dragStarting(int dragType);

}