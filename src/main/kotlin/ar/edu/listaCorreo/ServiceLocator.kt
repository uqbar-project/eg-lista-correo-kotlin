package ar.edu.listaCorreo

/**
 * El servide locator es un objeto con baja cohesión que permite un grado extra de indirección
 * para tener objetos muy frecuentemente usados que por esta naturaleza se vuelven globales.
 * En esta implementación se respeta una interfaz para cada referencia:
 * 1. tenemos que pasarle un objeto de tipo MailSender y
 * 2. esa referencia se inicializa tardíamente
 */
object serviceLocator {
    lateinit var mailSender: MailSender
}
