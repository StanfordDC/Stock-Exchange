import java.util.*;

public class MarketOrder extends Order{

    static final StockExchange stockExchange = StockExchange.getStockExchange();
    static final HashMap<Order, Integer> sellRecords = stockExchange.getSellOrders();
    static final HashMap<Order, Integer> buyRecords = stockExchange.getBuyOrders();
    static final ArrayList<Order> orderRecords = stockExchange.getOrderRecords();

    public MarketOrder(String name, double price, int quantity, Side side, Type type){
        super(name, price, quantity, side, type);
    }

    @Override
    public void buy(String name, double price, int quantity){
        MarketOrder newOrder = new MarketOrder(name, price, quantity, Side.BUY, Type.MKT);

        double newPrice = 0.0;
        //Add the order to orderRecords
        orderRecords.add(newOrder);
        //Put order in buy records and set the filled to 0
        buyRecords.put(newOrder, 0);

        //Iterate the orders in sell records
        for(Map.Entry<Order, Integer> entry : sellRecords.entrySet()){
            Order currentOrder = entry.getKey();
            String orderName = currentOrder.getName();
            double sellPrice = currentOrder.getPrice();

            //Avoid matching the order with another market order
            if(currentOrder.getType() == Type.MKT){
                continue;
            }

            //Check if the name of buy order matches the current iterated order name
            if(!name.equals(orderName)){
                continue;
            }

            if(fillOrder(newOrder, currentOrder)){
                 //Price of the order is updated to the highest transacted price
                newPrice = Math.max(newPrice, sellPrice);
                newOrder.setPrice(newPrice);
            }
        }
    }

    @Override
    public void sell(String name, double price, int quantity){
        MarketOrder newOrder = new MarketOrder(name, price, quantity, Side.SELL, Type.MKT);

        double newPrice = 0.0;
        //Add the order to orderRecords
        orderRecords.add(newOrder);
         //Put order in sell records and set the filled to 0
         sellRecords.put(newOrder, 0);

         //Iterate the orders in buy records
        for(Map.Entry<Order, Integer> entry : buyRecords.entrySet()){
            Order currentOrder = entry.getKey();
            String orderName = currentOrder.getName();
            double buyPrice = currentOrder.getPrice();

            //Avoid matching the order with another market order
            if(currentOrder.getType() == Type.MKT){
                continue;
            }

            //Check if the name of sell order matches the current iterated order name
            if(!name.equals(orderName)){
                continue;
            }

            if(fillOrder(newOrder, currentOrder)){
                //Price of the order is updated to the highest transacted price
                newPrice = Math.max(newPrice, buyPrice);
                newOrder.setPrice(newPrice);
            }
        }
    }

    @Override
    public String toString(){
        return "You have placed a market "+this.getSide().toLower()+" order for "+this.getQuantity()+" "+this.getName()+" shares";
    }

}
