import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

public class Main {

    static final StockExchange stockExchange = StockExchange.getStockExchange();
    static final DecimalFormat df = new DecimalFormat("0.00");
    static final String first = "Invalid command";

    public static void main(String[] args){
        
        try {
            File file = new File("test.txt");
            FileReader fr = new FileReader(file); 
            BufferedReader br = new BufferedReader(fr); 
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("QUIT")){
                    break;
                }
                System.out.println("Action: "+line);
                String[] command = line.split(" ");

                // Handle the command using the first word
                if (command[0].equals("BUY") || command[0].equals("SELL")){
                    orderHandler(command);
                } else if(command[0].equals("VIEW")){
                    viewHandler(command);
                }  else if(command[0].equals("QUOTE")){
                    quoteHandler(command);
                } else{
                    throw new IllegalArgumentException(first);
                }
                System.out.println();
            }
            // closes the stream
            fr.close(); 
        } catch (NumberFormatException nfe){
            System.out.println("The price or quantity specified is not valid");
        } catch (IllegalArgumentException iae){
            System.out.println(iae.getMessage());
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println();
    }

    //Invoke this method when the first word is BUY or SELL
    public static void orderHandler(String[] command){
        Side orderSide = command[0].equals("BUY") ? Side.BUY : Side.SELL;
        String orderName = command[1];

        //Invalid order type
        if(!command[2].equals("LMT") && !command[2].equals("MKT")){
            throw new IllegalArgumentException("Invalid order type");
        }

        Type orderType = command[2].equals("LMT") ? Type.LMT : Type.MKT;
        double orderPrice = 0.0;
        int orderQuantity = 0;
        Order order;

        //Invalid command if limit order length is not 5 or market order length is not 4
        if((orderType == Type.LMT && (command.length < 5 || command.length > 5)) || (orderType == Type.MKT && (command.length < 4 || command.length > 4))){
            throw new IllegalArgumentException(first);
        }
        
        //Instantiate the correct type of order
        if(orderType == Type.LMT){
            orderPrice = Double.parseDouble(command[3].replaceAll("[^0-9.\s]", ""));
            orderQuantity = Integer.parseInt(command[4]);
            order = new LimitOrder(orderName, orderPrice, orderQuantity, orderSide, orderType);
        } else{
            orderQuantity = Integer.parseInt(command[3]);
            order = new MarketOrder(orderName, orderPrice, orderQuantity, orderSide, orderType);
        }

        //Invoke the appropriate method based on the side
        if(orderSide == Side.BUY){
            order.buy(orderName, orderPrice, orderQuantity);
        } else {
            order.sell(orderName, orderPrice, orderQuantity);
        }
        
        System.out.println(order.toString());
    }

    //Invoke this method when the command is VIEW ORDERS
    public static void viewHandler(String[] command){
        //Invalid command
        if(command.length < 2 || !command[1].equals("ORDERS") || command.length > 2){
            throw new IllegalArgumentException(first);
        }

        //If there are no orders submitted
        if(stockExchange.getOrderRecords().isEmpty()){
            System.out.println("There are no orders");
            return;
        }

        int index = 1;
        for(Order order : stockExchange.getOrderRecords()){
            int orderFilled = 0;
            if(order.getSide() == Side.BUY){
                orderFilled = stockExchange.getBuyOrders().get(order);
            } else{
                orderFilled = stockExchange.getSellOrders().get(order);
            }

            //For market orders that are not filled at all
            if(order.getPrice() == 0.0){
                System.out.println(index+". "+order.getName()+" "+order.getType()+" "+order.getSide()+" "+orderFilled+"/"+order.getQuantity()+" "+order.getStatus());
            } else{
                System.out.println(index+". "+order.getName()+" "+order.getType()+" "+order.getSide()+" $"+df.format(order.getPrice())+" "+orderFilled+"/"+order.getQuantity()+" "+order.getStatus());
            }
            index++;
        }
    }

    //Invoke this method when the first word is QUOTE
    public static void quoteHandler(String[] command){
        //Invalid command
        if(command.length < 2 || command.length > 2){
            throw new IllegalArgumentException(first);
        }

        String orderName = command[1];
        double[] arr = stockExchange.getQuote(orderName);

        //If there are no specified orders within the orderRecords
        boolean exist = false;
        for(int i = 0 ; i < arr.length ; i++){
            if(arr[i] != 0.0){
                exist = true;
                break;
            }
        }
        if(!exist){
            System.out.println("Data is insufficient");
            return;
        }

        System.out.println(orderName+" BID: $"+df.format(arr[0])+" ASK: $"+df.format(arr[1])+" LAST: $"+df.format(arr[2]));
    }
}
