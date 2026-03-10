package com.codeblog.blog.blog_app_apis.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static com.sun.tools.javac.jvm.ByteCodes.error;

@Slf4j
@Service
public class ExcelServiceImpl {

    public  void uploadExcel(String path, MultipartFile file) throws IOException{
        String originalName=file.getOriginalFilename();
        if (originalName == null|| originalName.isBlank()){
            throw new IllegalArgumentException("File name is invalid");
        }
        String extension =originalName.substring(originalName.lastIndexOf(".")+1);
        List<String>allowedExtensions= List.of("xlsx","xls");

        if (!allowedExtensions.contains(extension.toLowerCase())){
            throw new IllegalArgumentException("Only Excel files are allowed");
        }
        //Read Excel
        Workbook workbook= new XSSFWorkbook(file.getInputStream());
        Sheet sheet= workbook.getSheetAt(0);
        Iterator<Row>rows=sheet.iterator();
        if (!rows.hasNext()){
            throw new IllegalArgumentException("Only Excel files are allowed");
        }
        rows.next();
        while (rows.hasNext()){
            Row row= rows.next();
            int  rowNumber=row.getRowNum()+1;
            try {
                if (row.getPhysicalNumberOfCells() < 3) {
                    error.add("Row " + rowNumber + " has missing columns");
                    continue;
                }

                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                int age = (int) row.getCell(2).getNumericCellValue();

                //Validation
                if (name.isBlank()) {
                    throw new IllegalArgumentException("Name cannot be empty at row " + row.getRowNum());
                }
                if (!email.contains("@")) {
                    throw new IllegalArgumentException("Invalid email at row " + row.getRowNum());
                }
                if (age < 18) {
                    throw new IllegalArgumentException("Age must be 18+ at row " + row.getRowNum());
                }
                log.info("Valid Row -> Name: {},Email,Age: {}", name, email, age);
            }catch (Exception e){
                error.add("Row " + rowNumber + "Invalid data format");
            }
    }
        workbook.close();

        if (!error.isEmpty()){
            throw new IllegalArgumentException("Excel validation Errors: "+error);
        }
        log.info("Excel validation completed successfully");
}
private String getStringValue(Cell cell){
        if (cell == null)return null;

        if (cell.getCellType()==cellType.)
}
}
