package com.samant.acharyaassignment.imageLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.widget.ImageView
import java.io.File
import java.lang.ref.WeakReference
import android.util.LruCache
import java.io.FileOutputStream
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.drawable.TransitionDrawable
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ImageLoaderTask(
    private val context: Context,
    private val imageView: ImageView,
    private val placeholderResId: Int,
    private val errorPlaceholderResId: Int
) : AsyncTask<String, Void, Bitmap>() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val imageViewRef: WeakReference<ImageView> = WeakReference(imageView)
    private var imageUrl: String? = null
    private var isPaused: Boolean = false

    override fun doInBackground(vararg params: String): Bitmap? {
        imageUrl = params[0]
        if (isPaused) {
            return null
        }
        // Load image from disk cache or network
        return loadImageFromCacheOrNetwork(context, imageUrl!!)
    }

    override fun onPreExecute() {
        imageViewRef.get()?.setImageResource(placeholderResId)
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        if (!isPaused) {
            val crossfadeDuration = 500 // Adjust crossfade duration as needed
            val currentDrawable = imageView.drawable
            val bitmapDrawable = BitmapDrawable(context.resources, bitmap ?: BitmapFactory.decodeResource(context.resources, placeholderResId))

            val crossfadeDrawable = TransitionDrawable(arrayOf(currentDrawable, bitmapDrawable))
            imageView.setImageDrawable(crossfadeDrawable)
            crossfadeDrawable.startTransition(crossfadeDuration)
        }
    }

    fun pauseImageLoading() {
        isPaused = true
    }

    override fun onCancelled() {
        imageViewRef.clear()
    }

    // load image from memory or disk cache, or network
    private fun loadImageFromCacheOrNetwork(context: Context, imageUrl: String): Bitmap? {
        try {
            if (imageUrl == null) return null
            val context = contextRef.get() ?: return null
            val memoryCache = getMemoryCache(context)
            val cachedBitmap = memoryCache.get(imageUrl)

            try {
                // First, try loading from memory cache
                if (cachedBitmap != null) {
                    return cachedBitmap
                }
            }catch (e:Exception){
                Log.e("ImageLoader: Cache",e.toString())
            }

            val diskCacheDir = context.cacheDir
            val file = File(diskCacheDir, imageUrl.hashCode().toString())
            try {
                // If not found in memory cache, try loading from disk cache
                if (file.exists() && cachedBitmap == null) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    if (bitmap != null) {
                        memoryCache.put(imageUrl, bitmap)
                        return bitmap
                    }
                }
            }catch (e:Exception){
                Log.e("ImageLoader: Disk",e.toString())
            }

            try {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val url = URL(imageUrl)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.doInput = true
                        connection.connect()
                        val inputStream = connection.inputStream
                        val bufferedInputStream = BufferedInputStream(inputStream)
                        val bitmap = BitmapFactory.decodeStream(bufferedInputStream)

                        memoryCache.put(imageUrl, bitmap)
                        saveImageToDiskCache(context, imageUrl, bitmap)

                        GlobalScope.launch(Dispatchers.Main) {
                            imageView.setImageBitmap(bitmap)
                        }
                    } catch (e: Exception) {
                        Log.e("ImageLoader: Remote", e.toString())
                    }
                }

            }catch (e:Exception){
                Log.e("ImageLoader: Remote",e.toString())
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // get LRU memory cache
    private fun getMemoryCache(context: Context): LruCache<String, Bitmap> {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        return object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }
    }

    // save image to disk cache
    private fun saveImageToDiskCache(context: Context, imageUrl: String, bitmap: Bitmap) {
        val diskCacheDir = context.cacheDir // Use cache directory for disk cache
        val file = File(diskCacheDir, imageUrl.hashCode().toString())
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
    }

}