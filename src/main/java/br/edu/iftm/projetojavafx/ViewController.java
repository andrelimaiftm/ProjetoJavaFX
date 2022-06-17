package br.edu.iftm.projetojavafx;

import br.edu.iftm.projetojavafx.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML
    private MenuItem menuItemVendedor;

    @FXML
    private MenuItem menuItemDepartamento;

    @FXML
    private MenuItem menuItemSobre;

    @FXML
    public void onMenuItemVendedorAction(){
        System.out.println("onMenuItemVendedorAction");
    }

    @FXML
    public void onMenuItemDepartamentoAction(){
        loadView2("listaDepartamento.fxml");
        //System.out.println("onMenuItemDepartamentoAction");
    }

    @FXML
    public void onMenuItemSobreAction(){
        loadView("sobre.fxml");
        //System.out.println("onMenuItemSobreAction");
    }





    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void loadView(String nomeDaView){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeDaView));
            VBox novaVBox =  loader.load();
            Scene scenePrincipal = Main.getScene();
            VBox vboxPrincipal = (VBox) ((ScrollPane) scenePrincipal.getRoot()).getContent();

            Node menuPrincipal = vboxPrincipal.getChildren().get(0);
            vboxPrincipal.getChildren().clear();
            vboxPrincipal.getChildren().add(menuPrincipal);
            vboxPrincipal.getChildren().addAll(novaVBox.getChildren());

        } catch (IOException e) {
            Alerts.showAlert("IOException", "Erro ao carregar a view",
                    e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void loadView2(String nomeDaView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeDaView));
            VBox novaVBox =  loader.load();
            Scene scenePrincipal = Main.getScene();
            VBox vboxPrincipal = (VBox) ((ScrollPane) scenePrincipal.getRoot()).getContent();
            Node menuPrincipal = vboxPrincipal.getChildren().get(0);
            vboxPrincipal.getChildren().clear();
            vboxPrincipal.getChildren().add(menuPrincipal);
            vboxPrincipal.getChildren().addAll(novaVBox.getChildren());

            ListaDepartamentoController controller  = loader.getController();
            controller.setDepartamentoServico(new DepartamentoService());
            controller.updateTableView();

        } catch (IOException e) {
            Alerts.showAlert("IOException", "Erro ao carregar a view",
                    e.getMessage(), Alert.AlertType.ERROR);
        }catch (IllegalAccessException e){
            Alerts.showAlert("IllegalAccessException", "Erro ao carregar a view",
                    e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}