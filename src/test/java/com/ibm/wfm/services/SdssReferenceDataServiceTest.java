package com.ibm.wfm.services;

//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;

//import java.util.List;

import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;

//import com.ibm.wfm.beans.BrandDim;
//import com.ibm.wfm.beans.SourcingStrategyDim;

//@SpringBootTest
class SdssReferenceDataServiceTest {
	
    @Test
    void validTest() {
        // Passes immediately.
    }

	/*
    @Autowired
    private SdssReferenceDataService service;

    @Test
    void retrieveJrsTaxonomyReturnsBrand() {
        BrandDim brand = service.retrieveJrsTaxonomy();

        assertNotNull(
            brand,
            "The JRS taxonomy should return a Brand"
        );
    }


    @Test
    void retrieveSourcingStrategiesReturnsResults() {
        List<SourcingStrategyDim> results =
            service.retrieveSourcingStrategies();

        assertNotNull(
            results,
            "The sourcing-strategy result should not be null"
        );

        assertFalse(
            results.isEmpty(),
            "The sourcing-strategy result should not be empty"
        );
    }
    */

}