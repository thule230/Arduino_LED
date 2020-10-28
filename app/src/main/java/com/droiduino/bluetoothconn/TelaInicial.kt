package com.droiduino.bluetoothconn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

class TelaInicial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_inicial)
    }

    fun scanear (componente : View){
        IntentIntegrator(this@TelaInicial).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        IntentIntegrator(this@TelaInicial).setPrompt("Teste QR Code")
        IntentIntegrator(this@TelaInicial).setCameraId(0)
        IntentIntegrator(this@TelaInicial).initiateScan()
    }

//    fun conectar (componente: View){
//        val intent = Intent(this, SelectDeviceActivity::class.java)
//        startActivity(intent)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("deviceName", "Arduino")
            intent.putExtra("deviceAddress", "00:19:09:26:09:5A")
            startActivity(intent)
        }
        else {
            Toast.makeText(this, "Erro de leitura", Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}