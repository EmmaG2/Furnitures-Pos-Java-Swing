package views;

import controllers.FurnituresController;
import controllers.SalesController;
import models.Furniture;
import models.FurnitureSold;
import repositories.FurnitureRepository;
import repositories.SalesRepository;
import services.FurnitureService;
import services.SaleService;
import utils.components.ComonTextStyle;
import utils.PriceFormatter;
import utils.components.TitleStyledFont;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateSaleFrame extends JPanel {
    private final FurnituresController furnituresController;
    private final SalesController salesController;

    private final JPanel resumeTab = new JPanel();
    private final JLabel totalPrice = new JLabel("$0.00");
    private final JButton btnPay = new JButton("Pagar orden");
    JButton btnAdd = new JButton("Agregar Mueble");
    JButton btnUpdate = new JButton("Actualizar tabla");
    JLabel lblTotalPrice = new JLabel("Total: ");


    private final JTable resultTable = new JTable();
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final List<FurnitureSold> order = new ArrayList<>();

    private String furnitureName = "";
    private String furniturePrice = "";
    private String furnitureId = "";

    private List<Furniture> furnitures = new ArrayList<>();

    public GenerateSaleFrame() throws IOException {
        FurnitureRepository furnitureRepository = FurnitureRepository.getInstance();
        SalesRepository salesRepository = SalesRepository.getInstance();

        FurnitureService furnitureService = new FurnitureService(furnitureRepository);
        SaleService saleService = new SaleService(salesRepository, furnitureRepository);
        AtomicInteger orderIndex = new AtomicInteger();

        this.furnituresController = new FurnituresController(furnitureService);
        this.salesController = new SalesController(saleService);

        setLayout(null);

        initializeComponets();

        initializeTableResults();

        btnAdd.addActionListener(_ -> {
            if (furnitureName.isEmpty() || furniturePrice.isEmpty()) return;
            if (itemIsInTabView(furnitureName)) return;

            Furniture realFurniture = furnituresController.getFurnitureById(furnitureId)
                    .orElseThrow(()-> new RuntimeException("No se encontró el mueble"));

            FurnitureSold furnitureSold = new FurnitureSold(
                    realFurniture.getId(),
                    furnitureName,
                    1,
                    realFurniture.getSalePrice()
            );

            order.add(furnitureSold);

            JPanel item = new JPanel();
            item.setBackground(Color.WHITE);
            item.setLayout(null);
            item.setPreferredSize(new Dimension(310, 63));

            item.setLayout(null);
            JLabel furnitureNameLabel = new JLabel(order.get(orderIndex.get()).getFurnitureName());
            JLabel furniturePriceLabel = new JLabel(PriceFormatter.formatPrice(order.get(orderIndex.get()).getPrice()));
            JSpinner furnitureQuantitySpinner = new JSpinner();

            orderIndex.incrementAndGet();

            furnitureNameLabel.setBounds(30, 20, 63, 19);
            furniturePriceLabel.setBounds(123, 20, 83, 19);
            furnitureQuantitySpinner.setBounds(213, 20, 43, 19);
            furnitureQuantitySpinner.setModel(new SpinnerNumberModel(1, 0, 100, 1));

            furnitureQuantitySpinner.addChangeListener(_ -> {
                updateQuantityToBuy(furnitureNameLabel.getText(), (int) furnitureQuantitySpinner.getValue());
                updateAndDisplayTotalPrice();
            });

            item.add(furnitureNameLabel);
            item.add(furniturePriceLabel);
            item.add(furnitureQuantitySpinner);

            resumeTab.add(item);

            updateAndDisplayTotalPrice();

            item.setVisible(true);

            resumeTab.setVisible(true);
        });

        btnPay.addActionListener(_ -> {

            int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de confirmar la compra?", "Confirmar compra", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                clearEmptyFurnitures();
                salesController.generateSale(order);
                resumeTab.removeAll();
                order.clear();
                updateAndDisplayTotalPrice();
                orderIndex.set(0);
            }
        });

        btnUpdate.addActionListener(_ -> {
            updateDataTable();
        });
    }

    private void initializeComponets() {
        resumeTab.setLayout(new FlowLayout(FlowLayout.LEFT));
        resumeTab.setBounds(924, 22, 330, 700);

        btnAdd.setBounds(33, 680, 132, 40);
        btnUpdate.setBounds(194, 680, 132, 40);
        btnPay.setBounds(730, 680, 162, 40);
        btnPay.setVisible(false);

        lblTotalPrice.setFont(new Font("Manrope", Font.BOLD, 33));
        lblTotalPrice.setBounds(366, 680, 200, 40);

        totalPrice.setBounds(486, 680, 200, 40);
        totalPrice.setForeground(new Color(255, 0, 0));
        totalPrice.setFont(new TitleStyledFont(33));

        add(resumeTab);
        add(btnAdd);
        add(btnPay);
        add(totalPrice);
        add(lblTotalPrice);
        add(btnUpdate);
    }

    private void clearEmptyFurnitures() {
        order.removeIf(furniture -> furniture.getQuantity() == 0);
    }

    private void initializeTableResults() {
        String[] columnNames = {"Id", "Nombre", "Precio"};
        tableModel.setColumnIdentifiers(columnNames);
        furnitures = furnituresController.getAllFurnitures();

        for (Furniture furniture : furnitures) {
            String priceFormated = PriceFormatter.formatPrice(furniture.getSalePrice());

            Object[] row = {furniture.getId(), furniture.getName(), priceFormated};
            tableModel.addRow(row);
        }

        resultTable.setModel(tableModel);

        resultTable.setPreferredScrollableViewportSize(new Dimension(527, 230));
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        resultTable.setFillsViewportHeight(true);
        resultTable.setFont(new ComonTextStyle(13));

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(33, 22, 863, 640);

        resultTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = resultTable.getSelectedRow();

            if (!e.getValueIsAdjusting() && selectedRow != -1) {
                furnitureId = (String) resultTable.getValueAt(selectedRow, 0);
                furnitureName = (String) resultTable.getValueAt(selectedRow, 1);
                furniturePrice = (String) resultTable.getValueAt(selectedRow, 2);
            }
        });

        add(scrollPane);
    }

    private void updateAndDisplayTotalPrice() {
        double totalPriceValue = 0.0;

        for (FurnitureSold furnitureSold : order) {
            totalPriceValue += furnitureSold.getPrice() * furnitureSold.getQuantity();
        }

        btnPay.setVisible(totalPriceValue != 0);

        totalPrice.setText(PriceFormatter.formatPrice(totalPriceValue));
    }

    private void updateQuantityToBuy(String fName, int quantity) {
        for (FurnitureSold furnitureSold : order) {
            if (Objects.equals(fName, furnitureSold.getFurnitureName())) {
                furnitureSold.setQuantity(quantity);
            }
        }
    }

    private boolean itemIsInTabView(String furnitureNameLabel) {
        for (Component item : resumeTab.getComponents()) {
            JPanel panel = (JPanel) item;
            for (Component component : panel.getComponents()) {
                JLabel label;

                if (component instanceof JLabel) {
                    label = (JLabel) component;
                    if (furnitureNameLabel.equals(label.getText())) return true;
                }
            }
        }
        return false;
    }

    private void updateDataTable() {
        tableModel.setRowCount(0);
        furnitures = furnituresController.getAllFurnitures();

        for (Furniture furniture : furnitures) {
            String priceFormated = PriceFormatter.formatPrice(furniture.getSalePrice());

            Object[] row = {furniture.getId(), furniture.getName(), priceFormated};
            tableModel.addRow(row);
        }
    }
}
