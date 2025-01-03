package com.BCU.testingapplication

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val profileImageUrl: Double? = null,
    val createdAt: Timestamp? = null,
    val byte: Double? = null
)