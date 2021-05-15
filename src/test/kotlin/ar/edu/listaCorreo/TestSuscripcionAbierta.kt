package ar.edu.listaCorreo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TestSuscripcionAbierta: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("dada una lista de suscripción abierta") {
        val lista = ListaCorreo()
        val usuario = Usuario(mailPrincipal = "user@usuario.com")
        it("cualquier usuario debe poder suscribirse directamente") {
            lista.suscribir(usuario)
            lista.contieneUsuario(usuario) shouldBe true
        }
        it("tratar de confirmar la suscripción de un usuario debe dar error") {
            shouldThrow<BusinessException> { lista.confirmarSuscripcion(Usuario(mailPrincipal = "otroUsuario@usuario.com")) }
        }
    }
})