package ro.pdm.muno_pdm.account.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.utils.session.SessionService

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            SessionService(requireActivity().application).delete("id").await()
            SessionService(requireActivity().application).delete("token").await()

            changeNavigationContext()

            val inflater = findNavController().navInflater
            val graph = inflater.inflate(R.navigation.nav_graph)
            graph.startDestination = R.id.loginFragment

            findNavController().graph = graph
        }
    }

    private fun changeNavigationContext() {
        val navigationView : NavigationView? = activity?.findViewById(R.id.nav_view)
        if (navigationView != null) {
            navigationView.menu.findItem(R.id.createProductFragment).isVisible = false
            navigationView.menu.findItem(R.id.viewProductListFragment).isVisible = false
            navigationView.menu.findItem(R.id.viewAccountFragment).isVisible = false
            navigationView.menu.findItem(R.id.logoutFragment).isVisible = false
            navigationView.menu.findItem(R.id.loginFragment).isVisible = true
            navigationView.menu.findItem(R.id.createAccountFragment).isVisible = true
        }
    }
}