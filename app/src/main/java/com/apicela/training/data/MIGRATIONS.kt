package com.apicela.training.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apicela.training.models.Exercise
import com.apicela.training.models.Workout
import com.google.gson.Gson

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
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Cria a tabela para a nova entidade
        database.execSQL("ALTER TABLE `Exercise` ADD COLUMN `metricType` TEXT NOT NULL DEFAULT 'CARGA'")

    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Cria a tabela para a nova entidade
        database.execSQL("ALTER TABLE `Execution` ADD COLUMN `isCardio` INTEGER NOT NULL DEFAULT 0")

    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            // Cria a tabela temporária
            database.execSQL(
                """
            CREATE TABLE Division_temp (
                id TEXT PRIMARY KEY NOT NULL,
                workoutId TEXT NOT NULL,
                name TEXT NOT NULL,
                image TEXT NOT NULL,
                listOfExercises TEXT NOT NULL,
                FOREIGN KEY(workoutId) REFERENCES Workout(id) ON DELETE CASCADE
            )
        """
            )

            // Pega os dados da tabela antiga
            var cursor =
                database.query("SELECT id, workoutId, name, image, listOfExercises FROM division")

            // Insere os dados convertidos na tabela temporária
            while (cursor.moveToNext()) {
                val id = cursor.getString(0)
                val workoutId = cursor.getString(1)
                val name = cursor.getString(2)
                val image = cursor.getString(3)
                val oldListOfExercises = cursor.getString(4) // Lista JSON

                // Converte a lista JSON para uma lista de IDs
                val listOfExercises = convertJsonToListOfIds(oldListOfExercises)

                // Insere na tabela temporária
                database.execSQL(
                    """
            INSERT INTO division_temp (id, workoutId, name, image, listOfExercises)
            VALUES (?, ?, ?, ?, ?)
            """, arrayOf(id, workoutId, name, image, listOfExercises)
                )
            }

            cursor.close()

            // Remove a tabela antiga
            database.execSQL("DROP TABLE division")

            // Renomeia a tabela temporária para o nome original
            database.execSQL("ALTER TABLE division_temp RENAME TO division")


            database.execSQL(
                """    
                    CREATE TABLE workout_temp (
                    id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                image TEXT NOT NULL,
                listOfDivision TEXT NOT NULL
            )
            """
            )

            // Pega os dados da tabela antiga
            cursor =
                database.query("SELECT id, name, description, image, listOfDivision FROM workout")

            // Insere os dados convertidos na tabela temporária
            while (cursor.moveToNext()) {
                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val description = cursor.getString(2)
                val image = cursor.getString(3)
                val oldListOfExercises = cursor.getString(4) // Lista JSON

                // Converte a lista JSON para uma lista de IDs
                val listOfDivision = convertJsonToListOfIds2(oldListOfExercises)

                // Insere na tabela temporária
                database.execSQL(
                    """
            INSERT INTO workout_temp (id, name, description, image, listOfDivision)
            VALUES (?, ?, ?, ?, ?)
            """, arrayOf(id, name, description, image, listOfDivision)
                )
            }

            cursor.close()

            // Remove a tabela antiga
            database.execSQL("DROP TABLE workout")

            // Renomeia a tabela temporária para o nome original
            database.execSQL("ALTER TABLE workout_temp RENAME TO workout")
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }

    }

    // Função para converter lista JSON em uma lista de IDs
    private fun convertJsonToListOfIds(json: String): String {
        // Supondo que você tenha uma biblioteca JSON como Gson ou Moshi
        val gson = Gson()
        val list = gson.fromJson(json, Array<Exercise>::class.java).map { it.id }
        return gson.toJson(list)
    }

    private fun convertJsonToListOfIds2(json: String): String {
        // Supondo que você tenha uma biblioteca JSON como Gson ou Moshi
        val gson = Gson()
        val list = gson.fromJson(json, Array<Workout>::class.java).map { it.id }
        return gson.toJson(list)
    }
}





