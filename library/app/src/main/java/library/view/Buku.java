package library.view;

import library.controller.LibControl;
import library.model.Book;
import library.model.Loan;
import library.model.Member;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Buku extends JFrame {
    private final LibControl controller;

    // Components
    private JTextField bookIdField, bookTitleField, bookAuthorField;
    private JTextField memberIdField, memberNameField, memberEmailField, memberPhoneField;
    private JTextField searchBookField, searchMemberField;
    private JTextField loanIdField, loanMemberIdField, returnLoanIdField;
    private JDateChooser actualReturnDateField;
    private JComboBox<String> loanBookIdComboBox;
    private JTable bookTable, memberTable, loanTable;
    private DefaultTableModel bookTableModel, memberTableModel, loanTableModel;
    private JLabel bookInfoLabel;
    private JDateChooser loanDateChooser;
    private JDateChooser returnDateChooser;

    // Tambahkan ini untuk tabel riwayat peminjaman
    private JTable historyTable;
    private DefaultTableModel historyTableModel;

    public Buku(LibControl controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setTitle("Aplikasi Perpustakaan");
        setSize(800, 700);
        // Define the green color
        Color green = Color.decode("#00CC66");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);        

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    updateBookTable();
                } else if (tabbedPane.getSelectedIndex() == 1) {
                    updateMemberTable();
                } else if (tabbedPane.getSelectedIndex() == 2) {
                    updateHistoryTable(); // Update history table when Loans tab is selected
                }
            }
        });

        // Book Management Tab
        JPanel bookPanel = new JPanel(new GridBagLayout());
        bookPanel.setBackground(green);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        bookIdField = new JTextField(10);
        bookTitleField = new JTextField(10);
        bookAuthorField = new JTextField(10);
        searchBookField = new JTextField(10);

        JButton addBookButton = new JButton("Tambah Buku");
        addBookButton.setPreferredSize(new Dimension(150, 40));
        JButton editBookButton = new JButton("Ubah Buku");
        JButton removeBookButton = new JButton("Hapus Buku");
        JButton searchBookButton = new JButton("Cari Buku");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        bookPanel.add(new JLabel("ID Buku:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bookPanel.add(bookIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        bookPanel.add(new JLabel("Judul:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bookPanel.add(bookTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        bookPanel.add(new JLabel("Pengarang:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bookPanel.add(bookAuthorField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        bookPanel.add(addBookButton, gbc);

        gbc.gridx = 2;
        bookPanel.add(editBookButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        bookPanel.add(removeBookButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        bookPanel.add(new JLabel("Cari:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bookPanel.add(searchBookField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        bookPanel.add(searchBookButton, gbc);

        bookTableModel = new DefaultTableModel(new String[]{"ID Buku", "Judul", "Pengarang", "Status"}, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane bookScrollPane = new JScrollPane(bookTable);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        bookPanel.add(bookScrollPane, gbc);

        tabbedPane.addTab("Buku", bookPanel);

        // Member Management Tab
        JPanel memberPanel = new JPanel(new GridBagLayout());
        memberPanel.setBackground(green);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        memberIdField = new JTextField(10);
        memberNameField = new JTextField(10);
        memberEmailField = new JTextField(10);
        memberPhoneField = new JTextField(10);
        searchMemberField = new JTextField(10);

        JButton addMemberButton = new JButton("Tambah Member");
        JButton editMemberButton = new JButton("Ubah Member");
        JButton removeMemberButton = new JButton("Hapus Member");
        JButton searchMemberButton = new JButton("Cari Member");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        memberPanel.add(new JLabel("ID Member:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        memberPanel.add(memberIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        memberPanel.add(new JLabel("Nama:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        memberPanel.add(memberNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        memberPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        memberPanel.add(memberEmailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        memberPanel.add(new JLabel("Nomor HP:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        memberPanel.add(memberPhoneField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        memberPanel.add(addMemberButton, gbc);

        gbc.gridx = 2;
        memberPanel.add(editMemberButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        memberPanel.add(removeMemberButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        memberPanel.add(new JLabel("Cari:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        memberPanel.add(searchMemberField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        memberPanel.add(searchMemberButton, gbc);

        memberTableModel = new DefaultTableModel(new String[]{"Member ID", "Nama", "Email", "Nomor HP"}, 0);
        memberTable = new JTable(memberTableModel);
        JScrollPane memberScrollPane = new JScrollPane(memberTable);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        memberPanel.add(memberScrollPane, gbc);

        tabbedPane.addTab("Member", memberPanel);

        // Loan Management Tab
        JPanel loanPanel = new JPanel(new GridBagLayout());
        loanPanel.setBackground(green);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        loanIdField = new JTextField(10);
        loanMemberIdField = new JTextField(10);
        returnLoanIdField = new JTextField(10);
        actualReturnDateField = new JDateChooser();
        loanBookIdComboBox = new JComboBox<>();

        // Initializing new components
        bookInfoLabel = new JLabel();
        loanDateChooser = new JDateChooser();
        returnDateChooser = new JDateChooser();

        updateLoanBookIdComboBox();

        JButton loanBookButton = new JButton("Pinjam Buku");
        JButton returnBookButton = new JButton("Kembalikan Buku");
        JButton printReceiptButton = new JButton("Cetak Tanda Terima");
        JButton calculateFineButton = new JButton("Hitung Denda");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("ID Pinjaman:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(loanIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("ID Buku:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(loanBookIdComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("Info Buku:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(bookInfoLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("Tanggal Pinjaman:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(loanDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("Tanggal Kembali:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(returnDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("ID Member:"), gbc); // Add member ID label

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(loanMemberIdField, gbc); // Add member ID field

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        loanPanel.add(loanBookButton, gbc);

        gbc.gridx = 2;
        loanPanel.add(returnBookButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("ID Pengembalian Pinjaman:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(returnLoanIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        loanPanel.add(new JLabel("Tanggal Pengembalian Sebenarnya:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        loanPanel.add(actualReturnDateField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        loanPanel.add(printReceiptButton, gbc);

        gbc.gridx = 2;
        loanPanel.add(calculateFineButton, gbc);

        // Inisialisasi history table
        historyTableModel = new DefaultTableModel(new String[]{"ID Pinjaman", "ID Buku", "Judul Buku", "ID Member", "Nama Member", "Tanggal Pinjaman", "Tanggal Pengembalian", "Tanggal Pengembalian Sebenarnya", "Denda"}, 0);
        historyTable = new JTable(historyTableModel);
        JScrollPane historyScrollPane = new JScrollPane(historyTable);

        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        loanPanel.add(historyScrollPane, gbc);

        tabbedPane.addTab("Pinjam", loanPanel);
        // Create the "Delete History" button and set its preferred size
        JButton deleteHistoryButton = new JButton("Hapus Riwayat");
        deleteHistoryButton.setPreferredSize(new Dimension(120, 25)); // Adjusted size (width, height)

        // Set the layout constraints for the "Delete History" button
        gbc.gridx = 2;
        gbc.gridy = 10; // Ensure this is the correct row
        gbc.gridwidth = 1; // Ensure it doesn't span multiple columns
        gbc.anchor = GridBagConstraints.NORTHWEST; // Anchor to top-left for better alignment
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding around the button
        loanPanel.add(deleteHistoryButton, gbc);
        add(tabbedPane);

        // Action Listeners
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = bookIdField.getText();
                String title = bookTitleField.getText();
                String author = bookAuthorField.getText();
                controller.addBook(new Book(id, title, author, true));
                updateBookTable();
                updateLoanBookIdComboBox();
            }
        });

        editBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = bookIdField.getText();
                String title = bookTitleField.getText();
                String author = bookAuthorField.getText();
                controller.editBook(id, title, author);
                updateBookTable();
                updateLoanBookIdComboBox();
                JOptionPane.showMessageDialog(null, "Buku Berhasil Diubah!");
            }
        });

        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = bookIdField.getText();
                if (id == null || id.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "ID Buku Tidak Boleh Kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controller.removeBook(id);
                updateBookTable();
                updateLoanBookIdComboBox();
                JOptionPane.showMessageDialog(null, "Buku Berhasil Dihapus!");
            }
        });

        searchBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchBookField.getText();
                List<Book> books = controller.searchBooks(keyword);
                updateBookTable(books);
                if (books.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Buku Tidak Ditemukan");
                }
            }
        });

        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = memberIdField.getText();
                String name = memberNameField.getText();
                String email = memberEmailField.getText();
                String phone = memberPhoneField.getText();
                controller.addMember(new Member(id, name, email, phone));
                updateMemberTable();
            }
        });

        editMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = memberIdField.getText();
                String name = memberNameField.getText();
                String email = memberEmailField.getText();
                String phone = memberPhoneField.getText();
                controller.editMember(id, name, email, phone);
                updateMemberTable();
                JOptionPane.showMessageDialog(null, "Member Berhasil Diubah!");
            }
        });

        removeMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = memberIdField.getText();
                controller.removeMember(id);
                updateMemberTable();
                JOptionPane.showMessageDialog(null, "Member Berhasil Dihapus!");
            }
        });

        searchMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchMemberField.getText();
                List<Member> members = controller.searchMembers(keyword);
                updateMemberTable(members);
                if (members.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Member Tidak Ditemukan");
                }
            }
        });

        loanBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loanId = loanIdField.getText();
                String bookId = (String) loanBookIdComboBox.getSelectedItem();
                String memberId = loanMemberIdField.getText(); // Ensure member ID is fetched
                Date loanDate = loanDateChooser.getDate();
                Date returnDate = returnDateChooser.getDate();

                Book book = controller.searchBook(bookId);
                Member member = controller.searchMember(memberId);

                if (book != null && !book.isAvailable()) {
                    JOptionPane.showMessageDialog(null, "Saat Ini Buku Sedang Dipinjam dan Tidak Bisa Dipinjam.");
                    return;
                }

                if (book != null) {
                    System.out.println("Buku Ditemukan: " + book.getTitle());
                } else {
                    System.out.println("Buku Tidak Ditemukan: " + bookId);
                }

                if (member != null) {
                    System.out.println("Member Ditemukan: " + member.getName());
                } else {
                    System.out.println("Member Tidak Ditemukan: " + memberId);
                }

                if (book != null && member != null) {
                    controller.loanBook(loanId, book, member, loanDate, returnDate);
                    JOptionPane.showMessageDialog(null, "Buku Berhasil Dipinjam!");
                    updateLoanBookIdComboBox();
                    updateBookTable(); // Refresh the book table
                    updateHistoryTable(); // Refresh the history table
                } else {
                    JOptionPane.showMessageDialog(null, "Buku atau Member Tidak Ditemukan!");
                }
            }
        });

        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loanId = returnLoanIdField.getText();
                Date actualReturnDate = actualReturnDateField.getDate();

                Loan loan = controller.searchLoan(loanId);
                if (loan == null) {
                    JOptionPane.showMessageDialog(null, "Pinjaman Tidak Ditemukan!");
                    return;
                }

                controller.returnBook(loanId, actualReturnDate);

                double fine = controller.calculateFine(loan.getReturnDate(), actualReturnDate);
                if (fine > 0) {
                    JOptionPane.showMessageDialog(null, "Buku Dikembalikan dengan Denda: " + fine);
                } else {
                    JOptionPane.showMessageDialog(null, "Buku Dikembalikan dengan Tepat Waktu!");
                }

                controller.printReturnReceipt(loan, actualReturnDate, fine);

                updateLoanBookIdComboBox();
                updateBookTable(); // Refresh the book table
                updateHistoryTable(); // Refresh the history table
            }
        });
        printReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loanId = returnLoanIdField.getText();
                for (Loan loan : controller.getLoans()) {
                    if (loan.getId().equals(loanId)) {
                        controller.printLoanReceipt(loan);
                        break;
                    }
                }
            }
        });

        calculateFineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loanId = returnLoanIdField.getText();
                Date actualReturnDate = actualReturnDateField.getDate();
                try {
                    for (Loan loan : controller.getLoans()) {
                        if (loan.getId().equals(loanId)) {
                            double fine = controller.calculateFine(loan.getReturnDate(), actualReturnDate);
                            JOptionPane.showMessageDialog(null, "Denda: " + fine);
                            break;
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Format Tanggal Tidak Valid. Gunakan Tahun-Bulan-Tanggal.");
                }
            }
        });

        loanBookIdComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookId = (String) loanBookIdComboBox.getSelectedItem();
                Book book = controller.searchBook(bookId);
                if (book != null) {
                    bookInfoLabel.setText(book.getTitle() + " oleh " + book.getAuthor());
                } else {
                    bookInfoLabel.setText("Buku Tidak Temukan");
                }
            }
        });

        bookTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) {
                    bookIdField.setText(bookTableModel.getValueAt(selectedRow, 0).toString());
                    bookTitleField.setText(bookTableModel.getValueAt(selectedRow, 1).toString());
                    bookAuthorField.setText(bookTableModel.getValueAt(selectedRow, 2).toString());
                }
            }
        });
        deleteHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = historyTable.getSelectedRow();
                if (selectedRow != -1) {
                    String loanId = historyTableModel.getValueAt(selectedRow, 0).toString();
                    int confirm = JOptionPane.showConfirmDialog(null, "Apa Anda Yakin Ingin Menghapus Riwayat?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        controller.deleteLoanHistory(loanId);
                        updateHistoryTable();
                        JOptionPane.showMessageDialog(null, "Riwayat Berhasil Dihapus!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Silahkan Pilih Riwayat yang Ingin Dihapus.");
                }
            }
        });
        memberTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = memberTable.getSelectedRow();
                if (selectedRow != -1) {
                    memberIdField.setText(memberTableModel.getValueAt(selectedRow, 0).toString());
                    memberNameField.setText(memberTableModel.getValueAt(selectedRow, 1).toString());
                    memberEmailField.setText(memberTableModel.getValueAt(selectedRow, 2).toString());
                    memberPhoneField.setText(memberTableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
        updateBookTable();
        updateMemberTable();
    }

    private void updateBookTable() {
        List<Book> books = controller.getBooks();
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.isAvailable() ? "Tersedia" : "Dipesan"});
        }
    }

    private void updateBookTable(List<Book> books) {
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.isAvailable() ? "Tersedia" : "Dipesan"});
        }
    }

    private void updateMemberTable() {
        List<Member> members = controller.getMembers();
        memberTableModel.setRowCount(0);
        for (Member member : members) {
            memberTableModel.addRow(new Object[]{member.getId(), member.getName(), member.getEmail(), member.getPhoneNumber()});
        }
    }

    private void updateMemberTable(List<Member> members) {
        memberTableModel.setRowCount(0);
        for (Member member : members) {
            memberTableModel.addRow(new Object[]{member.getId(), member.getName(), member.getEmail(), member.getPhoneNumber()});
        }
    }

    private void updateLoanBookIdComboBox() {
        List<Book> books = controller.getBooks();
        loanBookIdComboBox.removeAllItems();
        for (Book book : books) {
            loanBookIdComboBox.addItem(book.getId());
        }
    }

    private void updateHistoryTable() {
        List<Loan> loans = controller.getLoans();
        historyTableModel.setRowCount(0);
        for (Loan loan : loans) {
            historyTableModel.addRow(new Object[]{
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getMember().getId(),
                loan.getMember().getName(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getActualReturnDate() != null ? loan.getActualReturnDate() : "",
                loan.getFine()
            });
        }
    }
}