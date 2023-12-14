package gui.vendas;

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
import model.entities.Vendas;
import model.services.VendaService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VendaViewController implements Initializable, DataChangeListener {
    @FXML
    private Button btNovo;
    @FXML
    private TableView<Vendas> table;
    @FXML
    private TableColumn<Vendas,Integer> id;
    @FXML
    private TableColumn<Vendas,Integer> idCliente;
    @FXML
    private TableColumn<Vendas,Integer> idFuncionario;

    @FXML
    private TableColumn<Vendas,Double> preco;
    @FXML
    private TableColumn<Vendas,String> nomeProduto;
    @FXML
    private TableColumn<Vendas, Vendas> editar;
    @FXML
    private TableColumn<Vendas, Vendas> deletar;

    private ObservableList<Vendas> obs;
    private VendaService service;

    @FXML
    public void onBtNovoAction(ActionEvent event) {
        Stage stagePai = Utils.currentStage(event);
        Vendas obj = new Vendas();
        criarFormulario(obj, "/gui/vendas/FormularioVendaView.fxml", stagePai);
    }

    public void setVendaService(VendaService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTable();
    }

    private void inicializarTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        idFuncionario.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        nomeProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        Stage stage = (Stage) Main.retornaCena().getWindow();
        table.prefHeightProperty().bind(stage.heightProperty());
    }

    public void atualizarTable() {
        if (service == null) {
            throw new IllegalStateException("Service está nulo");
        }
        List<Vendas> list = service.buscarTodos();
        obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
        inicializarBotoesEditar();
        inicializarBotoesRemover();
    }

    private void criarFormulario(Vendas obj, String absoluteNome, Stage stagePai) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteNome));
            Pane pane = loader.load();

            FormularioVendaViewController controller = loader.getController();
            controller.setVenda(obj);
            controller.setVendaService(new VendaService());
            controller.atualizarDataChangeListener(this);
            controller.atualizarDadosFormulario();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastro de Vendas");
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
        editar.setCellFactory(param -> new TableCell<Vendas, Vendas>() {
            private final Button button = new Button("editar");

            @Override
            protected void updateItem(Vendas obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> criarFormulario(obj, "/gui/vendas/FormularioVendaView.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void inicializarBotoesRemover() {
        deletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deletar.setCellFactory(param -> new TableCell<Vendas, Vendas>() {
            private final Button button = new Button("remover");

            @Override
            protected void updateItem(Vendas obj, boolean empty) {
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

    private void removerEntity(Vendas obj) {
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