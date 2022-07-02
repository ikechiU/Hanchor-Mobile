package com.nextgendevs.hanchor.business.datasource.cache.auth

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "auth_token_table")
data class AuthTokenEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "tokenId")
    var tokenId: String = "",

    @ColumnInfo(name = "token")
    var token: String = "",
)
