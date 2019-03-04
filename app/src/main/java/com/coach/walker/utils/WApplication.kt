package com.coach.walker.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import java.io.*
import java.nio.charset.Charset

/**
 * Created by pierre-alexandrevezinet on 20/02/2019.
 *
 */

class WApplication {
    companion object {
        fun launchActivity(from: Activity, to: Class<*>, closePreviousActivity: Boolean) {
            val i = Intent(from, to)
            from.startActivity(i)
            if (closePreviousActivity) {
                from.finish()
            }
        }

        /**
         * @param context
         * *
         * @param fontName
         * *
         * @return
         */
        fun getFont(context: Context, fontName: String): Typeface? {

            var typeface: Typeface? = null

            if (fontName != null) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + fontName)
            }

            return typeface
        }

        @SuppressLint("MissingPermission")
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun getJsonString(context: Context, filePath: String): String {
            var json: String? = null
            try {
                val `is` = context.assets.open(filePath)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                val UTF8_CHARSET = Charset.forName("UTF-8")
                json = String(buffer, UTF8_CHARSET)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return json!!

        }

        /*
        SampleSize is used to subsample the original image and return a smaller image, ie
        SampleSize == 4 returns an image that is 1/4 the width/height of the original.
        Quality is used to hint the compressor, input range is between 0-100. 0 meaning compress for small size,
        100 meaning compress for max quality
         */
        fun compressBitmap(file: File, sampleSize: Int, quality: Int): File {
            var selectedBitmap: Bitmap? = null
            var lengthInKb = file.length() / 1024 //in kb
            var lengthInKbBitMap = 0
            try {
                var options: BitmapFactory.Options = BitmapFactory.Options()
                options.inSampleSize = sampleSize
                var inputStream = FileInputStream(file)

                selectedBitmap = BitmapFactory.decodeStream(inputStream, null, options)
                var lengthBMInKb = selectedBitmap.allocationByteCount / 1024 //in kb
                inputStream.close()

                var outputStream = FileOutputStream(file.absolutePath)
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.close()
                lengthInKb = file.length() / 1024 //in kb
                lengthInKb = file.length() / 1024 //in kb
                lengthInKbBitMap = selectedBitmap.allocationByteCount / 1024
                selectedBitmap.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return file
        }

        fun resizeImage(file: File, scaleTo: Int = 500): Bitmap {
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor = Math.min(photoW / scaleTo, photoH / scaleTo)

            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor

            val resized = BitmapFactory.decodeFile(file.absolutePath, bmOptions)
            file.outputStream().use {
                resized.compress(Bitmap.CompressFormat.PNG, 100, it)
                //resized.recycle()
            }

            return resized
        }

        fun getImageUri(inContext: Context, inImage: Bitmap, quality: Int): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, quality, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "", null)
            return Uri.parse(path)
        }

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "", null)
            return Uri.parse(path)
        }

    }

}

