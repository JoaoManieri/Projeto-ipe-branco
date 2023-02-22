package br.com.manieri.ipe_branco.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.manieri.ipe_branco.dataBase.dao.UserDao
import br.com.manieri.ipe_branco.model.User


const val DATABASE_NAME = "ipeBranco.db"

@Database(
    entities = [
        User::class
    ], version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {


        private var APPDATABASE : AppDataBase?= null

        //singoton instance
        fun getInstance(context: Context? = null): AppDataBase {
            if (APPDATABASE == null)
                if (context == null)
                    throw java.lang.Exception("O contexto deve ser enviado pelo menos na primeira vez que o método getInstance(contexto) for chamado")
                else
                    synchronized(this) {
                        buildDatabase(context.applicationContext).also {
                            APPDATABASE = it
                        }
                    }
            return APPDATABASE!!
        }

        private fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
//                .allowMainThreadQueries()
//                .addMigrations(MenuMigrations.MIGRATION_1_2)
//                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
        }

    }
}