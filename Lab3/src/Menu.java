import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
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

    private ArrayList<String[]>SongURl=new ArrayList<>();

    private boolean isPause;

    private BasicPlayer player;

    private JPanel main;

    private JButton Play,Pause,Skipf, Skipb, Delete,Add;

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

    private Connection connection = DriverManager.getConnection(url, user, password);

    public StreamPlayerGUI() throws SQLException {

        main = new JPanel();

        main.setLayout(new FlowLayout());

        try {
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
                String URl=resultSet.getString("URL");
                newTable.addRow(new Object[]{iD,title,genre,artist,year});
                SongURl.add(new String[]{iD, URl});
            }
            System.out.println("Done");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        table=new JTable(newTable);



        MouseListener mouseListener = new MouseAdapter() {
            //this will print the selected row index when a user clicks the table
            public void mousePressed(MouseEvent e) {
                CurrentSelectedRow = table.getSelectedRow();
            }
        };

        table.addMouseListener(mouseListener);

        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);

        column = table.getColumnModel().getColumn(1); //Description is Column 1
        column.setPreferredWidth(150);

        table.setVisible(true);

        Skipf=new JButton(">>");

        Play=new JButton(">");

        Pause=new JButton("| |");

        Skipb=new JButton("<<");

        Delete=new JButton("Delete");

        Add =new JButton("Add");

        Play.addActionListener(bl);

        Pause.addActionListener(pl);

        Skipf.addActionListener(sf);

        Skipb.addActionListener(sb);

        Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteSong();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String url =JOptionPane.showInputDialog(this,"Enter the URL of the song");
                try {
                    addSong(url);
                } catch (UnsupportedTagException unsupportedTagException) {
                    unsupportedTagException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InvalidDataException invalidDataException) {
                    invalidDataException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(500,75+30*SongURl.size()));

        main.add(scrollPane);

        main.add(Skipb);

        main.add(Play);

        main.add(Pause);

        main.add(Skipf);

        main.add(Delete);

        main.add(Add);

        main.setDropTarget(new MyDropTarget(this));

        player = new BasicPlayer();

        this.setTitle("StreamPlayer by Nhan Vo");//change the name to yours

        this.setSize(525, 200+50*SongURl.size());

        this.add(main);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public String getURL(){
        String n= SongURl.get(CurrentSelectedRow)[1];
        return n;
    }

    public void addSong(String n) throws InvalidDataException, IOException, UnsupportedTagException, SQLException {
        Mp3Song song=new Mp3Song(n);
        String insert="INSERT INTO `songs`(Title, Genre, Artist, Year, URL) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(insert);
        preparedStatement.setString(1,song.getTitle());
        preparedStatement.setString(2,song.getGenres());
        preparedStatement.setString(3,song.getArtist());
        preparedStatement.setInt(4,song.getReleasedYear());
        preparedStatement.setString(5,song.getURL());
        preparedStatement.executeUpdate();
        SongURl.add(new String[]{getLastestSongId(),song.getURL()});
        newTable.addRow(new Object[]{SongURl.get(SongURl.size()-1)[0],song.getTitle(),song.getGenres(),song.getArtist(),song.getReleasedYear()});
    }

    public String getLastestSongId(){
        int max;
        if(isEmpty()){
            max=0;
        }
        else{
            max= Integer.parseInt(SongURl.get(0)[0]);
            for(int i=0;i<SongURl.size();i++){
                int n=Integer.parseInt(SongURl.get(i)[0]);
                if(n>max){
                    max=n;
                }
            }
        }
        return Integer.toString(max+1);
    }

    public Boolean isEmpty(){
        return (SongURl.size()==0) ? true:false;
    }

    public void deleteSong() throws SQLException {
        String delete="DELETE FROM songs WHERE SongID=" +SongURl.get(CurrentSelectedRow)[0];
        PreparedStatement preparedStatement=connection.prepareStatement(delete);
        preparedStatement.executeUpdate();
        newTable.removeRow(CurrentSelectedRow);
        SongURl.remove(CurrentSelectedRow);
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

                else{

                    player.open(new File(url));

                    player.play();

                    isPause=false;

                }

            } catch (BasicPlayerException ex) {

                System.out.println("BasicPlayer exception");

                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);

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
