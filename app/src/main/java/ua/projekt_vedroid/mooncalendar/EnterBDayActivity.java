package ua.projekt_vedroid.mooncalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class EnterBDayActivity extends AppCompatActivity {

    static GregorianCalendar calendar = new GregorianCalendar();

    private NumberPicker numberPickerD;
    private NumberPicker numberPickerM;
    private NumberPicker numberPickerY;

    private int d;
    private int m;
    private int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_bday);

        numberPickerD = (NumberPicker) findViewById(R.id.numberPickerD);
        numberPickerD.setMinValue(1);
        numberPickerD.setMaxValue(31);

        numberPickerM = (NumberPicker) findViewById(R.id.numberPickerM);
        numberPickerM.setMinValue(1);
        numberPickerM.setMaxValue(12);

        numberPickerY = (NumberPicker) findViewById(R.id.numberPickerY);
        numberPickerY.setMinValue(MainActivity.getCurrentYear() - 100);
        numberPickerY.setMaxValue(MainActivity.getCurrentYear());

        setDividerColor(numberPickerD, R.color.colorDivider);
        setDividerColor(numberPickerM, R.color.colorDivider);
        setDividerColor(numberPickerY, R.color.colorDivider);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void onClickNext(View view) {
        d = numberPickerD.getValue();
        m = numberPickerM.getValue();
        y = numberPickerY.getValue();

        if ((m == 4 || m == 6 || m == 9 || m == 11) && d == 31) {
            Toast.makeText(
                    EnterBDayActivity.this, getResources().getString(R.string.text_invalid_date),
                    Toast.LENGTH_LONG).show();

        } else if (!calendar.isLeapYear(y)) {
            if (m == 2 && (d == 31 || d == 30 || d == 29)) {
                Toast.makeText(
                        EnterBDayActivity.this, getResources().getString(R.string.text_invalid_date),
                        Toast.LENGTH_LONG)
                        .show();
            } else next();
        } else if (m == 2 && (d == 31 || d == 30)) {
            Toast.makeText(
                    EnterBDayActivity.this, getResources().getString(R.string.text_invalid_date),
                    Toast.LENGTH_LONG).show();
        } else {
            next();
        }
    }

    private void next() {
        Intent intent = new Intent(this, PersonalPredictionActivity.class);
        startActivity(intent);

        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        editor.putInt(MainActivity.APP_PREFERENCES_BDAY, d);
        editor.putInt(MainActivity.APP_PREFERENCES_BMONTH, m);
        editor.putInt(MainActivity.APP_PREFERENCES_BYEAR, y);
        editor.putBoolean(MainActivity.APP_PREFERENCES_DIsSet, true);
        editor.apply();
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException | IllegalAccessException | Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
