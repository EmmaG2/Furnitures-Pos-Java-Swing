package views;

import controllers.FurnituresController;
import models.Furniture;
import repositories.FurnitureRepository;
import services.FurnitureService;
import utils.components.ComonTextStyle;
import utils.PriceFormatter;
import utils.components.TitleStyledFont;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class FurnituresStockFrame extends JPanel {
    private final FurnituresController furnituresController;
    private final JLabel mainTitle = new JLabel("Muebles", SwingConstants.CENTER);

    private final JTable furnitureStockTable = new JTable();
    private final DefaultTableModel furnitureStockTableModel = new DefaultTableModel();

    private final JButton btnUpdate = new JButton("Actualizar información");

    private final JButton btnAddFurniture = new JButton("Agregar Mueble");
    private final JButton btnUpdateFurniture = new JButton("Actualizar Mueble");
    private final JButton btnUpdateStock = new JButton("Actualizar Stock");
    private final JButton btnRemoveFurniture = new JButton("Remover Mueble");

    private String furnitureId;

    public FurnituresStockFrame() throws IOException {
        FurnitureRepository furnitureRepository = FurnitureRepository.getInstance();
        FurnitureService furnitureService = new FurnitureService(furnitureRepository);
        furnituresController = new FurnituresController(furnitureService);

        setLayout(null);

        displayComponents();

        btnUpdate.addActionListener(_ -> {
            updateTableData();
        });

        initializeTable();

        btnAddFurniture.addActionListener(_ -> {
            FurnitureFormFrame furnitureFormFrame = new FurnitureFormFrame("Agregar mueble", "Agregar Nuevo Mueble");
            furnitureFormFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    updateTableData();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    updateTableData();
                }
            });
        });

        btnUpdateFurniture.addActionListener(_ -> {
            FurnitureFormFrame furnitureFormFrame = new FurnitureFormFrame(
                    "Actualizar Mueble",
                    "Actualizar Mueble",
                    furnitureId
            );

            furnitureFormFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    updateTableData();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    updateTableData();
                }
            });
        });

        btnRemoveFurniture.addActionListener(_ -> {
            int opt = JOptionPane.showConfirmDialog(null,
                    "¿Deseas eliminar el mueble " + furnitureId + "?", "Eliminar Mueble", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (opt == JOptionPane.YES_OPTION) {
                furnituresController.deleteById(furnitureId);
                updateTableData();
            }

        });
    }

    private void updateTableData() {
        furnitureStockTableModel.setRowCount(0);

        List<Furniture> allFurnitures = furnituresController.getAllFurnitures();

        for (Furniture furniture : allFurnitures) {
            Object[] row = {
                    furniture.getId(),
                    furniture.getName(),
                    furniture.getStock(),
                    PriceFormatter.formatPrice(furniture.getSalePrice()),
                    PriceFormatter.formatPrice(furniture.getPurchasePrice()),
            };
            furnitureStockTableModel.addRow(row);
        }
    }

    private void initializeTable() {
        JScrollPane tableScrollPane = new JScrollPane(furnitureStockTable);
        String[] colums = {"Id", "Nombre", "Stock", "Precio de venta", "Precio de compra"};
        furnitureStockTableModel.setColumnIdentifiers(colums);

        List<Furniture> allFurnitures = furnituresController.getAllFurnitures();

        for (Furniture furniture : allFurnitures) {
            Object[] row = {
                    furniture.getId(),
                    furniture.getName(),
                    furniture.getStock(),
                    PriceFormatter.formatPrice(furniture.getSalePrice()),
                    PriceFormatter.formatPrice(furniture.getPurchasePrice()),
            };
            furnitureStockTableModel.addRow(row);
        }

        furnitureStockTable.setModel(furnitureStockTableModel);
        furnitureStockTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        furnitureStockTable.setPreferredScrollableViewportSize(new Dimension(1141, 554));
        furnitureStockTable.setFillsViewportHeight(true);
        furnitureStockTable.setFont(new ComonTextStyle(13));

        furnitureStockTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = furnitureStockTable.getSelectedRow();

            if (!e.getValueIsAdjusting() && selectedRow != -1) {
                furnitureId = (String) furnitureStockTable.getValueAt(selectedRow, 0);
            }
        });

        tableScrollPane.setViewportView(furnitureStockTable);
        tableScrollPane.setBounds(62, 122, 1141, 554);
        add(tableScrollPane);
    }

    private void displayComponents() {
        mainTitle.setFont(new TitleStyledFont(33));
        mainTitle.setBounds(482, 27, 317, 45);

        btnUpdate.setBounds(62, 64, 208, 34);

        btnAddFurniture.setBounds(62, 680, 208, 34);

        btnUpdateFurniture.setBounds(550, 680, 208, 34);

        btnRemoveFurniture.setBounds(995, 680, 208, 34);

        add(btnUpdate);
        add(mainTitle);
        add(btnAddFurniture);
        add(btnUpdateFurniture);
        add(btnRemoveFurniture);
    }
}
