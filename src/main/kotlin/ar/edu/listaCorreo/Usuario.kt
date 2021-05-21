package ar.edu.listaCorreo

data class Usuario(
    val mailPrincipal: String = "",
    private val mailsAlternativos: List<String> = mutableListOf()
) {
    private var mailsEnviados = 0
    var activo = true

    fun envioPost() {
        mailsEnviados++
    }

    fun envioMuchosMensajes() = mailsEnviados > 5
    fun bloquear() {
        activo = false
    }
}