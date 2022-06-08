module br.edu.iftm.projetojavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens br.edu.iftm.projetojavafx to javafx.fxml;
    exports br.edu.iftm.projetojavafx;
}