import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class TreeList extends JPanel {
    private ArrayList<String> PlayList;
    private JTree Library;
    private JTree PlaylistTree;
    private String data;
    public TreeList() throws SQLException {
        PlayList=new PlayList().getPlalistname();
        DefaultMutableTreeNode root=new DefaultMutableTreeNode("Playlist");
        DefaultMutableTreeNode lib=new DefaultMutableTreeNode("Library");
        innerMenu menu=new innerMenu(PlaylistTree);
        root.add(new DefaultMutableTreeNode("Empty"));
        for(int i=0;i<PlayList.size()-1;i++){
            root.add(new DefaultMutableTreeNode(PlayList.get(i)));
        }
        Library=new JTree(lib);
        PlaylistTree =new JTree(root);
        PlaylistTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.isPopupTrigger()){
                    menu.show(e.getComponent(),e.getX(),e.getY());
                }
            }
        });

        PlaylistTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) PlaylistTree.getLastSelectedPathComponent();
                //if(PlaylistTree.getSelectionPath()!=null){
                //}

                if(node == null)return;
                Object nodeInfo = node.getUserObject();
                data=nodeInfo.toString();
                try {
                    StreamPlayerGUI.getInstance().update(nodeInfo.toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });


        this.setVisible(true);
        this.add(Library);
        this.add(PlaylistTree);
        this.setSize(new Dimension(80,400));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(20,0)));

    }

    public void addPlaylist(String n){
        DefaultTreeModel newModel= (DefaultTreeModel) PlaylistTree.getModel();
        DefaultMutableTreeNode oldRoot= (DefaultMutableTreeNode) newModel.getRoot();
        newModel.insertNodeInto(new DefaultMutableTreeNode(n),oldRoot,oldRoot.getChildCount());
        PlayList.add(n);
    }

    public void removePlaylist(){
        DefaultMutableTreeNode CurrentNode= (DefaultMutableTreeNode) PlaylistTree.getSelectionPath().getLastPathComponent();
        DefaultTreeModel model= (DefaultTreeModel) PlaylistTree.getModel();
        model.removeNodeFromParent(CurrentNode);
    }


    class innerMenu extends JPopupMenu{
        public innerMenu(JTree t){
            this.setPopupSize(150,100);
            JMenuItem newWindow=new JMenuItem("Open in new window");
            JMenuItem removePlaylist=new JMenuItem("Delete Playlist");
            newWindow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Popup newwin =new Popup(data);
                        newwin.setVisible(true);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    // Add new window implementation
                }
            });

            removePlaylist.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        DefaultMutableTreeNode CurrentNode= (DefaultMutableTreeNode) PlaylistTree.getSelectionPath().getLastPathComponent();
                        PlayList Plist=new PlayList();
                        removePlaylist();
                        Plist.deletePlaylist(CurrentNode);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
            });

            this.add(newWindow);
            this.add(removePlaylist);
        }
    }

}