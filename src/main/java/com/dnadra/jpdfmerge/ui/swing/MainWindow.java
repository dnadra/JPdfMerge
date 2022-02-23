package com.dnadra.jpdfmerge.ui.swing;

import com.dnadra.jpdfmerge.tools.JPdfMerge;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class MainWindow extends JFrame {
    @Serial
    private static final long serialVersionUID = -7773794866340238454L;
    private static final int GRID_COLUMN_COUNT = 2;
    private static final int GRID_ROW_COUNT = 8;
    private static final double GRID_CELL_GROW = 1.0;
    private static final String FILE_EXT = ".pdf";
    private DefaultListModel<String> listInputFilesModel;
    private JList<String> listInputFiles;
    private JTextField textFieldOutputFile;
    private JTextPane textPaneMessages;
    private JButton btnRemove;
    private JButton btnMerge;

    private MainWindow() {
        this.initialize();
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new MainWindow();
            } catch (final Exception e) {
                throw new RuntimeException("Failed to load main window", e);
            }
        });
    }

    private void addComponentsToPane() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[GRID_COLUMN_COUNT];
        gridBagLayout.columnWeights = new double[GRID_COLUMN_COUNT];
        gridBagLayout.rowHeights = new int[GRID_ROW_COUNT];
        gridBagLayout.rowWeights = new double[GRID_ROW_COUNT];
        gridBagLayout.columnWeights[0] = GRID_CELL_GROW;
        gridBagLayout.rowWeights[3] = GRID_CELL_GROW;
        gridBagLayout.rowWeights[7] = GRID_CELL_GROW;
        this.getContentPane().setLayout(gridBagLayout);

        JLabel lblInputFiles = new JLabel("Input Files");
        GridBagConstraints gbc_lblInputFiles = new GridBagConstraints();
        gbc_lblInputFiles.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblInputFiles.anchor = GridBagConstraints.NORTH;
        gbc_lblInputFiles.insets = new Insets(5, 5, 5, 5);
        gbc_lblInputFiles.gridx = 0;
        gbc_lblInputFiles.gridy = 0;
        this.getContentPane().add(lblInputFiles, gbc_lblInputFiles);

        JScrollPane listInputFilesScrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
        gbc_scrollPane.gridheight = 3;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        this.getContentPane().add(listInputFilesScrollPane, gbc_scrollPane);

        listInputFilesModel = new DefaultListModel<>();
        this.listInputFiles = new JList<>(listInputFilesModel);
        this.listInputFiles.addListSelectionListener(this::do_listInputFiles_ListSelectionListener);
        listInputFiles.setLayoutOrientation(JList.VERTICAL);
        listInputFilesScrollPane.setViewportView(this.listInputFiles);

        JButton btnAdd = new JButton("Add File");
        btnAdd.addActionListener(this::do_btnAdd_actionPerformed);
        GridBagConstraints gbc_btnAdd = new GridBagConstraints();
        gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnAdd.anchor = GridBagConstraints.NORTH;
        gbc_btnAdd.insets = new Insets(5, 5, 5, 5);
        gbc_btnAdd.gridx = 1;
        gbc_btnAdd.gridy = 1;
        this.getContentPane().add(btnAdd, gbc_btnAdd);

        this.btnRemove = new JButton("Remove File");
        this.btnRemove.addActionListener(this::do_btnRemove_actionPerformed);
        GridBagConstraints gbc_btnRemove = new GridBagConstraints();
        gbc_btnRemove.gridheight = 2;
        gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRemove.anchor = GridBagConstraints.NORTH;
        gbc_btnRemove.insets = new Insets(5, 5, 5, 5);
        gbc_btnRemove.gridx = 1;
        gbc_btnRemove.gridy = 2;
        this.getContentPane().add(this.btnRemove, gbc_btnRemove);

        JLabel lblOutputFile = new JLabel("Output File");
        GridBagConstraints gbc_lblOutputFile = new GridBagConstraints();
        gbc_lblOutputFile.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblOutputFile.anchor = GridBagConstraints.NORTH;
        gbc_lblOutputFile.insets = new Insets(5, 5, 5, 5);
        gbc_lblOutputFile.gridx = 0;
        gbc_lblOutputFile.gridy = 4;
        this.getContentPane().add(lblOutputFile, gbc_lblOutputFile);

        this.textFieldOutputFile = new JTextField();
        GridBagConstraints gbc_textFieldOutputFile = new GridBagConstraints();
        gbc_textFieldOutputFile.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldOutputFile.insets = new Insets(5, 5, 5, 5);
        gbc_textFieldOutputFile.gridx = 0;
        gbc_textFieldOutputFile.gridy = 5;
        this.getContentPane().add(this.textFieldOutputFile, gbc_textFieldOutputFile);
        this.textFieldOutputFile.setColumns(10);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(this::do_btnBrowse_actionPerformed);
        GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
        gbc_btnBrowse.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnBrowse.anchor = GridBagConstraints.NORTH;
        gbc_btnBrowse.insets = new Insets(5, 5, 5, 5);
        gbc_btnBrowse.gridx = 1;
        gbc_btnBrowse.gridy = 5;
        this.getContentPane().add(btnBrowse, gbc_btnBrowse);

        JLabel lblMessages = new JLabel("Messages");
        GridBagConstraints gbc_lblMessages = new GridBagConstraints();
        gbc_lblMessages.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblMessages.anchor = GridBagConstraints.NORTH;
        gbc_lblMessages.insets = new Insets(5, 5, 5, 5);
        gbc_lblMessages.gridx = 0;
        gbc_lblMessages.gridy = 6;
        this.getContentPane().add(lblMessages, gbc_lblMessages);

        JScrollPane textPaneMessagesScrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.insets = new Insets(5, 5, 5, 5);
        gbc_scrollPane_1.gridx = 0;
        gbc_scrollPane_1.gridy = 7;
        this.getContentPane().add(textPaneMessagesScrollPane, gbc_scrollPane_1);

        this.textPaneMessages = new JTextPane();
        textPaneMessagesScrollPane.setViewportView(this.textPaneMessages);

        this.btnMerge = new JButton("Merge");
        this.btnMerge.addActionListener(this::do_btnMerge_actionPerformed);
        GridBagConstraints gbc_btnMerge = new GridBagConstraints();
        gbc_btnMerge.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnMerge.insets = new Insets(5, 5, 5, 5);
        gbc_btnMerge.anchor = GridBagConstraints.NORTH;
        gbc_btnMerge.gridx = 1;
        gbc_btnMerge.gridy = 7;
        this.getContentPane().add(this.btnMerge, gbc_btnMerge);

        this.getContentPane().setSize(this.getContentPane().getPreferredSize());
    }

    private void initialize() {
        this.setTitle("JPdfMerge");
        this.setMinimumSize(new Dimension(600, 400));
        this.setBounds(100, 100, 600, 517);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.addComponentsToPane();
        this.pack();
        this.updateGui();
        this.setVisible(true);
    }

    private void updateGui() {
        this.btnMerge.setEnabled((this.listInputFiles.getModel().getSize() >= 2) &&
                (this.textFieldOutputFile.getText().trim().length() > 0));
        this.btnRemove.setEnabled(this.listInputFiles.getSelectedIndices().length != 0);
    }

    private void do_listInputFiles_ListSelectionListener(ListSelectionEvent listSelectionEvent) {
        this.updateGui();
    }

    private void do_btnAdd_actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return FILE_EXT;
            }

            @Override
            public boolean accept(File f) {
                return (f.isDirectory() || (f.isFile() && f.exists() && f.getName().endsWith(FILE_EXT)));
            }
        });
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File path : fileChooser.getSelectedFiles()) {
                this.listInputFilesModel.addElement(path.getAbsolutePath());
            }
            this.updateGui();
        }
    }

    private void do_btnRemove_actionPerformed(ActionEvent e) {
        if (this.listInputFiles.getSelectedIndices().length == 0) return;
        for (int i : this.listInputFiles.getSelectedIndices()) {
            this.listInputFilesModel.remove(i);
        }
        this.updateGui();
    }

    private void do_btnBrowse_actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return FILE_EXT;
            }

            @Override
            public boolean accept(File f) {
                return (f.isDirectory() || (f.isFile() && f.exists() && f.getName().endsWith(FILE_EXT)));
            }
        });
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(FILE_EXT)) path += FILE_EXT;
            this.textFieldOutputFile.setText(path);
            this.updateGui();
        }
    }

    private void do_btnMerge_actionPerformed(ActionEvent e) {
        JPdfMerge merge = new JPdfMerge();
        for (int i = 0; i < this.listInputFilesModel.getSize(); i++) {
            merge.addInputPdf(this.listInputFilesModel.get(i));
        }
        merge.setOutputPdf(this.textFieldOutputFile.getText());
        try {
            merge.mergePdfs();
            this.textPaneMessages.setText("Merge completed.");
        } catch (IOException ex) {
            this.textPaneMessages.setText("Failed to merge PDF files. Details:" + "\n" + ex);
        }
        this.updateGui();
    }
}
