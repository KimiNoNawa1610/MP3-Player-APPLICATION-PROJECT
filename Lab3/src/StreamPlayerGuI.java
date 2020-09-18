import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

class StreamPlayerGUI extends JFrame {

    BasicPlayer player;

    JPanel main;

    JButton rock, the80s, purDeutsch;//the three buttons

    JLabel nowPlaying;

    ButtonListener bl;

    public StreamPlayerGUI() {

        player = new BasicPlayer();

        main = new JPanel();

        main.setLayout(new FlowLayout());

        bl = new ButtonListener();

        rock = new JButton("Rock");

        rock.addActionListener(bl);

//create the other two buttons and assign them an action listener
        the80s=new JButton("The 80's");

        the80s.addActionListener(bl);

        purDeutsch=new JButton("Pur Deutsch");

        purDeutsch.addActionListener(bl);

        nowPlaying = new JLabel("Now playing: nothing");

        main.add(rock);

        main.add(the80s);

        main.add(purDeutsch);

        main.add(nowPlaying);

        this.setTitle("StreamPlayer by Nhan Vo");//change the name to yours

        this.setSize(400, 150);

        this.add(main);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    class ButtonListener implements ActionListener {


        @Override

        public void actionPerformed(ActionEvent e) {

            String url=null;

            if("Rock".equals(e.getActionCommand())){

                System.out.println("Rock");

                url = "https://mp3.ffh.de/ffhchannels/hqrock.mp3";

            }
            else if("The 80's".equals(e.getActionCommand())){

                System.out.println("The 80's");

                url = "https://mp3.ffh.de/ffhchannels/hq80er.mp3";

            }
            else if("Pur Deutsch".equals(e.getActionCommand())){

                System.out.println("Pur Deutsch");

                url = "https://mp3.ffh.de/ffhchannels/hqdeutsch.mp3";

            }


//create if, output and url assignment statements for the other two channels

            try {

                nowPlaying.setText(url);

                player.open(new URL(url));

                player.play();

            } catch (MalformedURLException ex) {

                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);

                System.out.println("Malformed url");

            } catch (BasicPlayerException ex) {

                System.out.println("BasicPlayer exception");

                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

}
