package com.example.shareappsettingswithgiraffe.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
entities = [
    ShareEntity::class,
    ShareHistory::class,
    ShareNews::class,
    NewsUpdateStrategy::class,
    FirstLoad::class,
],
    version = 6,
    autoMigrations = [
        // this type of migration doesn't need anything (when we add a column)
        AutoMigration(from = 1, to = 2),
        // this type we use when we change name of an existing column etc.
        AutoMigration(from = 2, to = 3, spec = AppDataBase.Migration2To3::class),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6)
    ]
)

abstract class AppDataBase: RoomDatabase() {
    abstract val dataBaseDao : DataBaseDao

//     here we specify spec for migration from 2 to 3
    @RenameColumn(tableName = "ShareNews", fromColumnName = "opnPrice", toColumnName = "currentPrice")
    class Migration2To3 : AutoMigrationSpec

    companion object {
        // This migration we use whe we do migration manually
        // also we mention this migration in DataBaseModule (.addMigrations(AppDataBase.migration3To4))
        val migration3To4 = object : Migration(3,4){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS FirstLoad (isFirstCreatedId INTEGER NOT NULL PRIMARY KEY, isFirstCreated INTEGER NOT NULL DEFAULT 0)"
                )
            }
        }
    }

}


