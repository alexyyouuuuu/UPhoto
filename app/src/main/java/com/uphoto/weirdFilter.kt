package com.uphoto

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import java.io.OutputStream


class weirdFilter : Fragment() {

    private lateinit var image: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var bitcheckpoint: Bitmap
    private lateinit var ogImage: Bitmap
    private lateinit var invert: ImageButton
    private lateinit var monochrome: ImageButton
    private lateinit var eightbit: ImageButton
    private lateinit var toon: Button
    private lateinit var deepFry: Button
    private lateinit var flower: Button
    private lateinit var revert: Button
    private lateinit var finish: Button
    private lateinit var home: ImageButton
    private var height = 0
    private var width = 0
    private var dataPasser: OnDataPass? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weird_filter, container, false)
        invert = view.findViewById(R.id.invert)
        monochrome = view.findViewById(R.id.black_white)
        image = view.findViewById(R.id.imageView)
        eightbit = view.findViewById(R.id.eightbit)
        toon = view.findViewById(R.id.toon)
        deepFry = view.findViewById(R.id.deepfry)
        flower = view.findViewById(R.id.flower)
        revert = view.findViewById(R.id.revert)
        finish = view.findViewById(R.id.finish_button)
        home = view.findViewById(R.id.home)
        home.setOnClickListener{
            newFrag("main menu alt")
        }
        invert.setOnClickListener{
            invert()
        }
        monochrome.setOnClickListener {
            monochrome()
        }
        eightbit.setOnClickListener {
            eightBitmap()
        }
        toon.setOnClickListener {
            toon()
        }
        deepFry.setOnClickListener {
            deepFry()
        }
        flower.setOnClickListener {
            putflower()
        }
        revert.setOnClickListener{
            revert()
        }
        finish.setOnClickListener {
            context?.let { it1 -> saveImage(bitmap, it1, "Uphoto")
                newFrag("main menu")}

        }
        val activity: MainActivity? = activity as MainActivity?
        if (activity != null) {
            ogImage = activity.getbit()!!
        }
        bitmap = ogImage
        bitcheckpoint = ogImage

        height = bitmap.height
        width = bitmap.width
        if (height.toDouble()/width >= 1.75)
        {
            setDimensions(image, 1150)
        }
        if (height > 1150)
        {
            setDimensions(image, 1150)
        }

        image.setImageBitmap(bitmap)
        return view
    }
    private fun setDimensions(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.layoutParams = params
    }
    private fun revert()
    {
        image.setImageBitmap(ogImage)
        bitmap = ogImage
    }
    private fun putflower()
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val flower1 = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.flower1
        )
        val flower2 = BitmapFactory.decodeResource(
            requireContext().resources,
            R.drawable.flower2
        )
        val matrix1 = Matrix()
        val scale1w = 100f/flower1.width
        val scale1h = 100f/flower1.height
        matrix1.postScale(scale1w, scale1h)
        val resizedflower1 = Bitmap.createBitmap(
            flower1, 0, 0, flower1.width, flower1.height, matrix1, false
        )
        val matrix2 = Matrix()
        val scale2w = 100f/flower2.width
        val scale2h = 100f/flower2.height
        matrix2.postScale(scale2w, scale2h)
        val resizedflower2 = Bitmap.createBitmap(
            flower2, 0, 0, flower2.width, flower2.height, matrix2, false
        )
        val canvas = Canvas(ret)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        var x = 0
        var y = 0
        var flower_1 = true
        while (x+100<width)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, x.toFloat(), 0f, paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, x.toFloat(), 0f, paint)
                flower_1 = true
            }
            x=x+100
        }
        while (y+100<height)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, width-100f, y.toFloat(), paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, width-100f, y.toFloat(), paint)
                flower_1 = true
            }
            y=y+100
        }
        while (x>0)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, x.toFloat(), height-100f, paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, x.toFloat(), height-100f, paint)
                flower_1 = true
            }
            x=x-100
        }
        while (y>0)
        {
            if (flower_1)
            {
                canvas.drawBitmap(resizedflower1, 0f, y.toFloat(), paint)
                flower_1 = false
            }
            else
            {
                canvas.drawBitmap(resizedflower2, 0f, y.toFloat(), paint)
                flower_1 = true
            }
            y=y-100
        }

        image.setImageBitmap(ret)
    }

    private fun test()
    {

    }
    private fun invert()
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val invertFilter = GPUImageColorInvertFilter()
        gpuImage.setFilter(invertFilter)

        bitmap = gpuImage.getBitmapWithFilterApplied(ret)
        image.setImageBitmap(bitmap)
    }
    private fun monochrome()
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val monochromefilter = GPUImageMonochromeFilter(1f,  floatArrayOf(.3f,.3f,.3f,.3f))
        gpuImage.setFilter(monochromefilter)

        bitmap = gpuImage.getBitmapWithFilterApplied(ret)
        image.setImageBitmap(bitmap)
    }
    private fun eightBitmap() {
        val area = 1.0*height*width
        val factor = Math.sqrt(area/400).toInt()
        val heightscale = Math.ceil(height/factor.toDouble())
        val widthscale = Math.ceil(width/factor.toDouble())
        var x = 0
        var y = 0
        var xsquare = 0
        var ysquare = 0
        Log.d("main_activity", "widthscale: $widthscale, heightscale: $heightscale")
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        while(xsquare<widthscale)
        {
            while(ysquare<heightscale)
            {
                while (x<factor)
                {
                    var color = 0
                    if (factor*xsquare+x+5 < width && factor*ysquare+y+5 < height)
                    {
                        color = ret.getColor(factor*xsquare+5, factor*ysquare+5).toArgb()
                    }
                    else
                    {
                        color = ret.getColor(factor*xsquare, factor*ysquare).toArgb()
                    }

                    while (y<factor)
                    {
                        if (factor*xsquare+x < width && factor*ysquare+y < height)
                        {
                            ret.setPixel(factor*xsquare+x,factor*ysquare+y, color)
                        }
                        y++
                    }
                    x++
                    y=0
                }
                x = 0
                ysquare++
            }
            ysquare = 0
            xsquare++
        }

        bitmap = ret
        image.setImageBitmap(bitmap)
    }
    private fun toon()
    {
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val gpuImage = GPUImage(context)
        val toonfilter = GPUImageToonFilter(64f, 1f)
        gpuImage.setFilter(toonfilter)
        bitmap = gpuImage.getBitmapWithFilterApplied(ret)
        image.setImageBitmap(bitmap)
    }
    private fun deepFry()
    {
        val gpuImage = GPUImage(context)
        var ret: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val saturationFilter = GPUImageSaturationFilter(2f)
        gpuImage.setFilter(saturationFilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)

        val cm = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 50f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val canvas = Canvas(ret)

        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(ret, 0.toFloat(), 0.toFloat(), paint)
        val huefilter = GPUImageHueFilter(1f)
        gpuImage.setFilter(huefilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)
        val toonfilter = GPUImageToonFilter(20f, 3f)
        gpuImage.setFilter(toonfilter)
        ret = gpuImage.getBitmapWithFilterApplied(ret)
        val scaleWidth = 1f/1.2f
        val scaleHeight = 1f/1.2f
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        ret =Bitmap.createBitmap(
            ret, 0, 0, width, height, matrix, false
        )



        bitmap = ret

        image.setImageBitmap(bitmap)
    }
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {

        val values = contentValues()
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        // RELATIVE_PATH and IS_PENDING are introduced in API 29.

        val uri: Uri? =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
        }
    }
    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun newFrag(name: String)
    {
        dataPasser?.passBit(bitmap)
        dataPasser?.onDataPass(name)
    }
    companion object {
        fun newInstance():weirdFilter{
            return weirdFilter()
        }
    }
}