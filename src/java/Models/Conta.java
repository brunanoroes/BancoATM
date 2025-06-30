package Models;

public class Conta {
    
    private int id;
    private String conta;
    private String tipo;
    private double saldo;
    private String data;

    public Conta(String conta, String data, String tipo, Double saldo) {
        this.conta = conta;
        this.data = data;
        this.tipo = tipo;
        this.saldo = saldo;
    }
    
    public Conta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
