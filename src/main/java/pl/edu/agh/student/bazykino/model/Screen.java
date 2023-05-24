package pl.edu.agh.student.bazykino.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Screen {
    private Long id;
    private int screenNumber;
    private int n_rows;
    private int n_columns;
    private int seats_total;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public int getN_rows() {
        return n_rows;
    }

    public void setN_rows(int n_rows) {
        this.n_rows = n_rows;
    }

    public int getN_columns() {
        return n_columns;
    }

    public void setN_columns(int n_columns) {
        this.n_columns = n_columns;
    }

    public int getSeats_total() {
        return seats_total;
    }

    public void setSeats_total(int seats_total) {
        this.seats_total = seats_total;
    }

    @Column(unique = true)
    public int getScreenNumber() {
        return screenNumber;
    }

    public void setScreenNumber(int screenNumber) {
        this.screenNumber = screenNumber;
    }
}
