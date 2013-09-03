package uap.workflow.modeler.sheet.editor;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import nc.bs.logging.Logger;
import nc.ui.wfengine.sheet.swing.LookAndFeelTweaks;
import nc.ui.wfengine.sheet.swing.UserPreferences;

/**
 * FilePropertyEditor. <br>
 *
 */
public class FilePropertyEditor extends AbstractPropertyEditor {

	protected JTextField textfield;

	private JButton button;

	public FilePropertyEditor() {
		editor = new JPanel(new BorderLayout(0, 0));
		((JPanel) editor).add("Center", textfield = new JTextField());
		((JPanel) editor).add("East", button = new JButton(". . ."));
		button.setMargin(new Insets(0, 0, 0, 0));
		textfield.setBorder(LookAndFeelTweaks.EMPTY_BORDER);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectFile();
			}
		});

		textfield.setTransferHandler(new FileTransferHandler());
	}

	class FileTransferHandler extends TransferHandler {
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			for (int i = 0, c = transferFlavors.length; i < c; i++) {
				if (transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) { return true; }
			}
			return false;
		}

		public boolean importData(JComponent comp, Transferable t) {
			try {
				List list = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
				if (list.size() > 0) {
					File oldFile = (File) getValue();
					File newFile = (File) list.get(0);
					String text = newFile.getAbsolutePath();
					textfield.setText(text);
					firePropertyChange(oldFile, newFile);
				}
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
			return true;
		}
	}

	public Object getValue() {
		if ("".equals(textfield.getText().trim())) {
			return null;
		} else {
			return new File(textfield.getText());
		}
	}

	public void setValue(Object value) {
		if (value instanceof File) {
			textfield.setText(((File) value).getAbsolutePath());
		} else {
			textfield.setText("");
		}
	}

	protected void selectFile() {
		JFileChooser chooser = UserPreferences.getDefaultFileChooser();
		chooser.setDialogTitle(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("101203", "UPP101203-000117")/*@res "文件选择"*/);
		chooser.setApproveButtonText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UC000-0004044")/*@res "选择"*/);

		if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(editor)) {
			File oldFile = (File) getValue();
			File newFile = chooser.getSelectedFile();
			String text = newFile.getAbsolutePath();
			textfield.setText(text);
			firePropertyChange(oldFile, newFile);
		}
	}

}