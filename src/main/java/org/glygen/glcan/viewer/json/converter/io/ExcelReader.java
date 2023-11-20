package org.glygen.glcan.viewer.json.converter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glygen.glcan.viewer.json.converter.om.Error;
import org.glygen.glcan.viewer.json.converter.om.FileReadingResult;
import org.glygen.glcan.viewer.json.converter.om.Record;

public class ExcelReader
{
    private Record m_lastTopLevel = null;

    public FileReadingResult readFile(String t_fileNamePath) throws IOException
    {
        FileInputStream t_file = new FileInputStream(new File(t_fileNamePath));

        // Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook t_workbook = new XSSFWorkbook(t_file);
        t_file.close();
        // Get first/desired sheet from the workbook
        for (int i = 0; i < t_workbook.getNumberOfSheets(); i++)
        {
            XSSFSheet t_sheet = t_workbook.getSheetAt(i);
            // Process your sheet here.
            if (t_sheet.getSheetName().equals("Residues"))
            {
                FileReadingResult t_result = this.readData(t_sheet);
                t_workbook.close();
                return t_result;
            }
        }
        t_workbook.close();
        throw new IOException("Unable to find sheet 'Resisudes'.");
    }

    private FileReadingResult readData(XSSFSheet a_sheet)
    {
        Integer t_counterRow = 0;
        FileReadingResult t_result = new FileReadingResult();
        // Iterate through each rows one by one
        Iterator<Row> rowIterator = a_sheet.iterator();
        while (rowIterator.hasNext())
        {
            t_counterRow++;
            Row t_row = rowIterator.next();
            if (t_counterRow > 1)
            {
                if (t_row.getFirstCellNum() != -1)
                {
                    this.processRow(t_row, t_result, t_counterRow);
                }
            }
        }
        return t_result;
    }

    private void processRow(Row a_row, FileReadingResult a_result, Integer a_counterRow)
    {
        Record t_record = new Record();
        boolean t_topLevel = false;
        // For each row, iterate through all the columns
        for (int t_counterColumn = 0; t_counterColumn < 8; t_counterColumn++)
        {
            Cell t_cell = a_row.getCell(t_counterColumn);
            if (t_counterColumn == 0)
            {
                String t_valueString = this.sanitizeString(t_cell);
                // top level
                if (t_valueString != null)
                {
                    t_record.setIupacSymbol(t_valueString);
                    t_topLevel = true;
                    this.m_lastTopLevel = null;
                }
            }
            else if (t_counterColumn == 1)
            {
                String t_valueString = this.sanitizeString(t_cell);
                // child level
                if (t_valueString != null)
                {
                    if (t_record.getIupacSymbol() != null)
                    {
                        Error t_error = new Error();
                        t_error.setMessage("Record has top level and child level names");
                        t_error.setRow(a_counterRow);
                        a_result.getErrors().add(t_error);
                        return;
                    }
                    t_record.setIupacSymbol(t_valueString);
                }
                else
                {
                    if (t_record.getIupacSymbol() == null)
                    {
                        Error t_error = new Error();
                        t_error.setMessage("Record has neither top level and child level names");
                        t_error.setRow(a_counterRow);
                        a_result.getErrors().add(t_error);
                        return;
                    }
                }
            }
            else if (t_counterColumn == 2)
            {
                String t_valueString = this.sanitizeString(t_cell);
                // name
                if (t_valueString == null)
                {
                    Error t_error = new Error();
                    t_error.setMessage("Missing name column value");
                    t_error.setRow(a_counterRow);
                    a_result.getErrors().add(t_error);
                    return;
                }
                else
                {
                    t_record.setName(t_valueString);
                }
            }
            else if (t_counterColumn == 3)
            {
                String t_valueString = this.sanitizeString(t_cell);
                // ignore
                if (t_valueString == null)
                {
                    Error t_error = new Error();
                    t_error.setMessage("Missing ignore column value");
                    t_error.setRow(a_counterRow);
                    a_result.getErrors().add(t_error);
                    return;
                }
                else
                {
                    if (t_valueString.equalsIgnoreCase("yes"))
                    {
                        t_record.setIgnore(true);
                    }
                    else if (t_valueString.equalsIgnoreCase("no"))
                    {
                        t_record.setIgnore(false);
                    }
                    else
                    {
                        Error t_error = new Error();
                        t_error.setMessage("Invalid value for ignore column");
                        t_error.setRow(a_counterRow);
                        a_result.getErrors().add(t_error);
                        return;
                    }
                }
            }
            else if (t_counterColumn == 4)
            {
                String t_valueString = this.sanitizeString(t_cell);
                // image
                t_record.setImage(t_valueString);
            }
            else if (t_counterColumn == 5)
            {
                String t_valueString = this.sanitizeString(t_cell);
                // URL
                t_record.setUrl(t_valueString);
            }
            else if (t_counterColumn == 6)
            {
                Integer t_valueInt = this.sanitizeInteger(t_cell);
                // URL
                t_record.setPubChemCompound(t_valueInt);
            }
            else if (t_counterColumn == 7)
            {
                String t_valueString = this.sanitizeString(t_cell);
                // URL
                t_record.setDescription(t_valueString);
            }
        }
        if (t_topLevel)
        {
            a_result.getRecords().add(t_record);
            this.m_lastTopLevel = t_record;
        }
        else
        {
            if (this.m_lastTopLevel == null)
            {
                Error t_error = new Error();
                t_error.setMessage("Unable to add child level due to error in top level record");
                t_error.setRow(a_counterRow);
                a_result.getErrors().add(t_error);
                return;
            }
            List<Record> t_children = this.m_lastTopLevel.getChildren();
            if (t_children == null)
            {
                t_children = new ArrayList<>();
                this.m_lastTopLevel.setChildren(t_children);
            }
            t_children.add(t_record);
        }
    }

    private Integer sanitizeInteger(Cell a_cell)
    {
        if (a_cell == null)
        {
            return null;
        }
        Integer t_value = (int) a_cell.getNumericCellValue();
        return t_value;
    }

    private String sanitizeString(Cell a_cell)
    {
        if (a_cell == null)
        {
            return null;
        }
        String t_value = a_cell.getStringCellValue();
        if (t_value == null)
        {
            return null;
        }
        String t_string = t_value.trim();
        if (t_string.length() == 0)
        {
            return null;
        }
        return t_string;
    }
}
