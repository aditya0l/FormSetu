package com.example.formsetu.data.repository

import android.content.Context
import com.example.formsetu.data.model.FormSchema
import com.google.gson.Gson

object FormRepository {

    fun loadFormSchema(context: Context, formPath: String): FormSchema? {
        return try {
            val jsonStr = context.assets.open(formPath)
                .bufferedReader().use { it.readText() }

            Gson().fromJson(jsonStr, FormSchema::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
