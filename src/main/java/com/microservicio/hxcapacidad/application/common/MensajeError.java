package com.microservicio.hxcapacidad.application.common;

public enum MensajeError {

    DATOS_OBLIGATORIOS("El campo %s es obligatorio"),
    NOMBRE_DUPLICADO("Nombre duplicado"),
    TECNOLOGIA_INCOMPLETAS("La capacidad %s debe de tener mínimo 3 tecnologías asociadas"),
    CAPACIDAD_INCOMPLETAS("El bootcamp %s debe de tener mínimo 1 capacidades asociadas"),
    LONGITUD_PERMITIDA("Longitud permitida para %s es de %s"),
    TECNOLOGIA_NO_PERMITIDAS("La capacidad %s debe de tener máximo 20 tecnologías asociadas"),
    CAPACIDAD_NO_PERMITIDAS("El bootcamp %s debe de tener máximo 4 capacidades asociadas"),
    TECNOLOGIA_REPETIDAS("La capacidad %s contiene tecnologías repetidas."),
    CAPACIDAD_REPETIDAS("El bootcamp %s contiene tecnologías repetidas."),
    COLUMNA_ORDENAMIENTO_INCORRECTO("La columba de ordenamiento es incorrecta"),
    METODO_ORDENAMIENTO_INCORRECTO("El metodo de ordenamiento es incorrecto, solo se permite asc o desc");

    private final String mensaje;

    MensajeError(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String formato(Object... args) {
        return String.format(mensaje, args);
    }
}
