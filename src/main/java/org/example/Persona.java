package org.example;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;

public class Persona {
    private String curp;
    private LocalDate fechaNac;
    private String genero;
    private String estado;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    // SETTERS

    public void setCurp(String curp) {
        this.curp = curp;

        // Si el CURP es válido, extrae automáticamente datos
        if (curp.length() >= 13) {
            try {
                String fechaTexto = curp.substring(4, 10); // AAMMDD
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
                this.fechaNac = LocalDate.parse(fechaTexto, formatter);

                String generoLetra = curp.substring(10, 11);
                this.genero = generoLetra.equalsIgnoreCase("H") ? "Masculino" : "Femenino";

                String estadoClave = curp.substring(11, 13).toUpperCase();
                this.estado = estadosMap.getOrDefault(estadoClave, "Desconocido");
            } catch (Exception e) {
                // Evita crashear si el CURP está incompleto
                this.fechaNac = null;
                this.genero = null;
                this.estado = null;
            }
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellido) {
        this.apellidoPaterno = apellido;
    }

    public void setApellidoMaterno(String apellido) {
        this.apellidoMaterno = apellido;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    // GETTERS

    public String getCurp() {
        return curp;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public String getGenero() {
        return genero;
    }

    public String getEstado() {
        return estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    // MAPA DE ESTADOS

    private static final Map<String, String> estadosMap = new HashMap<>();
    static {
        estadosMap.put("AS", "Aguascalientes");
        estadosMap.put("BC", "Baja California");
        estadosMap.put("BS", "Baja California Sur");
        estadosMap.put("CC", "Campeche");
        estadosMap.put("CL", "Coahuila");
        estadosMap.put("CM", "Colima");
        estadosMap.put("CS", "Chiapas");
        estadosMap.put("CH", "Chihuahua");
        estadosMap.put("DF", "Ciudad de México");
        estadosMap.put("DG", "Durango");
        estadosMap.put("GT", "Guanajuato");
        estadosMap.put("GR", "Guerrero");
        estadosMap.put("HG", "Hidalgo");
        estadosMap.put("JC", "Jalisco");
        estadosMap.put("MC", "México");
        estadosMap.put("MN", "Michoacán");
        estadosMap.put("MS", "Morelos");
        estadosMap.put("NT", "Nayarit");
        estadosMap.put("NL", "Nuevo León");
        estadosMap.put("OC", "Oaxaca");
        estadosMap.put("PL", "Puebla");
        estadosMap.put("QT", "Querétaro");
        estadosMap.put("QR", "Quintana Roo");
        estadosMap.put("SP", "San Luis Potosí");
        estadosMap.put("SL", "Sinaloa");
        estadosMap.put("SR", "Sonora");
        estadosMap.put("TC", "Tabasco");
        estadosMap.put("TS", "Tamaulipas");
        estadosMap.put("TL", "Tlaxcala");
        estadosMap.put("VZ", "Veracruz");
        estadosMap.put("YN", "Yucatán");
        estadosMap.put("ZS", "Zacatecas");
        estadosMap.put("NE", "Nacido en el extranjero");
    }

    public static Map<String, String> getEstadosMap() {
        return estadosMap;
    }
}
