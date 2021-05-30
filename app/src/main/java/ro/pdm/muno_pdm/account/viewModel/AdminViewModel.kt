package ro.pdm.muno_pdm.account.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ro.pdm.muno_pdm.account.models.User

class AdminViewModel (application: Application) : AndroidViewModel(application) {

    var userList: List<User>? = null
}