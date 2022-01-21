package ar.edu.unsam.algo2.listaCorreo

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify

class TestMalasPalabras : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("dada una lista") {
        val malasPalabrasObserver = MalasPalabrasObserver().apply {
            agregarMalaPalabra("container")
            agregarMalaPalabra("podrido")
        }
        val lista = ListaCorreo().apply {
            suscribir(Usuario(mailPrincipal = "usuario1@usuario.com"))
            suscribir(Usuario(mailPrincipal = "usuario2@usuario.com"))
            agregarPostObserver(malasPalabrasObserver)
        }
        it("al enviar un post con malas palabras se registra en el observer") {
            val usuario = Usuario(mailPrincipal = "user@usuario.com")
            val post = Post(emisor = usuario, asunto = "Es verdad?", mensaje = "No me la container!")
            lista.recibirPost(post)
            malasPalabrasObserver.postConMalasPalabras shouldContain post
        }
        it("al enviar un post que no tiene malas palabras no se registra en el observer") {
            val usuario = Usuario(mailPrincipal = "user@usuario.com")
            val post = Post(emisor = usuario, asunto = "Es verdad?", mensaje = "No te la puedo!")
            lista.recibirPost(post)
            malasPalabrasObserver.postConMalasPalabras.isEmpty() shouldBe true
        }
    }
})
