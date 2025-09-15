package com.ibm.wfm.services;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.NaryTreeNode;
import com.ibm.wfm.beans.NaryTreeNodeEvaluation;
import com.ibm.wfm.beans.TaxonomyEvaluationResponse;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.utils.Helpers;
import com.ibm.wfm.utils.NaryTree;

@Component
public class TaxonomyEvaluatorService {
	private NaryTree naTree = null;
	private boolean useFullkey = false;
	
	private static final Logger l = Logger.getLogger(TaxonomyEvaluatorService.class.getName());
	

	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	public TaxonomyEvaluatorService() {}
	
	public TaxonomyEvaluatorService(NaryTree naTree, boolean useFullkey) {
		super();
		this.naTree = naTree;
		this.useFullkey = useFullkey;
	}

	public static void main(String[] args) {
		boolean verbose = false;
		boolean validParams = true;
		String delimiter = ",";
		String taxFileName = null;
		String keyStr = "0,1,2,3";        //basic
                      //"0,2,4,6,8,15";    //Standard JR/S 
                      //"0,2,16,18,20,15"; //JRSS_*
		
		String dataFileName = null;
		String outputFileName = null;
		String dataFileOffsetStr = null;
		boolean useFullkey = false;
		boolean outputErrorsOnly = false;
		
		try {
			for (int optind = 0; optind < args.length; optind++) {
				if (args[optind].equalsIgnoreCase("-tax") || args[optind].equalsIgnoreCase("--taxonomy")) {
					taxFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-dat") || args[optind].equalsIgnoreCase("--data")) {
					dataFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-o") || args[optind].equalsIgnoreCase("-output")) {
					outputFileName = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-k") || args[optind].equalsIgnoreCase("--keyOffsets")) {
					keyStr = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-dataOffsets") || args[optind].equalsIgnoreCase("--dataOffsets")) {	
					dataFileOffsetStr = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-d") || args[optind].equalsIgnoreCase("--delimiter")) {
					delimiter = args[++optind];
				} else if (args[optind].equalsIgnoreCase("-verbose")) {
					verbose = true;
				} else if (args[optind].equalsIgnoreCase("-useFullkey")) {
					useFullkey = true;
				} else if (args[optind].equalsIgnoreCase("-outputErrorsOnly")) {
					outputErrorsOnly = true;
				} else if (args[optind].equalsIgnoreCase("-h") || args[optind].equalsIgnoreCase("-help")) {
					validParams = false;
				} else {
					// logger.info("E0001: Unknown parameter specified: " + args[optind]);
					System.out.println("E0001: Unknown parameter specified: " + args[optind]);
					validParams = false;
				}
			} // end - for (int optind = 0; optind < args.length; optind++)			
		} // end try
		catch (Exception e) {
			// logger.error(msgs.getText("error.e0001", e.getMessage()));
			e.printStackTrace();
			validParams = false;
		}
		
		String[] keysString = keyStr.split(",");
		int[] keyOffsets = new int[keysString.length];
		for (int i=0; i<keysString.length; i++) {
			keyOffsets[i] = Integer.parseInt(keysString[i]);
		}

		int[] dataFileOffsets = Helpers.parseIntegerList(dataFileOffsetStr);

		if (!validParams) {
			// logger.info(msgs.getText("info.usage"));
			System.out.println("Usage: com.ibm.gbs.tools.TaxonomyEvaluator"); 
			System.out.println(" ");
			System.out.println("TaxonomyEvaluator Parameters");
			System.out.println("----------------------------");
			System.out.println("[-tax | --taxonomy]   - Taxonomy CSV file name");
			System.out.println("[-dat | --data]       - Data CSV file name");
			System.out.println("[-d   | --delimiter]  - Specifies the delimiter to use  (default: comma)");
			System.out.println("[-k   | --keyOffsets] - Offsets for the keys for each node level of the taxonomy.");
			System.out.println("[-useFullKey]         - Use the full key from the taxonomy.  Used in cases where a code is not unique within the tree.");
			System.out.println("                        However, the code must be unique within the branch (default: true).");
			System.out.println(" ");
			System.out.println("Output Parameters");
			System.out.println("-----------------");
			System.out.println("[-o | -output]    - Output file name. (default: system output)");
			System.out.println(" ");
			System.out.println("Debug Parameters");
			System.out.println("----------------");
			System.out.println("[-h | -help]    - Display usage parameters.");
			System.out.println("[-verbose]      - Display verbose messages.");
			
			System.exit(-99);
		}
		
				
		System.out.println("Processing started at "+new java.util.Date());
		
		TaxonomyEvaluatorService te = new TaxonomyEvaluatorService();
		ArrayList<NaryTreeNodeEvaluation> evaluateNodes = te.evaluate(taxFileName, dataFileName, delimiter, useFullkey, keyOffsets, dataFileOffsets);

		
		if (evaluateNodes==null) {
			System.out.println("Que??");
		}
		else {
			try {
		        BufferedWriter out = null;
				
				if (outputFileName==null) {
					System.out.println(NaryTreeNodeEvaluation.toStringHeading());
				}
				else {
					out = new BufferedWriter(new FileWriter(outputFileName));
					out.write(NaryTreeNodeEvaluation.toCsvStringHeading()+System.lineSeparator());
				}
				double validCnt = 0;
				double nodeFoundInvalidBranchCnt = 0;
				double nodeNotFound=0;
				double invalidNodeCnt = 0;
				double invalidArcCnt = 0;
				double invalidNodeArcCnt = 0;
				for (int i=0; i<evaluateNodes.size();i++) {
					NaryTreeNodeEvaluation ne = evaluateNodes.get(i);
					
					
					if (outputFileName==null) {
						System.out.println(ne.toString(i+1, keyOffsets.length));
					}
					else {
						out.write(ne.toCsvString(i+1, keyOffsets.length)+System.lineSeparator());
					}
	
					if (ne.getFoundInd()==NaryTreeNodeEvaluation.NODE_FOUND_VALID_BRACH) validCnt++;
					else if (ne.getFoundInd()==NaryTreeNodeEvaluation.NODE_FOUND_INVALID_BRACH) nodeFoundInvalidBranchCnt++;
					else nodeNotFound++;
				}
				

				if (out!=null) {
			        out.flush();
			        out.close();
				}
				
				System.out.println("");
				System.out.println("Number of taxonomy levels:\t"+Helpers.formatInt(keyOffsets.length,12));
				System.out.println("Total records:            \t"+Helpers.formatInt(evaluateNodes.size(),12));
				System.out.println("Valid:                    \t"+Helpers.formatInt((int)validCnt,12) + "\t" 
				                 + Helpers.formatDouble((validCnt/evaluateNodes.size())*100, "00.00")+"%");
				System.out.println("Node found, invalid brach:\t"+Helpers.formatInt((int)nodeFoundInvalidBranchCnt,12) + "\t"
				                 + Helpers.formatDouble((nodeFoundInvalidBranchCnt/evaluateNodes.size())*100, "00.00")+"%");
				System.out.println("Node not found:           \t"+Helpers.formatInt((int)nodeNotFound,12) + "\t"
						         + Helpers.formatDouble((nodeNotFound/evaluateNodes.size())*100, "00.00")+"%");
				double total = validCnt+nodeFoundInvalidBranchCnt+nodeNotFound;
				System.out.println("Tie out? "+(evaluateNodes.size()==total?"Yes ":"No  ")
						+"             \t"+Helpers.formatInt((int)total,12));
			}
			catch (IOException e) {
				
			}
		}
		
		System.out.println("Processing ended at "+new java.util.Date());
	}
	
