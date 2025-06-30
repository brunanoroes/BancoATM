package Models;

import java.math.BigDecimal;
import java.util.Date;

public class Investimento {

    private int id;
    private int contaId;
    private String tipoInvestimento;
    private BigDecimal valorAplicado;
    private Date dataAplicacao;
    private Date dataVencimento;
    private BigDecimal rendimentoEsperado;
    private BigDecimal taxaAnualPercentual;

    public Investimento() {
    }

    public Investimento(int contaId, String tipoInvestimento, BigDecimal valorAplicado, Date dataAplicacao, Date dataVencimento, BigDecimal rendimentoEsperado, BigDecimal taxaAnualPercentual) {
        this.contaId = contaId;
        this.tipoInvestimento = tipoInvestimento;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = dataAplicacao;
        this.dataVencimento = dataVencimento;
        this.rendimentoEsperado = rendimentoEsperado;
        this.taxaAnualPercentual = taxaAnualPercentual;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContaId() {
        return contaId;
    }

    public void setContaId(int contaId) {
        this.contaId = contaId;
    }

    public String getTipoInvestimento() {
        return tipoInvestimento;
    }

    public void setTipoInvestimento(String tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }

    public BigDecimal getValorAplicado() {
        return valorAplicado;
    }

    public void setValorAplicado(BigDecimal valorAplicado) {
        this.valorAplicado = valorAplicado;
    }

    public Date getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(Date dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getRendimentoEsperado() {
        return rendimentoEsperado;
    }

    public void setRendimentoEsperado(BigDecimal rendimentoEsperado) {
        this.rendimentoEsperado = rendimentoEsperado;
    }

    public BigDecimal getTaxaAnualPercentual() {
        return taxaAnualPercentual;
    }

    public void setTaxaAnualPercentual(BigDecimal taxaAnualPercentual) {
        this.taxaAnualPercentual = taxaAnualPercentual;
    }
}

