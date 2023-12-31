package model.entities;

public class Produto {
    private Integer id;
    private String nome;
    private String lote;
    private Double preco;
    private Integer quantidade;

    public Produto(){
    }

    public Produto(Integer id, String nome, String lote, Double preco, Integer quantidade){
        this.id = id;
        this.nome = nome;
        this.lote = lote;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getLote() {
        return lote;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getPreco() {
        return preco;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Produto other = (Produto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", nome=" + nome + " , lote = " + lote + ", preco = " + preco +
                ", quantidade = " +quantidade  + "]";
    }
}
