package br.edu.iftm.projetojavafx;

import br.edu.iftm.projetojavafx.util.Alerts;
import br.edu.iftm.projetojavafx.util.Utils;
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

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListaVendedorController implements Initializable, AtualizaDadoListener {

    private VendedorService service;

    private ObservableList<Vendedor> obsLista;

    @FXML
    private TableView<Vendedor> tableViewVendedor;

    @FXML
    private TableColumn<Vendedor, Integer> tableColumnId;

    @FXML
    private TableColumn<Vendedor, String> tableColumnEmail;

    @FXML
    private TableColumn<Vendedor, Date> tableColumnDataNasc;

    @FXML
    private TableColumn<Vendedor, Double> tableColumnSalario;

    @FXML
    private TableColumn<Vendedor, String> tableColumnNome;

    @FXML
    private TableColumn<Vendedor, Vendedor> tableColumnEdit;

    @FXML
    private TableColumn<Vendedor, Vendedor> tableColumnRemove;

    @FXML
    private Button btNovo;

    @FXML
    public void onBtNovoAction(ActionEvent event){
        //System.out.println("onBtNovoAction");
        Stage parentStage = Utils.stageCorrente(event);
        Vendedor obj = new Vendedor();
        criaCaixaDeDialogo(obj, "formVendedor.fxml", parentStage);

    }

    public void setVendedorServico(VendedorService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicalizaNodes();
    }

    private void inicalizaNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
        Utils.formatTableColumnDate(tableColumnDataNasc, "dd/MM/yyyy");

        tableColumnSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        Utils.formatTableColumnDouble(tableColumnSalario, 2);

        Stage stage = (Stage) Main.getScene().getWindow();
        tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView() throws IllegalAccessException {
        if(service == null){
            throw new IllegalAccessException("Servico está nulo");
        }
        List<Vendedor> lista = service.buscaTodos();
        obsLista = FXCollections.observableList(lista);
        tableViewVendedor.setItems(obsLista);
        inicializaBotaoEditar();
        inicializaBotaoRemover();

    }

    private void criaCaixaDeDialogo(Vendedor obj, String nomeDaView, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeDaView));
            Pane pane = loader.load();

            FormVendedorController controller = loader.getController();
            controller.setVendedor(obj);
            controller.setServices(new VendedorService(), new DepartamentoService());
            controller.carregarObjetosAssociados();
            controller.adicionaAtualizaDadosListener(this);
            controller.atualizaDadosFormulario();

            Stage caixaDialogoStage = new Stage();
            caixaDialogoStage.setTitle("Digite dados para Vendedor");
            caixaDialogoStage.setScene(new Scene(pane));
            caixaDialogoStage.setResizable(false);
            caixaDialogoStage.initOwner(parentStage);
            caixaDialogoStage.initModality(Modality.WINDOW_MODAL);
            caixaDialogoStage.showAndWait();

        }catch (IOException e){
            Alerts.showAlert("IOExpetion", "Erro ao carregar a view",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }

    }

    @Override
    public void onMudancaDados() throws IllegalAccessException {
        updateTableView();
    }

    private void inicializaBotaoEditar() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
            private final Button button = new Button("editar");
            @Override
            protected void updateItem(Vendedor obj, boolean vazio) {
                super.updateItem(obj, vazio);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> criaCaixaDeDialogo(
                                obj, "formVendedor.fxml",Utils.stageCorrente(event)));
            }
        });
    }

    private void inicializaBotaoRemover() {
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
            private final Button button = new Button("remover");
            @Override
            protected void updateItem(Vendedor obj, boolean vazio) {
                super.updateItem(obj, vazio);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> {
                    try {
                        removeVendedor(obj);
                    } catch (IllegalAccessException e) {
                        Alerts.showAlert("IllegalAccessException", "Erro ao remover departamento.",
                                e.getMessage(),
                                Alert.AlertType.ERROR);
                    }
                });
            }
        });
    }

    private void removeVendedor(Vendedor obj) throws IllegalAccessException {
        Optional<ButtonType> result = Alerts.mostrarConfirmacao("Confirmação",
                "Você tem certeza que deseja remover o Departamento?");

        if(result.get() == ButtonType.OK){
            if(service == null){
                throw new IllegalStateException("Servico está nulo.");
            }

            service.remover(obj);
            updateTableView();
        }
    }
}
