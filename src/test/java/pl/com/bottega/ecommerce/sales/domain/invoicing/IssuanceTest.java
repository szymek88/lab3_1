package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItemBuilder.requestItem;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class IssuanceTest {

	private BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
	
	private InvoiceRequest invoiceRequest;
	private Money money = new Money(0);
	private Tax tax = new Tax(money, null);
	
	private TaxPolicy mockTaxPolicy;
	
	@Before
	public void setUp() {
		invoiceRequest = new InvoiceRequest(null);
		
		mockTaxPolicy = mock(TaxPolicy.class);
		when(mockTaxPolicy.calculateTax(null, money)).thenReturn(tax);
	}
	
	@Test
	public void shouldReturnSingleItemInvoiceForSingleItemRequest() {
		
		RequestItem item = a(requestItem().ofQuantity(1));
		invoiceRequest.add(item);
		
		Invoice invoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);
		
		assertThat(invoice.getItems().size(), equalTo(1));
	}
	
	@Test
	public void shouldCallTwiceCalculateTaxMethodForTwoItemsRequest() {
		
		Money money = new Money(1000);
		RequestItem item = a(requestItem().ofQuantity(3).ofTotalCost(money));
		invoiceRequest.add(item);
		invoiceRequest.add(item);
		
		when(mockTaxPolicy.calculateTax(null, money)).thenReturn(tax);
		
		bookKeeper.issuance(invoiceRequest, mockTaxPolicy);
		
		verify(mockTaxPolicy, times(2)).calculateTax(null, money);
	}
	
	@Test
	public void shouldReturnEmptyInvoiceForEmptyRequest() {
		
		Invoice invoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);
		
		assertThat(invoice.getItems().size(), equalTo(0));
	}
	
	@Test
	public void shouldNoTCallCalculateTaxMethodForEmptyRequest() {
		
		bookKeeper.issuance(invoiceRequest, mockTaxPolicy);
		
		verify(mockTaxPolicy, never()).calculateTax(null, null);
	}
	
	private RequestItem a(RequestItemBuilder builder) {
		return builder.build();
}

}
