package ar.edu.listaCorreo

data class Usuario(
    val mailPrincipal: String = "",
    val mailsAlternativos: List<String> = mutableListOf()
) {
    var mailsEnviados = 0
    var activo = true

    fun envioPost() {
        mailsEnviados++
    }

    fun envioMuchosMensajes() = mailsEnviados > 5
    fun bloquear() {
        activo = false
    }
}