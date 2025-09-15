package com.ibm.wfm.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.wfm.beans.Db2Table;
import com.ibm.wfm.beans.Db2Column;
import com.ibm.wfm.beans.ExcelColumn;
import com.ibm.wfm.configurations.FileStorageProperties;
import com.ibm.wfm.utils.DaoArtifactGenerator;
import com.ibm.wfm.utils.DataMarshaller;

import freemarker.template.TemplateException;

@Component
public class DaoArtifactGeneratorService extends AbstractDaoService {
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	
	public List<String> generateArtifcat(int artifactRequestValue, String excelFileName, String excelTabName, String outputDirectory) throws IOException, TemplateException, NoSuchMethodException, SecurityException {
		//String templatePath = System.getProperty("user.dir")+fileStorageProperties.getTemplateDir();
		String templatePath = fileStorageProperties.getTemplateDir();
		
		List<String> generatedFiles = new ArrayList<String>();
		
		List<Db2Table> db2Tables = DataMarshaller.getObjectListFromExcel(Db2Table.class, excelFileName, "Tables");
		
		String[] tabs = excelTabName.split(",");

		for (Db2Table db2Table: db2Tables) {
			if (excelTabName.equalsIgnoreCase("all") || Arrays.stream(tabs).anyMatch(db2Table.getName()::equals)) {
				List <Db2Column> dbColumns = DataMarshaller.getObjectListFromExcel(Db2Column.class, excelFileName, db2Table.getName());
				List <ExcelColumn> excelColumns = DataMarshaller.getObjectListFromExcel(ExcelColumn.class, excelFileName, db2Table.getName());
				
				//for (Db2Column dbc: dbColumns) {
				//	System.out.println(dbc.toString());
				//}

				//excelColumns.removeIf(element -> (element.getP("a")));
				Iterator<ExcelColumn> iter = excelColumns.iterator();
				while (iter.hasNext()) {
					ExcelColumn e = iter.next();
				  if (e.getPosition()==-1) iter.remove();
				}
				
				db2Table.setDbColumns(dbColumns);
				db2Table.setExcelColumns(excelColumns.size()==0?null:excelColumns);
				
				if (Db2Table.isDdlRequested(artifactRequestValue)) {
					Writer out = new FileWriter(outputDirectory+"/"+db2Table.getName()+"_DIM.ddl");
					DaoArtifactGenerator.generate(templatePath, "ddl.ftlh", db2Table, out);
					generatedFiles.add(db2Table.getName()+"_DIM.ddl");
				}
				if (Db2Table.isBeanRequested(artifactRequestValue)) {
					Writer out = new FileWriter(outputDirectory+"/"+db2Table.getNameProperCase()+"Dim.java");
					DaoArtifactGenerator.generate(templatePath, "bean.ftlh", db2Table, out);
					generatedFiles.add(db2Table.getNameProperCase()+"Dim.java");
				}
				if (Db2Table.isControllerRequested(artifactRequestValue)) {
					Writer out = new FileWriter(outputDirectory+"/"+db2Table.getNameProperCase()+"Controller.java");
					DaoArtifactGenerator.generate(templatePath, "controller.ftlh", db2Table, out);
					generatedFiles.add(db2Table.getNameProperCase()+"Controller.java");
				}
				if (Db2Table.isDataSourceRequested(artifactRequestValue)) {
					Writer out = new FileWriter(outputDirectory+"/"+db2Table.getNameParmCase()+".json");
					DaoArtifactGenerator.generate(templatePath, "data-source.ftlh", db2Table, out);
					generatedFiles.add(db2Table.getNameParmCase()+".json");
				}
			}
		}
		
		return generatedFiles;
	}

	@Override
	public Connection getConnection() {
		// not required - Excel only
		return null;
	}
}
