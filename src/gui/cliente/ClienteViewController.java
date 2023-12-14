package gui.cliente;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.services.ClienteService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClienteViewController implements Initializable, DataChangeListener {
    @FXML
    private Button btNovo;
    @FXML
    private TableView<Cliente> table;
    @FXML
    private TableColumn<Cliente,Integer> id;
    @FXML
    private TableColumn<Cliente,String> nome;
    @FXML
    private TableColumn<Cliente,String> endereco;
    @FXML
    private TableColumn<Cliente,String> CPF;
    @FXML
    private TableColumn<Cliente, Cliente> editar;
    @FXML
    private TableColumn<Cliente, Cliente> deletar;

    private ObservableList<Cliente> obs;
    private ClienteService service;

    @FXML
    public void onBtNovoAction(ActionEvent event) {
        Stage stagePai = Utils.currentStage(event);
        Cliente obj = new Cliente();
        criarFormulario(obj, "/gui/cliente/FormularioCliView.fxml", stagePai);
    }

    public void setClienteService(ClienteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTable();
    }

    private void inicializarTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        CPF.setCellValueFactory(new PropertyValueFactory<>("CPF"));
        Stage stage = (Stage) Main.retornaCena().getWindow();
        table.prefHeightProperty().bind(stage.heightProperty());
    }

    public void atualizarTable() {
        if (service == null) {
            throw new IllegalStateException("Service está nulo");
        }
        List<Cliente> list = service.buscarTodos();
        obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
        inicializarBotoesEditar();
        inicializarBotoesRemover();
    }

    private void criarFormulario(Cliente obj, String absoluteNome, Stage stagePai) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteNome));
            Pane pane = loader.load();

            FormularioCliViewController controller = loader.getController();
            controller.setCliente(obj);
            controller.setClienteService(new ClienteService());
            controller.atualizarDataChangeListener(this);
            controller.atualizarDadosFormulario();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastro de Cliente");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(stagePai);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        atualizarTable();
    }

    private void inicializarBotoesEditar() {
        editar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        editar.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
            private final Button button = new Button("editar");

            @Override
            protected void updateItem(Cliente obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> criarFormulario(obj, "/gui/cliente/FormularioCliView.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void inicializarBotoesRemover() {
        deletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deletar.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
            private final Button button = new Button("remover");

            @Override
            protected void updateItem(Cliente obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removerEntity(obj));
            }
        });
    }

    private void removerEntity(Cliente obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Quer realmente deletar?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remover(obj);
                atualizarTable();
            }
            catch (DbIntegrityException e) {
                Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}


