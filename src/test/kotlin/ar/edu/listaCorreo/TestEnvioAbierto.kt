package ar.edu.listaCorreo

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify

class TestEnvioAbierto: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("dada una lista de envio abierto") {
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
        val lista = ListaCorreo().apply {
            suscribir(Usuario(mailPrincipal = "usuario1@usuario.com"))
            suscribir(Usuario(mailPrincipal = "usuario2@usuario.com"))
            suscribir(Usuario(mailPrincipal = "usuario3@usuario.com"))
            agregarPostObserver(MailObserver().apply {
                // como es un Singleton no puedo cambiar el mail sender
                // mailSender = mockedMailSender
                prefijo = "algo2"
            })
        }
        it("un usuario no suscripto puede enviar posts a la lista y le llegan solo a los suscriptos - prueba con mock falla") {
            val usuario = Usuario(mailPrincipal = "user@usuario.com")
            val post = Post(emisor = usuario, asunto = "Sale asado?", mensaje = "Lo que dice el asunto")
            lista.recibirPost(post)
            // esta verificación es exhaustiva pero también hace que este test se rompa muy fácilmente
            // este test es muy social, está testeando
            // 1. el mail que se genera (con todos los destinatarios en orden)
            // 2. que no se envía el mail al usuario que envía el post
            // 3. que se envía un solo mail
            //
            // otra alternativa podría ser crear tests unitarios
            verify(exactly = 1) {
                mockedMailSender.sendMail(
                    mail = Mail(
                        from = "user@usuario.com",
                        to = "usuario1@usuario.com, usuario2@usuario.com, usuario3@usuario.com",
                        subject = "[algo2] Sale asado?",
                        content = "Lo que dice el asunto"
                    )
                )
            }
        }
        it("un usuario no suscripto puede enviar posts a la lista y le llegan solo a los suscriptos - prueba con stub fijo anda") {
            // Como el StubMailSender es una instancia global, nunca se recrea en los tests unitarios
            // otra desventaja es que para que este test pase hay que blanquear las referencias
            stubMailSender.reset()
            //
            val usuario = Usuario(mailPrincipal = "user@usuario.com")
            val post = Post(emisor = usuario, asunto = "Sale asado?", mensaje = "Lo que dice el asunto")
            lista.recibirPost(post)

            stubMailSender.mailsEnviados.size shouldBe 1
            stubMailSender.envioMail(usuario) shouldBe true
        }
    }
})
