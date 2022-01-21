package ar.edu.unsam.algo2.listaCorreo

data class Usuario(
    val mailPrincipal: String = "",
    val mailsAlternativos: List<String> = mutableListOf()
)