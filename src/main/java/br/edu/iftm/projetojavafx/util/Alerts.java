package br.edu.iftm.projetojavafx.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

    public static void showAlert(String titulo, String cabecalho, String mensagem, AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.show();
    }

    public static Optional<ButtonType> mostrarConfirmacao(String titulo, String conteudo){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        return alert.showAndWait();
    }
}
