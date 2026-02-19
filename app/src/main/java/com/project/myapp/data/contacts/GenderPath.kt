package com.project.myapp.data.contacts

import kotlin.random.Random

enum class GenderPath(
    private val urlSegment: String,
) {
    WOMEN("women"),
    MEN("men");

    fun avatarUrl(id: Int): String = "https://randomuser.me/api/portraits/$urlSegment/$id.jpg"

    companion object {
        fun random(): GenderPath = if (Random.nextBoolean()) WOMEN else MEN
    }
}
