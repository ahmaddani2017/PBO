/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package library;

import library.controller.LibControl;
import library.view.Buku;
import library.controller.DbSetup;

public class App {
    public static void main(String[] args) {
        DbSetup.createTable();
        LibControl controller = new LibControl();
        Buku view = new Buku(controller);
        view.setVisible(true);
    }
}
