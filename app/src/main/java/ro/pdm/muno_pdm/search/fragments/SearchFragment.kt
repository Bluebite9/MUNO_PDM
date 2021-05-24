package ro.pdm.muno_pdm.search.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.search.adaptor.SearchAdaptor
import ro.pdm.muno_pdm.search.viewModels.SearchViewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.searchProductsRecyclerView)
        val queryEt = view.findViewById<EditText>(R.id.queryEt)

        queryEt.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                viewLifecycleOwner.lifecycleScope.launch {
                    val munoResponse = viewModel.searchClick(queryEt.text.toString()).await()

                    if (munoResponse.errorMessage != null) {
                        AlertDialog.Builder(context).setTitle("Atentie!")
                            .setMessage(munoResponse.errorMessage)
                            .setPositiveButton("OK", null)
                            .create()
                            .show()
                    } else {
                        viewModel.productList = munoResponse.value
                        recyclerView.adapter = SearchAdaptor(viewModel.productList)
                    }
                }

                return@OnKeyListener true
            }
            false
        })

        recyclerView.layoutManager = LinearLayoutManager(activity)
    }
}