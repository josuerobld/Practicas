package org.controller;



import org.db.conexion;
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

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import javafx.scene.control.TextArea;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;	
import org.apache.poi.ss.usermodel.Row;


public class HomeController implements Initializable {
    
    private Stage stage;
    @FXML private TextField txtFile;
    @FXML private Button btnSelect;
    @FXML private Button btnExport;
    @FXML private Button btnExit;
    @FXML private TextArea txtSalida;
    FileChooser fileChooser = new FileChooser();
    File selectedFile;
    
    String[] Categoria;
    String[] DescPelicula = new String[3883];;
    String[] AnioPelicula = new String[3883];
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        txtSalida.setText("******Titulo*******");
        for(int i=0;i<DescPelicula.length;i++){
            
            txtSalida.setText(txtSalida.getText() +"\n" +"Posicion "+i+": "+DescPelicula[i]);
        }
        
        txtSalida.setText(txtSalida.getText() +"\n*******Anio*******");
        for(int i=0;i<AnioPelicula.length;i++){
            
            txtSalida.setText(txtSalida.getText() +"\n" +"Posicion "+i+": "+AnioPelicula[i]);
        }

        
        
        /*try{
            PreparedStatement procedimiento= conexion.getInstancia().getConexion().prepareCall("{call listarprueba}");
             ResultSet resultado= procedimiento.executeQuery();
             System.out.print(resultado);
        } catch (Exception e){
        }
        */
    }
    
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

                                /*if(cont==289){
                                    System.out.print("");
                                    System.out.print("");
                                }*/
                                System.out.println("Titulo: "+titulo);//para obtener la fecha
                                DescPelicula[cont] = titulo;
                                
                                System.out.println("Año: "+anio);//para obtener la fecha
                                AnioPelicula[cont] = anio;

                             }else if(j==2){
                                System.out.println("Categoria: "+bar[j]);//para obtener la fecha
                                
                             }
                        }
                        
                    }else {
                        
                    }
                }
                
                cont++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
  
    public void BuscarCategoria(String Busqueda){
      
        //Probar, todavia no funciona
        for(int i=0;i<Categoria.length;i++){
            
            if(Categoria[i].equals(Busqueda)){
                break; 
            }else{
                Categoria[i] = Busqueda;
            }
        }
     }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        btnExport.setDisable(true);
    }    
    
}
