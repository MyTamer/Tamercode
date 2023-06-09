package com.lowagie.examples.objects.tables.alternatives;

import java.awt.Color;
import java.io.FileOutputStream;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Shows how a table is split if it doesn't fit the page.
 */
public class RepeatingTable {

    /**
     * Shows how a table is split if it doesn't fit the page.
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        System.out.println("table splitting");
        Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("repeatingtable.pdf"));
            document.addAuthor("Alan Soukup");
            document.addSubject("This is the result of a Test.");
            document.open();
            Table datatable = new Table(10);
            int headerwidths[] = { 10, 24, 12, 12, 7, 7, 7, 7, 7, 7 };
            datatable.setWidths(headerwidths);
            datatable.setWidth(100);
            datatable.setPadding(3);
            Cell cell = new Cell(new Phrase("Administration -System Users Report", FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setLeading(30);
            cell.setColspan(10);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
            datatable.addCell(cell);
            datatable.getDefaultCell().setBorderWidth(2);
            datatable.getDefaultCell().setHorizontalAlignment(1);
            datatable.addCell("User Id");
            datatable.addCell("Name\nAddress");
            datatable.addCell("Company");
            datatable.addCell("Department");
            datatable.addCell("Admin");
            datatable.addCell("Data");
            datatable.addCell("Expl");
            datatable.addCell("Prod");
            datatable.addCell("Proj");
            datatable.addCell("Online");
            datatable.endHeaders();
            datatable.getDefaultCell().setBorderWidth(1);
            for (int i = 1; i < 30; i++) {
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                datatable.addCell("myUserId");
                datatable.addCell("Somebody with a very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very long long name");
                datatable.addCell("No Name Company");
                datatable.addCell("D" + i);
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                datatable.addCell("No");
                datatable.addCell("Yes");
                datatable.addCell("No");
                datatable.addCell("Yes");
                datatable.addCell("No");
                datatable.addCell("Yes");
            }
            document.add(new Paragraph("com.lowagie.text.Table - Cells split"));
            document.add(datatable);
            document.newPage();
            document.add(new Paragraph("com.lowagie.text.pdf.PdfPTable - Cells split\n\n"));
            datatable.setConvert2pdfptable(true);
            document.add(datatable);
            document.newPage();
            document.add(new Paragraph("com.lowagie.text.Table - Cells kept together"));
            datatable.setConvert2pdfptable(false);
            datatable.setCellsFitPage(true);
            document.add(datatable);
            document.newPage();
            document.add(new Paragraph("com.lowagie.text.pdf.PdfPTable - Cells kept together\n\n"));
            datatable.setConvert2pdfptable(true);
            document.add(datatable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }
}
