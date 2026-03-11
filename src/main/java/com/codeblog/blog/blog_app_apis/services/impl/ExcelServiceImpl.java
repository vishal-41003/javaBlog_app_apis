package com.codeblog.blog.blog_app_apis.services.impl;

import com.codeblog.blog.blog_app_apis.entities.ExcelUser;
import com.codeblog.blog.blog_app_apis.repository.UserExcelRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class ExcelServiceImpl {

        private final UserExcelRepo userExcelRepo;

        public void uploadExcel(MultipartFile file) throws Exception {

            log.info("Excel upload started");

            // File extension validation
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                throw new RuntimeException("Only .xlsx Excel files are allowed");
            }

            if (file.isEmpty()) {
                throw new RuntimeException("Excel file is empty");
            }

            Pattern namePattern = Pattern.compile("^[A-Za-z]+$");
            Pattern lastPattern= Pattern.compile("^[A-Za-z']+$");
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

                Sheet sheet = workbook.getSheetAt(0);

                // HEADER VALIDATION
                Row headerRow = sheet.getRow(0);

                if (headerRow == null) {
                    throw new RuntimeException("Excel header row is missing");
                }

                if (headerRow.getCell(0) == null ||
                        headerRow.getCell(1) == null ||
                        headerRow.getCell(2) == null ||
                        headerRow.getCell(3) == null) {

                    throw new RuntimeException("Invalid Excel header format");
                }

                String header1 = headerRow.getCell(0).getStringCellValue().trim();
                String header2 = headerRow.getCell(1).getStringCellValue().trim();
                String header3 = headerRow.getCell(2).getStringCellValue().trim();
                String header4 = headerRow.getCell(3).getStringCellValue().trim();

                if (!header1.equalsIgnoreCase("Name") ||
                        !header2.equalsIgnoreCase("LastName") ||
                        !header3.equalsIgnoreCase("Email") ||
                        !header4.equalsIgnoreCase("Age")) {

                    throw new RuntimeException(
                            "Invalid Excel format. Required headers: Name, LastName, Email, Age"
                    );
                }

                if (sheet.getPhysicalNumberOfRows() <= 1) {
                    throw new RuntimeException("Excel contains only header, no data");
                }

                Set <String> excelEmails= new HashSet<>();

                Set<String> existingEmails=userExcelRepo.findAll()
                        .stream()
                        .map(ExcelUser::getEmail)
                        .collect(Collectors.toSet());

                List<ExcelUser> users=new ArrayList<>();

                // READ DATA ROWS
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    Row row = sheet.getRow(i);

                    if (row == null) {
                        log.warn("Row " + (i + 1) + " is empty");
                        continue;
                    }

                    // NAME
                    Cell nameCell = row.getCell(0);
                    if (nameCell == null || nameCell.getCellType() != CellType.STRING) {
                        throw new RuntimeException("Invalid Name at row " + (i + 1));
                    }

                    String name = nameCell.getStringCellValue().trim();

                    if (!namePattern.matcher(name).matches()) {
                        throw new RuntimeException(
                                "Name must contain only alphabets at row " + (i + 1)
                        );
                    }

                    // LASTNAME
                    Cell lastCell = row.getCell(1);
                    if (lastCell == null || lastCell.getCellType() != CellType.STRING) {
                        throw new RuntimeException("Invalid Lastname at row " + (i + 1));
                    }

                    String lastname = lastCell.getStringCellValue().trim();

                    if (!lastPattern.matcher(lastname).matches()) {
                        throw new RuntimeException(
                                "Lastname must contain only alphabets at row " + (i + 1)
                        );
                    }

                    // EMAIL
                    Cell emailCell = row.getCell(2);
                    if (emailCell == null || emailCell.getCellType() != CellType.STRING) {
                        throw new RuntimeException("Invalid Email at row " + (i + 1));
                    }

                    String email = emailCell.getStringCellValue().trim();

                    if (!emailPattern.matcher(email).matches()) {
                        throw new RuntimeException(
                                "Invalid Email format at row " + (i + 1)
                        );
                    }

                    // duplicate inside excel
                    if (excelEmails.contains(email)) {
                        throw new RuntimeException("Duplicate email in Excel file at row " + (i + 1));
                    }

                    excelEmails.add(email);

                   // duplicate in database
                    if(existingEmails.contains(email)){
                        log.warn("Duplicate email found in database, skipping row {} : {}", (i+1), email);
                        continue;
                    }

                    // AGE
                    Cell ageCell = row.getCell(3);

                    if (ageCell == null) {
                        throw new RuntimeException("Age missing at row " + (i + 1));
                    }

                    if (ageCell.getCellType() != CellType.NUMERIC) {
                        throw new RuntimeException(
                                "Age must be numeric at row " + (i + 1)
                        );
                    }

                    int age = (int) ageCell.getNumericCellValue();

                    if (age < 15 || age > 80) {
                        throw new RuntimeException(
                                "Age must be between 15 and 80 at row " + (i + 1)
                        );
                    }

                    // SAVE USER
                    ExcelUser user = new ExcelUser();
                    user.setName(name);
                    user.setLastname(lastname);
                    user.setEmail(email);
                    user.setAge(age);

                    users.add(user);
                    existingEmails.add(email);

                    log.info("User added to batch list: {}", email);
                }
                userExcelRepo.saveAll(users);
                log.info("Total users saved from excel: {}",users.size());
                log.info("Excel upload completed successfully");
            }
        }
    }