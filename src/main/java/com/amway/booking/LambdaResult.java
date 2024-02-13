package com.amway.booking;

import lombok.Data;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@ToString
public class LambdaResult<T> {

	private Integer resultCode;

	private String errorMessage;

	private List<T> result;


	public LambdaResult(Integer resultCode) {
		this.resultCode = resultCode;
	}

}
