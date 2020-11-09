import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;

public class TreeList extends JPanel {
    private ArrayList<String> PlayList=new ArrayList<>();
    private JTree Library;
    private JTree PlaylistTree;
    private DefaultMutableTreeNode root=new DefaultMutableTreeNode("Playlist");
    public TreeList(){
        DefaultMutableTreeNode lib=new DefaultMutableTreeNode("Library");
        Library=new JTree(lib);
        PlaylistTree =new JTree(root);
        this.setVisible(true);
        this.add(Library);
        this.add(PlaylistTree);
        this.setSize(new Dimension(100,400));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(20,0)));

    }

    public void addPlaylist(String n){
        DefaultTreeModel newModel= (DefaultTreeModel) PlaylistTree.getModel();
        DefaultMutableTreeNode oldRoot= (DefaultMutableTreeNode) newModel.getRoot();
        newModel.insertNodeInto(new DefaultMutableTreeNode(n),oldRoot,oldRoot.getChildCount());
        PlayList.add(n);
    }



}
