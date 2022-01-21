package ar.edu.unsam.algo2.listaCorreo

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
                // cambio la referencia (indirecta) en el service locator
                serviceLocator.mailSender = mockedMailSender
                prefijo = "algo2"
            })
        }
        it("un usuario no suscripto puede enviar posts a la lista y le llegan solo a los suscriptos - prueba con mock falla") {
            val usuario = Usuario(mailPrincipal = "user@usuario.com")
            val post = Post(emisor = usuario, asunto = "Sale asado?", mensaje = "Lo que dice el asunto")
            lista.recibirPost(post)
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
            // cambio la referencia (indirecta) en el service locator y la reseteo para evitar efectos colaterales de otros tests
            serviceLocator.mailSender = StubMailSender
            StubMailSender.reset()
            //
            val usuario = Usuario(mailPrincipal = "user@usuario.com")
            val post = Post(emisor = usuario, asunto = "Sale asado?", mensaje = "Lo que dice el asunto")
            lista.recibirPost(post)

            StubMailSender.mailsEnviados.size shouldBe 1
            StubMailSender.envioMail(usuario) shouldBe true
        }
    }
})
