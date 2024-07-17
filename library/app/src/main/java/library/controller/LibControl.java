package library.controller;

import library.model.Loan;
import library.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import library.model.Book;

public class LibControl {
    private final Connection connection;

    public LibControl() {
        connection = DbKoneksi.getConnection();
    }

    // Manajemen Buku
    public void addBook(Book book) {
        if (isBookIdExists(book.getId())) {
            JOptionPane.showMessageDialog(null, "Book ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO books (id, title, author, isAvailable) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setBoolean(4, book.isAvailable());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editBook(String id, String newTitle, String newAuthor) {
        String query = "UPDATE books SET title = ?, author = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newAuthor);
            stmt.setString(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBook(String id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book searchBook(String bookId) {
        String query = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(rs.getString("id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("isAvailable"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE id LIKE ? OR title LIKE ? OR author LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(rs.getString("id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("isAvailable")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                books.add(new Book(rs.getString("id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("isAvailable")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Manajemen Anggota
    public void addMember(Member member) {
        if (isMemberIdExists(member.getId())) {
            JOptionPane.showMessageDialog(null, "Member ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO members (id, name, email, phoneNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, member.getId());
            stmt.setString(2, member.getName());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getPhoneNumber());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Member added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editMember(String id, String newName, String newEmail, String newPhoneNumber) {
        String query = "UPDATE members SET name = ?, email = ?, phoneNumber = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setString(3, newPhoneNumber);
            stmt.setString(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMember(String id) {
        String query = "DELETE FROM members WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Member searchMember(String memberId) {
        String query = "SELECT * FROM members WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Member(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("phoneNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Member> searchMembers(String keyword) {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members WHERE id LIKE ? OR name LIKE ? OR email LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(new Member(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("phoneNumber")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public List<Member> getMembers() {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                members.add(new Member(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("phoneNumber")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // Transaksi Peminjaman & Pengembalian
    public void loanBook(String loanId, Book book, Member member, Date loanDate, Date returnDate) {
        if (isLoanIdExists(loanId)) {
            loanId = generateNewLoanId();
        }

        String query = "INSERT INTO loans (id, book_id, member_id, loan_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, loanId);
            stmt.setString(2, book.getId());
            stmt.setString(3, member.getId());
            stmt.setDate(4, new java.sql.Date(loanDate.getTime()));
            stmt.setDate(5, new java.sql.Date(returnDate.getTime()));
            stmt.executeUpdate();
            updateBookAvailability(book.getId(), false); // Set status to reserved
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(String loanId, Date actualReturnDate) {
    String query = "UPDATE loans SET actual_return_date = ?, fine = ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        Loan loan = searchLoan(loanId);
        if (loan != null) {
            double fine = calculateFine(loan.getReturnDate(), actualReturnDate);
            stmt.setDate(1, new java.sql.Date(actualReturnDate.getTime()));
            stmt.setDouble(2, fine);
            stmt.setString(3, loanId);
            stmt.executeUpdate();
            updateBookAvailability(loan.getBook().getId(), true); // Set status to available
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    
    private void updateLoanHistory(Loan loan, Date actualReturnDate, double fine) {
    String query = "INSERT INTO loan_history (loan_id, book_id, member_id, loan_date, return_date, actual_return_date, fine) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, loan.getId());
        stmt.setString(2, loan.getBook().getId());
        stmt.setString(3, loan.getMember().getId());
        stmt.setDate(4, new java.sql.Date(loan.getLoanDate().getTime()));
        stmt.setDate(5, new java.sql.Date(loan.getReturnDate().getTime()));
        stmt.setDate(6, new java.sql.Date(actualReturnDate.getTime()));
        stmt.setDouble(7, fine);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void updateBookAvailability(String bookId, boolean isAvailable) {
        String query = "UPDATE books SET isAvailable = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setString(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getBookIdFromLoan(String loanId) {
        String query = "SELECT book_id FROM loans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("book_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printLoanReceipt(Loan loan) {
        String receipt = "Loan ID: " + loan.getId() +
                         "\nBook: " + loan.getBook().getTitle() +
                         "\nMember: " + loan.getMember().getName() +
                         "\nLoan Date: " + loan.getLoanDate() +
                         "\nReturn Date: " + loan.getReturnDate();
        JOptionPane.showMessageDialog(null, receipt, "Loan Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    public double calculateFine(Date returnDate, Date actualReturnDate) {
        long diff = actualReturnDate.getTime() - returnDate.getTime();
        long daysLate = diff / (1000 * 60 * 60 * 24);
        if (daysLate > 0) {
            double finePerDay = 5000; // Contoh denda per hari
            return daysLate * finePerDay;
        }
        return 0;
    }

    public List<Loan> getLoans() {
    List<Loan> loans = new ArrayList<>();
    String query = "SELECT * FROM loans";
    try (Statement stmt = connection.createStatement()) {
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Book book = searchBook(rs.getString("book_id"));
            Member member = searchMember(rs.getString("member_id"));
            Loan loan = new Loan(
                rs.getString("id"), 
                book, 
                member, 
                rs.getDate("loan_date"), 
                rs.getDate("return_date")
            );
            loan.setActualReturnDate(rs.getDate("actual_return_date"));
            loan.setFine(rs.getDouble("fine"));
            loans.add(loan);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return loans;
}

    // Fungsi tambahan untuk mengecek apakah ID Buku sudah ada
    private boolean isBookIdExists(String bookId) {
        String query = "SELECT id FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Mengembalikan true jika ID ditemukan
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fungsi tambahan untuk mengecek apakah ID Anggota sudah ada
    private boolean isMemberIdExists(String memberId) {
        String query = "SELECT id FROM members WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Mengembalikan true jika ID ditemukan
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metode untuk mencari peminjaman
    public Loan searchLoan(String loanId) {
        String query = "SELECT * FROM loans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Book book = searchBook(rs.getString("book_id"));
                Member member = searchMember(rs.getString("member_id"));
                Loan loan = new Loan(
                    rs.getString("id"), 
                    book, 
                    member, 
                    rs.getDate("loan_date"), 
                    rs.getDate("return_date")
                );
                loan.setActualReturnDate(rs.getDate("actual_return_date"));
                loan.setFine(rs.getDouble("fine"));
                return loan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metode untuk menghasilkan Loan ID baru
    private String generateNewLoanId() {
        String newLoanId = "L";
        String query = "SELECT MAX(id) FROM loans";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String maxId = rs.getString(1);
                int newId = Integer.parseInt(maxId.substring(1)) + 1;
                newLoanId += String.format("%03d", newId);
            } else {
                newLoanId += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            newLoanId += "001"; // Default Loan ID jika ada error
        }
        return newLoanId;
    }

    // Metode untuk memeriksa apakah Loan ID sudah ada
    private boolean isLoanIdExists(String loanId) {
        String query = "SELECT id FROM loans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, loanId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void printReturnReceipt(Loan loan, Date actualReturnDate, double fine) {
    String receipt = "Return Receipt\n" +
                     "Loan ID: " + loan.getId() + "\n" +
                     "Book: " + loan.getBook().getTitle() + "\n" +
                     "Member: " + loan.getMember().getName() + "\n" +
                     "Loan Date: " + loan.getLoanDate() + "\n" +
                     "Return Date: " + loan.getReturnDate() + "\n" +
                     "Actual Return Date: " + actualReturnDate + "\n" +
                     "Fine: " + fine;
    JOptionPane.showMessageDialog(null, receipt, "Return Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    public void deleteLoanHistory(String loanId) {
    String query = "DELETE FROM loans WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, loanId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}