package com.example.sesliasistan.assistant


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Telephony
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sesliasistan.BuildConfig
import com.example.sesliasistan.Ilaclar
import com.example.sesliasistan.R
import com.example.sesliasistan.databinding.ActivityAssistantBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AssistantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssistantBinding//view ve datalara erişmek ve activity assistant ile veriler bağlanması için


    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer

//    private lateinit var keeper: String
    private  lateinit var locale: Locale
//private var layoutManager:RecyclerView.LayoutManager?=null
   // private  var adapter:RecyclerView.Adapter<AssistantAdapter.ViewHolder>?=null
    private lateinit var liste:ArrayList<Konusma>
//lateinit var asistan:ArrayList<String>
//lateinit var human:ArrayList<String>

    private lateinit var lastCallPersonName : String

    private var REQUESTCALL = 1
//    private var SENDSMS = 2
    private var READSMS = 3
    private var SHAREFILE = 4
//    private var SHARETEXTFILE = 5
    private var READCONTACTS = 6
    private var CAPTUREPHOTO = 7
//
    private var REQUEST_CODE_SELECT_DOC: Int = 100
    private var REQUEST_ENABLE_BT = 1000

    private var bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraID: String
//    private lateinit var clipboardManager: ClipboardManager//kopyalanan mesajı göstermek için
//    private lateinit var ringtone: Ringtone

    private val logtts = "TTS"
    private val logsr = "SR"
//    private val logkeeper = "keeper"
//
    private var imageIndex: Int = 0
//    private lateinit var imgUri: Uri//fotonun tam konumunu yakalamak için
//    private lateinit var helper: OpenWeatherMapHelper//foto çekebilmek için yardımcı
lateinit var btn:ImageButton
lateinit var rcy:RecyclerView


    @Suppress("DEPRECATION")
    private val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/assistant/" //harici depolamada tutulan resmin adresi


    @SuppressLint("ClickableViewAccesibilty", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)
        overridePendingTransition(R.anim.non_movable, R.anim.non_movable)
locale= Locale("tr","TR");
//        binding = DataBindingUtil.setContentView(
//            this,
//            R.layout.activity_assistant
//        )//xml dosyası ile bağlantı kuruldu

        //layoutManager=LinearLayoutManager(this)

        rcy =findViewById<RecyclerView>(R.id.recyclerview1);

        rcy.layoutManager = LinearLayoutManager(this)
      liste= arrayListOf<Konusma>()






        btn=findViewById(R.id.assistant_action_button)
        btn.setOnClickListener{
                        if(SpeechRecognizer.isRecognitionAvailable(this)){
                val intent=Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,5)
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"say something")
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"tr")
                startActivityForResult(intent,111)
            }
        }
        textToSpeech= TextToSpeech(applicationContext,TextToSpeech.OnInitListener { status->
            if(status!=TextToSpeech.ERROR){

                textToSpeech.language= locale
            }
        })


