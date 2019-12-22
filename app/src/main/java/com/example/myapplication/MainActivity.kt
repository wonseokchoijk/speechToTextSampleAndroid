package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.speech.SpeechRecognizer
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.Manifest
import kotlinx.android.synthetic.main.activity_main.*
import android.speech.RecognizerIntent
import java.security.AccessControlContext
import java.security.AccessController.getContext
import android.Manifest.permission
import android.Manifest.permission.RECORD_AUDIO
import android.app.PendingIntent.getActivity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.widget.Toast
import androidx.core.content.ContextCompat




class MainActivity : AppCompatActivity() {

    lateinit var i: Intent
    lateinit var mRecognizer: SpeechRecognizer
    val MY_PERMISSIONS_RECORD_AUDIO = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.packageName)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP")
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        mRecognizer.setRecognitionListener(listener)



        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

            println("-------------------------------------- 音声認識スタート!")
            Toast.makeText(applicationContext, "音声入力スタート", Toast.LENGTH_SHORT).show()

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    MY_PERMISSIONS_RECORD_AUDIO
                )
            } else {
                try {
                    mRecognizer.startListening(i)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    var listener = object: RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            println("##### onReadyForSpeech.........................")
        }

        override fun onBeginningOfSpeech() {
            println("##### onBeginningOfSpeech.........................")
        }

        override fun onRmsChanged(p0: Float) {
            println("##### onRmsChanged.........................")
        }

        override fun onEndOfSpeech() {
            println("##### onEndOfSpeech.........................")
        }

        override fun onBufferReceived(p0: ByteArray?) {
            println("##### onBufferReceived.........................")
        }

        override fun onPartialResults(p0: Bundle?) {
            println("##### onPartialResults.........................")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            println("##### onEvent.........................")
        }

        override fun onError(p0: Int) {
            Toast.makeText(applicationContext, "もう一回喋ってください。", Toast.LENGTH_SHORT).show()
        }

        override fun onResults(results: Bundle?) {
            var key = ""
            key = SpeechRecognizer.RESULTS_RECOGNITION
            val mResult = results!!.getStringArrayList(key)
            val rs = arrayOfNulls<String>(mResult!!.size)
            mResult!!.toArray(rs)
            var resultStr = ""
            for (str in rs) {
                resultStr += "$str\n"
                println("###### $str")
            }
            Toast.makeText(applicationContext, resultStr, Toast.LENGTH_SHORT).show()
            println("##### $resultStr")
//            mRecognizer.startListening(i)
        }

    }
}
