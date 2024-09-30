package views;

import controllers.FurnituresController;
import models.Furniture;
import repositories.FurnitureRepository;
import services.FurnitureService;
import utils.components.JLabelStyled;
import utils.components.TitleStyledFont;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class FurnitureFormFrame extends JFrame {
    private final FurnituresController furnituresController;

    private final JLabel mainTitle;
    private final JLabelStyled lblName = new JLabelStyled("Nombre:", 14);
    private final JLabelStyled lblPurchaseOrFabricationDate = new JLabelStyled("Fecha de compra/fabricación:", 14);
    private final JLabelStyled lblStock = new JLabelStyled("Stock inicial:", 14);
    private final JLabelStyled lblSalePrice = new JLabelStyled("Precio de venta:", 14);
    private final JLabelStyled lblPurchasePrice = new JLabelStyled("Precio de compra:", 14);

    private final JLabelStyled warnLblName = new JLabelStyled("", 12);
    private final JLabelStyled warnLblPurchaseOrFabricationDate = new JLabelStyled("", 12);
    private final JLabelStyled warnLblStock = new JLabelStyled("", 12);
    private final JLabelStyled warnLblSalePrice = new JLabelStyled("", 12);
    private final JLabelStyled warnLblPurchasePrice = new JLabelStyled("", 12);

    private final JTextField inputName = new JTextField();
    private final JTextField inputPurchaseOrFabricationDate = new JTextField();
    private final JTextField inputStock = new JTextField();
    private final JTextField inputSalePrice = new JTextField();
    private final JTextField inputPurchasePrice = new JTextField();

    private final Border defaultBorder = inputPurchaseOrFabricationDate.getBorder();
    private final Color defaultColor = new Color(0, 0, 0);
    private final Color errorColor = new Color(255, 0, 0);

    private JButton btnAddFurniture = new JButton("Agregar");

    private boolean inputsAreValid;

    public FurnitureFormFrame(String title1, String title2) {
        super(title1);
        FurnitureRepository furnitureRepository;
        try {
            furnitureRepository = FurnitureRepository.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FurnitureService furnitureService = new FurnitureService(furnitureRepository);
        furnituresController = new FurnituresController(furnitureService);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        mainTitle = new JLabel(title2, JLabel.CENTER);

        initializeComponents();

        btnAddFurniture.addActionListener(_ -> {
            validateInputs();

            if (inputsAreValid) {
                createAndSaveFurniture();
                dispose();
            }
        });

        setVisible(true);
    }

    public FurnitureFormFrame(String title1, String title2, String id) {
        super(title1);
        FurnitureRepository furnitureRepository;
        btnAddFurniture = new JButton("Actualizar");
        try {
            furnitureRepository = FurnitureRepository.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FurnitureService furnitureService = new FurnitureService(furnitureRepository);
        furnituresController = new FurnituresController(furnitureService);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        mainTitle = new JLabel(title2, JLabel.CENTER);

        initializeComponents();
        startFields(id);

        btnAddFurniture.addActionListener(_ -> {
            validateInputs();

            if (inputsAreValid)
            {
                updateFurniture(id);
                dispose();
            }
        });

        setVisible(true);
    }

    private void initializeComponents() {
        mainTitle.setBounds(222, 49, 370, 40);
        mainTitle.setFont(new TitleStyledFont(33));

        lblName.setBounds(51, 144, 65, 19);
        lblPurchaseOrFabricationDate.setBounds(51, 203, 225, 19);
        lblStock.setBounds(51, 270, 95, 19);
        lblSalePrice.setBounds(269, 270, 123, 19);
        lblPurchasePrice.setBounds(518, 270, 138, 19);

        inputName.setBounds(132, 144, 595, 19);
        inputPurchaseOrFabricationDate.setBounds(293, 203, 434, 19);
        inputStock.setBounds(161, 270, 56, 19);
        inputSalePrice.setBounds(410, 270, 56, 19);
        inputPurchasePrice.setBounds(671, 270, 56, 19);

        warnLblName.setBounds(51, 171, 200, 15);
        warnLblPurchaseOrFabricationDate.setBounds(51, 225, 758, 15);
        warnLblStock.setBounds(51, 297, 200, 15);
        warnLblSalePrice.setBounds(269, 297, 200, 15);

        btnAddFurniture.setBounds(348, 376, 103, 22);

        add(mainTitle);
        add(lblName);
        add(lblPurchaseOrFabricationDate);
        add(lblStock);
        add(lblSalePrice);
        add(lblPurchasePrice);

        add(inputName);
        add(inputPurchaseOrFabricationDate);
        add(inputStock);
        add(inputSalePrice);
        add(inputPurchasePrice);

        add(warnLblName);
        add(warnLblPurchaseOrFabricationDate);
        add(warnLblStock);
        add(warnLblSalePrice);
        add(warnLblPurchasePrice);

        add(btnAddFurniture);
    }

    private void validateInputs() {
        String purchaseOrFabricationDateText = inputPurchaseOrFabricationDate.getText();
        if (inputName.getText().isEmpty()) {
            setErrorStyle(warnLblName, lblName, inputName, "Este campo no puede estar vacío");
            inputsAreValid = false;
        } else {
            setDefaultStyle(warnLblName, lblName, inputName);
        }
        if (purchaseOrFabricationDateText.isEmpty()) {
            setErrorStyle(
                    warnLblPurchaseOrFabricationDate,
                    lblPurchaseOrFabricationDate,
                    inputPurchaseOrFabricationDate,
                    "Este campo no puede estar vacío."
            );
            inputsAreValid = false;
        } else if (!purchaseOrFabricationDateText.matches("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
            setErrorStyle(
                    warnLblPurchaseOrFabricationDate,
                    lblPurchaseOrFabricationDate,
                    inputPurchaseOrFabricationDate,
                    "Por favor ingresa una fecha válida en el formato: yyyy-MM-dd"
            );
        }

        if (!purchaseOrFabricationDateText.isEmpty() &&
                purchaseOrFabricationDateText.matches("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
        ) setDefaultStyle(warnLblPurchaseOrFabricationDate,
                lblPurchaseOrFabricationDate,
                inputPurchaseOrFabricationDate);

        if (inputStock.getText().isEmpty()) {
            setErrorStyle(warnLblStock, lblStock, inputStock, "Este campo no puede estar vacío");
        } else setDefaultStyle(warnLblStock, lblStock, inputStock);

        if (inputSalePrice.getText().isEmpty()) {
            setErrorStyle(warnLblSalePrice, lblSalePrice, inputSalePrice, "Este campo no puede estar vacío.");
        } else setDefaultStyle(warnLblSalePrice, lblSalePrice, inputSalePrice);


        inputsAreValid =
                !inputName.getText().isEmpty() &&
                        !purchaseOrFabricationDateText.isEmpty() &&
                        purchaseOrFabricationDateText.matches("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") &&
                        !inputStock.getText().isEmpty() &&
                        !inputSalePrice.getText().isEmpty();
    }

    private void setErrorStyle(JLabel warnLabel, JLabel label, JTextField input, String text) {
        label.setForeground(errorColor);
        warnLabel.setText(text);
        warnLabel.setForeground(errorColor);
        input.setBorder(BorderFactory.createLineBorder(errorColor));
        input.setForeground(errorColor);
    }

    private void setDefaultStyle(JLabel warnLabel, JLabel label, JTextField input) {
        label.setForeground(defaultColor);
        warnLabel.setText("");
        warnLabel.setForeground(defaultColor);
        input.setBorder(BorderFactory.createLineBorder(defaultColor));
        input.setForeground(defaultColor);
    }

    private void createAndSaveFurniture() {
        Furniture furnitureInstance = createFurnitureFromInputs();

        furnituresController.save(furnitureInstance);
    }

    private void startFields(String id) {
        Furniture originalFurniture = furnituresController.getFurnitureById(id)
                .orElseThrow(() -> new RuntimeException("No fue encontrado el mueble"));

        inputName.setText(originalFurniture.getName());
        inputPurchasePrice.setText(originalFurniture.getPurchasePrice() + "");
        inputSalePrice.setText(originalFurniture.getSalePrice() + "");
        inputStock.setText(originalFurniture.getStock() + "");
        inputPurchaseOrFabricationDate.setText(originalFurniture.getPurchaseOrFabricationDate());

    }

    private void updateFurniture(String id) {
        Furniture furnitureInstance = createFurnitureFromInputs();

        furnituresController.updateById(id, furnitureInstance);
    }

    private Furniture createFurnitureFromInputs() {
        Furniture furnitureInstance = new Furniture();

        furnitureInstance.setName(inputName.getText());
        furnitureInstance.setPurchaseOrFabricationDate(inputPurchaseOrFabricationDate.getText());
        furnitureInstance.setStock(Integer.parseInt(inputStock.getText()));
        furnitureInstance.setSalePrice(Double.parseDouble(inputSalePrice.getText()));

        if (inputPurchasePrice.getText().isEmpty()) {
            furnitureInstance.setPurchasePrice(0);
        } else furnitureInstance.setPurchasePrice(Double.parseDouble(inputPurchasePrice.getText()));

        return furnitureInstance;
    }

}
