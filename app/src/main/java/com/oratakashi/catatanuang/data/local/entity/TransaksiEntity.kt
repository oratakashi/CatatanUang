package com.oratakashi.catatanuang.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class TransaksiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val nominal: Double,
    val kategori: String,
    val tipe: String, // "pemasukan" atau "pengeluaran"
    val catatan: String = "",
    val tanggal: Long = System.currentTimeMillis()
)

