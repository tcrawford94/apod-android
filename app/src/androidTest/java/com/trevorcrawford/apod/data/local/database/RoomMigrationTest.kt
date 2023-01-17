package com.trevorcrawford.apod.data.local.database

import androidx.room.testing.MigrationTestHelper
import androidx.room.util.TableInfo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), AppDatabase::class.java)

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        // Given - DB version 1 with some data
        helper.createDatabase(TEST_DB, 1).apply {
            this.execSQL(
                "INSERT INTO 'astronomy_pictures' (uid,title,explanation,date,url,hdUrl) " +
                        "VALUES (1234,'title for migration','explanation for migration'," +
                        "'2023-01-15','https://google.com','https://android.google.com')"
            )
            close()
        }

        // When - re-open db with version 2, run migrations & validate...
        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true)

        // Then - verify that the data was migrated properly.
        val info = TableInfo.read(db, "astronomy_pictures")
        assertEquals(7, info.columns.size)

        val cursor = db.query(
            "SELECT title FROM 'astronomy_pictures' WHERE date = '2023-01-15'"
        )
        assertEquals(true, cursor.moveToNext())
        assertEquals("title for migration", cursor.getString(0))
    }

}
