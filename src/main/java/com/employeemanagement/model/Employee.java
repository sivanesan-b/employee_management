package com.employeemanagement.model;

public class Employee {
    private long id;
    private String name;
    private String phone;
    private final Address permanentAddress;
    private final Address currentAddress;

    public void setId(long id) {
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public Address getPermanentAddress() {
        return permanentAddress;
    }

    public Employee(){
        this.permanentAddress = new Address();
        this.currentAddress = new Address();
    }

    public Employee(String employeeName, String phoneNumber){
        this.name = employeeName;
        this.phone = phoneNumber;
        this.currentAddress = new Address();
        this.permanentAddress = new Address();
    }

    public Employee(Long id, String employeeName, String phoneNumber){
        this.id = id;
        this.name = employeeName;
        this.phone = phoneNumber;
        this.currentAddress = new Address();
        this.permanentAddress = new Address();
    }

//    @Override
//    public String toString() {
//        return "Employee " +
//                "id=" + id + "\n" +
//                "name=" + name + "\n" +
//                "phone=" + phone + "\n" +
//                permanentAddress + "\n" +
//                currentAddress;
//    }

    @Override
    public String toString() {
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("{");
        jsonString.append("\"id\":").append(id).append(",");
        jsonString.append("\"name\":\"").append(name).append("\",");
        jsonString.append("\"phone number\":\"").append(phone).append("\",");

        if (permanentAddress.getAddressType().equals(AddressTypes.BOTH_PERMANENT_AND_CURRENT)) {
            jsonString.append("\"both permanent and current address is\":").append(permanentAddress);
        } else {
            jsonString.append("\"permanent address is \":").append(permanentAddress.getAddressType() != null ? permanentAddress.toString() : "");
            if (currentAddress.getAddressType() != null) {
                jsonString.append(",\"current Address\":").append(currentAddress);
            }
        }
        jsonString.append("}");
        return jsonString.toString();
    }
}