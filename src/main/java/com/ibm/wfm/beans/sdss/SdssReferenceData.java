package com.ibm.wfm.beans.sdss;

import java.util.List;
import java.util.Set;

import com.ibm.wfm.beans.BrandDim;
import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.SourcingStrategyDim;
import com.ibm.wfm.services.SdssReferenceDataService;

/**
 * Container for the reference data used when validating SDSS staging records.
 * <p>
 * An {@code SdssReferenceData} instance provides the taxonomy and reference
 * structures required by {@link SdssValidationService} to validate the
 * attributes of an {@code SdssStagingDim} record. The reference data is
 * retrieved from the WFM-DMF service layer and represents the current
 * authoritative taxonomy data used by SDSS validation.
 * </p>
 *
 * <p>
 * The reference data currently includes:
 * </p>
 * <ul>
 *   <li>
 *     The JRS taxonomy, represented by a single {@code BrandDim} root node.
 *     The JRS hierarchy is validated by traversing the taxonomy from the
 *     IBM Consulting root through the requested taxonomy levels.
 *   </li>
 *   <li>
 *     The SDSS Geography taxonomy, represented as a collection of
 *     {@code NaryTreeNode} Geography Type roots. The taxonomy returned by
 *     WFM-DMF contains an artificial {@code "Top"} / {@code "Dummy Root"}
 *     node; its children represent the actual Geography Type roots used
 *     during SDSS geography validation.
 *   </li>
 *   <li>
 *     Sourcing Strategy reference data used to validate other SDSS staging
 *     attributes and relationships.
 *   </li>
 * </ul>
 *
 * <p>
 * Taxonomy nodes ultimately derive from {@code NaryTreeNode}, allowing the
 * validation service to use common taxonomy operations such as
 * {@code validateBranchByLevels()} regardless of the concrete node type.
 * The JRS and Geography taxonomies differ structurally: JRS has a single
 * logical root, while the Geography taxonomy has multiple Geography Type
 * roots beneath the artificial Top node.
 * </p>
 *
 * <p>
 * Reference data should represent the current state of the underlying WFM-DMF
 * data and is therefore intended to be populated when SDSS validation is
 * performed rather than treated as permanently cached application state.
 * </p>
 */
public class SdssReferenceData {
	
	private BrandDim brand;
	private List<NaryTreeNode> geographyTypes;
	private List<SourcingStrategyDim> sourcingStrategyDims;
	private List<SdssExceptionDim> sdssExceptions;
	
    public SdssReferenceData(
            SdssReferenceDataService referenceDataService) {

        this.brand =
            referenceDataService.retrieveJrsTaxonomy();
        
        this.geographyTypes = 
        	referenceDataService.retrieveSdssGeoTaxonomy();

        this.sourcingStrategyDims =
            referenceDataService.retrieveSourcingStrategies();
        
        this.sdssExceptions =
                referenceDataService.retrieveSdssExceptions();
                     
    }

    public Set<String> bandCategories() {
        return Set.of(
            "ALL BANDS",
            "BAND 7 AND BELOW",
            "BAND 8 AND ABOVE",
            "BAND 8 AND BELOW",
            "BAND 9 AND ABOVE"
        );
    }

    public Set<String> marketScarcityIndexes() {
        return Set.of("HIGH", "LOW", "MEDIUM", "NO VALUE");
    }

    public Set<String> anticipatedDemandValues() {
        return Set.of(
        		 "DECLINE"
        		,"GROW"
        		,"MAINTAIN"
        		,"NOT APPLICABLE"
        		,"NO VALUE AVAILABLE"
        		,"GROW B9+ / DECLINE <= B8"
        		,"MAINTAIN B9+ / DECLINE <= B8"
        		,"GROW B8+ / DECLINE <=B7"
        		,"MAINTAIN B8+ / DECLINE<=B7"
        		);
    }

    public boolean isValidSourcingStrategy(SdssStagingDim row) {
        return true;
    }
    
    public boolean isValidGeographyPath(SdssStagingDim row) {
        return true;
    }

    public boolean isValidJrsPath(SdssStagingDim row) {
        return true;
    }

	public BrandDim getBrand() {
		return brand;
	}

	public void setBrand(BrandDim brand) {
		this.brand = brand;
	}


	public List<SourcingStrategyDim> getSourcingStrategyDims() {
		return sourcingStrategyDims;
	}


	public void setSourcingStrategyDims(List<SourcingStrategyDim> sourcingStrategyDims) {
		this.sourcingStrategyDims = sourcingStrategyDims;
	}

	public List<NaryTreeNode> getGeographyTypes() {
		return geographyTypes;
	}

	public void setGeographyTypes(List<NaryTreeNode> geographyTypes) {
		this.geographyTypes = geographyTypes;
	}

	public List<SdssExceptionDim> getSdssExceptions() {
		return sdssExceptions;
	}

	public void setSdssExceptions(List<SdssExceptionDim> sdssExceptions) {
		this.sdssExceptions = sdssExceptions;
	}
}