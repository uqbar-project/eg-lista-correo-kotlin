package ar.edu.unsam.algo2.listaCorreo

data class Post(val emisor: Usuario, val asunto: String, val mensaje: String) {
    fun mailEmisor() = emisor.mailPrincipal

    fun tienePalabra(palabra: String) = mensaje.uppercase().contains(palabra.uppercase())
}