package lld.onlineFoodOrdering;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

// Event classes and interfaces
class RatingEvent {
    private String orderId;
    private String restaurantId;
    private String foodItemId;
    private int rating;
    
    public RatingEvent(String orderId, String restaurantId, String foodItemId, int rating) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.foodItemId = foodItemId;
        this.rating = rating;
    }
    public String getOrderId() { return orderId; }
    public String getRestaurantId() { return restaurantId; }
    public String getFoodItemId() { return foodItemId; }
    public int getRating() { return rating; }
}

interface OrderRatingListener {
    void onOrderRated(RatingEvent event);
}

interface RestaurantRatingListener {
    void onRestaurantRatingUpdated(RatingEvent event, double newRating);
}

public class Solution implements Q05RestaurantRatingInterface {
    RestaurantManager restaurantManager;
    OrderManager orderManager;
    RatingService ratingService;
    
    @Override
    public void init() {
        restaurantManager = new RestaurantManager();
        orderManager = new OrderManager();
        ratingService = new RatingService();
        // Register listeners
        ratingService.subscribeOrderRatingListener(orderManager);
        ratingService.subscribeRestaurantRatingListener(restaurantManager);
    }
    
    @Override
    public void orderFood(String orderId, String restaurantId, String foodItemId) {
        orderManager.orderFood(orderId, restaurantId, foodItemId);
    }

    @Override
    public void rateOrder(String orderId, int rating) {
        ratingService.rateOrder(orderId, rating, orderManager.getOrder(orderId));
    }

    @Override
    public List<String> getTopRestaurantsByFood(String foodItemId) {
        return restaurantManager.getTopRestaurantsByFood(foodItemId);
    }

    @Override
    public List<String> getTopRatedRestaurants() {
        return restaurantManager.getTopRatedRestaurants();
    }
}

class RestaurantManager implements RestaurantRatingListener {
    Map<String, Restaurant> restaurants;
    public RestaurantManager() {
        this.restaurants = new HashMap<>();
    }
    @Override
    public void onRestaurantRatingUpdated(RatingEvent event, double newRating) {
        Restaurant restaurant = restaurants.get(event.getRestaurantId());
        if (restaurant != null) {
            if (restaurant.rating == null) {
                restaurant.rating = new Rating();
            }
            restaurant.rating.stars = (int) Math.round(newRating);
        }
    }
    public void addRestaurant(String id, Restaurant restaurant) {
        restaurants.put(id, restaurant);
    }
    
    public List<String> getTopRestaurantsByFood(String foodItemId) {
        return restaurants.values().stream()
            .filter(restaurant -> restaurant.foodsItems != null && 
                                restaurant.foodsItems.containsKey(foodItemId))
            .sorted(Comparator.comparing((Restaurant r) -> 
                r.rating != null ? r.rating.stars : 0).reversed())
            .limit(10) // Return top 10 restaurants
            .map(restaurant -> restaurant.id)
            .collect(Collectors.toList());
    }
    
    public List<String> getTopRatedRestaurants() {
        return restaurants.values().stream()
            .filter(restaurant -> restaurant.rating != null && restaurant.rating.stars > 0)
            .sorted(Comparator.comparing((Restaurant r) -> r.rating.stars).reversed())
            .limit(10) // Return top 10 restaurants
            .map(restaurant -> restaurant.id)
            .collect(Collectors.toList());
    }
}

class OrderManager implements OrderRatingListener {
    Map<String, Order> orders = new HashMap<>();
    @Override
    public void onOrderRated(RatingEvent event) {
        Order order = orders.get(event.getOrderId());
        if (order != null) {
            // Optionally store the rating in the order or elsewhere
        }
    }
    public void orderFood(String orderId, String restaurantId, String foodItemId) {
        Order order = new Order();
        order.orderId = orderId;
        order.restaurantId = restaurantId;
        order.foodItemId = foodItemId;
        orders.put(orderId, order);
    }
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }
}

class RatingService {
    private List<OrderRatingListener> orderListeners = new ArrayList<>();
    private List<RestaurantRatingListener> restaurantListeners = new ArrayList<>();
    private Map<String, List<Integer>> restaurantRatings = new HashMap<>();
    public void subscribeOrderRatingListener(OrderRatingListener listener) {
        orderListeners.add(listener);
    }
    public void subscribeRestaurantRatingListener(RestaurantRatingListener listener) {
        restaurantListeners.add(listener);
    }
    public void rateOrder(String orderId, int rating, Order order) {
        if (order == null) return;
        RatingEvent event = new RatingEvent(orderId, order.restaurantId, order.foodItemId, rating);
        // Notify order listeners
        for (OrderRatingListener listener : orderListeners) {
            listener.onOrderRated(event);
        }
        // Update restaurant ratings
        List<Integer> ratings = restaurantRatings.computeIfAbsent(order.restaurantId, k -> new ArrayList<>());
        ratings.add(rating);
        double averageRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        // Notify restaurant listeners
        for (RestaurantRatingListener listener : restaurantListeners) {
            listener.onRestaurantRatingUpdated(event, averageRating);
        }
    }
}

class Restaurant {
    String id;
    Map<String, Food> foodsItems;
    Rating rating;
    
    public Restaurant() {
        this.foodsItems = new HashMap<>();
    }
}

class Order {
    String orderId;
    String restaurantId;
    String foodItemId;
}

class Food {
    String name;
    double price;
    Rating rating;
}

class Rating {
    int stars;
}
