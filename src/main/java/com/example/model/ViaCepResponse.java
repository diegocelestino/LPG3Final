package com.example.model;

import com.google.gson.annotations.SerializedName;

public class ViaCepResponse {
    
    @SerializedName("cep")
    private String cep;
    
    @SerializedName("logradouro")
    private String logradouro;
    
    @SerializedName("complemento")
    private String complemento;
    
    @SerializedName("bairro")
    private String bairro;
    
    @SerializedName("localidade")
    private String localidade;
    
    @SerializedName("uf")
    private String uf;
    
    @SerializedName("ibge")
    private String ibge;
    
    @SerializedName("gia")
    private String gia;
    
    @SerializedName("ddd")
    private String ddd;
    
    @SerializedName("siafi")
    private String siafi;
    
    @SerializedName("erro")
    private Boolean erro;
    
    public ViaCepResponse() {}
    
    // Getters and Setters
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    
    public String getLocalidade() { return localidade; }
    public void setLocalidade(String localidade) { this.localidade = localidade; }
    
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    
    public String getIbge() { return ibge; }
    public void setIbge(String ibge) { this.ibge = ibge; }
    
    public String getGia() { return gia; }
    public void setGia(String gia) { this.gia = gia; }
    
    public String getDdd() { return ddd; }
    public void setDdd(String ddd) { this.ddd = ddd; }
    
    public String getSiafi() { return siafi; }
    public void setSiafi(String siafi) { this.siafi = siafi; }
    
    public Boolean getErro() { return erro; }
    public void setErro(Boolean erro) { this.erro = erro; }
    
    public boolean hasError() {
        return erro != null && erro;
    }
    
    @Override
    public String toString() {
        return "ViaCepResponse{" +
               "cep='" + cep + '\'' +
               ", logradouro='" + logradouro + '\'' +
               ", bairro='" + bairro + '\'' +
               ", localidade='" + localidade + '\'' +
               ", uf='" + uf + '\'' +
               '}';
    }
}
