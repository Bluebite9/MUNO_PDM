package ro.pdm.muno_pdm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var navController: NavController
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.nav_view)
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.graph = navController.navInflater.inflate(R.navigation.nav_graph)
        NavigationUI.setupWithNavController(collapsingToolbarLayout, toolbar, navController, drawer)
        navigationView.setupWithNavController(navController)

        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
            navController.navigate(R.id.createProductFragment)
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            if (destination.id == R.id.viewProductListFragment) {
                fab.show()
            } else {
                fab.hide()
            }
        }

        navigationView.menu.findItem(R.id.createProductFragment).isVisible = false
        navigationView.menu.findItem(R.id.viewProductListFragment).isVisible = false
        navigationView.menu.findItem(R.id.viewAccountFragment).isVisible = false
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer)
    }
}