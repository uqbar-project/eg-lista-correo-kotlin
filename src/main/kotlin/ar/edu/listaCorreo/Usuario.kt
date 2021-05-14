package ar.edu.listaCorreo

data class Usuario(
    val mailPrincipal: String = "",
    val mailsAlternativos: List<String> = mutableListOf()
) {
}