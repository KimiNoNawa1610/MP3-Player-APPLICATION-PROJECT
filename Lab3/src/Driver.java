import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.IOException;
import java.sql.SQLException;

public class Driver {

    public static void main(String[] args) throws SQLException, InvalidDataException, IOException, UnsupportedTagException {

        StreamPlayerGUI player=StreamPlayerGUI.getInstance();

       player.setVisible(true);

    }

}
