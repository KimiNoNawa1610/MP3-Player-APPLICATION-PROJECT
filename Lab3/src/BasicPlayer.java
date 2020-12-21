import java.net.URL;
//Nhan Vo
//CECS 343 Lab Mock Object
public class BasicPlayer {

    private double gain=0;
    static final int
            UNKNOWN=0,
            OPENED=1,
            PLAYING=2,
            STOPPED=3;
    private int status=UNKNOWN;

    public BasicPlayer() {
        System.out.println("MBP - Creating BasicPlayer object with status UNKNOWN");
    }

    public int getStatus() throws BasicPlayerException {
        System.out.println("MBP - Getting status - status is " + status);
        return status;
    }

    public void open(URL url) throws BasicPlayerException {
        System.out.println("MBP - Opening URL " + url);
        status = OPENED;
    }

    public void play() throws BasicPlayerException {
        System.out.println("MBP - Playing...");
        status = PLAYING;
    }

    public void setGain(double ga) throws BasicPlayerException {
        gain = ga;
        System.out.println("MBP - Setting gain to " + ga);
    }

    public void resume() throws BasicPlayerException {
        System.out.println("MBP - Resuming playback");
        status = PLAYING;
    }

    public void stop() throws BasicPlayerException {
        System.out.println("MBP - Stopping play");
        status = STOPPED;
    }

}
