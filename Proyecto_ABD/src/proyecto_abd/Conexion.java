
package proyecto_abd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class Conexion {
    
    Connection con = null;
    public Connection Conexion (){
         
       //Cargar Dirver
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con =DriverManager.getConnection("jdbc:mysql://127.0.0.1/pizzeria","root","");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al conectar con la base de datos"+e.getMessage());
        }
        return con;  
    }
    
    
}