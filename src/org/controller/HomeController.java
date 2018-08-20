package org.controller;



import org.db.conexion;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.*;
import org.main.Principal;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.IOException;
import javafx.scene.control.TextArea;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;	
import org.apache.poi.ss.usermodel.Row;




/**
 *
 * @author programacion
 */
public class HomeController implements Initializable {
    
    private Stage stage;
    @FXML private TextField txtFile;
    @FXML private Button btnSelect;
    @FXML private Button btnExport;
    @FXML private Button btnExit;
    @FXML private TextArea txtSalida;
    FileChooser fileChooser = new FileChooser();
    File selectedFile;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {

        
        try{
            PreparedStatement procedimiento= conexion.getInstancia().getConexion().prepareCall("{call listarprueba}");
             ResultSet resultado= procedimiento.executeQuery();
             System.out.print(resultado);
        } catch (Exception e){
        }

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
        }
       
       
        
    }
    
    @FXML
    private void Export(ActionEvent event) {
        System.out.println(txtFile.getText());
        String rutaArchivoExcel = txtFile.getText();
        
        
try {
            FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            
            Row row;
            
            while (rowIterator.hasNext()){
                row = rowIterator.next();
                
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                
                int i=0;
                while (cellIterator.hasNext()){
                    celda = cellIterator.next();
                    String resultado ;
                    i++;
                    
                    resultado = celda.toString();
                     System.out.println("i:  "+i+"///"+resultado);
                     //String []  bar = resultado.split("::|\\|");
                     String []  bar = resultado.split("::");
                     
                     for(int j = 0; j<bar.length;j++){
                         System.out.println("Dato"+j+":              "+bar[j]);
                     }
                     System.out.println(bar + " barr");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
