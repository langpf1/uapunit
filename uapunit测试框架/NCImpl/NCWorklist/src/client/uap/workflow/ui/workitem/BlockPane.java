package uap.workflow.ui.workitem;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.uitheme.ui.ThemeResourceCenter;

/**
 * Çø¿éÃæ°å
 * @author zhangrui
 *
 */
public class BlockPane extends UIPanel {
	
	private static final long serialVersionUID = 1765802424089334417L;
	
	private UILabel lblName;
	private JButton btnRemove;
	private String name;
	private Object value;
	private ActionListener removeActionListener;
	
	public BlockPane(String name, Object value) {
		this.name = name;
		this.value = value;
		
		initialize();
	}
	
	protected void initialize() {
		setLayout(new FlowLayout());
		
		add(getBtnRemove(), FlowLayout.LEFT);
		add(getLblName(), FlowLayout.LEFT);
		
		attachEventListener();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	private void attachEventListener() {
		final Object source = this;
		getBtnRemove().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeActionListener.actionPerformed(new BlockPaneEvent(e, source, getLblName().getText(), value));
			}
		});
	}
	
	public void setRemoveActionListener(ActionListener listener) {
		removeActionListener = listener;
	}
	
	private UILabel getLblName() {
		if (lblName == null) {
			lblName = new UILabel(name);
			lblName.setForeground(Color.BLUE);
		}
		return lblName;
	}
	
	private JButton getBtnRemove() {
		if (btnRemove == null) {
			ImageIcon icon = ThemeResourceCenter.getInstance().getImage(
					"themeres/control/approve/close.png");
			btnRemove = new UIButton(icon);
			btnRemove.setCursor(Cursor
					.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnRemove.setRolloverEnabled(true);
			btnRemove.setBorder(BorderFactory.createEmptyBorder());
			Dimension size = new Dimension(15, 15);
			btnRemove.setSize(size);
			btnRemove.setPreferredSize(size);
			btnRemove.setContentAreaFilled(false);
//			btnRemove.repaint();
		}
		return btnRemove;
	}
}
