package com.dhondoi.nonaseblak.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.dhondoi.nonaseblak.activity.OrderActivity;
import com.dhondoi.nonaseblak.entity.Order;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothHelperBackup {

    // android built in classes for bluetooth operations
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    private OutputStream mmOutputStream;
    private InputStream mmInputStream;
    private Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    byte FONT_TYPE;

    private static final String printer_id = "MYJS";

    private final Context context;

    public BluetoothHelperBackup(Context context) {
        this.context = context;
        findBT();
        openBT();
    }

    private void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(context, "Device Bluetooth tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((OrderActivity) context).startActivityForResult(enableBluetooth, 0);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(printer_id)) {
                        mmDevice = device;
                        break;
                    }
                }
            }
        } catch (Exception e) {
//            DialogUtil.showDialog1Button(context, "Terjadi Kesalahan. Hubungi Programmer!");
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    private void openBT() {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
        } catch (Exception e) {
//            DialogUtil.showDialog1Button(context, "Terjadi Kesalahan. Hubungi Programmer!");
            e.printStackTrace();
        }
    }
//
//    public void printQrCode(Bitmap qRBit) {
//        try {
//            PrintPic printPic1 = PrintPic.getInstance();
//            printPic1.init(qRBit);
//            byte[] bitmapdata2 = printPic1.printDraw();
//            mmOutputStream.write(bitmapdata2);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    public void printOrder(String nameCustomer, List<Order> orders, Long totalPriceOrder, String note) {
        try {
//            StringBuilder text = new StringBuilder();
//            text.append("--------------------------------");
//            text.append("\n----------NONA SEBLAK-----------");
//            text.append("\n--------------------------------");
//            text.append("\nNama    : ");
//            text.append(nameCustomer);
//            text.append("\nTanggal : ");
//            text.append(DateUtil.getStringDateNowForPrint());
//            text.append("\n--------------------------------");
//            for (Order order : orders) {
//                text.append("\n");
//                String nameProduct = order.getProduct().getName().toUpperCase();
//                if (nameProduct.length() > 16) {
//                    text.append(nameProduct.substring(0, 16));
//                } else {
//                    text.append(nameProduct);
//                }
//                if (nameProduct.length() < 9) {
//                    text.append("\t");
//                }
//                text.append("\t");
//                text.append(order.getQuantity());
//                text.append(" ");
//                text.append(CurrencyUtil.toCurrency(order.getTotal().intValue()));
//            }
//            text.append("\n--------------------------------");
//            text.append("\nTOTAL : ");
//            text.append(CurrencyUtil.toCurrency(totalPriceOrder.intValue()));
//            text.append("\n--------------------------------");
//            text.append("\nCATATAN : ");
//            text.append(note.toUpperCase());
//            text.append("\n--------------------------------");
//            text.append("\n----------TERIMA KASIH----------");
//            text.append("\n--------------------------------");
//            text.append("\n");
//            text.append("\n");
//            text.append("\n");
//            mmOutputStream.write(text.toString().getBytes(Charset.forName("UTF-8")));

            mmOutputStream.write("--------------------------------".getBytes());
            mmOutputStream.write("\n----------NONA SEBLAK-----------".getBytes());
            mmOutputStream.write("\n--------------------------------".getBytes());
            mmOutputStream.write("\nNama    : ".getBytes());
            mmOutputStream.write(nameCustomer.getBytes());
            mmOutputStream.write("\nTanggal : ".getBytes());
            mmOutputStream.write(DateUtil.getStringDateNowForPrint().getBytes());
            mmOutputStream.write("\n--------------------------------".getBytes());
            for (Order order : orders) {
                mmOutputStream.write("\n".getBytes());
                String nameProduct = order.getProduct().getName().toUpperCase();
                if (nameProduct.length() > 16) {
                    mmOutputStream.write(nameProduct.substring(0, 16).getBytes());
                } else {
                    mmOutputStream.write(nameProduct.getBytes());
                }
                if (nameProduct.length() < 9) {
                    mmOutputStream.write("\t".getBytes());
                }
                mmOutputStream.write("\t".getBytes());
                mmOutputStream.write(order.getQuantity().toString().getBytes());
                mmOutputStream.write(" ".getBytes());
                mmOutputStream.write(CurrencyUtil.toCurrency(order.getTotal().intValue()).getBytes());
            }
            mmOutputStream.write("\n--------------------------------".getBytes());
            mmOutputStream.write("\nTOTAL \t\t: ".getBytes());
            mmOutputStream.write(CurrencyUtil.toCurrency(totalPriceOrder.intValue()).getBytes());
            mmOutputStream.write("\n--------------------------------".getBytes());
            mmOutputStream.write("\nCATATAN : ".getBytes());
            mmOutputStream.write(note.toUpperCase().getBytes());
            mmOutputStream.write("\n--------------------------------".getBytes());
            mmOutputStream.write("\n----------TERIMA KASIH----------".getBytes());
            mmOutputStream.write("\n--------------------------------".getBytes());
            mmOutputStream.write("\nInstagram : nonaseblakgbj".getBytes());
            mmOutputStream.write("\nGoFood : NONA SEBLAK, Griya Bukit Jaya".getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write("\n".getBytes());
            closeBT();
        } catch (Exception e) {
//            DialogUtil.showDialog1Button(context, "Terjadi Kesalahan. Hubungi Programmer!");
            e.printStackTrace();
        }
    }

    public void printEmpty() {
        try {
            mmOutputStream.write("\n\n\n\n\n".getBytes());
            closeBT();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    public void beginListenForData() {
        try {
            final Handler handler = new Handler();
            // this is the ASCII code for a newline character
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );
                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
//                                                myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
//                            DialogUtil.showDialog1Button(context, "Terjadi Kesalahan. Hubungi Programmer!");
                            ex.printStackTrace();
                            stopWorker = true;
                        }
                    }
                }
            });
            workerThread.start();
        } catch (Exception e) {
//            DialogUtil.showDialog1Button(context, "Terjadi Kesalahan. Hubungi Programmer!");
            e.printStackTrace();
        }
    }

    //    this will update data printer name in ModelUser
    // close the connection to bluetooth printer.
    public void closeBT() {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (Exception e) {
//            DialogUtil.showDialog1Button(context, "Terjadi Kesalahan. Hubungi Programmer!");
            e.printStackTrace();
        }
    }

}
