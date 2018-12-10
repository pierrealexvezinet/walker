package com.coach.walker.ws.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import com.coach.walker.R
import com.coach.walker.utils.WApplicationConstants
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.Years
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by pierre-alexandrevezinet on 07/12/2018.
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
         * @param dp
         * @param context
         * *
         * @return Int
         */
        fun convertDpToPx(dp: Int, context: Context): Int {
            return Math.round(dp * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }

        /**
         * @param dp
         * @param context
         * *
         * @return Int
         */
        fun dpToPx(dp: Int, context: Context): Int {
            return (dp * context.resources.displayMetrics.density.toInt())
        }

        /**
         * @param dp
         * @param context
         * *
         * @return Int
         */
        fun pxToDp(px: Int, context: Context): Int {
            return (px / context.resources.displayMetrics.density.toInt())
        }

        /**
         * @param emailStr
         * *
         * @return true or false
         */
        fun validateEmail(emailStr: String): Boolean {
            val matcher = android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr)
            return matcher.find()
        }

        /**
         * @param password
         * *
         * @return true or false
         */
        fun isValidPassword(password: String?): Boolean {
            if (password == null) {
                return false
            } else {
                val lengthPassword = password.length
                var countDigit = 0
                var countLetter = 0
                var countUpperLetter = 0
                var countSpecialCharacter = 0

                for (i in 0..lengthPassword - 1) {
                    val c = password[i]
                    if (Character.isDigit(c)) {
                        countDigit++
                    } else if (Character.isLetter(c)) {
                        countLetter++
                        if (Character.isUpperCase(c)) {
                            countUpperLetter++
                        }
                    } else {
                        countSpecialCharacter++
                    }
                }
                if (countDigit >= 1 && countLetter >= 1 && countUpperLetter >= 1 && lengthPassword >= 8 && countSpecialCharacter >= 1) {

                    return true
                } else {
                    return false
                }
            }
        }

        /**
         * @param bitmap
         * @param pixels
         * *
         * @return mutableBitmap
         */
        fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width, bitmap
                    .height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = pixels.toFloat()

            paint.setAntiAlias(true)
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) as Xfermode?
            canvas.drawBitmap(bitmap, rect, rect, paint)

            return output
        }

        /**
         * @param drawable
         * @param widthPixel
         * @param heightPixels
         * *
         * @return mDate
         */
        fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
            val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(mutableBitmap)
            drawable.setBounds(0, 0, widthPixels, heightPixels)
            drawable.draw(canvas)

            return mutableBitmap
        }

        /**
         * @param dateString
         * *
         * @return Date
         */
        fun getJJMMYYYYDate(dateString: String): Date? {
            val df = SimpleDateFormat("yyyy-MM-dd")
            var mDate: Date? = null
            try {
                mDate = df.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return mDate
        }

        /**
         * @param dateString
         * *
         * @return Date
         */
        fun getJJMMYYYYSimpleDate(dateString: String, format: String): Date? {
            val df = SimpleDateFormat(format)
            var mDate: Date? = null
            try {
                mDate = df.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return mDate
        }

        /**
         * @param dateString
         * *
         * @return Date
         */
        fun getyyyyMMddTHHmmssZDate(dateString: String): Date? {
            if (dateString.contains("/")) {
                dateString.replace("/", "-")
            }
            val df = SimpleDateFormat(WApplicationConstants.yyyyMMddTHHmmssSSSZ)
            var mDate: Date? = null
            try {
                mDate = df.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return mDate
        }

        /**
         * @param dateString
         * *
         * @return Date
         */
        fun getDate(format: String, dateString: String): Date? {
            val df = SimpleDateFormat(format, Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("GMT")
            var mDate: Date? = null
            try {
                mDate = df.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return mDate
        }

        /**
         * @param date
         * *
         * @return Date
         */
        fun convertDateToJJMMYYYY(date: Date): Date {
            var myDate = date
            System.out.println(myDate)
            return SimpleDateFormat("dd-MM-yyyy").parse(myDate.toString())
        }

        fun convertDateToSimpleDateFormat(date: Date): String {
            val tz = TimeZone.getTimeZone("UTC")
            val df = SimpleDateFormat(WApplicationConstants.yyyyMMddTHHmmZ) // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            val nowAsISO = df.format(date)

            return nowAsISO
        }

        /***********************************DATAS CHART METHODS*****************************/

        /**
         * @param date
         * *
         * @return Date
         */
        fun convertStringToSimpleDateFormat(date: String): Date {
            val tz = TimeZone.getTimeZone("GMT")
            val df = SimpleDateFormat(WApplicationConstants.yyyyMMddTHHmmssSSSZ) // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            val nowAsISO = df.parse(date)

            return nowAsISO
        }

        fun getDaysListInYear(dateRegister: Date): ArrayList<String> {
            var listDaysOInYear: ArrayList<String> = ArrayList()
            var registeredDate: Date = dateRegister
            var currentDate = Date()

            val tz = TimeZone.getTimeZone("GMT")
            val df = SimpleDateFormat(WApplicationConstants.EEEMMMddHHmmsszzzyyyy) // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            var diffJours = getDateDiff(registeredDate, currentDate)
            diffJours += 1

            val cal = Calendar.getInstance()
            cal.time = currentDate
            var datePlus1 = cal.time
            var datePlus1_jjMMyyyy = SimpleDateFormat("dd/MM/yyyy").format(datePlus1).toString()
            listDaysOInYear.add(datePlus1_jjMMyyyy)

            for (i in 0 until diffJours) {
                val cal = Calendar.getInstance()
                cal.time = registeredDate
                cal.add(Calendar.DATE, i)
                var datePlus1 = cal.time
                var datePlus1_jjMMyyyy = SimpleDateFormat("dd/MM/yyyy").format(datePlus1).toString()
                listDaysOInYear.add(datePlus1_jjMMyyyy)
            }

            return listDaysOInYear
        }

        /**
         * Get a diff between two dates
         *
         * @param oldDate the old date
         * @param newDate the new date
         * @return the diff value, in the days
         */
        fun getDateDiff(oldDate: Date, newDate: Date): Int {
            try {
                return TimeUnit.DAYS.convert(newDate.time - oldDate.time, TimeUnit.MILLISECONDS).toInt()
            } catch (e: Exception) {
                e.printStackTrace()
                return 0
            }

        }

        /**
         * *
         * @return ArrayList<String>
         */
        fun getDayOfMonthsInYear(): ArrayList<String> {
            var listMonthInYear: ArrayList<String> = ArrayList()
            return listMonthInYear

        }

        /**
         * @param registeredDate
         * *
         * @return  ArrayList<String>
         */
        fun getMonthsInYear(registeredDate: Date): ArrayList<String> {
            var listMonth: ArrayList<String> = ArrayList()
            var beginDate: Date = registeredDate
            var actualDate = Date()
            while (beginDate.before(actualDate)) {
                // add one month to date per loop

                var monthPlus = DateTime(beginDate).plusMonths(1)
                var month = getMonthOfDate(monthPlus.toDate())
                beginDate = monthPlus.toDate()
                listMonth.add(getMonthInString(month, "fr_FR"))
            }

            return listMonth
        }

        fun addOrSubstractDaysToDate(date: Date, nbDayToAdd: Int): String {
            val calItemDate = Calendar.getInstance()
            calItemDate.time = date
            calItemDate.add(Calendar.DATE, nbDayToAdd)

            var year = calItemDate.get(Calendar.YEAR).toString()
            var monthOfYear = (calItemDate.get(Calendar.MONTH) + 1).toString()
            var dayOfMonth = calItemDate.get(Calendar.DAY_OF_MONTH).toString()

            if (dayOfMonth.length == 1) {
                dayOfMonth = "0" + dayOfMonth
            }

            if (monthOfYear.length == 1) {
                monthOfYear = "0" + monthOfYear
            }

            var selectedDateAppointmentSimpleFormat = "$year-$monthOfYear-$dayOfMonth"
            var dateThour = selectedDateAppointmentSimpleFormat + "T" + "00:00:00" + "+00:00"

            return dateThour
        }

        /**
         * @param date
         * *
         * @return Int
         */
        fun getDayOfDate(date: Date): Int {
            var simpleDateformat = SimpleDateFormat("EEEE") // the day of the week spelled out completely
            println(simpleDateformat.format(date))

            val calendar = Calendar.getInstance()
            calendar.time = date
            val day_of_week = calendar.get(Calendar.DAY_OF_WEEK)
            return day_of_week
        }

        /**
         * @param date
         * *
         * @return  Int
         */
        fun getMonthOfDate(date: Date): Int {
            var simpleDateformat = SimpleDateFormat("MM") // the day of the week spelled out completely
            println(simpleDateformat.format(date))

            val calendar = Calendar.getInstance()
            calendar.time = date
            val month = calendar.get(Calendar.MONTH)
            return month
        }

        /**
         * @param format
         * @param input
         * *
         * @return  Int
         */
        fun getWeekOfYear(format: String, input: String): Int {
            try {
                val df = SimpleDateFormat(format)
                val date = df.parse(input)
                val cal = Calendar.getInstance()
                cal.time = date
                val week = cal.get(Calendar.WEEK_OF_YEAR)
                return week
            } catch (e: ParseException) {
                System.out.println("Could not find a week in " + input)
                return 0
            }
        }

        /**
         * @param value
         * @param lang
         * *
         * @return  String
         */
        fun getDayOfWeek(value: Int, lang: String): String {
            var day: String = ""
            if (lang.equals("fr_FR")) {
                when (value) {
                    1 -> {
                        day = "DIMANCHE"
                    }
                    2 -> {
                        day = "LUNDI"
                    }
                    3 -> {
                        day = "MARDI"
                    }
                    4 -> {
                        day = "MERCREDI"
                    }
                    5 -> {
                        day = "JEUDI"
                    }
                    6 -> {
                        day = "VENDREDI"
                    }
                    7 -> {
                        day = "SAMEDI"
                    }
                }
            }

            if (lang.equals("en_EN")) {
                when (value) {
                    1 -> {
                        day = "SUNDAY"
                    }
                    2 -> {
                        day = "MONDAY"
                    }
                    3 -> {
                        day = "TUESDAY"
                    }
                    4 -> {
                        day = "WEDNESDAY"
                    }
                    5 -> {
                        day = "THURSDAY"
                    }
                    6 -> {
                        day = "FRIDAY"
                    }
                    7 -> {
                        day = "SATURDAY"
                    }
                }

            }
            return day
        }

        /**
         * @param value
         * @param lang
         * *
         * @return  String
         */
        fun getMonthInString(value: Int, lang: String): String {
            var month = ""

            if (lang.equals("fr_FR")) {
                when (value) {
                    1 -> {
                        month = "JANVIER"
                    }
                    2 -> {
                        month = "FEVRIER"
                    }
                    3 -> {
                        month = "MARS"
                    }
                    4 -> {
                        month = "AVRIL"
                    }
                    5 -> {
                        month = "MAI"
                    }
                    6 -> {
                        month = "JUIN"
                    }
                    7 -> {
                        month = "JUILLET"
                    }
                    8 -> {
                        month = "AOUT"
                    }
                    9 -> {
                        month = "SEPTEMBRE"
                    }
                    10 -> {
                        month = "OCTOBRE"
                    }
                    11 -> {
                        month = "NOVEMBRE"
                    }
                    12 -> {
                        month = "DECEMBRE"
                    }
                }
            }
            if (lang.equals("en_EN")) {
                when (value) {
                    1 -> {
                        month = "JANUARY"
                    }
                    2 -> {
                        month = "FEBRUARY"
                    }
                    3 -> {
                        month = "MARCH"
                    }
                    4 -> {
                        month = "APRIL"
                    }
                    5 -> {
                        month = "MAY"
                    }
                    6 -> {
                        month = "JUNE"
                    }
                    7 -> {
                        month = "JULY"
                    }
                    8 -> {
                        month = "AUGUST"
                    }
                    9 -> {
                        month = "SEPTEMBER"
                    }
                    10 -> {
                        month = "OCTOBER"
                    }
                    11 -> {
                        month = "NOVEMBER"
                    }
                    12 -> {
                        month = "DECEMBER"
                    }
                }
            }
            return month
        }

        /**
         * @param first
         * *
         * @return Int
         */
        fun getUserAge(first: Date): Int {

            val day = DateFormat.format("dd", first) as String
            val monthNumber = DateFormat.format("MM", first) as String
            val year = DateFormat.format("yyyy", first) as String

            val birthdate = LocalDate(year.toInt(), monthNumber.toInt(), day.toInt())
            val now = LocalDate()
            val age = Years.yearsBetween(birthdate, now)

            var ageToString: String = age.toString()
            if (age.toString().contains("P")) {
                ageToString = ageToString.replace("P", "")
            }
            if (age.toString().contains("Y")) {
                ageToString = ageToString.replace("Y", "")
            }
            return ageToString.toInt()
        }

        /**
         * @param month
         * *
         * @return  Int
         */
        fun getMinDayInAMonth(month: Int): Int {
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, month)
            val dayMin = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
            return dayMin

        }

        /**
         * @param month
         * *
         * @return  Int
         */
        fun getMaxDayInAMonth(month: Int): Int {
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, month)
            val dayMax = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            return dayMax
        }

        /**
         * @param min
         * @param max
         * *
         * @return  ArrayList<String
         */
        fun getListDaysInIntegerFromMinAndMax(min: Int, max: Int): ArrayList<String> {
            var listIntegerDays: ArrayList<String> = ArrayList()
            var min = min
            var max = max + 1
            for (j in min until max) {
                listIntegerDays.add(j.toString())
            }

            return listIntegerDays
        }

        /***********************************END DATAS CHART METHOD**************************/

        fun getDateISO8601FormatForCurrentDay(): String {
            var simpleDateOfTheDay = Date()
            var calItemDate = Calendar.getInstance()
            calItemDate.time = simpleDateOfTheDay
            var dateDay = calItemDate.get(Calendar.DAY_OF_MONTH).toString()
            var dateMonth = (calItemDate.get(Calendar.MONTH) + 1).toString()
            var dateYear = calItemDate.get(Calendar.YEAR).toString()

            if (dateDay.length == 1) {
                dateDay = "0" + dateDay
            }
            if (dateMonth.length == 1) {
                dateMonth = "0" + dateMonth
            }
            var dateValueConcat = "$dateYear-$dateMonth-$dateDay"
            var finalDateIso8601 = dateValueConcat + "T" + "00:00:00" + "+01:00"

            return finalDateIso8601
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

            if (fontName == WApplicationConstants.OPEN_SANS_BOLD) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_BOLD)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_BOLD_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_BOLD_ITALIC)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_EXTRA_BOLD) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_EXTRA_BOLD)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_LIGHT) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_LIGHT)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_ITALIC)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_EXTRA_BOLD_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_EXTRA_BOLD_ITALIC)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_LIGHT_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_LIGHT_ITALIC)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_SEMI_BOLD) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_SEMI_BOLD)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_SEMI_BOLD_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_SEMI_BOLD_ITALIC)
            }

            if (fontName == WApplicationConstants.OPEN_SANS_REGULAR) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.OPEN_SANS_REGULAR)
            }

            if (fontName == WApplicationConstants.GOTHAM_BLACK) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_BLACK)
            }

            if (fontName == WApplicationConstants.GOTHAM_BLACK_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_BLACK_ITALIC)
            }

            if (fontName == WApplicationConstants.GOTHAM_BOLD) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_BOLD)
            }

            if (fontName == WApplicationConstants.GOTHAM_BOLD_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_BOLD_ITALIC)
            }
            if (fontName == WApplicationConstants.GOTHAM_BOOK) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_BOOK)
            }

            if (fontName == WApplicationConstants.GOTHAM_BOOK_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_BOOK_ITALIC)
            }

            if (fontName == WApplicationConstants.GOTHAM_LIGHT) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_LIGHT)
            }

            if (fontName == WApplicationConstants.GOTHAM_LIGHT_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_LIGHT_ITALIC)
            }

            if (fontName == WApplicationConstants.GOTHAM_MEDIUM) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_MEDIUM)
            }

            if (fontName == WApplicationConstants.GOTHAM_MEDIUM_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_MEDIUM_ITALIC)
            }

            if (fontName == WApplicationConstants.GOTHAM_THIN) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_THIN)
            }

            if (fontName == WApplicationConstants.GOTHAM_THIN_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_THIN_ITALIC)
            }

            if (fontName == WApplicationConstants.GOTHAM_ULTRA) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_ULTRA)
            }

            if (fontName == WApplicationConstants.GOTHAM_ULTRA_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_ULTRA_ITALIC)
            }

            if (fontName == WApplicationConstants.GOTHAM_X_LIGHT) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_X_LIGHT)
            }

            if (fontName == WApplicationConstants.GOTHAM_X_LIGHT_ITALIC) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_X_LIGHT_ITALIC)
            }

            if (fontName == WApplicationConstants.GOTHAM_LIGHT_OTF) {
                typeface = Typeface.createFromAsset(context.assets, "fonts/" + WApplicationConstants.GOTHAM_LIGHT_OTF)
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

        @RequiresApi(Build.VERSION_CODES.O)
        fun encodeFileToBase64Binary(fileName: String): String {
            var file = File(fileName)
            var encoded = android.util.Base64.encode(file.readBytes(), android.util.Base64.DEFAULT)
            //var encoded = Base64.getEncoder().encode(file.readBytes())
            return String(encoded, StandardCharsets.US_ASCII)
        }

        fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri): String {
            val stringsOrNulls = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor = context.contentResolver.query(contentUri, stringsOrNulls, null, null, null)
            var columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor!!.getString(columnIndex)
        }

        @SuppressLint("NewApi")
        fun getRealPathFromURI_API19(context: Context, uri: Uri): String {
            var filePath = ""
            var wholeID = DocumentsContract.getDocumentId(uri)

            // Split at colon, use second item in the array
            var id = wholeID.split(":")[1]
            val column = arrayOf(MediaStore.Images.Media.DATA)
            val stringsOrNulls = arrayOf(id)
            stringsOrNulls[0] = MediaStore.Images.Media.DATA

            var stringsId = arrayOfNulls<String>(1)
            stringsId[0] = id

            // where id is equal to
            var sel = MediaStore.Images.Media._ID + "=?"

            var cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, stringsId, null)

            var columnIndex = cursor.getColumnIndex(column[0])

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
            return filePath
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

        fun scaleImage(context: Context, view: ImageView, drawable: BitmapDrawable) {
            // Get bitmap from the the ImageView.
            var bitmap: Bitmap? = null

            try {
                var drawing: Drawable = drawable
                bitmap = (drawing as BitmapDrawable).bitmap
            } catch (e: NullPointerException) {
                throw  NoSuchElementException("No drawable on given view")
            } catch (e: ClassCastException) {
                // Check bitmap is Ion drawable
            }

            // Get current dimensions AND the desired bounding box
            var width = 0

            try {
                width = bitmap!!.width
            } catch (e: NullPointerException) {
                throw NoSuchElementException("Can't find bitmap on given view/drawable")
            }

            var height = bitmap.getHeight()
            var bounding = dpToPx(250, context)
            Log.i("Test", "original width = " + Integer.toString(width))
            Log.i("Test", "original height = " + Integer.toString(height))
            Log.i("Test", "bounding = " + Integer.toString(bounding))

            // Determine how much to scale: the dimension requiring less scaling is
            // closer to the its side. This way the image always stays inside your
            // bounding box AND either x/y axis touches it.
            var xScale: Float = (bounding / width).toFloat()
            var yScale: Float = (bounding / height).toFloat()
            var scale: Float? = null
            if (xScale <= yScale) {
                scale = xScale
            } else {
                scale = yScale
            }

            // Create a matrix for the scaling and add the scaling data
            var matrix = Matrix()
            matrix!!.postScale(scale, scale)

            // Create a new bitmap and convert it to a format understood by the ImageView
            var scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
            width = scaledBitmap.width // re-use
            height = scaledBitmap.height // re-use
            var result: BitmapDrawable = BitmapDrawable(scaledBitmap)
            Log.i("Test", "scaled width = " + Integer.toString(width))
            Log.i("Test", "scaled height = " + Integer.toString(height))

            // Apply the scaled bitmap
            view.setImageDrawable(result)

            // Now change ImageView's dimensions to match the scaled image
            var params: ViewGroup.LayoutParams = view.layoutParams
            params.width = (width * 1.5).toInt()
            params.height = (height * 1.5).toInt()
            view.layoutParams = params
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

        fun cleanUserInput(mString: String): String {
            return mString.trim()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun saveImageInBase64(context: Context, mSelectedImage: Uri): String {
            var selectedImage: Uri = mSelectedImage
            var photo: Bitmap? = null
            var selectedBitMapRotated: Bitmap? = null
            var imageRealPath: String = ""
            var file: File? = null
            var dir: File? = null
            var newFile: File? = null
            var fos: FileOutputStream? = null
            var decodedString: ByteArray? = null
            var decodedImage: Bitmap? = null

            imageRealPath = WApplication.getRealPathFromURI_BelowAPI11(context, selectedImage!!)
            file = File(imageRealPath)
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            newFile = File(dir.absoluteFile, file.name)
            newFile.createNewFile()
            fos = FileOutputStream(newFile, true)
            fos.write(file.readBytes())
            fos.flush()
            fos.close()
            return encodeFileToBase64Binary(newFile.path)
        }

        fun isAppRunning(context: Context, packageName: String): Boolean {
            var activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            var procInfos: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses;
            if (procInfos != null) {
                for (i in 0 until procInfos.size) {
                    var processInfo = procInfos.get(i)
                    if (processInfo.processName.equals(packageName)) {
                        return true;
                    }
                }
            }
            return false;
        }

        fun getBitmapFromURL(url: URL): Bitmap {
            var bm: Bitmap? = null
            try {
                var conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (conn.responseCode != 200) {
                    return bm!!
                }
                conn.connect()
                var inputStream = conn.inputStream;

                var bis = BufferedInputStream(inputStream)
                try {
                    bm = BitmapFactory.decodeStream(bis);
                } catch (ex: OutOfMemoryError) {
                    Log.d(WApplicationConstants.LOG_WALKER, ex.message)
                }
                bis.close();
                inputStream.close()
            } catch (e: Exception) {
                Log.d(WApplicationConstants.LOG_WALKER, e.message)
            }
            return bm!!
        }

        fun handleToolbarTitleVisibility(percentage: Float, mTitle: View, titleIsVisible: Boolean): Boolean {
            var mIsTheTitleVisible = titleIsVisible
            if (percentage >= WApplicationConstants.PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

                if (!mIsTheTitleVisible) {
                    startAlphaAnimation(mTitle, WApplicationConstants.ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                    mIsTheTitleVisible = true;
                }

            } else {

                if (mIsTheTitleVisible) {
                    startAlphaAnimation(mTitle, WApplicationConstants.ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                    mIsTheTitleVisible = false;
                }
            }

            return mIsTheTitleVisible
        }

        fun handleAlphaOnTitle(percentage: Float, mTitleContainer: View, containerIsVisible: Boolean): Boolean {

            var mIsTheTitleContainerVisible = containerIsVisible
            if (percentage >= WApplicationConstants.PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
                if (mIsTheTitleContainerVisible) {
                    startAlphaAnimation(mTitleContainer, WApplicationConstants.ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                    mIsTheTitleContainerVisible = false
                }

            } else {

                if (!mIsTheTitleContainerVisible) {
                    startAlphaAnimation(mTitleContainer, WApplicationConstants.ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                    mIsTheTitleContainerVisible
                }
            }

            return mIsTheTitleContainerVisible
        }

        fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
            var alphaAnimation: AlphaAnimation? = null

            if (visibility == View.VISIBLE) {
                alphaAnimation = AlphaAnimation(0f, 1f)
            } else {
                alphaAnimation = AlphaAnimation(1f, 0f)
            }

            alphaAnimation.duration = duration;
            alphaAnimation.fillAfter = true
            v.startAnimation(alphaAnimation)
        }

        fun decimalToRational(d: Double): String {
            val ds = d.toString().trimEnd('0').trimEnd('.')
            val index = ds.indexOf('.')
            if (index == -1) return (ds.toLong() / 1L).toString()
            var num = ds.replace(".", "").toLong()
            var den = 1L
            for (n in 1..ds.length - index - 1) den *= 10L
            while (num % 2L == 0L && den % 2L == 0L) {
                num /= 2L
                den /= 2L
            }
            while (num % 5L == 0L && den % 5L == 0L) {
                num /= 5L
                den /= 5L
            }

            return "$num/$den"
        }

        fun getColorWithAlpha(color: Int, ratio: Float): Int {
            var newColor = 0
            val alpha = Math.round(Color.alpha(color) * ratio)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            newColor = Color.argb(alpha, r, g, b)
            return newColor
        }

        fun getSpecificsStrings(str: String): List<Int> {
            var listResStrings: ArrayList<Int> = ArrayList()
            val fields = R.string::class.java.fields
            for (field in fields) {
                val name = field.name //name of string
                try {
                    val id = field.getInt(R.string::class.java) //id of string
                    if (name.contains(str)) {
                        listResStrings.add(id)
                    }
                } catch (ex: Exception) {
                    //do smth
                }

            }

            return listResStrings
        }

    }

}
