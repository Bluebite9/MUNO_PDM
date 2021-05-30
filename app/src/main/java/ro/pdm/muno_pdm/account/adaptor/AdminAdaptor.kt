package ro.pdm.muno_pdm.account.adaptor

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ro.pdm.muno_pdm.R
import ro.pdm.muno_pdm.account.models.User


class AdminAdaptor (
    var adminList: MutableList<User>?,
    private val viewLifecycleOwner: LifecycleOwner,
    val application: Application
) : RecyclerView.Adapter<AdminAdaptor.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        var firstNameTv: TextView = view.findViewById(R.id.firstNameTv)
        var lastNameTv: TextView = view.findViewById(R.id.lastNameTv)
        var emailTv: TextView = view.findViewById(R.id.emailTv)
        var cityTv: TextView = view.findViewById(R.id.cityTv)
        var countryTv: TextView = view.findViewById(R.id.countryTv)
        var phoneTv: TextView = view.findViewById(R.id.phoneTv);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAdaptor.VH {
        return AdminAdaptor.VH(
            LayoutInflater.from(parent.context).inflate(R.layout.admin_user_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdminAdaptor.VH, position: Int) {
        val user = adminList?.get(position)

        if (user != null) {
            holder.firstNameTv.text = user.firstName
            holder.lastNameTv.text = user.lastName
            holder.emailTv.text = user.email
            holder.cityTv.text = user.city
            holder.countryTv.text = user.county
            holder.phoneTv.text = user.phone

        }

//        holder.itemView.setOnClickListener {
//            val bundle = Bundle()
//            if (user != null) {
//                bundle.putSerializable("userId", user.id)
//            }
//            //TODO ce fragment pun aici, mai creez unul, cred ca a facut, am pus ala in nav_grav si dupa aici
//            it.findNavController().navigate(R.id.adminFragment, bundle)
//        }


    }

    override fun getItemCount(): Int {
        return adminList?.size ?: 0
    }

}