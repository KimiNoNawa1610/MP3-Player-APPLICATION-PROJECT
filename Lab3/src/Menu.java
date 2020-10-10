import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/*

Nhan Vo
CECS 343-project

 */
class StreamPlayerGUI extends JFrame {

    boolean isPause;

    BasicPlayer player;

    JPanel main;

    JLabel nowPlaying;

    JButton Play,Pause,Skipf, Skipb;

    ButtonListener bl= new ButtonListener();

    PauseListener pl=new PauseListener();

    SkipFoward sf=new SkipFoward();

    SkipBackward sb=new SkipBackward();

    JTable table;

    JScrollPane scrollPane;

    int CurrentSelectedRow;

    public StreamPlayerGUI() {

        main = new JPanel();

        main.setLayout(new FlowLayout());

        String[] columns = {"Title", "Description", "Artist","Year"};

        Object[][] data = {{"Big City Boy","Rap Việt","Binz","2020"}};


        table=new JTable(data,columns);

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

        Skipb=new JButton("Skip Backward");

        Play=new JButton("Play");

        Pause=new JButton("Pause");

        Skipf=new JButton("Skip Forward");

        Play.addActionListener(bl);

        Pause.addActionListener(pl);

        Skipf.addActionListener(sf);

        Skipb.addActionListener(sb);

        scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(new Dimension(500,100));

        main.add(scrollPane);

        main.add(Play);

        main.add(Pause);

        main.add(Skipf);

        main.add(Skipb);

        main.setDropTarget(new MyDropTarget());

        player = new BasicPlayer();

        this.setTitle("StreamPlayer by Nhan Vo");//change the name to yours

        this.setSize(525, 180);

        this.add(main);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public String getURL(){
        return "D:\\CECS-343\\Lab3\\Big City Boi - Binz_ Touliver.mp3";
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
