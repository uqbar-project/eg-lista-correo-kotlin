
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




