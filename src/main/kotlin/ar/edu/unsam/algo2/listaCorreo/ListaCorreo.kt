package ar.edu.unsam.algo2.listaCorreo

class ListaCorreo {
    private val suscriptos = mutableListOf<Usuario>()
    private val usuariosPendientes = mutableListOf<Usuario>()
    var tipoSuscripcion: TipoSuscripcion = SuscripcionAbierta()
    var validacionEnvio: ValidacionEnvio = EnvioLibre()
    lateinit var mailSender: MailSender
    var prefijo = ""

    fun suscribir(usuario: Usuario) {
        tipoSuscripcion.suscribir(usuario, this)
    }

    fun confirmarSuscripcion(usuario: Usuario) {
        tipoSuscripcion.confirmarSuscripcion(usuario, this)
    }

    fun rechazarSuscripcion(usuario: Usuario) {
        this.eliminarUsuarioPendiente(usuario)
    }

    fun recibirPost(post: Post) {
        validacionEnvio.validarPost(post, this)
        mailSender.sendMail(
            Mail(from = post.mailEmisor(),
                to = this.getMailsDestino(post),
                subject = "[${prefijo}] ${post.asunto}",
                content = post.mensaje)
        )
    }

    private fun getMailsDestino(post: Post) = this.suscriptos
        .filter { usuario -> usuario != post.emisor }
        .map { it.mailPrincipal }
        .joinToString(", ")

    /*********************** Definiciones internas  ***************************/
    fun agregarUsuario(usuario: Usuario) {
        this.suscriptos.add(usuario)
    }

    fun agregarUsuarioPendiente(usuario: Usuario) {
        this.usuariosPendientes.add(usuario)
    }

    fun eliminarUsuarioPendiente(usuario: Usuario) {
        this.usuariosPendientes.remove(usuario)
    }

    fun contieneUsuario(emisor: Usuario): Boolean = this.suscriptos.contains(emisor)
}

/**********************************************************************************
                           SUSCRIPCION
 **********************************************************************************/
interface TipoSuscripcion {
    fun suscribir(usuario: Usuario, listaCorreo: ListaCorreo)
    fun confirmarSuscripcion(usuario: Usuario, listaCorreo: ListaCorreo)
}

class SuscripcionAbierta : TipoSuscripcion {
    override fun suscribir(usuario: Usuario, listaCorreo: ListaCorreo) {
        listaCorreo.agregarUsuario(usuario)
    }

    override fun confirmarSuscripcion(usuario: Usuario, listaCorreo: ListaCorreo) {
        throw BusinessException("No debe confirmar la suscripción para la lista de suscripción abierta")
    }
}

class SuscripcionCerrada : TipoSuscripcion {
    override fun suscribir(usuario: Usuario, listaCorreo: ListaCorreo) {
        listaCorreo.agregarUsuarioPendiente(usuario)
    }

    override fun confirmarSuscripcion(usuario: Usuario, listaCorreo: ListaCorreo) {
        listaCorreo.eliminarUsuarioPendiente(usuario)
        listaCorreo.agregarUsuario(usuario)
    }
}

/**********************************************************************************
                         VALIDACION DE ENVIO
 **********************************************************************************/
interface ValidacionEnvio {
    fun validarPost(post: Post, listaCorreo: ListaCorreo)
}

class EnvioLibre : ValidacionEnvio {
    override fun validarPost(post: Post, listaCorreo: ListaCorreo) {
        // Null Object Pattern, no hay validación
    }
}

class EnvioRestringido : ValidacionEnvio {
    override fun validarPost(post: Post, listaCorreo: ListaCorreo) {
        if (!listaCorreo.contieneUsuario(post.emisor)) {
            throw BusinessException("No puede enviar un mensaje porque no pertenece a la lista")
        }
    }
}