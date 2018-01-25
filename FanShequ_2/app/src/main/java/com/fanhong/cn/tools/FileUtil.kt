package com.fanhong.cn.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import java.io.*

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtil {
    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    fun compressImage(bitmap: Bitmap, filename: String): File {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 1024) {  //循环判断如果压缩后图片是否大于1M,大于继续压缩
            baos.reset()//重置baos即清空baos
            options -= 10//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            val length = baos.toByteArray().size.toLong()
        }
        val file = File(filename)
        try {
            val fos = FileOutputStream(file)
            try {
                fos.write(baos.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

//        bitmap.recycle()
        return file
    }
}
