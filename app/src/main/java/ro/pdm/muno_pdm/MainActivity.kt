package ro.pdm.muno_pdm

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.account.service.AccountService
import ro.pdm.muno_pdm.utils.session.SessionService
import ro.pdm.muno_pdm.utils.shared.Constants


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var navController: NavController
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    lateinit var fab: FloatingActionButton
    val accountService = AccountService()

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

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.viewProductListFragment) {
                fab.show()
            } else {
                fab.hide()
            }
        }

        lifecycleScope.launch {
            // if user is not logged in
            val userIdMunoDatabaseObject = SessionService(application).get("id").await()
            val userTokenMunoDatabaseObject = SessionService(application).get("token").await()

            if (userIdMunoDatabaseObject == null) {
                navigationView.menu.findItem(R.id.createProductFragment).isVisible = false
                navigationView.menu.findItem(R.id.viewProductListFragment).isVisible = false
                navigationView.menu.findItem(R.id.viewAccountFragment).isVisible = false
                navigationView.menu.findItem(R.id.logoutFragment).isVisible = false
            }
            // if user is logged in
            else {
                val munoResponse =
                    accountService.getUserById(
                        userIdMunoDatabaseObject.value?.toLong()!!,
                        userTokenMunoDatabaseObject.value!!
                    ).await()

                if (munoResponse.errorMessage != null) {
                    AlertDialog.Builder(this@MainActivity).setTitle("Atentie!")
                        .setMessage(munoResponse.errorMessage)
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
                } else {
                    if (munoResponse.value?.role != "ROLE_ADMIN") {
                        navigationView.menu.findItem(R.id.adminFragment).isVisible = false
                    }
                }

                navigationView.menu.findItem(R.id.loginFragment).isVisible = false
                navigationView.menu.findItem(R.id.createAccountFragment).isVisible = false

                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.startDestination = R.id.searchFragment

                navController.graph = graph
            }

            val munoDatabaseObject = SessionService(application).get("ip").await()

            if (munoDatabaseObject != null) {
                if (munoDatabaseObject.value != null) {
                    Constants.ip = munoDatabaseObject.value!!
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer)
    }
}