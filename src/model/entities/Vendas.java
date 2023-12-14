package model.entities;

public class Vendas {
    private Integer id;
    private int idCliente;
    private int idFuncionario;
    private Double preco;
    private String nomeProduto;

    public Vendas(){

    }
    public Vendas(Integer id, int idCliente, int idFuncionario, Double preco, String nomeProduto){
        this.id = id;
        this.idCliente = idCliente;
        this.idFuncionario = idFuncionario;
        this.preco = preco;
        this.nomeProduto = nomeProduto;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }



    public String getNomeProduto() {
        return nomeProduto;
    }

    public Double getPreco() {
        return preco;
    }

    public Integer getId() {
        return id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdFuncionario() {
        return idFuncionario;
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
        Vendas other = (Vendas) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
