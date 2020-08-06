package ru.job4j.lsp.food;

import java.time.LocalDateTime;

/**
 * класс - модель данных
 */
public class Food {
    private String name;
    private LocalDateTime expiredDate;
    private LocalDateTime createDate;
    private double price;
    private double discount;

    public Food(String name, LocalDateTime createDate,
                LocalDateTime expiredDate, double price, double discount) {
        this.name = name;
        this.expiredDate = expiredDate;
        this.createDate = createDate;
        this.price = price;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean addDiscount(double discount) {
        if (this.discount + discount < 1) {
            this.discount += discount;
            return true;
        } else {
            System.out.println("Discount can't be more then 100%");
            return false;
        }
    }
}
