package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class IssuanceTest {

	private BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
	
	@Test
	public void shouldReturnSingleItemInvoiceForSingleItemRequest() {
		
		InvoiceRequest invoiceRequest = new InvoiceRequest(null);
		ProductData productData = new ProductData(null, null, null, null, null);
		Money totalCost = new Money(0);
		invoiceRequest.add(new RequestItem(productData, 0, totalCost));
		
		TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
		Money taxMoney = new Money(0);
		Tax tax = new Tax(taxMoney, null);
		when(mockTaxPolicy.calculateTax(null, totalCost)).thenReturn(tax);
		
		Invoice invoice = bookKeeper.issuance(invoiceRequest, mockTaxPolicy);
		
		assertThat(invoice.getItems().size(), equalTo(1));
	}
	
	@Test
	public void shouldCallTwiceCalculateTaxMethodForTwoItemsRequest() {
		
		InvoiceRequest invoiceRequest = new InvoiceRequest(null);
		ProductData productData = new ProductData(null, null, null, null, null);
		Money totalCost = new Money(0);
		RequestItem item = new RequestItem(productData, 0, totalCost);
		invoiceRequest.add(item);
		invoiceRequest.add(item);
		
		TaxPolicy mockTaxPolicy = mock(TaxPolicy.class);
		Money taxMoney = new Money(0);
		Tax tax = new Tax(taxMoney, null);
		when(mockTaxPolicy.calculateTax(null, totalCost)).thenReturn(tax);
		
		bookKeeper.issuance(invoiceRequest, mockTaxPolicy);
		
		verify(mockTaxPolicy, times(2)).calculateTax(null, totalCost);
	}

}
