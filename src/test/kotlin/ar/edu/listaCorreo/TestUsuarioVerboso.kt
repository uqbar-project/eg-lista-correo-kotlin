package ar.edu.listaCorreo

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify

class TestUsuarioVerboso : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("dada una lista") {
        val usuarioVerbosoObserver = BloqueoUsuarioVerbosoObserver()
        val lista = ListaCorreo().apply {
            suscribir(Usuario(mailPrincipal = "usuario1@usuario.com"))
            suscribir(Usuario(mailPrincipal = "usuario2@usuario.com"))
            agregarPostObserver(usuarioVerbosoObserver)
        }
        it("si un usuario envía muchos posts lo bloquea") {
            val usuario = Usuario(mailPrincipal = "user@usuario.com")
            lista.recibirPost(Post(emisor = usuario, asunto = "Es verdad 1?", mensaje = "Jajajaja 1!"))
            lista.recibirPost(Post(emisor = usuario, asunto = "Es verdad 2?", mensaje = "Jajajaja 2!"))
            lista.recibirPost(Post(emisor = usuario, asunto = "Es verdad 3?", mensaje = "Jajajaja 3!"))
            lista.recibirPost(Post(emisor = usuario, asunto = "Es verdad 4?", mensaje = "Jajajaja 4!"))
            lista.recibirPost(Post(emisor = usuario, asunto = "Es verdad 5?", mensaje = "Jajajaja 5!"))
            usuario.activo shouldBe true

            lista.recibirPost(Post(emisor = usuario, asunto = "Es verdad 6?", mensaje = "Jajajaja 6!"))
            usuario.activo shouldBe false
        }
    }
})
