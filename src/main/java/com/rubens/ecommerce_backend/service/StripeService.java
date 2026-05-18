package com.rubens.ecommerce_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import com.rubens.ecommerce_backend.dto.StripeProductResponse;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeKey;

    public StripeProductResponse createProduct(String name, Long price) throws Exception {
        Stripe.apiKey = stripeKey;

        ProductCreateParams productParams =
                ProductCreateParams.builder()
                        .setName(name)
                        .build();

        Product stripeProduct = Product.create(productParams);

        PriceCreateParams priceParams =
                PriceCreateParams.builder()
                        .setUnitAmount(price)
                        .setCurrency("brl")
                        .setProduct(stripeProduct.getId())
                        .build();

        Price priceObj = Price.create(priceParams);

        return new StripeProductResponse(
                stripeProduct.getId(),
                priceObj.getId()
        );
    }

    public String createNewPrice(String productId, Long price) throws Exception {
        Stripe.apiKey = stripeKey;

        PriceCreateParams params =
                PriceCreateParams.builder()
                        .setUnitAmount(price)
                        .setCurrency("brl")
                        .setProduct(productId)
                        .build();

        Price priceObj = Price.create(params);

        return priceObj.getId();
    }

    public void updateProductName(String productId, String newName) throws Exception {
        Stripe.apiKey = stripeKey;

        Product stripeProduct = Product.retrieve(productId);

        ProductUpdateParams params =
                ProductUpdateParams.builder()
                        .setName(newName)
                        .build();

        stripeProduct.update(params);
    }

    public void updateDefaultPrice(String productId, String priceId) throws Exception {
        Stripe.apiKey = stripeKey;

        Product product = Product.retrieve(productId);

        ProductUpdateParams params =
                ProductUpdateParams.builder()
                        .setDefaultPrice(priceId)
                        .build();

        product.update(params);
    }
}