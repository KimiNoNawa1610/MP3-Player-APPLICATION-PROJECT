public class Driver {
    public static void main(String[] args){
        SQLConnection sql=new SQLConnection();
        //sql.Connect();
        StreamPlayerGUI player= new StreamPlayerGUI();
        player.setVisible(true);
    }
}
