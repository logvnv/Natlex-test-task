package com.zpsx.NatlexTestTask.service.helper;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.domain.ImportExportJob;
import com.zpsx.NatlexTestTask.domain.Section;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobStatus;
import com.zpsx.NatlexTestTask.domain.exception.TableParseException;
import com.zpsx.NatlexTestTask.repository.GeoClassRepo;
import com.zpsx.NatlexTestTask.repository.ImportExportJobRepo;
import com.zpsx.NatlexTestTask.repository.SectionRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class ImportExportHelper {

    @Value("${export-dir}")
    private String exportDir;

    @Autowired
    private ImportExportJobRepo importExportJobRepo;
    @Autowired
    private SectionRepo sectionRepo;
    @Autowired
    private GeoClassRepo geoClassRepo;
    @Autowired @Lazy
    private ImportExportHelper self;

    @Async
    public void importFile(InputStream fileInputStream, ImportExportJob importJob) {

        try {
            self.parseFile(fileInputStream);
            importJob.setStatus(ImportExportJobStatus.DONE);
        }
        catch (TableParseException e) {
            importJob.setMessage(e.getMessage());
            importJob.setStatus(ImportExportJobStatus.ERROR);
        }
        importExportJobRepo.save(importJob);
    }

    @Transactional(rollbackFor = TableParseException.class)
    public void parseFile(InputStream fileInputStream) throws TableParseException{
        int r = 0, c = 0;

        try(Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            r++; Row row = rows.next();// Table header
            Iterator<Cell> cells = row.cellIterator();

            int n_cols = 0; // Number of named columns
            while(cells.hasNext()){
                c++;
                if (cellIsEmpty(cells.next()))
                    break;
                n_cols++;
            }

            if(n_cols % 2 == 0){ // Must be 1 (Section name) + n * 2 (GC name and code)
                throw new TableParseException("Table must have odd number of columns", r, c);
            }

            List<String> sectionNames = new ArrayList<>();
            while(rows.hasNext()){
                r++; c=0; row = rows.next();
                cells = row.cellIterator();

                c++;Cell cell = cells.next();
                if(cellIsEmpty(cell))
                    break; // Blank section name assumed to be end of rows

                String sectionName = cell.getStringCellValue();
                if (sectionNames.contains(sectionName))
                    throw new TableParseException("Table contains duplicate section name", r, c);

                List<GeoClass> geoClasses = new ArrayList<>();
                for(int i = 0; i < n_cols-1; i+=2){
                    c++; String geoClassName = cells.next().getStringCellValue();
                    c++; String geoClassCode = cells.next().getStringCellValue();
                    if(!geoClassName.isEmpty() && !geoClassCode.isEmpty()){ // Deciding on GC
                        for (GeoClass gc: geoClasses) {
                            if (Objects.equals(gc.getName(), geoClassName)
                                    && Objects.equals(gc.getCode(), geoClassCode))
                                throw new TableParseException("Section must not have " +
                                        "duplicate geological classes", r, c);
                        }
                        GeoClass geoClass = geoClassRepo.findByCodeAndName(geoClassCode, geoClassName)
                                .orElse(new GeoClass(geoClassCode, geoClassName));
                        if(geoClass.getId() == 0)
                            geoClassRepo.save(geoClass);
                        geoClasses.add(geoClass);
                    }
                    else if (!(geoClassName.isEmpty() == geoClassCode.isEmpty())){
                        if (geoClassName.isEmpty()) c--;
                        throw new TableParseException("Corresponding geological class code" +
                                " and name must either be filled or not both", r, c);
                    }
                }
                Section section = sectionRepo.findByName(sectionName)
                        .orElse(new Section(sectionName));
                section.setGeoClasses(geoClasses);
                sectionRepo.save(section);
                sectionNames.add(sectionName);
            }
        } catch (IOException e) {
            throw new TableParseException("Unknown error occurred.", r, c);
        }
    }

    @Async
    public void createFile(ImportExportJob exportJob) {
        List<Section> sections = sectionRepo.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet 1");
            int row_n = 0;
            int cell_n = 0;
            int maxGeoClassesCount = 0;

            sheet.createRow(row_n++).createCell(0).setCellValue("Section name");

            for (Section section : sections) {
                Row row = sheet.createRow(row_n++);
                row.createCell(cell_n++).setCellValue(section.getName());

                for (GeoClass geoClass : section.getGeoClasses()) {
                    row.createCell(cell_n++).setCellValue(geoClass.getName());
                    row.createCell(cell_n++).setCellValue(geoClass.getCode());
                }
                cell_n =  (cell_n - 1) / 2;
                if (cell_n > maxGeoClassesCount) maxGeoClassesCount = cell_n;
                cell_n = 0;
            }

            Row row = sheet.getRow(0);
            for (int i = 0; i < maxGeoClassesCount; i += 1) {
                row.createCell(2 * i + 1).setCellValue(String.format("Class %d name", i+1));
                row.createCell(2 * (i + 1)).setCellValue(String.format("Class %d code", i+1));
            }

            File file = new File(exportDir + "/" + exportJob.getFileName());
            assert file.getParentFile().exists() || file.getParentFile().mkdirs();
            assert file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);

            exportJob.setStatus(ImportExportJobStatus.DONE);
        }
        catch (IOException e){
            exportJob.setMessage("Error creating export file.");
            exportJob.setStatus(ImportExportJobStatus.ERROR);
        }
        finally {
            importExportJobRepo.save(exportJob);
        }
    }

    private boolean cellIsEmpty(Cell cell){
        return cell == null || cell.getCellType() == CellType.BLANK ||
                (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
    }
}
