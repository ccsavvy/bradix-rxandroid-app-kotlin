package com.wineadvocate.bradixapp

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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 *  Created by Christian on Tuesday Mar, 2019
 */

class AlbumActivity : AppCompatActivity() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private var adapter: PhotoAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle: Bundle = intent.extras
        val albumId:String = bundle.getString("albumId")

        val responsePubObservable = ServiceGenerator
            .createAPIService(RequestInterface::class.java, this)
            .getPhotos("2")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { listOfDataClassPhotos ->
                    handleResponse(listOfPhotos = listOfDataClassPhotos as ArrayList<Photo>)
                },
                { error -> handleError(error) }
            )

        disposable.add(responsePubObservable)
    }

    private fun handleResponse(listOfPhotos: ArrayList<Photo>) {

        adapter = PhotoAdapter(this, listOfPhotos)
        albumList.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    inner class PhotoAdapter: BaseAdapter {

        private var context: Context? = null
        private var listOfDataClassPhotos = ArrayList<Photo>()

        constructor(context: Context, listOfPhoto: ArrayList<Photo>): super() {

            this.context = context
            this.listOfDataClassPhotos = listOfPhoto
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val photo = listOfDataClassPhotos[position]
            var photoView = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)

            val thumbNail = photoView.findViewById(R.id._thumb_nail) as ImageView
            val title = photoView.findViewById(R.id.albumTitleLabel) as TextView
            val albumId = photoView.findViewById<TextView>(R.id.userIdLabel)

            val picasso: Picasso = Picasso.get()
            picasso.isLoggingEnabled = true

            picasso.load(photo.url!!)
                .resize(600, 600).centerCrop().into(thumbNail)

            title.text = photo.title!!
            albumId.text = "Album id: #${photo.albumId!!}"

//            photoView.setOnClickListener{
//                val intent = Intent(context, AlbumActivity::class.java)
//                intent.putExtra("albumId", photo.albumId)
//                context!!.startActivity(intent)
//            }

            return photoView
        }

        override fun getItem(position: Int): Any {
            return listOfDataClassPhotos[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfDataClassPhotos.size
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_to_right)
    }
}
