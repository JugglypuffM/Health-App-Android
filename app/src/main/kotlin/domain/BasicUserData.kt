package domain

data class BasicUserData(val name: String){
    override fun toString(): String {
        return "BasicUserData(${name})"
    }
}