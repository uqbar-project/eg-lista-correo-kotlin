package ar.edu.unsam.algo2.listaCorreo

interface MailSender {
    fun sendMail(mail: Mail)
}

data class Mail(val from: String, val to: String, val subject: String, val content: String)
