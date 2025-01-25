package com.dhondoi.nonaseblak.util;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class BluetoothHelper {
    private static final String TAG = "APP_DEBUG";
    private final BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;

    private final String printerName = "PeriPage_46D0";

    // Commands for printing
    private final byte[] cmdPrintStart = new byte[]{(byte) 0x10, (byte) 0xFF, (byte) 0xFE, (byte) 0x01};
    private final byte[] cmdPrintEnd = new byte[]{(byte) 0x1B, (byte) 0x4A, (byte) 0x40, (byte) 0x10, (byte) 0xFF, (byte) 0xFE, (byte) 0x45};
    private final byte[] cmdSetPrintInfo = new byte[]{(byte) 0x1D, (byte) 0x76, (byte) 0x30, (byte) 0x00, (byte) 0x30, (byte) 0x00};
    private boolean btPerm = false;

    private final BitmapHelper bitmapHelper;

    private static final int BLUETOOTH_CONNECT_PERMISSION_CODE = 1;

    private ComponentActivity appActivity;
    private Context context;

    private boolean printSuccess;

    public BluetoothHelper(ComponentActivity appActivity, Context context) {
        this.appActivity = appActivity;
        this.context = context;
        bluetoothManager = (BluetoothManager) appActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bitmapHelper = new BitmapHelper(context);
        setBtPermissions();
    }

    private void setBtPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            btPerm = ActivityCompat.checkSelfPermission(appActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        } else {
            btPerm = ActivityCompat.checkSelfPermission(appActivity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(appActivity, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void checkAndRequestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(appActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(appActivity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH_CONNECT_PERMISSION_CODE);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(appActivity, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(appActivity, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(appActivity, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, BLUETOOTH_CONNECT_PERMISSION_CODE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (requestCode == BLUETOOTH_CONNECT_PERMISSION_CODE) {
                btPerm = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
        } else {
            if (requestCode == BLUETOOTH_CONNECT_PERMISSION_CODE) {
                btPerm = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
        }
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothOn() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public boolean isPrinterConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    public void turnOnBluetooth() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (!btPerm) {
                checkAndRequestBluetoothPermission();
                Log.d(TAG, "No permission for bluetooth");
            } else if (!isBluetoothOn()) {
                enableBluetoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(enableBluetoothIntent);
                Log.d(TAG, "Turning on bluetooth");
            } else {
                Log.d(TAG, "Bluetooth already on");
            }
        }
    }

    public boolean connectPrinter() {
        if (isPrinterConnected()) {
            Log.d(TAG, "Printer is already connected");
            return false;
        }

        BluetoothDevice device = findBluetoothDevice();
        if (device == null) {
            return false;
        }

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try {
            if (!btPerm) {
                checkAndRequestBluetoothPermission();
                return false;
            } else {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnectPrinter() {
        try {
            if (outputStream != null) outputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BluetoothDevice findBluetoothDevice() {
        if (!btPerm) {
            checkAndRequestBluetoothPermission();
            return null;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices != null) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(printerName)) {
                    return device;
                }
            }
        }
        return null;
    }

    public Bitmap messageToBitmap(String message, int font, float fontSize) {
        // 384 maxWidtg
        return bitmapHelper.textToMultilineBitmap(message, font, fontSize, 470);
    }

    public boolean printFeed(int lines, boolean blank) {
        if (!isPrinterConnected()) {
            Log.d(TAG, "No Printer is connected");
            return false;
        }

        if (lines > 65535) {
            return false;
        }

        byte[] heightBytes = ByteBuffer.allocate(2).putShort((short) lines).array();

        byte[] line = new byte[48];
        Arrays.fill(line, blank ? (byte) 0 : (byte) 0xFF);


        new Thread(() -> {
            try {
                outputStream.write(cmdPrintStart);
                outputStream.write(concatArrays(cmdSetPrintInfo, heightBytes));

                for (int i = 0; i < lines; i++) {
                    outputStream.write(line);
                    Thread.sleep(20);
                }

                outputStream.write(cmdPrintEnd);
                outputStream.flush();
                printSuccess = true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                printSuccess = false;
            }
        }).start();

        return printSuccess;
    }

    private void bluetoothPrinterIsSecure() {
        checkAndRequestBluetoothPermission();
        turnOnBluetooth();
        connectPrinter();
    }

    public boolean printImage(Bitmap bitmap) {
        bluetoothPrinterIsSecure();

        if (!isPrinterConnected()) {
            Log.d(TAG, "No Printer is connected");
            Toast.makeText(context, "Printer Belum Terkoneksi!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int width = 384;
        float scale = (float) width / bitmap.getWidth();
        int height = (int) (bitmap.getHeight() * scale);

        if (height > 65535) {
            return false;
        }

        Bitmap grayBitmap = bitmapHelper.convertToGrayscale(bitmap);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(grayBitmap, width, height, true);
        byte[] imageBytes = bitmapHelper.bitmapToByteArray(resizedBitmap);

        byte[] heightBytes = ByteBuffer.allocate(2).putShort((short) height).array();

        new Thread(() -> {
            try {
                outputStream.write(cmdPrintStart);
                outputStream.write(concatArrays(cmdSetPrintInfo, heightBytes));

                for (int i = 0; i < imageBytes.length; i += 48) {
                    int max = Math.min(i + 48, imageBytes.length);
                    byte[] chunk = Arrays.copyOfRange(imageBytes, i, max);
                    outputStream.write(chunk);
                    Thread.sleep(20);
                }

                byte[] lineHeightBytes = ByteBuffer.allocate(2).putShort((short) 30).array();
                outputStream.write(concatArrays(cmdSetPrintInfo, lineHeightBytes));
                byte[] line = new byte[48];
                Arrays.fill(line, (byte) 0);

                for (int i = 0; i < 30; i++) {
                    outputStream.write(line);
                    Thread.sleep(20);
                }

                outputStream.write(cmdPrintEnd);
                outputStream.flush();
                printSuccess = true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
//                Toast.makeText(context, "Koneksi Printer Terputus!", Toast.LENGTH_SHORT).show();
                printSuccess = false;
            }
        }).start();
        return printSuccess;
    }

    private byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
}
