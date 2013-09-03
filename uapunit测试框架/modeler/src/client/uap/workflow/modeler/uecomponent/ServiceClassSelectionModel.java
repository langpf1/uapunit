
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
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // 如果是以/开头的
                if (name.charAt(0) == '/') {
                    // 获取后面的字符串
                    name = name.substring(1);
                }
                // 如果前半部分和定义的包名相同
                if (name.startsWith(packageDirName)) {
                    System.out.println("====ClassScan==== 前半部分和定义的包名相同的包 : "+ (packageDirName));
                    int idx = name.lastIndexOf('/');
                    // 如果以"/"结尾 是一个包
                    if (idx != -1) {
                        // 获取包名 把"/"替换成"."
                        packageName = name.substring(0, idx).replace('/', '.');
                    }
                    // 如果可以迭代下去 并且是一个包
                    if ((idx != -1) || recursive) {
                        // 如果是一个.class文件 而且不是目录
                        if (name.endsWith(".class")&& !entry.isDirectory()) {
                            // 去掉后面的".class" 获取真正的类名
                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                            try {
                                // 添加到classes
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
