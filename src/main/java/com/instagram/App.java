package com.instagram;

import com.instagram.controller.MainController;

public class App {

    public static void main(String[] args) {

        MainController mainController =
                new MainController();

        mainController.start();
    }
}