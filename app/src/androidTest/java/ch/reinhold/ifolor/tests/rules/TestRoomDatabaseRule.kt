package ch.reinhold.ifolor.tests.rules;

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.Description
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
class TestRoomDatabaseRule<DB : RoomDatabase>(private val clazz: KClass<DB>) :
    InstantTaskExecutorRule() {

    lateinit var database: DB

    val testDispatcher = TestCoroutineDispatcher()
    val testScope = TestCoroutineScope(testDispatcher)

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)

        val context = getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(context, clazz.java)
            .build()
    }

    override fun finished(description: Description?) {
        database.close()
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }
}
