package com.wineadvocate.bradixapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.wineadvocate.extension.getProgressbarDrawable
import com.wineadvocate.extension.loadImage
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

class PhotoActivity : AppCompatActivity() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private var adapter: PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        val responsePubObservable = ServiceGenerator
            .createAPIService(RequestInterface::class.java)
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
        adapter = PhotoAdapter(listOfPhotos)
        albumList.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    class PhotoAdapter(listOfPhoto: ArrayList<Photo>) : BaseAdapter() {

        private var photos = listOfPhoto
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val photoViewHolder: PhotoViewHolder
            if (convertView == null) {
                photoViewHolder = PhotoViewHolder()
                photoViewHolder.position = position
                convertView?.tag = photoViewHolder
            } else photoViewHolder = convertView.tag as PhotoViewHolder


            photoViewHolder.albumId?.text = photos[position].albumId
            photoViewHolder.photoTitle?.text = photos[position].title
            convertView?.context?.let {
                photoViewHolder.thumbNailUrl
                    ?.loadImage(photos[position].thumbnailUrl,
                        getProgressbarDrawable(context = it))
            }

            return convertView
        }

        override fun getItem(position: Int): Any {
            return photos[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return photos.size
        }

    }

    internal class PhotoViewHolder {
        val albumId: TextView? = null
        val photoTitle: TextView? = null
        val thumbNailUrl: ImageView? = null
        var position: Int = 0
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_to_right)
    }


}
