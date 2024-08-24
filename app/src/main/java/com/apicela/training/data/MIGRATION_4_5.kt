package com.apicela.training.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Cria a tabela para a nova entidade
        database.execSQL("ALTER TABLE `Exercise` ADD COLUMN `metricType` TEXT NOT NULL DEFAULT 'CARGA'")

    }
}
