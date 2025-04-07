package model;


import jakarta.persistence.*;

@Entity
@Table(name = "itemdetails")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brandName", nullable = false)
    private String brandName;

//    @Column(name = "mobile_number", nullable = false, unique = true)
//    private String mobileNumber;

    @Column(name = "price", nullable = false)
    private double price;

//    @Column(name = "department", nullable = false)
//    private String department;

    // Default constructor
    public Item() {}

    // Parameterized constructor
    public Item(String category, String name, String brandName,  double price) {
        this.category = category;
        this.name = name;
        this.brandName = brandName;
//        this.mobileNumber = mobileNumber;
        this.price = price;
//        this.department = department;
    }

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
//    public String getMobileNumber() { return mobileNumber; }
//    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
//    public String getDepartment() { return department; }
//    public void setDepartment(String department) { this.department = department; }
    
    
}