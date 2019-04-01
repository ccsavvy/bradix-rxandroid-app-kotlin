package com.wineadvocate.bradixapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import com.wineadvocate.domain.PhotoViewState
import com.wineadvocate.model.DataClassPhoto
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MviActivity<PhotosView, PhotosPresenter>(), PhotosView {

    private val itemClickSubject = PublishSubject.create<DataClassPhoto>()

    override fun createPresenter() = PhotosPresenter()

    override fun showAlbumByIdIntent(albumId: String) = itemClickSubject

    override fun showPhotos() = Observable.just(true)!!

    override fun render(state: PhotoViewState) {

        when(state) {
            is PhotoViewState.LoadingState -> renderLoadingState()
            is PhotoViewState.DataState -> renderDataState(state)
            is PhotoViewState.ErrorState -> renderErrorState(state)
        }
    }

    private fun renderLoadingState() {
        loadingIndicator.visibility = View.VISIBLE
        listview.visibility = View.INVISIBLE
    }

    private fun renderDataState(dataState: PhotoViewState.DataState) {
        loadingIndicator.visibility = View.INVISIBLE
        listview.apply {
            visibility = View.VISIBLE
            adapter = DataClassPhotoAdapter(context, dataState.photos)
        }
    }

    private fun renderErrorState(errorState: PhotoViewState.ErrorState) {
        loadingIndicator.visibility = View.INVISIBLE
        listview.visibility = View.INVISIBLE
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // @TODO: load DataClassPhoto/Photo from repository (nisud here)
        // MVC architecture
    }

    private fun itemClickedListener(photo: DataClassPhoto?) {

        val intent = Intent(this, AlbumActivity::class.java)
        intent.putExtra("albumId", photo!!.albumId)
        this!!.startActivity(intent)

        val activity: Activity = this
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out)
    }

    inner class DataClassPhotoAdapter: BaseAdapter {

        private var context: Context?= null
        private var listOfPhotos = ArrayList<DataClassPhoto>()

        constructor(context:Context, listOfDataClassPhoto: ArrayList<DataClassPhoto>): super() {
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

            picasso.load(photo.url!!)
                .resize(150, 150).centerCrop().into(thumbNail)

            title.text = photo.title!!
            albumId.text = "Album id: #${photo.albumId!!}"

            photoView.clicks().map {
                itemClickedListener(photo)
            }.subscribe()

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
