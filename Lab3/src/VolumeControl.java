import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VolumeControl extends JSlider {
    public VolumeControl(BasicPlayer player){
        super();
        setMinimum(0);
        setMaximum(100);
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float volume=getValue();
                volume=volume/80;
                System.out.println("Current Volume: "+volume);
                try {
                    player.setGain(volume);
                } catch (BasicPlayerException basicPlayerException) {
                    basicPlayerException.printStackTrace();
                }
            }
        });
    }

}
