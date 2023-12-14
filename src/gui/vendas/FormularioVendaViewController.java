package gui.vendas;

import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Vendas;
import model.services.VendaService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class FormularioVendaViewController implements Initializable {

    private Vendas entidade;

    private VendaService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField textId;

    @FXML
    private TextField textIdCliente;

    @FXML
    private TextField textIdFuncionario;

    @FXML
    private TextField textPreco;
    @FXML
    private TextField textNomeProduto;
    @FXML
    private Button btSalvar;

    @FXML
    private Button btCancelar;

    public void setVenda(Vendas entidade) {
        this.entidade = entidade;
    }

    public void setVendaService(VendaService service) {
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

    private Vendas getDadosFormulario() {
        Vendas obj = new Vendas();
        obj.setId(Utils.tryParseToInt(textId.getText()));
        obj.setIdCliente(Integer.parseInt(textIdCliente.getText()));
        obj.setIdFuncionario(Integer.parseInt(textIdFuncionario.getText()));
        obj.setPreco(Double.valueOf(textPreco.getText()));
        obj.setNomeProduto(textNomeProduto.getText());
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
            textIdCliente.setText(String.valueOf(entidade.getIdCliente()));
            textIdFuncionario.setText(String.valueOf(entidade.getIdFuncionario()));
            textPreco.setText(String.valueOf(entidade.getPreco()));
            textNomeProduto.setText(entidade.getNomeProduto());
        }
    }
}