package com.example.keor.businesscardscanner.Model;

/**
 * Created by keor on 27-08-2015.
 */
public class BEBusinessCard {

    private int m_id;
    private String m_firstname;
    private String m_lastname;
    private String m_address;
    private String m_phonenumber;
    private String m_country;
    private String m_city;
    private String m_company;
    private String m_title;
    private String m_homepage;
    private String m_postal;
    private String m_fax;
    private String m_email;
    private String m_other;
    private String m_encodedImage;
    private String m_createdDate;
    private int m_createdUserId;
    private boolean m_isDeleted;

    public BEBusinessCard(){

    }
    public BEBusinessCard(int id, String firstname, String lastname, String address, String phonenumber, String country, String city, String company,
                          String title, String homepage, String postal, String fax, String email, String other, String encodedImage, String createdDate,
                          int createdUserId, boolean isDeleted) {
        m_id = id;
        m_firstname = firstname;
        m_lastname = lastname;
        m_address = address;
        m_phonenumber = phonenumber;
        m_country = country;
        m_city = city;
        m_company = company;
        m_title = title;
        m_homepage = homepage;
        m_postal = postal;
        m_fax = fax;
        m_email = email;
        m_other = other;

        m_encodedImage = encodedImage;
        m_createdDate = createdDate;
        m_createdUserId = createdUserId;
        m_isDeleted = isDeleted;
    }

    public BEBusinessCard(String firstname, String lastname, String address, String phonenumber, String country, String city, String company,
                          String title, String homepage, String postal, String fax, String email, String other, String encodedImage, String createdDate,
                          int createdUserId, boolean isDeleted) {
        m_firstname = firstname;
        m_lastname = lastname;
        m_address = address;
        m_phonenumber = phonenumber;
        m_country = country;
        m_city = city;
        m_company = company;
        m_title = title;
        m_homepage = homepage;
        m_postal = postal;
        m_fax = fax;
        m_email = email;
        m_other = other;
        m_encodedImage = encodedImage;
        m_createdDate = createdDate;
        m_createdUserId = createdUserId;
        m_isDeleted = isDeleted;
    }

    public int getId() {
        return m_id;
    }

    public String getFirstname() {
        return m_firstname;
    }

    public void setFirstname(String m_firstname) {
        this.m_firstname = m_firstname;
    }

    public String getLastname() {
        return m_lastname;
    }

    public void setLastname(String m_lastname) {
        this.m_lastname = m_lastname;
    }

    public String getAddress() {
        return m_address;
    }

    public void setAddress(String m_address) {
        this.m_address = m_address;
    }

    public String getPhonenumber() {
        return m_phonenumber;
    }

    public void setPhonenumber(String m_phonenumber) {
        this.m_phonenumber = m_phonenumber;
    }

    public String getCountry() {
        return m_country;
    }

    public void setCountry(String m_country) {
        this.m_country = m_country;
    }

    public String getCity() {
        return m_city;
    }

    public void setCity(String m_city) {
        this.m_city = m_city;
    }

    public String getCompany() {
        return m_company;
    }

    public void setCompany(String m_company) {
        this.m_company = m_company;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String m_title) {
        this.m_title = m_title;
    }

    public String getHomepage() {
        return m_homepage;
    }

    public void setHomepage(String m_homepage) {
        this.m_homepage = m_homepage;
    }

    public String getPostal() {
        return m_postal;
    }

    public void setPostal(String m_postal) {
        this.m_postal = m_postal;
    }

    public String getFax() {
        return m_fax;
    }

    public void setFax(String m_fax) {
        this.m_fax = m_fax;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String m_email) {
        this.m_email = m_email;
    }

    public String getOther() {
        return m_other;
    }

    public void setOther(String m_other) {
        this.m_other = m_other;
    }

    public String getEncodedImage() {
        return m_encodedImage;
    }

    public void setEncodedImage(String m_encodedImage) {
        this.m_encodedImage = m_encodedImage;
    }

    public String getCreatedDate() {
        return m_createdDate;
    }

    public void setCreatedDate(String m_createdDate) {
        this.m_createdDate = m_createdDate;
    }

    public int getCreatedUserId() {
        return m_createdUserId;
    }

    public void setCreatedUserId(int m_createdUserId) {
        this.m_createdUserId = m_createdUserId;
    }

    public boolean getIsDeleted() {
        return m_isDeleted;
    }

    public void setIsDeleted(boolean m_isDeleted) {
        this.m_isDeleted = m_isDeleted;
    }
    public String getFullname(){
        return getFirstname() + " " + getLastname();
    }

}
