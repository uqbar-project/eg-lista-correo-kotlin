package ar.edu.listaCorreo

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TestSuscripcionCerrada: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("dada una lista de suscripción cerrada") {
        val lista = ListaCorreo().apply {
            tipoSuscripcion = SuscripcionCerrada()
        }
        val usuario = Usuario(mailPrincipal = "user@usuario.com")
        it("el usuario debe quedar pendiente cuando se intenta suscribir") {
            lista.suscribir(usuario)
            lista.contieneUsuario(usuario) shouldBe false
        }
        it("el usuario queda suscripto si intenta suscribirse y lo aceptan") {
            // el usuario solicita suscribirse
            lista.suscribir(usuario)
            // el administrador confirma la suscripción
            lista.confirmarSuscripcion(usuario)
            lista.contieneUsuario(usuario) shouldBe true
        }
        it("si el usuario solicita suscribirse y lo rechazan no debe quedar suscripto") {
            // el usuario solicita suscribirse
            lista.suscribir(usuario)
            // el administrador rechaza la suscripción
            lista.rechazarSuscripcion(usuario)
            lista.contieneUsuario(usuario) shouldBe false
        }
    }
})