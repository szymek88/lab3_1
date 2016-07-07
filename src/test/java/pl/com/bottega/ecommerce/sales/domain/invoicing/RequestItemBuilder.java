package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
	
	private ProductData productData = new ProductData(null, null, null, null, null);
	private int quantity = 0;
	private Money totalCost = new Money(0);

	public RequestItem build() {
		return new RequestItem(productData, quantity, totalCost);
	}

	public RequestItemBuilder withProductData(ProductData productData) {
		this.productData = productData;
		return this;
	}

	public RequestItemBuilder ofQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public RequestItemBuilder ofTotalCost(Money totalCost) {
		this.totalCost = totalCost;
		return this;
	}
	
	public static RequestItemBuilder requestItem() {
		return new RequestItemBuilder();
	}

}
