import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLConnection {
    String user="root";
    String password="";
    String url="jdbc:mysql://localhost:3306/mp3player";

    Connection connection;
    Statement statement;

    public DefaultTableModel Connect(String query){
        System.out.println("Connecting to mp3player...");
        try{
            connection=DriverManager.getConnection(url,user,password);
            System.out.println("Connected to mp3player"+"\nRetriving information...");
            java.sql.Statement statement=connection.createStatement();
            statement.execute(query);
            ResultSet resultset=statement.getResultSet();
            ResultSetMetaData reMeta=resultset.getMetaData();
            int colums= reMeta.getColumnCount();
            DefaultTableModel table=new DefaultTableModel();
            Vector Col=new Vector();
            Vector Row=new Vector();
            for(int i=1;i<colums+1;i++){
                Col.addElement(reMeta.getColumnName(i));
            }
            table.setColumnIdentifiers(Col);
            while(resultset.next()){
                Row=new Vector();
                for(int i=1;i<colums+1;i++){
                    Row.addElement(resultset.getString(i));
                }
                table.addRow(Row);
            }
            return table;


        } catch(SQLException ex){
            Logger.getLogger(SQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
