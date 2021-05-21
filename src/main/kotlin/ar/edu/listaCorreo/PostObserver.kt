package ar.edu.listaCorreo

interface PostObserver {
    fun postEnviado(post: Post, lista: ListaCorreo)
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

    override fun postEnviado(post: Post, listaCorreo: ListaCorreo) {
        val emisor = post.emisor
        if (emisor.envioMuchosMensajes()) {
            emisor.bloquear()
        }
    }

}

class MalasPalabrasObserver : PostObserver {

    val malasPalabras = mutableListOf<String>()
    val postConMalasPalabras = mutableListOf<Post>()

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

