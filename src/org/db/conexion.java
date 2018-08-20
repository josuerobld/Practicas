package org.db;



import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.Connection;  
import java.sql.SQLException; 
import java.sql.Statement;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class conexion {
    
    private Connection conexion;  
    private static conexion instancia;
    
    public conexion() { 
          
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();  
            conexion=DriverManager.getConnection("jdbc:sqlserver://localhost;dataBaseName=DBEjercicio;user=SA;password=123;");        

        }catch(ClassNotFoundException e){ 
                e.printStackTrace();
               e.getMessage();
        }catch(InstantiationException e){ 
                e.printStackTrace();
                e.getMessage();

        }catch(IllegalAccessException e){ 
                e.printStackTrace();
                e.getMessage();
        }catch(SQLException e){ 
            e.printStackTrace();
            e.getMessage();
        } 
    }  
    
    public static conexion getInstancia(){ 
        if(instancia == null){ 
            instancia = new conexion();
        } 
        return instancia;
    } 


    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
}
