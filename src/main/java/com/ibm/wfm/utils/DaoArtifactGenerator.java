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

		String templateContent = "SELECT CHRGPLAN_CD, EFF_MONTH_NUM, ORIG_COUNTRY_CD, ORIG_COMPANY_CD, CHRG_GROUP_CD, RATECLAS_CD, RATELST_ID, SERVICE_GROUP_ID, SERVICE_RESTYP_CD, APPL_USE_CD, RATELST_TYP_CD, ORIG_DIV_CD, ORIG_FA_CD, ORIG_LOC_CD, SERVICE_DESC, BASE_RATE_AMT, UM_DESC, ORIG_CURRENCY_CD, SKEW_CD, PRIORITY, SERVICE_CLASS_CD, SERV_GROUP_OPT_IND, STATUS, CONTROL_CHRGGRP_CD, REACTIVATED_DATE, APPRV_CNUM_ID, REQUEST_ID, REQUEST_GRP_ID, REQ_CNUM_ID FROM BMSIW.BASE_LABOR_RATE_V WHERE APPL_USE_CD='S'<#if !(year?exists) || !(year?has_content) || year!='*'> AND CHRGPLAN_CD={year}</#if><#if (month?exists) && (month?has_content)> AND EFF_MONTH_NUM={month}</#if> AND RATELST_TYP_CD='CHRG' AND STATUS='O'<#if !(country?exists) || !(country?has_content) || country!='*'> AND ORIG_COUNTRY_CD={country}</#if><#if !(company?exists) || !(company?has_content) || company!='*'> AND ORIG_COMPANY_CD={company}</#if><#if (charge_group?exists) && (charge_group?has_content)> AND CHRG_GROUP_CD={charge_group}</#if><#if !(rate_class?exists) || !(rate_class?has_content) || rate_class!='*'> AND RATECLAS_CD={rate_class}</#if><#if (rate_list?exists) && (rate_list?has_content)> AND RATELST_ID={rate_list} </#if><#if (service_group?exists) && (service_group?has_content)> AND SERVICE_GROUP_ID={service_group}</#if><#if !(skill_family?exists) || !(skill_family?has_content) || skill_family!='*'> AND SERVICE_RESTYP_CD={skill_family} </#if>";
				
		Map<String, Object> model = new HashMap<>();
		model.put("country", "*");

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
