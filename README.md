
## Lista de Correo

[![build](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml) [![coverage](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/03-observers-singleton/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/03-observers-singleton/graph/badge.svg) 

![image](./images/mailingList.png)

### Branch 03-observers-singleton

En esta rama vas a notar que el build falla, y eso es esperable, porque aquí en el MailObserver no inyectamos la dependencia al mail sender sino que utilizamos un **Singleton**:

```kt
class MailObserver : PostObserver {
    lateinit var prefijo: String

    override fun postEnviado(post: Post, listaCorreo: ListaCorreo) {
        stubMailSender.sendMail(
            Mail(from = post.mailEmisor(),
                to = listaCorreo.getMailsDestino(post),
                subject = "[${prefijo}] ${post.asunto}",
                content = post.mensaje)
        )
    }
```

El objetivo del Singleton es proveer un acceso global a un objeto desde cualquier parte de mi aplicación. Esto es

- cómodo, porque es fácil acceder a la referencia StubMailSender
- pero ahora ya no puedo configurar el mail sender, está _hardcoded_ (fijo)

Si justamente tenemos dos tipos de tests: 1. de comportamiento (mock) y 2. de estado (stub), no podemos cambiarlo dinámicamente y los tests que dependen de los mocks fallan.

## La definición del Singleton

Mientras que en otros lenguajes como Java el Singleton involucra [cierta burocracia](), en Kotlin contamos con el concepto `object` que es muy útil:

```kt
object stubMailSender : MailSender {
    val mailsEnviados = mutableListOf<Mail>()

    override fun sendMail(mail: Mail) {
        mailsEnviados.add(mail)
    }

    fun envioMail(usuario: Usuario) = mailsEnviados.any { it.from == usuario.mailPrincipal }
```

## El efecto en lo global

Aun cuando este test funciona:

```kt
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
```

la referencia global StubMailSender **nunca se limpia**. Esto provoca que cuando queremos verificar que solo se envió 1 mail, ese assert fallará si algún otro test anteriormente pudo enviar correctamente mails. Esto es porque **nunca se recrea el estado del stub mail sender**, es decir nunca limpiamos después de cada test los mails que se enviaron, que quedan en la lista del Stub.

Esto requiere como solución que llamemos a un método `reset()` en forma manual, para que deje la colección vacía:

```kt
fun reset() {
    mailsEnviados.clear()
}
```

## El Singleton como anti-pattern

En definitiva, utilizar singletons para esta solución introdujo muchos problemas que no teníamos:

- resolví la inyección de dependencias eliminando la posibilidad de configurar las referencias a un objeto
- pero eso me obliga a utilizar una instancia concreta y no poder cambiarla: no puedo tener un mail sender para los tests y otro para el código productivo (tendría que correr los tests y enviar mails o bien que no mande mails cuando la aplicación esté deployada)
- y como la referencia es global, corta la independencia de los tests y hay que solucionarlo manualmente

> Por lo dicho antes, nuestro consejo es que el singleton es una técnica que debe introducirse con mucho cuidado en nuestras soluciones y debemos estar al tanto de las desventajas de su utilización
