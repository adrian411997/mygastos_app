package com.projects.personals.mygastos;



public class gastosList {
    public int id;
    public String nombre;
    public double monto;
    public String mes;
    public int dia;
    public int year;
    public String type;

    public gastosList(String nombre,int id, double monto,  String mes, Integer dia, Integer year, String type) {
        this.nombre = nombre;
        this.monto = monto;
        this.mes = mes;
        this.dia = dia;
        this.year = year;
        this.type = type;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer año) {
        this.year = año;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public int getMesInt() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        int mesInt = -1; // Valor predeterminado en caso de que no se encuentre el mes

        for (int i = 0; i < meses.length; i++) {
            if (meses[i].equalsIgnoreCase(mes)) {
                mesInt = i + 1; // Sumar 1 porque los meses se cuentan desde 1 (enero) hasta 12 (diciembre)
                break;
            }
        }

        return mesInt;
    }
}
