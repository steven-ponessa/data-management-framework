package com.ibm.wfm.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.JrsDim;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.SourcingStrategyDim;
import com.ibm.wfm.beans.sdss.CountryDim;
import com.ibm.wfm.beans.sdss.SdssExceptionDim;

/**
 * Provides direct access to reference data required by the SDSS validation
 * framework without invoking the application's REST APIs.
 * <p>
 * This service encapsulates retrieval of commonly used reference datasets,
 * including:
 * <ul>
 *   <li>The JRS taxonomy hierarchy.</li>
 *   <li>Sourcing Strategy reference data.</li>
 * </ul>
 * <p>
 * Unlike the original implementation, which performed HTTP requests against
 * locally hosted REST endpoints using {@code RestTemplate}, this service
 * invokes the underlying DAO services directly. This eliminates the overhead
 * of HTTP communication while reusing the existing data access and taxonomy
 * construction logic implemented by the DAO layer.
 * <p>
 * The current implementation delegates to the existing
 * {@code AbstractDaoService.find(...)} methods and extracts the response body
 * from the returned {@code ResponseEntity}. This approach minimizes changes to
 * the existing framework while providing a service-oriented abstraction for
 * SDSS reference data retrieval.
 *
 * @author Steven J. Ponessa
 * @since 1.0
 */
@Service
public class SdssReferenceDataService {

    private final EdsDaoService edsDaoService;
    private final SourcingStrategyDaoService sourcingStrategyDaoService;
    
    /**
     * Constructs a new {@code SdssReferenceDataService}.
     *
     * @param edsDaoService
     *            DAO service used to retrieve the JRS taxonomy hierarchy.
     * @param sourcingStrategyDaoService
     *            DAO service used to retrieve Sourcing Strategy reference data.
     */
    public SdssReferenceDataService(
            EdsDaoService edsDaoService,
            SourcingStrategyDaoService sourcingStrategyDaoService) {

        this.edsDaoService = edsDaoService;
        this.sourcingStrategyDaoService = sourcingStrategyDaoService;
    }
    
    /**
     * Retrieves the complete SDSS geography taxonomy hierarchy.
     * <p>
     * The taxonomy is retrieved with {@code includeParentage=true}, causing
     * the DAO service to construct the complete hierarchical tree rooted at
     * the Geography Type level. The returned {@link GeographyType} represents the root node
     * of the taxonomy and provides access to all descendant Geography,
     * Market, Region, and Country leaf nodes.
     *
     * @return
     *         the root {@link GeographyType} of the complete JRS taxonomy.
     *
     * @throws IllegalStateException
     *         if the taxonomy cannot be retrieved or no Brand node is
     *         returned.
     */
	public List<NaryTreeNode> retrieveSdssGeoTaxonomy() {
		List<NaryTreeNode> geographyTypes;
		try {
			geographyTypes = (List<NaryTreeNode>) edsDaoService.find(
				    CountryDim.class    // Class               type
				  , null            // Map<String, Object> pathVarMap
				  , ""              // String              filters
				  , ""              // String              orderByCols
				  , true            // boolean             includeParentage
				  , false           // boolean             selfReferencing
				  , "Top"           // String              topNodeNm
				  , false           // boolean             returnCsv
				  , false           // boolean             includePit
				  , ""              // String              pit
				  , false           // boolean             edsOnly
				  , -1              // int                 offset
				  , "All"           // String              resultSetMaxSize
				  , null            // String              pathName
				  , null            // Integer             orgMask
				  , null            // HttpServletRequest  request
			).getBody();
		} 
		catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			geographyTypes=null;
		}
	
	    if (geographyTypes == null || geographyTypes.isEmpty()) {
	        throw new IllegalStateException(
	            "The SDSS Geography taxonomy did not return a list of geography types."
	        );
	    }
	
