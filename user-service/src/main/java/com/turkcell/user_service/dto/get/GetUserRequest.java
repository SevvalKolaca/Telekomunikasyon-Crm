package com.turkcell.user_service.dto.get;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserRequest {
    private UUID id;

    //public GetUserRequest(UUID id) {
    //    this.id = id;
    //}

    public UUID getId() {
        return id;
    }  

    public void setId(UUID id) {
        this.id = id;
    }   
}
