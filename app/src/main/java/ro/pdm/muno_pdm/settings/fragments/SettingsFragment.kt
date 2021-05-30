package ro.pdm.muno_pdm.settings.fragments

import android.os.Bundle
import android.provider.SyncStateContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.utils.session.MunoDatabaseObject
import ro.pdm.muno_pdm.utils.session.SessionService
import ro.pdm.muno_pdm.utils.shared.Constants

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ipEt: EditText = view.findViewById(R.id.ipEt)
        ipEt.text.insert(0, Constants.ip)

        view.findViewById<Button>(R.id.saveBt).setOnClickListener {
            Constants.ip = ipEt.text.toString()
            val munoDatabaseObject = MunoDatabaseObject()
            munoDatabaseObject.key = "ip"
            munoDatabaseObject.value = Constants.ip
            SessionService(requireActivity().application).insert(munoDatabaseObject)

            findNavController().popBackStack()
        }
    }

}