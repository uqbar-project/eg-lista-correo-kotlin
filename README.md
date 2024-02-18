
## Lista de Correo

[![build](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml/badge.svg?branch=01-observers-constructor)](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml) [![coverage](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/01-observers-constructor/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/01-observers-constructor/graph/badge.svg) 

![image](./images/mailingList.png)

### Branch 01-observers-constructor: parte 1

En esta rama definimos varios observers:

- el envío de mails pasa a estar en un observer aparte, al cual le pasamos el _mailSender_ utilizando la técnica **constructor injection**.

```kt
val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
describe("dada una lista de envio abierto") {
    val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
    val lista = ListaCorreo().apply {
        suscribir(Usuario(mailPrincipal = "usuario1@usuario.com"))
        ...
        agregarPostObserver(MailObserver(mailSender = mockedMailSender, prefijo = "algo2"))
```

El MailObserver define un constructor que exige que le pasemos el _mailSender_:

```kt
class MailObserver(val mailSender: MailSender, val prefijo: String) : PostObserver {
```

- también se implementan el bloqueo al usuario que envía muchos post (fíjense que se delega al usuario muchas de las preguntas, pero es el observer el que dispara el cambio)

```kt
class BloqueoUsuarioVerbosoObserver : PostObserver {

    override fun postEnviado(post: Post, listaCorreo: ListaCorreo) {
        val emisor = post.emisor
        if (emisor.envioMuchosMensajes()) {
            emisor.bloquear()
        }
    }
```

- y por último el registro de post con "malas palabras" que se puede configurar

```kt
class MalasPalabrasObserver : PostObserver {

    val malasPalabras = mutableListOf<String>()
    val postConMalasPalabras = mutableListOf<Post>()

    override fun postEnviado(post: Post, listaCorreo: ListaCorreo) {
        if (tieneMalasPalabras(post)) {
            //println("Mensaje enviado a admin por mensaje con malas palabras: " + post.mensaje)
            postConMalasPalabras.add(post)
        }
    }

    fun tieneMalasPalabras(post: Post) = malasPalabras.any { post.tienePalabra(it) }
```

## Notificación a los observers

La lista de correo notifica a los interesados en el evento "post recibido":

```kt
fun recibirPost(post: Post) {
    if (!post.emisor.activo) throw BusinessException("El usuario está inhabilitado para enviar posts.")
    validacionEnvio.validarPost(post, this)
    post.enviado()
    postObservers.forEach { it.postEnviado(post, this) }
}
```
