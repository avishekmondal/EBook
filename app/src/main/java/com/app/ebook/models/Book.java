package com.app.ebook.models;

public class Book {
    private String eduClass;
    private String board;
    private String bookType;
    private String book;
    private String writer;
    private int price;
    private String readingType;
    private String photoUrl;

    public Book(String eduClass, String board, String bookType, String book, String writer, int price, String readingType, String photoUrl) {
        this.eduClass = eduClass;
        this.board = board;
        this.bookType = bookType;
        this.book = book;
        this.writer = writer;
        this.price = price;
        this.readingType = readingType;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEduClass() {
        return eduClass;
    }

    public void setEduClass(String eduClass) {
        this.eduClass = eduClass;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(String readingType) {
        this.readingType = readingType;
    }
}
