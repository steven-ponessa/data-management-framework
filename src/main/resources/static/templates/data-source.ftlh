,
	{
		"id": 5,
		"dataSourceId": "${db2table.nameParmCase}",
		"name": "${db2table.nameProperCase}",
		"description": "${db2table.remarks}",
		"owner": "Steve Ponessa",
		"techOwner": "Steve Ponessa",
		"baseUrl": "https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/${db2table.groupNm}/${db2table.nameParmCase}",
		"contentLocationUrl": "https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/swagger-ui.html#/${db2table.groupNm}-controller",
		"creationDt": "2023-02-20-22.09.59.720",
		"noSelectionCode": "zz",
		"attributes": [
			{
				"id": "lock",
				"name": "",
				"isEditable": false,
				"isSortable": false,
				"className": "headerLock"
			},
<#list db2table.dbColumns as dbcolumn>			
			{
				"id": "${dbcolumn.nameCamelCase}",
				"name": "${dbcolumn.nameProperCase}",
				"columnName": "${dbcolumn.name}",
				"dataType": "${dbcolumn.dataType}",
				"length": ${dbcolumn.length},
				"scale": "",
				"nullable": "N",
				"defaultValue": "",
				"keySeq": ${dbcolumn.keySeq},
				"foreignKeySeq": ${dbcolumn.foreignKeySeq},
				"isId": false,
				"isEditable": true,
				"isSortable": true
			},
</#list>
			{
				"id": "actions",
				"name": "",
				"isEditable": false,
				"isSortable": false,
				"className": "headerActions"
			}
		]
	}