//                        keeper.contains("teşekkürler") -> speak("ne demek, bu benim işim.")
//                        keeper.contains("hoşgeldiniz") -> speak("çok naziksin")
//                        keeper.contains("clear") -> assistantViewModel.onClear()
//                        keeper.contains("tarih") -> getDate()
//                        keeper.contains("saat") -> getTime()
//                        keeper.contains("rehberden arama yap") -> makeAPhoneCall()
//                        keeper.contains("mesaj gönder") -> sendSMS()
//                        keeper.contains("mesajı oku") -> readSMS()
//                        keeper.contains("gmaili aç") -> openGmail()
//                        keeper.contains("Whatsapp aç") -> openWhatsapp()
//                        keeper.contains("facebook aç") -> openFacebook()
//                        keeper.contains("mesajları aç") -> openMessages()
//                        keeper.contains("dosya paylaş") -> shareAFile()
//                        keeper.contains("text mesajı paylaş") -> shareATextMessages()
//                        keeper.contains("kişiyi ara") -> callContact()
//                        //keeper.contains("bluetooth aç") -> turnOnBluetooth()
//                        //keeper.contains("bluetooth kapat") -> turnOffBluetooth()
//                        //keeper.contains("get bluetooth devices") -> getAllPairedDevices()
//                        keeper.contains("flaşı aç") -> turnOnFlash()
//                        keeper.contains("flaşı kapat") -> turnOfFlash()
//                        keeper.contains("panoya kopyala") -> clipboardCopy()
//                        keeper.contains("panodan oku") -> clipboardSpeak()
//                        keeper.contains("fotograf çek") -> capturePhoto()
//                        keeper.contains("zil sesi çal") -> playRingtone()
//                        keeper.contains("zil sesini durdur") ||keeper.contains("top ringtone")
//                        -> stopRingtone()
//                        keeper.contains("oku") -> readMe()
//                        keeper.contains("alarm kur") -> setAlarm()
//                        keeper.contains("hava durumu") -> weather()
//                        keeper.contains("burçlar") -> horoscope()
//                        keeper.contains("medikal") -> medicalApplication()
//                        keeper.contains("şaka") -> joke()
//                        keeper.contains("soru") -> question()
//                        keeper.contains("merhaba") ||keeper.contains("merhaba") || keeper.contains("hey")
//                        -> speak("merhaba, nasıl yardımcı olabilirim?")
//                        else -> speak("geçersiz komut,tekrar deneyin")

    }
private fun ekle(txtHuman:String,txtAsistan:String){
    val aa=Konusma(txtHuman,txtAsistan)
    liste.add(aa)
    rcy.adapter=AssistantAdapter(liste)
}
    private fun temizle(){
        liste.clear()
        rcy.adapter=AssistantAdapter(liste)
    }
    private fun checkIfSpeechRecognizerAvailable() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.d(logsr, "yes")
        } else {
            Log.d(logsr, "false")
        }
    }

    fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")

    }

    fun getDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val d:String=sdf.format(Date())
        return "tarih $d"

    }

    fun getTime():String {
        val clendar = Calendar.getInstance()
        val format = SimpleDateFormat("HH:mm:ss")
        val time: String = format.format(clendar.getTime())
        //speak("Saat $time")
        return "Saat $time"
    }


//    private fun makeAPhoneCall() {
//        val keeperSplit = keeper.replace(" ".toRegex(), "").split("o").toTypedArray()
//        val number =0
//
//         number must not have any spaces
//        if (number> 0) {
//
//             runtime message
//            if (ContextCompat.checkSelfPermission(
//                    this@AssistantActivity,
//                    Manifest.permission.CALL_PHONE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this@AssistantActivity,
//                    arrayOf(Manifest.permission.CALL_PHONE),
//                    REQUESTCALL
//                )
//            } else {
//                 passing intent
//                val dial = "tel:$number"
//                speak("Aranıyor $number")
//                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
//
//            }
//        } else {
//             invalid phone
//            Toast.makeText(this@AssistantActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show()
//        }
//    }

//    fun sendSMS() {

