package mandela.cct.ansteph.kazihealth.view.tip;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.mpt.android.stv.callback.OnTextClick;

import mandela.cct.ansteph.kazihealth.R;
import mandela.cct.ansteph.kazihealth.model.TipItem;

public class TipDetail extends AppCompatActivity {

    TextView txtTipTitle , txtTipContent;
    TipItem mCurrentTipItem;

    private String[] tipContents;

    SpannableTextView stvTipDetail;

    FrameLayout frlTopPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        frlTopPanel =(FrameLayout) findViewById(R.id.topPanel);

        txtTipTitle =(TextView) findViewById(R.id.txtTipTitle);
        txtTipContent =(TextView) findViewById(R.id.txtTipContent);

        stvTipDetail = findViewById(R.id.stvTip);

        tipContents = loadTipContent();

        Bundle b = getIntent().getExtras();

        if(b!=null){

            mCurrentTipItem = (TipItem) b.getSerializable("Tip");
            txtTipTitle.setText(getString(R.string.tip_title, mCurrentTipItem.getName()));

            txtTipContent.setText(tipContents[Integer.parseInt(mCurrentTipItem.getPosition())-1]);
            int position =Integer.parseInt(mCurrentTipItem.getPosition());
            switch (position)
            {
                case 1:buildBloodPressure(); changeImage(1) ; break;
                case 2:buildWeight();changeImage(2)      ;break;
                case 3:buildCholesterol();changeImage(3) ;break;
                case 4:buildBloodGlucose();changeImage(4) ;break;
            }


        }



    }


    void changeImage( int position)
    {

        switch (position)
        {
            case 1:frlTopPanel.setBackground(getResources().getDrawable(R.drawable.blood_pressure));;break;
            case 2:frlTopPanel.setBackground(getResources().getDrawable(R.drawable.weight_manag));;break;
            case 3:frlTopPanel.setBackground(getResources().getDrawable(R.drawable.blood_lipid));;break;
            case 4:frlTopPanel.setBackground(getResources().getDrawable(R.drawable.blood_glucose));;break;
        }
    }


    public void buildWeight()
    {
        String[] blocks = getResources().getStringArray(R.array.tip_weight_block);

        //block title 1
        stvTipDetail.addSlice(new Slice.Builder("Eat a healthy Balanced Diet:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 1
        stvTipDetail.addSlice(new Slice.Builder(blocks[0]).textSize(50).build());

        //block title 2
        stvTipDetail.addSlice(new Slice.Builder("Be Active:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 2
        stvTipDetail.addSlice(new Slice.Builder(blocks[1]).textSize(50).build());


        //block title 3
        stvTipDetail.addSlice(new Slice.Builder("Decrease or eliminate harmful substance use:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 3
        stvTipDetail.addSlice(new Slice.Builder(blocks[2]).textSize(50).build());
      //  stvTipDetail.addSlice(new Slice.Builder("\u25AA If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder(" If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder("decrease your smoking.\n").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).setOnTextClick(new OnTextClick() {
            @Override
            public void onTextClick(View view, Slice slice) {
                //add Hyperlink
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cdc.gov/tobacco/campaign/tips/quit-smoking/index.html"));
                startActivity(browserIntent);
            }
        }).underline().build());

        stvTipDetail.addSlice(new Slice.Builder(" until you can stop. \n\u25AA If you do not smoke, do not ever start.\n\n").textSize(50).build());



        //block title 4
        stvTipDetail.addSlice(new Slice.Builder("Reduce Stress:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 4
        stvTipDetail.addSlice(new Slice.Builder(blocks[3]).textSize(50).build());


       // stvTipDetail.addSlice(new Slice.Builder("\u25AA Practice ways to destress today. Refer to the ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder(" Practice ways to destress today. Refer to the ").textSize(50).build());

        stvTipDetail.addSlice(new Slice.Builder("KaziBantu").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).style(Typeface.ITALIC).underline().build());
        stvTipDetail.addSlice(new Slice.Builder(" Stress Manual.\n").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).setOnTextClick(new OnTextClick() {
            @Override
            public void onTextClick(View view, Slice slice) {
                //add Hyperlink
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kazibantu.org"));
                startActivity(browserIntent);
            }
        }).underline().build());
        stvTipDetail.display();

    }

    public void buildCholesterol()
    {
        String[] blocks = getResources().getStringArray(R.array.tip_cholesterol_block);

        //block title 1
        stvTipDetail.addSlice(new Slice.Builder("Eat a Healthy Balanced Diet:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 1
        stvTipDetail.addSlice(new Slice.Builder(blocks[0]).textSize(50).build());

        //block title 2
        stvTipDetail.addSlice(new Slice.Builder("Be Active:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 2
        stvTipDetail.addSlice(new Slice.Builder(blocks[1]).textSize(50).build());


        //block title 3
        stvTipDetail.addSlice(new Slice.Builder("Lose Excess Weight:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 3
        stvTipDetail.addSlice(new Slice.Builder(blocks[2]).textSize(50).build());


        //block title 4
        stvTipDetail.addSlice(new Slice.Builder("Decrease or eliminate harmful substance use:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 4
        stvTipDetail.addSlice(new Slice.Builder(blocks[3]).textSize(50).build());
       // stvTipDetail.addSlice(new Slice.Builder("\u25AA If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder(" If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder("decrease your smoking.\n").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).setOnTextClick(new OnTextClick() {
            @Override
            public void onTextClick(View view, Slice slice) {
                //add Hyperlink
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cdc.gov/tobacco/campaign/tips/quit-smoking/index.html"));
                startActivity(browserIntent);
            }
        }).underline().build());

        stvTipDetail.addSlice(new Slice.Builder(" until you can stop.\n\n").textSize(50).build());



        //block title 5
        stvTipDetail.addSlice(new Slice.Builder("Reduce Stress:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 5
        stvTipDetail.addSlice(new Slice.Builder(blocks[4]).textSize(50).build());
        stvTipDetail.display();

    }


    public void buildBloodGlucose()
    {
        String[] blocks = getResources().getStringArray(R.array.tip_blood_glucose_block);

        //block title 1
        stvTipDetail.addSlice(new Slice.Builder("Eat a Healthy Balanced Diet:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 1
        stvTipDetail.addSlice(new Slice.Builder(blocks[0]).textSize(50).build());

        //block title 2
        stvTipDetail.addSlice(new Slice.Builder("Be Active:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 2
        stvTipDetail.addSlice(new Slice.Builder(blocks[1]).textSize(50).build());


        //block title 3
        stvTipDetail.addSlice(new Slice.Builder("Decrease or eliminate harmful substance use::\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 3
        stvTipDetail.addSlice(new Slice.Builder(blocks[2]).textSize(50).build());
       // stvTipDetail.addSlice(new Slice.Builder("\u25AA If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder(" If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder("decrease your smoking.\n").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).setOnTextClick(new OnTextClick() {
            @Override
            public void onTextClick(View view, Slice slice) {
                //add Hyperlink
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cdc.gov/tobacco/campaign/tips/quit-smoking/index.html"));
                startActivity(browserIntent);
            }
        }).underline().build());

        stvTipDetail.addSlice(new Slice.Builder(" until you can stop.\n\n").textSize(50).build());



        //block title 4
        stvTipDetail.addSlice(new Slice.Builder("Reduce Stress:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 4
        stvTipDetail.addSlice(new Slice.Builder(blocks[3]).textSize(50).build());


        //block title 4
        stvTipDetail.addSlice(new Slice.Builder("Behavioural Intervention:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 4
        stvTipDetail.addSlice(new Slice.Builder(blocks[4]).textSize(50).build());

        stvTipDetail.display();

    }


    public void buildBloodPressure()
    {
        String[] blocks = getResources().getStringArray(R.array.tip_blood_pressure_block);

        //block title 1
        stvTipDetail.addSlice(new Slice.Builder("Eat a Healthy Balanced Diet:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 1
        stvTipDetail.addSlice(new Slice.Builder(blocks[0]).textSize(50).build());

        //block title 2
        stvTipDetail.addSlice(new Slice.Builder("Be Active:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 2
        stvTipDetail.addSlice(new Slice.Builder(blocks[1]).textSize(50).build());


        //block title 3
        stvTipDetail.addSlice(new Slice.Builder("Lose Excess Weight:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 3
        stvTipDetail.addSlice(new Slice.Builder(blocks[2]).textSize(50).build());


        //block title 4
        stvTipDetail.addSlice(new Slice.Builder("Quit Smoking:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 4
        stvTipDetail.addSlice(new Slice.Builder(blocks[3]).textSize(50).build());

        //stvTipDetail.addSlice(new Slice.Builder("\u25AA If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder(" If you smoke, find ways to  ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder("decrease your smoking.\n").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).setOnTextClick(new OnTextClick() {
            @Override
            public void onTextClick(View view, Slice slice) {
                //add Hyperlink
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cdc.gov/tobacco/campaign/tips/quit-smoking/index.html"));
                startActivity(browserIntent);
            }
        }).underline().build());

      //  stvTipDetail.addSlice(new Slice.Builder(" until you can stop. \n\u25AA If you do not smoke, do not ever start.\n\n").textSize(50).build());

        stvTipDetail.addSlice(new Slice.Builder(" until you can stop. \n If you do not smoke, do not ever start.\n\n").textSize(50).build());


        //block title 5
        stvTipDetail.addSlice(new Slice.Builder("Medicate, as prescribed by your doctor:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 5
        stvTipDetail.addSlice(new Slice.Builder(blocks[4]).textSize(50).build());

        //block title 6
        stvTipDetail.addSlice(new Slice.Builder("Reduce Stress:\n\n").textSize(60).style(Typeface.BOLD).build());

        //block 6
        stvTipDetail.addSlice(new Slice.Builder(blocks[5]).textSize(50).build());

       // stvTipDetail.addSlice(new Slice.Builder("\u25AA Practice ways to destress today. Refer to the ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder(" Practice ways to destress today. Refer to the ").textSize(50).build());
        stvTipDetail.addSlice(new Slice.Builder("KaziBantu").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).style(Typeface.ITALIC).underline().build());
        stvTipDetail.addSlice(new Slice.Builder(" Stress Manual.\n").textColor(getResources().getColor(R.color.hyperlink)).textSize(50).setOnTextClick(new OnTextClick() {
            @Override
            public void onTextClick(View view, Slice slice) {
                //add Hyperlink
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kazibantu.org"));
                startActivity(browserIntent);
            }
        }).underline().build());
        stvTipDetail.display();

    }
    public String[] loadTipContent() {
        return getResources().getStringArray(R.array.tip);
    }

}
