package views;

import controllers.SalesController;
import models.FurnitureSold;
import models.Sale;
import repositories.FurnitureRepository;
import repositories.SalesRepository;
import services.SaleService;
import utils.components.JLabelStyled;
import utils.PriceFormatter;
import utils.components.SubtitleStyledFont;
import utils.components.TitleStyledFont;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SalesReportsFrame extends JPanel {
    private final SalesController salesController;

    private final JLabel mainTitle = new JLabel("Registro de ventas");
    private final JLabel salesByDateSubtitle = new JLabel("Obtener ventas por rango de fecha");
    private final JLabel saleByIdSubtitle = new JLabel("Obtener venta por ID");
    private final JLabel earningsValue = new JLabel("$0.00");

    private final JLabelStyled lblStartDate = new JLabelStyled("Fecha de inicio: ");
    private final JLabelStyled lblEndDate = new JLabelStyled("Fecha final: ");
    private final JLabelStyled lblSaleId = new JLabelStyled("ID de la venta: ");
    private final JLabel lblEarnings = new JLabel("Ganancias: ");

    private final JTextField inputStartDate = new JTextField();
    private final JTextField inputEndDate = new JTextField();
    private final JTextField inputSaleId = new JTextField();

    private final JButton btnGetSalesByDate = new JButton("Obtener ventas");
    private final JButton btnGetSalesById = new JButton("Obtener venta");

    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable();

    public SalesReportsFrame() {
        SalesRepository salesRepository;
        FurnitureRepository furnitureRepository;
        try {
            salesRepository = SalesRepository.getInstance();
            furnitureRepository = FurnitureRepository.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SaleService saleService = new SaleService(salesRepository, furnitureRepository);

        salesController = new SalesController(saleService);

        setLayout(null);

        displayComponents();
        initiallizeTable();

        btnGetSalesByDate.addActionListener(_ -> {
            try {
                displaySalesByDateInTheTable(inputStartDate.getText(), inputEndDate.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnGetSalesById.addActionListener(_ -> {
            displaySalesByIdInTheTable(inputSaleId.getText());
        });
    }

    private void displaySalesByDateInTheTable(String startDate, String endDate) throws IOException {
        tableModel.setRowCount(0);
        List<Sale> salesByDate = salesController.getSalesBetweenDates(startDate, endDate);

        for (Sale sale : salesByDate) {
            StringBuilder furnituresSoldString = new StringBuilder();
            StringBuilder furnitureQuantityString = new StringBuilder();
            for (FurnitureSold furnitureSold : sale.getFurnituresSold()) {

                furnituresSoldString
                        .append(furnitureSold.getFurnitureName())
                        .append(" ");

                furnitureQuantityString
                        .append(furnitureSold.getQuantity())
                        .append(" ");
            }

            Object[] row = {
                    sale.getId(),
                    sale.getDate(),
                    furnituresSoldString.toString(),
                    furnitureQuantityString.toString(),
                    PriceFormatter.formatPrice(sale.getTotalPrice())
            };

            tableModel.addRow(row);
        }
        tableModel.fireTableDataChanged();
        calculateAndDisplayEarnings(salesByDate);
    }

    private void displaySalesByIdInTheTable(String id ) {
        tableModel.setRowCount(0);
        Sale sale = salesController.getSaleById(id).orElseThrow(() -> new RuntimeException("El pepe"));

        StringBuilder furnituresSoldString = new StringBuilder();
        StringBuilder furnitureQuantityString = new StringBuilder();

        for (FurnitureSold furnitureSold: sale.getFurnituresSold()) {
            furnituresSoldString.append(furnitureSold.getFurnitureName()).append(" ");
            furnitureQuantityString.append(furnitureSold.getQuantity()).append(" ");
        }

        String[] row = {
                sale.getId(),
                sale.getDate(),
                furnituresSoldString.toString(),
                furnitureQuantityString.toString(),
                PriceFormatter.formatPrice(sale.getTotalPrice())
        };

        tableModel.addRow(row);
        tableModel.fireTableDataChanged();
        calculateAndDisplayEarnings(sale);
    }

    private void calculateAndDisplayEarnings(List<Sale> sales) {
        double eargings = 0;

        for (Sale sale : sales) {
            eargings += sale.getTotalPrice();
        }

        earningsValue.setText(PriceFormatter.formatPrice(eargings));

    }

    private void calculateAndDisplayEarnings(Sale sale) {
        earningsValue.setText(PriceFormatter.formatPrice(sale.getTotalPrice()));
    }

    private void displayComponents() {
        mainTitle.setFont(new TitleStyledFont(33));
        mainTitle.setBounds(482, 27, 317, 45);

        salesByDateSubtitle.setFont(new SubtitleStyledFont(20));
        saleByIdSubtitle.setFont(new SubtitleStyledFont(20));

        salesByDateSubtitle.setBounds(81, 112, 354, 27);
        saleByIdSubtitle.setBounds(925, 112, 248, 27);

        lblStartDate.setBounds(81, 173, 91, 18);
        lblEndDate.setBounds(81, 213, 81, 18);

        inputStartDate.setBounds(201, 173, 214, 18);
        inputEndDate.setBounds(201, 213, 214, 18);

        lblSaleId.setBounds(849, 172, 91, 18);
        inputSaleId.setBounds(969, 172, 214, 18);

        lblEarnings.setBounds(82, 683, 192, 35);
        lblEarnings.setFont(new TitleStyledFont(33));

        earningsValue.setBounds(290, 683, 210, 35);
        earningsValue.setFont(new TitleStyledFont(33));
        earningsValue.setForeground(new Color(255, 0, 0));

        btnGetSalesByDate.setBounds(201, 248, 135, 25);
        btnGetSalesById.setBounds(969, 248, 135, 25);

        add(mainTitle);
        add(salesByDateSubtitle);
        add(saleByIdSubtitle);
        add(lblStartDate);
        add(lblEndDate);
        add(lblSaleId);
        add(lblEarnings);

        add(inputStartDate);
        add(inputEndDate);
        add(inputSaleId);

        add(btnGetSalesByDate);
        add(btnGetSalesById);
        add(earningsValue);
    }

    private void initiallizeTable() {
        String[] columns = {"Id", "Fecha", "Mubles", "Cantidad vendida", "total"};
        tableModel.setColumnIdentifiers(columns);
        table.setModel(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setPreferredScrollableViewportSize(new Dimension(112, 365));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBounds(81, 309, 1112, 365);
        scrollPane.setVisible(true);
        add(scrollPane);
    }
}

