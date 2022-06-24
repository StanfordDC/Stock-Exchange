import java.util.*;
import java.text.DecimalFormat;

public class LimitOrder extends Order{
    
    static final StockExchange stockExchange = StockExchange.getStockExchange();
    static final HashMap<Order, Integer> sellRecords = stockExchange.getSellOrders();
    static final HashMap<Order, Integer> buyRecords = stockExchange.getBuyOrders();
    static final ArrayList<Order> orderRecords = stockExchange.getOrderRecords();

    public LimitOrder(String name, double price, int quantity, Side side, Type type){
        super(name, price, quantity, side, type);
    }

    @Override
    public void buy(String name, double price, int quantity){
        LimitOrder newOrder = new LimitOrder(name, price, quantity, Side.BUY, Type.LMT);

        //Add the order to orderRecords
        orderRecords.add(newOrder);
        //Put order in buy records and set the filled orders to 0
        buyRecords.put(newOrder, 0);

        //Iterate the orders in sell records
        for(Map.Entry<Order, Integer> entry : sellRecords.entrySet()){
            Order currentOrder = entry.getKey();
            String orderName = currentOrder.getName();
            double sellPrice = currentOrder.getPrice();
            Type orderType = currentOrder.getType();

            //Check if the name of buy order matches the current iterated order name
            if(!name.equals(orderName)){
                continue;
            }

            //Check if the sell price is below the limit of buy price or a sell market order
            if(sellPrice <= price || orderType == Type.MKT){
                if(fillOrder(newOrder, currentOrder)){
                    //If matches with a market order, the price of market order is updated if it is lower
                    if(orderType == Type.MKT){
                        price = Math.max(price, sellPrice);
                        currentOrder.setPrice(price);
                    }
                }
            }
        }
    }

    @Override
    public void sell(String name, double price, int quantity){
        LimitOrder newOrder = new LimitOrder(name, price, quantity, Side.SELL, Type.LMT);

        //Add the order to orderRecords
        orderRecords.add(newOrder);
         //Put order in sell records and set the filled to 0
         sellRecords.put(newOrder, 0);

         //Iterate the orders in buy records
        for(Map.Entry<Order, Integer> entry : buyRecords.entrySet()){
            Order currentOrder = entry.getKey();
            String orderName = currentOrder.getName();
            double buyPrice = currentOrder.getPrice();
            Type orderType = currentOrder.getType();

            //Check if the name of sell order matches the current iterated order name
            if(!name.equals(orderName)){
                continue;
            }

            //Check if the buy price is at least the sell price or a buy market order
            if(buyPrice >= price || orderType == Type.MKT){
                if(fillOrder(newOrder, currentOrder)){
                    //If matches with a market order, the price of market order is updated if it is lower
                    if(orderType == Type.MKT){
                        price = Math.max(price, buyPrice);
                        currentOrder.setPrice(price);
                    }
                }
            }
        }
    }

    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.00");
        return "You have placed a limit "+this.getSide().toLower()+" order for "+this.getQuantity()+" "+this.getName()+" shares at $"+df.format(this.getPrice())+" each";
    }

}
