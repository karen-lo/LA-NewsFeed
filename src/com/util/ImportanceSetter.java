package com.util;



import java.util.*;

import net.billylieurance.azuresearch.AbstractAzureSearchQuery.AZURESEARCH_QUERYTYPE;
import net.billylieurance.azuresearch.*;

import org.apache.http.impl.client.*;

public class ImportanceSetter {

	private final String API_KEY = "B1rIpd+rFK8Rd3St15JWSGF7Kbq8vhlE5u20z1Iu7SQ";
	private final int UPPER_LIMIT = 1250000;
	private long store;

	public void setImportance(ArrayList<Article> a) {
		for (int i = 0; i < a.size(); i++) {
			double  ratio = (searchImportance(a.get(i).getTitle())+ 0.0) / UPPER_LIMIT;
			
			if(ratio > 1)
				ratio = 1;
			
			a.get(i).setPriority((int) (ratio * 255));
		}
	}

	private int searchImportance(final String sentence) {

		new Thread(new Runnable() {

			public synchronized void run() {
				AzureSearchCompositeQuery aq = new AzureSearchCompositeQuery();

				aq.setAppid(API_KEY);
				aq.setQuery(sentence);
				aq.setSources(new AZURESEARCH_QUERYTYPE[] { AZURESEARCH_QUERYTYPE.WEB });
				aq.doQuery();

				AzureSearchResultSet<AbstractAzureSearchResult> ars = aq
						.getQueryResult();

				store = ars.getWebTotal();			
			}
			  
		}).start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (int) store;

	}
	
	/*public static int toHex( int n ) {
		return Integer.valueOf(String.valueOf(n), 16);
	}*/
}