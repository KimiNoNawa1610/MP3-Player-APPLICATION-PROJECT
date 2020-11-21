import java.sql.SQLException;

public class Driver {

    public static void main(String[] args) throws SQLException{

        StreamPlayerGUI player=StreamPlayerGUI.getInstance();

        player.setVisible(true);

    }

}
