package org.glygen.glcan.viewer.json.converter;

import java.io.IOException;

import org.glygen.glcan.viewer.json.converter.io.ExcelReader;
import org.glygen.glcan.viewer.json.converter.io.JSONWriter;
import org.glygen.glcan.viewer.json.converter.om.Error;
import org.glygen.glcan.viewer.json.converter.om.FileReadingResult;

public class App
{

    public static void main(String[] a_args)
    {
        String t_dataFolderPath = "./data/";
        ExcelReader t_readerExcel = new ExcelReader();
        JSONWriter t_writerJSON = new JSONWriter();
        try
        {
            FileReadingResult t_results = t_readerExcel
                    .readFile(t_dataFolderPath + "/glycan-viewer-residues.xlsx");
            if (t_results.getErrors().size() != 0)
            {
                System.out.println("There have been errors while reading the Excel file:");
                for (Error t_error : t_results.getErrors())
                {
                    System.out.println(
                            "\tLine " + t_error.getRow().toString() + ": " + t_error.getMessage());
                }
            }
            t_writerJSON.writeFile(t_dataFolderPath + "/glycan-viewer-residues.json",
                    t_results.getRecords());
        }
        catch (IOException e)
        {
            System.err.println("Unable to process file: " + e.getMessage());
        }
        System.out.println("Finished!");
    }
}
