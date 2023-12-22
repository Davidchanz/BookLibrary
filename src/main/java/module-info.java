module org.dbtest {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires jakarta.persistence;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires aspose.pdf;
    requires org.apache.commons.io;
    requires com.jfoenix;

    opens org.dbtest to javafx.fxml;
    opens org.dbtest.controller to javafx.fxml, aspose.pdf, org.apache.commons.io, com.jfoenix;
    opens org.dbtest.model to org.hibernate.orm.core, javafx.fxml;

    exports org.dbtest;
    exports org.dbtest.controller;
    exports org.dbtest.model;
    exports org.dbtest.service;
    opens org.dbtest.service to javafx.fxml, org.hibernate.orm.core;
}