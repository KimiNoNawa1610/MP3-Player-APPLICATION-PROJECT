import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*

Nhan Vo

CECS 343-project

*/
class StreamPlayerGUI extends JFrame {

    private boolean isPause;

    private boolean isPlaying;

    private int isPlayingrow;

    private final BasicPlayer player;

    private final PauseListener pl=new PauseListener();

    private final JTable table;

    private final JTextField playSong;

    private int CurrentSelectedRow;

    private final Library lib=new Library();

    public StreamPlayerGUI() throws SQLException {

        JPanel main = new JPanel();

        TreeList listTree=new TreeList();

        JPanel sidePanel=new JPanel();

        sidePanel.setLayout(new FlowLayout());

        main.setLayout(new FlowLayout());

        JMenuBar topMenu=new JMenuBar();

        JMenu menu=new JMenu("Menu");

        JMenuItem mitem1=new JMenuItem("Play");

        menu.add(mitem1);

        JMenuItem mitem2=new JMenuItem("Add");

        menu.add(mitem2);

        JMenuItem mitem3=new JMenuItem("Delete");

        menu.add(mitem3);

        JMenuItem mitem4=new JMenuItem("Add Playlist");

        menu.add(mitem4);

        topMenu.add(menu);

        //topMenu.add(Box.createHorizontalStrut(800));

        this.add(topMenu, BorderLayout.NORTH);

        this.add(listTree, BorderLayout.WEST);

        table=lib.getTable();

        table.setRowHeight(20);

        TableColumnModel columnModel = table.getColumnModel();

        JPopupMenu popmenu = new JPopupMenu();

        popmenu.setPopupSize(50,100);

        MouseListener mouseListener = new MouseAdapter() {
            //this will print the selected row index when a user clicks the table
            public void mousePressed(MouseEvent e) {
                CurrentSelectedRow = table.getSelectedRow();
            }
        };

        table.addMouseListener(mouseListener);

        table.setVisible(true);

        JButton skipf = new JButton(">>");

        JButton play1 = new JButton(">");

        JButton pause = new JButton("| |");

        JButton skipb = new JButton("<<");

        JButton stop = new JButton("Stop");

        JLabel notInData = new JLabel("enter the url to just play song");

        playSong = new JTextField("",15);

        ButtonListener bl = new ButtonListener();

        play1.addActionListener(bl);

        pause.addActionListener(pl);

        SkipFoward sf = new SkipFoward();

        skipf.addActionListener(sf);

        SkipBackward sb = new SkipBackward();

        skipb.addActionListener(sb);

        StopListener st=new StopListener();

        stop.addActionListener(st);

        mitem1.addActionListener(bl);

        mitem2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String n=FileOpener();

                try {

                    addSong(n);

                } catch (InvalidDataException | IOException | UnsupportedTagException | SQLException invalidDataException) {

                    invalidDataException.printStackTrace();

                }
            }

        });

        mitem3.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                try {

                    deleteSong();

                } catch (SQLException throwables) {

                    throwables.printStackTrace();

                }

            }

        });

        mitem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName=JOptionPane.showInputDialog(null,"Please enter the playlist name");
                listTree.addPlaylist(listName);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(750,300));

        JMenuItem add=new JMenuItem("Add");

        JMenuItem delete=new JMenuItem("Delete");

        JMenuItem play=new JMenuItem("Play");

        add.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String n=FileOpener();

                try {

                    addSong(n);

                } catch (InvalidDataException | IOException | UnsupportedTagException | SQLException invalidDataException) {

                    invalidDataException.printStackTrace();

                }

            }

        });

        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    deleteSong();

                } catch (SQLException throwables) {

                    throwables.printStackTrace();

                }

            }

        });

        play.addActionListener(bl);

        popmenu.add(add);

        popmenu.add(delete);

        popmenu.add(play);

        table.setComponentPopupMenu(popmenu);

        main.add(scrollPane);

        main.add(skipb);

        main.add(play1);

        main.add(pause);

        main.add(skipf);

        main.add(stop);

        main.add(notInData);

        main.add(playSong);

        main.setDropTarget(new MyDropTarget(this));

        player = new BasicPlayer();

        this.setTitle("StreamPlayer by Nhan and Brandon");//change the name to yours

        this.setSize(850, 450);

        //this.add(listTree);

        this.add(main, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void DisplayError(String n){

        JPanel error=new JPanel();

        JOptionPane.showMessageDialog(error,n,"ERROR",JOptionPane.ERROR_MESSAGE);

    }

    public void addSong(String n) throws InvalidDataException, IOException, UnsupportedTagException, SQLException {

        Mp3Song song=new Mp3Song(n);

        if(lib.SongExist(song)){

            DisplayError(song.getTitle()+" already Exists ");

        }
        else{

            String insert="INSERT INTO `songs`(Title, Genre, Artist, Year, Comment, URL) VALUES (?,?,?,?,?,?)";

            PreparedStatement preparedStatement=lib.MakeSQLStatement(insert);

            preparedStatement.setString(1,song.getTitle());

            preparedStatement.setString(2,song.getGenres());

            preparedStatement.setString(3,song.getArtist());

            preparedStatement.setInt(4,song.getReleasedYear());

            preparedStatement.setString(5,song.getComment());

            preparedStatement.setString(6,song.getURL());

            preparedStatement.executeUpdate();

            lib.AddSong(song);

        }

    }

    public Boolean isPlaying(){

        return isPlayingrow == CurrentSelectedRow && isPlaying;

    }

    public void deleteSong() throws SQLException {

        String delete="DELETE FROM songs WHERE SongID=" +lib.getSongid(CurrentSelectedRow);

        if(isPlaying()){

            DisplayError(" Song id: "+lib.getSongid(CurrentSelectedRow)+" is playing \nCannot delete at this time");

        }
        else{

            PreparedStatement preparedStatement=lib.MakeSQLStatement(delete);

            preparedStatement.executeUpdate();

            lib.RemoveSong(CurrentSelectedRow);

            CurrentSelectedRow--;

            isPlayingrow--;

        }

    }

    public String FileOpener(){

        JFileChooser choosefile=new JFileChooser();

        choosefile.showOpenDialog(null);

        File file=choosefile.getSelectedFile();

        return file.getAbsolutePath();

    }

    class ButtonListener implements ActionListener {

        @Override

        public void actionPerformed(ActionEvent e) {

            String url=lib.getElement(CurrentSelectedRow);
            //create if, output and url assignment statements for the other two channels

            try {

                if(isPause && pl.getPauseRow()==CurrentSelectedRow){

                    player.resume();

                    isPause=false;

                    isPlaying=true;

                }
                else if(!playSong.getText().equals("")){

                    String song=playSong.getText();

                    try {

                        player.open(new File(song));

                        player.play();

                    } catch (BasicPlayerException basicPlayerException) {

                        basicPlayerException.printStackTrace();

                    }

                    isPause=false;

                    isPlaying=true;

                }

                else{

                    player.open(new File(url));

                    player.play();

                    isPause=false;

                    isPlaying=true;

                }

            } catch (BasicPlayerException ex) {

                System.out.println("BasicPlayer exception");

                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

    class StopListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {

                isPlaying=false;

                player.stop();

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }

        }

    }

    class PauseListener implements ActionListener {

        int PauseRow;

        @Override
        public void actionPerformed(ActionEvent e) {

            try{

                PauseRow=CurrentSelectedRow;

                System.out.println(CurrentSelectedRow);

                System.out.println(PauseRow);

                player.pause();

                isPause=true;

                isPlaying=false;

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }

        }

        public int getPauseRow(){

            return PauseRow;

        }

    }

    class SkipFoward implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            if(CurrentSelectedRow<lib.getSongURLSize()-1){

                CurrentSelectedRow+=1;

            }

            else{

                CurrentSelectedRow=0;

            }

            isPlayingrow=CurrentSelectedRow;

            String url=lib.getElement(CurrentSelectedRow);

            try {

                player.open(new File(url));

                player.play();

                table.setRowSelectionInterval(CurrentSelectedRow,CurrentSelectedRow);

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }

        }

    }

    class SkipBackward implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            if(CurrentSelectedRow>0){

                CurrentSelectedRow-=1;

            }

            else{

                CurrentSelectedRow=lib.getSongURLSize()-1;

            }

            isPlayingrow=CurrentSelectedRow;

            String url=lib.getElement(CurrentSelectedRow);

            try {

                player.open(new File(url));

                player.play();

                table.setRowSelectionInterval(CurrentSelectedRow,CurrentSelectedRow);

            } catch (BasicPlayerException basicPlayerException) {

                basicPlayerException.printStackTrace();

            }



        }
    }

}
