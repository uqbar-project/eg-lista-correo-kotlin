package ar.edu.unsam.algo2.listaCorreo

interface PostObserver {
    fun postEnviado(post: Post, listaCorreo: ListaCorreo)
}

class MailObserver(val mailSender: MailSender, val prefijo: String) : PostObserver {

    override fun postEnviado(post: Post, listaCorreo: ListaCorreo) {
        mailSender.sendMail(
            Mail(from = post.mailEmisor(),
                to = listaCorreo.getMailsDestino(post),
                subject = "[${prefijo}] ${post.asunto}",
                content = post.mensaje)
        )
    }

}

class BloqueoUsuarioVerbosoObserver : PostObserver {

    val postsPorUsuario = mutableMapOf<Usuario, Int>()

    override fun postEnviado(
        post: Post,
        listaCorreo: ListaCorreo
    ) {
        val usuarioEmisor = post.emisor
        val cantidadPosts = postsPorUsuario.getOrDefault(usuarioEmisor, 0) + 1
        postsPorUsuario.put(usuarioEmisor, cantidadPosts)
        if (cantidadPosts > 5) {
            usuarioEmisor.bloquear()
        }
    }

}

class MalasPalabrasObserver : PostObserver {

    val malasPalabras = mutableSetOf<String>()
    val postConMalasPalabras = mutableSetOf<Post>()

    override fun postEnviado(post: Post, listaCorreo: ListaCorreo) {
        if (tieneMalasPalabras(post)) {
            //println("Mensaje enviado a admin por mensaje con malas palabras: " + post.mensaje)
            postConMalasPalabras.add(post)
        }
    }

    fun tieneMalasPalabras(post: Post) = malasPalabras.any { post.tienePalabra(it) }

    fun agregarMalaPalabra(palabra: String) {
        malasPalabras.add(palabra)
    }

}
