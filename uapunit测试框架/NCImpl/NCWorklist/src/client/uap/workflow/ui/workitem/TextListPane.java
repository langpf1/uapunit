package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import nc.ui.plaf.basic.UIRefButtonUI;
import nc.ui.pub.beans.RefPaneIconFactory;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPaneBorder;
import nc.ui.pub.beans.UITextField;
import nc.uitheme.ui.ThemeResourceCenter;

/**
 * 文本列表类
 * @author zhangrui
 *
 */
public class TextListPane extends UIPanel {
	private static final long serialVersionUID = 4971916414399918780L;
	
	private UITextField txtTextList = null;
	private UIButton button = null;
	private ActionListener actionListener;
	public List<TextItem> itemList = new ArrayList<TextItem>();
	
	public List<TextItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<TextItem> itemList) {
		this.itemList = itemList;
	}

	public class TextItem {
		private String text;
		private Object value;
		
		public TextItem(String text, Object value) {
			this.text = text;
			this.value = value;
		}
		
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	public TextListPane() {
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		
		add(getTxtTextList(), (Object) BorderLayout.CENTER);
		add(getButton(), (Object) BorderLayout.EAST);
		
		setBorder(new UIRefPaneBorder());
		
		attachEventListener();
	}
	
	public void setText(String txt) {
		getTxtTextList().setText(txt);
	}
	
	private void attachEventListener() {
		getButton().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(actionListener != null) {
					actionListener.actionPerformed(e);
				}
			}
		});
	}
	
	public void clearTexts() {
		itemList.clear();
		
		setText();
	}
	
	public void addTextItem(String text, Object value) {
		TextItem item = new TextItem(text, value);
		itemList.add(item);
		
		setText();
	}
	
	private void setText() {
		getTxtTextList().setText("");
		StringBuffer sbText = new StringBuffer();
		for(int i=0; i < itemList.size(); i++) {
			sbText.append(itemList.get(i).getText());
			if(i < itemList.size() - 1) {
				sbText.append(", ");
			}
		}
		getTxtTextList().setText(sbText.toString());
	}
	
	private UIButton getButton() {
		if (button == null) {
			button = new UIButton();
			button.setName("UIButton");
			button.setUI(new UIRefButtonUI());
			Icon icon = getRefIcon(RefPaneIconFactory.REFENABLE);
			button.setIcon(icon);
			button.setDisabledIcon(icon);
			button.setRolloverIcon(getRefIcon(RefPaneIconFactory.REFMOUSEOVER));
			button.setPressedIcon(getRefIcon(RefPaneIconFactory.REFPRESSED));

			button.setPreferredSize(new java.awt.Dimension(20, icon
							.getIconHeight()));
		}
		return button;
	}
	
	private Icon getRefIcon(String state) {
		ImageIcon icon = ThemeResourceCenter.getInstance().getImage(
				"themeres/control/input/input_search.png");

		if (RefPaneIconFactory.REFPRESSED.equals(state)
				|| RefPaneIconFactory.REFMOUSEOVER.equals(state)) {
			icon = ThemeResourceCenter.getInstance().getImage(
					"themeres/control/input/input_search_highlight.png");
		}
		return icon;
	}
	
	private UITextField getTxtTextList() {
		if (txtTextList == null) {
			txtTextList = new UITextField();
			txtTextList.setEditable(false);
			txtTextList.setBorder(null);
			txtTextList.setPreferredSize(new Dimension(300, txtTextList.getHeight()));
		}
		return txtTextList;
	}

	public void setActionListener(ActionListener listener) {
		actionListener = listener;
	}
}
