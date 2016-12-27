package com.rxnctrllabs.androidledblink;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Gpio gpio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        listAvailableGpio();
        connectToGpioPort("BCM21");
    }

    @Override
    protected void onDestroy() {
        if (null != gpio)
            try {
                gpio.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onDestroy();
    }

    private void listAvailableGpio() {
        TextView availableGpioText = (TextView) findViewById(R.id.availableGpioText);

        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> portList = manager.getGpioList();

        availableGpioText.setText(portList.toString());
    }

    private void connectToGpioPort(String gpioName) {
        try {
            PeripheralManagerService manager = new PeripheralManagerService();
            gpio = manager.openGpio(gpioName);

            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            gpio.setActiveType(Gpio.ACTIVE_LOW);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.w(TAG, "Thread sleep exception", e);
            }

            gpio.setValue(true);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO", e);
        }
    }
}
