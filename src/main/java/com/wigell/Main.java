package com.wigell;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

public class Main extends Application {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("hibernate");
    final ObservableList olArtist = FXCollections.observableArrayList();//dropdown
    final ObservableList olPerson = FXCollections.observableArrayList();//dropdown
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Layout
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        HBox hBox3 = new HBox();
        HBox hBox4 = new HBox();
        HBox hBox5 = new HBox();
        HBox hBox6 = new HBox();
        VBox vBoxLeft = new VBox();
        VBox vBoxRight = new VBox();
        VBox vBoxBottom = new VBox();
        VBox vBoxPerson = new VBox();
        VBox vBoxConcert = new VBox();
        VBox vBoxAttend = new VBox();
        TextArea textArea = new TextArea();

        // Buttons
        Button bAddPerson = new Button("Lägg till");
        Button bAddConcert = new Button("Lägg till");
        Button bUpdateComboBoxes = new Button("Uppdatera");
        Button bGoing = new Button("Anmälda");
        Button bAttend = new Button("Skriv upp mig!");

        // Textfields
        TextField tfName = new TextField();
        tfName.setPromptText("Namn");
        TextField tfAge = new TextField();
        TextField tfArtist = new TextField();
        tfArtist.setPromptText("Artist/Grupp");
        TextField tfdate = new TextField();
        tfdate.setPromptText("yyyy-mm-dd");

        // Labels
        Label lName = new Label("Namn");
        lName.setMinWidth(80);
        Label lAge = new Label("Ålder");
        lAge.setMinWidth(80);
        Label lArtist = new Label("Artist/Grupp");
        lArtist.setMinWidth(80);
        Label lDate = new Label("Datum");
        lDate.setMinWidth(80);
        Label lAttending = new Label("Anmälda");
        lAttending.setMinWidth(80);

        // ComboBoxes
        ComboBox comboBox1 = new ComboBox(olArtist);
        comboBox1.setPromptText("Artist/Grupp");
        ComboBox comboBox2 = new ComboBox(olPerson);
        comboBox2.setPromptText("Namn");

        // Button actions
        bAddPerson.setOnAction(event -> {
           addPerson(tfName, tfAge);
            tfName.clear();
            tfAge.clear();
        });
        bAddConcert.setOnAction(event -> {
            addConcert(tfArtist, tfdate);
            tfArtist.clear();
            tfdate.clear();
        });
        bUpdateComboBoxes.setOnAction(event -> {
            clearComboBox(comboBox1);
            clearComboBox(comboBox2);
            updateComboBoxPerson();
            updateComboBoxArtist();
        });
        bAttend.setOnAction(event -> {
         attendConcert(comboBox1, comboBox2);
        });
        bGoing.setOnAction(event -> {
            going(comboBox1, textArea);
        });


        BorderPane borderPane = new BorderPane();
        // add to hBox
        hBox1.getChildren().addAll(lName, tfName);
        hBox2.getChildren().addAll(lAge, tfAge);
        hBox3.getChildren().addAll(bAddPerson);
        hBox4.getChildren().addAll(lArtist, tfArtist);
        hBox5.getChildren().addAll(lDate, tfdate);
        hBox6.getChildren().addAll(bAddConcert);

        // add to VBoxes
        vBoxBottom.getChildren().addAll(lAttending, textArea);
        vBoxPerson.getChildren().addAll(hBox1, hBox2, hBox3);
        vBoxConcert.getChildren().addAll(hBox4, hBox5, hBox6);
        vBoxLeft.getChildren().addAll(vBoxPerson, vBoxConcert);
        vBoxAttend.getChildren().addAll(comboBox1, comboBox2, bUpdateComboBoxes, bAttend, bGoing);
        vBoxRight.getChildren().addAll(vBoxAttend);


        // Add to layout
        borderPane.setLeft(vBoxLeft);
        borderPane.setRight(vBoxRight);
        borderPane.setBottom(vBoxBottom);
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void going(ComboBox comboBox1, TextArea textArea) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            String selectedArtist =(String)comboBox1.getSelectionModel().getSelectedItem();
            Query query = entityManager.createNativeQuery("SELECT name " +
                    "FROM consert " +
                    "JOIN concertPerson " +
                    "ON consert.concertId = concertPerson.concertId " +
                    "JOIN person " +
                    "ON person.personId =concertPerson.personId " +
                    "WHERE consert.artist = '"+selectedArtist+"'");
            List<String>going = query.getResultList();
            textArea.clear();
            for(String s : going){
                textArea.appendText(s+"\n");
                System.out.println(s);
            }

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void attendConcert(ComboBox comboBox1, ComboBox comboBox2) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

           String selctedArtist = (String)comboBox1.getSelectionModel().getSelectedItem();
           String selctedPerson = (String)comboBox2.getSelectionModel().getSelectedItem();

           Query queryArtist = entityManager.createNativeQuery("SELECT concertId FROM consert WHERE artist = '"+selctedArtist+ "'");
           List<Integer>chosenArtist = queryArtist.getResultList();
           int iArtist = chosenArtist.get(0);

            Query queryPerson = entityManager.createNativeQuery("SELECT personId FROM person WHERE name = '"+selctedPerson+ "'");
            List<Integer>chosenPerson = queryPerson.getResultList();
            int iperson = chosenPerson.get(0);

            addConcertPerson(iArtist, iperson);

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void addConcertPerson(int iArtist, int iperson) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

        ConcertPerson cp = new ConcertPerson();
        cp.setPersonId(iperson);
        cp.setConcertId(iArtist);
        entityManager.persist(cp);
            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
        }

    }

    private void clearComboBox(ComboBox comboBox1) {

    }


    private void updateComboBoxArtist() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createNativeQuery("SELECT artist FROM consert");
            List<String>artists = query.getResultList();
            for(String a : artists){
                olArtist.add(a);
            }

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void updateComboBoxPerson() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createNativeQuery("SELECT name FROM person");
            List<String>persons = query.getResultList();
            for(String p : persons){
                olPerson.add(p);
            }

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void addConcert(TextField tfArtist, TextField tfdate) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Consert consert = new Consert();
            consert.setArtist(tfArtist.getText());
            consert.setDate(Date.valueOf(tfdate.getText()));
            entityManager.persist(consert);

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void addPerson(TextField tfName, TextField tfAge) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Person person = new Person();
            person.setName(tfName.getText());
            person.setAge(Integer.parseInt(tfAge.getText()));
            entityManager.persist(person);

            transaction.commit();

        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }
}