	public TaxonomyEvaluationResponse evaluateTaxonomy(String taxFileName, String dataFileName, String delimiter, boolean useFullkey, int[] keyOffsets, String outputFileNm
			, boolean outputErrorsOnly, int[] dataFileOffsets) {
		
		NaryTree naTree = new NaryTree();
		NaryTreeNode rootNode = naTree.populateNaryTreeFromCsv(taxFileName, delimiter, useFullkey);
		return evaluateTaxonomy(rootNode, dataFileName, delimiter, useFullkey, keyOffsets, outputFileNm, outputErrorsOnly, dataFileOffsets);
		
	}
	
	public TaxonomyEvaluationResponse evaluateTaxonomy(NaryTreeNode rootNode, String dataFileName, String delimiter, boolean useFullkey, int[] keyOffsets, String outputFileNm
			, boolean outputErrorsOnly, int[] dataFileOffsets) {	
		Date startTime = new Date();
		Date taxUploadStartTime = new Date();
		Date dataUploadStartTime = new Date();
		Date evaluationStartTime;
		Date statisticsStartTime;
		Date completionTime;
		
		/*
		-tax /Users/steve/$WFM/wf360/data/jrs_taxononomy.csv
		-dat /Users/steve/$WFM/wf360/data/rah_people_data.csv
		-k 0,2,16,18,20,15
		-useFullKey
		-o /Users/steve/$WFM/wf360/data/jrs_rah_reconciliation.csv
		*/
		evaluationStartTime = new Date();
		
		String uploadDir = fileStorageProperties.getUploadDir();
		String outputFileName = uploadDir+"/"+outputFileNm;;
		
		TaxonomyEvaluationResponse ter = null;	
		
		ArrayList<NaryTreeNodeEvaluation> evaluateNodes = this.evaluate(rootNode, dataFileName, delimiter, useFullkey, keyOffsets, dataFileOffsets);
		
		statisticsStartTime = new Date();
		if (evaluateNodes==null) {
			System.out.println("Que??");
		}
		else {
			try {
		        BufferedWriter out = null;
				
				if (outputFileName==null) {
					System.out.println(NaryTreeNodeEvaluation.toStringHeading());
				}
				else {
					out = new BufferedWriter(new FileWriter(outputFileName));
					
					String dataFileHeader = null;
					if (dataFileOffsets!=null && dataFileOffsets.length>0) {
						FileReader frdr = new FileReader(dataFileName);
						LineNumberReader lnrdr = new LineNumberReader(frdr);
						String lineBuffer = null;
						lineBuffer = lnrdr.readLine();
						if (dataFileOffsets[0]==-1) {
							dataFileHeader=","+lineBuffer;
						}
						else {
							String[] columns = Helpers.parseLine(lineBuffer);
							dataFileHeader = Helpers.buildStringFromOffsets(columns, dataFileOffsets);
						}
						lnrdr.close();
						frdr.close();
					}
					
					out.write(NaryTreeNodeEvaluation.toCsvStringHeading(dataFileHeader)+System.lineSeparator());
				}
				double validCnt = 0;
				double nodeFoundInvalidBranchCnt = 0;
				double nodeNotFound=0;
				for (int i=0; i<evaluateNodes.size();i++) {
					NaryTreeNodeEvaluation ne = evaluateNodes.get(i);
					
					if (outputFileName==null) {
						if (!outputErrorsOnly || ne.getValidNodeInd()+ne.getValidArcInd()>0)
							System.out.println(ne.toString(i+1, keyOffsets.length));
					}
					else {
						if (!outputErrorsOnly || ne.getValidNodeInd()+ne.getValidArcInd()>0)
							out.write(ne.toCsvString(i+1, keyOffsets.length)+System.lineSeparator());
					}
	
					if (ne.getFoundInd()==NaryTreeNodeEvaluation.NODE_FOUND_VALID_BRACH) validCnt++;
					else if (ne.getFoundInd()==NaryTreeNodeEvaluation.NODE_FOUND_INVALID_BRACH) nodeFoundInvalidBranchCnt++;
					else nodeNotFound++;
				}
				

				if (out!=null) {
			        out.flush();
			        out.close();
				}
				
				System.out.println("");
				System.out.println("Number of taxonomy levels:\t"+Helpers.formatInt(keyOffsets.length,12));
				System.out.println("Total records:            \t"+Helpers.formatInt(evaluateNodes.size(),12));
				System.out.println("Valid:                    \t"+Helpers.formatInt((int)validCnt,12) + "\t" 
				                 + Helpers.formatDouble((validCnt/evaluateNodes.size())*100, "00.00")+"%");
				System.out.println("Node found, invalid brach:\t"+Helpers.formatInt((int)nodeFoundInvalidBranchCnt,12) + "\t"
				                 + Helpers.formatDouble((nodeFoundInvalidBranchCnt/evaluateNodes.size())*100, "00.00")+"%");
				System.out.println("Node not found:           \t"+Helpers.formatInt((int)nodeNotFound,12) + "\t"
						         + Helpers.formatDouble((nodeNotFound/evaluateNodes.size())*100, "00.00")+"%");
				double total = validCnt+nodeFoundInvalidBranchCnt+nodeNotFound;
				System.out.println("Tie out? "+(evaluateNodes.size()==total?"Yes ":"No  ")
						+"             \t"+Helpers.formatInt((int)total,12));
				
		        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		                .path("/api/v1/downloadFile/")
		                .path(outputFileNm)
		                .toUriString();
				
		        completionTime = new Date();
		        
				ter = new TaxonomyEvaluationResponse(startTime, taxUploadStartTime, dataUploadStartTime
						, evaluationStartTime, statisticsStartTime, completionTime
						, keyOffsets.length
						, evaluateNodes.size()
						, (int)validCnt
						, (validCnt/evaluateNodes.size())*100
						, (int)nodeFoundInvalidBranchCnt
						, (nodeFoundInvalidBranchCnt/evaluateNodes.size())*100
						, (int)nodeNotFound
						, (nodeNotFound/evaluateNodes.size())*100
						, evaluateNodes.size()==total
						, outputErrorsOnly
						, fileDownloadUri) ;
			}
			catch (IOException e) {
				
			}
		}
		
		return ter;
	}
	
