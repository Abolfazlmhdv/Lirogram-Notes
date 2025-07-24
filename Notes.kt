package com.example.lirogram
data class Notes(val title: String, val caption: String,val date: String = System.currentTimeMillis().toString(),val imagePath: String? = null)