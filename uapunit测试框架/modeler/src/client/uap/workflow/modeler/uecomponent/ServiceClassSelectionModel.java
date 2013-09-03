
package uap.workflow.modeler.uecomponent;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.tree.DefaultMutableTreeNode;

public class ServiceClassSelectionModel extends DefaultTreeModel {
    public ServiceClassSelectionModel(TreeNode root,boolean b){
        super(root,b);
    }

    public ServiceClassSelectionModel(TreeNode root){
        super(root);
        //javax.swing.tree.DefaultMutableTreeNode treeNode1 = (javax.swing.tree.DefaultMutableTreeNode)root;
    }

    private Set<Class<?>> getClasses(String path){
        String packageName = "";
        boolean recursive = true;
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        String packageDirName = packageName.replace('.', '/');
        Enumeration<JarEntry> entries = getEntriesFromJar(path);
        if(entries != null){
            while (entries.hasMoreElements()) {
                // ��ȡjar���һ��ʵ�� ������Ŀ¼ ��һЩjar����������ļ� ��META-INF���ļ�
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // �������/��ͷ��
                if (name.charAt(0) == '/') {
                    // ��ȡ������ַ���
                    name = name.substring(1);
                }
                // ���ǰ�벿�ֺͶ���İ�����ͬ
                if (name.startsWith(packageDirName)) {
                    System.out.println("====ClassScan==== ǰ�벿�ֺͶ���İ�����ͬ�İ� : "+ (packageDirName));
                    int idx = name.lastIndexOf('/');
                    // �����"/"��β ��һ����
                    if (idx != -1) {
                        // ��ȡ���� ��"/"�滻��"."
                        packageName = name.substring(0, idx).replace('/', '.');
                    }
                    // ������Ե�����ȥ ������һ����
                    if ((idx != -1) || recursive) {
                        // �����һ��.class�ļ� ���Ҳ���Ŀ¼
                        if (name.endsWith(".class")&& !entry.isDirectory()) {
                            // ȥ�������".class" ��ȡ����������
                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                            try {
                                // ��ӵ�classes
                                System.out.println("====ClassScan====" + packageName + '.'+ className);
                                classes.add(Class.forName(packageName + '.'+ className, true, Thread.currentThread().getContextClassLoader()));
                                System.out.println("====ClassScan====" + packageName + '.'+ className);
                            } catch (Exception e) {
                                System.out.println("====ClassScan====" + e.getMessage());
                            }
                        }
                    }
                }
            }
        }

        return classes;
    }


    public void setPath(String path){
        try{
            DefaultMutableTreeNode _root = (DefaultMutableTreeNode)getRoot();
            Enumeration<JarEntry> entries = getEntriesFromJar(path);

            Map<String, DefaultMutableTreeNode> pathNodeMap = new HashMap<String, DefaultMutableTreeNode>();

            if (entries != null) {
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    String entryPath = entry.getName();

                    String[] entryPaths = entryPath.split("/");

                    if (entryPaths.length == 0) {
                    } else {
                        StringBuffer sb = new StringBuffer();

                        for (int i = 0; i < entryPaths.length; i++) {
                            String parentPath = sb.toString();
                            DefaultMutableTreeNode parentNode = null;
                            if (i == 0) {
                                parentNode = (DefaultMutableTreeNode) root;
                            } else {
                                parentNode = pathNodeMap.get(parentPath);
                            }
                            sb.append(entryPaths[i]);

                            if (!pathNodeMap.containsKey(sb.toString())) {
                                DefaultMutableTreeNode node = new DefaultMutableTreeNode(entryPaths[i]);
                                pathNodeMap.put(sb.toString(), node);
                                parentNode.add(node);
                            }

                        }
                    }
                }
            }


        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

	public static Enumeration<JarEntry>  getEntriesFromJar(String filepath){
		JarFile ufj = null;
		try {
			ufj = new JarFile(new File(filepath));
			return ufj.entries();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
}
