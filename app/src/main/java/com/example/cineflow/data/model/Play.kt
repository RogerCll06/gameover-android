package com.example.cineflow.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["category_id"])]
)
data class Play(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val synopsis: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Long?
)
