package com.whatevervalue.myquis.Controller

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.whatevervalue.myquis.Model.DownloadingObject
import com.whatevervalue.myquis.Model.ParsePlantUtility
import com.whatevervalue.myquis.Model.Plant
import com.whatevervalue.myquis.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var cameraButton: Button? = null
    private var photoGalleryButton: Button? = null
    private var imageTaken: ImageView? = null

    val OPEN_CAMERA_BUTTON_REQUEST_ID = 1000
    val OPEN_PHOTO_GALLERY_BUTTON_ID = 2000

    var correctAnswerIndex: Int = 0
    var correctPlant: Plant? = null

    var numberOfTimesUserAnswerCorrectly: Int = 0
    var numberOfTimesUserAnswerWrong: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setProgressBar(false)
        displayUIWidgets(false)
        YoYo.with(Techniques.Pulse)
            .duration(700)
            .repeat(5)
            .playOn(btnNextPlant)


        cameraButton = findViewById(R.id.btnOpenaCamera)
        photoGalleryButton = findViewById(R.id.btnPhotoGallery)

        imageTaken = findViewById<ImageView>(R.id.imgTaken)

        cameraButton?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "The Camera Button is clicked", Toast.LENGTH_SHORT).show()

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, OPEN_CAMERA_BUTTON_REQUEST_ID)

        })
        photoGalleryButton?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "The Photo Gallery is opened", Toast.LENGTH_SHORT).show()

            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, OPEN_PHOTO_GALLERY_BUTTON_ID)

        })
        // SEE THE NEXT PLANT
        btnNextPlant.setOnClickListener (View.OnClickListener {

            if(checkForInternetConnection()) {

                setProgressBar(true)

                try {
                    val innerClassObject = DownloadingPlantTask()
                    innerClassObject.execute()
                }catch (e: Exception){
                    e.printStackTrace()
                }


                val gradientColors = IntArray(2)
                gradientColors.set(0, Color.parseColor("#FFFFFFFF"))
                gradientColors.set(1, Color.parseColor("#BBF3D6"))
                val gradientDrawable: GradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors)
                val convertedDipValue = dipToFloat(this@MainActivity, 50f)
                gradientDrawable.setCornerRadius(convertedDipValue)
                gradientDrawable.setSize(5, Color.parseColor("#FFFFFF"))


                button1.background = gradientDrawable
                button2.background = gradientDrawable
                button3.background = gradientDrawable
                button4.background = gradientDrawable

            }
        })

    }

    private fun dipToFloat(context: Context, dipValue: Float): Float {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OPEN_CAMERA_BUTTON_REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {

                val imageData = data?.extras?.get("data") as Bitmap
                imageTaken?.setImageBitmap(imageData)

            }
        }
        if (requestCode == OPEN_PHOTO_GALLERY_BUTTON_ID) {

            if (resultCode == Activity.RESULT_OK) {

                val contentURI = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                imageTaken?.setImageBitmap(bitmap)

            }
        }

    }


    fun button1IsClicked(buttonView: View) {

        specifyTheRightAndWrongAnswer(0)

    }

    fun button2IsClicked(buttonView: View) {


    specifyTheRightAndWrongAnswer(1)
    }

    fun button3IsClicked(buttonView: View) {

    specifyTheRightAndWrongAnswer(2)
    }

    fun button4IsClicked(buttonView: View) {

    specifyTheRightAndWrongAnswer(3)
    }

    @SuppressLint("StaticFieldLeak")
    inner class DownloadingPlantTask : AsyncTask<String, Int, List<Plant>>() {

        override fun doInBackground(vararg params: String?): List<Plant>? {
            //can access Background thread.

            val parsePlant = ParsePlantUtility()

            return parsePlant.parsePlantObjectFromJSONData()
        }

        override fun onPostExecute(result: List<Plant>?) {
            super.onPostExecute(result)
            //can access User Interface thread. No background thread

            val numberOfPlants= result?.size ?: 0

            if(numberOfPlants > 0) {
                val randomPlantIndexForButton1: Int = (Math.random() * result!!.size).toInt()
                val randomPlantIndexForButton2: Int = (Math.random() * result.size).toInt()
                val randomPlantIndexForButton3: Int = (Math.random() * result.size).toInt()
                val randomPlantIndexForButton4: Int = (Math.random() * result.size).toInt()

                val allRandomPlants = ArrayList<Plant>()
                allRandomPlants.add(result.get(randomPlantIndexForButton1))
                allRandomPlants.add(result.get(randomPlantIndexForButton2))
                allRandomPlants.add(result.get(randomPlantIndexForButton3))
                allRandomPlants.add(result.get(randomPlantIndexForButton4))

                button1.text = result.get(randomPlantIndexForButton1).toString()
                button2.text = result.get(randomPlantIndexForButton2).toString()
                button3.text = result.get(randomPlantIndexForButton3).toString()
                button4.text = result.get(randomPlantIndexForButton4).toString()

                correctAnswerIndex = (Math.random() * allRandomPlants.size).toInt()
                correctPlant = allRandomPlants.get(correctAnswerIndex)

                val downloadingImageTask = DownloadingImageTask()
                downloadingImageTask.execute(allRandomPlants.get(correctAnswerIndex).pictureName)

            }
        }

    }

    override fun onStart() {
        super.onStart()

        //Toast.makeText(this, "The OnStart Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        checkForInternetConnection()
    }

    override fun onPause() {
        super.onPause()
        //Toast.makeText(this, "The OnPause Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        //Toast.makeText(this, "The OnStop Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        //Toast.makeText(this, "The OnRestart Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Toast.makeText(this, "The OnDestroy Method is called", Toast.LENGTH_SHORT).show()
    }

    fun imageViewIsClicked(view: View) {


        val randomNumber: Int = (Math.random() * 6).toInt() + 1
        Log.i("TAG", "THE RANDOM BUTTON NUMBER IS: $randomNumber")

        when (randomNumber) {

            1 -> btnOpenaCamera.setBackgroundColor(Color.YELLOW)
            2 -> btnPhotoGallery.setBackgroundColor(Color.MAGENTA)
            3 -> button1.setBackgroundColor(Color.WHITE)
            4 -> button2.setBackgroundColor(Color.GREEN)
            5 -> button3.setBackgroundColor(Color.RED)
            6 -> button4.setBackgroundColor(Color.BLUE)
        }
    }

    //check for internet connection

    private fun checkForInternetConnection(): Boolean{

        val connectivityManager = this.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isDeviceConnectedToInternet = networkInfo !=null && networkInfo.isConnectedOrConnecting
        return if(isDeviceConnectedToInternet){

            true
        }else{

            createAlert()
            false
        }

    }

    private fun createAlert(){

        val alertDialog: AlertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle("Network Error")
        alertDialog.setMessage("Please check your internet connection")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK") {
                dialog: DialogInterface?, which: Int ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") {
                dialog: DialogInterface?, which: Int ->

            Toast.makeText(this@MainActivity, "You must be connected to Internet", Toast.LENGTH_SHORT).show()
            finish()
        }

        alertDialog.show()
    }

    // SPECIFY RIGHT AND WRONG ANSWER

    @SuppressLint("SetTextI18n")
    private fun specifyTheRightAndWrongAnswer(userGuess: Int)  {

        when (correctAnswerIndex) {

            0 -> button1.setBackgroundColor(Color.GREEN)
            1 -> button2.setBackgroundColor(Color.GREEN)
            2 -> button3.setBackgroundColor(Color.GREEN)
            3 -> button4.setBackgroundColor(Color.GREEN)
        }
        if (userGuess == correctAnswerIndex) {

            txtState.text = "Right!"
            numberOfTimesUserAnswerCorrectly++
            txtRightAnswer.text = "$numberOfTimesUserAnswerCorrectly"
        }else {

            val correctPlantName = correctPlant.toString()
            txtState.text = "Wrong. Choose : $correctPlantName"
            numberOfTimesUserAnswerWrong++
            txtWrongAnswer.setText("$numberOfTimesUserAnswerWrong")
        }
    }

    // DOWNLOADING IMAGE PROCESS

    @SuppressLint("StaticFieldLeak")
    inner class DownloadingImageTask: AsyncTask<String, Int, Bitmap?>() {


        override fun doInBackground(vararg pictureName: String?): Bitmap? {

            try{
                val downloadingObject = DownloadingObject()

                return downloadingObject.downloadPlantPicture(pictureName[0])

            }catch (e: Exception){

                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)

            setProgressBar(false)
            displayUIWidgets(true)
            playAnimationOnView(button1, Techniques.RollIn)
            playAnimationOnView(button2, Techniques.RollIn)
            playAnimationOnView(button3, Techniques.RollIn)
            playAnimationOnView(button4, Techniques.RollIn)
            playAnimationOnView(txtState, Techniques.Swing)
            playAnimationOnView(txtWrongAnswer, Techniques.BounceInLeft)
            playAnimationOnView(txtRightAnswer, Techniques.Landing)

            imgTaken.setImageBitmap(result)
        }
    }

    // PROGRESS BAR VISIBILITY

  private fun setProgressBar(show: Boolean) {

      if (show) {

          progressBar.visibility = View.VISIBLE
          progressBarTool.visibility = View.VISIBLE
          window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
      } else if (!show) {

          progressBar.visibility = View.GONE
          progressBarTool.visibility = View.GONE
          window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
      }
    }

    // SET VISIBILITY OF UI WIDGETS

    private fun displayUIWidgets(display: Boolean) {

        if (display) {

            imgTaken.visibility = View.VISIBLE
            button1.visibility = View.VISIBLE
            button2.visibility = View.VISIBLE
            button3.visibility = View.VISIBLE
            button4.visibility = View.VISIBLE
            txtState.visibility = View.VISIBLE
            txtWrongAnswer.visibility = View.VISIBLE
            txtRightAnswer.visibility = View.VISIBLE

        }else if(!display) {

            imgTaken.visibility = View.INVISIBLE
            button1.visibility = View.GONE
            button2.visibility = View.GONE
            button3.visibility = View.GONE
            button4.visibility = View.GONE
            txtState.visibility = View.GONE
            txtWrongAnswer.visibility = View.GONE
            txtRightAnswer.visibility = View.GONE
        }
    }

    // PLAYING ANIMATION
    private fun playAnimationOnView(view: View?, techniques: Techniques) {

        YoYo.with(techniques)
            .duration(700)
            .repeat(0)
            .playOn(view)


    }

}