//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.SEND_SMS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.SEND_SMS),
//                SENDSMS
//            )
//            Log.d("keeper", "Done1")
//        } else {
//            Log.d("keeper", "Done2")
//            val keeperReplaced = keeper.replace(" ".toRegex(), "")
//            val number =
//                keeperReplaced.split("o").toTypedArray()[1].split("t").toTypedArray()[0]
//            val message = keeper.split("that").toTypedArray()[1]
//
//            Log.d("chk", number + message)
//            val mySmsManager = SmsManager.getDefault()
//            mySmsManager.sendTextMessage(
//                number.trim { it <= ' ' },
//                null,
//                message.trim { it <= ' ' },
//                null,
//                null
//            )
//            speak("mesaj gönderildi $message")
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun readSMS() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                READSMS
            )
        } else {
            val cursor = contentResolver.query(Uri.parse("content://sms"), null, null, null)
            cursor!!.moveToFirst()
            speak("Son mesajınız" + cursor.getString(12))
        }
    }

    fun openMessages() {
        val intent =
            packageManager.getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(this))
        intent?.let { startActivity(it) }
    }

    fun openFacebook() {
        val intent = packageManager.getLaunchIntentForPackage("com.facebook")
        intent?.let { startActivity(it) }
    }

    private fun openWhatsapp() {
        val intent = packageManager.getLaunchIntentForPackage("com.whatsapp")
        intent?.let { startActivity(intent) }
    }

    fun openGmail() {
        val intent = packageManager.getLaunchIntentForPackage("com.google.android.gm")
        intent?.let { startActivity(it) }
    }

    private fun shareAFile() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                SHAREFILE
            )
        } else {
            //val builder = StrictMode.VmPolicy.Builder()
            //StrictMode.setVmPolicy(builder.build())

            val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
            //val myFileIntent = Intent(Intent.ACTION_CHOOSER)
            myFileIntent.type = "application/pdf"

            startActivityForResult(myFileIntent, REQUEST_CODE_SELECT_DOC)
        }
    }
//
//    private fun shareATextMessages() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ),
//                SHARETEXTFILE
//            )
//        } else {
//            val builder = StrictMode.VmPolicy.Builder()
//            StrictMode.setVmPolicy(builder.build())
//            val message = keeper.split("that").toTypedArray()[1]
//            val intentShare = Intent(Intent.ACTION_SEND)
//            intentShare.type = "text/plain"
//            intentShare.putExtra(Intent.EXTRA_TEXT, message)
//            startActivity(Intent.createChooser(intentShare, "paylaşılan metin"))
//        }
//    }
//
    private fun callContact() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                READCONTACTS
            )
        } else {

            val name = lastCallPersonName
            //val name = keeper.split("call").toTypedArray()[1].trim { it <= ' ' }
            Log.d("chk", name)
            try {
                val cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE
                    ), "DISPLAY_NAME='$name'", null, null
                )
                cursor!!.moveToFirst()
                val number = cursor.getString(0)
                if (number.trim { it <= ' ' }.length > 0) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            REQUESTCALL
                        )
                    } else {
                        val dial = "tel:$number"
                        startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                    }
                } else {
                    Toast.makeText(this, "Telefon numarası giriniz", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                //speak("birşeyler hatalı")
                speak(lastCallPersonName + " adlı bir kişi bulunamadı")
            }
        }
    }


    private fun turnOnBluetooth()
    {
        if(!bluetoothAdapter.isEnabled())
        {

            speak("bluetooth açılıyor")
            val intent=Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                startActivityForResult(intent,REQUEST_ENABLE_BT)
            }

        }
        else{
            speak("Bluetooth zaten açık")
        }
    }
    private fun turnOffBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter.disable()
            }

            speak("Bluetooth kapatılıyor")
        } else {
            speak("Bluetooth zaten kapalı")
        }
    }

