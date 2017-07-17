package com.kappa.currencyconverter;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    public static Double currencyRate = 1.1405;

    public void loadCurrencyRate()
    {
        TextView euroTextview = (TextView) findViewById(R.id.euroTextview);
        TextView dollarTextview = (TextView) findViewById(R.id.dollarTextview);

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL("http://api.fixer.io/latest?symbols=EUR,USD");
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    String inputLine = in.readLine();

                    if(inputLine != null)
                    {
                        JSONObject json = new JSONObject(inputLine);
                        JSONObject rates = json.getJSONObject("rates");
                        Double rate = rates.getDouble("USD");

                        currencyRate = rate;

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable()
                        {
                            public void run()
                            {
                                Toast.makeText(MainActivity.this, "Rate successfully updated.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    in.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();

                    Log.i("myException", e.toString());

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(MainActivity.this, "Rate failed to update.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch(JSONException e)
                {
                    e.printStackTrace();

                    Log.i("myException", e.toString());

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(MainActivity.this, "Rate failed to update.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        thread.start();

        euroTextview.setText("1 EUR = " + String.format("%.2f", (1 * currencyRate)) + " USD");
        dollarTextview.setText("1 USD = " + String.format("%.2f", (1 / currencyRate)) + " EUR");
    }

    public void fromEuroToDollar(View view)
    {
        EditText euroTextbox = (EditText) findViewById(R.id.euroTextbox);
        EditText dollarTextbox = (EditText) findViewById(R.id.dollarTextbox);

        String text = euroTextbox.getText().toString();

        if(text.length() == 0)
        {
            Toast.makeText(MainActivity.this, "Please fill the text field", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dollarTextbox.setText("" + String.format("%.2f", Double.parseDouble(text) * currencyRate));
        }
    }

    public void fromDollarToEuro(View view)
    {
        EditText euroTextbox = (EditText) findViewById(R.id.euroTextbox);
        EditText dollarTextbox = (EditText) findViewById(R.id.dollarTextbox);

        String text = dollarTextbox.getText().toString();

        if(text.length() == 0)
        {
            Toast.makeText(MainActivity.this, "Please fill the text field", Toast.LENGTH_SHORT).show();
        }
        else
        {
            euroTextbox.setText("" + String.format("%.2f", Double.parseDouble(text) / currencyRate));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCurrencyRate();
    }
}