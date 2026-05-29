package com.example.cineflow.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class PlayWithCategory(
    @Embedded val play: Play,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: Category?
)