//    private fun getAllPairedDevices() {
//        if (bluetoothAdapter.isEnabled()) {
//            speak("Eşleştirilmiş cihazlar")
//            var text = ""
//            var count = 1
//            val devices: Set<BluetoothDevice>=bluetoothAdapter.getBondedDevices()
//            for (device in devices) {
//                text += "\nCihazlar:$count ${device.name},$device"
//                count += 1
//            }
//            speak(text)
//        } else {
//            speak("eşleştirilmiş cihazlara erişmek için bluetooth'u açın")
//        }
//    }
var isFlash=false
    private fun turnOffFlash() {
        try {
            if (isFlash==true) {
                cameraManager.setTorchMode(cameraID, false)

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun turnOnFlash()
    {
        try {
            if (isFlash==false) {
                val cm=cameraManager.cameraIdList[0]
                cameraManager.setTorchMode(cm, true)

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

//    fun clipboardCopy() {
//        val data = keeper.split("that").toTypedArray()[1].trim { it <= ' ' }
//        if (!data.isEmpty()) {
//            val clipData = ClipData.newPlainText("text", data)
//            clipboardManager.setPrimaryClip(clipData)
//            speak("$data kopyalandı")
//        }
//    }
//
//    fun clipboardSpeak() {
//        val item = clipboardManager.primaryClip!!.getItemAt(0)
//        val pasteData = item.text.toString()
//        if (pasteData != "") {
//            speak("son kopyalanan veri:" + pasteData)
//        } else {
//            speak("pano boş")
//        }
//    }
//
    private fun capturePhoto() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAPTUREPHOTO
            )
        } else {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            imageIndex++
            val file: String = imageDirectory + imageIndex + ".jpg"
            val newFile = File(file)
            try {
                newFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val outputFileUri = Uri.fromFile(newFile)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            startActivity(cameraIntent)
            speak("fotoğraf kaydedildi")
        }
    }

//    fun playRingtone() {
//        speak("zil sesi oynatılıyor")

//        ringtone.play()
//    }
//
//    fun stopRingtone() {
//        speak("zil sesi durduruldu")
//        ringtone.stop()
//    }

//    private fun readMe() {
//        CropImage.startPickImageActivity(this)
//    }
//
//    private fun getTextFromBitmap(bitmap: Bitmap) {
//        val image = InputImage.fromBitmap(bitmap,0)
//        val recognizer = TextRecognition.getClient()
//        val result = recognizer.process(image).addOnSuccessListener { visionText ->
//            val resultText = visionText.text
//            if (keeper.contains("summarise")) {
//                speak("resim okunuyor ve özetleniyor:\n" + summariseText(resultText))
//            } else {
//                speak("resim okunuyor:\n" + resultText)
//            }
//        }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Error" + e.message, Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun summariseText(text: String): String? {
//        val summary: kotlin.jvm.internal.Ref.ObjectRef<*>;kotlin.jvm.internal.Ref.ObjectRef<Any?>()
//        summary = kotlin.jvm.internal.Ref.ObjectRef<Any?>()
//        summary.element = Text2Summary.Companion.summarize(text, 0.4f)
//        return summary.element as String
//    }
//
//    private fun setAlarm() {
//
//
//    }
//    private fun medicalApplication()
//    {
//
//    }
//
//    private fun weather() {
//        if (keeper.contains("fahrenheit")) {
//            helper.setUnits(Units.IMPERIAL)
//        } else if (keeper.contains("celcius")) {
//            helper.setUnits(Units.METRIC)
//        }
//        val keeperSplit = keeper.replace(" ".toRegex(), "").split("w").toTypedArray()
//        val city = keeperSplit[0]
//        helper.getCurrentWeatherByCityName(city, object : CurrentWeatherCallback {
//
//            override fun onSuccess(currentWeather: CurrentWeather?) {
//                speak(
//                    """
//                Coordinates:${currentWeather!!.coord.lat},${currentWeather!!.coord.lon}
//                weather Description:${currentWeather!!.main.tempMax}
//                Temperature:${currentWeather!!.main.tempMax}
//                wind speed:${currentWeather!!.wind.speed}
//                city,country:${currentWeather!!.name},${currentWeather!!.sys.country}""".trimIndent()
//                )
//            }
//
//            override fun onFailure(throwable: Throwable?) {
//                speak("Error" + throwable!!.message)
//            }
//        })
//    }
//
//    private fun horoscope() {
//        Log.d("chk", "hello")
//        val hGemini: Horoscope? = Horoscope.Zodiac(this)
//            .requestSunSign(SUNSIGN.GEMINI)
//            .requestDuration(DURATION.TODAY)
//            .showLoader(true)
//            .isDebuggable(true)
//            .fetchHoroscope()
//
//        val cGemini = HorologyController(object : Response {
//            override fun onResponseObtained(zodiac: Zodiac?) {
//                val horoscope: String = zodiac!!.getHoroscope()
//                val sunsign: String = zodiac!!.getSunSign()
//                val date: String = zodiac!!.getDate()
//                Log.d("chk", horoscope + sunsign + date)
//            }
//
//            override fun onErrorObtained(errormsg: String?) {
//                speak("veri noktalarına ulaşılamıyor.")
//            }
//        })
//        cGemini.requestConstellations(hGemini)
//    }
//
//    private fun joke() {
//
//    }
//
//    private fun question() {
//
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUESTCALL) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //makeAPhoneCall()
                callContact()
            } else {
                Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show()
            }

        }
//         if (requestCode == SENDSMS) {
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                sendSMS()
//            } else {
//                Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show()
//            }
//
//        }
        if (requestCode == READSMS) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSMS()
            } else {
                Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
         else if (requestCode == SHAREFILE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareAFile()
            } else {
                Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show()
            }

        }
