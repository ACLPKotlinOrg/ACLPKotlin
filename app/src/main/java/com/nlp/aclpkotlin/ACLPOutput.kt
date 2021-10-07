package com.nlp.aclpkotlin

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

//Test Comment

class ACLPOutput {
    companion object {
        fun outputFile(result: String, programName: String, activity: Activity) {
            /*val file = this.get().path.toString() + "/" + programName
            val f = File(file)
            f.writeText(result)*/
            val myExternalFile = File(activity.getExternalFilesDir("")?.parentFile, programName)
            try {
                val fileOutPutStream = FileOutputStream(myExternalFile)
                fileOutPutStream.write(result.toByteArray())
                fileOutPutStream.close()
                Log.i("ACLPOUTPUT","Success")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("ACLPOUTPUT","Fail")
            }

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("File Created Successfully!")
            builder.setMessage("File Directory: $myExternalFile\nFile Name: $programName")
            builder.show()
        }
    }
}