package ro.pdm.muno_pdm.utils.shared

import ro.pdm.muno_pdm.account.models.User
import ro.pdm.muno_pdm.product.models.Product

class Validators {
    //functie de validare
    //1. validate usser,
    // returneaza un obiect muno pe care in pun in shared munop validate response,
    // are 2 prop, validate message de tip string, si isValid
    //obiectul asta e populat de functia de validare

    private fun isEmailValid(email: String?): Boolean {
        if (email == null) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //"^[A-Z][a-zA-Z]*$"
    private fun isNameValid(name: String?): Boolean {
        if (name == null) {
            return false;
        }
        val regex = Regex(pattern = "^[A-Z][a-zA-Z]*$")
        return regex.containsMatchIn(input = name)
    }

    private fun isDescriptionValid(description: String?): Boolean {
        if (description == null) {
            return false;
        }
        val regex = Regex(pattern = "[a-zA-Z]*$")
        return regex.containsMatchIn(input = description)
    }

    private fun isPhoneValid(phoneNr: String?): Boolean {
        if (phoneNr == null) {
            return false;
        }
        return android.util.Patterns.PHONE.matcher(phoneNr).matches()
    }

    //"^(?=.[a-z])(?=.[A-Z])(?=.\d)(?=.[@$!%?&])[A-Za-z\d@$!%?&]{8,}$"
    private fun isPasswordValid(password: String?): Boolean {
        if (password == null) {
            return false
        }

        val regex =
            Regex(pattern = "^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$")
        return regex.containsMatchIn(input = password);
    }

    fun validateUser(user: User): MunoValidateResponse {
        val munoValidateResponse = MunoValidateResponse()
        if (!isNameValid(user.firstName)) {
            munoValidateResponse.message = "Prenumele trebuie completat"
            return munoValidateResponse
        }

        if (!isNameValid(user.lastName)) {
            munoValidateResponse.message = "Numele trebuie completat"
            return munoValidateResponse
        }

        if (!isEmailValid(user.email)) {
            munoValidateResponse.message = "Emailul trebuie completat"
            return munoValidateResponse
        }

        if (!isPhoneValid(user.phone)) {
            munoValidateResponse.message = "Telefonul trebuie completat"
            return munoValidateResponse
        }

        if (!isPasswordValid(user.password)) {
            munoValidateResponse.message = "Parola trebuie completat"
            return munoValidateResponse
        }

        if (!isNameValid(user.county)) {
            munoValidateResponse.message = "Judetul trebuie completat"
            return munoValidateResponse
        }

        if (!isNameValid(user.city)) {
            munoValidateResponse.message = "Orasul trebuie completat"
            return munoValidateResponse
        }

        return munoValidateResponse
    }

    //2.validate product, la fel
    fun validateProduct(product: Product): MunoValidateResponse {
        val munoValidateResponse = MunoValidateResponse()

        if (!isNameValid(product.name)) {
            munoValidateResponse.message = "Numele trebuie completat"
            return munoValidateResponse
        }

        if (!isDescriptionValid(product.description)) {
            munoValidateResponse.message = "Descrierea trebuie completata"
            return munoValidateResponse
        }

        if (product.price == null) {
            munoValidateResponse.message = "Pretul trebuie completat"
            return munoValidateResponse
        }

        if (product.unit == null) {
            munoValidateResponse.message = "Tipul de cantitate (Buc/ KG/ L)"
            return munoValidateResponse
        }
        if (product.category == null) {
            munoValidateResponse.message = "Introduceti categoria produsului"
            return munoValidateResponse
        }
        return munoValidateResponse
    }
}