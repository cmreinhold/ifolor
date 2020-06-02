package ch.reinhold.ifolor.core.di

import androidx.room.Room
import ch.reinhold.ifolor.data.db.IfolorDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val IFOLOR_DB_FILE = "Ifolor.db"

/**
 * Koin main module where all dependencies are defined.
 */
fun provideIfolorModule() = module {

    single {
        Room.databaseBuilder(androidContext(), IfolorDatabase::class.java, IFOLOR_DB_FILE)
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<IfolorDatabase>().dao()
    }

}
