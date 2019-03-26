package com.wineadvocate.bradixapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.wineadvocate.model.Photo
import com.wineadvocate.network.RequestInterface
import com.wineadvocate.network.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // private var listOfDataClassPhotos = ArrayList<Photo>()
    private var adapter:DataClassPhotoAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Debugging purposes
//        Picasso.get().load("https://via.placeholder.com/150/771796").fit().into(imageView)

        // @TODO: load DataClassPhoto/Photo from network (nisud here)
        val responsePubObservable = ServiceGenerator
            .createAPIService(RequestInterface::class.java, this)
            .getPhotos()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { listOfPhotos ->
                    handleResponse(listofPhotos = listOfPhotos as ArrayList<Photo>)
                },
                { error -> handleError(error) }
            )

//        Sample data
//        listOfDataClassPhotos.add(Photo("https://via.placeholder.com/150/92c952", "Title A"))
//        listOfDataClassPhotos.add(Photo("https://via.placeholder.com/150/92c952", "Title B"))
//        listOfDataClassPhotos.add(Photo("https://via.placeholder.com/150/92c952", "Title C"))
//        adapter = DataClassPhotoAdapter(this, listOfDataClassPhotos)
//        listview.adapter = adapter

    }

    private fun handleResponse(listofPhotos: ArrayList<Photo>) {

        adapter = DataClassPhotoAdapter(this, listofPhotos)
        listview.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    class DataClassPhotoAdapter: BaseAdapter {

        private var context: Context?= null
        private var listOfPhotos = ArrayList<Photo>()

        constructor(context:Context, listOfDataClassPhoto: ArrayList<Photo>): super() {
            this.context = context
            this.listOfPhotos = listOfDataClassPhoto
        }

        @SuppressLint("ViewHolder", "SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val photo = listOfPhotos[position]
            var photoView = LayoutInflater.from(context).inflate(R.layout.photos, parent, false)

            val thumbNail = photoView.findViewById(R.id._thumb_nail) as ImageView
            val title = photoView.findViewById(R.id._album_title) as TextView
            val albumId = photoView.findViewById<TextView>(R.id._album_id)

            val picasso: Picasso = Picasso.get()
            picasso.isLoggingEnabled = true

            picasso.load(photo.thumbnailUrl!!)
                .resize(150, 150).centerCrop().into(thumbNail)

            title.text = photo.title!!
            albumId.text = "Album id: #${photo.albumId!!}"

            photoView.setOnClickListener{
                val intent = Intent(context, AlbumActivity::class.java)
                intent.putExtra("albumId", photo.albumId)
                context!!.startActivity(intent)

                val activity: Activity = context as Activity
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out)
            }

            return photoView
        }

        override fun getItem(position: Int): Any {
            return listOfPhotos[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfPhotos.size
        }
    }
}
