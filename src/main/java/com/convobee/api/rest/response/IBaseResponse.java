package com.convobee.api.rest.response;

public interface IBaseResponse<T>   {
	T execute() throws Exception ;
}