	public ArrayList<NaryTreeNodeEvaluation> evaluate(String taxFileName, String dataFileName, String delimiter, boolean useFullkey, int[] keyOffsets) {
		NaryTree naTree = new NaryTree();
		NaryTreeNode rootNode = naTree.populateNaryTreeFromCsv(taxFileName, delimiter, useFullkey);
		return evaluate(rootNode, dataFileName, delimiter, useFullkey, keyOffsets, null);
	}
	
	public ArrayList<NaryTreeNodeEvaluation> evaluate(String taxFileName, String dataFileName, String delimiter, boolean useFullkey, int[] keyOffsets, int[] dataFileOffsets) {
		NaryTree naTree = new NaryTree();
		NaryTreeNode rootNode = naTree.populateNaryTreeFromCsv(taxFileName, delimiter, useFullkey);
		return evaluate(rootNode, dataFileName, delimiter, useFullkey, keyOffsets, dataFileOffsets);
	}
	
	
	public ArrayList<NaryTreeNodeEvaluation> evaluate(NaryTreeNode rootNode, String dataFileName, String delimiter, boolean useFullkey, int[] keyOffsets, int[] dataFileOffsets) {
		ArrayList<NaryTreeNodeEvaluation> evaluatedNodes = null;
		
		useFullkey = true;
		
		/*
		 * Load taxonomy
		 */
		NaryTree naTree = new NaryTree();
		//NaryTreeNode rootNode = naTree.populateNaryTreeFromCsv(taxFileName, delimiter, useFullkey);
		naTree.setRootNode(rootNode);
		
		FileReader frdr;
		try {
			frdr = new FileReader(dataFileName);
	
			LineNumberReader lnrdr = new LineNumberReader(frdr);
			String lineBuffer = null;
	
			boolean rootFound = false;
			int cnt = 0;
			while ((lineBuffer = lnrdr.readLine()) != null) {
				if (cnt++==0) continue;
				if (cnt%10000 == 0) System.out.println(cnt);
				else if(cnt%1000 == 0) System.out.print(".");
				
				String[] columns = Helpers.parseLine(lineBuffer);
				
				String[] keys = getBranchKeys(columns, keyOffsets);
				
				String origKey = "";
				for (int i=0; i<keys.length; i++) origKey+=(keys[i]+":");
				
				rootFound = false;
				
				int foundInd = NaryTreeNodeEvaluation.NODE_NOT_FOUND; //-1
				NaryTreeNode leafNode = null;
				NaryTreeNode lastValidLeafNode = null;
				int validNodeInd = 0;
				int validArcInd = 0;
				
				for (int i=keys.length-1; i>=0; i--) {
					String fullKey = "";
					for (int j=0; j<i+1; j++) fullKey+=keys[j].trim()+":";
					
					NaryTreeNode nodeFullKey = naTree.find(fullKey, rootNode, true);
					
					if (nodeFullKey==null) { //Branch-segment not found
						NaryTreeNode node = naTree.find(keys[i], rootNode, false);
						if (node==null) { //Node not found
							if (i>0) 
								validArcInd += (int)Math.pow(2,i-1);
							validNodeInd+= (int)Math.pow(2,i);
						}
						else {
							if (i>0 && !node.getParentNode().getCode().equals(keys[i-1])) {
								validArcInd += (int)Math.pow(2,i-1);
							}
							if (i==keys.length-1) {
								leafNode = node;
								foundInd = NaryTreeNodeEvaluation.NODE_FOUND_INVALID_BRACH;
							}
							//if (lastValidLeafNode==null) lastValidLeafNode = node;							
						}
					}
					else { //Branch-segment found
						if (i==keys.length-1) { ////Branch-segment is the full branch
							leafNode = nodeFullKey;
							foundInd = NaryTreeNodeEvaluation.NODE_FOUND_VALID_BRACH;
						}
						if (lastValidLeafNode==null) lastValidLeafNode = nodeFullKey;
						NaryTreeNodeEvaluation ne = new NaryTreeNodeEvaluation(foundInd
								, origKey
								, leafNode
								, lastValidLeafNode
								, validNodeInd, validArcInd);
						if (evaluatedNodes==null) evaluatedNodes = new ArrayList<NaryTreeNodeEvaluation>();
						
						//Add the data file columns
						if (dataFileOffsets!=null && dataFileOffsets.length>0) {
							if (dataFileOffsets[0]==-1) ne.setDataFileRow(lineBuffer);
							else {
								ne.setDataFileRow(Helpers.buildStringFromOffsets(columns, dataFileOffsets));
							}
						}
						
						evaluatedNodes.add(ne);
						rootFound = true;
						i=-1; //exit the loop
					}
				} //end - for (int i=
				if (!rootFound) {
					NaryTreeNodeEvaluation ne = new NaryTreeNodeEvaluation(foundInd
							, origKey
							, leafNode
							, lastValidLeafNode
							, validNodeInd, validArcInd);
					if (evaluatedNodes==null) evaluatedNodes = new ArrayList<NaryTreeNodeEvaluation>();
					
					//Add the data file columns
					if (dataFileOffsets!=null && dataFileOffsets.length>0) {
						if (dataFileOffsets[0]==-1) ne.setDataFileRow(lineBuffer);
						else {
							ne.setDataFileRow(Helpers.buildStringFromOffsets(columns, dataFileOffsets));
						}
					}
					
					evaluatedNodes.add(ne);
				}
			}
			lnrdr.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return evaluatedNodes;		
	}
	
	public NaryTreeNode populateTaxonomyFromCsv(String csvFileName, String delimiter, boolean useFullKey) {
		NaryTree naTree = new NaryTree();
		NaryTreeNode rootNode = naTree.populateNaryTreeFromCsv(csvFileName, delimiter, useFullkey);
		return rootNode;
	}
	
	public NaryTree getNaTree() {
		return naTree;
	}

	public void setNaTree(NaryTree naTree) {
		this.naTree = naTree;
	}

	public boolean isUseFullkey() {
		return useFullkey;
	}

	public void setUseFullkey(boolean useFullkey) {
		this.useFullkey = useFullkey;
	}
	
	public String[] getBranchKeys(String[] columns, int[] keyOffsets) {
		String[] branchKeys = new String[keyOffsets.length];
		for (int i=0; i<keyOffsets.length; i++) {
			branchKeys[i] = columns[keyOffsets[i]];
		}
		return branchKeys;
	}

}
