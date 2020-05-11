package mandela.cct.ansteph.kazihealth.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.Calendar;

public class PageFooter extends PdfPageEventHelper {
    Font ffont =new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);

    public PageFooter()  {
    }

    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Calendar calendar = Calendar.getInstance();
        String date = calendar.getTime().toString();
        Phrase header = new Phrase(date, ffont);
        Phrase footer = new Phrase("KaziBantu Healthy Schools for Healthy Communities || Report generated via the KaziHealth app", ffont);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                header,
                 2 + document.leftMargin(),
                document.top() + 10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }
}
