package domain

@Deprecated("Use UserInfo instead")
data class BasicUserData(val name: String){
    override fun toString(): String {
        return "BasicUserData(${name})"
    }
}