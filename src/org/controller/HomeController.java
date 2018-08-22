package org.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.main.Principal;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;	
import org.apache.poi.ss.usermodel.Row;


public class HomeController implements Initializable {
    
    private Stage stage;
    private static Connection Conexion;  
    @FXML private TextField txtFile;
    @FXML private Button btnSelect;
    @FXML private Button btnExport;
    @FXML private Button btnExit;
    @FXML private TextArea txtSalida;
    FileChooser fileChooser = new FileChooser();
    File selectedFile;
   
    
    ArrayList <String> Categoria = new ArrayList <String> ();
    ArrayList <String> DescPelicula = new ArrayList <String> ();
    ArrayList <String> CatPelicula = new ArrayList <String> ();
    ArrayList <String> AnioPelicula = new ArrayList <String> ();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        try {
            
            MySQLConnection("admin", "1234", "prueba");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //***************************************************Seleccionar Archivo********************************
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
    
    //*************************************************************Exportar********************************
    @FXML
    private void Export(ActionEvent event) {
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
                    i++;
                    
                    if(i<=1){
                        
                        resultado = celda.toString();
                        System.out.println("***************************************************************************");
                        System.out.println("Cont:   "+cont);
                        System.out.println("Original:   "+resultado);
                        
                        //String []  bar = resultado.split("::|\\|"); //para remplazar por ! tambien
                        String []  bar = resultado.split("::");

                        for(int j = 0; j<bar.length;j++){

                            if(j==0){

                                System.out.println("ID:  "+bar[j]);

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
                                        //titulo = titulo.replace("(","");
                                        titulo = titulo.replace(">","(");
                                    }
                                    
                                }

                                if(cont==3881){
                                    System.out.print("");
                                    System.out.print("");
                                }
                                
                                System.out.println("Titulo: "+titulo);//para obtener la fecha
                                DescPelicula.add(titulo);
                                
                                
                                System.out.println("Año: "+anio);//para obtener la fecha
                                AnioPelicula.add(anio);
                                
                                InsertPelicula(DescPelicula.get(cont),AnioPelicula.get(cont));

                             }else if(j==2){
                                System.out.println("Categoria: "+bar[j]);//para obtener la fecha
                                CatPelicula.add(bar[j]);
                                
                                bar[j] = bar[j].replace("|", ":");
                                String [] categoria = bar[j].split(":");
                                    
                                for(int m=0;m<categoria.length;m++){
                                    
                                    BuscarCategoria(cont,categoria[m]);
                                    
                                }
                                
                             }
                        }
                        
                    }else {
                        //si hay dos columnas
                    }
                }  
                cont++;
            }
            
            
            InsertCategoriaPelicula();
            
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
  
    public void BuscarCategoria(int id,String Busqueda){
      
        boolean found = false;
        
        //Probar, todavia no funciona
        for(int i=0;i<Categoria.size();i++){
            
            if(Categoria.get(i) != null){
                if(Categoria.get(i).equals(Busqueda)){
                    found = true;
                    break;
                }
            }
        }
        
        if(found){
            //Categoria[id] = null;   
        }else{
            Categoria.add(Busqueda);
            InsertCategoria(Busqueda);
        }
     }
      
        //**************************************************************initialize********************************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnExport.setDisable(true);
        try {
            // TODO
            MySQLConnection("admin", "1234", "prueba");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } 

    //***********************************************************Base de datos********************************
    
    public void MySQLConnection(String user, String pass, String db_name) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db_name, user, pass);
            JOptionPane.showMessageDialog(null, "Se ha iniciado la conexión con el servidor de forma exitosa");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  

    public void InsertPelicula(String tittle, String year) {
        try {
            String Query = "INSERT INTO `pelicula` (`descripcion`, `anio`) VALUES (\""+tittle+"\", " + "\"" + year + "\"" + ");";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error en el almacenamiento de datos");
        }
    }
    
    public void InsertCategoria(String descripcion) {
        try {
            String Query = "INSERT INTO `categoria` (`descripcion`) VALUES (\""+descripcion+"\");";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            
        }
    }
    
    public void InsertCategoriaPelicula() {
        try {
            
            for(int q=0;q<DescPelicula.size();q++){
                
                CatPelicula.get(q).replace("|", ":");
                String [] categoria = CatPelicula.get(q).split(":");
                
                for(int j=0;j<categoria.length;j++){
                    
                    String Query = "INSERT INTO `pelicula_categoria` (`id_Pelicula`,`id_Categoria`) "
                    + "VALUES (\""+ q+1 +"\", " + "\"" + BuscarCat(categoria[j]) + 1 + "\"" + ");";
                    
                    System.out.println(Query);
                    Statement st = Conexion.createStatement();
                    st.executeUpdate(Query);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }
    
    public int BuscarCat(String descripcion) {
        
        int res=0;
        
        for(int i=0;i<Categoria.size();i++){
            
            
            if(Categoria.get(i).equals(descripcion)){
                res = i;
            }
        }
            
        
        return res;
    }
    
    
}


