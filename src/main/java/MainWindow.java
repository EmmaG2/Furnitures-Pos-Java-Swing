import views.FurnituresStockFrame;
import views.SalesReportsFrame;
import views.GenerateSaleFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {
    public MainWindow() throws IOException {
        super("Proyecto");
        setSize(new Dimension(1280, 800));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Realizar ventas", new GenerateSaleFrame());
        tabbedPane.addTab("Reportes de ventas", new SalesReportsFrame());
        tabbedPane.addTab("Stock de muebles", new FurnituresStockFrame());

        add(tabbedPane);

        setVisible(true);
    }
}
