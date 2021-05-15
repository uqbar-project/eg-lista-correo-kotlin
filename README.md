
## Lista de Correo

[![build](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-lista-correo-kotlin/actions/workflows/build.yml) [![coverage](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/master/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-lista-correo-kotlin/branch/master/graph/badge.svg) 

![image](./images/mailingList.png)

Este ejercicio consta de varios pasos, en la fase inicial se exploran

- ideas de diseño: trabajo con condicionales, herencia, strategy y un posible decorator
- implementación de casos de uso asincrónicos

### Branch master: parte 1

Se requiere un software que maneje listas de correo. El sistema debe contemplar:

- Enviar un mensaje, recibiendo la dirección de e-mail origen del correo, título y texto. El envío de mensajes a la lista puede definirse como abierto (cualquiera puede enviar a la lista) o restringido a los miembros de la lista. Para mandar los mensajes, el sistema debe interactuar con otro sistema que tiene la capacidad de mandar un mail a una dirección específica. También debemos definir la interfaz con la que nos vamos a comunicar con ese otro sistema. Contemplar la posibilidad de que el sistema de envío de mails no pueda enviar un correo determinado. Como primera medida vamos a ignorar los mails que no se pudieron enviar. Cada usuario puede tener definida más de una dirección de e-mail, desde las que puede enviar mensajes a la(s) lista(s). De todas las direcciones de e-mail que tenga, una es a la que se le envían los mails.
- Suscribir un nuevo miembro a una lista de correo. La suscripción también puede ser definida como abierta (cualquiera que se suscribe es admitido) o cerrada (los pedidos de suscripción deben ser aprobados por un administrador).

## Solución

- [Link al desarrollo de la solución](https://docs.google.com/document/d/1aw8p79d78zos47ommvwZw6fIkHH_Qx_SBfwU3yfJ96k/edit?usp=sharing)

En esta variante elegimos

- modelar con un strategy los modos de suscripción
- y también con un strategy las formas de validación, eso nos permite encontrar formas de subclasificar las listas de correo por otro criterio a futuro
- aunque por el momento hay una sola validación, definimos dos tipos de envío: el abierto no tiene validación, termina siendo un [Null Object Pattern](https://refactoring.guru/es/introduce-null-object) o una forma de evitar tener una referencia nullable
- en este test para envíos abiertos

```kt
    it("un usuario no suscripto puede enviar posts a la lista y le llegan solo a los suscriptos") {
        val usuario = Usuario(mailPrincipal = "user@usuario.com")
        val post = Post(emisor = usuario, asunto = "Sale asado?", mensaje = "Lo que dice el asunto")
        lista.recibirPost(post)
        verify(exactly = 1) { mockedMailSender.sendMail(mail = Mail(from="user@usuario.com", to="usuario1@usuario.com, usuario2@usuario.com, usuario3@usuario.com", subject="[algo2] Sale asado?", content = "Lo que dice el asunto")) }
    }
```

estamos probando muchas cosas a la vez (1. el mail que se genera con los destinatarios ordenados, 2. que no se envía el mail al usuario que envía el post y 3. que se envía un solo mail). Otra alternativa podría ser generar varios tests unitarios por separado, aunque tendríamos que repetir la clase de equivalencia y la construcción del fixture para ese test. Por el momento preferimos dejarlo así, aun sabiendo que es un test que fácilmente se rompe si alguna de las condiciones del negocio cambia.
