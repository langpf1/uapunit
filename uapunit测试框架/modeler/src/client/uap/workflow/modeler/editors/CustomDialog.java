
package uap.workflow.modeler.editors;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.graph.IGraphPersist;
import nc.ui.ml.NCLangRes;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
class CustomDialog extends JDialog implements ActionListener,PropertyChangeListener {
   
	private String code = null;
	private String name =null;
    private JTextField buzicode;
    private JTextField buziname;
    private JOptionPane optionPane;
    
    private String btnString1 =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000026")/*确定*/;
    private String btnString2 =NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000027")/*取消*/;
  
    private boolean isOK=true;

    public String getCode() {
        return code;
    }
    
    public String getName(){
    	return name;
    }

    public boolean isOK() {
		return isOK;
	}


	public void setOK(boolean isOK) {
		this.isOK = isOK;
	}


	/** Creates the reusable dialog. */
    public CustomDialog(Dialog aFrame,String code,String name) {
        super(aFrame, true);        
        setTitle(NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000028")/*输入业务编码和名称*/);
        buzicode = new JTextField(10);
        buziname =new JTextField(10);
        buzicode.setText(StringUtil.isEmptyWithTrim(code)?"":code);
        buziname.setText(StringUtil.isEmptyWithTrim(name)?"":name);
        
        setSize(new Dimension(270,200));
        setLocation(new Point(400,300));

        //Create an array of the text and components to be displayed.
        String msgString1 = NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000029")/*请输入业务编码  */;
        String msgString2 = NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000030")/*请输入业务名称  */;
        Object[] array = {msgString1,buzicode,msgString2,buziname};


        //Create an array specifying the number of dialog buttons
        //and their text.

        Object[] options = {btnString1, btnString2};


        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);


        //Make this dialog display it.
        setContentPane(optionPane);


        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
				optionPane.setValue(Integer.valueOf(JOptionPane.CLOSED_OPTION));
            }
        });


        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                buzicode.requestFocusInWindow();
            }
        });


        //Register an event handler that puts the text into the option pane.
        buzicode.addActionListener(this);


        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }


    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }


    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();


        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();


            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);


            if (btnString1.equals(value)) {
                    code = buzicode.getText();
                    name =buziname.getText();
                    if(!StringUtil.isEmptyWithTrim(code)&&!StringUtil.isEmptyWithTrim(name)) {
                    //we're done; clear and dismiss the dialog
                    clearAndHide();
                } else {
                    //text was invalid
                    buzicode.selectAll();
                    JOptionPane.showMessageDialog(
                                    CustomDialog.this,
                                    NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000031")/*业务流名称和编码不能为空！*/,
                                    NCLangRes.getInstance().getStrByID("pfgraph", "graphhint-000032")/*再次输入*/,
                                    JOptionPane.ERROR_MESSAGE);
                    code = null;
                    buzicode.requestFocusInWindow();
                }
                isOK =true;
            } else { //user closed dialog or clicked cancel

                code = null;
                clearAndHide();
                isOK=false;
            }
        }
    }
 

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
        buzicode.setText(null);
        setVisible(false);
    }
}