	    return geographyTypes.get(0).getChildren();
	}    

    /**
     * Retrieves the complete JRS taxonomy hierarchy.
     * <p>
     * The taxonomy is retrieved with {@code includeParentage=true}, causing
     * the DAO service to construct the complete hierarchical tree rooted at
     * the Brand level. The returned {@link BrandDim} represents the root node
     * of the taxonomy and provides access to all descendant Growth Platforms,
     * Service Lines, Practices, Service Areas, and JRS leaf nodes.
     *
     * @return
     *         the root {@link BrandDim} of the complete JRS taxonomy.
     *
     * @throws IllegalStateException
     *         if the taxonomy cannot be retrieved or no Brand node is
     *         returned.
     */
    public BrandDim retrieveJrsTaxonomy() {
    	List<BrandDim> brands;
    	try {
			brands = (List<BrandDim>) edsDaoService.find(
				    JrsDim.class    // Class               type
				  , null            // Map<String, Object> pathVarMap
				  , ""              // String              filters
				  , ""              // String              orderByCols
				  , true            // boolean             includeParentage
				  , false           // boolean             selfReferencing
				  , null            // String              topNodeNm
				  , false           // boolean             returnCsv
				  , false           // boolean             includePit
				  , ""              // String              pit
				  , false           // boolean             edsOnly
				  , -1              // int                 offset
				  , "All"           // String              resultSetMaxSize
				  , null            // String              pathName
				  , null            // Integer             orgMask
				  , null            // HttpServletRequest  request
			).getBody();
		} 
    	catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			brands=null;
		}

        if (brands == null || brands.isEmpty()) {
            throw new IllegalStateException(
                "The JRS taxonomy did not return a Brand."
            );
        }

        return brands.get(0);
    }

    /**
     * Retrieves all Sourcing Strategy reference data.
     * <p>
     * The returned collection contains every Sourcing Strategy record available
     * from the reference data source. Unlike the JRS taxonomy, these records
     * are returned as a flat list and do not require hierarchical
     * parent-child construction.
     *
     * @return
     *         a list containing all {@link SourcingStrategyDim} reference
     *         records, or {@code null} if the retrieval fails.
     */
    public List<SourcingStrategyDim> retrieveSourcingStrategies() {
        List<SourcingStrategyDim> sourcingStrategies;
		try {
			sourcingStrategies = (List<SourcingStrategyDim>) sourcingStrategyDaoService.find(
				  SourcingStrategyDim.class // Class               type
				  , null            // Map<String, Object> pathVarMap
				  , ""              // String              filters
				  , ""              // String              orderByCols
				  , false           // boolean             includeParentage
				  , false           // boolean             selfReferencing
				  , null            // String              topNodeNm
				  , false           // boolean             returnCsv
				  , false           // boolean             includePit
				  , ""              // String              pit
				  , false           // boolean             edsOnly
				  , -1              // int                 offset
				  , "All"           // String              resultSetMaxSize
				  , null            // String              pathName
				  , null            // Integer             orgMask
				  , null            // HttpServletRequest  request
			).getBody();;
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sourcingStrategies = null;
		}
		
		if (sourcingStrategies == null) {
		    throw new IllegalStateException(
		        "Unable to retrieve Sourcing Strategy reference data."
		    );
		}

        return sourcingStrategies ;
    }
    
    /**
     * Retrieves all SDSS Exception Codes reference data.
     * <p>
     * The returned collection contains every SDSS Exception record available
     * from the reference data source. Unlike the JRS taxonomy, these records
     * are returned as a flat list and do not require hierarchical
     * parent-child construction.
     *
     * @return
     *         a list containing all {@link SdssExceptionDim} reference
     *         records, or {@code null} if the retrieval fails.
     */
    public List<SdssExceptionDim> retrieveSdssExceptions() {
        List<SdssExceptionDim> sdssExceptions;
		try {
			sdssExceptions = (List<SdssExceptionDim>) sourcingStrategyDaoService.find(
					SdssExceptionDim.class // Class               type
				  , null            // Map<String, Object> pathVarMap
				  , ""              // String              filters
				  , ""              // String              orderByCols
				  , false           // boolean             includeParentage
				  , false           // boolean             selfReferencing
				  , null            // String              topNodeNm
				  , false           // boolean             returnCsv
				  , false           // boolean             includePit
				  , ""              // String              pit
				  , false           // boolean             edsOnly
				  , -1              // int                 offset
				  , "All"           // String              resultSetMaxSize
				  , null            // String              pathName
				  , null            // Integer             orgMask
				  , null            // HttpServletRequest  request
			).getBody();;
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sdssExceptions = null;
		}
		
		if (sdssExceptions == null) {
		    throw new IllegalStateException(
		        "Unable to retrieve Sourcing Strategy reference data."
		    );
		}

        return sdssExceptions ;
    }    

}