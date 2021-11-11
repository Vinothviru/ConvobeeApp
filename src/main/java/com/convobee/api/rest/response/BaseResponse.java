package com.convobee.api.rest.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.convobee.constants.Constants;
import com.convobee.utils.ParseConstantsUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BaseResponse<T> {

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("status_message")
    private String statusMessage;

    private T data;

    public ResponseEntity<BaseResponse<T>> getResponse(IBaseResponse<T> br)
    {
        //TODO add try catch and handle api exeptions
        try {
            this.data=  br.execute();
            this.statusCode = HttpStatus.OK.value();
            this.statusMessage = "Success";
            ResponseEntity<BaseResponse<T>> re = new ResponseEntity<BaseResponse<T>>(this, HttpStatus.OK);
            return re;
        }
        catch (Exception ex){

            this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.statusMessage =  ex.getLocalizedMessage() ;
            ResponseEntity<BaseResponse<T>> re = new ResponseEntity<BaseResponse<T>>(  this, HttpStatus.INTERNAL_SERVER_ERROR);
            return re;
        }

    }
}
