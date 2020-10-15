import java.sql.SQLException;

public class Driver {
    public static void main(String[] args) throws SQLException {
        StreamPlayerGUI player= new StreamPlayerGUI();
        player.setVisible(true);
    }
}
