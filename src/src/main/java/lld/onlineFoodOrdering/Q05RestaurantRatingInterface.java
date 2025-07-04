package lld.onlineFoodOrdering;

import java.util.List;

public interface Q05RestaurantRatingInterface {
    public void init();
    
    public void orderFood(String orderId, String restaurantId, String foodItemId);
    
    public void rateOrder(String orderId, int rating);

    public List<String> getTopRestaurantsByFood(String foodItemId);

    public List<String> getTopRatedRestaurants();
}
