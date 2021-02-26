package ua.vedroid.mooncalendar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String SERVER_URL = "http://93.72.95.145:7878";
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_BDAY = "Day";
    public static final String APP_PREFERENCES_BMONTH = "Month";
    public static final String APP_PREFERENCES_BYEAR = "Year";
    public static final String APP_PREFERENCES_pID = "pID";
    public static final String APP_PREFERENCES_DIsSet = "DIsSet";
    private static final Calendar calendar = Calendar.getInstance();
    private static final int currentYear = calendar.get(Calendar.YEAR);

    public static SharedPreferences mSettings;
    static boolean bDayIsSet = false;
    static boolean payID = false;
    private static String result = "Null";
    private static int day;
    private static int lunarDay;

    private TextView textView;
    private ImageView imageView;
    private URLCon uc;
    private boolean firstCon = true;
    private int[] imgArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_pID)) {
            payID = mSettings.getBoolean(APP_PREFERENCES_pID, payID);
        }
        if (mSettings.contains(APP_PREFERENCES_DIsSet)) {
            bDayIsSet = mSettings.getBoolean(APP_PREFERENCES_DIsSet, bDayIsSet);
        }

        startService(new Intent(this,
                ServiceNewPrediction.class));

        initTextView();
        initImage();
    }

    private void initTextView() {
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(R.string.text_wait);
        initThread();
        setTextView();
    }

    private void initImage() {
        imgArr = new int[]{
                R.drawable.mp_1, R.drawable.mp_2,
                R.drawable.mp_3, R.drawable.mp_4,
                R.drawable.mp_5, R.drawable.mp_6,
                R.drawable.mp_7, R.drawable.mp_8,
                R.drawable.mp_9, R.drawable.mp_10,
                R.drawable.mp_11, R.drawable.mp_12,
                R.drawable.mp_13, R.drawable.mp_14,
                R.drawable.mp_15, R.drawable.mp_16,
                R.drawable.mp_17, R.drawable.mp_18,
                R.drawable.mp_19, R.drawable.mp_20,
                R.drawable.mp_21, R.drawable.mp_22,
                R.drawable.mp_23, R.drawable.mp_24,
                R.drawable.mp_25, R.drawable.mp_26,
                R.drawable.mp_27, R.drawable.mp_28,
                R.drawable.mp_29, R.drawable.mp_30
        };
    }

    private void setTextView() {
        textView.setText(Html.fromHtml(result));
    }

    private void setImageView() {
        imageView.setImageResource(imgArr[lunarDay - 1]);
    }

    private void initThread() {
        if (isOnline()) {
            uc = new URLCon(SERVER_URL, day, payID);
            uc.start();
            waitThread(uc);
        } else {
            result = getResources().getString(R.string.text_no_internet);
        }
    }

    private void refThread() {
        if (uc != null) waitThread(uc);
        initThread();
    }

    private void waitThread(Thread thread) {
        if (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onClickRef(View view) {
        textView.setText(R.string.text_wait);
        day = 0;
        if (firstCon) {
            firstCon = false;
            initThread();
        } else refThread();
        setTextView();
        setImageView();
    }

    public void onClickNext(View view) {
        textView.setText(R.string.text_wait);
        if (payID) {
            day++;
            refThread();
            setTextView();
            setImageView();
        } else {
            Intent intent = new Intent(this, PayInfoActivity.class);
            startActivity(intent);
        }
    }

    public void onClickPre(View view) {
        textView.setText(R.string.text_wait);
        if (payID) {
            day--;
            refThread();
            setTextView();
            setImageView();
        } else {
            Intent intent = new Intent(this, PayInfoActivity.class);
            startActivity(intent);
        }
    }

    public void onClickPP(View view) {
        textView.setText(R.string.text_wait);
        if (payID) {
            Intent intent;
            if (bDayIsSet) {
                intent = new Intent(this, PersonalPredictionActivity.class);
            } else {
                intent = new Intent(this, EnterBDayActivity.class);
            }
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PayInfoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static int getCurrentYear() {
        return currentYear;
    }

    public static void setResult(String inputStr) {
        result = inputStr;
    }

    public static void setLunarDay(String inputFirstLine) {
        lunarDay = Integer.parseInt(inputFirstLine);
    }
}
