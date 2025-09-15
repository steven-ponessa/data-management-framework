package com.ibm.wfm.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ibm.wfm.beans.Db2Table;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class DaoArtifactGenerator {

	public static void main(String[] args) throws IOException, TemplateException {
		/*
		Db2Table daoTemplate = new Db2Table();
		daoTemplate.setSchema("REFT");
		daoTemplate.setName("GBS_TALL_LIST");
		daoTemplate.setRemarks("GBS Short List of Skills");
		daoTemplate.setScd(true);
		// daoTemplate.setExtensionNm("GBS");
		/*
		 * public DbColumn(String name, String dataType, int length, int scale, boolean
		 * nullable, String defaultValue, int keySeq, boolean id, boolean scd, String
		 * remarks name, dataType, length, scale, nullable, defaultValue, keySeq, id,
		 * scd, remarks
		 *
		daoTemplate.addDbColumn(new Db2Column("JRS_ID", "INTEGER", 4, 0, false, null, -1, true, false, null));
		daoTemplate.addDbColumn(new Db2Column("JRS_CD", "CHAR", 8, 0, false, null, 1, false, false, null));
		daoTemplate.addDbColumn(new Db2Column("JRS_2_CD", "CHAR", 8, 0, false, null, 2, false, false, null));
		daoTemplate.addDbColumn(new Db2Column("JRS_3_CD", "CHAR", 8, 0, false, null, 3, false, false, null));
		daoTemplate.addDbColumn(new Db2Column("JRS_NM", "VARCHAR", 256, 0, false, null, -1, false, false, ""));
		daoTemplate.addDbColumn(new Db2Column("F_1", "VARCHAR", 256, 0, false, null, -1, false, false, ""));
		daoTemplate.addDbColumn(new Db2Column("FI_2", "DATE", 10, 0, false, null, -1, false, false, ""));
		daoTemplate.addDbColumn(new Db2Column("FIL_3", "TIMESTAMP", 26, 0, false, null, -1, false, false, ""));
		daoTemplate.addDbColumn(new Db2Column("FILL_4", "DECIMAL", 20, 2, false, null, -1, false, false, ""));
		daoTemplate.addDbColumn(new Db2Column("FILLE_5", "VARCHAR", 256, 0, false, null, -1, false, false, ""));
		daoTemplate.addDbColumn(new Db2Column("FILLER_6", "VARCHAR", 256, 0, false, null, -1, false, false, ""));

		daoTemplate.addExcelColumn(new ExcelColumn(1, "A", "Any thing", "JRS_2_CD"));
		daoTemplate.addExcelColumn(new ExcelColumn(1, "A", "Any thing", "JRS_5_CD"));


		String templatePath = System.getProperty("user.dir") + "/target/classes/static/templates/";// with ddl.ftlh

		String templateName = "ddl.ftlh"; // "bean.ftlh";

		Writer out = new OutputStreamWriter(System.out);
		DaoArtifactGenerator.generate(templatePath, templateName, daoTemplate, out);
		*/
		
		//SELECT A.CHRG_GROUP_CD, A.CHRG_GROUP_DESC, B.CNT FROM BMSIW.CHRG_GROUP_REF_V A, ( SELECT CHRG_GROUP_CD, COUNT(*) AS CNT from bmsiw.base_labor_rate_v where chrgplan_cd={year} and orig_country_cd={country} and orig_company_cd={company} and status={status} GROUP BY CHRG_GROUP_CD ) B WHERE A.CHRG_GROUP_CD=B.CHRG_GROUP_CD AND A.COUNTRY_CD={country} AND A.COMPANY_CD={company}
				
		//String templateContent = "SELECT <#if !(country?exists) || !(country?has_content) || country != '*'> put out country filer</#if> rest";
		//String templateContent = "SELECT A.CHRG_GROUP_CD, A.CHRG_GROUP_DESC, B.CNT FROM BMSIW.CHRG_GROUP_REF_V A, (SELECT CHRG_GROUP_CD, COUNT(*) AS CNT from bmsiw.base_labor_rate_v where chrgplan_cd={year}<#if !(country?exists) || !(country?has_content) || country!='*'> and orig_country_cd={country}</#if><#if !(company?exists) || !(company?has_content) || company!='*'> and orig_company_cd={company}</#if> and status={status} GROUP BY CHRG_GROUP_CD ) B WHERE A.CHRG_GROUP_CD=B.CHRG_GROUP_CD<#if !(country?exists) || !(country?has_content) || country!='*'> AND A.COUNTRY_CD={country}</#if><#if !(company?exists) || !(company?has_content) || company!='*'> AND A.COMPANY_CD={company}</#if>";
		//String templateContent = "SELECT CHRGPLAN_CD, EFF_MONTH_NUM, ORIG_COUNTRY_CD, ORIG_COMPANY_CD, CHRG_GROUP_CD, RATECLAS_CD, RATELST_ID, SERVICE_GROUP_ID, SERVICE_RESTYP_CD, APPL_USE_CD, RATELST_TYP_CD, ORIG_DIV_CD, ORIG_FA_CD, ORIG_LOC_CD, SERVICE_DESC, BASE_RATE_AMT, UM_DESC, ORIG_CURRENCY_CD, SKEW_CD, PRIORITY, SERVICE_CLASS_CD, SERV_GROUP_OPT_IND, STATUS, CONTROL_CHRGGRP_CD, REACTIVATED_DATE, APPRV_CNUM_ID, REQUEST_ID, REQUEST_GRP_ID, REQ_CNUM_ID FROM BMSIW.BASE_LABOR_RATE_V WHERE APPL_USE_CD='S' <#if !(year?exists) || !(year?has_content) || year!='*'> AND CHRGPLAN_CD={year} </#if> <#if !(month?exists) || !(month?has_content) || month!='*'> AND EFF_MONTH_NUM={month} </#if> AND RATELST_TYP_CD='CHRG' AND STATUS='O' <#if !(country?exists) || !(country?has_content) || country!='*'> AND ORIG_COUNTRY_CD={country} </#if> <#if !(company?exists) || !(company?has_content) || company!='*'> AND ORIG_COMPANY_CD={company} </#if> <#if !(charge-group?exists) || !(charge-group?has_content) || charge-group!='*'> AND CHRG_GROUP_CD={charge-group} </#if> <#if !(rate-class?exists) || !(rate-class?has_content) || rate-class!='*'> AND RATECLAS_CD={rate-class} </#if> <#if !(rate-list?exists) || !(rate-list?has_content) || rate-list!='*'> AND RATELST_ID={rate-list} </#if> <#if !(service-group?exists) || !(service-group?has_content) || service-group!='*'> AND SERVICE_GROUP_ID={service-group} </#if> <#if !(skill-family?exists) || !(skill-family?has_content) || skill-family!='*'> AND SERVICE_RESTYP_CD={skill-family} </#if>";
		//                                                                                                                          10                                                                                                  20                                                                                                  30                                                                                                  40                                                                                                  50                                                                                                  60                                                                                                  70                                                                                                  80                                                                                                  90
		//                        ....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0....*....1....*....2....*....3....*....4....*....5....*....6....*....7....*....8....*....9....*....0
		//String templateContent = "SELECT CHRGPLAN_CD, EFF_MONTH_NUM, ORIG_COUNTRY_CD, ORIG_COMPANY_CD, CHRG_GROUP_CD, RATECLAS_CD, RATELST_ID, SERVICE_GROUP_ID, SERVICE_RESTYP_CD, APPL_USE_CD, RATELST_TYP_CD, ORIG_DIV_CD, ORIG_FA_CD, ORIG_LOC_CD, SERVICE_DESC, BASE_RATE_AMT, UM_DESC, ORIG_CURRENCY_CD, SKEW_CD, PRIORITY, SERVICE_CLASS_CD, SERV_GROUP_OPT_IND, STATUS, CONTROL_CHRGGRP_CD, REACTIVATED_DATE, APPRV_CNUM_ID, REQUEST_ID, REQUEST_GRP_ID, REQ_CNUM_ID FROM BMSIW.BASE_LABOR_RATE_V WHERE APPL_USE_CD='S'<#if !(year?exists) || !(year?has_content) || year!='*'> AND CHRGPLAN_CD={year}</#if><#if !(month?exists) || !(month?has_content) || month!='*'> AND EFF_MONTH_NUM={month}</#if> AND RATELST_TYP_CD='CHRG' AND STATUS='O'<#if !(country?exists) || !(country?has_content) || country!='*'> AND ORIG_COUNTRY_CD={country}</#if><#if !(company?exists) || !(company?has_content) || company!='*'> AND ORIG_COMPANY_CD={company}</#if><#if !(charge_group?exists) || !(charge_group?has_content) || charge_group!='*'> AND CHRG_GROUP_CD={charge_group}</#if><#if !(rate_class?exists) || !(rate_class?has_content) || rate_class!='*'> AND RATECLAS_CD={rate_class}</#if><#if !(rate_list?exists) || !(rate_list?has_content) || rate_list!='*'> AND RATELST_ID={rate_list} </#if><#if !(service_group?exists) || !(service_group?has_content) || service_group!='*'> AND SERVICE_GROUP_ID={service_group}</#if><#if !(skill_family?exists) || !(skill_family?has_content) || skill_family!='*'> AND SERVICE_RESTYP_CD={skill_family} </#if>";
		String templateContent = "SELECT CHRGPLAN_CD, EFF_MONTH_NUM, ORIG_COUNTRY_CD, ORIG_COMPANY_CD, CHRG_GROUP_CD, RATECLAS_CD, RATELST_ID, SERVICE_GROUP_ID, SERVICE_RESTYP_CD, APPL_USE_CD, RATELST_TYP_CD, ORIG_DIV_CD, ORIG_FA_CD, ORIG_LOC_CD, SERVICE_DESC, BASE_RATE_AMT, UM_DESC, ORIG_CURRENCY_CD, SKEW_CD, PRIORITY, SERVICE_CLASS_CD, SERV_GROUP_OPT_IND, STATUS, CONTROL_CHRGGRP_CD, REACTIVATED_DATE, APPRV_CNUM_ID, REQUEST_ID, REQUEST_GRP_ID, REQ_CNUM_ID FROM BMSIW.BASE_LABOR_RATE_V WHERE APPL_USE_CD='S'<#if !(year?exists) || !(year?has_content) || year!='*'> AND CHRGPLAN_CD={year}</#if><#if (month?exists) && (month?has_content)> AND EFF_MONTH_NUM={month}</#if> AND RATELST_TYP_CD='CHRG' AND STATUS='O'<#if !(country?exists) || !(country?has_content) || country!='*'> AND ORIG_COUNTRY_CD={country}</#if><#if !(company?exists) || !(company?has_content) || company!='*'> AND ORIG_COMPANY_CD={company}</#if><#if (charge_group?exists) && (charge_group?has_content)> AND CHRG_GROUP_CD={charge_group}</#if><#if !(rate_class?exists) || !(rate_class?has_content) || rate_class!='*'> AND RATECLAS_CD={rate_class}</#if><#if (rate_list?exists) && (rate_list?has_content)> AND RATELST_ID={rate_list} </#if><#if (service_group?exists) && (service_group?has_content)> AND SERVICE_GROUP_ID={service_group}</#if><#if !(skill_family?exists) || !(skill_family?has_content) || skill_family!='*'> AND SERVICE_RESTYP_CD={skill_family} </#if>";
				
		Map<String, Object> model = new HashMap<>();

		//model.put("year", "2024");
		model.put("country", "*");
		//model.put("company", "*");
		//model.put("status", "O");

		String renderedTemplate = DaoArtifactGenerator.renderTemplate(templateContent, model);
		System.out.println(renderedTemplate);


	}

	public static String renderTemplate(String templateContent, Map<String, Object> model) {
		/* Create and adjust the configuration singleton */
		Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_29);
		try {
			Template template = new Template("template", new StringReader(templateContent), freemarkerConfig);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch (Exception ex) {
			throw new RuntimeException("Error rendering FreeMarker template", ex);
		}
	}

	public static void generate(String templatePath, String templateName, Db2Table db2Table, Writer out)
			throws IOException, TemplateException {

		/* Create and adjust the configuration singleton */
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

		cfg.setDirectoryForTemplateLoading(new File(templatePath));

		// Recommended settings for new projects:
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		cfg.setFallbackOnNullLoopVariable(false);

		/* Get the template (uses cache internally) */
		Template temp = cfg.getTemplate(templateName);

		Map root = new HashMap();
		root.put("db2table", db2Table);

		/* Merge data-model with template */
		// Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);
		out.flush();
		out.close();
		return;
	}

}
