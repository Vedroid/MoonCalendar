package ua.projekt_vedroid.mooncalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //static final String URL = "http://192.168.1.102:7878";    //Vad Local
    //static final String URL = "http://93.72.59.160:7878";     //Vad
    //static final String URL = "http://192.168.0.100:7878";    //Boda Local
    static final String URL = "http://93.72.95.145:7878";    //Boda

    public static SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_BDAY = "Day";
    public static final String APP_PREFERENCES_BMONTH = "Month";
    public static final String APP_PREFERENCES_BYEAR = "Year";
    public static final String APP_PREFERENCES_pID = "pID";
    public static final String APP_PREFERENCES_DIsSet = "DIsSet";

    static boolean bDayIsSet = false;
    static boolean payID = false;

    private static Calendar calendar = Calendar.getInstance();
    private TextView textView;
    private ImageView imageView;
    private URLCon uc;

    private static String result = "Null";
    private static int currentYear = calendar.get(Calendar.YEAR);
    //private static int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    private static int day; //= currentDay;
    private static int lunarDay;
    private boolean firstCon = true;
    private int[] imgArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE); //Загрузка настроек
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

    private void initTextView() {    //Инициализация TextView
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

    private void initThread() {                             //Инициализация
        if (isOnline()) {
            uc = new URLCon(URL, day, payID);               //Создание потока
            uc.start();                                     //Запуск потока
            waitThread(uc);                                 //Ожидание завершения потока
        } else {
            result = getResources().getString(R.string.text_no_internet);
        }
    }

    private void refThread() {                              //Перезапуск
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            //if (day > 30) day = 1;
            refThread();
            setTextView();
            setImageView();
        } else {
            Intent intent = new Intent(this, PayInfoActivity.class);
            startActivity(intent);                                        //Запуск PayInfoActivity
        }
    }

    public void onClickPre(View view) {
        textView.setText(R.string.text_wait);
        if (payID) {
            day--;
            //if (day < 1) day = 30;
            refThread();
            setTextView();
            setImageView();
        } else {
            Intent intent = new Intent(this, PayInfoActivity.class);
            startActivity(intent);                                        //Запуск PayInfoActivity
        }
    }

    public void onClickPP(View view) {
        textView.setText(R.string.text_wait);
        if (payID) {
            if (bDayIsSet) {
                Intent intent = new Intent(this, PersonalPredictionActivity.class);
                startActivity(intent);                                    //Запуск PPActivity
            } else {
                Intent intent = new Intent(this, EnterBDayActivity.class);
                startActivity(intent);                                    //Запуск EnterBDayActivity
            }
        } else {
            Intent intent = new Intent(this, PayInfoActivity.class);
            startActivity(intent);                                        //Запуск PayInfoActivity
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
