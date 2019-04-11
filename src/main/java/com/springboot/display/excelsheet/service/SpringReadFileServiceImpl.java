package com.springboot.display.excelsheet.service;

import com.springboot.display.excelsheet.Repositiry.SpringReadFileRepositiry;
import com.springboot.display.excelsheet.model.User;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional

public class SpringReadFileServiceImpl {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @Autowired
    private SpringReadFileRepositiry springReadFileRepositiry;

    public List<User> findAll() {
        return (List<User>) springReadFileRepositiry.findAll();
    }

    public boolean saveDataFromUpload(MultipartFile file) throws Exception {
        boolean isFlag = false;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")) {
            isFlag = getReadDataFromExcel(file);
        }
        return isFlag;
    }

    private boolean getReadDataFromExcel(MultipartFile file) throws IOException {
        InputStream stream = file.getInputStream();
        LOGGER.info("Create Workbook");
        Workbook workbook = new XSSFWorkbook(stream);
        LOGGER.info("Get Sheet from Workbook");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next();
        while (rows.hasNext()) {
            Row row = rows.next();
            User user = new User();
            if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
                LOGGER.info("Get Firstname from Excel File");
                user.setFirstName(row.getCell(0).getStringCellValue());
            }
            if (row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
                LOGGER.info("Get Lirstname from Excel File");
                user.setLastName(row.getCell(1).getStringCellValue());
            }
            user.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
            springReadFileRepositiry.save(user);
            LOGGER.info("Users Details store in user DB table");
        }
        return true;
    }

    private Workbook getWorkBook(MultipartFile file) {
        Workbook workbook = null;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            if (extension.equalsIgnoreCase("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (extension.equalsIgnoreCase("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            LOGGER.finer("File Upload Successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error Occur in File Type");
        }
        return null;
    }
}
