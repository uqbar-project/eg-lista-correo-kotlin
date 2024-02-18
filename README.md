
## Lista de Correo

[![build](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml/badge.svg?branch=02-observers-setter)](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml) [![coverage](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/02-observers-setter/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/02-observers-setter/graph/badge.svg) 

![image](./images/mailingList.png)

### Branch 02-observers-setter

En esta pequeña variante el MailObserver no define un constructor específico, entonces la referencia al _mailSender_ se inyecta vía **setter**:

```kt
    describe("dada una lista de envio abierto") {
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
        val lista = ListaCorreo().apply {
            suscribir(Usuario(mailPrincipal = "usuario1@usuario.com"))
            ...
            agregarPostObserver(MailObserver().apply {
                mailSender = mockedMailSender
                prefijo = "algo2"
            })
```

La referencia mailSender se inicializa en forma lazy mediante el modificador `lateinit`:

```kt
class MailObserver : PostObserver {
    lateinit var mailSender: MailSender
```

esto exige que antes de que lo utilicemos hayamos asignado dicha referencia. 

A diferencia de la inyección por constructor, que garantiza que nuestro observer quede construido en forma consistente, puede pasarnos que nos olvidemos de pasar la referencia al mailSender


```kt
    describe("dada una lista de envio abierto") {
        val lista = ListaCorreo().apply {
            suscribir(Usuario(mailPrincipal = "usuario1@usuario.com"))
            ...
            agregarPostObserver(MailObserver().apply {
//                mailSender = mockedMailSender
                prefijo = "algo2"
            })
```

y nos salte el siguiente error en runtime:

```bash
lateinit property mailSender has not been initialized
kotlin.UninitializedPropertyAccessException: lateinit property mailSender has not been initialized
	at ar.edu.listaCorreo.MailObserver.postEnviado(PostObserver.kt:12)
	at ar.edu.listaCorreo.ListaCorreo.recibirPost(ListaCorreo.kt:33)
	at ar.edu.listaCorreo.TestEnvioAbierto$1$1$1.invokeSuspend(TestEnvioAbierto.kt:25)
```

Pero también da cierta flexibilidad para modificar las referencias a diferentes mail senders.
