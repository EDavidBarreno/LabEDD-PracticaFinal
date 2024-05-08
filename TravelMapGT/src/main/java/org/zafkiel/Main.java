package org.zafkiel;

import org.zafkiel.frontend.HomePage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        HomePage homePage = new HomePage();
        homePage.setContentPane(homePage.panelHomePage);
        homePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homePage.setUndecorated(true);
        homePage.setVisible(true);

    }
}