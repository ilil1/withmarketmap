package com.example.YUmarket.data.repository.like

import com.example.YUmarket.model.Model

interface LikeListRepository<T : Model> {
    suspend fun getAll(): List<T>
    suspend fun insert(model: T)
    suspend fun delete(model: T)
    suspend fun deleteAll()
}