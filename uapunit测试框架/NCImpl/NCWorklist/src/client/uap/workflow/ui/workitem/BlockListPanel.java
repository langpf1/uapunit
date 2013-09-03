package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.layout.SingleLineLayout;

/**
 * 区块列表面板类
 * @author zhangrui
 *
 */
public class BlockListPanel extends UIPanel {
	
	private static final long serialVersionUID = -5762158531148444302L;
	
	private String title;
	private UILabel lblTitle;
	private UIPanel pnlContent;
	private List<Object> valueList = new ArrayList<Object>();
	private ActionListener removePaneListener;
	
	public BlockListPanel(String title) {
		super();
		
		this.title = title;
		
		initialize();
	}
	
	public void setRemovePaneListener(ActionListener removePaneListener) {
		this.removePaneListener = removePaneListener;
	}
	
	protected void initialize() {
		setLayout(new BorderLayout());
		
		add(getLblTitle(), BorderLayout.WEST);
		add(getPnlContent(), BorderLayout.CENTER);
		
		updateStatus();
	}
	
	private void updateStatus() {
		if(valueList.size() <= 0) {
			getLblTitle().setVisible(false);
		} else { 
			getLblTitle().setVisible(true);
		}
	}
	
	public void addNewBlock(String name, Object value) {
		valueList.add(value);
		BlockPane blockPane = new BlockPane(name, value);
		getPnlContent().add(blockPane, (Object)FlowLayout.LEFT);
		blockPane.setRemoveActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockPaneEvent event = (BlockPaneEvent) e;
				valueList.remove(event.getValue());

				removeBlockPane(event);
				
				updateStatus();
				
				updateUI();
			}
		});
		
		updateStatus();
	}

	private void removeBlockPane(BlockPaneEvent event) {
		BlockPane sourcePane = (BlockPane) event.getSource();
		Container blockPaneParent = sourcePane.getParent();
		blockPaneParent.remove(sourcePane);
		blockPaneParent.repaint();
		
		if(removePaneListener != null) {
			removePaneListener.actionPerformed(event);
		}
	}
	
	public void clearBlocks() {
		valueList.clear();
		getPnlContent().removeAll();
	}
	
	private UIPanel getPnlContent() {
		if(pnlContent == null) {
			pnlContent = new UIPanel(new SingleLineLayout(3));
		}
		return pnlContent;
	}
	
	private UILabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new UILabel(title + "：");
		}
		return lblTitle;
	}
}
