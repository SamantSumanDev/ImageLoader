package com.samant.acharyaassignment.ui.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.samant.acharyaassignment.imageLoader.ImageLoaderTask
import com.samant.acharyaassignment.R
import com.samant.acharyaassignment.ui.Model.Coverage

class MediaCoveragesAdapter(private val context: Context, private val data: MutableList<Coverage>) :
    RecyclerView.Adapter<MediaCoveragesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_media_coverage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.cancelTask()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.pauseImageLoading()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCover: ImageView = itemView.findViewById(R.id.imgMedia)
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val tvLanguage: AppCompatTextView = itemView.findViewById(R.id.tvLanguage)
        val tvPublisher: AppCompatTextView = itemView.findViewById(R.id.tvPublisher)

        var imageLoaderTask: ImageLoaderTask? = null

        fun bind(coverage: Coverage) {
            try {
                tvTitle.text = coverage.title
                tvLanguage.text = coverage.language
                tvPublisher.text = coverage.publishedBy
                cancelTask()
                val url =
                    coverage.thumbnail.domain + "/" + coverage.thumbnail.basePath + "/0/" + coverage.thumbnail.key
                imgCover.tag = url

                if (imageLoaderTask == null || imageLoaderTask?.status == AsyncTask.Status.FINISHED) {
                    imageLoaderTask = ImageLoaderTask(context, imgCover, R.drawable.ic_place_holder, R.drawable.ic_error_holder)
                    imageLoaderTask?.execute(url)
                }
            }catch (e:Exception){
                Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show()
            }


        }

        fun cancelTask() {
            imageLoaderTask?.cancel(true)
        }

        fun pauseImageLoading() {
            imageLoaderTask?.pauseImageLoading()
        }

    }

}
