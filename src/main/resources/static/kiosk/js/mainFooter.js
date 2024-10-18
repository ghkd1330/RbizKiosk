document.addEventListener("DOMContentLoaded", function() {
    const cartItemsContainer = document.getElementById('cart-items-container');

    cartItemsContainer.addEventListener('click', function(event) {
        const deleteButton = event.target.closest('.delete-btn');
        const plusButton = event.target.closest('.plus-btn');
        const minusButton = event.target.closest('.minus-btn');

        if (deleteButton) {
            const productId = deleteButton.getAttribute('data-product-id');
            removeItemFromCart(productId);
        } else if (plusButton) {
            const productId = plusButton.getAttribute('data-product-id');
            updateItemQuantity(productId, 1);
        } else if (minusButton) {
            const productId = minusButton.getAttribute('data-product-id');
            updateItemQuantity(productId, -1);
        }
    });

    function removeItemFromCart(productId) {
        fetch('/cart/removeItem', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({productId: productId})
        })
            .then(function(response) {
                if (response.ok) {
                    // DOM에서 해당 상품 제거
                    const itemDiv = document.querySelector('.delete-btn[data-product-id="' + productId + '"]').closest('.preview-cart-items');
                    itemDiv.parentNode.removeChild(itemDiv);
                    updateCartSummary();
                } else {
                    console.error('Failed to remove item from cart');
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
    }

    function updateItemQuantity(productId, quantityChange) {
        fetch('/cart/updateQuantity', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({productId: productId, quantityChange: quantityChange})
        })
            .then(function(response) {
                if (response.ok) {
                    // 수량 업데이트
                    const quantitySpan = document.querySelector('.item-quantity[data-product-id="' + productId + '"]');
                    if (!quantitySpan) {
                        console.error('quantitySpan not found for productId:', productId);
                        return;
                    }
                    let currentQuantity = parseInt(quantitySpan.textContent);
                    let newQuantity = currentQuantity + quantityChange;
                    if (newQuantity <= 0) {
                        // 수량이 0 이하이면 상품 제거
                        const itemDiv = quantitySpan.closest('.preview-cart-items');
                        itemDiv.parentNode.removeChild(itemDiv);
                    } else {
                        quantitySpan.textContent = newQuantity;
                    }
                    updateCartSummary();
                } else {
                    console.error('Failed to update item quantity');
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
    }

    function updateCartSummary() {
        // 총 수량 및 총 금액 업데이트
        fetch('/cart/summary', {
            method: 'GET'
        })
            .then(function(response) {
                return response.json();
            })
            .then(function(data) {
                // 장바구니 요약 정보 업데이트
                const totalQuantityElement = document.getElementById('total-quantity-result');
                const totalPriceElement = document.getElementById('total-price-result');

                if (totalQuantityElement) {
                    totalQuantityElement.textContent = data.totalQuantity;
                } else {
                    console.warn('Total quantity element not found');
                }

                if (totalPriceElement) {
                    totalPriceElement.textContent = data.totalPrice + '원';
                } else {
                    console.warn('Total price element not found');
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
    }
});
