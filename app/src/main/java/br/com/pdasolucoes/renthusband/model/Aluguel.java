package br.com.pdasolucoes.renthusband.model;

/**
 * Created by PDA on 05/07/2017.
 */

public class Aluguel {

    private int id;
    private String dataSolicitacao;
    private int diasSolicitacao;
    private int confirmacao;
    private String dataAluguel;
    private String dataDevolucao;
    private int idFerramenta;
    private int idUsuarioSolicita;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(String dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public int getDiasSolicitacao() {
        return diasSolicitacao;
    }

    public void setDiasSolicitacao(int diasSolicitacao) {
        this.diasSolicitacao = diasSolicitacao;
    }

    public int getConfirmacao() {
        return confirmacao;
    }

    public void setConfirmacao(int confirmacao) {
        this.confirmacao = confirmacao;
    }

    public String getDataAluguel() {
        return dataAluguel;
    }

    public void setDataAluguel(String dataAluguel) {
        this.dataAluguel = dataAluguel;
    }

    public String getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(String dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public int getIdFerramenta() {
        return idFerramenta;
    }

    public void setIdFerramenta(int idFerramenta) {
        this.idFerramenta = idFerramenta;
    }

    public int getIdUsuarioSolicita() {
        return idUsuarioSolicita;
    }

    public void setIdUsuarioSolicita(int idUsuarioSolicita) {
        this.idUsuarioSolicita = idUsuarioSolicita;
    }
}
