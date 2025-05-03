import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CloudStorageApp extends JFrame {
    private JPanel contentPane;
    private JTable fileTable;
    private JTextField searchField;
    private JButton uploadButton, downloadButton, newFolderButton, deleteButton, shareButton;
    private JLabel statusLabel, storageLabel;
    private JTree folderTree;
    private boolean isLoggedIn = true;
    private String currentUser;
    
    // Sample data for demonstration
    private String[][] fileData = {
        {"Document.docx", "Document", "2.5 MB", "04/05/2024"},
        {"Presentation.pptx", "Presentation", "5.8 MB", "04/02/2024"},
        {"Image.jpg", "Image", "1.2 MB", "04/01/2024"},
        {"Spreadsheet.xlsx", "Spreadsheet", "3.4 MB", "03/28/2024"},
        {"Notes.txt", "Text", "0.1 MB", "03/25/2024"}
    };
    
    private String[] columnNames = {"Name", "Type", "Size", "Modified Date"};

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    // Remove direct instantiation since we'll create it from LoginPage
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public CloudStorageApp(User user) {
        this.currentUser = user.getUsername();
        setTitle("Cloud Storage System - Welcome " + currentUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        
        // Initialize all components
        initializeComponents();
        
        // Show the main window directly
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Main content pane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        // Create menu bar
        createMenuBar();
        
        // Create toolbar
        createToolbar();
        
        // Create main split pane
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(200);
        contentPane.add(splitPane, BorderLayout.CENTER);
        
        // Create folder tree
        createFolderTree(splitPane);
        
        // Create file table panel
        createFilePanel(splitPane);
        
        // Create status bar
        createStatusBar();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        JMenuItem uploadItem = new JMenuItem("Upload Files...");
        fileMenu.add(uploadItem);
        
        JMenuItem downloadItem = new JMenuItem("Download Selected");
        fileMenu.add(downloadItem);
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        
        JMenuItem renameItem = new JMenuItem("Rename");
        editMenu.add(renameItem);
        
        JMenuItem deleteItem = new JMenuItem("Delete");
        editMenu.add(deleteItem);
        
        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        
        JMenuItem refreshItem = new JMenuItem("Refresh");
        viewMenu.add(refreshItem);
        
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Cloud Storage System v1.0\n© 2024 Cloud Storage Inc.", 
                "About", 
                JOptionPane.INFORMATION_MESSAGE)
        );
        helpMenu.add(aboutItem);
    }
    
    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        contentPane.add(toolBar, BorderLayout.NORTH);
        
        uploadButton = new JButton("Upload");
        uploadButton.setToolTipText("Upload files to cloud");
        uploadButton.addActionListener(e -> uploadFiles());
        toolBar.add(uploadButton);
        
        downloadButton = new JButton("Download");
        downloadButton.setToolTipText("Download selected files");
        downloadButton.addActionListener(e -> downloadFiles());
        downloadButton.setEnabled(false);
        toolBar.add(downloadButton);
        
        toolBar.addSeparator();
        
        newFolderButton = new JButton("New Folder");
        newFolderButton.setToolTipText("Create new folder");
        newFolderButton.addActionListener(e -> createNewFolder());
        toolBar.add(newFolderButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.setToolTipText("Delete selected files");
        deleteButton.addActionListener(e -> deleteFiles());
        deleteButton.setEnabled(false);
        toolBar.add(deleteButton);
        
        toolBar.addSeparator();
        
        shareButton = new JButton("Share");
        shareButton.setToolTipText("Share selected files");
        shareButton.addActionListener(e -> shareFiles());
        shareButton.setEnabled(false);
        toolBar.add(shareButton);
        
        toolBar.add(Box.createHorizontalGlue());
        
        JLabel searchLabel = new JLabel("Search: ");
        toolBar.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 25));
        toolBar.add(searchField);
        
        JButton searchButton = new JButton("Go");
        searchButton.addActionListener(e -> searchFiles());
        toolBar.add(searchButton);
    }
    
    private void createFolderTree(JSplitPane splitPane) {
        // Create root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("My Cloud");
        
        // Create child nodes
        DefaultMutableTreeNode documents = new DefaultMutableTreeNode("Documents");
        DefaultMutableTreeNode images = new DefaultMutableTreeNode("Images");
        DefaultMutableTreeNode videos = new DefaultMutableTreeNode("Videos");
        DefaultMutableTreeNode music = new DefaultMutableTreeNode("Music");
        DefaultMutableTreeNode shared = new DefaultMutableTreeNode("Shared with me");
        
        // Add child nodes to root
        root.add(documents);
        root.add(images);
        root.add(videos);
        root.add(music);
        root.add(shared);
        
        // Add some sub-folders
        documents.add(new DefaultMutableTreeNode("Work"));
        documents.add(new DefaultMutableTreeNode("Personal"));
        images.add(new DefaultMutableTreeNode("Vacation"));
        images.add(new DefaultMutableTreeNode("Family"));
        
        // Create the tree
        folderTree = new JTree(root);
        folderTree.setShowsRootHandles(true);
        
        // Add tree to a scroll pane
        JScrollPane treeScrollPane = new JScrollPane(folderTree);
        
        // Add to split pane
        splitPane.setLeftComponent(treeScrollPane);
    }
    
    private void createFilePanel(JSplitPane splitPane) {
        JPanel filePanel = new JPanel(new BorderLayout());
        
        // Create table model with data
        DefaultTableModel model = new DefaultTableModel(fileData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table
        fileTable = new JTable(model);
        fileTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        fileTable.setRowHeight(25);
        fileTable.getTableHeader().setReorderingAllowed(false);
        
        // Add selection listener
        fileTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean hasSelection = fileTable.getSelectedRow() != -1;
                downloadButton.setEnabled(hasSelection);
                deleteButton.setEnabled(hasSelection);
                shareButton.setEnabled(hasSelection);
            }
        });
        
        // Add table to scroll pane
        JScrollPane tableScrollPane = new JScrollPane(fileTable);
        filePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Add to split pane
        splitPane.setRightComponent(filePanel);
    }
    
    private void createStatusBar() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new EmptyBorder(2, 5, 2, 5));
        statusPanel.setLayout(new BorderLayout());
        
        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        storageLabel = new JLabel("Storage: 10.9 MB / 15 GB used");
        statusPanel.add(storageLabel, BorderLayout.EAST);
        
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        
        updateStatusBar();
    }
    
    private void updateStatusBar() {
        if (isLoggedIn) {
            statusLabel.setText("Logged in as: " + currentUser);
        } else {
            statusLabel.setText("Not logged in");
        }
    }
    
    // Action methods
    private void uploadFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // In a real app, you would upload the files to the cloud here
            JOptionPane.showMessageDialog(this, 
                "Files would be uploaded to the cloud here.", 
                "Upload", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // For demo, add the selected files to the table
            DefaultTableModel model = (DefaultTableModel) fileTable.getModel();
            java.io.File[] files = fileChooser.getSelectedFiles();
            
            for (java.io.File file : files) {
                String type = getFileType(file.getName());
                String size = (file.length() / 1024) + " KB";
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String date = sdf.format(new Date(file.lastModified()));
                
                model.addRow(new Object[]{file.getName(), type, size, date});
            }
        }
    }
    
    private String getFileType(String fileName) {
        if (fileName.lastIndexOf('.') != -1 && fileName.lastIndexOf('.') != 0) {
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            switch (extension.toLowerCase()) {
                case "doc":
                case "docx":
                    return "Document";
                case "xls":
                case "xlsx":
                    return "Spreadsheet";
                case "ppt":
                case "pptx":
                    return "Presentation";
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                    return "Image";
                case "mp3":
                case "wav":
                    return "Audio";
                case "mp4":
                case "avi":
                case "mov":
                    return "Video";
                case "txt":
                    return "Text";
                case "pdf":
                    return "PDF";
                default:
                    return extension.toUpperCase();
            }
        }
        return "Unknown";
    }
    
    private void downloadFiles() {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length > 0) {
            StringBuilder files = new StringBuilder();
            for (int row : selectedRows) {
                files.append(fileTable.getValueAt(row, 0)).append("\n");
            }
            
            JOptionPane.showMessageDialog(this, 
                "The following files would be downloaded:\n" + files.toString(), 
                "Download", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void createNewFolder() {
        String folderName = JOptionPane.showInputDialog(this, 
            "Enter folder name:", 
            "New Folder", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (folderName != null && !folderName.trim().isEmpty()) {
            // In a real app, you would create the folder in the cloud here
            JOptionPane.showMessageDialog(this, 
                "Folder '" + folderName + "' would be created here.", 
                "New Folder", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // For demo, add the folder to the tree
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) 
                folderTree.getLastSelectedPathComponent();
            
            if (selectedNode == null) {
                selectedNode = (DefaultMutableTreeNode) folderTree.getModel().getRoot();
            }
            
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(folderName);
            selectedNode.add(newNode);
            
            // Refresh tree
            ((DefaultTreeModel) folderTree.getModel()).reload(selectedNode);
        }
    }
    
    private void deleteFiles() {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length > 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete the selected files?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // In a real app, you would delete the files from the cloud here
                
                // For demo, remove the rows from the table
                DefaultTableModel model = (DefaultTableModel) fileTable.getModel();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    model.removeRow(selectedRows[i]);
                }
            }
        }
    }
    
    private void shareFiles() {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length > 0) {
            StringBuilder files = new StringBuilder();
            for (int row : selectedRows) {
                files.append(fileTable.getValueAt(row, 0)).append("\n");
            }
            
            String email = JOptionPane.showInputDialog(this, 
                "Enter email address to share with:", 
                "Share Files", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (email != null && !email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "The following files would be shared with " + email + ":\n" + files.toString(), 
                    "Share", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void searchFiles() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Searching for: " + searchTerm, 
                "Search", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // In a real app, you would search the cloud storage here
        }
    }
}