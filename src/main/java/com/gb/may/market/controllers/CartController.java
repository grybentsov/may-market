package com.gb.may.market.controllers;

import com.gb.may.market.beans.Cart;
import com.gb.may.market.services.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private ProductsService productsService;
    private Cart cart;

//    @GetMapping
//    public String showCartPage(Model model){
//        model.addAttribute("cart", cart);
//        return "cart";
//    }

    @GetMapping
    public String showCartPage(){
        return "cart";
    }

    @GetMapping("/add/{productId}")
    public void addProductToCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.add(productsService.findById(productId));
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/decrement/{productId}")
    public void decrementProductInCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.decrement(productsService.findById(productId));
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/remove/{productId}")
    public void removeProductFromCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.removeByProductId(productId);
        response.sendRedirect(request.getHeader("referer"));
    }
}
