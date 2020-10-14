import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) throws SQLException {
        StreamPlayerGUI player= new StreamPlayerGUI();
        player.setVisible(true);
    }
}
