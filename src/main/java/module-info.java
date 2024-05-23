module com.github.stephenwanjala.demospringfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires okhttp3;
    requires com.google.gson;
    requires annotations;

    opens com.github.stephenwanjala.demospringfx to javafx.fxml;
    exports com.github.stephenwanjala.demospringfx;
}