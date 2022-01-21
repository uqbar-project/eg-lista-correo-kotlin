package ar.edu.unsam.algo2.listaCorreo

object StubMailSender : MailSender {
    val mailsEnviados = mutableListOf<Mail>()

    override fun sendMail(mail: Mail) {
        mailsEnviados.add(mail)
    }

    fun envioMail(usuario: Usuario) = mailsEnviados.any { it.from == usuario.mailPrincipal }

    fun reset() {
        mailsEnviados.clear()
    }
}
