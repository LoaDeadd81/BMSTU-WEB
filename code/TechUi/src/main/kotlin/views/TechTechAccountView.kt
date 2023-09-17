package techUi.views

class TechTechAccountView {

    fun logInForm(): Pair<String, String> {
        print("Введите логин: ")
        val login = readln()
        print("Введите пароль: ")
        val password = readln()

        return Pair(login, password)
    }
}