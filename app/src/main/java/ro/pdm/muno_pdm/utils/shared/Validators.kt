package ro.pdm.muno_pdm.utils.shared

import ro.pdm.muno_pdm.account.models.User

class Validators {
    //functie de validare
    //1. validate usser,
    // returneaza un obiect muno pe care in pun in shared munop validate response,
    // are 2 prop, validate message de tip string, si isValid
    //obiectul asta e populat de functia de validare

    fun validateUser(user: User ): MunoValidateResoponse {
        val munoValidateResoponse = MunoValidateResoponse()
        if(user.firstName == null) {
            munoValidateResoponse.message = "Numele trebuie completat"
            return munoValidateResoponse
        }

        return munoValidateResoponse
    }
    //2.validate product, la fel
}