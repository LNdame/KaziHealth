package mandela.cct.ansteph.kazihealth.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.IOException;

public class PdfCreatorUtils {

    public static BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
    public static BaseColor blackAccent = new BaseColor(0, 0, 0, 255);
    public static float bodyFontSize = 12.0f;
    public static float valueFontSize = 14.0f;
    public static float valueCellSize = 30.0f;


    static Paragraph headerParagraph(String title) throws IOException, DocumentException {
        BaseFont  baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
        Font headerFont  = new Font(baseFont, 26.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk headerTitleChunk = new Chunk(title, headerFont);
        headerTitleChunk.setUnderline(0.1f,-2.0f);
        Paragraph headerParagraph = new Paragraph(headerTitleChunk);
        headerParagraph.setAlignment(Element.ALIGN_CENTER);
        headerParagraph.setSpacingAfter(40.0f);
        return headerParagraph;
    }

    static Paragraph subHeaderParagraph(String title) throws IOException, DocumentException {
        BaseFont  baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
        Font headerFont  = new Font(baseFont, 20.0f, Font.NORMAL, BaseColor.BLACK);
        Chunk headerTitleChunk = new Chunk(title, headerFont);
        Paragraph headerParagraph = new Paragraph(headerTitleChunk);
        headerParagraph.setAlignment(Element.ALIGN_LEFT);
        headerParagraph.setSpacingAfter(20.0f);
        return headerParagraph;
    }

    static Paragraph bodyParagraph(String body, float fontSize, BaseColor color ) throws IOException, DocumentException {
        BaseFont  baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
        Font headerFont  = new Font(baseFont, fontSize, Font.NORMAL, color);
        Chunk headerTitleChunk = new Chunk(body, headerFont);
        return new Paragraph(headerTitleChunk);
    }

    static Chunk bodyLineSeparator(){
        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        return new Chunk(lineSeparator);
    }

    static PdfPCell tableBodyCell(String body, float fontSize, BaseColor color)throws IOException, DocumentException{
        BaseFont  baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
        Font cellFont  = new Font(baseFont, fontSize, Font.NORMAL, color);
        PdfPCell cell = new PdfPCell(new Phrase(body,cellFont));
        cell.setFixedHeight(valueCellSize);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(Rectangle.TOP);
        cell.setBorder(Rectangle.BOTTOM);
        return cell;
    }

    static PdfPCell tableHeaderCell(String body, float fontSize, BaseColor color)throws IOException, DocumentException{
        BaseFont  baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
        Font cellFont  = new Font(baseFont, fontSize, Font.BOLD, color);
        PdfPCell cell = new PdfPCell(new Phrase(body,cellFont));
        cell.setFixedHeight(valueCellSize);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(Rectangle.TOP);
        cell.setBorder(Rectangle.BOTTOM);
        return cell;
    }

}
