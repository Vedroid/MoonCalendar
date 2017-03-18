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
    private String inputStr;            //Текст предсказания
    private String inputFirstLine;      //Первая строка (Лунный день)
    private int urlId;

    URLCon(String url) {
        this.sURL = url + "/test";
        urlId = 0;
    }

    URLCon(String url, int d, boolean b) {
        if (b) {
            this.sURL = url + "/day?d=" + d;
        } else {
            this.sURL = url + "/day?d=0";
        }
        urlId = 1;
    }

    URLCon(String url, int d, int m, int y) {
        this.sURL = url + "/pay?d=" + d + "&m=" + m + "&y=" + y;
        urlId = 2;
    }

    @Override
    public void run() {                                                 // Смотри в самый низ!!!!!!!!!!
        boolean err = false;
        try {
            URL url = new URL(sURL);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(3 * 1000);                  // Set timeout to 3 seconds
            urlConnection.connect();

            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            inputStr = "";
            String inputLine;

            inputFirstLine = in.readLine();
            while ((inputLine = in.readLine()) != null) {               // Считывание по строке
                inputStr = inputStr + inputLine + "<br>";               // Запись строк в одну
            }
        } catch (MalformedURLException e) {
            err = true;
            inputStr = "MalformedURLException " + e.toString();
        } catch (SocketTimeoutException e) {
            err = true;
            inputStr = "\nНе удалось подключится к серверу\n" +
                    "Повторите попытку позже.\n\n" + e.toString();              // Убрать e.туСтринг!!!!!!!!!!!!!!!!!!!
        } catch (IOException e) {
            err = true;
            inputStr = "\nНе удалось подключится к серверу\n" +
                    "Повторите попытку позже.\n\nIOException " + e.toString();
        } catch (Exception e) {
            err = true;
            inputStr = "Exception " + e.toString();
        } finally {
            switch (urlId) {                                            // Отправка результата
                case 0:
                    if (err) inputFirstLine = "16";
                    ServiceNewPrediction.setLunarDay(inputFirstLine);
                    break;
                case 1:
                    MainActivity.setResult(inputStr);
                    if (err) inputFirstLine = "16";
                    MainActivity.setLunarDay(inputFirstLine);
                    break;
                case 2:
                    PersonalPredictionActivity.setResult(inputFirstLine + inputStr);
                    break;
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