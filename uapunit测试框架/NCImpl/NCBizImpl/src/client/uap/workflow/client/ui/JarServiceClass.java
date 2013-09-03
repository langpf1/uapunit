
package uap.workflow.client.ui;

import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JarServiceClass.java
 *
 * Created on 2012-6-25, 20:32:09
 */

/**
 *
 * @author wcj
 */
public class JarServiceClass extends UIDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JarServiceClassModel classMethodModel;

    private String result = null;

    public String getServiceClass() {
        return result;
    }

//    public void setResult(String result) {
//        this.result = result;
//    }


    
    /** Creates new form JarServiceClass */
    public JarServiceClass(UIDialog parent) {
        super(parent);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane2 = new UIScrollPane();
        classesTree = new UITree();
        btnOK = new UIButton();
        btnCancel = new UIButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        classesTree.setModel(getTreeModel());
        classesTree.setName("classesTree"); // NOI18N
        jScrollPane2.setViewportView(classesTree);

        //org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(desktopapplication1.DesktopApplication1.class).getContext().getResourceMap(JarServiceClass.class);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnOK)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOK))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {
        TreePath selectionPath = classesTree.getSelectionPath();
        Object[] paths = selectionPath.getPath();

        if (paths == null || paths.length == 1) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < paths.length; i++) {
            if (i != 1) {
                sb.append(".");
            }
            sb.append(paths[i].toString());
        }

        result = sb.toString();
        this.dispose();
}

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        result = null;
        this.dispose();
}

    public JarServiceClassModel getTreeModel(){
        if (classMethodModel == null){
            javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode("Module");
            classMethodModel = new JarServiceClassModel(root);
            classMethodModel.setPath("E:\\Workflow\\activiti-5.9\\setup\\files\\dependencies\\libs\\activation-1.1.jar");
        }
        return classMethodModel;
    }
    // Variables declaration - do not modify
    private UIButton btnCancel;
    private UIButton btnOK;
    private UITree classesTree;
    private UIScrollPane jScrollPane2;
    // End of variables declaration

}
