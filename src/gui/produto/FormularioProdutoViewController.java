package gui.produto;

import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Produto;
import model.services.ProdutoService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FormularioProdutoViewController implements Initializable {

    private Produto entidade;

    private ProdutoService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField textId;

    @FXML
    private TextField textNome;

    @FXML
    private TextField textLote;

    @FXML
    private TextField textPreco;
    @FXML
    private TextField textQuantidade;
    @FXML
    private Button btSalvar;

    @FXML
    private Button btCancelar;

    public void setProduto(Produto entidade) {
        this.entidade = entidade;
    }

    public void setProdutoService(ProdutoService service) {
        this.service = service;
    }

    public void atualizarDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSalvarAction(ActionEvent event) {
        if (entidade == null) {
            throw new IllegalStateException("Entidade está nula");
        }
        if (service == null) {
            throw new IllegalStateException("Service está nulo");
        }

        entidade = getDadosFormulario();
        service.salvarOuAtualizar(entidade);
        notificarDataChangeListeners();
        Utils.currentStage(event).close();

    }

    private void notificarDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Produto getDadosFormulario() {
        Produto obj = new Produto();
        obj.setId(Utils.tryParseToInt(textId.getText()));
        obj.setNome(textNome.getText());
        obj.setLote(textLote.getText());
        obj.setPreco(Double.valueOf(textPreco.getText()));
        obj.setQuantidade(Integer.valueOf(textQuantidade.getText()));
        return obj;
    }

    @FXML
    public void onBtCancelarAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void atualizarDadosFormulario() {
        if (entidade == null) {
            throw new IllegalStateException("Entidade está nula");
        }
        if (entidade.getId() == null)
            textId.setText("");
        else {
            textId.setText(String.valueOf(entidade.getId()));
            textNome.setText(entidade.getNome());
            textLote.setText(entidade.getLote());
            textPreco.setText(String.valueOf(entidade.getPreco()));
            textQuantidade.setText(String.valueOf(entidade.getQuantidade()));
        }
    }
}