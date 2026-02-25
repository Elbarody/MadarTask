package com.madar.madartask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.madar.madartask.ui.navigation.NavGraph
import com.madar.madartask.ui.theme.MadarTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadarTaskTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}