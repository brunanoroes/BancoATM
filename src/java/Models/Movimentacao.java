package Models;

public class Movimentacao {
    private String data;
    private String descricao;
    private String tipo;
    private double valor;

    private String contaOrigem;
    private String contaDestino;

    public Movimentacao(String data, String descricao, String tipo, double valor,
                        String contaOrigem, String contaDestino) {
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }

    public String getData() { return data; }
    public String getDescricao() { return descricao; }
    public String getTipo() { return tipo; }
    public double getValor() { return valor; }
    public String getContaOrigem() { return contaOrigem; }
    public String getContaDestino() { return contaDestino; }
}
