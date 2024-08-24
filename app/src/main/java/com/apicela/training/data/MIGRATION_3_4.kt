package com.apicela.training.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Cria a tabela para a nova entidade
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `Observation` (
                `date` INTEGER NOT NULL PRIMARY KEY,
                `observation` TEXT NOT NULL
            )
            """
        )
    }
}
