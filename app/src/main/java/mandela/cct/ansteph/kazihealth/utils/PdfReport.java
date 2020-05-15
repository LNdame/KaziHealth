package mandela.cct.ansteph.kazihealth.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.FileUriExposedException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import mandela.cct.ansteph.kazihealth.model.RiskItem;
import mandela.cct.ansteph.kazihealth.model.RiskProfileItem;
import mandela.cct.ansteph.kazihealth.model.User;

public class PdfReport {

    private static String TAG = PdfReport.class.getSimpleName();
    private final Context context;
    private final List<RiskProfileItem> riskProfileItems;
    private final List<RiskItem> riskItems;
    private final User user;

    public PdfReport(Context context, List<RiskProfileItem> riskProfileItems, List<RiskItem> riskItems, User user) {
        this.context = context;
        this.riskProfileItems = riskProfileItems;
        this.riskItems = riskItems;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void createPdf( String destination) {
        File file = new File(destination);

        if (file.exists()) {
            file.delete();
        }
        try {
            //creating the document
            Document document = new Document();
            //Location to save
           PdfWriter writer= PdfWriter.getInstance(document, new FileOutputStream(destination));
            PageFooter footer = new PageFooter();
            writer.setPageEvent(footer);
            document.open();
            //Document settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Kazihealth Report");
            document.addCreator("Kazibantu Project");

            //Title order Details
            //Adding title
            document.add(PdfCreatorUtils.headerParagraph("Individual Health Risk Report"));
            document.add(new Paragraph(""));
            document.add(new Paragraph(""));

            String khNum = !TextUtils.isEmpty(getUser().getKh_number())? String.format("(%s)",getUser().getKh_number()) :"";
            document.add(PdfCreatorUtils.subHeaderParagraph(String.format("%s %s", getUser().getName(), khNum)));
            document.add(new Paragraph(""));
            document.add(PdfCreatorUtils.bodyLineSeparator());
            document.add(new Paragraph(""));
            document.add(new Paragraph(""));

            //Creating a table
            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(new float[]{260.0f, 140.0f,150.0f});
            table.setLockedWidth(true);

           //header
            table.addCell(PdfCreatorUtils.tableHeaderCell("Indicator",PdfCreatorUtils.bodyFontSize,PdfCreatorUtils.blackAccent));
            table.addCell(PdfCreatorUtils.tableHeaderCell("Actual Measurement",PdfCreatorUtils.bodyFontSize,PdfCreatorUtils.blackAccent));
            table.addCell(PdfCreatorUtils.tableHeaderCell("Status",PdfCreatorUtils.bodyFontSize,PdfCreatorUtils.blackAccent));

            for (int i = 0; i < riskItems.size(); i++) {
                //value header
                table.addCell(PdfCreatorUtils.tableBodyCell(riskItems.get(i).getName(),PdfCreatorUtils.bodyFontSize,PdfCreatorUtils.colorAccent));
                //value
                table.addCell(PdfCreatorUtils.tableBodyCell( String.format("%s %s",riskProfileItems.get(i).getMeasurement(),riskItems.get(i).getUnit()),PdfCreatorUtils.valueFontSize,PdfCreatorUtils.blackAccent));
                table.addCell(PdfCreatorUtils.tableBodyCell( String.format("%s",riskProfileItems.get(i).getComment()),PdfCreatorUtils.bodyFontSize,PdfCreatorUtils.blackAccent));
            }

            document.add(table);
            document.close();

            Toast.makeText(context, "Created... :)", Toast.LENGTH_SHORT).show();

            //FileUtils.openFile(context, new File(destination));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                openPdf(file);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void openPdf(File  file){
        try {
        Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".provider", file  );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(fileUri !=null){
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri,"application/pdf");

        context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "No Application found", Toast.LENGTH_SHORT).show();
        }catch (FileUriExposedException ue){
            Log.e(TAG, ue.getMessage());
        }
    }
}




