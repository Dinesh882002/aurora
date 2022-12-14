package explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import app.Window;

public class FileExplorer extends JPanel implements ActionListener{

	String dirPath;
	File directory;
	DefaultMutableTreeNode rootNode;
	DefaultTreeModel model;
	JTree tree;
	JButton openFolder;
	boolean fileOpened = false;
	ClickListener clickListener;
	Window window;
	
	public FileExplorer(Window window) {
		this.window = window;
		openFolder = new JButton("Open Folder");
		openFolder.addActionListener(this);
		
		
		
		this.add(openFolder);
		
	}
	
	//Method to load files and folders CAUTION : Highly recursive
	private void displayFiles(DefaultMutableTreeNode root, File file) {
		File[] files = file.listFiles();
		
		for(File f : files) {
			
			if(f.isDirectory()) {
				DefaultMutableTreeNode sub = new DefaultMutableTreeNode(f.getName());
				sub.setAllowsChildren(true);      		//this line is not needed because it true by default but i added it for better recognition
				root.add(sub);
				displayFiles(sub, f);
			}else if(f.isFile()) {
				DefaultMutableTreeNode fi = new DefaultMutableTreeNode(f.getName());
				fi.setAllowsChildren(false);
				root.add(fi);
			}
		}
	}
	
	//
	private void openFolder() {
		
		JFileChooser chooser = new JFileChooser();
		int response;
		String path;
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		response = chooser.showOpenDialog(this);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			
			directory = chooser.getSelectedFile();
			fileOpened = true;
		}else {
			
		}
		clickListener = new ClickListener(directory, tree, window);
	}
	
	private void loadFiles() {
		rootNode = new DefaultMutableTreeNode(directory.getName());
		model = new DefaultTreeModel(rootNode, true);
		tree = new JTree();
		tree.addTreeSelectionListener(clickListener);
		tree.addMouseListener(clickListener);
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			
			 @Override
	            public Component getTreeCellRendererComponent(JTree tree,
	                    Object value, boolean selected, boolean expanded,
	                    boolean isLeaf, int row, boolean focused) {
	                Component c = super.getTreeCellRendererComponent(tree, value,
	                        selected, expanded, isLeaf, row, focused);
	              DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	              String str = (String) node.getUserObject();
	             // System.out.println(str);
	              
	              if(str.endsWith(".java")) {
	            	  setIcon(IconManager.getIcon("javac"));
	              }else if(str.endsWith(".py")) {
	            	  setIcon(IconManager.getIcon("python1"));
	              }else if(str.endsWith(".c")) {
	            	  setIcon(IconManager.getIcon("c"));
	              }else if(str.endsWith(".js")) {
	            	  setIcon(IconManager.getIcon("javascript"));
	              }else if(str.endsWith(".cpp")) {
	            	  setIcon(IconManager.getIcon("cpp"));
	              }
	                return c;
	            }
			
		});
		displayFiles(rootNode, directory);
		tree.setModel(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		openFolder();
		if(directory != null) {
			loadFiles();
			this.remove(openFolder);
			this.setLayout(new BorderLayout());
			this.add(tree, BorderLayout.CENTER);
			this.updateUI();
		}
		
	}
	
	public JTree getTree() {
		return tree;
	}
}
