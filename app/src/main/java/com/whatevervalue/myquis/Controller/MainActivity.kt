package com.whatevervalue.myquis.Controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.whatevervalue.myquis.Model.DownloadingObject
import com.whatevervalue.myquis.Model.Plant
import com.whatevervalue.myquis.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var cameraButton: Button? = null
    private var photoGalleryButton: Button? = null
    private var imageTaken: ImageView? = null

    /*private var button1 : Button? = null
     private var button2 : Button? = null
     private var button3 : Button? = null
     private var button4 : Button? = null*/


    val OPEN_CAMERA_BUTTON_REQUEST_ID = 1000
    val OPEN_PHOTO_GALLERY_BUTTON_ID = 2000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)  // is going to put the tool bar on top of the Layout. toolbar is the ID we gave it inside the Activity_Main

        val innerClassObject = DownloadingPlantTask()
        innerClassObject.execute()


        /* Toast.makeText(this,"The OnCreate Method is called", Toast.LENGTH_SHORT).show()
        val myPlant: Plant = Plant("", "", "","","","",0,0)

       // Plant("Koelreuteria", "paniculata", "","Golden Rain Tree","Koelreuteria_paniculata_branch.JPG","Branch of Koelreuteria paniculata",3,24)
        myPlant.plantName = "Wadas Memory Magnolia"
         var nameOfPlant = myPlant.plantName */

        /* var flower = Plant()
        var tree = Plant()

        var collectionOfPlants : ArrayList<Plant> = ArrayList()
        collectionOfPlants.add(flower)
        collectionOfPlants.add(tree)*/

        cameraButton = findViewById(R.id.btnOpenaCamera)
        photoGalleryButton = findViewById(R.id.btnPhotoGallery)
        /*button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)*/




        imageTaken = findViewById<ImageView>(R.id.imageTaken)

        cameraButton?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "The Camera Button is clicked", Toast.LENGTH_SHORT).show()

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, OPEN_CAMERA_BUTTON_REQUEST_ID)

        })
        photoGalleryButton?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "The Photo Gallery is opened", Toast.LENGTH_SHORT).show()

            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, OPEN_PHOTO_GALLERY_BUTTON_ID)

        })

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

        Toast.makeText(this, "The Button1 is clicked", Toast.LENGTH_SHORT).show()

        var myNumber = 20
        val myName: String = "Federico"
        var numbersOfLetters = myName.length

        var animalName: String? = null
        var numberOfCharactersOfAnimalName = animalName?.length ?: 100
    }

    fun button2IsClicked(buttonView: View) {

        Toast.makeText(this, "The Button2 is clicked", Toast.LENGTH_SHORT).show()
    }

    fun button3IsClicked(buttonView: View) {

        Toast.makeText(this, "The Button3 is clicked", Toast.LENGTH_SHORT).show()
    }

    fun button4IsClicked(buttonView: View) {

        Toast.makeText(this, "The Button4 is clicked", Toast.LENGTH_SHORT).show()
    }


    inner class DownloadingPlantTask : AsyncTask<String, Int, List<Plant>>() {

        override fun doInBackground(vararg params: String?): List<Plant>? {
            //can access Background thread.

            val downloadingObject =
                DownloadingObject()
            var jsonData = downloadingObject.downloadJSONDataFromLink("http://plantplaces.com/perl/mobile/flashcard.pl")
            Log.i("JSON", jsonData)

            return null
        }


        override fun onPostExecute(result: List<Plant>?) {
            super.onPostExecute(result)
            //can access User Interface thread.
        }

    }

    override fun onStart() {
        super.onStart()

        Toast.makeText(this, "The OnStart Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "The OnResume Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "The OnPause Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "The OnStop Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "The OnRestart Method is called", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "The OnDestroy Method is called", Toast.LENGTH_SHORT).show()
    }

    fun imageViewIsClicked(view: View) {


/*        val randomNumber : Int = (Math.random() * 6).toInt() + 1
        Log.i("TAG", "THE RANDOM BUTTON NUMBER IS: $randomNumber")

        if(randomNumber == 1){

            btnOpenaCamera.setBackgroundColor(Color.YELLOW)
        }

        else if ( randomNumber == 2){

            btnPhotoGallery.setBackgroundColor(Color.MAGENTA)
        }else if (randomNumber == 3){

            button1.setBackgroundColor(Color.WHITE)

        }else if (randomNumber == 4){

            button2.setBackgroundColor(Color.GREEN)
        }else if ( randomNumber == 5){

            button3.setBackgroundColor(Color.RED)
        }else if (randomNumber == 6){

            button4.setBackgroundColor(Color.BLUE)
        }
*/
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

}



