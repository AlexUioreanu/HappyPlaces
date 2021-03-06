package com.example.happyplaces

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.happyplaces.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()

        binding.appBarMain.fab.setOnClickListener {
            val i = Intent(this,AddEditActivity::class.java)
            startActivity(i)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_about, R.id.nav_contact
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun share(item: MenuItem?) {
        intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
        val intent = this.intent.setType("text/plain")
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }


    fun openEmailLink(view: View?) {
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.contact_email))
        startActivity(intent)
    }

    fun openTwitterLink(view: View?) {
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.contact_twitter))
        startActivity(intent)
    }

    fun openLinkedInLink(view: View?) {
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.contact_linkedin))
        startActivity(intent)
    }

    fun openGithubLink(view: View?) {
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.contact_github))
        startActivity(intent)
    }
}