package org.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.main.Principal;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;	
import org.apache.poi.ss.usermodel.Row;


public class HomeController implements Initializable {
    
    private long tiempoInicio;
    private Stage stage;
    private static Connection conexion;  
    @FXML private TextField txtFile;
    @FXML private Button btnSelect ;
    @FXML private Button btnExport;
    @FXML private Button btnExit;
    @FXML private ProgressBar progress;
    @FXML private TextArea txtSalida;
    FileChooser fileChooser = new FileChooser();
    File selectedFile;

    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.exit(0);
    }
    
    //**********************************************************************************Seleccionar Archivo**************************************************************
    @FXML
    private void newFile(ActionEvent event) {
        
        stage = Principal.getInstancia().getStage();
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx", "*.xls")
       );
        fileChooser.setTitle("Selecciona un archivo excel");
         selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null){
            txtFile.setText(selectedFile.getAbsolutePath());
            btnExport.setDisable(false);
        }     
    }
    
    //********************************************************************************Exportar*************************************************************************
    @FXML
    private void Export(ActionEvent event) {
    
    try {
           
           if( MySQLConnection("admin", "1234", "prueba")){
               
                btnExport.setDisable(true);

                 Thread thread1 = new Thread(){

                     public void run(){

                        tiempoInicio = System.currentTimeMillis();
                         
                         System.out.println(txtFile.getText());
                         String rutaArchivoExcel = txtFile.getText();

                         try {
                             FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));
                             HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                             String anio = "";
                             String titulo = "";

                             HSSFSheet sheet = workbook.getSheetAt(0);
                             Iterator<Row> rowIterator = sheet.iterator();


                             Row row;

                             int cont=0;
                             while (rowIterator.hasNext()){
                                 row = rowIterator.next();

                                 Iterator<Cell> cellIterator = row.cellIterator();
                                 Cell celda;

                                 int i=0;
                                 while (cellIterator.hasNext()){
                                    celda = cellIterator.next();
                                    String resultado ;
                                    String temp;
                                    String ID = "";
                                    i++;

                                     if(i==1){
                                         

                                         resultado = celda.toString();


                                         System.out.println("***************************************************************************");
                                         System.out.println("Cont:   "+cont);
                                         System.out.println("Original:   "+resultado);
                                         String []  bar = resultado.split("::");

                                         for(int j = 0; j<bar.length;j++){

                                             if(j==0){
                                             
                                                if(cont==0){
                                                    txtSalida.appendText(txtSalida.getText()+ "\n" + "Separando los datos");
                                                     progress.setProgress(0.26);

                                                 }

                                                 System.out.println("ID:  "+bar[j]);
                                                 ID = bar[j] ;

                                             }else if(j==1){//si es el titulo y si el string contiene (

                                                 if(bar[j].contains("(") == true){


                                                     temp = bar[j].replace("(", ">");//si le dejo el ( da error 
                                                     String []  split = temp.split(">"); //los separo por :

                                                     anio = split[1]; //obtengo el segundo valor
                                                     anio = anio.replace(")", ""); //lo reemplaso por un valor vacio

                                                     titulo = split[0]; // para obtener el valor del titulo
                                                     titulo = titulo.replace("(","");
                                                     try {
                                                         Integer.parseInt(anio);
                                                     } catch (NumberFormatException excepcion) {

                                                         anio = split[2];
                                                         anio = anio.replace(")", "");

                                                         titulo = "";

                                                         titulo = split[0] + "("+split[1]; // para obtener el valor del titulo
                                                         titulo = titulo.replace(">","(");
                                                     }

                                                 }

                                                 if(cont==1){
                                                     txtSalida.appendText(txtSalida.getText()+ "\n" + "La pelicula nuemero 1 ha sido guardada correctamente");
                                                     progress.setProgress(0.39);
                                                 }else if (cont == 1000){
                                                     txtSalida.appendText(txtSalida.getText()+ "\n" + "La pelicula nuemero 1000 ha sido guardada correctamente");
                                                     progress.setProgress(0.52);
                                                 }else if(cont == 2000){
                                                     txtSalida.appendText(txtSalida.getText()+ "\n" + "La pelicula nuemero 2000 ha sido guardada correctamente");
                                                     progress.setProgress(0.65); 
                                                 }else if (cont == 3000){
                                                     txtSalida.appendText(txtSalida.getText()+ "\n" + "La pelicula nuemero 3000 ha sido guardada correctamente");
                                                     progress.setProgress(0.78);
                                                 }else if(cont==3882){
                                                     txtSalida.appendText(txtSalida.getText()+ "\n" + "La pelicula nuemero 3882 ha sido guardada correctamente");
                                                     progress.setProgress(0.91);
                                                 }

                                                 System.out.println("Titulo: "+titulo);//para obtener la fecha

                                                 System.out.println("Año: "+anio);//para obtener la fecha                                                                                                 
                                                 InsertPelicula(ID,titulo,anio);

                                              }else if(j==2){
                                                 System.out.println("Categoria: "+bar[j]);//para obtener la fecha

                                                 bar[j] = bar[j].replace("|", ":");
                                                 String [] categoria = bar[j].split(":");

                                                 for(int m=0;m<categoria.length;m++){
                                                     BuscarCategoria(cont,categoria[m]);
                                                 }                                                                                             
                                                
                                                try {
                                                    String Query = "SELECT * FROM `pelicula` WHERE `codigo_pelicula` = "+"\""+ID+"\"";

                                                    System.out.println(Query);
                                                    Statement st = conexion.createStatement();
                                                    java.sql.ResultSet resultSet;
                                                    resultSet = st.executeQuery(Query);

                                                    while (resultSet.next()) {
                                                        InsertCategoriaPelicula(bar[j],resultSet.getInt("codigo_pelicula") );
                                                    }
                                                                                                         
                                                } catch (SQLException ex) {
                                                    JOptionPane.showMessageDialog(null, "Error en la adquisición de datos");
                                                    ex.printStackTrace();
                                                }
                                            }
                                         }
                                     }else {
                                         //si hay dos columnas
                                     }
                                 }  
                                 cont++;
                             }
                            
                             //Conexion.commit();



                         } catch (Exception e) {
                             e.printStackTrace();
                         }

                         txtSalida.appendText(txtSalida.getText()+ "\n" + "Proceso Terminado en "+((System.currentTimeMillis() - tiempoInicio)*0.001) + " segundos.");
                         progress.setProgress(1);
                     }

                 };  

                 thread1.start();
           }else{
               JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos");
           }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
  
    public void BuscarCategoria(int id,String Busqueda){
      
        try {
            String Query = "SELECT * FROM `genero` WHERE `descripcion` = "+"\""+Busqueda+"\"";

            System.out.println(Query);
            Statement st = conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);

            if (resultSet.next()== false) {
                InsertCategoria(Busqueda);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos");
            ex.printStackTrace();
        }
     }
      
        //**********************************************************************initialize***************************************************************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnExport.setDisable(true);

    } 

    //********************************************************************Base de datos***************************************************************************
    

    public boolean MySQLConnection(String user, String pass, String db_name) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db_name, user, pass);
            JOptionPane.showMessageDialog(null, "Se ha iniciado la conexión con el servidor de forma exitosa");
            return true;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }  

    public void InsertPelicula(String ID, String tittle, String year) {
        try {
            if(ID.equals("1")){
                 //Conexion.setAutoCommit(false);
            }
            String Query = "INSERT INTO `pelicula` (`codigo_pelicula`, `descripcion`, `anio_produccion`) "
            + "VALUES (\""+ID+"\", \""+tittle+"\", " + "\"" + year + "\"" + ");";
            System.out.println(Query);
            Statement st = conexion.createStatement();
            st.executeUpdate(Query);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error en el almacenamiento de datos");
        }
    }
    
    public void InsertCategoria(String descripcion) {
        try {
            String Query = "INSERT INTO `genero` (`descripcion`) VALUES (\""+descripcion+"\");";
            System.out.println(Query);
            Statement st = conexion.createStatement();
            st.executeUpdate(Query);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void InsertCategoriaPelicula( String CategoriaPelicula, int IDPelicula) {
        try {
            
            String [] categoria = CategoriaPelicula.split(":");

            for(int j=0;j<categoria.length;j++){

                String Query = "INSERT INTO `pelicula_genero` (`codigo_pelicula`,`codigo_genero`) "
                + "VALUES (\""+ (IDPelicula) +"\", " + "\"" + (BuscarCat(categoria[j]) )+ "\"" + ");";

                System.out.println(Query);
                Statement st = conexion.createStatement();
                st.executeUpdate(Query);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public int BuscarCat(String descripcion) {
        
        int res=0;

        try {
            String Query = "SELECT * FROM `genero` WHERE `descripcion` = "+"\""+descripcion+"\"";

            System.out.println(Query);
            Statement st = conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);

            while (resultSet.next()) {
                res = resultSet.getInt("codigo_genero");
            }            

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos");
        }
        return res;
    }
    
}


