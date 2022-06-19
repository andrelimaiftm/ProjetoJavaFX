package br.edu.iftm.projetojavafx;

import br.edu.iftm.projetojavafx.Exception.ValidacaoException;
import br.edu.iftm.projetojavafx.util.Alerts;
import br.edu.iftm.projetojavafx.util.RestricoesTela;
import br.edu.iftm.projetojavafx.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class FormVendedorController implements Initializable {

    private VendedorService service;

    private Vendedor vendedor;

    private List<AtualizaDadoListener> atualizaDadoListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpDataNasc;

    @FXML
    private TextField txtSalario;

    @FXML
    private Label labelNomeErro;

    @FXML
    private Label labelEmailErro;

    @FXML
    private Label labelDataNascErro;
    @FXML
    private Label labelSalarioErro;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btCancelar;

    public void setVendedor(Vendedor vendedor){
        this.vendedor = vendedor;
    }

    public void setVendedorService(VendedorService service){
        this.service = service;
    }

    @FXML
    public void onBtSalvarAction(ActionEvent event){
        //System.out.println("onBtSalvar");
        if(vendedor == null){
            throw new IllegalStateException("Vendedor está vazio");
        }
        try {
            vendedor = getDadosFormulario();
            service.SaveOrUpdate(vendedor);
            notificaAtualizaDadoListerners();
            Utils.stageCorrente(event).close();
        }catch (ValidacaoException e){
            setMensagemErros(e.getErros());
        }

    }

    @FXML
    public void onBtCancelarAction(ActionEvent event){
        Utils.stageCorrente(event).close();
    }

    public void adicionaAtualizaDadosListener(AtualizaDadoListener listener){
        atualizaDadoListeners.add(listener);
    }

    private void notificaAtualizaDadoListerners() {
        for (AtualizaDadoListener listener: atualizaDadoListeners) {
            try {
                listener.onMudancaDados();
            } catch (IllegalAccessException e) {
                Alerts.showAlert("IllegalAccessException", "Erro ao atualizar dados",
                        e.getMessage(),
                        Alert.AlertType.ERROR);
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();

    }

    private void initializeNodes(){
        RestricoesTela.setTextFieldInteger(txtId);
        RestricoesTela.setTextFieldMaxLength(txtNome, 70);
        RestricoesTela.setTextFieldDouble(txtSalario);
        RestricoesTela.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpDataNasc, "dd/MM/yyyy");
    }

    public void atualizaDadosFormulario(){
        if(vendedor == null){
            throw new IllegalStateException("Vendedor está vazio");
        }
        txtId.setText(vendedor.getId() == null ? "" : String.valueOf(vendedor.getId()));
        txtNome.setText(vendedor.getNome());
        txtEmail.setText(vendedor.getEmail());

        Locale.setDefault(Locale.US);
        txtSalario.setText( vendedor.getSalario() == null ? "" : String.format("%.2f", vendedor.getSalario()));
        if(vendedor.getDataNasc() != null){
            dpDataNasc.setValue(LocalDate.ofInstant(vendedor.getDataNasc().toInstant(),
                    ZoneId.systemDefault()));
        }
    }

    private Vendedor getDadosFormulario() {
        Vendedor obj = new Vendedor();

        ValidacaoException exception = new ValidacaoException("Erro de validação");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtNome.getText() == null || txtNome.getText().trim().equals("")){
            exception.addErro("nome", "Campo não pode ser vazio");
        }
        obj.setNome(txtNome.getText());

        if (exception.getErros().size() > 0){
            throw exception;
        }
        return obj;
    }

    private void setMensagemErros(Map<String, String> erros) {
        Set<String> campos = erros.keySet();

        if(campos.contains("nome")){
            labelNomeErro.setText(erros.get("nome"));
        }
    }
}
