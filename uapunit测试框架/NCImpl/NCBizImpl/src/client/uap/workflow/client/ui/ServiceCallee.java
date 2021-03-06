package uap.workflow.client.ui;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.bpmn2.model.EventListener;
import uap.workflow.bpmn2.model.FieldExtension;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
//import org.jdesktop.application.Action;
import nc.ui.pub.beans.UILabel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ServiceCallee.java
 *
 * Created on 2012-6-25, 18:32:08
 */
/**
 *
 * @author wcj
 */
public class ServiceCallee extends UIDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ParameterTableModel tableModel = null;

    private EventListener vo = null;
    
    private boolean isOK = false;
    
    /** Creates new form ServiceCallee */
    public ServiceCallee(UIDialog parent) {
    	super(parent);
        initComponents();
        tableParameters.setModel(tableModel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        comboBoxSerivceType = new UIComboBox();
        textBoxServiceClass = new UITextField();
        btnServiceReference = new UIButton();
        jLabel1 = new UILabel();
        jLabel2 = new UILabel();
        jLabel3 = new UILabel();
        comboBoxMethod = new UIComboBox();
        jLabel4 = new UILabel();
        btnOK = new UIButton();
        btnCancel = new UIButton();
        jScrollPane1 = new UIScrollPane();
        tableParameters = new UITable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(desktopapplication1.DesktopApplication1.class).getContext().getResourceMap(ServiceCallee.class);
        setTitle("ServiceFunction"); // NOI18N
        setName("ServiceFunction"); // NOI18N

        comboBoxSerivceType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "flowWidget", "service", "action" }));
        comboBoxSerivceType.setSelectedIndex(1);
        comboBoxSerivceType.setName("comboBoxSerivceType"); // NOI18N

        textBoxServiceClass.setText(""); // NOI18N
        textBoxServiceClass.setName("textBoxServiceClass"); // NOI18N

        btnServiceReference.setText("..."); // NOI18N
        btnServiceReference.setName("btnServiceReference"); // NOI18N
        btnServiceReference.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceReferenceActionPerformed(evt);
            }
        });

        jLabel1.setText("服务类型"); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("服务函数类"); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("调用方法"); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        comboBoxMethod.setName("comboBoxMethod"); // NOI18N
        comboBoxMethod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MethodSelectedChanged(evt);
            }
        });

        jLabel4.setText("参数"); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        btnOK.setText("OK"); // NOI18N
        btnOK.setName("btnOK"); // NOI18N
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel"); // NOI18N
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableParameters.setModel(getTableModel());
        tableParameters.setName("tableParameters"); // NOI18N
        jScrollPane1.setViewportView(tableParameters);
        tableParameters.getColumnModel().getColumn(0).setResizable(false);
        tableParameters.getColumnModel().getColumn(0).setHeaderValue("类型"); // NOI18N
        tableParameters.getColumnModel().getColumn(1).setResizable(false);
        tableParameters.getColumnModel().getColumn(1).setHeaderValue("参数名"); // NOI18N
        tableParameters.getColumnModel().getColumn(2).setHeaderValue("值/表达式"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(comboBoxSerivceType, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxMethod, 0, 513, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textBoxServiceClass, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnServiceReference))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel)
                        .addGap(11, 11, 11)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboBoxSerivceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textBoxServiceClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnServiceReference))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboBoxMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel)
                    .addComponent(btnOK))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private ParameterTableModel getTableModel(){
        if (tableModel == null)
            tableModel = new ParameterTableModel();
        return tableModel;
    }

    public boolean getReferenceStatus(){
    	return isOK;
    }
    public EventListener getService(){
    	return vo;
    }

    public void setService(EventListener vo){
    	this.vo = vo;
        
        this.comboBoxSerivceType.setSelectedItem(vo.getImplementationType());
        textBoxServiceClass.setText(vo.getImplementation());
        EnumMethod(vo.getImplementation());
       // this.comboBoxMethod.setSelectedItem(vo.getMethod());
       // List<FieldExtension> parameters = vo.getFieldExtensions();
       // tableModel.setParamValues(parameters);
    }

    private void MethodSelectedChanged(java.awt.event.ItemEvent evt) {

        Method item = (Method) evt.getItem();
        EnumParameters(item);
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        vo = null;
        isOK = false;
        this.dispose();
    }
    
    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {
        isOK = true;
        vo = new EventListener();
        vo.setImplementationType(this.comboBoxSerivceType.getSelectedItem().toString());
        vo.setImplementation(textBoxServiceClass.getText());
       // vo.setMethod(this.comboBoxMethod.getSelectedItem().toString());
        //vo.setFieldExtensions(tableModel.getParamValues());

        this.dispose();
}


    private Method[] EnumMethod(String classPath) {
        Class<?> _class = null;
        try {
            classPath = classPath.replaceAll(".class", "");
            _class = Class.forName(classPath, true, Thread.currentThread().getContextClassLoader());

            Method[] methods = _class.getClass().getMethods();

            comboBoxMethod.removeAllItems();
            for (int i = 0; i < methods.length; i++) {
                comboBoxMethod.addItem(methods[i]);
            }
            return methods;

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private Map<String, String> EnumParameters(Method method) {

        tableModel.setMethod(method);

        tableParameters.updateUI();

        Map<String, String> methods = new HashMap<String, String>();

        return methods;
    }

    private void flowGedgetReference(){

    }

    private void serviceFunctioinReference(){
        JarServiceClass service = new JarServiceClass(this);
        service.show();
        String serviceClass = service.getServiceClass();
        textBoxServiceClass.setText(serviceClass);
        EnumMethod(serviceClass);
    }

    private void ActionServiceReference(){

    }

    private void btnServiceReferenceActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = comboBoxSerivceType.getSelectedIndex();
        switch (selectedIndex) {
            case 0:     //"流程组件"
                flowGedgetReference();
                break;
            case 1:     //"服务函数"
                serviceFunctioinReference();
                break;
            case 2:     //"动作服务"
                ActionServiceReference();
                break;
        }
    }

    // Variables declaration - do not modify
    private UIButton btnCancel;
    private UIButton btnOK;
    private UIButton btnServiceReference;
    private UIComboBox comboBoxMethod;
    private UIComboBox comboBoxSerivceType;
    private UILabel jLabel1;
    private UILabel jLabel2;
    private UILabel jLabel3;
    private UILabel jLabel4;
    private UIScrollPane jScrollPane1;
    private UITable tableParameters;
    private UITextField textBoxServiceClass;
    // End of variables declaration
}
