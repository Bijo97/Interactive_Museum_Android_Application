package com.example.xtalker.manprotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Bijo97 on 12/11/2017.
 */

public class Scanner extends Activity implements ZXingScannerView.ResultHandler{
    ZXingScannerView scanner;
    Boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        IntentIntegrator scanIntegrator = new IntentIntegrator(Scanner.this);
        scanIntegrator.setPrompt("Scan a barcode");
        scanIntegrator.setBeepEnabled(true);
        scanIntegrator.setOrientationLocked(true);
        scanIntegrator.setBarcodeImageEnabled(true);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        /*As an example in order to get the content of what is scanned you can do the following*/
        String scanContent = scanningResult.getContents().toString();
        Toast.makeText(getApplicationContext(), scanContent, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleResult(Result result) {

    }

//    @Override
//    public void handleResult(Result result) {
//        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_LONG).show();
//        scanner.resumeCameraPreview(this);
//        Intent itemIntent = new Intent(Scanner.this, DetailItem.class);
//        itemIntent.putExtra("hasil", result.getText());
//        startActivityForResult(itemIntent, 1);
//    }
//
//    public void clicked (View view){
//        scanner = new ZXingScannerView(this);
//        setContentView(scanner);
//        scanner.setResultHandler(this);
//        status = true;
//        scanner.startCamera();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (status == true){
//            scanner.stopCamera();
//        }
//    }
}
