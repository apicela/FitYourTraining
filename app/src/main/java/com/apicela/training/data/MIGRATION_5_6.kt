package com.apicela.training.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Cria a tabela para a nova entidade
        database.execSQL("ALTER TABLE `Execution` ADD COLUMN `isCardio` INTEGER NOT NULL DEFAULT 0")

    }
}