//        else if (requestCode == SHARETEXTFILE) {
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                shareATextMessages()
//            } else {
//                Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show()
//            }
//
//        }
        else if (requestCode == READCONTACTS) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callContact()
            } else {
                Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show()
            }

        }
            else if (requestCode == CAPTUREPHOTO) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto()
            } else {
                Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK){
            val res : ArrayList<String>? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            //Toast.makeText(this, res?.get(0).toString(),Toast.LENGTH_LONG).show()
           val soylenen :String=res?.get(0).toString();
            if(soylenen.toLowerCase().equals("merhaba")){
                speak("merhaba, nasıl yardımcı olabilirim?")
                ekle(soylenen,"merhaba, nasıl yardımcı olabilirim?")
            }
            else if(soylenen.toLowerCase().equals("temizle")){
                temizle()
                speak("liste temizlendi..")
            }
            else if(soylenen.toLowerCase().equals("teşekkürler")){
                speak("ne demek, bu benim işim.")
                ekle(soylenen,"ne demek, bu benim işim.")
            }
            else if(soylenen.toLowerCase().equals("tarih")){
                speak(getDate())
                ekle(soylenen,getDate())
            }
            else if(soylenen.toLowerCase().equals("saat kaç")){
                speak(getTime())
                ekle(soylenen,getTime())
            }
            else if(soylenen.toLowerCase().equals("mesajları aç")){
                openMessages()
                speak("mesajlar açılıyor")
            }
            else if(soylenen.toLowerCase().equals("whatsapp'ı aç")){
                openWhatsapp()
                speak("whatsapp açılıyor")
            }
            else if(soylenen.toLowerCase().equals("Gmail aç")){
                openGmail()
                speak("Gmail açılıyor")
            }
            else if(soylenen.toLowerCase().equals("Facebook aç")){
                openFacebook()
                speak("Facebook açılıyor")
            }
            else if (soylenen.toLowerCase().equals("sms oku")) {
                    readSMS()
            }
            else if(soylenen.toLowerCase().equals("bluetooth aç"))
            {
                turnOnBluetooth()
                ekle(soylenen,"bluetooth açıldı.")
            }
            else if(soylenen.toLowerCase().equals("bluetooth kapat"))
            {
                turnOffBluetooth()
                ekle(soylenen,"bluetooth kapatıldı.")
            }
            else if(soylenen.toLowerCase().equals("fotoğraf çek"))
            {
                capturePhoto()
            }
            else if(soylenen.toLowerCase().equals("flash aç"))
            {
               turnOnFlash()
                speak("Flash açıldı")
               ekle(soylenen,"flash açıldı")
            }
            else if(soylenen.toLowerCase().equals("flash kapat"))
            {
                turnOffFlash()
                speak("Flash kapatıldı")
                ekle(soylenen,"flash kapatıldı")
            }
            /*
            * Yaşlı insanlar için ilaç hatırlatma (ilaç ismi ve saat alıp alarm çalmalı)
            * Dosya paylaşımı
            * Rehberden arama (ok)
            *
            * UI iyileştirilebilir mi?
            * Türkçe cevap olur mu??
             */
            else if (soylenen.toLowerCase().endsWith("ara")) {
                lastCallPersonName = soylenen.substringBefore("ara").trim()
                Log.d("myTag","last call person name " + lastCallPersonName);
                callContact()
            }
            else if (soylenen.toLowerCase().equals("dosya paylaş")) {
                shareAFile()
            }
            else if (soylenen.toLowerCase().equals("ilaçlarım")) {
                speak("ilaç listeniz açılıyor")
                val intent = Intent(this, Ilaclar::class.java)
                startActivity(intent)
            }

            else{
                speak("anlayamadım,lütfen tekrar söyleyin")
                ekle(soylenen,"anlayamadım,lütfen tekrar söyleyin")
            }

        }

        if (requestCode == REQUEST_CODE_SELECT_DOC && resultCode == RESULT_OK) {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "application/pdf"
            shareIntent.putExtra(Intent.EXTRA_STREAM, data!!.data!!)
            startActivity(Intent.createChooser(shareIntent, "Sendingg.."));

        }
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                speak("Bluetooth açık")
            } else {
                speak("Bluetooth açılamıyor")
            }
        }
