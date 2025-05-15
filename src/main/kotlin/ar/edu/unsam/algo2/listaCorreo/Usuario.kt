package ar.edu.unsam.algo2.listaCorreo

data class Usuario(
    val mailPrincipal: String = "",
    private val mailsAlternativos: List<String> = mutableListOf()
) {
    var activo = true

    fun bloquear() {
        activo = false
    }
}