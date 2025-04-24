package com.example.domain.entity

import com.sun.jndi.toolkit.url.Uri

class Music(
    val title: String,
    val artist: String,
    val albumUri: Uri? = null
)