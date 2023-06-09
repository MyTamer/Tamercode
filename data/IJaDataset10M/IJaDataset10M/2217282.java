package com.lowagie.examples.objects.tables;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * A very simple PdfPTable example using getDefaultCell().
 */
public class DefaultCell {

    /**
	 * Demonstrates the use of getDefaultCell().
	 * 
	 * @param args
	 *            no arguments needed
	 */
    public static void main(String[] args) {
        System.out.println("DefaultCell");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("DefaultCell.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(3);
            PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
            cell.setColspan(3);
            table.addCell(cell);
            table.addCell("1.1");
            table.addCell("2.1");
            table.addCell("3.1");
            table.getDefaultCell().setGrayFill(0.8f);
            table.addCell("1.2");
            table.addCell("2.2");
            table.addCell("3.2");
            table.getDefaultCell().setGrayFill(0f);
            table.getDefaultCell().setBorderColor(new Color(255, 0, 0));
            table.addCell("cell test1");
            table.getDefaultCell().setColspan(2);
            table.getDefaultCell().setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
            table.addCell("cell test2");
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
