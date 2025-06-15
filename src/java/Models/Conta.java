package Models;

public class Conta {
    private String conta;
    private String data;
    private String tipo;
    private double saldo;

    public Conta(String conta, String data, String tipo, Double saldo) {
        this.conta = conta;
        this.data = data;
        this.tipo = tipo;
        this.saldo = saldo;
    }

    public String getConta() { return conta; }
    public String getData() { return data; }
    public String getTipo() { return tipo; }
    public double getSaldo() { return saldo; }

}
