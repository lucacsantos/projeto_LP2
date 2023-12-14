package gui.fornecedor;

import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Fornecedor;
import model.services.FornecedorService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FormularioFornecedorViewController implements Initializable {

    private Fornecedor entidade;

    private FornecedorService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField textId;

    @FXML
    private TextField textNome;

    @FXML
    private TextField textEndereco;

    @FXML
    private TextField textEmpresa;
    @FXML
    private Button btSalvar;

    @FXML
    private Button btCancelar;

    public void setFornecedor(Fornecedor entidade) {
        this.entidade = entidade;
    }

    public void setFornecedorService(FornecedorService service) {
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

    private Fornecedor getDadosFormulario() {
        Fornecedor obj = new Fornecedor();
        obj.setId(Utils.tryParseToInt(textId.getText()));
        obj.setNome(textNome.getText());
        obj.setEmpresa(textEmpresa.getText());
        obj.setEndereco(textEndereco.getText());
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
            textEmpresa.setText(entidade.getEmpresa());
            textEndereco.setText(entidade.getEndereco());
        }
    }
}