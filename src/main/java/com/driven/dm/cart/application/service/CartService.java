package com.driven.dm.cart.application.service;

import com.driven.dm.cart.application.exception.CartErrorCode;
import com.driven.dm.cart.domain.entity.Cart;
import com.driven.dm.cart.domain.entity.CartItem;
import com.driven.dm.cart.infrastructure.repository.CartItemRepository;
import com.driven.dm.cart.infrastructure.repository.CartRepository;
import com.driven.dm.cart.presentation.dto.request.AddItemRequest;
import com.driven.dm.cart.presentation.dto.response.CartItemResponse;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.infrastructure.repository.MenuRepository;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;

    public CartItemResponse addItem(UUID userId, UUID shopId, AddItemRequest req) {
        if (req.getQuantity() <= 0) {
            throw new AppException(CartErrorCode.INVALID_QUANTITY);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
            .orElseThrow(() -> new AppException(CartErrorCode.CART_NOT_FOUND));

        Menu menu = menuRepository.findById(req.getMenuId())
            .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        // if (!menu.getShop().getId().equals(shopId)) throw new IllegalArgumentException("해당 가게의 메뉴가 아닙니다.");

        Cart cart = cartRepository.findByUser_IdAndShop_Id(userId, shopId)
            .orElseGet(() -> cartRepository.save(Cart.of(user, shop)));

        Cart.MenuSnapshot snapshot = new Cart.MenuSnapshot(
            menu, menu.getId(), menu.getName(), menu.getPrice()
        );
        CartItem item = cart.addOrIncrease(snapshot, req.getQuantity());
        cartRepository.saveAndFlush(cart);

        return CartItemResponse.from(item);
    }
}
