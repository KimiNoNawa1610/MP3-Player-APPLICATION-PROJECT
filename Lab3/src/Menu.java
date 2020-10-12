import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*

Nhan Vo

CECS 343-project

 */
class StreamPlayerGUI extends JFrame {

    private ArrayList<String[]> storage=new ArrayList<>();

    private boolean isPause;

    private BasicPlayer player;

    private JPanel main;

    private JButton Play,Pause,Skipf, Skipb;

    private ButtonListener bl= new ButtonListener();

    private PauseListener pl=new PauseListener();

    private SkipFoward sf=new SkipFoward();

    private SkipBackward sb=new SkipBackward();

    private JTable table;

    private JScrollPane scrollPane;

    private int CurrentSelectedRow;

    private String[] Cname={"ID","Tilte","Genre","Artist","Released Year"};

    private DefaultTableModel newTable = new DefaultTableModel(Cname, 0);

    private String user="root";
    private String password="";
    private String url="jdbc:mysql://localhost:3306/mp3player";
    private String columns;

    public StreamPlayerGUI() {

        main = new JPanel();

        main.setLayout(new FlowLayout());

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to SQL data base");
            Statement statement=connection.createStatement();
            columns="SELECT * FROM songs";
            ResultSet resultSet=statement.executeQuery(columns);
            System.out.println("Retrieveing information from SQL data base...");
            while(resultSet.next()){
                System.out.println("...");
                String iD=resultSet.getString("SongID");
                String title= resultSet.getString("Title");
                String genre=resultSet.getString("Genre");
                String artist=resultSet.getString("Artist");
                String year=resultSet.getString("Year");
                newTable.addRow(new Object[]{iD,title,genre,artist,year});

            }
            System.out.println("Done");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        table=new JTable(newTable);
        table.setAutoCreateColumnsFromModel(true);

        MouseListener mouseListener = new MouseAdapter() {
            //this will print the selected row index when a user clicks the table
            public void mousePressed(MouseEvent e) {
                CurrentSelectedRow = table.getSelectedRow();
            }
        };

        table.addMouseListener(mouseListener);

        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(150);

        column = table.getColumnModel().getColumn(1); //Description is Column 1
        column.setPreferredWidth(30);

        table.setVisible(true);

        Skipb=new JButton(">>");

        Play=new JButton(">");

        Pause=new JButton("| |");

        Skipf=new JButton("<<");

        Play.addActionListener(bl);

        Pause.addActionListener(pl);

        Skipf.addActionListener(sf);

        Skipb.addActionListener(sb);

        scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(500,100));

        main.add(scrollPane);

        main.add(Skipf);

        main.add(Play);

        main.add(Pause);

        main.add(Skipb);

        main.setDropTarget(new MyDropTarget(this));

        player = new BasicPlayer();

        this.setTitle("StreamPlayer by Nhan Vo");//change the name to yours

        this.setSize(525, 180);

        this.add(main);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public String getURL(){

        return "D:\\CECS-343\\Lab3\\Big City Boi - Binz_ Touliver.mp3";

    }

    public void addSong(String n) throws InvalidDataException, IOException, UnsupportedTagException, SQLException {
        Mp3Song song=new Mp3Song(n);
        String user="root";
        String password="";
        String url="jdbc:mysql://localhost:3306/mp3player";
        Connection connection = DriverManager.getConnection(url, user, password);
        String insert="INSERT INTO `songs`(Title, Genre, Artist, Year) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(insert);
        preparedStatement.setString(1,song.getTitle());
        preparedStatement.setString(2,song.getGenres());
        preparedStatement.setString(3,song.getArtist());
        preparedStatement.setInt(4,2020);
        System.out.println(song.getReleasedYear());
        preparedStatement.executeUpdate();
    }

    class ButtonListener implements ActionListener {

        @Override

        public void actionPerformed(ActionEvent e) {

            String url=getURL();

            //create if, output and url assignment statements for the other two channels

            try {

                if(isPause==true&& pl.getPauseRow()==CurrentSelectedRow){

                    player.resume();

                    isPause=false;

                }

                else if(pl.getPauseRow()!=CurrentSelectedRow){

                    player.open(new File(url));

                    player.play();

                    isPause=false;

                }

                else{

                    player.open(new File(url));

                    player.play();

                }

            } catch (BasicPlayerException ex) {

                System.out.println("BasicPlayer exception");

                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

    class PauseListener implements ActionListener {
        int PauseRow=CurrentSelectedRow;
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                player.pause();
                isPause=true;
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
            CurrentSelectedRow+=1;
            String url=getURL();
            try {
                player.open(new File(url));
                player.play();
            } catch (BasicPlayerException basicPlayerException) {
                basicPlayerException.printStackTrace();
            }

        }
    }

    class SkipBackward implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            CurrentSelectedRow-=1;
            String url=getURL();
            try {
                player.open(new File(url));
                player.play();
            } catch (BasicPlayerException basicPlayerException) {
                basicPlayerException.printStackTrace();
            }

        }
    }

}
