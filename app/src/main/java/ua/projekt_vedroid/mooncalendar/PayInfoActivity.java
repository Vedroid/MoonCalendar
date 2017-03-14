package ua.projekt_vedroid.mooncalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class PayInfoActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay_info);
    }

    public void onClickBuy(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_dialog_title);
        builder.setMessage(R.string.alert_dialog_message);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.text_no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton(R.string.text_yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mock();             //Затычка
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void mock() {                   //Покупка
        SharedPreferences.Editor editor = MainActivity.mSettings.edit();
        editor.putBoolean(MainActivity.APP_PREFERENCES_pID, true);
        editor.apply();

        MainActivity.payID = true;

        intent = new Intent(this, EnterBDayActivity.class);
        startActivity(intent);                                            //Запуск EnterBDayActivity
        Toast.makeText(this, "Purchased.", Toast.LENGTH_LONG).show();
    }

    public void onClickBack(View view) {
        back();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);                                        //Запуск MainActivity
        super.onBackPressed();
    }
}
