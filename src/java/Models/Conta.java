package Models;

public class Conta {
    private String tipo;
    private double saldo;

    public Conta(String tipo, Double saldo) {
        this.tipo = tipo;
        this.saldo = saldo;
    }

    public String getTipo() {
        return tipo;
    }
    public Double getSaldo() {
        return saldo;
    }
}
