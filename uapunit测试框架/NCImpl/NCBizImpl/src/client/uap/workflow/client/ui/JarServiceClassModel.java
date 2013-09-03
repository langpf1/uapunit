
package uap.workflow.client.ui;

import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.tree.DefaultMutableTreeNode;

public class JarServiceClassModel extends DefaultTreeModel {
	private static final long serialVersionUID = -7802292682436430060L;

	public JarServiceClassModel(TreeNode root,boolean b){
        super(root,b);
    }

    public JarServiceClassModel(TreeNode root){
        super(root);
    }

    public void setPath(String path){
        try{
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
        	e.printStackTrace();
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
