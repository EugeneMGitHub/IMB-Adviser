package com.example.shareappsettingswithgiraffe

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.shareappsettingswithgiraffe.data.database.AppDataBase
import com.example.shareappsettingswithgiraffe.data.database.FirstLoad
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


private const val  DB_NAME = "test"

@RunWith(AndroidJUnit4::class)
class MyDataBaseTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDataBase::class.java,
        // these are only auto migration with specs
        listOf(AppDataBase.Migration2To3()),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration1To2(){

        var db = helper.createDatabase(DB_NAME,1).apply {
            execSQL("INSERT INTO ShareNews VALUES('0','+','100','365','23.4','SBER')")
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME,2,true)

        db.query("SELECT * FROM ShareNews").apply {
           // Move to the first row of the table and make suse wi did that
            assertThat(moveToFirst()).isTrue()
            // find column index
            val columnIndex = getColumnIndex("opnPrice")
            // get value of the column first row
            val columnValue = getDouble(columnIndex)
            // make sure its equal to the default value
            assertThat(columnValue).isEqualTo(0)
            // or
//            assertThat(getDouble(getColumnIndex("opnPrice"))).isEqualTo(0)
        }


    }

    @Test
    fun migration2To3(){

        var db = helper.createDatabase(DB_NAME,2).apply {
            execSQL("INSERT INTO ShareNews VALUES('0','+','100','365','23.4','SBER','0')")
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME,3,true)

        db.query("SELECT * FROM ShareNews").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getDouble(getColumnIndex("currentPrice"))).isEqualTo(0)
        }

    }


    @Test
    fun migration3To4(){

        var db = helper.createDatabase(DB_NAME,3).apply {
            execSQL("INSERT INTO ShareNews VALUES('0','+','100','365','23.4','SBER','0')")
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME,4, true, AppDataBase.migration3To4)

        db.execSQL("INSERT INTO FirstLoad (isFirstCreatedId) VALUES('1')")


        val resultCursor = db.query("SELECT * FROM FirstLoad")
        assertThat(resultCursor.moveToFirst()).isTrue()

        val isFirstCreatedIdIndex = resultCursor.getColumnIndex("isFirstCreatedId")
        val valueOfIsFirstCreatedId = resultCursor.getInt(isFirstCreatedIdIndex)
        assertThat(valueOfIsFirstCreatedId).isEqualTo(1)

        val isFirstCreatedIndex = resultCursor.getColumnIndex("isFirstCreated")
        val valueOfIsFirstCreated = resultCursor.getInt(isFirstCreatedIndex)
        assertThat(valueOfIsFirstCreated).isEqualTo(0)

        resultCursor.close()


        /*or the same*/


        db.query("SELECT * FROM FirstLoad").apply {

            assertThat(moveToFirst()).isTrue()
            assertThat(getColumnIndex("isFirstCreated")).isNotNull()
            assertThat(getInt(getColumnIndex("isFirstCreated"))).isNotNull()

            val indexOfIsFirstCreatedId = getColumnIndex("isFirstCreatedId")
            val valueOfisFirstCreatedId = getInt(indexOfIsFirstCreatedId)
            assertThat(valueOfisFirstCreatedId).isEqualTo(1)

            val indexOfisFirstCreated = getColumnIndex("isFirstCreated")
            val valueOfisFirstCreated = getInt(indexOfisFirstCreated)
            assertThat(valueOfisFirstCreated).isEqualTo(0)

            close()

        }




    }


    @Test
    fun migration4To5(){


       var db = helper.createDatabase(DB_NAME,4)

        db = helper.runMigrationsAndValidate(DB_NAME,5,true)



        db.execSQL("INSERT INTO FirstLoad (isFirstCreatedId) VALUES ('1')")

        db.query("SELECT * FROM FirstLoad").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getString(getColumnIndex("dayOfDBUpdate"))).isEqualTo("2020-01-01")
        }
    }


    @Test
    fun migration5To6(){


        var db = helper.createDatabase(DB_NAME,5)

        db = helper.runMigrationsAndValidate(DB_NAME,6,true)



        db.execSQL("INSERT INTO FirstLoad (isFirstCreatedId) VALUES ('1')")

        db.query("SELECT * FROM FirstLoad").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getInt(getColumnIndex("isToUpdate"))).isEqualTo(0)
        }
    }


    /**To test all migrations at once*/

    @Test
    fun testAllMigrations(){

        helper.createDatabase(DB_NAME,1).apply {
            close()
        }

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDataBase::class.java,
            DB_NAME
        )
            .addMigrations(
                AppDataBase.migration3To4,
            ).build()
            .apply {
                openHelper.writableDatabase.close()
            }

    }



//    @Entity
//    data class ShareNews(
//        @PrimaryKey(autoGenerate = true)
//        val newsId: Int = 0,
//        val changeDirection: String,
//        val changePercent: String,
//        val changePeriod: String,
//        val comparisonTarget: String,
//        val secId: String,
//    )


}
