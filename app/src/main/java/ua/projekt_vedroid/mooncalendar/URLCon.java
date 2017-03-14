package ua.projekt_vedroid.mooncalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

class URLCon extends Thread {

    private BufferedReader in = null;
    private String sURL = "";
    private String inputStr;
    private boolean pay = false;

    URLCon(String url, int d, boolean b) {
        if (b) {
            this.sURL = url + "/day?d=" + d;
        } else {
            this.sURL = url + "/day?d=0";
        }
    }

    URLCon(String url, int d, int m, int y) {
        this.sURL = url + "/pay?d=" + d + "&m=" + m + "&y=" + y;
        pay = true;
    }

    @Override
    public void run() {                                                 // Смотри в самый низ!!!!!!!!!!
        try {
            URL url = new URL(sURL);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(3 * 1000);                  // Set timeout to 3 seconds
            urlConnection.connect();

            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            inputStr = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {               // Считывание по строке
                inputStr = inputStr + inputLine + "<br>";               // Запись строк в одну
            }
        } catch (MalformedURLException e) {
            inputStr = "MalformedURLException " + e.toString();
        } catch (SocketTimeoutException e) {
            inputStr = "\nНе удалось подключится к серверу\n" +
                    "Повторите попытку позже.\n\n" + e.toString();              // Убрать e.туСтринг!!!!!!!!!!!!!!!!!!!
        } catch (IOException e) {
            inputStr = "IOException " + e.toString();
        } catch (Exception e) {
            inputStr = "Exception " + e.toString();
        } finally {
            if (pay) {                                                  // Отправка результата
                PersonalPredictionActivity.setResult(inputStr);
            } else {
                MainActivity.setResult(inputStr);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}