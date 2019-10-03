package mandela.cct.ansteph.kazihealth.view.register;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import mandela.cct.ansteph.kazihealth.R;

/**
 * Created by loicstephan on 2018/06/22.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Use the current date as default date in picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month,day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        TextView mDOB = (TextView) getActivity().findViewById(R.id.editDob);

        String monthSt, day;
        if ((month + 1) < 10) {
            monthSt = "0" + String.valueOf(month + 1);
        } else {
            monthSt = String.valueOf(month + 1);
        }

        if (dayOfMonth < 10) {
            day = "0" + String.valueOf(dayOfMonth);
        } else {
            day = String.valueOf(dayOfMonth);
        }


            mDOB.setText(String.valueOf(year) + "-" + monthSt + "-" + day);


    }
    }
