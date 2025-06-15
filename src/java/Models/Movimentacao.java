package Models;

public class Movimentacao {
    private String data;
    private String descricao;
    private String tipo;
    private double valor;
    private String conta;

    public Movimentacao(String data, String descricao, String tipo, double valor, String conta) {
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.conta = conta;
    }

    public String getData() {
        return data;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getTipo() {
        return tipo;
    }
    public double getValor() {
        return valor;
    }
    public String getConta() {
        return conta;
    }
}
