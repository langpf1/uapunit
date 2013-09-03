package uap.workflow.ui.taskhandling;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.ExtTabbedPane;
import nc.ui.pub.beans.UILabel;

/**
 * 主界面上的未读消息条数显示
 * 
 * @author leijun 2009-7
 * @since 6.0
 */
public class MessageCountLabel extends UILabel implements MouseListener {

	public static MessageCountLabel instance = new MessageCountLabel();

	public final ImageIcon iconFlag = new ImageIcon(getClass().getResource("/images/PF/flag_red.gif"));

	public static MessageCountLabel getInstance() {
		return instance;
	}

	private MessageCountLabel() {
		super();

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		addMouseListener(this);
	}

	public void setCount(int iCount) {
		String painText = getPlainText(iCount);
		//
		setIcon(iconFlag);
		setText(painText);
		setForeground(Color.BLUE);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		Dimension d = getPreferredSize();
		d.width = getFontMetrics(getFont()).stringWidth(painText) + 20;
		setPreferredSize(d);
		validate();
	}

	private String getPlainText(int iCount) {
		return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000067", null,
				new String[] { String.valueOf(iCount) })/*您有{0}条未读消息！*/;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		doMouseClicked();
	}

	private void doMouseClicked() {
		//选中首页
		ExtTabbedPane tabPane = WorkbenchEnvironment.getInstance().getWorkbench()
				.getWorkSpaceTabbedPane();
		Component comp = null;
		for (int i = 0; i < tabPane.getTabCount(); i++) {
			comp = tabPane.getComponentAt(i);
			if (comp instanceof MessagePanel) {
				break;
			}
		}
		if (comp != null)
			tabPane.setSelectedComponent(comp);
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
