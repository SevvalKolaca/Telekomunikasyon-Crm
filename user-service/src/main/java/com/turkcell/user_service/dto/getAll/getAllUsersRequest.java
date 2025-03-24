package com.turkcell.user_service.dto.getAll;
//@NoArgsConstructor
//@AllArgsConstructor
public class getAllUsersRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    public getAllUsersRequest() {}

    public getAllUsersRequest(String firstname, String lastname, String email, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }   

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }   

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
  
    
}