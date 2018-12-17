package de.hagen.ps.nlp.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.hagen.nlp.backend.core.IEXNews;
import de.hagen.nlp.backend.core.IEXNewsApi;

public class TestIEXApi {

	@Test
	public void readMarketNews() {
		IEXNewsApi api = new IEXNewsApi();
		List<IEXNews> result = api.getMarketNews();
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void readStockNews() {
		IEXNewsApi api = new IEXNewsApi();
		List<IEXNews> result = api.getStockNews("AAPL");
		assertTrue(result.size() > 0);
	}
	
}
