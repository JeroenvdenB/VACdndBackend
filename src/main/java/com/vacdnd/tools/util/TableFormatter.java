package com.vacdnd.tools.util;

public class TableFormatter {
	public static String formatTable(String input) {
				
		String table;
		String niceTable;
		String header;
		
		header = input.substring(0, input.indexOf('\n'));
		table = input.substring(input.indexOf('\n')+1);
		
		header = "^" + header.replace('\t', '^') + " ^\n";
				
		niceTable = table.replace('\t', '|');
		niceTable = niceTable.replace("\n", "|\n|");		
		niceTable = "|" + niceTable + "|";

		return header+niceTable;
	}
}
