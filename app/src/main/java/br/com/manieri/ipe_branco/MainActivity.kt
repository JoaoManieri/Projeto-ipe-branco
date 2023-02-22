package br.com.manieri.ipe_branco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import br.com.manieri.ipe_branco.dataBase.AppDataBase

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val mainViewModel : MainViewModel by viewModels()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppDataBase.getInstance(this)

        val navHostFragment =supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        supportActionBar?.hide()

        //AppDataBase.getInstance(this)

    }
}