//
//        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
//            val imageUri = CropImage.getPickImageResultUri(this, data)
//            imgUri = imageUri
//            startCrop(imageUri)
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
//            if (resultCode == RESULT_OK) {
//                imgUri = result.uri
//                try {
//                    val inputStream = contentResolver.openInputStream(imgUri)
//                    val bitmap = BitmapFactory.decodeStream(inputStream)
//                    getTextFromBitmap(bitmap)
//                } catch (e: FileNotFoundException) {
//                    e.printStackTrace()
//                }
//
//                Toast.makeText(this, "görüntü başarıyla yakalandı", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

//    private fun startCrop(imageUri:Uri)
//    {
//        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this)
//    }
//
//    private fun circularRevealActivity()
//    {
//        val cx:Int=binding.assistantConstraitLayout.getRight()-getDips(44)
//        val cy:Int=binding.assistantConstraitLayout.getBottom()-getDips(44)
//        val finalRadius:Int=Math.max(
//            binding.assistantConstraitLayout.getWidth(),
//            binding.assistantConstraitLayout.getHeight(),
//            )
//        val circularReveal=ViewAnimationUtils.createCircularReveal(
//            binding.assistantConstraitLayout,
//            cx,
//            cy,
//            0f,
//            finalRadius.toFloat()
//        )
//        circularReveal.duration=1250
//        binding.assistantConstraitLayout.setVisibility(View.VISIBLE)
//        circularReveal.start()
//    }
//
    private fun getDips(dps:Int):Int{
        val resources:Resources=resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dps.toFloat(),
            resources.getDisplayMetrics()
        ).toInt()
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
//            val cx: Int=binding.assistantConstraitLayout.getWidth()-getDips(44)
//            val cy:Int=binding.assistantConstraitLayout.getHeight()-getDips(44)
//
//            val finalRadius:Int=Math.max(
//                binding.assistantConstraitLayout.getWidth(),
//                binding.assistantConstraitLayout.getHeight()
//            )
//            val circularReveal=
//                ViewAnimationUtils.createCircularReveal(
//                    binding.assistantConstraitLayout,cx,cy,
//                    finalRadius.toFloat(),0f
//                )
//
//            circularReveal.addListener(object :Animator.AnimatorListener{
//                override fun onAnimationStart(p0: Animator?) {
//
//                }
//
//                override fun onAnimationEnd(p0: Animator?) {
//                    binding.assistantConstraitLayout.setVisibility(View.INVISIBLE)
//                    finish()
//                }
//
//                override fun onAnimationCancel(p0: Animator?) {
//
//                }
//
//                override fun onAnimationRepeat(p0: Animator?) {
//
//                }
//
//            })
//
//            circularReveal.duration=1250
//            circularReveal.start()
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        textToSpeech.stop()
//        textToSpeech.shutdown()
//        speechRecognizer.cancel()
//        speechRecognizer.destroy()
//        Log.i(logsr,"destroy")
//        Log.i(logtts,"destroy")
    }
}

