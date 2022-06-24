# Stock-Exchange

A simple stock exchange system that accepts and matches orders built using Java with **Singleton design pattern**

## Usage Overview
User can write commands to simulate submitting orders, viewing orders, and quoting stocks

## Functionalities
Simply use the command template provided to use the functions of the stock exchange.

- **Submit buy limit order** -> `BUY {STOCK_NAME} LMT ${PRICE} {QUANTITY}`

- **Submit buy market order** -> `BUY {STOCK_NAME} MKT {QUANTITY}`

- **Submit sell limit order** -> `SELL {STOCK_NAME} LMT ${PRICE} {QUANTITY}`

- **Submit sell market order** -> `SELL {STOCK_NAME} MKT {QUANTITY}`

- **View submitted orders and their status** -> `VIEW ORDERS`

- **Quote orders** -> `QUOTE {STOCK_NAME}`

Order matching is executed automatically upon submitting.

## How to run

1. Clone the repository on local computer
2. Open the repository on an IDE and navigate to the repository folder on IDE terminal
3. Compile the code : `javac Main.java`
4. Run the code : `java Main` 
5. Commands are written on test.txt file and can be modified as required


