package ar.edu.listaCorreo

data class Post(val emisor: Usuario, val asunto: String, val mensaje: String) {
    fun mailEmisor() = emisor.mailPrincipal
    fun enviado() {
        emisor.envioPost()
    }

    fun tienePalabra(palabra: String) = mensaje.toUpperCase().contains(palabra.toUpperCase())
}