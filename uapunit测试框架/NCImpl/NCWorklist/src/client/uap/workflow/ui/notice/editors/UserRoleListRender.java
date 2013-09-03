package uap.workflow.ui.notice.editors;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import nc.vo.pub.pf.plugin.MsgReceiverTypes;
import nc.vo.pub.pf.plugin.ReceiverVO;

/**
 * 用户角色列表渲染器
 * 
 * @author leijun 2006-6-19
 */
public class UserRoleListRender extends DefaultListCellRenderer {
	// 角色图标
	ImageIcon iconRole = new ImageIcon(getClass().getResource("/images/treeImages/role.gif"));

	// 用户图标
	ImageIcon iconUser = new ImageIcon(getClass().getResource("/images/treeImages/user.gif"));

	public Component getListCellRendererComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value instanceof ReceiverVO) {
			ReceiverVO rvo = (ReceiverVO) value;
			if (rvo.getType() == MsgReceiverTypes.REVEIVER_TYPE_ROLE) {
				this.setIcon(iconRole);
			} else if (rvo.getType() == MsgReceiverTypes.REVEIVER_TYPE_USER) {
				this.setIcon(iconUser);
			}
		}
		return this;
	}

}
