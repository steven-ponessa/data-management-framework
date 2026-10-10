package com.ibm.wfm.beans.sdss;

import java.time.LocalDateTime;

public record SdssExtractMetadata(
    String dataSource,
    String name,
    String title,
    String extractedAt,
    int totalRecords,
    String filterCondition,
    String baseUrl,
    String filterParam
) {
	public void show() {
		System.out.println("SdssExtractMetadata");
		System.out.println("dataSource     : "+ dataSource);
		System.out.println("name           : "+ name);
		System.out.println("title          : "+ title);
		System.out.println("extractedAt    : "+ extractedAt);
		System.out.println("totalRecords   : "+ totalRecords);
		System.out.println("filterCondition: "+ filterCondition);
		System.out.println("baseUrl        : "+ baseUrl);
		System.out.println("filterParam    : "+ filterParam);
	}
}